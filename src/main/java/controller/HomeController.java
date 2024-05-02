package controller;

import com.jfoenix.controls.JFXButton;
import dao.custom.OrderDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Label lblTodaySales;
    public Label lblMonthlySales;
    public Label lblWeeklySales;
    public JFXButton btnCategories;
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

    private OrderDAO orderDAO = DAOFactory.getInstance().getDao(DAOType.ORDER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // For today's sales
        double todaySales = orderDAO.getTotalSalesOfCurrentDay();
        String formattedTodaySales = formatSales(todaySales);
        lblTodaySales.setText("Rs. " + formattedTodaySales);

// For weekly sales
        try {
            double weeklySales = orderDAO.getTotalSalesOfCurrentWeek();
            String formattedWeeklySales = formatSales(weeklySales);
            lblWeeklySales.setText("Rs. " + formattedWeeklySales);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

// For monthly sales
        try {
            double monthlySales = orderDAO.getTotalSalesOfCurrentMonth();
            String formattedMonthlySales = formatSales(monthlySales);
            lblMonthlySales.setText("Rs. " + formattedMonthlySales);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private String formatSales(double sales) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00"); // Format with two decimal points and comma separators
        return df.format(sales);
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
    public void viewCategories(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Categories.fxml"))));
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
    public void categoriesButtonOnAction(ActionEvent actionEvent) throws IOException {
        viewCategories(actionEvent);
    }

    public void notificationsButtonOnAction() {
    }

    public void logoutButtonOnAction() {
    }

    public void editButtonOnAction() {
    }

    public void settingsButtonOnAction() {
    }


}
