package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/automationsystemdb";

    //  Database credentials
    static final String USER = "psilos";
    static final String PASS = "psilos";
    static Connection conn;

    public static Connection Connect() {

        if(conn!=null){
            return conn;
        }

        if(conn==null){
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                return conn;
            } catch (Exception se) {
                //Handle errors for JDBC
              System.out.println("error to connect to db");
            }
        }
        return null;
    }

    public static void Disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
             e.printStackTrace();
        }
    }
}
