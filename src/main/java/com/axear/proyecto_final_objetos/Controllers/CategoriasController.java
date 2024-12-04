package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.Objects.Categoria;
import com.axear.proyecto_final_objetos.DAO.CategoriaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoriasController {
    @FXML
    private TextField idCategoriaField;
    @FXML
    private TextField descripcionField;

    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;

    @FXML
    private TableView<Categoria> categoriaTable;
    @FXML
    private TableColumn<Categoria, Integer> idCategoriaColumn;
    @FXML
    private TableColumn<Categoria, String> descripcionColumn;

    private static CategoriaDAO categoriaDAO;
    private static List<Categoria> categoriasList = new ArrayList<>();

    public CategoriasController() {
        categoriaDAO = new CategoriaDAO(Main.connection); // Initialize CategoriaDAO
        categoriasList = categoriaDAO.leerCategorias();
        System.out.println(categoriasList.size());
    }

    @FXML
    public void initialize() {
        idCategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        categoriaTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, categoriaSeleccionada) -> {
            if (categoriaSeleccionada != null) {
                idCategoriaField.setText(String.valueOf(categoriaSeleccionada.getIdCategoria()));
                descripcionField.setText(categoriaSeleccionada.getDescripcion());

                idCategoriaField.setDisable(true);
                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
            }
        });

        actualizarUI();
    }

    @FXML
    private void agregarCategoria() {
        if (areFieldsValid()) {
            categoriasList.add(new Categoria(Integer.parseInt(idCategoriaField.getText()), descripcionField.getText()));
            categoriaDAO.escribirCategorias(categoriasList);
            actualizarUI();
        }
    }

    @FXML
    private void eliminarCategoria() {
        categoriasList.removeIf(categoria -> categoria.getIdCategoria() == Integer.parseInt(idCategoriaField.getText()));
        categoriaDAO.escribirCategorias(categoriasList);
        actualizarUI();
    }

    @FXML
    private void actualizarCategoria() {
        if (areFieldsValid()) {
            for (Categoria categoria : categoriasList) {
                if (categoria.getIdCategoria() == Integer.parseInt(idCategoriaField.getText())) {
                    categoria.setDescripcion(descripcionField.getText());

                    actualizarUI();
                    return;
                }
            }
            categoriaDAO.escribirCategorias(categoriasList);
        }
    }

    private void showFieldError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void resetFieldStyles() {
        idCategoriaField.setStyle("-fx-border-color: none;");
        descripcionField.setStyle("-fx-border-color: none;");
    }

    private boolean areFieldsValid() {
        resetFieldStyles();

        if (idCategoriaField.getText().isEmpty() || descripcionField.getText().isEmpty()) {
            showFieldError("Todos los campos son obligatorios.");
            return false;
        }

        try {
            Integer.parseInt(idCategoriaField.getText());
        } catch (NumberFormatException e) {
            idCategoriaField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showFieldError("ID de Categoria debe ser un numero.");
            return false;
        }

        return true;
    }

    public void actualizarUI() {
        categoriaTable.getItems().clear();
        ObservableList<Categoria> categoriasObservableList = FXCollections.observableArrayList(categoriasList);
        categoriaTable.setItems(categoriasObservableList);

        idCategoriaField.clear();
        descripcionField.clear();

        idCategoriaField.setDisable(false);
        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }
}
