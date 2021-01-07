package cms;

import java.sql.*;

public class dao {

    public static void getListAccount(String state) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM account where state='" + state + "';";
                if (!state.equals("online") && state.equals("offline"))
                    query = "SELECT * FROM account;";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String address = rs.getString("ip");
                    String accState = rs.getString("state");
                    String key = rs.getString("public_key");

                    System.out.println("ID = " + id);
                    System.out.println("NAME = " + name);
                    System.out.println("IP = " + address);
                    System.out.println("STATE = " + accState);
                    System.out.println("KEY = " + key);
                    System.out.println();
                }
                rs.close();
                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void initDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM account;");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("id");
                    String address = rs.getString("ip");
                    String salary = rs.getString("state");
                    String key = rs.getString("public_key");

                    System.out.println("ID = " + id);
                    System.out.println("NAME = " + name);
                    System.out.println("AGE = " + age);
                    System.out.println("ADDRESS = " + address);
                    System.out.println("SALARY = " + salary);
                    System.out.println("KEY = " + key);
                    System.out.println();
                }
                rs.close();
                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
