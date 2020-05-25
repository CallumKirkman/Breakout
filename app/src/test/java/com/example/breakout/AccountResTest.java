package com.example.breakout;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import com.example.account_res.*;

/**
 * Test class for testing the account-res module.
 */
class AccountResTest {

    /**
     * Test the email regex on valid email addresses.
     * @param email - test email.
     */
    @ParameterizedTest()
    @MethodSource("validEmails")
    void testEmailValid(String email) { assertTrue(InputValidation.validateEmail(email)); }


    /**
     * Test the email regex on invalid email addresses
     * @param email - test email.
     */
    @ParameterizedTest()
    @MethodSource("invalidEmails")
    void testEmailInvalid(String email) { assertFalse(InputValidation.validateEmail(email)); }


    /**
     * Test the password regex on valid passwords.
     * @param password - test password.
     */
    @ParameterizedTest()
    @MethodSource("validPasswords")
    void testPasswordValid(String password) { assertTrue(InputValidation.validatePassword(password)); }


    /**
     * Test the password regex on invalid passwords.
     * @param password - test password.
     */
    @ParameterizedTest()
    @MethodSource("invalidPasswords")
    void testPasswordInvalid(String password) { assertFalse(InputValidation.validatePassword(password)); }

    /**
     * Test the name regex on valid names.
     * @param name - test name.
     */
    @ParameterizedTest()
    @MethodSource("validNames")
    void testNameValid(String name) { assertTrue(InputValidation.validateName(name)); }


    /**
     * Test the name regex on invalid names.
     * @param name - test name.
     */
    @ParameterizedTest()
    @MethodSource("invalidNames")
    void testNameInvalid(String name) { assertFalse(InputValidation.validateName(name)); }


    /**
     * Test the credit card regex on valid numbers.
     * @param number - test card number.
     */
    @ParameterizedTest()
    @MethodSource("validCardNumbers")
    void testCardNumberValid(String number) { assertTrue(InputValidation.validateCard(number)); }


    /**
     * Test the credit card regex on invalid numbers.
     * @param number - test card number.
     */
    @ParameterizedTest()
    @MethodSource("invalidCardNumbers")
    void testCardNumberInvalid(String number) { assertFalse(InputValidation.validateCard(number)); }


    /**
     * Test the CVV regex on valid CVVs.
     * @param cvv - test CVV number.
     */
    @ParameterizedTest()
    @MethodSource("validCVVs")
    void testCvvValid(String cvv) { assertTrue(InputValidation.validateCVV(cvv)); }


    /**
     * Test the CVV regex on invalid CVVs.
     * @param cvv - test CVV number.
     */
    @ParameterizedTest()
    @MethodSource("invalidCVVs")
    void testCvvInvalid(String cvv) { assertFalse(InputValidation.validateCVV(cvv)); }


    /**
     * Test the expiry data regex on valid card expiry dates.
     * @param date - test expiry date.
     */
    @ParameterizedTest()
    @MethodSource("validExpiryDates")
    void testExpiryDateValid(String date) { assertTrue(InputValidation.validateDate(date)); }


    /**
     * Test the expiry data regex on invalid card expiry dates.
     * @param date - test expiry date.
     */
    @ParameterizedTest()
    @MethodSource("invalidExpiryDates")
    void testExpiryDateInvalid(String date) { assertFalse(InputValidation.validateDate(date)); }


    /**
     * Test that a password with two different salts produces a different hash.
     * @param password - test password.
     */
    @ParameterizedTest
    @MethodSource("validPasswords")
    void testDifferentSaltDifferentPassword(String password) {
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
    }


    /**
     * Test that hashing the same string twice produces the same hash both times.
     * @param password - test password.
     */
    @ParameterizedTest()
    @MethodSource("validPasswords")
    void testSameStringSameHash(String password) {
        try {
            byte[] hash = PasswordUtilities.generateHash(password, "SHA-256");
            String hashed1 = PasswordUtilities.hexBytes(hash);
            // Generate the hash again for the same string, test they're equal.
            byte[] repeatHash = PasswordUtilities.generateHash(password, "SHA-256");
            String hashed2 = PasswordUtilities.hexBytes(repeatHash);

            assertEquals(hashed1, hashed2);
        }
        catch(NoSuchAlgorithmException exc) { }
    }


