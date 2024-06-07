package co.edu.uptc.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;
    static Scene sc;
    public static void main(String[] args) {
        launch();
    }
    public static  void setRoot(String fxml) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml + ".fxml")));
        primaryStage.setScene(new Scene(root));
    }

    public static void setResizable(boolean b) {
        primaryStage.setResizable(b);
    }


    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Calculadora");
        Image applicationIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/co/edu/uptc/view/images/Windows_Calculator_icon.png")));
        primaryStage.getIcons().add(applicationIcon);
        setRoot("calculator");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

