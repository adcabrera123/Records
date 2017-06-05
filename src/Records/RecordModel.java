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

        try {
            String queryString = "UPDATE Artists set Name = ?, Copies = ? WHERE Artists.Name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, artist.getName());
            preparedStatement.setString(2, artist.getQuantity());
            preparedStatement.setString(3, name);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNewArtistItem(RecordItems items, String oldAlbum, String artist) {
        try {
            String queryString;
                 queryString = "UPDATE "+ artist +" set Album = ?, Year = ?, Variant = ?, Quantity = ?" +
                        " WHERE " + artist + ".Album = ?";


                PreparedStatement preparedStatement = connection.prepareStatement(queryString);

                preparedStatement.setString(1, items.getAlbum());
                preparedStatement.setString(2, items.getYear());
                preparedStatement.setString(3, items.getVariant());
                preparedStatement.setString(4, items.getQuantity());
                preparedStatement.setString(5, oldAlbum);
                preparedStatement.executeUpdate();
                preparedStatement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewTableForArtist(String artist) {
        try {
            String queryString = "Create Table " + artist + " (Quantity INT, Year INT, Variant CHAR(100), Album CHAR(100))";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewArtistToTable(String artist) {
        try {
            String queryString = "INSERT INTO Artists (Name, Copies) VALUES (?, 0)";

            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, artist);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void creatNewRow(String name) {
        try {
            String queryString = "INSERT INTO " + name + " (Quantity, Year, Variant, Album) VALUES ('', '', '', '')";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteArtistAndInformation(String artist) {
        String query = "DROP TABLE " + artist;
        String query1 = "DELETE FROM Artists WHERE Artists.Name == ?";

        try {
            PreparedStatement dropTable = connection.prepareStatement(query);
            dropTable.executeUpdate();

            PreparedStatement deleteFromArtistsTable = connection.prepareStatement(query1);
            deleteFromArtistsTable.setString(1, artist);
            deleteFromArtistsTable.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

}
