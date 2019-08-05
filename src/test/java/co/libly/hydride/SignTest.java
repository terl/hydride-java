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

public class SignTest extends BaseTest {

    private String context = "context1";
    private byte[] contextBytes = context.getBytes();

    private String message = "This is a message.";
    private byte[] messageBytes = message.getBytes();

    @BeforeAll
    public void contextIsTheRightLength() {
        assertEquals(contextBytes.length, Hydrogen.HYDRO_SIGN_CONTEXTBYTES);
    }

    @Test
    public void sign() {
        // Generate keyPair
        Hydrogen.HydroSignKeyPair keyPair = new Hydrogen.HydroSignKeyPair();
        hydrogen.hydro_sign_keygen(keyPair);

        byte[] sig = new byte[Hydrogen.HYDRO_SIGN_BYTES];
        hydrogen.hydro_sign_create(sig, messageBytes, messageBytes.length, contextBytes, keyPair.getSecretKey());
        int verified = hydrogen.hydro_sign_verify(sig, messageBytes, messageBytes.length, contextBytes, keyPair.getPublicKey());

        assertEquals(0, verified);
    }

    @Test
    public void signDeterministic() {
        // Generate keyPair
        Hydrogen.HydroSignKeyPair keyPair = new Hydrogen.HydroSignKeyPair();
        Hydrogen.HydroSignKeyPair keyPair2 = new Hydrogen.HydroSignKeyPair();

        byte[] seed = new byte[Hydrogen.HYDRO_SIGN_SEEDBYTES];
        hydrogen.hydro_random_buf(seed, Hydrogen.HYDRO_SIGN_SEEDBYTES);

        hydrogen.hydro_sign_keygen_deterministic(keyPair, seed);
        hydrogen.hydro_sign_keygen_deterministic(keyPair2, seed);

        // Are the secret keys the same?
        assertTrue(arraysEqual(keyPair.getSecretKey(), keyPair2.getSecretKey()));

        byte[] sig = new byte[Hydrogen.HYDRO_SIGN_BYTES];
        hydrogen.hydro_sign_create(sig, messageBytes, messageBytes.length, contextBytes, keyPair.getSecretKey());
        int verified = hydrogen.hydro_sign_verify(sig, messageBytes, messageBytes.length, contextBytes, keyPair.getPublicKey());
        assertEquals(0, verified);
    }


    @Test
    public void signMultiPart() {
        // Generate keyPair
        Hydrogen.HydroSignKeyPair keyPair = new Hydrogen.HydroSignKeyPair();
        hydrogen.hydro_sign_keygen(keyPair);

        byte[] sig = new byte[Hydrogen.HYDRO_SIGN_BYTES];
        byte[] message2 = "another message".getBytes();
        Hydrogen.HydroSignState state = new Hydrogen.HydroSignState();

        // Sign
        hydrogen.hydro_sign_init(state, contextBytes);
        hydrogen.hydro_sign_update(state, messageBytes, messageBytes.length);
        hydrogen.hydro_sign_update(state, message2, message2.length);
        hydrogen.hydro_sign_final_create(state, sig, keyPair.getSecretKey());

        // Now verify
        hydrogen.hydro_sign_init(state, contextBytes);
        hydrogen.hydro_sign_update(state, messageBytes, messageBytes.length);
        hydrogen.hydro_sign_update(state, message2, message2.length);
        int verified = hydrogen.hydro_sign_final_verify(state, sig, keyPair.getPublicKey());
        assertEquals(0, verified);
    }




}
