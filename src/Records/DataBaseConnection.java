package Records;
import java.sql.*;

/**
 * Created by andrewcabrera on 5/25/17.
 */
public class DataBaseConnection {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/andrewcabrera/Desktop/RecordsAppJava/src/Records/jdbc:sqlite:");

            return conn;
        } catch (Exception e) {
            return null;
        }
    }
}
