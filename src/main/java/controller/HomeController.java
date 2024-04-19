package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private JFXButton btnNotifications;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnSettings;
    @FXML
    private JFXButton btnPlaceOrders;
    @FXML
    private JFXButton btnCustomers;
    @FXML
    private JFXButton btnOrders;
    @FXML
    private JFXButton btnItems;
    @FXML
    private JFXButton btnDashboard;

    public void notificationsButtonOnAction() {
    }

    public void logoutButtonOnAction() {
    }

    public void editButtonOnAction() {
    }

    public void settingsButtonOnAction() {
    }

    public void viewPlaceOrder(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/PlaceOrders.fxml"))));
        stage.show();
    }
    public void viewOrders(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Orders.fxml"))));
        stage.show();
    }
    public void viewCustomers(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Customers.fxml"))));
        stage.show();
    }
    public void viewItems(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Items.fxml"))));
        stage.show();
    }

    public void viewHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
        stage.show();
    }

    public void placeOrdersButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewPlaceOrder(actionEvent);
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewOrders(actionEvent);
    }

    public void customersButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewCustomers(actionEvent);
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewItems(actionEvent);
    }

    public void dashboardButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewHome(actionEvent);
    }
}
