/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KdfTest extends BaseTest {

    private String context = "acontext";
    private byte[] contextBytes = context.getBytes();

    @BeforeAll
    public void contextIsTheRightLength() {
        assertEquals(contextBytes.length, Hydrogen2.HYDRO_KDF_CONTEXTBYTES);
    }

    @Test
    public void deriveKeys() {
        // Generate master key
        byte[] masterKey = new byte[Hydrogen2.HYDRO_KDF_KEYBYTES];
        hydrogen.hydro_kdf_keygen(masterKey);

        // Generate subkeys
        byte[] subKey = new byte[Hydrogen2.HYDRO_KDF_BYTES_MIN];
        byte[] subKey2 = new byte[Hydrogen2.HYDRO_KDF_BYTES_MIN];
        byte[] subKey3 = new byte[Hydrogen2.HYDRO_KDF_BYTES_MIN];

        hydrogen.hydro_kdf_derive_from_key(subKey, subKey.length, 1L, contextBytes, masterKey);
        hydrogen.hydro_kdf_derive_from_key(subKey2, subKey2.length, 1L, contextBytes, masterKey);

        // Subkey 1 and 3 should be the same
        assertTrue(hasAtLeastOneNonZeroNumber(subKey));
        assertTrue(arraysEqual(subKey, subKey2));

        // Now generate another subkey with a different subKeyId
        hydrogen.hydro_kdf_derive_from_key(subKey3, subKey3.length, 2L, contextBytes, masterKey);

        // Subkey 1 and 3 should not be the same as the subKeyId parameter is different
        assertFalse(arraysEqual(subKey, subKey3));
    }


}
