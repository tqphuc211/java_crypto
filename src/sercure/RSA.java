package sercure;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import config.config;

public class RSA {

    Key publicKey;
    Key privateKey;

    public static void main(String[] args) throws NoSuchAlgorithmException, GeneralSecurityException, IOException {

        System.out.println("Creating RSA class");
        RSA rsa = new RSA();
        rsa.createRSA();
    }

    void createRSA() throws NoSuchAlgorithmException, GeneralSecurityException, IOException {

        KeyPairGenerator kPairGen = KeyPairGenerator.getInstance("RSA");
        kPairGen.initialize(1024);
        KeyPair kPair = kPairGen.genKeyPair();
        publicKey = kPair.getPublic();
        System.out.println(publicKey);
        privateKey = kPair.getPrivate();

        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(kPair.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kPair.getPrivate(), RSAPrivateKeySpec.class);
        serializeToFile(config.release.get(config.PUBLIC_KEY_PATH), pub.getModulus(), pub.getPublicExponent());                // this will give public key file
        serializeToFile(config.release.get(config.PRIVATE_KEY_PATH), priv.getModulus(), priv.getPrivateExponent());            // this will give private key file

    }

    void serializeToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream ObjOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

        try {
            ObjOut.writeObject(mod);
            ObjOut.writeObject(exp);
            System.out.println("Key File Created: " + fileName);
        } catch (Exception e) {
            throw new IOException(" Error while writing the key object", e);
        } finally {
            ObjOut.close();
        }
    }

    private static PrivateKey readPrivateKeyFromFile(String fileName) throws IOException {

        FileInputStream in = new FileInputStream(fileName);
        ObjectInputStream readObj = new ObjectInputStream(new BufferedInputStream(in));

        try {
            BigInteger m = (BigInteger) readObj.readObject();
            BigInteger d = (BigInteger) readObj.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, d);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey priKey = fact.generatePrivate(keySpec);
            return priKey;
        } catch (Exception e) {
            throw new RuntimeException("Some error in reading private key", e);
        } finally {
            readObj.close();
        }
    }

    public static byte[] encryptMessage(byte[] message) {
        byte[] messEncrypted = null;
        try {
            PublicKey pK = readPublicKeyFromFile(config.release.get(config.PUBLIC_KEY_PATH));
            System.out.println("Encrypting the message using RSA Public Key" + pK);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pK);
            long time1 = System.nanoTime();
            messEncrypted = cipher.doFinal(message);
            long time2 = System.nanoTime();
            long totalRSA = time2 - time1;
            System.out.println("Time taken by RSA Encryption (Nano Seconds) : " + totalRSA);
        } catch (Exception e) {
            System.out.println("exception encoding key: " + e.getMessage());
            e.printStackTrace();
        }
        return messEncrypted;
    }

    public static byte[] decryptMessage(byte[] encryptedMessage) {
        byte[] message = null;
        PrivateKey privKey = null;
        Cipher keyDecipher = null;
        try {
            privKey = readPrivateKeyFromFile(config.release.get(config.PRIVATE_KEY_PATH));            //  private key
            keyDecipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");        // initialize the cipher...
            keyDecipher.init(Cipher.DECRYPT_MODE, privKey);
            System.out.println(">>AES>"+ Base64.getEncoder().encodeToString(keyDecipher.doFinal(encryptedMessage)));
            message = keyDecipher.doFinal(encryptedMessage);
            System.out.println();
            System.out.println(" AES key after decryption : " + message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception decrypting the aes key: " + e.getMessage());
        }
        return message;
    }

    private static PublicKey readPublicKeyFromFile(String fileName) throws IOException {

        FileInputStream in = new FileInputStream(fileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpecifications = new RSAPublicKeySpec(m, e);

            KeyFactory kF = KeyFactory.getInstance("RSA");
            PublicKey pubK = kF.generatePublic(keySpecifications);
            return pubK;
        } catch (Exception e) {
            throw new RuntimeException("Some error in reading public key", e);
        } finally {
            oin.close();
        }
    }

}
