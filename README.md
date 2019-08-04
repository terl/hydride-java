# Hydride (Java)

Hydride is an encryption library that allows developers to incorporate amazing 
cryptography that is compatible on a wide range of platforms into their projects. 
Hydride uses the [Libhydrogen](https://github.com/jedisct1/libsodium) project via JNA wrapping.

## Usage

To get started simple initialise an `Hydrogen` object and use its methods. Here's a simple example to get started:

```java
Hydrogen hydrogen = new Hydrogen();
byte[] key = new byte[Hydrogen.HYDRO_SIGN_SECRETKEYBYTES];
hydrogen.hydro_secretbox_keygen(key);

// Make a cipher array to hold the resulting encrypted text
byte[] cipher = new byte[Hydrogen.HYDRO_SECRETBOX_HEADERBYTES + messageBytes.length];
String context = "context1";
byte[] contextBytes = context.getBytes();
String message = "This is a message that will be encrypted.";
byte[] messageBytes = message.getBytes();
final NativeLong messageId = new NativeLong(0);

// Now encrypt
int encryptSuccess = hydrogen.hydro_secretbox_encrypt(cipher, messageBytes, messageBytes.length, messageId, contextBytes, key);
```

For more information please refer to the [Libhydrogen wiki](https://github.com/jedisct1/libhydrogen/wiki) where 
you can find a full list of all available operations.