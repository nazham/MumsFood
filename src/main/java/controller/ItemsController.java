package controller;

import bo.BOFactory;
import bo.custom.CategoryBO;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.util.BOType;
import dto.CategoryDTO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.tm.CustomerTM;
import dto.tm.ItemTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemsController implements Initializable {
    public TableColumn<?, ?> colId;
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
    private JFXTextField txtSearch;

    @FXML
    private TableView<ItemTM> tblItems;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colOption;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXComboBox<String> cmbCategory;

    private List<CategoryDTO> categories;

    private CategoryBO categoryBO = BOFactory.getInstance().getBo(BOType.CATEGORY);

    private final ItemBO itemBO = BOFactory.getInstance().getBo(BOType.ITEM);

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

    private void loadItems() {
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
        try {
            List<ItemDTO> dtoList = itemBO.allItems();
            for (ItemDTO itemDTO : dtoList) {
                JFXButton btn = new JFXButton("Delete");
                ItemTM itemTm = new ItemTM(
                        itemDTO.getId(),
                        itemDTO.getCode(),
                        itemDTO.getDesc(),
                        itemDTO.getUnitPrice(),
                        itemDTO.getCategoryId(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteItem(itemTm.getCode());
                });

                tmList.add(itemTm);
            }

            tblItems.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadItems();
        btnUpdate.setDisable(true);

        try {
            categories = categoryBO.allCategory();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ObservableList<String> list = FXCollections.observableArrayList();

        for (CategoryDTO categoryDTO : categories) {
            list.add(categoryDTO.getCategoryName());
        }

        cmbCategory.setItems(list);

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observableValue, o, newValue) -> {
            if (newValue != null) {
                cmbCategory.setValue(newValue.toString());
            }
        });



        tblItems.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
                btnUpdate.setDisable(false);
                btnSave.setDisable(true); // Disable save button when a record is selected
            } else {
                btnSave.setDisable(false); // Enable save button when no record is selected
            }


        });
    }

    private void setData(ItemTM newValue) {
        if (newValue != null) {
            txtCode.setText(newValue.getCode());
            txtDescription.setText(newValue.getDesc());
            txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
            cmbCategory.setValue(newValue.getCategoryId());
        }
    }

    private boolean validateDescription() {
        String name = txtDescription.getText();
        if (name.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Description");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a Description");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateUnitPrice() {
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(txtUnitPrice.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Unit Price");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid unit price");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public boolean isCategorySelected() {
        if (cmbCategory.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Category Not Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a category.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

//    private boolean validateCode() {
//        Pattern pattern = Pattern.compile("^P[0-9]{3}$");
//        Matcher matcher = pattern.matcher(txtCode.getText());
//
//        if (matcher.find() && matcher.group().equals(txtCode.getText())) {
//            return true;
//        }
//
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle("Invalid Code");
//        alert.setHeaderText(null);
//        alert.setContentText("Please enter a valid code");
//        alert.showAndWait();
//        return false;
//    }

    private boolean isAnyInputDataInvalid() {
        return !validateUnitPrice() | !validateDescription() | !isCategorySelected();
    }

    public void deleteItem(String id) {
        try {
            boolean isDeleted = itemBO.deleteItem(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted!").show();
                loadItems();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveItem() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        try {
            CategoryDTO selectedCategory = null;
            for (CategoryDTO categoryDTO : categories) {
                if (categoryDTO.getCategoryName().equals(cmbCategory.getValue())) {
                    selectedCategory = categoryDTO;
                    break;
                }
            }
            if (selectedCategory == null) {
                    isCategorySelected();
                    return;
            }


            boolean isSaved = itemBO.saveItem(new ItemDTO(txtCode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    selectedCategory.getId()
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Item Saved!").show();
                loadItems();
                clearFields();
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.ERROR, "Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tblItems.refresh();
        txtCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        cmbCategory.setValue(null);
    }

    public void saveButtonOnAction() {
        saveItem();
    }

    public void updateItem() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        try {
            CategoryDTO selectedCategory = null;
            for (CategoryDTO categoryDTO : categories) {
                if (categoryDTO.getCategoryName().equals(cmbCategory.getValue())) {
                    selectedCategory = categoryDTO;
                    break;
                }
            }
            if (selectedCategory == null) {
                isCategorySelected();
                return;
            }

            ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();

            boolean isUpdated = itemBO.updateItem(new ItemDTO(
                    selectedItem.getId(),
                    txtCode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    selectedCategory.getId()
            ));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Item Updated!").show();
                loadItems();
                clearFields();
                btnUpdate.setDisable(true);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateButtonOnAction() {
        updateItem();
    }

    public void cmbCategoryOnAction(ActionEvent actionEvent) {

    }
}
