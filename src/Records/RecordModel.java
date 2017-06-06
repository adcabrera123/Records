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

    public void checkAndSaveQuantities(String artist) {
        if (getCountIntArtistTable(artist) != getRecordCount(artist))
        {
            String queryString = "UPDATE Artists SET Copies = ? WHERE Artists.Name == ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, Integer.toString(getRecordCount(artist)));
                preparedStatement.setString(2, artist);
                if(preparedStatement.executeUpdate() == 1)
                    System.out.println("count update successful");
                else
                    System.out.println("count update failed");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveNewArtistItem(RecordItems items, String oldAlbum, String artist) {
        try {
            String queryString;
                 queryString = "UPDATE '"+ artist + "' set Album = ?, Year = ?, Variant = ?, Quantity = ?" +
                        " WHERE '" + artist + "'.Album = ?";


                PreparedStatement preparedStatement = connection.prepareStatement(queryString);

                preparedStatement.setString(1, items.getAlbum());
                preparedStatement.setString(2, items.getYear());
                preparedStatement.setString(3, items.getVariant());
                preparedStatement.setString(4, items.getQuantity());
                preparedStatement.setString(5, oldAlbum);
                if (preparedStatement.executeUpdate() == 1)
                    System.out.println("successful save");
                else
                    createNewRow(items, artist);

                if (getRecordCount(artist) != getCountIntArtistTable(artist));
            {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewTableForArtist(String artist) {
        try {
            String queryString = "Create Table '" + artist + "' (Quantity INT, Year INT, Variant CHAR(100), Album CHAR(100))";
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
    public void createNewRow(String name) {
        try {
            String queryString = "INSERT INTO '" + name + "' (Quantity, Year, Variant, Album) VALUES ('', '', '', '')";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createNewRow (RecordItems items, String artist) {
        String query = "INSERT INTO '" + artist + "' (Quantity, Year, Variant, Album) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, items.getQuantity());
            preparedStatement.setString(2, items.getYear());
            preparedStatement.setString(3, items.getVariant());
            preparedStatement.setString(4, items.getAlbum());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteArtistAndInformation(String artist) {
        String query = "DROP TABLE '" + artist + "'";
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

    public void deleteRecordFromTable(RecordItems rec) {
        String query = "DELETE FROM '" + rec.getName() + "' WHERE '" + rec.getName() + "'.Album == ? " +
                "AND '" + rec.getName() + "'.variant == ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, rec.getAlbum());
            preparedStatement.setString(2, rec.getVariant());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getRecordCount(String artist) {
        int count = 0;
        String query = "SELECT Quantity FROM '" + artist + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                try {
                    count += Integer.parseInt(resultSet.getString(1));
                } catch (Exception e) {
                    count = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getCountIntArtistTable(String artist) {
        int count = 0;
        String query = "SELECT Copies FROM Artists WHERE Name == '" + artist + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                count = Integer.parseInt(resultSet.getString(1));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}
