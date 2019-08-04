/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;


import com.sun.jna.NativeLong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyExchangeXXTest extends BaseTest {

    private String context = "context1";
    private byte[] contextBytes = context.getBytes();

    private String message = "This is a message that will be encrypted.";
    private byte[] messageBytes = message.getBytes();

    @Test
    public void keyExchange() {
        // Generate server and client long-term keypairs
        Hydrogen.HydroKxKeyPair serverKeyPair = new Hydrogen.HydroKxKeyPair.ByReference();
        Hydrogen.HydroKxKeyPair clientKeyPair = new Hydrogen.HydroKxKeyPair.ByReference();
        hydrogen.hydro_kx_keygen(serverKeyPair);
        hydrogen.hydro_kx_keygen(clientKeyPair);

        // Client: Initiate a key exchange
        byte[] packet1 = new byte[Hydrogen.HYDRO_KX_XX_PACKET1BYTES];
        Hydrogen.HydroKxState stateClient = new Hydrogen.HydroKxState.ByReference();
        hydrogen.hydro_kx_xx_1(stateClient, packet1, null);

        // Server: process the initial request from the client, and compute the session keys
        byte[] packet2 = new byte[Hydrogen.HYDRO_KX_XX_PACKET2BYTES];
        Hydrogen.HydroKxState stateServer = new Hydrogen.HydroKxState.ByReference();
        Hydrogen.HydroKxSessionKeyPair serverSession = new Hydrogen.HydroKxSessionKeyPair.ByReference();
        hydrogen.hydro_kx_xx_2(stateServer, packet2, packet1, null, serverKeyPair);

        // Client: process the server packet and compute the session keys
        byte[] packet3 = new byte[Hydrogen.HYDRO_KX_XX_PACKET3BYTES];
        Hydrogen.HydroKxSessionKeyPair clientSession = new Hydrogen.HydroKxSessionKeyPair.ByReference();
        hydrogen.hydro_kx_xx_3(stateClient, clientSession, packet3, null, packet2, null, clientKeyPair);

        // Server: process the client packet and compute the session keys:
        hydrogen.hydro_kx_xx_4(stateServer, serverSession, null, packet3, null);


        // Encrypt
        NativeLong messageId = new NativeLong(1);
        byte[] cipher = new byte[Hydrogen.HYDRO_SECRETBOX_HEADERBYTES + messageBytes.length];
        int encryptSuccess = hydrogen.hydro_secretbox_encrypt(cipher, messageBytes, messageBytes.length, messageId, contextBytes, serverSession.getTx());
        assertEquals(0, encryptSuccess);

        // Now let's decrypt that message on the client,
        // take note of the client session keypair
        byte[] decrypted = new byte[messageBytes.length];
        int decryptSuccess = hydrogen.hydro_secretbox_decrypt(decrypted, cipher, cipher.length, messageId, contextBytes, clientSession.getRx());
        assertEquals(0, decryptSuccess);
        assertEquals(message, new String(decrypted));
    }


}
