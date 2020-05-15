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
        String emailRegex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" ;
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    /**
     * Check if the supplied String matches the following constraints:
     *      - 8 to 40 characters long.
     *      - contains at least one lowercase letter.
     *      - contains at least one uppercase letter.
     *      - contains at least one number.
     *      - contains at least one special character [ @ # $ % ! . ].
     *
     * @param password - the password to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validatePassword(String password) {
        String passwordRegex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    /**
     * Check if the supplied String matches the following constraints:
     *      - No numbers.
     *      - No special characters EXCLUDING - . and '.
     *
     * @param name - the name to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validateName(String name) {
        String nameRegex = "^[a-zA-Z \\-.\']*$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
}
