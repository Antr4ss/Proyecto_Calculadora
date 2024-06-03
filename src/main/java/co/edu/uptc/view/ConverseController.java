package co.edu.uptc.view;

import co.edu.uptc.control.ExpressionEvaluator;
import co.edu.uptc.control.BaseConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

import java.io.IOException;


public class ConverseController {
    @FXML
    private ComboBox<String> baseComboBox;

    @FXML
    private ComboBox<String> convertToBaseComboBox;

    @FXML
    private TextField expressionField;

    @FXML
    private TextField resultField; // Nuevo campo para mostrar el resultado

    @FXML
    Button backToCalc;

    @FXML
    private void initialize() {
        baseComboBox.getItems().addAll("Binario", "Octal", "Decimal", "Hexadecimal");
        convertToBaseComboBox.getItems().addAll("Binario", "Octal", "Decimal", "Hexadecimal");
        baseComboBox.getSelectionModel().select("Decimal");
        convertToBaseComboBox.getSelectionModel().select("Decimal");

        // Add a key listener to the expression field
        expressionField.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
    }

    @FXML
    private void handleKeyTyped(KeyEvent event) {
        String base = baseComboBox.getValue();
        String input = event.getCharacter();

        // Validate the input based on the active base
        if (!BaseConverter.isValidNumberForBase(input, getBaseValue(base))) {
            event.consume();
        }
    }

    @FXML
    private void handleBaseConversion(javafx.event.ActionEvent event) {
        String baseFrom = baseComboBox.getValue();
        String baseTo = convertToBaseComboBox.getValue();
        String number = expressionField.getText();

        int fromBase = getBaseValue(baseFrom);
        int toBase = getBaseValue(baseTo);

        try {
            String convertedNumber = BaseConverter.convert(number, fromBase, toBase);
            resultField.setText(convertedNumber); // Actualizar el campo de resultado
        } catch (Exception e) {
            showError("Error en la conversión de bases: " + e.getMessage());
        }
    }

    private int getBaseValue(String base) {
        switch (base) {
            case "Binario":
                return 2;
            case "Octal":
                return 8;
            case "Decimal":
                return 10;
            case "Hexadecimal":
                return 16;
            default:
                throw new IllegalArgumentException("Base desconocida: " + base);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void backToCalc() throws IOException {
        Main.setRoot("calculator");
    }
}