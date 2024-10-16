package com.android.settings.bootloader;

import android.util.Base64;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

class LogEncryptor {
    private static final byte[] SYM_ENCRYPT_ALGORITHM_IV = "bootloaderXiaomi".getBytes();
    private final String mEncrytedKey;
    private final SecretKey mSecretKey;

    public LogEncryptor() {
        byte[] decode = Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxPEmV1vZ60qc39gWvaSc\n7QgV/Ltc95eTBiWsRcN5VDeqjGwRPmk7TBXvU+YQ6q2LrfiaDQYg8ZwxjwUTsWoL\nJ7l8AHE0WdUEvdV36+BMbB9w7ts2IISZZNnJyyZleU+SImWYRybKkTPX//Ld/bgK\nNFz3dxJzYxLXdKzcZogHLI2Mvvj31/ZmqvKuRxXBQ2iU4oSPthQRXFY+KbQJ1Z3Z\nsFzMJfGaY1jj+8ymUd4zWGXgztQLuvpUNtiVHGW1WhP8854yJqbQ1VcqfIueKR74\nqoQgUbXHFuYbvz6B0c+bEgJ/tn/bXcM8Zo8aADFgZNCChbzAhB9wf3zx2RLJe7aN\nawIDAQAB", 0);
        try {
            try {
                PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decode));
                try {
                    KeyGenerator instance = KeyGenerator.getInstance("AES");
                    instance.init(256);
                    SecretKey generateKey = instance.generateKey();
                    this.mSecretKey = generateKey;
                    try {
                        Cipher instance2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                        instance2.init(1, generatePublic);
                        try {
                            this.mEncrytedKey = Base64.encodeToString(instance2.doFinal(generateKey.getEncoded()), 2);
                        } catch (BadPaddingException | IllegalBlockSizeException e) {
                            throw new RuntimeException("Should never happen. ", e);
                        }
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException e2) {
                        throw new RuntimeException(e2);
                    } catch (InvalidKeyException e3) {
                        throw new IllegalArgumentException("Encrypt log RSA public key is not valid. ", e3);
                    }
                } catch (NoSuchAlgorithmException e4) {
                    throw new RuntimeException(e4);
                }
            } catch (InvalidKeySpecException e5) {
                throw new IllegalArgumentException("The public key not valid. ", e5);
            }
        } catch (NoSuchAlgorithmException e6) {
            throw new RuntimeException(e6);
        }
    }

    public String wrapEncryptMsg(String str, String str2) {
        try {
            return String.format("%s%s!!%s%s", new Object[]{"#&^", this.mEncrytedKey, encryptMsg(str2), "^&#"});
        } catch (Exception e) {
            Log.e(str, String.format("Failed to encrypt the message: %s. ", new Object[]{e}));
            return "Log record failure";
        }
    }

    private String encryptMsg(String str) {
        try {
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            try {
                instance.init(1, this.mSecretKey, new IvParameterSpec(SYM_ENCRYPT_ALGORITHM_IV));
                try {
                    return Base64.encodeToString(instance.doFinal(str.getBytes(StandardCharsets.UTF_8)), 2);
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    throw new RuntimeException("Should never happen. ", e);
                }
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
                throw new RuntimeException("Should never happen. ", e2);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e3) {
            throw new RuntimeException("Should never happen. ", e3);
        }
    }
}
