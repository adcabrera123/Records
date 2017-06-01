package Records;

import java.sql.*;

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

    public ResultSet getArtistItems(String artistTable) {
        Statement statement = null;


        try{
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery("SELECT * FROM '" + artistTable +"'");
        } catch (SQLException e) {
            e.printStackTrace();
            resultSet = null;
        }

        return resultSet;
    }

    public void saveChangedItem(ArtistName artist, String name) {
//        Statement statement = null;

        try {
//            statement = connection.createStatement();
            String queryString = "UPDATE Artists set Name = ?, Copies = ? WHERE Artists.Name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, artist.getName());
            preparedStatement.setString(2, artist.getQuantity());
            preparedStatement.setString(3, name);
            System.out.println(queryString);
            preparedStatement.executeUpdate();

            System.out.println("anem is :" + name);
            System.out.println("Artist anme is: " + artist.getName());
//            statement.executeUpdate("UPDATE Artists " +
//                    "set Name = '" + artist.getName() + "',  Copies = '" + artist.getQuantity() +"'" +
//                    " WHERE Artists.Name = '" + name +"'");
//            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNewArtistItem(RecordItems items, String oldAlbum, String artist) {
        try {
            String queryString = "UPDATE "+ artist +" set Album = ?, Year = ?, Variant = ?, Quantity = ?" +
                    "WHERE " + artist + ".Album = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, items.getAlbum());
            preparedStatement.setString(2, items.getYear());
            preparedStatement.setString(3, items.getVariant());
            preparedStatement.setString(4, items.getQuantity());
            preparedStatement.setString(5, oldAlbum);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
