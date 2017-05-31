package Records;

 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;

 import javafx.scene.control.TableColumn.CellEditEvent;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableView;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.control.cell.TextFieldTableCell;

 import javafx.event.EventHandler;
 import java.net.URL;
 import java.sql.*;
 import java.util.ResourceBundle;


/**
 * Created by andrewcabrera on 5/24/17.
 */
public class RecordOutputController  implements Initializable {

   private RecordModel recordModel = new RecordModel();


   //right tableView
    @FXML
   public TableView allDetailsForArtistView;

    //left Tableview
   @FXML
   public TableView<ArtistName> artistSelectorView;

   //list name for artistSelector
   @FXML
   private TableColumn artistNameCol;

   //List Quantity for ArtistSelector
   @FXML
   private TableColumn quantityCol;


    @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");
             artistNameCol.setCellValueFactory(new PropertyValueFactory<ArtistName,String>("name"));
             quantityCol.setCellValueFactory(new PropertyValueFactory<ArtistName, String>("quantity"));
             artistNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
             artistNameCol.setOnEditCommit(new EventHandler<CellEditEvent<ArtistName, String>>() {
                 @Override
                 public void handle(TableColumn.CellEditEvent<ArtistName, String> t) {
                     String oldName;
                     ArtistName artistChanged = null;
                     oldName = (t.getOldValue());
                     artistChanged  = ((ArtistName) t.getTableView().getItems().get(
                              t.getTablePosition().getRow())
                      );
                     artistChanged.setName(t.getNewValue());
                     recordModel.saveChangedItem(artistChanged, oldName);
                 }

             });
             quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());
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

   private void addNewEditableCell() {

   }




}
