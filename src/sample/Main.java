package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("Hello My TodoList");
        Scene scene = new Scene(root , 1000, 555);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init(){
        try {
            TodoDataFileSystem.getInstance().loadData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void stop() {
        try {
            TodoDataFileSystem.getInstance().writeToDisk();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
