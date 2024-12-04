package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.Objects.Autor;
import com.axear.proyecto_final_objetos.DAO.AutorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class AutoresController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apaternoField;
    @FXML
    private TextField amaternoField;
    @FXML
    private TextField idAutorField;

    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;

    @FXML
    private TableView<Autor> autorTable;
    @FXML
    private TableColumn<Autor, String> nombreColumn;
    @FXML
    private TableColumn<Autor, String> apaternoColumn;
    @FXML
    private TableColumn<Autor, String> amaternoColumn;
    @FXML
    private TableColumn<Autor, String> idAutorColumn;

    private static AutorDAO autorDAO;
    private static List<Autor> autoresList = new ArrayList<>();

    public AutoresController() {
        autorDAO = new AutorDAO(Main.connection);
        autoresList = autorDAO.leerAutores();
    }

    @FXML
    public void initialize() {
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apaternoColumn.setCellValueFactory(new PropertyValueFactory<>("aPaterno"));
        amaternoColumn.setCellValueFactory(new PropertyValueFactory<>("aMaterno"));
        idAutorColumn.setCellValueFactory(new PropertyValueFactory<>("idAutor"));

        autorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, autorSeleccionado) -> {
            if (autorSeleccionado != null) {
                nombreField.setText(autorSeleccionado.getNombre());
                apaternoField.setText(autorSeleccionado.getAPaterno());
                amaternoField.setText(autorSeleccionado.getAMaterno());
                idAutorField.setText(String.valueOf(autorSeleccionado.getIdAutor()));

                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
                idAutorField.setDisable(true);
            }
        });

        actualizarUI();
    }

    @FXML
    private void agregarAutor() {
        if (areFieldsValid()) {
            autoresList.add(new Autor(nombreField.getText(), apaternoField.getText(), amaternoField.getText(), Integer.parseInt(idAutorField.getText())));
            autorDAO.escribirAutores(autoresList);
            actualizarUI();
        }
    }

    @FXML
    private void eliminarAutor() {
        autoresList.removeIf(autor -> autor.getIdAutor() == Integer.parseInt(idAutorField.getText()));
        autorDAO.escribirAutores(autoresList);
        actualizarUI();
    }

    @FXML
    private void actualizarAutor() {
        if (areFieldsValid()) {
            for (Autor autor : autoresList) {
                if (autor.getIdAutor() == Integer.parseInt(idAutorField.getText())) {
                    autor.setNombre(nombreField.getText());
                    autor.setAPaterno(apaternoField.getText());
                    autor.setAMaterno(amaternoField.getText());
                    actualizarUI();
                    return;
                }
            }
            autorDAO.escribirAutores(autoresList);
        }
    }

    private boolean areFieldsValid() {
        if (nombreField.getText().isEmpty() || apaternoField.getText().isEmpty() || amaternoField.getText().isEmpty() || idAutorField.getText().isEmpty()) {
            showFieldError("Todos los campos son obligatorios.");
            return false;
        }

        try {
            Integer.parseInt(idAutorField.getText());
        } catch (NumberFormatException e) {
            showFieldError("ID Autor debe ser un numero.");
            return false;
        }

        return true;
    }

    private void showFieldError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Validacion");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void resetFields() {
        nombreField.clear();
        apaternoField.clear();
        amaternoField.clear();
        idAutorField.clear();

        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        idAutorField.setDisable(false);
    }

    public void actualizarUI() {
        autorTable.getItems().clear();
        ObservableList<Autor> autoresObservableList = FXCollections.observableArrayList(autoresList);
        autorTable.setItems(autoresObservableList);
        resetFields();
    }
}
