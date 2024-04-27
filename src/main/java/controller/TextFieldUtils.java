package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;

public class TextFieldUtils {

    public static void setNumericInputFilter(JFXTextField textField, int maxLength) {
        // Apply numeric input filter to the provided textField
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d*") && newText.length() <= maxLength) { // Allows only digits and checks maximum length
                return change;
            }
            return null; // Reject change
        }));
    }
    public static void allowNumericAndPeriod(JFXTextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue.length() > maxLength) || (!newValue.matches("\\d*\\.?\\d*"))) {
                textField.setText(oldValue);
            }
        });
    }
    public static void allowNumericAndPeriod(JFXTextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    public static void allowAlphabeticInputOnly(JFXTextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                textField.setText(oldValue);
            }
        });
    }

    public static boolean isEmptyField(JFXTextField textField, String fieldName) {
        if (textField.getText().isEmpty()) {
            showAlert(fieldName + " field cannot be empty!");
            return true;
        }
        return false;
    }

    public static boolean isEmptyOrNonPositiveInteger(JFXTextField textField, String fieldName) {
        boolean isNonPositive = false;
        try {
            int value = Integer.parseInt(textField.getText());
            if (value <= 0) {
                showAlert(fieldName + " must be a positive integer!");
                isNonPositive = true;
            }
        } catch (NumberFormatException e) {
            showAlert(fieldName + " must be a valid integer!");
            isNonPositive = true;
        }
        if (isEmptyField(textField, fieldName) || isNonPositive) {
            return true;
        }

        return false;
    }
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isEmptyComboBox(JFXComboBox<?> comboBox, String fieldName) {
        if (comboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText(fieldName + " selection cannot be empty!");
            alert.showAndWait();
            return true;
        } else {
            return false;
        }
    }


}
