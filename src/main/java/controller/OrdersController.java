package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.custom.OrderDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import db.DBConnection;
import dto.OrdersDTO;
import dto.tm.OrderDetailTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    public JFXButton btnCategories;
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
    public TableColumn colTimeStamp;
    public TableColumn colCustomer;
    public TableColumn colOrderType;
    public TableColumn colUser;
    public TableColumn colTotal;
    public TableColumn colPrint;
    public TableColumn colDelete;
    private final OrderDAO orderDAO = DAOFactory.getInstance().getDao(DAOType.ORDER);
    private final HomeController home = new HomeController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTimeStamp.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOrderType.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPrint.setCellValueFactory(new PropertyValueFactory<>("print"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<OrderDetailTM> tmList = FXCollections.observableArrayList();
        try {
            List<OrdersDTO> orderDTOList = orderDAO.getAllOrders(); // Fetch all orders with their details

            for (OrdersDTO orderDTO : orderDTOList) {
                JFXButton btnPrint = createPrintButton(orderDTO);
                JFXButton btnDelete = new JFXButton("Delete");

                OrderDetailTM orderDetailTM = new OrderDetailTM(
                        orderDTO.getDateTime().toString(), // Assuming this property exists in OrderDTO
                        orderDTO.getOrderId(),
                        orderDTO.getCustomer().getName(),
                        orderDTO.getOrderType(),
                        orderDTO.getUserId(),
                        orderDTO.getTotalAmount(),
                        btnPrint,
                        btnDelete
                );
                tmList.add(orderDetailTM);
            }

            tblOrders.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private JFXButton createPrintButton(OrdersDTO orderDTO) {
        JFXButton btnPrint = new JFXButton("Print");
        btnPrint.setOnAction(actionEvent -> {
            try {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("orderId", orderDTO.getOrderId());
                parameters.put("subTotal", orderDTO.getTotalAmount().toString());
                parameters.put("discount", "0.00");

                // Load and compile report templates
                JasperDesign design = JRXmlLoader.load(getClass().getResourceAsStream("/reports/bill.jrxml"));
                JasperReport jasperReport = JasperCompileManager.compileReport(design);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);

                if (!orderDTO.getOrderType().equals("Dine In")) {
                    JasperDesign kitchenDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/kitchen_bill.jrxml"));
                    JasperReport jasperReportKitchen = JasperCompileManager.compileReport(kitchenDesign);
                    JasperPrint jasperPrintKitchen = JasperFillManager.fillReport(jasperReportKitchen, parameters, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jasperPrintKitchen, false);
                }
            } catch (JRException | ClassNotFoundException | SQLException e) {
                TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to print bill: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return btnPrint;
    }
    public void notificationsButtonOnAction() { //yet to implement
    }

    public void logoutButtonOnAction() {    //yet to implement
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
    public void categoriesButtonOnAction(ActionEvent actionEvent) throws IOException {
        home.viewCategories(actionEvent);
    }
}
