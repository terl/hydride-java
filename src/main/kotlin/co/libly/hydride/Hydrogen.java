package co.libly.hydride;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;

public class Hydrogen implements Library {

    public static final String JNA_LIBRARY_NAME = "hydrogen";

    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(Hydrogen.JNA_LIBRARY_NAME);

    public static final int HYDRO_VERSION_MAJOR = 1;

    public static final int HYDRO_VERSION_MINOR = 0;

    public static final int HYDRO_RANDOM_SEEDBYTES = 32;

    public static final int HYDRO_HASH_BYTES = 32;

    public static final int HYDRO_HASH_BYTES_MAX = 65535;

    public static final int HYDRO_HASH_BYTES_MIN = 16;

    public static final int HYDRO_HASH_CONTEXTBYTES = 8;

    public static final int HYDRO_HASH_KEYBYTES = 32;

    public static final int HYDRO_SECRETBOX_CONTEXTBYTES = 8;

    public static final int HYDRO_SECRETBOX_HEADERBYTES = (20 + 16);

    public static final int HYDRO_SECRETBOX_KEYBYTES = 32;

    public static final int HYDRO_SECRETBOX_PROBEBYTES = 16;

    public static final int HYDRO_KDF_CONTEXTBYTES = 8;

    public static final int HYDRO_KDF_KEYBYTES = 32;

    public static final int HYDRO_KDF_BYTES_MAX = 65535;

    public static final int HYDRO_KDF_BYTES_MIN = 16;

    public static final int HYDRO_SIGN_BYTES = 64;

    public static final int HYDRO_SIGN_CONTEXTBYTES = 8;

    public static final int HYDRO_SIGN_PUBLICKEYBYTES = 32;

    public static final int HYDRO_SIGN_SECRETKEYBYTES = 64;

    public static final int HYDRO_SIGN_SEEDBYTES = 32;

    public static final int HYDRO_KX_SESSIONKEYBYTES = 32;

    public static final int HYDRO_KX_PUBLICKEYBYTES = 32;

    public static final int HYDRO_KX_SECRETKEYBYTES = 32;

    public static final int HYDRO_KX_PSKBYTES = 32;

    public static final int HYDRO_KX_SEEDBYTES = 32;

    public static final int HYDRO_KX_N_PACKET1BYTES = (32 + 16);

    public static final int HYDRO_KX_KK_PACKET1BYTES = (32 + 16);

    public static final int HYDRO_KX_KK_PACKET2BYTES = (32 + 16);

    public static final int HYDRO_KX_XX_PACKET1BYTES = (32 + 16);

    public static final int HYDRO_KX_XX_PACKET2BYTES = (32 + 32 + 16 + 16);

    public static final int HYDRO_KX_XX_PACKET3BYTES = (32 + 16 + 16);

    public static final int HYDRO_KX_NK_PACKET1BYTES = (32 + 16);

    public static final int HYDRO_KX_NK_PACKET2BYTES = (32 + 16);

    public static final int HYDRO_PWHASH_CONTEXTBYTES = 8;

    public static final int HYDRO_PWHASH_MASTERKEYBYTES = 32;

    public static final int HYDRO_PWHASH_STOREDBYTES = 128;

    public static final int HYDRO_HWTYPE_ATMEGA328 = 1;

    public static native int hydroInit();

    public static native int hydroRandomU32();

    public static native int hydroRandomUniform(int upper_bound);

    public static native void hydroRandomBuf(Pointer out, NativeSize out_len);

    public static native void hydroRandomBufDeterministic(Pointer out, NativeSize out_len, ByteBuffer seed);

    public static native void hydroRandomRatchet();

    public static native void hydroRandomReseed();

    public static native void hydroHashKeygen(ByteBuffer key);

    public static native int hydroHashInit(HydroHashState state, ByteBuffer ctx, ByteBuffer key);

    public static native int hydroHashUpdate(HydroHashState state, Pointer in_, NativeSize in_len);

    public static native int hydroHashFinal(HydroHashState state, ByteBuffer out, NativeSize out_len);

    public static native int hydroHashHash(ByteBuffer out, NativeSize out_len, Pointer in_, NativeSize in_len, ByteBuffer ctx, ByteBuffer key);

    public static native void hydroSecretboxKeygen(ByteBuffer key);

    public static native int hydroSecretboxEncrypt(ByteBuffer c, Pointer m_, NativeSize mlen, long msg_id, ByteBuffer ctx, ByteBuffer key);

    public static native int hydroSecretboxDecrypt(Pointer m_, ByteBuffer c, NativeSize clen, long msg_id, ByteBuffer ctx, ByteBuffer key);

    public static native void hydroSecretboxProbeCreate(ByteBuffer probe, ByteBuffer c, NativeSize c_len, ByteBuffer ctx, ByteBuffer key);

    public static native int hydroSecretboxProbeVerify(ByteBuffer probe, ByteBuffer c, NativeSize c_len, ByteBuffer ctx, ByteBuffer key);

    public static native void hydroKdfKeygen(ByteBuffer key);

    public static native int hydroKdfDeriveFromKey(ByteBuffer subkey, NativeSize subkey_len, long subkey_id, ByteBuffer ctx, ByteBuffer key);

    public static native void hydroSignKeygen(HydroSignKeypair kp);

