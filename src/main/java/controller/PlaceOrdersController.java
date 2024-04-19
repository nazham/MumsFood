package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.custom.CustomerDAO;
import dao.util.BOType;
import dao.util.DAOFactory;
import dao.util.DAOType;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import dao.custom.OrderDAO;
import dao.custom.impl.OrderDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrdersController implements Initializable {
    public JFXButton btnNewCustomer;
    public JFXTextField txtPhnNum;
    public JFXTextField txtAddress;
    public Label lblSubTotal;
    public Label lblDiscount;
    public JFXTextField txtDiscount;
    public JFXComboBox cmbOrderType;
    public JFXTextField txtTableNum;
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
    private JFXButton btnEdit;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TableView<OrderTM> tblOrders;

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
    private JFXComboBox cmbItemCode;

    @FXML
    private JFXTextField txtName;
    @FXML
    private Label lblOrderId;
    @FXML
    private Label lblTotal;

    private CustomerBO customerBO = BOFactory.getInstance().getBo(BOType.CUSTOMER);
    private ItemBO itemBO = BOFactory.getInstance().getBo(BOType.ITEM);

    private CustomerDAO customerDAO = DAOFactory.getInstance().getDao(DAOType.CUSTOMER);
    private List<ItemDTO> items;
    private List<CustomerDTO> customers;

    private ObservableList<OrderTM> tmList = FXCollections.observableArrayList();
    private OrderDAO orderDAO = new OrderDAOImpl();

    private CustomerDTO customer;

    private double subTotal = 0.0;
    private double total = 0.0;
    private double discount = 0.0;


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
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/PlaceOrders.fxml"))));
        stage.show();
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Orders.fxml"))));
        stage.show();
    }

    public void customersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Customers.fxml"))));
        stage.show();
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Items.fxml"))));
        stage.show();
    }

    public void dashboardButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
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


        txtPhnNum.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    searchPhoneNumber();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newValue) -> {
            ItemDTO matchedItem = null;
            if (newValue != null) {matchedItem = findItemByCode(newValue.toString());}
            if (matchedItem != null) {
                txtDescription.setText(matchedItem.getDesc());
                txtUnitPrice.setText(String.format("%.2f", matchedItem.getUnitPrice()));
            }
        });

        setOrderId();
    }

    private ItemDTO findItemByCode(String code) {
        for (ItemDTO itemDTO : items) {
            if (itemDTO.getCode().equals(code)) {
                return itemDTO;
            }
        }
        return null;
    }

    private void searchPhoneNumber() throws SQLException, ClassNotFoundException {
        String phoneNumber = txtPhnNum.getText();

        customer = customerDAO.searchCustomer(phoneNumber);

        if (customer != null) {
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Customer Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No customer found with the given phone number.");
            alert.showAndWait();
            clearFields();
        }
    }

    private void loadItemCodes() {
        ObservableList list = FXCollections.observableArrayList();

        for (ItemDTO itemDTO : items) {
            list.add(itemDTO.getCode());
        }

        cmbItemCode.setItems(list);
    }

    private void setOrderId() {
        try {
            String lastOrderId = orderDAO.getLastOrderId();
            if (lastOrderId == null || lastOrderId.isEmpty()) {
                lblOrderId.setText("ODR#00001");
            } else {
                int num = Integer.parseInt(lastOrderId.substring(4)) + 1; // Extract the number part and increment
                lblOrderId.setText("ODR#" + String.format("%05d", num));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void placeOrderButtonOnAction() {
        List<OrderDetailDTO> list = new ArrayList<>();
        for (OrderTM orderTM : tmList) {
            list.add(new OrderDetailDTO(
                    lblOrderId.getText(),
                    findItemByCode(orderTM.getCode()).getId(),
                    orderTM.getQty(),
                    orderTM.getAmount()/orderTM.getQty()
            ));
        }

        OrderDTO dto = new OrderDTO(
                lblOrderId.getText(),
                LocalDateTime.now(),
                customer.getId(),
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
                cmbItemCode.getValue().toString(),
                txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtUnitPrice.getText())*Integer.parseInt(txtQty.getText()),
                btn
        );
        btn.setOnAction(actionEvent -> {
            tmList.remove(orderTM);
            subTotal -=orderTM.getAmount();
            lblSubTotal.setText(String.format("%.2f", subTotal));
            tblOrders.refresh();
            setDiscount();

        });
        boolean isExist = false;
        for (OrderTM order : tmList) {
            if (order.getCode().equals(orderTM.getCode())){
                order.setQty(order.getQty()+orderTM.getQty());
                order.setAmount(order.getAmount()+orderTM.getAmount());
                isExist = true;
                subTotal += orderTM.getAmount();
            }
        }
        if (!isExist){
            tmList.add(orderTM);
            subTotal +=orderTM.getAmount();
        }

        lblSubTotal.setText(String.format("%.2f", subTotal));

        tblOrders.setItems(tmList);
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        cmbItemCode.getSelectionModel().clearSelection();
        txtPhnNum.clear();
        cmbOrderType.getSelectionModel().clearSelection();
        txtDiscount.clear();
        tmList.clear();
        tblOrders.refresh();
        lblTotal.setText("0.00");
        lblSubTotal.setText("0.00");
        lblDiscount.setText("0.00");
        setOrderId();
    }

    public void txtPhnNumOnAction(ActionEvent actionEvent) {
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
    }

    private void setDiscount(){
        total=subTotal;
        discount = Double.parseDouble(txtDiscount.getText());
        discount /=100.00;
        discount *= subTotal;
        lblDiscount.setText(String.format("%.2f", discount));
        total -= discount;
        lblTotal.setText(String.format("%.2f", total));
    }

    public void txtDiscountOnAction(ActionEvent actionEvent) {
        setDiscount();
    }
}
