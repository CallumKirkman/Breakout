package com.example.account_res;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/** Represents a database instance. Connect to an Oracle database, run pre-written
 *  or custom queries.
 *
 */
class Database {

    private String h;
    private String u;
    private String p;
    private Connection connection;
    private Statement statement;


    Database(String s1, String s2, String s3) {
        h = s1;
        u = s2;
        p = s3;
    }

    boolean connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(h, u, p);
            statement = connection.createStatement();
            return true;
        }
        catch(SQLException | ClassNotFoundException exc) {
            return false;
        }
    }

     boolean disconnect() {
        try {
            connection.close();
            return true;
        }
        catch(SQLException exc) {
            return false;
        }
    }


    //---------- Queries ----------//

    /** Commit the database.
     *
     * @return - true if successful, false otherwise.
     */
    boolean commit() {
        try {
            statement.executeQuery("commit");
            return true;
        }
        catch(SQLException exc) { return false; }
    }


    /** Rollback the database.
     *
     * @return - true if successful, false otherwise.
     */
    boolean rollback() {
        try {
            statement.executeQuery("rollback");
            return true;
        }
        catch(SQLException exc) { return false; }
    }


    /** Execute a query using the parameter as the query.
     *
     * @param q - the SQL query as a String.
     * @return - results of the query as ResultSet.
     * @throws SQLException - invalid SQL code, no connection, etc.
     */
    ResultSet customQuery(String q) throws SQLException {
        return statement.executeQuery(q);
    }


    /** Retrieve the user's password from the database.
     *
     * @param email - the account's email.
     * @return - the password (ResultSet). Or null if SQLException.
     */
     ResultSet retrievePassword(String email) {
        try {
            return statement.executeQuery(String.format
                    ("SELECT user_password FROM app_user WHERE user_email = %s", email));
        }
        catch(SQLException exc) {
            return null;
        }
    }

    /** Retrieve the user's password salt from the database.
     *
     * @param email - the account's email.
     * @return - the password salt (ResultSet). Or null if SQLException.
     */
     ResultSet retrieveSalt(String email) {
        try {
            return statement.executeQuery(String.format
                    ("SELECT user_salt FROM app_user WHERE user_email = %s", email));
        }
        catch(SQLException exc) {
            return null;
        }
    }
}
