package cms;

import com.google.gson.JsonObject;
import sercure.Signing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dao {

    public static dto findByName(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM account where name='" + name + "';";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    dto acc = new dto();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setIp(rs.getString("ip"));
                    acc.setState(rs.getString("state"));
                    acc.setPublic_key(rs.getString("public_key"));

                    rs.close();
                    stmt.close();

                    conn.close();
                    return acc;
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static dto signup(dto acc) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "INSERT INTO account (name, pass, ip, state, public_key, token) VALUES ("
                        + "'" + acc.getName() + "', "
                        + "'" + acc.getPass() + "', "
                        + "'" + acc.getIp() + "', "
                        + "'" + acc.getState() + "', "
                        + "'" + acc.getPublic_key() + "', "
                        + "'" + acc.getToken() + "'"
                        + ");";
                stmt.executeUpdate(query);

                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return acc;
    }

    public static dto login(String name, String pass) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM account where " +
                        "name='" + name + "'" +
                        " AND pass='" + pass + "';";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    dto acc = new dto();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setIp(rs.getString("ip"));
                    acc.setState(rs.getString("state"));
//                    acc.setPublic_key(rs.getString("public_key"));

                    System.out.println("ID = " + acc.getId());
                    System.out.println("NAME = " + acc.getName());
                    System.out.println("IP = " + acc.getIp());
                    System.out.println("STATE = " + acc.getState());
//                    System.out.println("KEY = " + acc.getPublic_key());
                    System.out.println();

                    rs.close();
                    stmt.close();

                    conn.close();
                    return acc;
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static dto updateState(dto acc) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "UPDATE account SET state='" + acc.getState() + "' WHERE id=" + acc.getId() + ";";
                stmt.executeUpdate(query);

                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static dto updateIP(dto acc) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "UPDATE account SET ip='" + acc.getIp() + "' WHERE id=" + acc.getId() + ";";
                stmt.executeUpdate(query);

                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static dto updateToken(dto acc) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "UPDATE account SET token='" + acc.getToken() + "', "
                        + "cer='" + acc.getCer() + "' "
                        + "WHERE id=" + acc.getId() + ";";
                stmt.executeUpdate(query);

                stmt.close();

                conn.close();

                return acc;
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static dto updateKey(dto acc) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "UPDATE account SET public_key='" + acc.getPublic_key() + "' WHERE id=" + acc.getId() + ";";
                stmt.executeUpdate(query);

                query = "UPDATE account SET cer='" + acc.getCer() + "' WHERE id=" + acc.getId() + ";";
                stmt.executeUpdate(query);

                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<dto> getListAccount(String state) {
        List<dto> rsList = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM account where state='" + state + "';";
                if (state.equals(""))
                    query = "SELECT * FROM account;";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    dto acc = new dto();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setIp(rs.getString("ip"));
                    acc.setState(rs.getString("state"));
                    acc.setPublic_key(rs.getString("public_key"));

//                    System.out.println("ID = " + acc.getId());
//                    System.out.println("NAME = " + acc.getName());
//                    System.out.println("IP = " + acc.getIp());
//                    System.out.println("STATE = " + acc.getState());
//                    System.out.println("KEY = " + acc.getPublic_key());
//                    System.out.println();

                    rsList.add(acc);
                }
                rs.close();
                stmt.close();

                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return rsList;
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
