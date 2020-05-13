package com.example.breakout;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.account_res.*;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;


class TestUtilities {


    @ParameterizedTest
    @ValueSource(strings = {"Password!", "aaaaa23B%", "CCCCCCG$"})
    void testHashSaltedPasswords(String password) {
        String salt1 = PasswordUtilities.generateSalt(8);
        String salt2 = PasswordUtilities.generateSalt(8);
        // Salts generated could (potentially) be the same.
        assertNotEquals(salt1, salt2);
        try {
            byte[] hash1 = PasswordUtilities.generateHash(password, salt1, "SHA-256");
            byte[] hash2 = PasswordUtilities.generateHash(password, salt2, "SHA-256");

            assertNotEquals(hash1, hash2);
        }
        catch(NoSuchAlgorithmException exc) { }




        // Make a password.
        // Generate two salts.
        // Hash them both. Assert they're different.

    }

    @ParameterizedTest()
    @ValueSource(strings = {"Password!", "aaaaa23B%", "CCCCCCG$"})
    void testHashedPasswords(String password) {
        String emptySalt = "";
        try {
            byte[] hash = PasswordUtilities.generateHash(password, emptySalt, "SHA-256");
            String hashed1 = PasswordUtilities.hexBytes(hash);

            // Generate the hash again for the same string, test they're equal.
            byte[] repeatHash = PasswordUtilities.generateHash(password, emptySalt, "SHA-256");
            String hashed2 = PasswordUtilities.hexBytes(repeatHash);

            assertEquals(hashed1, hashed2);
        }
        catch(NoSuchAlgorithmException exc) { }
    }


    @Test
    void testFixedHashLength() {

        // Make a password, add salt, hash it.
        // Get length.
        // Repeat (different length password and salt).

        // Make sure all lengths are the same, regardless of inputted passwords.
    }
}
