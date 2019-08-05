/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    public Hydrogen hydrogen;

    @BeforeAll
    public void doBeforeEverything() {
        hydrogen = new Hydrogen();
    }


    public byte[] getSeed() {
        return new byte[] {
                2, 4, 6, 8, 10, 12, 14, 16,
                18, 93, 9, 11, 19, 12, 14, 3,
                30, 4, 11, 8, 10, 15, 22, 14,
                20, 1, 3, 10, 32, 29, 5, 4
        };
    }

    public boolean hasAtLeastOneNonZeroNumber(byte[] buffer) {
        boolean hasAtLeastOneNonZeroNumber = false;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0) {
                hasAtLeastOneNonZeroNumber = true;
                break;
            }
        }
        return hasAtLeastOneNonZeroNumber;
    }

    public boolean arraysEqual(byte[] buffer, byte[] buffer2) {
        return hydrogen.hydro_equal(buffer, buffer2, buffer.length);
    }

    public boolean encryptFromServerToClient(String message, byte[] contextBytes, byte[] serverKey, byte[] clientKey) {
        byte[] messageBytes = message.getBytes();
        // Now let's send from the server to the client,
        // take note of the server session keypair
        long messageId = 1L;
        byte[] cipher = new byte[Hydrogen.HYDRO_SECRETBOX_HEADERBYTES + messageBytes.length];
        int encryptSuccess = hydrogen.hydro_secretbox_encrypt(cipher, messageBytes, messageBytes.length, messageId, contextBytes, serverKey);
        assertEquals(0, encryptSuccess);

        // Now let's decrypt that message on the client,
        // take note of the client session keypair
        byte[] decrypted = new byte[messageBytes.length];
        int decryptSuccess = hydrogen.hydro_secretbox_decrypt(decrypted, cipher, cipher.length, messageId, contextBytes, clientKey);
        assertEquals(0, decryptSuccess);
        assertEquals(message, new String(decrypted));

        return true;
    }

}
