package co.edu.uptc.control;

import co.edu.uptc.model.Operation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalculatorController {

    private final List<Operation> history = new ArrayList<>();
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

    @FXML
    private ImageView historyImageView;

    @FXML
    private ImageView deleteImageView;

    @FXML
    private Button decButton;
    @FXML
    private Button hexButton;
    @FXML
    private Button binButton;
    @FXML
    private Button octButton;

    @FXML
    private Button button0;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    @FXML
    private Button buttonA;
    @FXML
    private Button buttonB;
    @FXML
    private Button buttonC;
    @FXML
    private Button buttonD;
    @FXML
    private Button buttonE;
    @FXML
    private Button buttonF;
    @FXML
    private Button buttonX;
    @FXML
    private Button buttonRaiz;
    @FXML
    private Button buttonPow;
    @FXML
    private Button buttonDot;



    private String currentBase = "DEC"; // Default base is decimal


    @FXML
    public void initialize() {
        expressionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String lastChar = String.valueOf(newValue.charAt(newValue.length() - 1));
                if (!isValidInput(lastChar)) {
                    expressionField.setText(oldValue);
                } else {
                    // Convert the entire text to uppercase
                    expressionField.setText(newValue.toUpperCase());
                }
            }
        });

        expressionField.setOnKeyPressed(event->{

            switch (event.getCode()){
                case ENTER -> handleEquals();
                case ESCAPE -> handleClear();
            }
        });
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(1.0); // Aumenta el brillo al máximo
        historyImageView.setEffect(colorAdjust);
        deleteImageView.setEffect(colorAdjust);

        updateBaseStyles();
    }


    @FXML
    private void handleNumber(ActionEvent event) {
        String number = ((Button) event.getSource()).getText();
        if (isValidInput(number)) {
            expressionField.appendText(number);
        }
    }

