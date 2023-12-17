package sejahterainformationsystem;
import db.DBHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SejahteraInformationSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.getIcons().add(new Image(SejahteraInformationSystem.class.getResourceAsStream("icon.png"))); 
        stage.setTitle("Sejahtera Restaurant Ordering System");
        
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
        
        // Font.loadFont(getClass().getResourceAsStream("/path/to/Montserrat-Regular.ttf"), 12);
        // stage.setWidth(1000);
        // stage.setHeight(625);
     
    }
    
    public static void main(String[] args) {
        DBHelper.getConnection();
        launch(args);
    }
    
}
