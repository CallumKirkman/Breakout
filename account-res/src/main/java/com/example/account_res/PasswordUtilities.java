package com.example.account_res;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/** Utility class of static methods for salting and hashing passwords.
 */
public class PasswordUtilities {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /** Securely generate and return a salt in bytes.
     * @param byteSize - size of the salt to be generated.
     * @return - the salt in bytes.
     */
    static public byte[] generateSalt(int byteSize) {
        byte[] saltBytes = new byte[byteSize];
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
    static public byte[] generateHash(String str, String alg, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alg);
        digest.reset();
        digest.update(salt);
        return digest.digest(str.getBytes());
    }


    /** Format the hash into a hexadecimal string.
     *
     * @param hash - the hash to be converted into the hex format.
     * @return - A string of the hex-formatted hash.
     */
    static public String hexHash(byte[] hash) {

        char[] hex = new char[hash.length*2];

        for(int i=0; i < hash.length; i++) {
            int j = hash[i] & 0xFF;
            hex[i*2] = hexArray[j >>> 4];
            hex[i*2+1] = hexArray[j & 0x0F];
        }
        return new String(hex);
    }
}
