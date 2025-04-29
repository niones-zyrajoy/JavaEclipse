package ChapterOne;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloComputer009 extends Application {  
    @Override
    public void start(Stage stage) {  
        Label label = new Label("Welcome to COMP 009!");
        label.setFont(new Font("Arial", 20));
        
        Label label1 = new Label("Let's get it on!");
        label1.setFont(new Font("Arial", 30));
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, label1);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        
        Scene scene = new Scene(vbox, 400, 200);
        
        stage.setTitle("COMP 009");
        stage.setScene(scene);
        stage.show();
    } // Added missing closing bracket for start method

    public static void main(String[] args) {
        launch(args);
    }
} 