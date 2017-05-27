package Records;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.Parent;
 import javafx.scene.control.ListView;
 import javafx.scene.control.SelectionMode;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableView;

 import java.net.URL;
 import java.sql.*;
 import java.util.ResourceBundle;

/**
 * Created by andrewcabrera on 5/24/17.
 */
public class RecordOutputController  implements Initializable {
   public RecordModel recordModel = new RecordModel();

   @FXML
   public TableView allDetailsForArtistView;

   @FXML
   public TableView artistSelectorView;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");

         } else {
             System.out.println("Not connected");
         }
   }

   public void updateAllDetailsForArtistView() {
        ResultSet resultSet = recordModel.getArtistList();
        if (resultSet == null)
            System.out.println("could not update ArtistView");
        else {

            try {
                while (resultSet.next())
                {
                    ObservableList<String> items = FXCollections.observableArrayList("Artist, Quantity");
                    ListView<String> lv = new ListView<>(items);
                    lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
   }

   public void updateArtistSelectorView() {

   }
}
