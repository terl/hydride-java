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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashTest extends BaseTest {

    String context = "acontext";
    String message = "A message";

    byte[] contextBytes = context.getBytes();
    byte[] messageBytes = message.getBytes();

    @BeforeAll
    public void contextIsTheRightLength() {
        assertEquals(contextBytes.length, Hydrogen2.HYDRO_HASH_CONTEXTBYTES);
    }

    @Test
    public void hashWithoutKey() {
        byte[] hash = new byte[Hydrogen2.HYDRO_HASH_BYTES];
        hydrogen.hydro_hash_hash(hash, hash.length, messageBytes, message.length(), contextBytes, null);
        assertTrue(hasAtLeastOneNonZeroNumber(hash));
    }

    @Test
    public void hashWithKey() {
        byte[] key = new byte[Hydrogen2.HYDRO_HASH_KEYBYTES];

        byte[] hash = new byte[Hydrogen2.HYDRO_HASH_BYTES];
        hydrogen.hydro_hash_hash(hash, hash.length, messageBytes, message.length(), contextBytes, key);

        byte[] hash2 = new byte[Hydrogen2.HYDRO_HASH_BYTES];
        hydrogen.hydro_hash_hash(hash2, hash2.length, messageBytes, message.length(), contextBytes, key);

        assertTrue(hasAtLeastOneNonZeroNumber(hash));
        assertTrue(arraysEqual(hash, hash2));
    }

    @Test
    public void multiPartHash() {
        String message2 = "A message 2";
        byte[] message2Bytes = message2.getBytes();

        byte[] key = new byte[Hydrogen2.HYDRO_HASH_KEYBYTES];
        hydrogen.hydro_hash_keygen(key);

        // Multi-hash message and message2 together and store in hash
        byte[] hash = new byte[Hydrogen2.HYDRO_HASH_BYTES];
        Hydrogen2.HydroHashState state = new Hydrogen2.HydroHashState.ByReference();
        hydrogen.hydro_hash_init(state, contextBytes, key);
        hydrogen.hydro_hash_update(state, messageBytes, messageBytes.length);
        hydrogen.hydro_hash_update(state, message2Bytes, message2Bytes.length);
        hydrogen.hydro_hash_final(state, hash, hash.length);

        // Multi-hash message and message2 together and store in hash2 using
        // the same key we used for hash.
        byte[] hash2 = new byte[Hydrogen2.HYDRO_HASH_BYTES];
        Hydrogen2.HydroHashState state2 = new Hydrogen2.HydroHashState.ByReference();
        hydrogen.hydro_hash_init(state2, contextBytes, key);
        hydrogen.hydro_hash_update(state2, messageBytes, messageBytes.length);
        hydrogen.hydro_hash_update(state2, message2Bytes, message2Bytes.length);
        hydrogen.hydro_hash_final(state2, hash2, hash2.length);

        // Hash and hash2 should be equal as we used the same key
        // and hashed the same messages.
        assertTrue(hasAtLeastOneNonZeroNumber(hash));
        assertTrue(arraysEqual(hash, hash2));
    }



}