    /**
     * Test that hash length isn't affected by password and salt length.
     * @param password - test password.
     */
    @ParameterizedTest()
    @MethodSource("validPasswords")
    void testFixedHashLength(String password) {
        String salt1 = PasswordUtilities.generateSalt(2);
        String salt2 = PasswordUtilities.generateSalt(8);
        String salt3 = PasswordUtilities.generateSalt(16);

        try {
            byte[] hash1 = PasswordUtilities.generateHash(password, salt1, "SHA-256");
            byte[] hash2 = PasswordUtilities.generateHash(password, salt2, "SHA-256");
            byte[] hash3 = PasswordUtilities.generateHash(password, salt3, "SHA-256");

            assertEquals(hash1.length, hash2.length, hash3.length);
        }
        catch(NoSuchAlgorithmException exc) { }
    }


    //---------- Test Data ----------//


    /**
     * An array of valid emails - as per:
     * https://gist.github.com/cjaoude/fd9910626629b53c4d25
     * @return - the array.
     */
    private static String[] validEmails() {
        return new String[] {
                "email@example.com", "firstname.lastname@example.com",
                "email@subdomain.example.com", "firstname+lastname@example.com",
                "email@123.123.123.123", "email@[123.123.123.123]",
                "\"email\"@example.com", "1234567890@example.com",
                "email@example-one.com", "_______@example.com",
                "email@example.name", "email@example.museum",
                "email@example.co.jp", "firstname-lastname@example.com",
                "very.unusual.”@”.unusual.com@example.com",
                "very.”(),:;<>[]”.VERY.”very@\\\"very”.unusual@strange.example.com", "much.”more\\ unusual”@example.com"};
    }


    /**
     * An array of invalid emails - as per:
     * https://gist.github.com/cjaoude/fd9910626629b53c4d25
     * @return - the array.
     */
    private static String[] invalidEmails() {
        return new String[] {
                "plainaddress", "#@%^%#$@#$@#.com",
                "@example.com", "Joe Smith <email@example.com>",
                "email.example.com", "email@example@example.com",
                ".email@example.com", "email.@example.com",
                "email..email@example.com", "あいうえお@example.com",
                "email@example.com (Joe Smith)", "email@example",
                "email@-example.com", "email@example.web",
                "email@111.222.333.44444", "email@example..com",
                "Abc..123@example.com", "”(),:;<>[\\]@example.com",
                "just”not”right@example.com", "this\\ is\"really\"not\\allowed@example.com" };
    }


    /**
     * An array of valid passwords.
     * Password constraints are defined in InputValidation.validatePassword().
     * @return - the array.
     */
    static String[] validPasswords() {
        return new String[] {
                "aB34567%",
                "12345567fffGGG%$",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1B!",
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!7!!!!!aB",
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA2AAAAAAAAb$",
                "myPassword123!",
                "S1mpl3p455w0rd%",
                ".....1aB",
                "--__123AAAbbG%",
                "123456_iJ"
        };
    }


    /**
     * An array of invalid passwords.
     * Password constraints are defined in InputValidation.validatePassword().
     * @return - the array.
     */
    private static String[] invalidPasswords() {
        return new String[] {
                "aB123456", // Missing special char.
                "a123456[", // Missing uppercase.
                "A123456[", // Missing lowercase.
                "aB@",      // Missing length, number.
                "aaaaaaaa", // Missing uppercase, number, special char.
                "aaaaaaaA", // Missing number, special char.
                "aaaaaaa!", // Missing uppercase, special char.
                "",         // Missing length, lowercase, uppercase, number, special char.
                "a",        // Missing length, uppercase, number, special char.
                "aB",       // Missing length, number, special char.
                "a6",       // Missing length, uppercase, special char.
                "a%",       // Missing length, uppercase, number.
                "A$",       // Missing length, lowercase, number.
                "abcdefgh", // Missing uppercase, number, special char.
                "B",        // Missing length, lowercase, number, special char.
                "!",        // Missing length, lowercase, uppercase, number.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", // Missing uppercase, number, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB", // Missing number, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa[", // Missing uppercase, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa7",  // Missing uppercase, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",    // Missing length, uppercase, number, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB",   // Missing length, number, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa5",   // Missing length, uppercase, special char.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa%",   // Missing length, uppercase, number.
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB%",  // Missing length, number.
                "aaaaaaaaaaaaaaaaaaaa",     // Missing uppercase, number, special char.
                "aaaaaaaaaaaaaaaaaaaaL",    // Missing number, special char.
                "aaaaaaaaaaaaaaaaaaaa@",    // Missing uppercase, number.
                "aaaaaaaaaaaaaaaaaaaa7"     // Missing uppercase, special char.
        };
    }


