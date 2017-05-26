package Records;
 import javafx.fxml.Initializable;
 import javafx.scene.Parent;

 import java.net.URL;
 import java.sql.*;
 import java.util.ResourceBundle;

/**
 * Created by andrewcabrera on 5/24/17.
 */
public class RecordOutputController  implements Initializable {
   public RecordModel recordModel = new RecordModel();

   @Override
   public void initialize(URL location, ResourceBundle resources) {
         if (recordModel.isDbConnected()) {
             System.out.println("connection successful");
         } else {
             System.out.println("Not connected");
         }
   }
}
