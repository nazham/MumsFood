package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.custom.OrderDetailDAO;
import dao.custom.impl.OrderDetailDAOImpl;
import dto.OrderDetailDTO;
import dto.tm.OrderDetailTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnItems;

    @FXML
    private JFXButton btnOrders;

    @FXML
    private JFXButton btnCustomers;

    @FXML
    private JFXButton btnPlaceOrders;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXButton btnNotifications;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<OrderDetailTM> tblOrders;

    @FXML
    private TableColumn colOrderId;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colUnitPrice;

    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();
    private final HomeController home = new HomeController();

    public void notificationsButtonOnAction() {
    }

    public void logoutButtonOnAction() {
    }

    public void editButtonOnAction() {
    }

    public void settingsButtonOnAction() {
    }

    public void placeOrdersButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewPlaceOrder(actionEvent);
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewOrders(actionEvent);
    }

    public void customersButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewCustomers(actionEvent);
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewItems(actionEvent);
    }

    public void dashboardButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewHome(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<OrderDetailTM> tmList = FXCollections.observableArrayList();
        try {
            List<OrderDetailDTO> dtoList = orderDetailDAO.getAll();
            for (OrderDetailDTO orderDetailDTO : dtoList) {
                OrderDetailTM orderDetailTM = new OrderDetailTM(
                        orderDetailDTO.getOrderId(),
                        orderDetailDTO.getItemId(),
                        orderDetailDTO.getQty(),
                        orderDetailDTO.getUnitPrice()
                );
                tmList.add(orderDetailTM);
            }

            tblOrders.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
