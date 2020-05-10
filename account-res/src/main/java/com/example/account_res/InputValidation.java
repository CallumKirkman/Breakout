package com.example.account_res;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class of static methods for validating user input
 * both locally and against the remote storage.
 */
public class InputValidation {

    /**
     * Check if the supplied String matches the email regular expression.
     *
     * @param email - the email to be validated against the regex.
     * @return - true if it meets the regex, false otherwise.
     */
    public static boolean validateEmail(String email) {
        // completeTODO: Compare against a regex for an email.
        //  I.E. [chars]@[chars].[chars]
        //"^(.+)@(.+)$"


        String emailRegex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" ;
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * Check if the supplied String matches the password constraints.
     *
     * @param password - the password to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validatePassword(String password) {
        // TODO: Compare against a regex (or use other methods) for a password.
        //  I.E. contains uppercase, lowercase, number, etc.
        /**
         *     Be between 8 and 40 characters long
         *     Contain at least one digit.
         *     Contain at least one lower case character.
         *     Contain at least one upper case character.
         *     Contain at least on special character from [ @ # $ % ! . ].
         * */
        String passwordRegex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();//returns true if password meets requirements
    }

    /**
     * Retrieve the user's password from the database.
     *
     * @param email - the account's email.
     * @return - the password.
     */
    public static String retrievePassword(String email) {
        //TODO: Query the DB for the user's (hashed) password. Return it.
        return "";
    }

    /**
     * Retrieve the user's password salt from the database.
     *
     * @param email - the account's email.
     * @return - the password salt.
     */
    public static String retrieveSalt(String email) {
        // TODO: Query the DB for the user's salt. Return it.
        return "";
    }
}
