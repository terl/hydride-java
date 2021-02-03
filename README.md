

<img width="80" style="display: inline;" src="https://filedn.com/lssh2fV92SE8dRT5CWJvvSy/libly/hydride.png" />


# Hydride (Java)

Hydride is a Java encryption library that allows developers to incorporate amazing 
cryptography that is compatible on a wide range of platforms into their projects. 
Hydride uses the [Libhydrogen](https://github.com/jedisct1/libsodium) project via JNA wrapping.

## Installation



```groovy
repositories {
    mavenCentral()
    maven {
        url  "https://dl.bintray.com/libly/maven"
    }
}

dependencies {
    implementation "co.libly:hydride-java:1.1.3"
}
```

## Supported platforms
Unlike other implementations, Hydride **packages the shared libraries** 
(libhydrogen.so, libhydrogen.dylib and libhydrogen.dll) within itself so you don't have to waste time compiling them.
Other implementations probably force you to build those shared libraries or include a build step to build those 
shared libraries which, in my experience, fail most of the time. Platforms that are currently supported:

* Windows 7 and above.
* Ubuntu 14.04 and above*.
* Android 16 and above.
* iOS 10.2 and above.
* MacOS 10.11 and above.
* Armv6 and above (e.g. Raspberry Pi 2+, ODroid).

More platforms can be happily added, just create an issue.

## Usage

To get started simple initialise an `Hydrogen` object and use its methods. Here's a simple example to get started:

```java
// Initialise
Hydrogen hydrogen = new Hydrogen();

// Make a key
byte[] key = new byte[Hydrogen.HYDRO_SIGN_SECRETKEYBYTES];
hydrogen.hydro_secretbox_keygen(key);

// Make a cipher array to hold the resulting encrypted text
byte[] cipher = new byte[Hydrogen.HYDRO_SECRETBOX_HEADERBYTES + messageBytes.length];
String context = "context1";
byte[] contextBytes = context.getBytes();
String message = "This is a message that will be encrypted.";
byte[] messageBytes = message.getBytes();
long messageId = 1L;

// Now encrypt
int encryptSuccess = hydrogen.hydro_secretbox_encrypt(cipher, messageBytes, messageBytes.length, messageId, contextBytes, key);
```

For more information on how to use please refer to the [Libhydrogen wiki](https://github.com/jedisct1/libhydrogen/wiki) where 
you can find a full list of all available operations. Just use `hydrogen.your_operation_name()` to use them.

