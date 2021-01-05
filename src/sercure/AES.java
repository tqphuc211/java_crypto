package sercure;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class AES {

    static String IV = "AAAAAAAAAAAAAAAA";

    public static void main(String[] args) throws NoSuchAlgorithmException, GeneralSecurityException, IOException {
        System.out.println("Creating AES class");
    }

    public static SecretKey generateAESkey() throws NoSuchAlgorithmException {
        SecretKey secretKey = null;
        KeyGenerator Gen = KeyGenerator.getInstance("AES");
        Gen.init(128);
        secretKey = Gen.generateKey();
        System.out.println("Genereated the AES key : " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        return secretKey;
    }


    public static byte[] encryptMessage(String s, SecretKey AESkey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = null;
        byte[] cipherText = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        cipher.init(Cipher.ENCRYPT_MODE, AESkey, new IvParameterSpec(IV.getBytes()));
        long time3 = System.nanoTime();
        cipherText = cipher.doFinal(s.getBytes());
        long time4 = System.nanoTime();
        long totalAES = time4 - time3;
        System.out.println("Time taken by AES Encryption (Nano Seconds) " + totalAES);
        return cipherText;
    }

    public static byte[] decryptMessage(byte[] encryptedMessage, SecretKey AESkey) {
        Cipher cipher = null;
        byte[] msg = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, AESkey, new IvParameterSpec(IV.getBytes()));
            msg = cipher.doFinal(encryptedMessage);
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            System.out.println("Exception genereated in decryptData method. Exception Name  :" + e.getMessage());
        }
        return msg;
    }
}
