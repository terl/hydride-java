/*
 * Copyright (c) Libly - Terl Tech Ltd  • 31/01/2021, 21:01 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.libly.generator

import com.github.javaparser.StaticJavaParser.parse
import com.github.javaparser.StaticJavaParser.parseClassOrInterfaceType
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.InitializerDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter
import com.github.javaparser.utils.SourceRoot
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


// 1. Run JNA script jnaerate.sh, it basically runs:
// java -jar ../jnaerator.jar -library hydrogen hydrogen.h -o hydrogen -v -mode Directory -runtime JNA -skipDeprecated -forceStringSignatures -dontCastConstants -direct -noPrimitiveArrays -limitComments -noComments -noStaticInit -package co.libly.hydride -f

// 2. Then run "Fix classes"
class Generator(val absolutePath: String) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val extractionDir = args[0]
            println("Extracting JNA files to: $extractionDir")
            Generator(extractionDir)
                    .manipulateHydrogenLibrary()
                    .camelCaseAllClasses()
        }
    }

    private fun camelCaseAllClasses() {
        val folder = File(absolutePath)
        val namesToCamelCase = mutableListOf<String>()
        if (folder.isDirectory) {
            folder.walk().forEach { file ->
                if (file.isFile) {
                    val nameOfFile = file.name
                    if (nameOfFile.contains("_")) {
                        namesToCamelCase.add(file.nameWithoutExtension)
                        Files.move(file.toPath(), file.toPath().resolveSibling(nameOfFile.toCamelCase()), StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }
        }
        val sourceRoot = SourceRoot(folder.toPath())
        sourceRoot.tryToParse().forEach { res ->
            res.ifSuccessful { unit ->
                unit.findAll(SimpleName::class.java)
                        .filter { s -> namesToCamelCase.contains(s.asString()) }
                        .forEach { s ->
                            s.id = s.camelCase()
                        }
                var didContainGetFieldOrder = false
                unit.findAll(MethodDeclaration::class.java)
                    .filter { method -> method.typeAsString.equals("List<?>", true)
                                            && method.nameAsString == "getFieldOrder" }
                    .forEach { method ->
                        method.type = parseClassOrInterfaceType("List<String>")
                        didContainGetFieldOrder = true
                    }
                // Add String import if we modified the thing above
                if (didContainGetFieldOrder) {
                    unit.imports.add(ImportDeclaration("java.lang.String", false, false))
                }

                val f = File(absolutePath, unit.storage.get().fileName)
                val newText = LexicalPreservingPrinter.print(unit)
                f.writeText(newText)
            }
        }

        println(namesToCamelCase)
    }

    private fun manipulateHydrogenLibrary(): Generator {
        val file = File(absolutePath, "HydrogenLibrary.java")
        if (!file.exists()) {
            println("HydrogenLibrary.java not found. Skipping.")
            return this
        }
        val parsed = parse(file)

        // Rename its class name
        println("Changing all HydrogenLibrary's to Hydrogen")
        parsed.findAll(SimpleName::class.java)
                .filter { simpleName -> simpleName.asString() == "HydrogenLibrary" }
                .forEach { s ->
                    s.id = "Hydrogen"
                }

        // Make all fields uppercase
        println("Ensure all fields are uppercase")
        parsed.findAll(FieldDeclaration::class.java)
                .forEach { field ->
                    if (field.isStatic && field.isFinal) {
                        field.variables.forEach { variable ->
                            variable.name = variable.name.asString().toUpperCase().toSimpleName()
                        }
                    }
                }

        // Remove redundant static block
        println("Delete static { } blocks")
        parsed.findAll(InitializerDeclaration::class.java)
                .forEach { init ->
                    if (init.isStatic) {
                        init.remove()
                    }
                }
        file.writeText(LexicalPreservingPrinter.print(parsed))
        Files.move(file.toPath(), file.toPath().resolveSibling("Hydrogen.java"), StandardCopyOption.REPLACE_EXISTING)
        return this
    }
}

fun SimpleName.camelCase() = asString().toCamelCase()

fun String.toSimpleName() = SimpleName(this)

fun String.toCamelCase() =
        split('_').joinToString("", transform = String::capitalize)