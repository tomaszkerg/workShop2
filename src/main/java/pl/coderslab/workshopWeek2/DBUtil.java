package pl.coderslab.workshopWeek2;

import java.sql.*;

public class DBUtil {
    private final static String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private final static String DB_USER = "root";
    private final static String DB_PASS = "coderslab";
    private final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection conWorkshop() {
        Connection con = null;
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException exc) {  // brak klasy sterownika
            System.out.println("Brak klasy sterownika");
            System.out.println(exc);
            System.exit(1);
        } catch (SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + DB_URL);
            System.out.println(exc);
            System.exit(1);
        }
        return con;
    }
    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement =
                     conn.prepareStatement(tableName);) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            int counter = 1;
            while (resultSet.next()) {
                for (String param : columnNames) {
                    System.out.println(counter+". "+resultSet.getString(param));
                    counter++;
                }
                counter = 1;
                System.out.println();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