    /**
     * An array of valid names.
     * Name constraints are defined in InputValidation.validateName().
     * @return - the array.
     */
    private static String[] validNames() {
        return new String[] {
                "Alex",
                "bob",
                "JIM",
                "mIxEdCaSe",
                "",
                "a",
                "st. dot",
                "st. dot-o",
                "st. dot-o'name",
                "one-two-three",
                "O'O'O'O",
                "Mary-Ann",
                "O'Neil",
                "Quite A Long Name With Spaces",
                "James-Second O'Something"
        };
    }


    /**
     * An array of invalid names.
     * Name constraints are defined in InputValidation.validateName().
     * @return - the array.
     */
    private static String[] invalidNames() {
        return new String[] {
                "name1",
                "NAME1",
                "name!",
                "nAmE%",
                "123",
                "2",
                "$&*",
                ".1",
                "st. name-two O'1",
                "john-0"
        };
    }


    /**
     *  An array of valid Visa, Mastercard, and American Express card numbers.
     * @return - the array.
     */
    private static String[] validCardNumbers() {
        return new String[] {
                "4123445575272",    // 13 digits, starting with a 4. (Visa)
                "4123445575272256", // 16 digits, starting with a 4. (Visa)
                "5134591289690298", // 16 digits, starting with 51.  (MasterCard)
                "5234591289690298", // 16 digits, starting with 52.  (MasterCard)
                "5234591289690298", // 16 digits, starting with 53.  (MasterCard)
                "5434591289690298", // 16 digits, starting with 54.  (MasterCard)
                "5534591289690298", // 16 digits, starting with 55.  (MasterCard)
                "344285723457823",  // 15 digits, starting with 34.  (American Express)
                "372347435634244",  // 15  digits, starting with 37. (American Express)
        };
    }


    /**
     *  An array of invalid Visa, Mastercard, and American Express card numbers.
     * @return - the array.
     */
    private static String[] invalidCardNumbers() {
        return new String[] {
                // Visa
                "412316346346",      // 12 digits, starting with a 4.
                "46346345563576453", // 17 digits, starting with a 4.
                "5253374634345",     // 13 digits, without starting with a 4.
                "8834453453345435",  // 16 digits, without starting with a 4.
                "445745623423154",    // 15 digits, starting with a 4.
                // MasterCard
                "512543452345234",       // 15 digits, starting with 51.
                "52254345234523465",     // 17 digits, starting with 52.
                "532543452345234",       // 15 digits, starting with 53.
                "54254345232945234",     // 17 digits, starting with 54.
                "552543452345234",       // 15 digits, starting with 55.
                "6325434524345234",     // 16 digits, not starting with 51-55.
                // American Express
                "34345345293452",       // 14 digits, starting with 34.
                "37345345293452",       // 14 digits, starting with 37.
                "3434537245293452",     // 16 digits, starting with 34.
                "3734534528393452",     // 16 digits, starting with 37.
                "968234545745645"       // 15 digits, not starting with 34 or 37.
        };
    }

    /**
     * An array of valid CVV numbers.
     * @return - the array.
     */
    private static String[] validCVVs() {
        return new String[] {
                "123",
                "000",
                "999",
                "1234",
                "0000",
                "9999"
        };
    }


    /**
     * An array of invalid CVV numbers.
     * @return - the array.
     */
    private static String[] invalidCVVs() {
        return new String[] {
                "1",
                "12",
                "12345",
                "12A",
                "123A",
                "1234A",
                "12%",
                "123!",
                "1234£"
        };
    }


    /**
     * An array of valid card expiry dates.
     * @return - the array.
     */
    private static String[] validExpiryDates() {
        return new String[] {
                "05/20",
                "0520",
                "05/2020",
                "052020",
        };
    }


    /**
     * An array of invalid card expiry dates.
     * @return - the array.
     */
    private static String[] invalidExpiryDates() {
        return new String[] {
                "05/202",
                "05/20200",
                "05220",
                "0520200",
                "05/2",
                "052",
                "05/20a",
                "05/2020!",
                "",
        };
    }
}
