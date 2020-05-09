package com.example.account_res;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;


/** Utility class of static methods for salting and hashing passwords.
 */
public class PasswordUtilities {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /** Securely generate and return a salt as a String.
     *
     * @param size - size of the salt to be generated.
     * @return - the salt.
     */
    public static String generateSalt(int size) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < size; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }
        return sb.toString();
    }


    /** Generate the hash of the supplied string and salt using the given algorithm.
     *
     * @param str - the string to be hashed.
     * @param salt - the salt to be used in the hash.
     * @param alg - the hashing algorithm to be used. I.E. "SHA-256".
     * @return - the hash in bytes.
     * @throws NoSuchAlgorithmException - if the algorithm parameter isn't recognised.
     */
    public static byte[] generateHash(String str, String salt, String alg) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alg);
        digest.reset();
        String hash = str + salt;

        return digest.digest(hash.getBytes());
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
