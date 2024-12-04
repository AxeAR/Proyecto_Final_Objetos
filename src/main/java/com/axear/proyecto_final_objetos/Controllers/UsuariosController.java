package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.Objects.Usuario;
import com.axear.proyecto_final_objetos.DAO.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UsuariosController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apaternoField;
    @FXML
    private TextField amaternoField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker fechaRegistroPicker;

    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;

    @FXML
    private TableView<Usuario> userTable;
    @FXML
    private TableColumn<Usuario, String> nombreColumn;
    @FXML
    private TableColumn<Usuario, String> apaternoColumn;
    @FXML
    private TableColumn<Usuario, String> amaternoColumn;
    @FXML
    private TableColumn<Usuario, String> direccionColumn;
    @FXML
    private TableColumn<Usuario, String> telefonoColumn;
    @FXML
    private TableColumn<Usuario, String> emailColumn;
    @FXML
    private TableColumn<Usuario, String> fechaRegistroColumn;

    private static UsuarioDAO usuarioDAO;
    private static List<Usuario> usuariosList = new ArrayList<>();

    public UsuariosController() {
        usuarioDAO = new UsuarioDAO(Main.connection);
        usuariosList = usuarioDAO.leerUsuarios();
    }

    @FXML
    public void initialize() {
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apaternoColumn.setCellValueFactory(new PropertyValueFactory<>("aPaterno"));
        amaternoColumn.setCellValueFactory(new PropertyValueFactory<>("aMaterno"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        fechaRegistroColumn.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, usuarioSeleccionado) -> {
            if (usuarioSeleccionado != null) {
                nombreField.setText(usuarioSeleccionado.getNombre());
                apaternoField.setText(usuarioSeleccionado.getAPaterno());
                amaternoField.setText(usuarioSeleccionado.getAMaterno());
                direccionField.setText(usuarioSeleccionado.getDireccion());
                telefonoField.setText(usuarioSeleccionado.getTelefono());
                emailField.setText(usuarioSeleccionado.getEmail());

                if (usuarioSeleccionado.getFechaRegistro() != null && !usuarioSeleccionado.getFechaRegistro().isEmpty()) {
                    LocalDate fechaRegistro = LocalDate.parse(usuarioSeleccionado.getFechaRegistro(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    fechaRegistroPicker.setValue(fechaRegistro);
                } else {
                    fechaRegistroPicker.setValue(null);
                }

                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
                emailField.setDisable(true);
            }
        });

        actualizarUI();
    }


    @FXML
    private void agregarUsuario() {
        if (areFieldsValid()) {
            LocalDate fechaRegistro = fechaRegistroPicker.getValue();
            String fechaRegistroStr = fechaRegistro != null ? fechaRegistro.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";

            usuariosList.add(new Usuario(nombreField.getText(), apaternoField.getText(), amaternoField.getText(),
                    direccionField.getText(), telefonoField.getText(), fechaRegistroStr, emailField.getText()));

            usuarioDAO.escribirUsuarios(usuariosList);

            actualizarUI();
        }
    }

    @FXML
    private void eliminarUsuario() {
        usuariosList.removeIf(usuario -> usuario.getEmail().equals(emailField.getText()));
        usuarioDAO.escribirUsuarios(usuariosList);
        actualizarUI();
    }

    @FXML
    private void actualizarUsuario() {
        if (areFieldsValid()) {
            for (Usuario usuario : usuariosList) {
                if (usuario.getEmail().equals(emailField.getText())) {
                    usuario.setNombre(nombreField.getText());
                    usuario.setAPaterno(apaternoField.getText());
                    usuario.setAMaterno(amaternoField.getText());
                    usuario.setDireccion(direccionField.getText());
                    usuario.setTelefono(telefonoField.getText());
                    LocalDate fechaRegistro = fechaRegistroPicker.getValue();
                    String fechaRegistroStr = fechaRegistro != null ? fechaRegistro.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                    usuario.setFechaRegistro(fechaRegistroStr);

                    actualizarUI();
                    return;
                }
            }
            usuarioDAO.escribirUsuarios(usuariosList);
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
        nombreField.setStyle("-fx-border-color: none;");
        apaternoField.setStyle("-fx-border-color: none;");
        amaternoField.setStyle("-fx-border-color: none;");
        direccionField.setStyle("-fx-border-color: none;");
        telefonoField.setStyle("-fx-border-color: none;");
        emailField.setStyle("-fx-border-color: none;");
        fechaRegistroPicker.setStyle("-fx-border-color: none;");
    }

    private boolean areFieldsValid() {
        resetFieldStyles();

        if (nombreField.getText().isEmpty() ||
                apaternoField.getText().isEmpty() ||
                amaternoField.getText().isEmpty() ||
                direccionField.getText().isEmpty() ||
                telefonoField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                fechaRegistroPicker.getValue() == null) {
            showFieldError("Todos los campos son obligatorios.");
            return false;
        }

        if (!telefonoField.getText().matches("\\d{10}")) {
            telefonoField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showFieldError("Telefono debe tener 10 digitos.");
            return false;
        }

        if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$").
                matcher(emailField.getText()).matches()) {
            emailField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showFieldError("Correo invalido.");
            return false;        }

        if (fechaRegistroPicker == null) {
            fechaRegistroPicker.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showFieldError("Fecha Registro es requerida.");
            return false;
        }

        return true;
    }

    public void actualizarUI() {
        userTable.getItems().clear();
        ObservableList<Usuario> usuariosObservableList = FXCollections.observableArrayList(usuariosList);
        userTable.setItems(usuariosObservableList);

        nombreField.clear();
        apaternoField.clear();
        amaternoField.clear();
        direccionField.clear();
        telefonoField.clear();
        emailField.clear();
        fechaRegistroPicker.setValue(null);

        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        emailField.setDisable(false);
    }
}
