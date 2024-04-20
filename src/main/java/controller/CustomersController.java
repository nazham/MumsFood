package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.util.BOType;
import db.DBConnection;
import dto.CustomerDTO;
import dto.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomersController implements Initializable {
    private final CustomerBO customerBO = BOFactory.getInstance().getBo(BOType.CUSTOMER);
    private final HomeController home = new HomeController();
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
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtPhnNum;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<CustomerTM> tblCustomers;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colPhnNum;
    @FXML
    private TableColumn<?, ?> colAddress;
    @FXML
    private TableColumn<?, ?> colOption;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnUpdate;
    private PlaceOrdersController placeOrderController;
    private CustomerDTO customerDto = new CustomerDTO();

    public void setPlaceOrderController(PlaceOrdersController placeOrderController) {
        this.placeOrderController = placeOrderController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhnNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadCustomers();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
                btnSave.setDisable(true); // Disable save button when a record is selected
            } else {
                btnSave.setDisable(false); // Enable save button when no record is selected
            }
        });
    }

    private void loadCustomers() {
        ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
        try {
            List<CustomerDTO> dtoList = customerBO.allCustomers();
            for (CustomerDTO customerDTO : dtoList) {
                JFXButton btn = new JFXButton("Delete");
                CustomerTM customerTM = new CustomerTM(
                        String.valueOf(customerDTO.getId()),
                        customerDTO.getName(),
                        customerDTO.getPhoneNumber(),
                        customerDTO.getAddress(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteCustomer(customerTM.getId());
                });

                tmList.add(customerTM);
            }

            tblCustomers.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setData(CustomerTM newValue) {
        if (newValue != null) {
            txtName.setText(newValue.getName());
            txtPhnNum.setText(newValue.getPhoneNumber());
            txtAddress.setText(newValue.getAddress());
        }
    }

    public void deleteCustomer(String id) {
        try {
            boolean isDeleted = customerBO.deleteCustomer(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!").show();
                loadCustomers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer ID format").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomer() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        customerDto = new CustomerDTO(
                txtName.getText(),
                txtPhnNum.getText(),
                txtAddress.getText()
        );

        try {
            boolean isSaved = customerBO.saveCustomer(customerDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Saved!").show();
                loadCustomers();
                clearFields();
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.ERROR, "Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveButtonOnAction(ActionEvent actionEvent) throws IOException {
        saveCustomer();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlaceOrders.fxml"));
        Parent root = loader.load();
        placeOrderController = loader.getController();

        // Pass the PlaceOrderController instance to CustomerFormController
        FXMLLoader customerFormLoader = new FXMLLoader(getClass().getResource("/view/Customers.fxml"));
        customerFormLoader.load();
        CustomersController customerFormController = customerFormLoader.getController();
        customerFormController.setPlaceOrderController(placeOrderController);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        placeOrderController.onNewCustomerAdded(customerDto);
    }

    private void clearFields() {
        tblCustomers.refresh();
        txtName.clear();
        txtAddress.clear();
        txtPhnNum.clear();
    }

    private boolean validatePhoneNumber() {
        Pattern pattern = Pattern.compile("^07\\d{8}$");
        Matcher matcher = pattern.matcher(txtPhnNum.getText());

        if (matcher.matches()) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Phone Number");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a valid phone number starting with '07' and containing 10 digits");
        alert.showAndWait();
        return false;
    }

    private boolean validateName() {
        String name = txtName.getText();
        if (name.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Name");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a name");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateAddress() {
        String address = txtAddress.getText();
        if (address.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Address");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an address");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    private boolean isAnyInputDataInvalid() {
        return !validateName() | !validatePhoneNumber() | !validateAddress();
    }

    public void updateCustomer() {
        if (isAnyInputDataInvalid()) {
            return;
        }

        CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        try {
            boolean isUpdated = customerBO.updateCustomer(new CustomerDTO(
                    Integer.parseInt(selectedCustomer.getId()),
                    txtName.getText(),
                    txtPhnNum.getText(),
                    txtAddress.getText()
            ));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").show();
                loadCustomers();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateButtonOnAction() {
        updateCustomer();
    }

    public void reportButtonOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/customer_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

}
