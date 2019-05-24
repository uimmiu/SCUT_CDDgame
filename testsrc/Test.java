import java.util.*;
import java.lang.*;
import java.sql.*;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Class.forName("org.mariadb.jdbc.Driver");
        System.out.println("Driver loaded");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/db_SPJ", "cddgame", "cddgame");
        } catch (Exception e) {
            System.err.print(e);
            System.exit(0);
        }
        System.out.println("works!");
        try {
            String sql = "select * from spj";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.print(rs.getString("SNO") + " ");
                System.out.print(rs.getString("PNO") + " ");
                System.out.print(rs.getString("JNO") + " ");
                System.out.println(rs.getString("QTY"));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        connection.close();
    }
}