    public static native void hydroSignKeygenDeterministic(HydroSignKeypair kp, ByteBuffer seed);

    public static native int hydroSignInit(HydroSignState state, ByteBuffer ctx);

    public static native int hydroSignUpdate(HydroSignState state, Pointer m_, NativeSize mlen);

    public static native int hydroSignFinalCreate(HydroSignState state, ByteBuffer csig, ByteBuffer sk);

    public static native int hydroSignFinalVerify(HydroSignState state, ByteBuffer csig, ByteBuffer pk);

    public static native int hydroSignCreate(ByteBuffer csig, Pointer m_, NativeSize mlen, ByteBuffer ctx, ByteBuffer sk);

    public static native int hydroSignVerify(ByteBuffer csig, Pointer m_, NativeSize mlen, ByteBuffer ctx, ByteBuffer pk);

    public static native void hydroKxKeygen(HydroKxKeypair static_kp);

    public static native void hydroKxKeygenDeterministic(HydroKxKeypair static_kp, ByteBuffer seed);

    public static native int hydroKxN1(HydroKxSessionKeypair kp, ByteBuffer packet1, ByteBuffer psk, ByteBuffer peer_static_pk);

    public static native int hydroKxN2(HydroKxSessionKeypair kp, ByteBuffer packet1, ByteBuffer psk, HydroKxKeypair static_kp);

    public static native int hydroKxKk1(HydroKxState state, ByteBuffer packet1, ByteBuffer peer_static_pk, HydroKxKeypair static_kp);

    public static native int hydroKxKk2(HydroKxSessionKeypair kp, ByteBuffer packet2, ByteBuffer packet1, ByteBuffer peer_static_pk, HydroKxKeypair static_kp);

    public static native int hydroKxKk3(HydroKxState state, HydroKxSessionKeypair kp, ByteBuffer packet2, HydroKxKeypair static_kp);

    public static native int hydroKxXx1(HydroKxState state, ByteBuffer packet1, ByteBuffer psk);

    public static native int hydroKxXx2(HydroKxState state, ByteBuffer packet2, ByteBuffer packet1, ByteBuffer psk, HydroKxKeypair static_kp);

    public static native int hydroKxXx3(HydroKxState state, HydroKxSessionKeypair kp, ByteBuffer packet3, ByteBuffer peer_static_pk, ByteBuffer packet2, ByteBuffer psk, HydroKxKeypair static_kp);

    public static native int hydroKxXx4(HydroKxState state, HydroKxSessionKeypair kp, ByteBuffer peer_static_pk, ByteBuffer packet3, ByteBuffer psk);

    public static native int hydroKxNk1(HydroKxState state, ByteBuffer packet1, ByteBuffer psk, ByteBuffer peer_static_pk);

    public static native int hydroKxNk2(HydroKxSessionKeypair kp, ByteBuffer packet2, ByteBuffer packet1, ByteBuffer psk, HydroKxKeypair static_kp);

    public static native int hydroKxNk3(HydroKxState state, HydroKxSessionKeypair kp, ByteBuffer packet2);

    public static native void hydroPwhashKeygen(ByteBuffer master_key);

    public static native int hydroPwhashDeterministic(ByteBuffer h, NativeSize h_len, String passwd, NativeSize passwd_len, ByteBuffer ctx, ByteBuffer master_key, long opslimit, NativeSize memlimit, byte threads);

    public static native int hydroPwhashCreate(ByteBuffer stored, String passwd, NativeSize passwd_len, ByteBuffer master_key, long opslimit, NativeSize memlimit, byte threads);

    public static native int hydroPwhashVerify(ByteBuffer stored, String passwd, NativeSize passwd_len, ByteBuffer master_key, long opslimit_max, NativeSize memlimit_max, byte threads_max);

    public static native int hydroPwhashDeriveStaticKey(ByteBuffer static_key, NativeSize static_key_len, ByteBuffer stored, String passwd, NativeSize passwd_len, ByteBuffer ctx, ByteBuffer master_key, long opslimit_max, NativeSize memlimit_max, byte threads_max);

    public static native int hydroPwhashReencrypt(ByteBuffer stored, ByteBuffer master_key, ByteBuffer new_master_key);

    public static native int hydroPwhashUpgrade(ByteBuffer stored, ByteBuffer master_key, long opslimit, NativeSize memlimit, byte threads);

    public static native void hydroMemzero(Pointer pnt, NativeSize len);

    public static native void hydroIncrement(ByteBuffer n, NativeSize len);

    public static native byte hydroEqual(Pointer b1_, Pointer b2_, NativeSize len);

    public static native int hydroCompare(ByteBuffer b1_, ByteBuffer b2_, NativeSize len);

    public static native String hydroBin2hex(String hex, NativeSize hex_maxlen, ByteBuffer bin, NativeSize bin_len);

    public static native int hydroHex2bin(ByteBuffer bin, NativeSize bin_maxlen, String hex, NativeSize hex_len, String ignore, String[] hex_end_p);

    public static native int hydroPad(ByteBuffer buf, NativeSize unpadded_buflen, NativeSize blocksize, NativeSize max_buflen);

    public static native int hydroUnpad(ByteBuffer buf, NativeSize padded_buflen, NativeSize blocksize);
}
