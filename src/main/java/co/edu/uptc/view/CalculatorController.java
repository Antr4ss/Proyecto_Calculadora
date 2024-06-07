package co.edu.uptc.view;

import co.edu.uptc.control.BaseConverter;
import co.edu.uptc.control.ExpressionEvaluator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CalculatorController {

    @FXML
    private TextField hexField;

    @FXML
    private TextField decField;

    @FXML
    private TextField octField;

    @FXML
    private TextField binField;

    @FXML
    private TextField expressionField;

    private String currentBase = "DEC"; // Default base is decimal

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        String number = ((Button) event.getSource()).getText();
        if (isValidInput(number)) {
            expressionField.appendText(number);
        }
    }

    @FXML
    private void handleOperator(javafx.event.ActionEvent event) {
        String operator = ((Button) event.getSource()).getText();
        if (operator.equals("-") && expressionField.getText().isEmpty()) {
            expressionField.appendText(operator);
        } else if (!expressionField.getText().isEmpty()) {
            char lastChar = expressionField.getText().charAt(expressionField.getText().length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                expressionField.appendText(operator);
            }
        }
    }

    @FXML
    private void handleEquals(javafx.event.ActionEvent event) {
        String expression = expressionField.getText();
        try {
            String result;
            if (currentBase.equals("BIN")) {
                result = evaluateBinaryExpression(expression);
            } else if (currentBase.equals("HEX")) {
                result = evaluateHexadecimalExpression(expression);
            } else {
                double resultValue = ExpressionEvaluator.evaluate(expression);
                result = String.valueOf(resultValue);
            }
            expressionField.setText(result);
            updateBaseFields(Double.parseDouble(result));
        } catch (Exception e) {
            showError("Error en la expresión aritmética");
        }
    }

    @FXML
    private void handleBaseChange(javafx.event.ActionEvent event) {
        currentBase = ((Button) event.getSource()).getText();
        updateExpressionBase();
    }

    @FXML
    private void handleSqrt(javafx.event.ActionEvent event) {
        String expression = expressionField.getText();
        try {
            double value = Double.parseDouble(expression);
            if (value < 0) {
                showError("No se puede calcular la raíz cuadrada de un número negativo");
                return;
            }
            double result = Math.sqrt(value);
            expressionField.setText(String.valueOf(result));
            updateBaseFields(result);
        } catch (NumberFormatException e) {
            showError("Entrada no válida para raíz cuadrada");
        }
    }

    @FXML
    private void handleClear(javafx.event.ActionEvent event) {
        expressionField.clear();
        hexField.clear();
        decField.clear();
        octField.clear();
        binField.clear();
    }

    @FXML
    private void handleParenthesis(javafx.event.ActionEvent event) {
        String text = ((Button) event.getSource()).getText();
        String currentText = expressionField.getText();

        // Add multiplication if needed
        if (text.equals("(") && !currentText.isEmpty()) {
            char lastChar = currentText.charAt(currentText.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                expressionField.appendText("*");
            }
        }

        expressionField.appendText(text);
    }

    private boolean isValidInput(String input) {
        return switch (currentBase) {
            case "BIN" -> input.equals("0") || input.equals("1");
            case "HEX" -> input.matches("[0-9A-Fa-f-]");
            case "DEC" -> input.matches("[0-9-]");
            case "OCT" -> input.matches("[0-7-]");
            default -> true;
        };
    }

    private void updateBaseFields(double value) {
        int intValue = (int) value;
        hexField.setText(BaseConverter.convert(String.valueOf(intValue), 10, 16));
        decField.setText(String.valueOf(intValue));
        octField.setText(BaseConverter.convert(String.valueOf(intValue), 10, 8));
        binField.setText(BaseConverter.convert(String.valueOf(intValue), 10, 2));
    }

    private void updateExpressionBase() {
        String expression = expressionField.getText();
        if (expression.isEmpty()) {
            return;
        }
        try {
            long value = Long.parseLong(expression, getBase(currentBase));
            switch (currentBase) {
                case "HEX":
                    expressionField.setText(Long.toHexString(value).toUpperCase());
                    break;
                case "DEC":
                    expressionField.setText(String.valueOf(value));
                    break;
                case "OCT":
                    expressionField.setText(Long.toOctalString(value));
                    break;
                case "BIN":
                    expressionField.setText(Long.toBinaryString(value));
                    break;
            }
        } catch (NumberFormatException e) {
            showError("Entrada no válida para la conversión de base");
        }
    }

    private int getBase(String base) {
        return switch (base) {
            case "HEX" -> 16;
            case "DEC" -> 10;
            case "OCT" -> 8;
            case "BIN" -> 2;
            default -> 10;
        };
    }

    private String evaluateBinaryExpression(String expression) throws Exception {
        StringBuilder decimalExpression = new StringBuilder();
        StringBuilder number = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (c == '0' || c == '1') {
                number.append(c);
            } else {
                if (!number.isEmpty()) {
                    decimalExpression.append(Integer.parseInt(number.toString(), 2));
                    number.setLength(0);
                }
                decimalExpression.append(c);
            }
        }

        if (!number.isEmpty()) {
            decimalExpression.append(Integer.parseInt(number.toString(), 2));
        }

        double result = ExpressionEvaluator.evaluate(decimalExpression.toString());

        return Integer.toBinaryString((int) result);
    }

    private String evaluateHexadecimalExpression(String expression) throws Exception {
        StringBuilder decimalExpression = new StringBuilder();
        StringBuilder number = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
                number.append(c);
            } else {
                if (!number.isEmpty()) {
                    decimalExpression.append(Integer.parseInt(number.toString(), 16));
                    number.setLength(0);
                }
                decimalExpression.append(c);
            }
        }

        if (!number.isEmpty()) {
            decimalExpression.append(Integer.parseInt(number.toString(), 16));
        }

        double result = ExpressionEvaluator.evaluate(decimalExpression.toString());

        return Integer.toHexString((int) result).toUpperCase();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