@FXML
private void handleOperator(ActionEvent event) {
    String operator = ((Button) event.getSource()).getText();
    if (!expressionField.getText().isEmpty()) {
        char lastChar = expressionField.getText().charAt(expressionField.getText().length() - 1);
        if (Character.isDigit(lastChar) || lastChar == ')' || (currentBase.equals("HEX") && ((lastChar >= 'A' && lastChar <= 'F') || (lastChar >= 'a' && lastChar <= 'f'))) || lastChar == '(') {
            expressionField.appendText(operator);
        }
    } else if (operator.equals("-")) {
        expressionField.appendText(operator);
    }
}

    @FXML
    private void handleEquals() {
        String expression = expressionField.getText();
        try {
            String result;
            switch (currentBase) {
                case "BIN" -> {
                    result = evaluateBinaryExpression(expression);
                    binField.setText(result);
                    updateBaseFields(Long.parseLong(result, 2));
                }

                case "HEX" -> {
                    result = evaluateHexadecimalExpression(expression);
                    hexField.setText(result);
                    updateBaseFields(Long.parseLong(result, 16));
                }
                case "OCT" -> {
                    result = evaluateOctalExpression(expression);
                    octField.setText(result);
                    updateBaseFields(Long.parseLong(result, 8));
                }
                default -> {
                    double resultValue = ExpressionEvaluator.evaluate(expression);
                    result = String.valueOf(resultValue);
                    decField.setText(result);
                    updateBaseFields(resultValue);
                }
            }
            expressionField.setText(result);
//            updateBaseFields(Double.parseDouble(result));
            history.add(new Operation(currentBase, expression, result));
        } catch (Exception e) {
            showError("Error en la expresión aritmética");
        }
    }

    @FXML
    private void handleBaseChange(ActionEvent event) {
        currentBase = ((Button) event.getSource()).getText();
        updateBaseStyles();
        updateExpressionBase();
    }

    private void updateBaseStyles() {
        resetNumberButtonStyles();
        switch (currentBase) {
            case "HEX" -> {
                hexButton.setStyle("-fx-background-color: #555555");
                decButton.setStyle("");
                octButton.setStyle("");
                binButton.setStyle("");
                enableHex();
            }
            case "OCT" -> {
                octButton.setStyle("-fx-background-color: #555555");
                hexButton.setStyle("");
                decButton.setStyle("");
                binButton.setStyle("");
                enableOctalNumberButtons();
            }
            case "BIN" -> {
                binButton.setStyle("-fx-background-color: #555555");
                hexButton.setStyle("");
                decButton.setStyle("");
                octButton.setStyle("");
                highlightBinaryButtons();
            }
            default -> {
                decButton.setStyle("-fx-background-color: #555555");
                hexButton.setStyle("");
                octButton.setStyle("");
                binButton.setStyle("");
                enableAllNumberButtons();
            }
        }
    }
    private void resetNumberButtonStyles() {
        button0.setStyle("");
        button1.setStyle("");
        button2.setStyle("");
        button3.setStyle("");
        button4.setStyle("");
        button5.setStyle("");
        button6.setStyle("");
        button7.setStyle("");
        button8.setStyle("");
        button9.setStyle("");
        buttonA.setStyle("");
        buttonB.setStyle("");
        buttonC.setStyle("");
        buttonD.setStyle("");
        buttonE.setStyle("");
        buttonF.setStyle("");
        buttonPow.setStyle("");
        buttonDot.setStyle("");
        buttonRaiz.setStyle("");
        buttonX.setStyle("");
    }
    private void enableAllNumberButtons() {
        button0.setDisable(false);
        button1.setDisable(false);
        button2.setDisable(false);
        button3.setDisable(false);
        button4.setDisable(false);
        button5.setDisable(false);
        button6.setDisable(false);
        button7.setDisable(false);
        button8.setDisable(false);
        button9.setDisable(false);
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
        buttonE.setDisable(true);
        buttonF.setDisable(true);
        buttonPow.setDisable(false);
        buttonDot.setDisable(false);
        buttonRaiz.setDisable(false);
        buttonX.setDisable(false);
    }
    private void enableOctalNumberButtons() {
        enableAllNumberButtons();
        button8.setDisable(true);
        button9.setDisable(true);
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
        buttonE.setDisable(true);
        buttonF.setDisable(true);
        buttonPow.setDisable(true);
        buttonDot.setDisable(true);
        buttonRaiz.setDisable(true);
        buttonX.setDisable(true);
    }
    private void highlightBinaryButtons() {
        button0.setDisable(false);
        button1.setDisable(false);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
        button5.setDisable(true);
        button6.setDisable(true);
        button7.setDisable(true);
        button8.setDisable(true);
        button9.setDisable(true);
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
        buttonE.setDisable(true);
        buttonF.setDisable(true);
        buttonPow.setDisable(true);
        buttonDot.setDisable(true);
        buttonRaiz.setDisable(true);
        buttonX.setDisable(false);
    }
    private void enableHex(){
        enableAllNumberButtons();
        buttonA.setDisable(false);
        buttonB.setDisable(false);
        buttonC.setDisable(false);
        buttonD.setDisable(false);
        buttonE.setDisable(false);
        buttonF.setDisable(false);
        buttonPow.setDisable(true);
        buttonDot.setDisable(true);
        buttonRaiz.setDisable(true);


    }

    @FXML
    private void handleSqrt() {
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

            history.add(new Operation(currentBase, "√" + expression, String.valueOf(result)));
        } catch (NumberFormatException e) {
            showError("Entrada no válida para raíz cuadrada");
        }
    }

    @FXML
    private void handleClear() {
        expressionField.clear();
        hexField.clear();
        decField.clear();
        octField.clear();
        binField.clear();
    }

    @FXML
    private void handleParenthesis(ActionEvent event) {
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

    @FXML
    private void handleDeleteButton() {
        String currentText = expressionField.getText();
        if (!currentText.isEmpty()) {
            expressionField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private boolean isValidInput(String input) {
        boolean isOperator = input.matches("[+\\-*/()^]");
        return isOperator || switch (currentBase) {
            case "BIN" -> input.equals("0") || input.equals("1");
            case "HEX" -> input.matches("[0-9A-Fa-f]");
            case "DEC" -> input.matches("[0-9.]");
            case "OCT" -> input.matches("[0-7]");
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
            case "OCT" -> 8;
            case "BIN" -> 2;
            default -> 10;
        };
    }


    private String evaluateBinaryExpression(String expression) {
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

    private String evaluateHexadecimalExpression(String expression) {
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

    private String evaluateOctalExpression(String expression) {
        StringBuilder decimalExpression = new StringBuilder();
        StringBuilder number = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (c >= '0' && c <= '7') {
                number.append(c);
            } else {
                if (!number.isEmpty()) {
                    decimalExpression.append(Integer.parseInt(number.toString(), 8));
                    number.setLength(0);
                }
                decimalExpression.append(c);
            }
        }

        if (!number.isEmpty()) {
            decimalExpression.append(Integer.parseInt(number.toString(), 8));
        }

        double result = ExpressionEvaluator.evaluate(decimalExpression.toString());

        return Integer.toOctalString((int) result);
    }


    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void showHistory() {
        // Create a new stage for the history window
        Stage historyStage = new Stage();
        historyStage.setTitle("Historial de Operaciones");
        // Create a ListView to show the history
        ListView<String> listView = new ListView<>();
        for (Operation operation : history) {
            listView.getItems().add("Base: " + operation.getBase() + "-->" + operation.getExpression() + " = " + operation.getResult());
        }
        // Create a Scene with the ListView and set it on the stage
        Scene scene = new Scene(listView, 360, 300);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/co/edu/uptc/view/history.css")).toExternalForm());
        historyStage.setScene(scene);
        // Show the history window
        historyStage.show();
    }
}

