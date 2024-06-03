package co.edu.uptc.view;

import co.edu.uptc.control.ExpressionEvaluator;
import co.edu.uptc.control.BaseConverter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

public class CalculatorController {

    @FXML
    private ComboBox<String> baseComboBox;

    @FXML
    private ComboBox<String> convertToBaseComboBox;

    @FXML
    private TextField expressionField;

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
    private void handleNumber(javafx.event.ActionEvent event) {
        expressionField.appendText(((javafx.scene.control.Button) event.getSource()).getText());
    }

    @FXML
    private void handleOperator(javafx.event.ActionEvent event) {
        expressionField.appendText(((javafx.scene.control.Button) event.getSource()).getText());
    }

    @FXML
    private void handleEquals(javafx.event.ActionEvent event) {
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

    @FXML
    private void handleBaseConversion(javafx.event.ActionEvent event) {
        String baseFrom = baseComboBox.getValue();
        String baseTo = convertToBaseComboBox.getValue();
        String number = expressionField.getText();

        int fromBase = getBaseValue(baseFrom);
        int toBase = getBaseValue(baseTo);

        try {
            String convertedNumber = BaseConverter.convert(number, fromBase, toBase);
            expressionField.setText(convertedNumber);
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
}
