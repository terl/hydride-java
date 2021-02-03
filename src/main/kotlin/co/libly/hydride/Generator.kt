/*
 * Copyright (c) Libly - Terl Tech Ltd  • 31/01/2021, 21:01 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.libly.hydride

import com.github.javaparser.StaticJavaParser.parse
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.InitializerDeclaration
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter
import com.github.javaparser.utils.SourceRoot
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

// java -jar ../jnaerator.jar -library hydrogen hydrogen.h -o hydrogen -v -mode Directory -runtime JNA -skipDeprecated -forceStringSignatures -dontCastConstants -direct -noPrimitiveArrays -limitComments -noComments -noStaticInit -package co.libly.hydride -f
class Generator {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Generator()
                    .manipulateHydrogenLibrary("/Users/gp/Documents/terl/libly/hydride/src/main/java/co/libly/hydride/HydrogenLibrary.java")
                    .camelCaseAllClasses("/Users/gp/Documents/terl/libly/hydride/src/main/java/co/libly/hydride/")
        }
    }

    private fun camelCaseAllClasses(absolutePathToFolder: String) {
        val folder = File(absolutePathToFolder)
        val namesToCamelCase = mutableListOf<String>()
        if (folder.isDirectory) {
            folder.walk().forEach { file ->
                if (file.isFile) {
                    val nameOfFile = file.name
                    if (nameOfFile.contains("_")) {
                        namesToCamelCase.add(nameOfFile)
                        Files.copy(file.toPath(), file.toPath().resolveSibling(nameOfFile.toCamelCase()), StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }
        }
        val sourceRoot = SourceRoot(folder.toPath())
        sourceRoot.tryToParse().forEach { res ->
            res.ifSuccessful { unit ->
                unit.findAll(SimpleName::class.java)
                        .filterNot { s -> namesToCamelCase.contains(s.asString()) }
                        .forEach { s ->
                            s.identifier = s.camelCase()
                        }
                val f = File(absolutePathToFolder, unit.storage.get().fileName)
                val newText = LexicalPreservingPrinter.print(unit)

                val writer = Files.newBufferedWriter(f.toPath())
                writer.write("")
                writer.write(newText)
                writer.flush()
            }
        }

        println(namesToCamelCase)
    }

    private fun manipulateHydrogenLibrary(absolutePath: String): Generator {
        val file = File(absolutePath)
        val parsed = parse(file)

        // Rename its class name
        parsed.findAll(ClassOrInterfaceDeclaration::class.java)
                .forEach { clzz ->
                    if (clzz.name.asString() == "HydrogenLibrary") {
                        clzz.name = SimpleName("Hydrogen")
                    }
                }

        // Make all fields uppercase
        parsed.findAll(FieldDeclaration::class.java)
                .forEach { field ->
                    if (field.isStatic && field.isFinal) {
                        field.variables.forEach { variable ->
                            variable.name = variable.name.asString().toUpperCase().toSimpleName()
                        }
                    }
                }

        // Remove redundant static block
        parsed.findAll(InitializerDeclaration::class.java)
                .forEach { init ->
                    if (init.isStatic) {
                        init.remove()
                    }
                }
        file.writeText(LexicalPreservingPrinter.print(parsed))
        Files.move(file.toPath(), file.toPath().resolveSibling(parsed.storage.get().fileName), StandardCopyOption.REPLACE_EXISTING)
        return this
    }
}

fun SimpleName.camelCase() = asString().toCamelCase()

fun String.toSimpleName() = SimpleName(this)

fun String.toCamelCase() =
        split('_').joinToString("", transform = String::capitalize)