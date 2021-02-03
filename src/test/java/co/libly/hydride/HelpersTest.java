/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;


import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class HelpersTest extends BaseTest {

    @Test
    public void zeroArray() {
        byte[] array = new byte[] {4, 3, 6, 7, 9};
        hydrogen.hydro_memzero(array, array.length);
        assertFalse(hasAtLeastOneNonZeroNumber(array));
    }

    @Test
    public void arraysAreEqual() {
        byte[] array1 = new byte[] {4, 3, 6, 7, 9};
        byte[] array2 = new byte[] {4, 3, 6, 7, 9};

        // Test from the Java side that they are equal
        boolean equal = true;
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                equal = false;
                break;
            }
        }

        // If Java and Libhydrogen both concur that
        // they are equal then we know that Libhydrogen's
        // hydro_equal function works.
        assertTrue(equal && hydrogen.hydro_equal(array1, array2, array1.length));
    }

    @Test
    public void hexadecimalTest() {
        String text = "Convert this";
        byte[] textBytes = text.getBytes();
        byte[] hex = new byte[textBytes.length * 2 + 1];

        int success = hydrogen.hydro_bin2hex(hex, hex.length, textBytes, textBytes.length);
        String libHydrogenHexed = new String(hex);
        // Is the hexadecimal output from libhydrogen the same
        // as ours?
        // We need to add a null byte at the end because libydrogen adds a null
        // byte when encoding to hexadecimal
        assertEquals(libHydrogenHexed, stringToHex(text).concat("\0"));

        // Now convert back from hexadecimal. Do we get the back the value we started with?
        byte[] backToString = new byte[textBytes.length];
        hydrogen.hydro_hex2bin(backToString, backToString.length, hex, hex.length, null, (byte) 0);
        String backTo = new String(backToString);
        assertEquals(backTo, text);
    }

    private String stringToHex(String s) {
        return new BigInteger(1, s.getBytes()).toString(16);
    }

}
