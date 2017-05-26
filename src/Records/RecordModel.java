package Records;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by andrewcabrera on 5/25/17.
 */
public class RecordModel {
    Connection connection;
    public RecordModel() {
        connection = DataBaseConnection.Connector();
        if (connection == null) System.exit(1);
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
