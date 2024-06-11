package co.edu.uptc.view;

import co.edu.uptc.control.CalculatorController;
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
    private static CalculatorController controller; // Add this field

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(fxml + ".fxml")));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        controller = loader.getController(); // Get the controller instance
    }


    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Calculadora");
        Image applicationIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/co/edu/uptc/view/images/Windows_Calculator_icon.png")));
        primaryStage.getIcons().add(applicationIcon);
        setRoot("calculator");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (controller != null) {
            controller.saveHistory(); // Save history when application is closing
        }
    }
}