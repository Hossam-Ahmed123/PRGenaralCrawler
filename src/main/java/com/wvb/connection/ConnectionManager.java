package com.wvb.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;




public class ConnectionManager {
//
//    private final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
//    private static Connection connection;
//    private final static String user = "WORLDVEST_DEV";
//    private final static String pass = "wvb";
//
//    public static Connection connectDataBase() {
//        try {
//            System.out.println("Database up....");
//            System.out.println("pass " + pass);
//            Class.forName(DRIVER);
//
//            try {
////                connection = DriverManager.getConnection("jdbc:oracle:thin:@//192.168.1.148:1521/WVBDEV", user, pass);
//                connection = DriverManager.getConnection("jdbc:oracle:thin:@//192.168.1.148:1521/wvbdev.us.wvb.com", user, "wvb");
////
//            } catch (SQLException ex) {
//                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } catch (ClassNotFoundException ex) {
//            System.out.println("Exception DB Connection " + ex);
//        }
//        return connection;
//    }
//
//    

    public static void main(String[] args) throws SQLException {
        Connection con = connectDataBase();

        System.out.println(con.getSchema());
    }
    private final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final static String conn = "jdbc:oracle:thin:@//64.244.240.214:1521/WVBNEWS"; //jdbc:oracle:thin:@//192.168.1.148:1521/wvbdev.us.wvb.com- jdbc:oracle:thin:@//64.244.240.214:1521/WVBNEWS

    private final static String user = "PR";
    private final static String pass = "pr4wvb";//wvb pr4wvb 
    private static Connection connection;

    public static Connection connectDataBase() {

        try {
            System.out.println("Database up....");

            Class.forName(DRIVER);

            connection = DriverManager.getConnection(conn, user, pass);

        } catch (ClassNotFoundException ex) {
            System.out.println("Exception DB Connection " + ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);

        }

        return connection;
    }

    public static String SavePath() {
        return "E:\\pdfs\\";
    }

    public static String GetDriverPath() {
        return "E:\\chrom\\chromedriver.exe";
    }

}
