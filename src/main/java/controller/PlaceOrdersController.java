package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.util.BOType;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.tm.OrderTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import dao.custom.OrderDAO;
import dao.custom.impl.OrderDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrdersController implements Initializable {
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
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TableView tblOrders;

    @FXML
    private TableColumn colCode;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colAmount;

    @FXML
    private TableColumn colOption;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXComboBox cmbxCustomerId;

    @FXML
    private JFXComboBox cmbxItemCode;

    @FXML
    private JFXTextField txtName;
    @FXML
    private Label lblOrderId;
    @FXML
    private Label lblTotal;

    private CustomerBO customerBO = BOFactory.getInstance().getBo(BOType.CUSTOMER);
    private ItemBO itemBO = BOFactory.getInstance().getBo(BOType.ITEM);
    private List<ItemDTO> items;
    private List<CustomerDTO> customers;

    private ObservableList<OrderTM> tmList = FXCollections.observableArrayList();
    private OrderDAO orderDAO = new OrderDAOImpl();

    private double total = 0.0;


    public void notificationsButtonOnAction() {
    }

    public void logoutButtonOnAction() {
    }

    public void editButtonOnAction() {
    }

    public void settingsButtonOnAction() {
    }

    public void placeOrdersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/PlaceOrders.fxml"))));
        stage.show();
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Orders.fxml"))));
        stage.show();
    }

    public void customersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Customers.fxml"))));
        stage.show();
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Items.fxml"))));
        stage.show();
    }

    public void dashboardButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Home.fxml"))));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        try {
            customers = customerBO.allCustomers();
            items = itemBO.allItems();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadItemCodes();
        loadCustomerIds();

        cmbxCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newValue) -> {
            for (CustomerDTO customerDTO : customers) {
                if (customerDTO.getId().equals(newValue.toString())){
                    txtName.setText(customerDTO.getName());
                }
            }
        });

        cmbxItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newValue) -> {
            for (ItemDTO itemDTO : items) {
                if (itemDTO.getCode().equals(newValue.toString())){
                    txtDescription.setText(itemDTO.getDesc());
                    txtUnitPrice.setText(String.format("%.2f",itemDTO.getUnitPrice()));
                }
            }
        });

        setOrderId();
    }



    private void loadItemCodes() {
        ObservableList list = FXCollections.observableArrayList();

        for (ItemDTO itemDTO : items) {
            list.add(itemDTO.getCode());
        }

        cmbxItemCode.setItems(list);
    }

    private void loadCustomerIds() {
        ObservableList list = FXCollections.observableArrayList();

        for (CustomerDTO customerDTO : customers) {
            list.add(customerDTO.getId());
        }

        cmbxCustomerId.setItems(list);
    }

    private void setOrderId() {
        try {
            String id = orderDAO.getLastOrderId();
            if (id.equals("D001")){
                lblOrderId.setText("D001");
                return;
            }
            int num = Integer.parseInt(id.split("[D]")[1]);
            num++;
            lblOrderId.setText(String.format("D%03d",num));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void placeOrderButtonOnAction() {
        List<OrderDetailDTO> list = new ArrayList<>();
        for (OrderTM orderTM : tmList) {
            list.add(new OrderDetailDTO(
                    lblOrderId.getText(),
                    orderTM.getCode(),
                    orderTM.getQty(),
                    orderTM.getAmount()/orderTM.getQty()
            ));
        }

        OrderDTO dto = new OrderDTO(
                lblOrderId.getText(),
                LocalDateTime.now(),
                cmbxCustomerId.getValue().toString(),
                Double.parseDouble(lblTotal.getText()),
                "Dine-In",
                "U001",
                list
        );


        try {
            boolean isSaved = orderDAO.save(dto);
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION, "Order saved!").show();
                setOrderId();
            }else{
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clearFields();
    }

    public void addToCartButtonOnAction() {
        JFXButton btn = new JFXButton("Delete");

        OrderTM orderTM = new OrderTM(
                cmbxItemCode.getValue().toString(),
                txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtUnitPrice.getText())*Integer.parseInt(txtQty.getText()),
                btn
        );
        btn.setOnAction(actionEvent -> {
            tmList.remove(orderTM);
            total-=orderTM.getAmount();
            lblTotal.setText(String.format("%.2f",total));
            tblOrders.refresh();
        });
        boolean isExist = false;
        for (OrderTM order : tmList) {
            if (order.getCode().equals(orderTM.getCode())){
                order.setQty(order.getQty()+orderTM.getQty());
                order.setAmount(order.getAmount()+orderTM.getAmount());
                isExist = true;
                total+= orderTM.getAmount();
            }
        }
        if (!isExist){
            tmList.add(orderTM);
            total+=orderTM.getAmount();
        }

        lblTotal.setText(String.format("%.2f",total));

        tblOrders.setItems(tmList);
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        cmbxItemCode.getSelectionModel().clearSelection();
        cmbxCustomerId.getSelectionModel().clearSelection();
        tmList.clear();
        tblOrders.refresh();
        lblTotal.setText("0.00");
        setOrderId();
    }
}
