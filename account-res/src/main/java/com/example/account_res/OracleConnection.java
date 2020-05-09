package com.example.account_res;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleConnection {

    /*

    public static void main(String[] args) throws Exception {
        // Create jdcb connection object and load class.
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@address:port:service",
                "name",
                "password"
        );

        // Create Statement
        Statement statement = con.createStatement();

        // Execute
        ResultSet rs = statement.executeQuery("SELECT * FROM simpletest");

        while(rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
     */
}
