package controller;

import bo.BOFactory;
import bo.custom.CategoryBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.util.BOType;
import dto.CategoryDTO;
import dto.tm.CategoryTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {
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
    private JFXButton btnCategories;

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
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<CategoryTM> tblCategories;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colOption;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;
    private final CategoryBO categoryBO = BOFactory.getInstance().getBo(BOType.CATEGORY);
    private final HomeController home = new HomeController();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadCategories();
        btnUpdate.setDisable(true);

        tblCategories.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
                txtId.setDisable(true);
                btnUpdate.setDisable(false);
                btnSave.setDisable(true); // Disable save button when a record is selected
            } else {
                btnSave.setDisable(false); // Enable save button when no record is selected
                txtId.setDisable(false);
                clearFields();

            }
        });

        TextFieldUtils.setNumericInputFilter(txtId, 2);
        txtId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {TextFieldUtils.isEmptyField(txtId, "Category Id");}
        });txtName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {TextFieldUtils.isEmptyField(txtName, "Category Name");}
        });

    }
    private void setData(CategoryTM newValue) {
        if (newValue != null) {
            txtId.setText(newValue.getId());
            txtName.setText(newValue.getCategoryName());
        }
    }

    private void loadCategories() {
        ObservableList<CategoryTM> tmList = FXCollections.observableArrayList();
        try {
            List<CategoryDTO> dtoList = categoryBO.allCategory();
            for (CategoryDTO categoryDTO : dtoList) {
                JFXButton btn = new JFXButton("Delete");
                CategoryTM categoryTM = new CategoryTM(
                        categoryDTO.getId(),
                        categoryDTO.getCategoryName(),
                        btn
                );

                btn.setOnAction(actionEvent ->
                        deleteCategory(categoryTM.getId())
                );

                tmList.add(categoryTM);
            }

            tblCategories.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteCategory(String id) {
        try {
            boolean isDeleted = categoryBO.deleteCategory(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Category Deleted!").show();
                loadCategories();
            }
        } catch (ClassNotFoundException | SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                String errorMsg = "Cannot delete category. It is associated with existing items.";
                System.out.println(errorMsg); // Log the error message for debugging
                new Alert(Alert.AlertType.ERROR, errorMsg).show();
            } else {
                String errorMsg = "Failed to delete category: " + e.getMessage();
                System.out.println(errorMsg); // Log the error message for debugging
                TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", errorMsg);
            }
            e.printStackTrace();
        }
    }




    private boolean isAnyInputDataInvalid() {
        return TextFieldUtils.isEmptyField(txtId, "Category Id") || TextFieldUtils.isEmptyField(txtName, "Category Name");
    }
    public void saveCategory() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        try {
            boolean isSaved = categoryBO.saveCategory(new CategoryDTO(txtId.getText(),
                    txtName.getText()
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Category Saved!").show();
                loadCategories();
                clearFields();
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.ERROR, "Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void clearFields() {
        tblCategories.refresh();
        txtId.clear();
        txtName.clear();
    }
    public void updateCategory() {
        if (isAnyInputDataInvalid()) {
            return;
        }
        try {

            CategoryTM selectedCategory = tblCategories.getSelectionModel().getSelectedItem();

            boolean isUpdated = categoryBO.updateCategory(new CategoryDTO(
                    selectedCategory.getId(),
                    txtName.getText()
            ));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Category Updated!").show();
                loadCategories();
                clearFields();
                btnUpdate.setDisable(true);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveButtonOnAction(ActionEvent actionEvent) { saveCategory();
    }

    public void updateButtonOnAction(ActionEvent actionEvent) { updateCategory();
    }
    public void dashboardButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewHome(actionEvent);
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewItems(actionEvent);
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewOrders(actionEvent);
    }

    public void customersButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewCustomers(actionEvent);
    }

    public void placeOrdersButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewPlaceOrder(actionEvent);
    }

    public void categoriesButtonOnAction(ActionEvent actionEvent) throws IOException {home.viewCategories(actionEvent);
    }

    public void settingsButtonOnAction(ActionEvent actionEvent) {//yet to implement
    }

    public void logoutButtonOnAction(ActionEvent actionEvent) {//yet to implement
    }

    public void notificationsButtonOnAction(ActionEvent actionEvent) {//yet to implement
    }

    public void editButtonOnAction(ActionEvent actionEvent) {//yet to implement
    }


}
