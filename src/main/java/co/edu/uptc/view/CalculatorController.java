package co.edu.uptc.view;

import co.edu.uptc.control.BaseConverter;
import co.edu.uptc.control.ExpressionEvaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;


import java.io.IOException;


public class CalculatorController {

    @FXML
    private ComboBox<String> baseComboBox;


    @FXML
    private TextField expressionField;

    @FXML
    Button buttonConversor;

    @FXML
    private void initialize(){
        baseComboBox.getItems().addAll("Binario", "Octal", "Decimal", "Hexadecimal");
        baseComboBox.getSelectionModel().select("Decimal");
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
    private void handleNumber(ActionEvent event) {
        expressionField.appendText(((Button) event.getSource()).getText());
    }

    @FXML
    private void handleOperator(ActionEvent event) {
        expressionField.appendText(((Button) event.getSource()).getText());
    }

    @FXML
    private void handleClear(ActionEvent event) {
        expressionField.clear();
    }

    @FXML
    private void handleEquals(ActionEvent event) {
        String expression = expressionField.getText();
        if (!isValidExpression(expression)) {
            showError("La expresión no es válida");
            return;
        }
        try {
            double result = ExpressionEvaluator.evaluate(expression);
            expressionField.setText(String.valueOf(result));
        } catch (Exception e) {
            showError("Error en la expresión aritmética");
        }
    }

    @FXML
    private void handleSqrt(ActionEvent event) {
        String expression = expressionField.getText();
        try {
            double value = Double.parseDouble(expression);
            if (value < 0) {
                showError("No se puede calcular la raíz cuadrada de un número negativo");
                return;
            }
            double result = Math.sqrt(value);
            expressionField.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            showError("Entrada no válida para raíz cuadrada");
        }
    }

    // Validate the expression for correct parenthesis
    private boolean isValidExpression(String expression) {
        int openParenthesis = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                openParenthesis++;
            } else if (c == ')') {
                if (openParenthesis == 0) {
                    return false;
                }
                openParenthesis--;
            }
        }
        return openParenthesis == 0;
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

    @FXML
    private void handleConverter() throws IOException{
        Main.setRoot("conversor");

    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
