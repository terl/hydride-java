/*
 * Copyright (c) Libly - Terl Tech Ltd  • 04/08/2019, 22:41 • libly.co, goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package co.libly.hydride;

import co.libly.hydride.utils.LibraryLoader;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Hydrogen {

    public static int HYDRO_SECRETBOX_CONTEXTBYTES = 8;
    public static int HYDRO_SECRETBOX_HEADERBYTES = 36;
    public static int HYDRO_SECRETBOX_KEYBYTES = 32;
    public static int HYDRO_SECRETBOX_PROBEBYTES = 16;


    public Hydrogen() {
        this(LibraryLoader.Mode.PREFER_SYSTEM);
    }

    public Hydrogen(LibraryLoader.Mode loadingMode) {
        LibraryLoader.getInstance().loadLibrary(loadingMode);
        hydro_init();
    }

    public native int hydro_init();

    // * --------
    // RANDOM
    // * --------

    public static int HYDRO_RANDOM_SEEDBYTES = 32;

    private native int hydro_random_u32();

    public int hydro_random_u32_get() {
        int random = hydro_random_u32();
        return Math.abs(random);
    }

    public native int hydro_random_uniform(int upperBound);
    public native void hydro_random_buf(byte[] buffer, int len);
    public native void hydro_random_buf_deterministic(byte[] buffer, int len, byte[] seed);
    public native void hydro_random_ratchet();



    // * --------
    // HASHING
    // * --------

    public static int HYDRO_HASH_BYTES = 32;
    public static int HYDRO_HASH_CONTEXTBYTES = 8;
    public static int HYDRO_HASH_KEYBYTES = 32;
    public static int HYDRO_HASH_BYTES_MAX = 65535;
    public static int HYDRO_HASH_BYTES_MIN = 16;

    public native void hydro_hash_keygen(byte[] key);
    public native int hydro_hash_hash(byte[] hash, int hashLen, byte[] message, int messageLen, byte[] context, byte[] key);
    public native int hydro_hash_init(HydroHashState state, byte[] context, byte[] key);
    public native int hydro_hash_update(HydroHashState state, byte[] input, int inputLen);
    public native int hydro_hash_final(HydroHashState state, byte[] output, int outputLen);

    public static class HydroHashState extends Structure {

        public static class ByReference extends HydroHashState implements Structure.ByReference { }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("state", "buf_off", "align");
        }

        public int[] state = new int[12];
        public byte buf_off = 0;
        public byte[] align = new byte[3];

    }




    // * --------
    // KDF
    // * --------

    public static int HYDRO_KDF_CONTEXTBYTES = 8;
    public static int HYDRO_KDF_KEYBYTES = 32;
    public static int HYDRO_KDF_BYTES_MIN = 16;
    public static int HYDRO_KDF_BYTES_MAX = 65535;

    public native int hydro_kdf_keygen(byte[] masterKey);
    public native int hydro_kdf_derive_from_key(byte[] subKey, int subKeyLen, NativeLong subKeyId, byte[] context, byte[] masterKey);

    public native void hydro_secretbox_keygen(byte[] key);
    public native int hydro_secretbox_encrypt(byte[] cipher, byte[] message, int messageLen, NativeLong messageId, String context, byte[] key);
    public native int hydro_secretbox_decrypt(byte[] message, byte[] cipher, int cipherLen, NativeLong messageId, String context, byte[] key);

    public native void hydro_secretbox_probe_create(byte[] probe, byte[] cipher, int cipherLen, byte[] context, byte[] key);
    public native int hydro_secretbox_probe_verify(byte[] probe, byte[] cipher, int cipherLen, byte[] context, byte[] key);



    // * --------
    // SIGN
    // * --------

    public static int HYDRO_SIGN_BYTES = 64;
    public static int HYDRO_SIGN_CONTEXTBYTES = 8;
    public static int HYDRO_SIGN_PUBLICKEYBYTES = 32;
    public static int HYDRO_SIGN_SECRETKEYBYTES = 64;
    public static int HYDRO_SIGN_SEEDBYTES = 32;

    public native int hydro_sign_keygen(HydroSignKeyPair keyPair);
    public native int hydro_sign_keygen_deterministic(HydroSignKeyPair keyPair, byte[] seed);
    public native int hydro_sign_create(byte[] signature, byte[] message, int messageLen, byte[] context, byte[] secretKey);
    public native int hydro_sign_verify(byte[] signature, byte[] message, int messageLen, byte[] context, byte[] publicKey);

    public native int hydro_sign_init(HydroSignState state, byte[] context);
    public native int hydro_sign_update(HydroSignState state, byte[] message, int messageLen);
    public native int hydro_sign_final_create(HydroSignState state, byte[] signature, byte[] secretKey);
    public native int hydro_sign_final_verify(HydroSignState state, byte[] signature, byte[] publicKey);

    public static class HydroSignKeyPair extends Structure {

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("pk", "sk");
        }

        public byte[] pk = new byte[HYDRO_SIGN_PUBLICKEYBYTES];
        public byte[] sk = new byte[HYDRO_SIGN_SECRETKEYBYTES];

        public byte[] getPublicKey() {
            return pk;
        }

        public byte[] getSecretKey() {
            return sk;
        }
    }

    public static class HydroSignState extends Structure {

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hash_st");
        }

        public HydroHashState hash_st = new  HydroHashState.ByReference();
    }



    // * --------
    // PASSWORD HASHING
    // * --------

    public static int HYDRO_PWHASH_CONTEXTBYTES = 8;
    public static int HYDRO_PWHASH_MASTERKEYBYTES = 32;
    public static int HYDRO_PWHASH_STOREDBYTES = 128;

    public native void hydro_pwhash_keygen(byte[] masterKey);
    public native int hydro_pwhash_deterministic(byte[] key, int keyLen, byte[] password, int passwordLen, byte[] context, byte[] masterKey, NativeLong opsLimit, int memLimit, byte threads);
    public native int hydro_pwhash_create(byte[] stored, byte[] password, int passwordLen, byte[] masterKey, NativeLong opsLimit, int memLimit, byte threads);
    public native int hydro_pwhash_verify(byte[] stored, byte[] password, int passwordLen, byte[] masterKey, NativeLong opsLimitMax, int memLimitMax, byte threadsMax);
    public native int hydro_pwhash_derive_static_key(byte[] staticKey, int staticKeyLen, byte[] stored, byte[] password, int passwordLen, byte[] context, byte[] masterKey, NativeLong opsLimitMax, int memLimitMax, byte threadsMax);
    public native int hydro_pwhash_reencrypt(byte[] stored, byte[] masterKey, byte[] newMasterKey);
    public native int hydro_pwhash_upgrade(byte[] stored, byte[] masterKey, NativeLong opsLimit, int memLimit, byte threads);



    // * --------
    // KEY EXCHANGE
    // * --------

    public static int HYDRO_KX_SESSIONKEYBYTES = 32;
    public static int HYDRO_KX_PUBLICKEYBYTES = 32;
    public static int HYDRO_KX_SECRETKEYBYTES = 32;
    public static int HYDRO_KX_PSKBYTES = 32;
    public static int HYDRO_KX_SEEDBYTES = 32;

    public static int HYDRO_KX_N_PACKET1BYTES = 32;
    public static int HYDRO_KX_KK_PACKET1BYTES = 32;
    public static int HYDRO_KX_KK_PACKET2BYTES = 32;
    public static int HYDRO_KX_XX_PACKET1BYTES = 32;
    public static int HYDRO_KX_XX_PACKET2BYTES = 80;
    public static int HYDRO_KX_XX_PACKET3BYTES = 48;

    public native void hydro_kx_keygen(HydroKxKeyPair keyPair);
    public native int hydro_kx_n_1(HydroKxSessionKeyPair keyPair, byte[] packet1, byte[] psk, byte[] peer_static_pk);
    public native int hydro_kx_n_2(HydroKxSessionKeyPair keyPair, byte[] packet1, byte[] psk, HydroKxKeyPair kp);

    public native int hydro_kx_kk_1(HydroKxState state, byte[] packet1, byte[] peerPublicKey, HydroKxKeyPair keyPair);
    public native int hydro_kx_kk_2(HydroKxSessionKeyPair sessionKeyPair, byte[] packet1, byte[] packet2, byte[] peerPublicKey, HydroKxKeyPair keyPair);
    public native int hydro_kx_kk_3(HydroKxState state, HydroKxSessionKeyPair sessionKeyPair, byte[] packet2, HydroKxKeyPair keyPair);

    public native int hydro_kx_xx_1(HydroKxState state, byte[] packet1, byte[] psk);
    public native int hydro_kx_xx_2(HydroKxState state, byte[] packet2, byte[] packet1, byte[] psk, HydroKxKeyPair keyPair);
    public native int hydro_kx_xx_3(HydroKxState state, HydroKxSessionKeyPair sessionKeyPair, byte[] packet3, byte[] peerPublicKey, byte[] packet2, byte[] psk, HydroKxKeyPair keyPair);
    public native int hydro_kx_xx_4(HydroKxState state, HydroKxSessionKeyPair sessionKeyPair, byte[] peerPublicKey, byte[] packet3, byte[] psk);


    public static class HydroKxKeyPair extends Structure {

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("pk", "sk");
        }

        public byte[] pk = new byte[HYDRO_KX_PUBLICKEYBYTES];
        public byte[] sk = new byte[HYDRO_KX_SECRETKEYBYTES];

        public byte[] getPublicKey() {
            return pk;
        }

        public byte[] getSecretKey() {
            return sk;
        }
    }

    public static class HydroKxSessionKeyPair extends Structure {

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("rx", "tx");
        }

        public byte[] rx = new byte[HYDRO_KX_SESSIONKEYBYTES];
        public byte[] tx = new byte[HYDRO_KX_SESSIONKEYBYTES];

        public byte[] getRx() {
            return rx;
        }

        public byte[] getTx() {
            return tx;
        }
    }

    public static class HydroKxState extends Structure {

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("eph_kp", "h", "ck", "k");
        }

        public HydroKxKeyPair eph_kp = new HydroKxKeyPair();
        public byte[] h = new byte[32];
        public byte[] ck = new byte[32];
        public byte[] k = new byte[32];

        public HydroKxKeyPair getEphKp() {
            return eph_kp;
        }

        public byte[] getH() {
            return h;
        }

        public byte[] getCk() {
            return ck;
        }

        public byte[] getK() {
            return k;
        }
    }





    // * --------
    // HELPERS
    // * --------

    public native void hydro_memzero(byte[] array, int arrayLen);
    public native boolean hydro_equal(byte[] array1, byte[] array2, int len);
    public native char hydro_bin2hex(byte[] hex, int hexLen, byte[] array, int arrayLen);
    public native int hydro_hex2bin(byte[] bin, int maxBinLen, byte[] hex, int hexLen, byte[] ignore, byte lastCharacterParsed);
    public native int hydro_pad(byte[] buffer, int unpaddedBufferLen, int blockSize, int maxBufferLen);
    public native int hydro_unpad(byte[] buffer, int paddedBufferLen, int blockSize);

}
