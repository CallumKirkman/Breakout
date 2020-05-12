package com.example.breakout;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.account_res.*;
import static org.junit.Assert.*;


class TestValidation {

    @ParameterizedTest()
    @MethodSource("validEmails")
    void testValidEmails(String email) {
        assertTrue(InputValidation.validateEmail(email));
    }

    @ParameterizedTest()
    @MethodSource("invalidEmails")
    void testInvalidEmails(String email) {
        assertFalse(InputValidation.validateEmail(email));
    }


    @ParameterizedTest()
    @MethodSource("validPasswords")
     void testValidPasswords(String password) {
        assertTrue(InputValidation.validatePassword(password));
    }

    @ParameterizedTest()
    @MethodSource("invalidPasswords")
    void testInvalidPasswords(String password) {
        assertFalse(InputValidation.validatePassword(password));
    }


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
                "very.”(),:;<>[]”.VERY.”very@\\ \"very”.unusual@strange.example.com", "much.”more\\ unusual”@example.com"};
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


    /** An array of valid passwords.
     *  Password constraints are defined in InputValidation.validatePassword().
     *
     * @return - the array.
     */
    private static String[] validPasswords() {
        return new String[] {
                "aB34567%",
                "12345567fffGGG%$",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB!",
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!aB",
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAb$"};
    }


    /** An array of invalid passwords.
     *  Password constraints are defined in InputValidation.validatePassword().
     *
     * @return - the array.
     */
    private static String[] invalidPasswords() {
        return new String[] {
                "aB123456",
                "a123456[",
                "A123456[",
                "aB@",
                "aaaaaaaa",
                "aaaaaaaA",
                "aaaaaaa!",
                "aB",
                "a%",
                "A$",
                "",
                "a",
                "abcdefgh",
                "B",
                "!",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa[",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa%",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaB%",
                "aaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaL",
                "aaaaaaaaaaaaaaaaaaaa@"};
    }
}
