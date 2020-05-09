package com.example.account_res;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/** Utility class of static methods for salting and hashing passwords.
 */
public class PasswordUtilities {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /** Securely generate and return a salt in bytes.
     *
     * @param size - size of the salt to be generated.
     * @return - the salt in bytes.
     */
    public static byte[] generateSalt(int size) {
        byte[] saltBytes = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);
        return saltBytes;
    }


    /** Generate the hash of the supplied string and salt using the given algorithm.
     *
     * @param str - the string to be hashed.
     * @param alg - the hashing algorithm to be used. I.E. "SHA-256".
     * @param salt - the salt to be used in the hash.
     * @return - the hash in bytes.
     * @throws NoSuchAlgorithmException - if the algorithm parameter isn't recognised.
     */
    public static byte[] generateHash(String str, String alg, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alg);
        digest.reset();
        digest.update(salt);
        return digest.digest(str.getBytes());
    }


    /** Format the bytes into a hexadecimal string.
     *
     * @param bytes - the hash to be converted into the hex format.
     * @return - A string of the hex-formatted hash.
     */
    public static String hexBytes(byte[] bytes) {

        char[] hex = new char[bytes.length*2];

        for(int i=0; i < bytes.length; i++) {
            int j = bytes[i] & 0xFF;
            hex[i*2] = hexArray[j >>> 4];
            hex[i*2+1] = hexArray[j & 0x0F];
        }
        return new String(hex);
    }
}
