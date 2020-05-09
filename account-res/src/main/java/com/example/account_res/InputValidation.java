package com.example.account_res;


/** Utility class of static methods for validating user input
 * both locally and against the remote storage.
 *
 */
public class InputValidation {

    /** Check if the supplied String matches the email regular expression.
     *
     * @param email - the email to be validated against the regex.
     * @return - true if it meets the regex, false otherwise.
     */
    public static boolean validateEmail(String email) {
        // TODO: Compare against a regex for an email.
        //  I.E. [chars]@[chars].[chars]
        return true;
    }


    /** Check if the supplied String matches the password constraints.
     *
     * @param password - the password to be validated.
     * @return - true if it meets the constraints, false otherwise.
     */
    public static boolean validatePassword(String password) {
        // TODO: Compare against a regex (or use other methods) for a password.
        //  I.E. contains uppercase, lowercase, number, etc.
        return true;
    }


    /** Retrieve the user's password from the database.
     *
     * @param email - the account's email.
     * @return - the password.
     */
    public static String retrievePassword(String email) {
        //TODO: Query the DB for the user's (hashed) password. Return it.
        return "";
    }


    /** Retrieve the user's password salt from the database.
     *
     * @param email - the account's email.
     * @return - the password salt.
     */
    public static String retrieveSalt(String email) {
        // TODO: Query the DB for the user's salt. Return it.
        return "";
    }
}
