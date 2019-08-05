/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;


import com.sun.jna.NativeLong;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PwHashTest extends BaseTest {

    private byte[] masterKey;
    private String context = "context1";
    private byte[] contextBytes = context.getBytes();

    private String password = "22ha£$a@@4ajah!nc";
    private byte[] passwordBytes = password.getBytes();

    // OpsLimit has to be positive
    private NativeLong opsLimit = new NativeLong(2L);
    private byte threads = (byte) 1;
    private int memLimit = 0; // 0 means use the default

    @BeforeAll
    public void contextIsTheRightLength() {
        // The master key should be generated offline
        // and only using the hydro_pwhash_keygen function.
        masterKey = new byte[Hydrogen.HYDRO_PWHASH_MASTERKEYBYTES];
        hydrogen.hydro_pwhash_keygen(masterKey);

        assertEquals(contextBytes.length, Hydrogen.HYDRO_PWHASH_CONTEXTBYTES);
    }


    private boolean passwordHash(byte[] key) {
        // Create a hash from a given password
        byte[] hash = new byte[Hydrogen.HYDRO_PWHASH_STOREDBYTES];
        int hashSuccess = hydrogen.hydro_pwhash_create(hash, passwordBytes, passwordBytes.length, key, opsLimit, memLimit, threads);

        // When we want to verify if that hash can be 'decrypted' using
        // a password, use this function
        int hashVerified = hydrogen.hydro_pwhash_verify(hash, passwordBytes, passwordBytes.length, key, opsLimit, memLimit, threads);
        return hashVerified == 0;
    }

    @Test
    public void passwordHashUsingMasterKey() {
        assertTrue(passwordHash(masterKey));
    }

    @Test
    public void passwordHashUsingDeterministicKey() {
        // If you want to derive a deterministic key
        // from a password, then use hydro_pwhash_deterministic.
        // Useful for things like file passwords.
        byte[] password = "a password".getBytes();
        byte[] keyDeterministic = new byte[Hydrogen.HYDRO_PWHASH_MASTERKEYBYTES];
        hydrogen.hydro_pwhash_deterministic(
                keyDeterministic,
                keyDeterministic.length,
                password,
                password.length,
                contextBytes,
                masterKey,
                opsLimit,
                memLimit,
                threads
        );
        assertTrue(passwordHash(keyDeterministic));
    }

    @Test
    public void passwordHashUsingDerivedKey() {
        // If you want to derive a deterministic key
        // from a password, then use hydro_pwhash_deterministic.
        // Useful for things like file passwords.
        byte[] password = "a password".getBytes();
        byte[] derivedKey = new byte[Hydrogen.HYDRO_PWHASH_MASTERKEYBYTES];

        // Create a key
        byte[] hash = new byte[Hydrogen.HYDRO_PWHASH_STOREDBYTES];
        int hashSuccess = hydrogen.hydro_pwhash_create(hash, passwordBytes, passwordBytes.length, masterKey, opsLimit, memLimit, threads);

        // Derive from the above key
        hydrogen.hydro_pwhash_derive_static_key(
                derivedKey,
                derivedKey.length,
                hash,
                password,
                password.length,
                contextBytes,
                masterKey,
                opsLimit,
                memLimit,
                threads
        );

        hydrogen.hydro_pwhash_derive_static_key(
                derivedKey,
                derivedKey.length,
                hash,
                password,
                password.length,
                contextBytes,
                masterKey,
                opsLimit,
                memLimit,
                threads
        );

        assertTrue(passwordHash(derivedKey));
    }


    @Test
    public void reEncryptUsingNewMasterKey() {
        byte[] newMasterKey = new byte[Hydrogen.HYDRO_PWHASH_MASTERKEYBYTES];
        hydrogen.hydro_pwhash_keygen(newMasterKey);

        byte[] hash = new byte[Hydrogen.HYDRO_PWHASH_STOREDBYTES];
        int hashSuccess = hydrogen.hydro_pwhash_create(hash, passwordBytes, passwordBytes.length, masterKey, opsLimit, memLimit, threads);

        int reEncryptSuccess = hydrogen.hydro_pwhash_reencrypt(hash, masterKey, newMasterKey);
        assertEquals(0, reEncryptSuccess);
    }


    @Test
    public void upgradeParameters() {
        byte[] hash = new byte[Hydrogen.HYDRO_PWHASH_STOREDBYTES];
        int hashSuccess = hydrogen.hydro_pwhash_create(hash, passwordBytes, passwordBytes.length, masterKey, opsLimit, memLimit, threads);
        assertEquals(0, hashSuccess);

        // Ops limit has to be positive
        NativeLong newOps = new NativeLong(4L);
        hydrogen.hydro_pwhash_upgrade(hash, masterKey, newOps, memLimit, threads);
        int hashVerified = hydrogen.hydro_pwhash_verify(hash, passwordBytes, passwordBytes.length, masterKey, newOps, memLimit, threads);

        assertEquals(0, hashVerified);
    }


}
