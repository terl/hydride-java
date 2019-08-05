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

public class SecretBoxTest extends BaseTest {

    private String context = "context1";
    private byte[] contextBytes = context.getBytes();

    private String message = "This is a message that will be encrypted.";
    private byte[] messageBytes = message.getBytes();

    @BeforeAll
    public void contextIsTheRightLength() {
        assertEquals(contextBytes.length, Hydrogen.HYDRO_SECRETBOX_CONTEXTBYTES);
    }

    @Test
    public void encrypt() {
        // Generate key
        byte[] key = new byte[Hydrogen.HYDRO_SECRETBOX_KEYBYTES];
        hydrogen.hydro_secretbox_keygen(key);
        assertTrue(encryptFromServerToClient(message, contextBytes, key, key));
    }

    @Test
    public void encryptWithProbe() {
        // Generate key
        byte[] key = new byte[Hydrogen.HYDRO_SECRETBOX_KEYBYTES];
        hydrogen.hydro_secretbox_keygen(key);

        // Make cipherText array
        byte[] probe = new byte[Hydrogen.HYDRO_SECRETBOX_PROBEBYTES];
        byte[] cipher = new byte[Hydrogen.HYDRO_SECRETBOX_HEADERBYTES + messageBytes.length];
        byte[] decrypted = new byte[messageBytes.length];
        final long messageId = 0L;

        // Encrypt first
        int encryptSuccess = hydrogen.hydro_secretbox_encrypt(cipher, messageBytes, messageBytes.length, messageId, contextBytes, key);
        // Did the encryption work?
        assertEquals(0, encryptSuccess);

        // Now create a probe
        hydrogen.hydro_secretbox_probe_create(probe, cipher, cipher.length, contextBytes, key);

        // When we receive the cipherText and the probe on the server
        // we now verify if that probe corresponds to our key
        int probeVerifySuccess = hydrogen.hydro_secretbox_probe_verify(probe, cipher, cipher.length, contextBytes, key);
        // Did the probe verification work?
        assertEquals(0, probeVerifySuccess);

        // Now after probe verification, we can continue on
        // with decryption thereby mitigating a large cipherText
        // attack
        int decryptSuccess = hydrogen.hydro_secretbox_decrypt(decrypted, cipher, cipher.length, messageId, contextBytes, key);
        // Did the decryption work?
        assertEquals(0, decryptSuccess);

        // Are the message and the decrypted message the same?
        assertTrue(arraysEqual(messageBytes, decrypted));
    }


}
