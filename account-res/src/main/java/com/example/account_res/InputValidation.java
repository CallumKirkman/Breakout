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
     * @param email - the email to be validated against the regex.
     * @return - true if it meets the regex, false otherwise.
     */
    public static boolean validateEmail(String email) {
        String emailRegex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
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
     * @param password - the password to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validatePassword(String password) {
        String passwordRegex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*£]).{8,40})";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    /**
     * Check if the supplied String matches the following constraints:
     *      - No numbers.
     *      - No special characters.
     * @param name - the name to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validateName(String name) {
        String nameRegex = "^[a-zA-Z \\-.\']*$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }


    /**
     * Check if the supplied String is a valid Visa, MasterCard, or American Express card.
     * @param number - the card number to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validateCard(String number) {
        String visaRegex = "4[0-9]{12}(?:[0-9]{3})";
        Pattern visaPattern = Pattern.compile(visaRegex);
        if(visaPattern.matcher(number).matches()) { return true; }

        String masterCardRegex = "5[1-5][0-9]{14}";
        Pattern masterCardPattern = Pattern.compile(masterCardRegex);
       if(masterCardPattern.matcher(number).matches()) { return true; }

       String amexRegex = "3[47][0-9]{13}";
       Pattern amexPattern = Pattern.compile(amexRegex);
       if(amexPattern.matcher(number).matches()) { return true; }

       return false;
    }


    /**
     * Check if the supplied String is 3 or 4 numbers
     * @param number - the CVV to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validateCVV(String number) {
        String numberRegex = "[0-9]{3,4}";
        Pattern pattern = Pattern.compile(numberRegex);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }


    /**
     * Check if the supplied String is the correct date format
     * @param number - the card expiry date to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validateDate(String number) {
        String numberRegex = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$";
        Pattern pattern = Pattern.compile(numberRegex);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }
}
