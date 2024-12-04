package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.Objects.Editorial;
import com.axear.proyecto_final_objetos.DAO.EditorialDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class EditorialesController {
    @FXML
    private TextField idEditorialField;
    @FXML
    private TextField descripcionField;

    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;

    @FXML
    private TableView<Editorial> editorialTable;
    @FXML
    private TableColumn<Editorial, Integer> idEditorialColumn;
    @FXML
    private TableColumn<Editorial, String> descripcionColumn;

    private static EditorialDAO editorialDAO;
    private static List<Editorial> editorialesList = new ArrayList<>();

    public EditorialesController() {
        editorialDAO = new EditorialDAO(Main.connection);
        editorialesList = editorialDAO.leerEditoriales();
        System.out.println(editorialesList.size());
    }

    @FXML
    public void initialize() {
        idEditorialColumn.setCellValueFactory(new PropertyValueFactory<>("idEditorial"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        editorialTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, editorialSeleccionada) -> {
            if (editorialSeleccionada != null) {
                idEditorialField.setText(String.valueOf(editorialSeleccionada.getIdEditorial()));
                descripcionField.setText(editorialSeleccionada.getDescripcion());

                idEditorialField.setDisable(true);
                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
            }
        });

        actualizarUI();
    }

    @FXML
    private void agregarEditorial() {
        if (areFieldsValid()) {
            editorialesList.add(new Editorial(Integer.parseInt(idEditorialField.getText()), descripcionField.getText()));
            editorialDAO.escribirEditoriales(editorialesList);
            actualizarUI();
        }
    }

    @FXML
    private void eliminarEditorial() {
        editorialesList.removeIf(editorial -> editorial.getIdEditorial() == Integer.parseInt(idEditorialField.getText()));
        editorialDAO.escribirEditoriales(editorialesList);
        actualizarUI();
    }

    @FXML
    private void actualizarEditorial() {
        if (areFieldsValid()) {
            for (Editorial editorial : editorialesList) {
                if (editorial.getIdEditorial() == Integer.parseInt(idEditorialField.getText())) {
                    editorial.setDescripcion(descripcionField.getText());

                    actualizarUI();
                    return;
                }
            }
            editorialDAO.escribirEditoriales(editorialesList);
        }
    }

    private boolean areFieldsValid() {
        resetFieldStyles();

        if (idEditorialField.getText().isEmpty() || descripcionField.getText().isEmpty()) {
            showFieldError("Todos los campos son obligatorios.");
            return false;
        }

        try {
            Integer.parseInt(idEditorialField.getText());
        } catch (NumberFormatException e) {
            idEditorialField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showFieldError("ID de Editorial debe ser un numero.");
            return false;
        }

        return true;
    }

    public void actualizarUI() {
        editorialTable.getItems().clear();
        ObservableList<Editorial> editorialesObservableList = FXCollections.observableArrayList(editorialesList);
        editorialTable.setItems(editorialesObservableList);

        idEditorialField.clear();
        descripcionField.clear();

        idEditorialField.setDisable(false);
        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    private void showFieldError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void resetFieldStyles() {
        idEditorialField.setStyle("-fx-border-color: none;");
        descripcionField.setStyle("-fx-border-color: none;");
    }
}