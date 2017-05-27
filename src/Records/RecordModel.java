package Records;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public ResultSet getArtistList() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery("SELECT * FROM Artists");
        } catch (SQLException e) {
            e.printStackTrace();
            resultSet = null;
        }
        return resultSet;
    }
}
