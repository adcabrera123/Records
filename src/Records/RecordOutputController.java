package Records;
 import javafx.beans.property.SimpleStringProperty;
 import javafx.beans.value.ObservableValue;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.Parent;
 import javafx.scene.control.ListView;
 import javafx.scene.control.SelectionMode;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableView;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.util.Callback;

 import java.net.URL;
 import java.sql.*;
 import java.util.ArrayList;
 import java.util.ResourceBundle;


/**
 * Created by andrewcabrera on 5/24/17.
 */
public class RecordOutputController  implements Initializable {
   private RecordModel recordModel = new RecordModel();


    @FXML
   public TableView allDetailsForArtistView;

   @FXML
   public TableView<ArtistName> artistSelectorView;

   @FXML
   private TableColumn artistNameCol;

   @FXML
   private TableColumn quantityCol;


    @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");
             artistNameCol.setCellValueFactory(new PropertyValueFactory<ArtistName,String>("name"));
             quantityCol.setCellValueFactory(new PropertyValueFactory<ArtistName, String>("quantity"));

             updateArtistSelectorView();
         } else {
             System.out.println("Not connected");
         }
   }

   public void updateAllDetailsForArtistView() {

   }


   private void updateArtistSelectorView() {
        ObservableList<ArtistName> data = FXCollections.observableArrayList();

       ResultSet resultSet = recordModel.getArtistList();
       if (resultSet == null)
           System.out.println("could not update ArtistView");
       else try {
           while (resultSet.next()) {
               ArtistName artist = new ArtistName(resultSet.getString(1), resultSet.getString(2));
               data.add(artist);
               artist.print();
           }

           artistSelectorView.setItems(data);
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
}
