package com.example.account_res;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class OracleConnection {

    public static void main(String[] args) throws Exception {
        // Create jdcb connection object and load class.
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@computing.bournemouth.ac.uk:1521:decprd",
                "s5107294_y1",
                "breakout2020"
        );

        Statement statement = con.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM simple");

        while(rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
