package Records;

 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.event.ActionEvent;
 import javafx.event.Event;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.fxml.Initializable;

 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.TableColumn.CellEditEvent;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableView;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.control.cell.TextFieldTableCell;

 import javafx.event.EventHandler;
 import javafx.scene.input.MouseEvent;
 import javafx.stage.Stage;

 import java.io.IOException;
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
   public TableView<RecordItems> allDetailsForArtistView;

    //left Tableview
   @FXML
   public TableView<ArtistName> artistSelectorView;

   //list name for artistSelector
   @FXML
   private TableColumn<ArtistName, String> artistNameCol;

   //List Quantity for ArtistSelector
   @FXML
   private TableColumn<ArtistName, String> quantityCol;

   @FXML
   private TableColumn<RecordItems, String> Quantity;

   @FXML
   private TableColumn<RecordItems, String> Year;

    @FXML
    private TableColumn<RecordItems, String> Variant;

    @FXML
    private TableColumn<RecordItems, String> Album;

    @FXML
    private Button addNewArtistButton;




    @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");
             artistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
             quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
             artistNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
             artistNameCol.setOnEditCommit(artistSelectorCellModified);
             quantityCol.setOnEditCommit(artistSelectorCellModified);
             quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());

             Quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
             Year.setCellValueFactory(new PropertyValueFactory<>("Year"));
             Variant.setCellValueFactory(new PropertyValueFactory<>("Variant"));
             Album.setCellValueFactory(new PropertyValueFactory<>("Album"));
             Quantity.setCellFactory(TextFieldTableCell.forTableColumn());
             Quantity.setOnEditCommit(artistItemCellModified);
             Year.setCellFactory(TextFieldTableCell.forTableColumn());
             Year.setOnEditCommit(artistItemCellModified);
             Variant.setCellFactory(TextFieldTableCell.forTableColumn());
             Variant.setOnEditCommit(artistItemCellModified);
             Album.setCellFactory(TextFieldTableCell.forTableColumn());
             Album.setOnEditCommit(artistItemCellModified);

             addNewArtistButton.setOnAction(new EventHandler<>() {
              public void handle(ActionEvent event) {
                  Parent root;
                  try {
                      root = FXMLLoader.load(getClass().getResource("/Records/AddNewArtistPage.fxml"));
                      Stage stage = new Stage();
                      stage.setTitle("My New Stage Title");
                      stage.setScene(new Scene(root, 450, 450));
                      stage.show();
                  }
                  catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });

             updateArtistSelectorView();
         } else {
             System.out.println("Not connected");
         }
   }

   private void updateAllDetailsForArtistView(String artist) {
       ObservableList<RecordItems> data  = FXCollections.observableArrayList();

       if (artist != "") {
           ResultSet resultSet = recordModel.getArtistItems(artist);

           if (resultSet == null)
               System.out.println("could not update the Items");
           else try {
               while (resultSet.next()) {
                   RecordItems recordItems = new RecordItems(resultSet.getString(1), resultSet.getString(2),
                           resultSet.getString(3), resultSet.getString(4));
                   data.add(recordItems);
                   recordItems.setName(artist);
               } //end while
               RecordItems recordItems = new RecordItems("", "", "", "");
               recordItems.setName(artist);
               data.add(recordItems);
               allDetailsForArtistView.setItems(data);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       } else
           System.out.println("Empty Cell");
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
           }
                ArtistName artist = new ArtistName("", "");
                data.add(artist);
           artistSelectorView.setItems(data);
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    @FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getClickCount() == 1)
        {
            updateAllDetailsForArtistView(artistSelectorView.getSelectionModel().getSelectedItem().getName());

        }
    }


    private EventHandler artistSelectorCellModified = new EventHandler<CellEditEvent<ArtistName, String>>() {
        @Override
        public void handle(TableColumn.CellEditEvent<ArtistName, String> t) {
            ArtistName artistChanged = t.getRowValue();
            String oldName = artistChanged.getName();
            TableColumn col = t.getTableColumn();

            if (oldName != "")
            {
                if (col == artistNameCol)
                    artistChanged.setName(t.getNewValue());
                else
                    artistChanged.setQuantity(t.getNewValue());

                recordModel.saveChangedItem(artistChanged, oldName);
            } else if (oldName == "") {
                if (col == artistNameCol)
                {
                    recordModel.createNewTableForArtist(t.getNewValue());
                    recordModel.addNewArtistToTable(t.getNewValue());
                    recordModel.creatNewRow(t.getNewValue());
                    updateArtistSelectorView();
                }
            }

        }

    };

    private EventHandler artistItemCellModified = (EventHandler<CellEditEvent<RecordItems, String>>) t -> {
        RecordItems changedItemObject = t.getRowValue();
        String albumName = changedItemObject.getAlbum();
        String value = t.getNewValue();

        switch (t.getTablePosition().getColumn())
        {
            case 0 : changedItemObject.setAlbum(value);
                break;
            case 1: changedItemObject.setQuantity(value);
                break;
            case 2: changedItemObject.setYear(value);
                break;
            case 3: changedItemObject.setVariant(value);
                break;
            default:
            System.out.println("item not set");
        }
        recordModel.saveNewArtistItem(changedItemObject, albumName, changedItemObject.getName());
    };


}
