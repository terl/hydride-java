


extern ""C"" {
int hydro_init();
	/** ---------------- */
	uint32_t hydro_random_u32();
	uint32_t hydro_random_uniform(const uint32_t upper_bound);
	void hydro_random_buf(void* out, size_t out_len);
	void hydro_random_buf_deterministic(void* out, size_t out_len, const uint8_t seed[32]);
	void hydro_random_ratchet();
	void hydro_random_reseed();
	/** ---------------- */
	typedef struct hydro_hash_state {
		uint32_t[12] state;
		uint8_t buf_off;
		uint8_t[3] align;
	} hydro_hash_state;
	void hydro_hash_keygen(uint8_t key[32]);
	int hydro_hash_init(hydro_hash_state* state, const char ctx[8], const uint8_t key[32]);
	int hydro_hash_update(hydro_hash_state* state, const void* in_, size_t in_len);
	int hydro_hash_final(hydro_hash_state* state, uint8_t* out, size_t out_len);
	int hydro_hash_hash(uint8_t* out, size_t out_len, const void* in_, size_t in_len, const char ctx[8], const uint8_t key[32]);
	/** ---------------- */
	void hydro_secretbox_keygen(uint8_t key[32]);
	int hydro_secretbox_encrypt(uint8_t* c, const void* m_, size_t mlen, uint64_t msg_id, const char ctx[8], const uint8_t key[32]);
	int hydro_secretbox_decrypt(void* m_, const uint8_t* c, size_t clen, uint64_t msg_id, const char ctx[8], const uint8_t key[32]);
	void hydro_secretbox_probe_create(uint8_t probe[16], const uint8_t* c, size_t c_len, const char ctx[8], const uint8_t key[32]);
	int hydro_secretbox_probe_verify(const uint8_t probe[16], const uint8_t* c, size_t c_len, const char ctx[8], const uint8_t key[32]);
	/** ---------------- */
	void hydro_kdf_keygen(uint8_t key[32]);
	int hydro_kdf_derive_from_key(uint8_t* subkey, size_t subkey_len, uint64_t subkey_id, const char ctx[8], const uint8_t key[32]);
	/** ---------------- */
	typedef struct hydro_sign_state {
		hydro_hash_state hash_st;
	} hydro_sign_state;
	typedef struct hydro_sign_keypair {
		uint8_t[32] pk;
		uint8_t[64] sk;
	} hydro_sign_keypair;
	void hydro_sign_keygen(hydro_sign_keypair* kp);
	void hydro_sign_keygen_deterministic(hydro_sign_keypair* kp, const uint8_t seed[32]);
	int hydro_sign_init(hydro_sign_state* state, const char ctx[8]);
	int hydro_sign_update(hydro_sign_state* state, const void* m_, size_t mlen);
	int hydro_sign_final_create(hydro_sign_state* state, uint8_t csig[64], const uint8_t sk[64]);
	int hydro_sign_final_verify(hydro_sign_state* state, const uint8_t csig[64], const uint8_t pk[32]);
	int hydro_sign_create(uint8_t csig[64], const void* m_, size_t mlen, const char ctx[8], const uint8_t sk[64]);
	int hydro_sign_verify(const uint8_t csig[64], const void* m_, size_t mlen, const char ctx[8], const uint8_t pk[32]);
	/** ---------------- */
	typedef struct hydro_kx_keypair {
		uint8_t[32] pk;
		uint8_t[32] sk;
	} hydro_kx_keypair;
	typedef struct hydro_kx_session_keypair {
		uint8_t[32] rx;
		uint8_t[32] tx;
	} hydro_kx_session_keypair;
	typedef struct hydro_kx_state {
		hydro_kx_keypair eph_kp;
		hydro_hash_state h_st;
	} hydro_kx_state;
	void hydro_kx_keygen(hydro_kx_keypair* static_kp);
	void hydro_kx_keygen_deterministic(hydro_kx_keypair* static_kp, const uint8_t seed[32]);
	/** NOISE_N */
	int hydro_kx_n_1(hydro_kx_session_keypair* kp, uint8_t packet1[(32 + 16)], const uint8_t psk[32], const uint8_t peer_static_pk[32]);
	int hydro_kx_n_2(hydro_kx_session_keypair* kp, const uint8_t packet1[(32 + 16)], const uint8_t psk[32], const hydro_kx_keypair* static_kp);
	/** NOISE_KK */
	int hydro_kx_kk_1(hydro_kx_state* state, uint8_t packet1[(32 + 16)], const uint8_t peer_static_pk[32], const hydro_kx_keypair* static_kp);
	int hydro_kx_kk_2(hydro_kx_session_keypair* kp, uint8_t packet2[(32 + 16)], const uint8_t packet1[(32 + 16)], const uint8_t peer_static_pk[32], const hydro_kx_keypair* static_kp);
	int hydro_kx_kk_3(hydro_kx_state* state, hydro_kx_session_keypair* kp, const uint8_t packet2[(32 + 16)], const hydro_kx_keypair* static_kp);
	/** NOISE_XX */
	int hydro_kx_xx_1(hydro_kx_state* state, uint8_t packet1[(32 + 16)], const uint8_t psk[32]);
	int hydro_kx_xx_2(hydro_kx_state* state, uint8_t packet2[(32 + 32 + 16 + 16)], const uint8_t packet1[(32 + 16)], const uint8_t psk[32], const hydro_kx_keypair* static_kp);
	int hydro_kx_xx_3(hydro_kx_state* state, hydro_kx_session_keypair* kp, uint8_t packet3[(32 + 16 + 16)], uint8_t peer_static_pk[32], const uint8_t packet2[(32 + 32 + 16 + 16)], const uint8_t psk[32], const hydro_kx_keypair* static_kp);
	int hydro_kx_xx_4(hydro_kx_state* state, hydro_kx_session_keypair* kp, uint8_t peer_static_pk[32], const uint8_t packet3[(32 + 16 + 16)], const uint8_t psk[32]);
	/** NOISE_NK */
	int hydro_kx_nk_1(hydro_kx_state* state, uint8_t packet1[(32 + 16)], const uint8_t psk[32], const uint8_t peer_static_pk[32]);
	int hydro_kx_nk_2(hydro_kx_session_keypair* kp, uint8_t packet2[(32 + 16)], const uint8_t packet1[(32 + 16)], const uint8_t psk[32], const hydro_kx_keypair* static_kp);
	int hydro_kx_nk_3(hydro_kx_state* state, hydro_kx_session_keypair* kp, const uint8_t packet2[(32 + 16)]);
	/** ---------------- */
	void hydro_pwhash_keygen(uint8_t master_key[32]);
	int hydro_pwhash_deterministic(uint8_t* h, size_t h_len, const char* passwd, size_t passwd_len, const char ctx[8], const uint8_t master_key[32], uint64_t opslimit, size_t memlimit, uint8_t threads);
	int hydro_pwhash_create(uint8_t stored[128], const char* passwd, size_t passwd_len, const uint8_t master_key[32], uint64_t opslimit, size_t memlimit, uint8_t threads);
	int hydro_pwhash_verify(const uint8_t stored[128], const char* passwd, size_t passwd_len, const uint8_t master_key[32], uint64_t opslimit_max, size_t memlimit_max, uint8_t threads_max);
	int hydro_pwhash_derive_static_key(uint8_t* static_key, size_t static_key_len, const uint8_t stored[128], const char* passwd, size_t passwd_len, const char ctx[8], const uint8_t master_key[32], uint64_t opslimit_max, size_t memlimit_max, uint8_t threads_max);
	int hydro_pwhash_reencrypt(uint8_t stored[128], const uint8_t master_key[32], const uint8_t new_master_key[32]);
	int hydro_pwhash_upgrade(uint8_t stored[128], const uint8_t master_key[32], uint64_t opslimit, size_t memlimit, uint8_t threads);
	/** ---------------- */
	void hydro_memzero(void* pnt, size_t len);
	void hydro_increment(uint8_t* n, size_t len);
	bool hydro_equal(const void* b1_, const void* b2_, size_t len);
	int hydro_compare(const uint8_t* b1_, const uint8_t* b2_, size_t len);
	char* hydro_bin2hex(char* hex, size_t hex_maxlen, const uint8_t* bin, size_t bin_len);
	int hydro_hex2bin(uint8_t* bin, size_t bin_maxlen, const char* hex, size_t hex_len, const char* ignore, const char** hex_end_p);
	int hydro_pad(unsigned char* buf, size_t unpadded_buflen, size_t blocksize, size_t max_buflen);
	int hydro_unpad(const unsigned char* buf, size_t padded_buflen, size_t blocksize);
}
