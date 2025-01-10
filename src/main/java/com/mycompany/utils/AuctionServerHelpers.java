package com.mycompany.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Cristopher
 */
public class AuctionServerHelpers {

    //RSA
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); // Initializes the RSA key pair generator
        keyGen.initialize(2048); // Sets the key size to 2048 bits (secure);
        return keyGen.generateKeyPair(); // Generates and returns the key pair
    }

    //Encriptando com a chave pública do cliente
    public static String encryptWithPublicKey(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA"); // Initializes the Cipher with the RSA algorithm
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); // Sets the Cipher for encryption mode using the public key
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes()); // Encrypts the data and stores it in encryptedBytes
        return Base64.getEncoder().encodeToString(encryptedBytes); // Encodes the encrypted data in Base64 and returns as a string
    }

    //Decriptando utilizando a chave privada do cliente
    public static String decryptWithPrivateKey(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText); // Decodes the Base64 string to bytes
        Cipher cipher = Cipher.getInstance("RSA"); // Initializes the Cipher with the RSA algorithm
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // Sets the Cipher for decryption mode using the private key
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes); // Decrypts the data and stores it in decryptedBytes
        return new String(decryptedBytes); // Converts the decrypted bytes to a string and returns it
    }

    // Assinando a mensagem com a chave privada
    public static byte[] signMessage(String message, PrivateKey privateKey) throws Exception {
        // Inicializa o objeto Signature com o algoritmo SHA256withRSA
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Configura o Signature para o modo de assinatura com a chave privada
        signature.initSign(privateKey);

        // Atualiza o Signature com os bytes da mensagem
        signature.update(message.getBytes());

        // Gera e retorna a assinatura digital
        return signature.sign();
    }

    // Verificando a assinatura
    public static boolean verifySignature(String message, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        // Inicializa o objeto Signature para verificação com SHA256withRSA
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Configura o Signature para o modo de verificação com a chave pública
        signature.initVerify(publicKey);

        // Atualiza o Signature com os bytes da mensagem
        signature.update(message.getBytes());

        // Verifica a assinatura digital e retorna o resultado
        return signature.verify(signatureBytes);
    }

    //Chave pública -> Base64
    public static String encodePublicKey(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded(); // Gets the encoded form of the public key
        return Base64.getEncoder().encodeToString(publicKeyBytes); // Encodes the byte array to a Base64 string and returns it
    }

    //Chave privada -> Base64
    public static String encodePublicKey(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded(); // Gets the encoded form of the private key
        return Base64.getEncoder().encodeToString(privateKeyBytes); // Encodes the byte array to a Base64 string and returns it
    }

    //Base64 -> Chave pública
    public static PublicKey decodePublicKey(String publicKeyString) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString); // Decodes the Base64 string to a byte array
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes); // Wraps the byte array in an X509EncodedKeySpec
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Gets a KeyFactory instance for RSA
            return keyFactory.generatePublic(keySpec); // Generates and returns the PublicKey object
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Base64 -> Chave Privada
    public static PrivateKey decodePrivateKey(String privateKeyString) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString); // Decodes the Base64 string to a byte array
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes); // Wraps the byte array in an X509EncodedKeySpec
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Gets a KeyFactory instance for RSA
            return keyFactory.generatePrivate(keySpec); // Generates and returns the PrivateKey object
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //AES
    public static SecretKey generateSymmetricKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES"); // Makes a instance of KeyGenerator to the AES algorithm
            keyGen.init(128); // init the KeyGenerator with the key size as 128 bits
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Base64 -> Chave Simétrica
    public static SecretKey decodeSymmetricKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey); // Decodes a Base64 String to bytes
        return new SecretKeySpec(decodedKey, "AES");
    }

    //Chave Simétrica -> Base64
    public static String encodeSymmetricKey(SecretKey key) {
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return encodedKey;
    }

    //Geração do Vetor de Inicialização (IV) a partir da chave simétrica
    public static IvParameterSpec generateIV(SecretKey key) {
        try {
            byte[] keyBytes = key.getEncoded(); // Gets the key bytes
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Uses SHA-256 to ensure 32 bytes of output
            byte[] iv = md.digest(keyBytes); // Generates a hash of the key

            byte[] iv16 = new byte[16]; // Creates a 16-byte array for the IV
            System.arraycopy(iv, 0, iv16, 0, 16); // Copies the first 16 bytes of the hash into the IV

            return new IvParameterSpec(iv16); // Returns the IV as an IvParameterSpec object
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuctionServerHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Cifrar a mensagem utilizando a chave simétrica (AES)
    public static String encrypt(String plainText, SecretKey secretKey, IvParameterSpec ivParameterSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // Modo CBC com preenchimento PKCS5
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Codificar para Base64
    }

    public static String decrypt(String encryptedText, SecretKey secretKey, IvParameterSpec ivParameterSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
