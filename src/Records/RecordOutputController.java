package Records;

 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;

 import javafx.scene.control.*;
 import javafx.scene.control.TableColumn.CellEditEvent;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.control.cell.TextFieldTableCell;

 import javafx.event.EventHandler;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;

 import java.net.URL;
 import java.sql.*;
 import java.util.Comparator;
 import java.util.ResourceBundle;


/**
 * Created by andrewcabrera on 5/24/17.
 */
public class RecordOutputController  implements Initializable {

   private RecordModel recordModel = new RecordModel();
   private MenuItem men = new MenuItem("Delete");
   private ContextMenu deleteItemFromSelectorView = new ContextMenu(men);
   private MenuItem men2  = new MenuItem("Delete");
   private ContextMenu deleteItemFromDetailsView = new ContextMenu(men2);

   //lists all albums and informatin for Artist
    @FXML
   public TableView<RecordItems> allDetailsForArtistView;

    //lists Artists in the DB
   @FXML
   public TableView<ArtistName> artistSelectorView;

   //List the names of the artists
   @FXML
   private TableColumn<ArtistName, String> artistNameCol;

   //List Quantity for ArtistSelector
   @FXML
   private TableColumn<ArtistName, String> quantityCol;

   //number of copies of a record
   @FXML
   private TableColumn<RecordItems, String> Quantity;

   //year album was acquired
   @FXML
   private TableColumn<RecordItems, String> Year;

   //description of Record variant
    @FXML
    private TableColumn<RecordItems, String> Variant;

    //Album title
    @FXML
    private TableColumn<RecordItems, String> Album;


    @FXML
    private Label totalLabel;

    @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");

             artistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
             quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
             artistNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
             artistNameCol.setOnEditCommit(artistSelectorCellModified);
             artistNameCol.setContextMenu(deleteItemFromSelectorView);

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
             Album.setContextMenu(deleteItemFromDetailsView);
             men.setOnAction(event ->  {recordModel.deleteArtistAndInformation(
                    artistSelectorView.getSelectionModel().getSelectedItem().getName());
                    updateArtistSelectorView();
                    clearDetailsForArtist();});
             men2.setOnAction(event -> {
                 String name = allDetailsForArtistView.getSelectionModel().getSelectedItem().getName();
                 recordModel.deleteRecordFromTable(allDetailsForArtistView.getSelectionModel().getSelectedItem());
                 updateAllDetailsForArtistView(name);
                 recordModel.checkAndSaveQuantities(name);
                 updateArtistSelectorView();
             });
             updateArtistSelectorView();
         } else {
             System.out.println("Not connected");
         }
   }

    /**
     *
     * @param artist
     */
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
               data.sort(Comparator.comparing(RecordItems::getAlbum));
               RecordItems recordItems = new RecordItems(" ", " ", " ", " ");
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

        int total = 0;
       ResultSet resultSet = recordModel.getArtistList();
       if (resultSet == null)
           System.out.println("could not update ArtistView");
       else try {
           while (resultSet.next()) {
               ArtistName artist = new ArtistName(resultSet.getString(1), resultSet.getString(2));
               total += Integer.parseInt(resultSet.getString(2));
               data.add(artist);
           }
                data.sort(Comparator.comparing(ArtistName::getName));
                ArtistName artist = new ArtistName("", "");
                data.add(artist);
           artistSelectorView.setItems(data);
           totalLabel.setText("Total Quantity: " + Integer.toString(total));
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    @FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getButton() == MouseButton.SECONDARY)
        {
            deleteItemFromSelectorView.show(artistSelectorView, event.getScreenX(), event.getScreenY());

        } else if (event.getClickCount() == 1) {
            updateAllDetailsForArtistView(artistSelectorView.getSelectionModel().getSelectedItem().getName());
            deleteItemFromSelectorView.hide();
        }
    }

    @FXML
    public void detailClickedItem(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            deleteItemFromDetailsView.show(allDetailsForArtistView, event.getScreenX(), event.getSceneY());
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
                    recordModel.createNewRow(t.getNewValue());
                    updateArtistSelectorView();
                }
            }

        }

    };

    private EventHandler artistItemCellModified = (EventHandler<CellEditEvent<RecordItems, String>>) t -> {
        int tempCount = 0;
        RecordItems changedItemObject = t.getRowValue();
        String albumName = changedItemObject.getAlbum();
        String value = t.getNewValue();

        switch (t.getTablePosition().getColumn())
        {
            case 0 :{
                changedItemObject.setAlbum(value);
                recordModel.saveNewArtistItem(changedItemObject, albumName, changedItemObject.getName());
                updateAllDetailsForArtistView(changedItemObject.getName());
            }
                break;
            case 1: {changedItemObject.setQuantity(value);
                recordModel.saveNewArtistItem(changedItemObject, albumName, changedItemObject.getName());
                recordModel.checkAndSaveQuantities(changedItemObject.getName());
                updateArtistSelectorView();
            }
                break;
            case 2: {
                changedItemObject.setYear(value);
                recordModel.saveNewArtistItem(changedItemObject, albumName, changedItemObject.getName());

            }
                break;
            case 3: {
                changedItemObject.setVariant(value);
                recordModel.saveNewArtistItem(changedItemObject, albumName, changedItemObject.getName());
            }
                break;
            default:
                System.out.println("item not set");
        }
    };

    private void clearDetailsForArtist() {
        allDetailsForArtistView.getItems().clear();
    }

}
