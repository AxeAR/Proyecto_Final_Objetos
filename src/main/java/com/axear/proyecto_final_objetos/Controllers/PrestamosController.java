package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.DAO.LibroDAO;
import com.axear.proyecto_final_objetos.DAO.PrestamoDAO;
import com.axear.proyecto_final_objetos.DAO.UsuarioDAO;
import com.axear.proyecto_final_objetos.Objects.Libro;
import com.axear.proyecto_final_objetos.Objects.Prestamo;
import com.axear.proyecto_final_objetos.Objects.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PrestamosController {

    @FXML
    private ComboBox<String> usuarioComboBox;
    @FXML
    private ComboBox<String> libroComboBox;
    @FXML
    private DatePicker fechaPrestamoPicker;
    @FXML
    private DatePicker fechaDevolucionPicker;
    @FXML
    private ComboBox<Integer> estadoComboBox;
    @FXML
    private TextField idPrestamoField;

    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;

    @FXML
    private TableView<Prestamo> prestamoTable;
    @FXML
    private TableColumn<Prestamo, Integer> idPrestamoColumn;
    @FXML
    private TableColumn<Prestamo, Integer> idUsuarioColumn;
    @FXML
    private TableColumn<Prestamo, Integer> idLibroColumn;
    @FXML
    private TableColumn<Prestamo, String> fechaPrestamoColumn;
    @FXML
    private TableColumn<Prestamo, String> fechaDevolucionColumn;
    @FXML
    private TableColumn<Prestamo, String> estadoColumn;

    private static PrestamoDAO prestamoDAO;
    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;

    private static ObservableList<Prestamo> prestamosList;

    public PrestamosController() {
        prestamoDAO = new PrestamoDAO(Main.connection);
        usuarioDAO = new UsuarioDAO(Main.connection);
        libroDAO = new LibroDAO(Main.connection);
    }

    @FXML
    public void initialize() {
        idPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        idUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        idLibroColumn.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        fechaPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        prestamosList = FXCollections.observableArrayList(prestamoDAO.leerPrestamos());

        estadoComboBox.setItems(FXCollections.observableArrayList(0, 1));

        fillUserComboBox();
        fillBookComboBox();

        prestamoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, prestamoSeleccionado) -> {
            if (prestamoSeleccionado != null) {
                idPrestamoField.setText(String.valueOf(prestamoSeleccionado.getIdPrestamo()));
                usuarioComboBox.getSelectionModel().select(prestamoSeleccionado.getIdUsuario());
                libroComboBox.getSelectionModel().select(prestamoSeleccionado.getIdLibro());
                fechaPrestamoPicker.setValue(LocalDate.parse(prestamoSeleccionado.getFechaPrestamo(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                fechaDevolucionPicker.setValue(LocalDate.parse(prestamoSeleccionado.getFechaDevolucion(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                estadoComboBox.getSelectionModel().select(prestamoSeleccionado.getEstado());

                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
            }
        });

        actualizarUI();
    }

    private void fillUserComboBox() {
        List<Usuario> usuarios = usuarioDAO.leerUsuarios();
        ObservableList<String> usuarioEmails = FXCollections.observableArrayList(
                usuarios.stream().map(Usuario::getEmail).collect(Collectors.toList())
        );
        usuarioComboBox.setItems(usuarioEmails);
    }

    private void fillBookComboBox() {
        List<Libro> libros = libroDAO.leerLibros();
        ObservableList<String> libroIsbns = FXCollections.observableArrayList(
                libros.stream().map(Libro::getISBN).collect(Collectors.toList())
        );
        libroComboBox.setItems(libroIsbns);
    }

    @FXML
    private void agregarPrestamo() {
        if (areFieldsValid()) {
            String usuario = usuarioComboBox.getSelectionModel().getSelectedItem();
            String libro = libroComboBox.getSelectionModel().getSelectedItem();
            LocalDate fechaPrestamo = fechaPrestamoPicker.getValue();
            LocalDate fechaDevolucion = fechaDevolucionPicker.getValue();
            int estado = estadoComboBox.getValue();

            Prestamo newPrestamo = new Prestamo(prestamosList.size() + 1, usuario, libro,
                    fechaPrestamo.toString(), fechaDevolucion != null ? fechaDevolucion.toString() : null,
                    estado);

            prestamosList.add(newPrestamo);
            prestamoDAO.escribirPrestamos(prestamosList);

            actualizarUI();
        }
    }

    @FXML
    private void eliminarPrestamo() {
        Prestamo selectedPrestamo = prestamoTable.getSelectionModel().getSelectedItem();
        if (selectedPrestamo != null) {
            prestamosList.remove(selectedPrestamo);
            prestamoDAO.escribirPrestamos(prestamosList);
            actualizarUI();
        } else {
            showFieldError("No hay ningun prestamo seleccionado para eliminar.");
        }
    }

    @FXML
    private void actualizarPrestamo() {
        Prestamo selectedPrestamo = prestamoTable.getSelectionModel().getSelectedItem();
        if (selectedPrestamo != null) {
            String selectedUsuario = usuarioComboBox.getSelectionModel().getSelectedItem();
            String selectedLibro = libroComboBox.getSelectionModel().getSelectedItem();
            LocalDate fechaPrestamo = fechaPrestamoPicker.getValue();
            LocalDate fechaDevolucion = fechaDevolucionPicker.getValue();
            Integer estado = estadoComboBox.getSelectionModel().getSelectedItem();

            if (selectedUsuario != null && selectedLibro != null && fechaPrestamo != null && estado != null) {
                selectedPrestamo.setIdUsuario(selectedUsuario);
                selectedPrestamo.setIdLibro(selectedLibro);
                selectedPrestamo.setFechaPrestamo(fechaPrestamo.toString());
                if (fechaDevolucion != null) {
                    selectedPrestamo.setFechaDevolucion(fechaDevolucion.toString());
                } else {
                    selectedPrestamo.setFechaDevolucion(null);
                }
                selectedPrestamo.setEstado(estado);

                actualizarUI();
            } else {
                showFieldError("Todos los campos obligatorios deben estar llenos para actualizar.");
            }
        } else {
            showFieldError("No hay ningun prestamo seleccionado para actualizar.");
        }

        prestamoDAO.escribirPrestamos(prestamosList);
    }

    private boolean areFieldsValid() {
        if (usuarioComboBox.getSelectionModel().isEmpty() ||
                libroComboBox.getSelectionModel().isEmpty() ||
                fechaPrestamoPicker.getValue() == null ||
                fechaDevolucionPicker.getValue() == null ||
                estadoComboBox.getSelectionModel().isEmpty()) {
            showFieldError("Todos los campos obligatorios deben estar llenos.");
            return false;
        }

        LocalDate fechaPrestamo = fechaPrestamoPicker.getValue();
        LocalDate fechaDevolucion = fechaDevolucionPicker.getValue();

        if (fechaDevolucion != null && fechaDevolucion.isBefore(fechaPrestamo)) {
            showFieldError("La fecha de devolucion debe ser posterior a la fecha de prestamo.");
            return false;
        }

        return true;
    }

    private void showFieldError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void actualizarUI() {
        prestamoTable.getItems().clear();
        ObservableList<Prestamo> prestamosObservableList = FXCollections.observableArrayList(prestamosList);
        prestamoTable.setItems(prestamosObservableList);

        idPrestamoField.clear();
        usuarioComboBox.getSelectionModel().clearSelection();
        libroComboBox.getSelectionModel().clearSelection();
        fechaPrestamoPicker.setValue(null);
        fechaDevolucionPicker.setValue(null);
        estadoComboBox.getSelectionModel().clearSelection();

        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }
}