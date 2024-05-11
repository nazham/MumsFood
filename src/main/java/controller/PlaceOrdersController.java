package controller;

import bo.BOFactory;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.custom.CustomerDAO;
import dao.custom.OrderDAO;
import dao.custom.impl.OrderDAOImpl;
import dao.util.BOType;
import dao.util.DAOFactory;
import dao.util.DAOType;
import db.DBConnection;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.tm.OrderTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceOrdersController implements Initializable {
    private final HomeController home = new HomeController();
    private final ItemBO itemBO = BOFactory.getInstance().getBo(BOType.ITEM);
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDao(DAOType.CUSTOMER);
    private final ObservableList<OrderTM> tmList = FXCollections.observableArrayList();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    public JFXButton btnNewCustomer;
    public JFXTextField txtPhnNum;
    public JFXTextField txtAddress;
    public Label lblSubTotal;
    public Label lblDiscount;
    public JFXTextField txtDiscount;
    public JFXComboBox cmbOrderType;
    public JFXTextField txtTableNum;
    public JFXButton btnPrintBill;
    public Label lblTableNum;
    @FXML
    private JFXButton btnCategories;
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
//    @FXML
//    private JFXComboBox<String> cmbItemCode;
    @FXML
    private JFXTextField txtName;
    @FXML
    private Label lblOrderId;
    @FXML
    private Label lblTotal;
    private List<ItemDTO> items;
    private CustomerDTO customer;
    private double subTotal = 0.0;
    private ObservableList<String> itemList;
    @FXML
    private SearchableComboBox<String> cmbItemCode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            items = itemBO.allItems();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadItemCodes();
        loadOrderTypes();

        txtDiscount.setText("0");
        txtTableNum.setVisible(false);
        lblTableNum.setVisible(false);

        btnAddToCart.setDisable(true);
        btnPlaceOrder.setDisable(true);
        btnPrintBill.setDisable(true);

        TextFieldUtils.setNumericInputFilter(txtPhnNum, 10);
        TextFieldUtils.setNumericInputFilter(txtQty, 3);
        TextFieldUtils.allowNumericAndPeriod(txtDiscount, 4);
        TextFieldUtils.setNumericInputFilter(txtTableNum, 2);

        txtName.setEditable(false);
        txtAddress.setEditable(false);
        txtDescription.setEditable(false);
        txtUnitPrice.setEditable(false);

        txtPhnNum.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    searchPhoneNumber();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        txtQty.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateQty();
            }
        });
        txtDiscount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateDiscount();
            }
        });
        cmbItemCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateCode();
            }
        });
        cmbOrderType.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateOrderType();
            }
        });

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newValue) -> {
            ItemDTO matchedItem = null;
            if (newValue != null) {
                matchedItem = findItemByCode(newValue.toString());
            }
            if (matchedItem != null) {
                txtDescription.setText(matchedItem.getDesc());
                txtUnitPrice.setText(String.format("%.2f", matchedItem.getUnitPrice()));
                txtQty.setText("1");
                btnAddToCart.setDisable(false);
            }
        });

        cmbOrderType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("Dine In")) {
                lblTableNum.setVisible(true);
                txtTableNum.setVisible(true);
                txtTableNum.clear();
            } else {
                txtTableNum.setVisible(false);
                lblTableNum.setVisible(false);
                txtTableNum.clear();
            }
        });

    }

    private boolean validateTableNum() {
        return TextFieldUtils.isEmptyOrNonPositiveInteger(txtTableNum, "Table Number");
    }

    private boolean validateCode() {
        return false;
//                TextFieldUtils.isEmptyComboBox(cmbItemCode, "Item Code");
    }

    private boolean validateOrderType() {return TextFieldUtils.isEmptyComboBox(cmbOrderType, "Order type");}

    private boolean validateQty() {
        return TextFieldUtils.isEmptyOrNonPositiveInteger(txtQty, "Item Quantity");
    }

    private boolean validateDiscount() {
        return TextFieldUtils.isEmptyField(txtDiscount, "Order Discount");
    }

    private boolean validatePhoneNumber() {
        Pattern pattern = Pattern.compile("^07\\d{8}$");
        Matcher matcher = pattern.matcher(txtPhnNum.getText());

        if (matcher.matches()) {
            return false;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Phone Number");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a valid phone number starting with '07' ");
        alert.showAndWait();
        return true;
    }

    private boolean isAnyInputDataInvalid() {
        if (validatePhoneNumber() ||
                TextFieldUtils.isEmptyField(txtName, "Customer Name") ||
                TextFieldUtils.isEmptyField(txtAddress, "Customer Address") ||
                validateCode() ||
                TextFieldUtils.isEmptyField(txtDescription, "Item Description") ||
                TextFieldUtils.isEmptyField(txtUnitPrice, "Item Unit Price") ||
                validateQty() ||
                validateOrderType() ||
                validateDiscount() || subTotal <= 0.00
        ) {
            return true;
        }

        if (cmbOrderType.getValue() != null && cmbOrderType.getValue().equals("Dine In")) {
            return validateTableNum();
        }
        return false;
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
        if (validatePhoneNumber()) {
            return;
        }
        String phoneNumber = txtPhnNum.getText();
        try {
            customer = customerDAO.searchCustomer(phoneNumber);
            if (customer != null) {
                txtName.setText(customer.getName());
                txtAddress.setText(customer.getAddress());
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Not Found");
                alert.setHeaderText(null);
                alert.setContentText("No customer found with the given phone number.");
                alert.showAndWait();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemCodes() {
        itemList = FXCollections.observableArrayList();

        for (ItemDTO itemDTO : items) {
            itemList.add(itemDTO.getCode());
        }
        cmbItemCode.setItems(itemList);
    }

    private void loadOrderTypes() {
        ObservableList<String> orderTypes = FXCollections.observableArrayList("Dine In", "Take Away", "Delivery");
        cmbOrderType.setItems(orderTypes);
    }

    private String setOrderId() {
        try {
            String lastOrderId = orderDAO.getLastOrderId();
            if (lastOrderId == null || lastOrderId.isEmpty()) {
                return "ODR#00001";
            } else {
                int num = Integer.parseInt(lastOrderId.substring(4)) + 1; // Extract the number part and increment
                return "ODR#" + String.format("%05d", num);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeOrderButtonOnAction() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        List<OrderDetailDTO> list = new ArrayList<>();
        String oderId = setOrderId();
        for (OrderTM orderTM : tmList) {
            list.add(new OrderDetailDTO(
                    oderId,
                    Objects.requireNonNull(findItemByCode(orderTM.getCode())).getId(),
                    orderTM.getQty(),
                    orderTM.getAmount() / orderTM.getQty()
            ));
        }

        OrderDTO dto = new OrderDTO(
                oderId,
                LocalDateTime.now(),
                customer.getId(),
                Double.parseDouble(lblTotal.getText()),
                cmbOrderType.getValue().toString(),
                "1",
                list
        );
        boolean isSaved = false;
        try {
            isSaved = orderDAO.save(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Order saved!").show();
                btnAddToCart.setDisable(true);
                btnPlaceOrder.setDisable(true);
                lblOrderId.setText(oderId);
                cmbItemCode.setDisable(true);
                txtPhnNum.setDisable(true);
                tblOrders.setDisable(true);
                txtDiscount.setDisable(true);
                cmbOrderType.setDisable(true);
                txtQty.setDisable(true);
            }
        } catch (SQLException | ClassNotFoundException e) {
            TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order: " + e.getMessage());
            throw new RuntimeException(e);
        }

        if (isSaved) {
            btnPrintBill.setDisable(false);
        }
    }

    public void printBillButtonOnAction(ActionEvent actionEvent) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", lblOrderId.getText());
            parameters.put("tblNo", txtTableNum.getText());
            parameters.put("subTotal", lblSubTotal.getText());
            parameters.put("discount", lblDiscount.getText());

            JasperDesign design = JRXmlLoader.load(getClass().getResourceAsStream("/reports/bill.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
            if (!(cmbOrderType.getValue().equals("Dine In"))) {
                JasperDesign kitchenDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/kitchen_bill.jrxml"));
                JasperReport jasperReportKitchen = JasperCompileManager.compileReport(kitchenDesign);
                JasperPrint jasperPrintKitchen = JasperFillManager.fillReport(jasperReportKitchen, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrintKitchen, false);
            }
        } catch (JRException | ClassNotFoundException | SQLException e) {
            // Show error message alert
            TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to print bill: " + e.getMessage());
            throw new RuntimeException(e);
        }
        lblOrderId.setText("");
        clearFields();
        btnPrintBill.setDisable(true);
        cmbItemCode.setDisable(false);
        cmbOrderType.setDisable(false);
        txtPhnNum.setDisable(false);
        tblOrders.setDisable(false);
        btnNewCustomer.setDisable(false);
        txtDiscount.setDisable(false);
        txtQty.setDisable(false);
        subTotal = 0.00;

    }

    public void addToCartButtonOnAction() throws SQLException, ClassNotFoundException {
        if (validateQty() || validateDiscount()) {
            return;
        }
        searchPhoneNumber();
        btnAddToCart.setDisable(false);
        btnPlaceOrders.setDisable(false);
        btnNewCustomer.setDisable(true);
        JFXButton btnDelete = new JFXButton("Delete");
        OrderTM orderTM = new OrderTM(
                cmbItemCode.getValue().toString(),
                txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtUnitPrice.getText()) * Integer.parseInt(txtQty.getText()),
                btnDelete
        );
        btnDelete.setOnAction(actionEvent -> {
            tmList.remove(orderTM);
            subTotal -= orderTM.getAmount();
            lblSubTotal.setText(String.format("%.2f", subTotal));
            tblOrders.refresh();
            TextFieldUtils.isEmptyField(txtDiscount, "Order Discount");
            txtDiscount.setText("0");
            setDiscount();
        });
        boolean isExist = false;
        for (OrderTM order : tmList) {
            if (order.getCode().equals(orderTM.getCode())) {
                order.setQty(order.getQty() + orderTM.getQty());
                order.setAmount(order.getAmount() + orderTM.getAmount());
                isExist = true;
                subTotal += orderTM.getAmount();
                tblOrders.refresh();
            }
        }
        if (!isExist) {
            tmList.add(orderTM);
            subTotal += orderTM.getAmount();
        }

        lblSubTotal.setText(String.format("%.2f", subTotal));

        tblOrders.setItems(tmList);
        setDiscount();
        btnPlaceOrder.setDisable(false);
    }

    private void clearFields() {

        txtName.clear();
        txtPhnNum.clear();
        txtAddress.clear();

        cmbItemCode.getSelectionModel().clearSelection();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQty.clear();

        cmbOrderType.getSelectionModel().clearSelection();
        txtTableNum.setVisible(false);
        lblTableNum.setVisible(false);

        tmList.clear();
        tblOrders.refresh();

        txtDiscount.setText("0.00");
        lblTotal.setText("0.00");
        lblSubTotal.setText("0.00");
        lblDiscount.setText("0.00");
    }

    public void txtPhnNumOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //to remove
    }

    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        home.viewCustomers(actionEvent);
    }

    public void onNewCustomerAdded(CustomerDTO customer) {
        CustomerDTO customerDTO = new CustomerDTO(
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
        txtPhnNum.setText(customerDTO.getPhoneNumber());
        txtName.setText(customerDTO.getName());
        txtAddress.setText(customerDTO.getAddress());
    }

    private void setDiscount() {
        double total = subTotal;
        double discount = Double.parseDouble(txtDiscount.getText());
        discount /= 100.00;
        discount *= subTotal;

        discount = Math.round(discount / 10.0) * 10.0;

        lblDiscount.setText(String.format("%.2f", discount));
        total -= discount;
        lblTotal.setText(String.format("%.2f", total));
    }

    public void txtDiscountOnAction(ActionEvent actionEvent) {
        setDiscount();
    }

    public void notificationsButtonOnAction() {
        //yet to implement
    }

    public void logoutButtonOnAction() {
        //yet to implement
    }

    public void editButtonOnAction() {
        //yet to implement
    }

    public void settingsButtonOnAction() {
        //yet to implement
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


    public void categoriesButtonOnAction(ActionEvent actionEvent) throws IOException { home.viewCategories(actionEvent);
    }
}
