package com.axear.proyecto_final_objetos.Controllers;

import com.axear.proyecto_final_objetos.DAO.AutorDAO;
import com.axear.proyecto_final_objetos.DAO.EditorialDAO;
import com.axear.proyecto_final_objetos.DAO.LibroDAO;
import com.axear.proyecto_final_objetos.Main;
import com.axear.proyecto_final_objetos.Objects.Autor;
import com.axear.proyecto_final_objetos.Objects.Editorial;
import com.axear.proyecto_final_objetos.Objects.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LibrosController {

    @FXML
    private TextField tituloField;
    @FXML
    private ComboBox<Integer> autorComboBox;
    @FXML
    private ComboBox<Integer> editorialComboBox;
    @FXML
    private DatePicker anioPublicacionPicker;
    @FXML
    private TextField isbnField;
    @FXML
    private Button agregarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button actualizarButton;
    @FXML
    private TableView<Libro> libroTable;
    @FXML
    private TableColumn<Libro, String> tituloColumn;
    @FXML
    private TableColumn<Libro, Integer> autorColumn;
    @FXML
    private TableColumn<Libro, Integer> editorialColumn;
    @FXML
    private TableColumn<Libro, Integer> anioPublicacionColumn;
    @FXML
    private TableColumn<Libro, Integer> disponibleColumn;
    @FXML
    private TableColumn<Libro, String> isbnColumn;

    private LibroDAO libroDAO;
    private AutorDAO autorDAO;
    private EditorialDAO editorialDAO;
    private List<Libro> librosList;

    public LibrosController() {
        this.libroDAO = new LibroDAO(Main.connection);
        this.autorDAO = new AutorDAO(Main.connection);
        this.editorialDAO = new EditorialDAO(Main.connection);
    }

    @FXML
    public void initialize() {
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("Titulo"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("Autor"));
        editorialColumn.setCellValueFactory(new PropertyValueFactory<>("Editorial"));
        anioPublicacionColumn.setCellValueFactory(new PropertyValueFactory<>("AnioPublicacion"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        librosList = libroDAO.leerLibros();

        populateComboBoxes();

        libroTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, libroSeleccionado) -> {
            if (libroSeleccionado != null) {
                tituloField.setText(libroSeleccionado.getTitulo());
                autorComboBox.setValue(libroSeleccionado.getAutor());
                editorialComboBox.setValue(libroSeleccionado.getEditorial());
                anioPublicacionPicker.setValue(LocalDate.of(libroSeleccionado.getAnioPublicacion(), 1, 1));
                isbnField.setText(libroSeleccionado.getISBN());
                isbnField.setDisable(true);

                agregarButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
            }
        });

        actualizarUI();
    }

    private void populateComboBoxes() {
        List<Autor> autores = autorDAO.leerAutores();
        List<Editorial> editoriales = editorialDAO.leerEditoriales();

        ObservableList<Integer> autorIds = FXCollections.observableArrayList(
                autores.stream().map(Autor::getIdAutor).collect(Collectors.toList())
        );
        ObservableList<Integer> editorialIds = FXCollections.observableArrayList(
                editoriales.stream().map(Editorial::getIdEditorial).collect(Collectors.toList())
        );

        autorComboBox.setItems(autorIds);
        editorialComboBox.setItems(editorialIds);
    }

    @FXML
    private void agregarLibro() {
        if (validarCampos()) {
            String titulo = tituloField.getText();
            Integer autor = autorComboBox.getSelectionModel().getSelectedItem();
            Integer editorial = editorialComboBox.getSelectionModel().getSelectedItem();
            LocalDate anioPublicacion = anioPublicacionPicker.getValue();
            String isbn = isbnField.getText();
            int disponible = 1;

            Libro nuevoLibro = new Libro(isbn, titulo, autor, editorial, anioPublicacion.getYear(), disponible);
            librosList.add(nuevoLibro);

            libroDAO.escribirLibros(librosList);

            actualizarUI();
        } else {
            mostrarError("Complete todos los campos requeridos.");
        }
    }

    @FXML
    private void actualizarLibro() {
        Libro libroSeleccionado = libroTable.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null && validarCampos()) {
            libroSeleccionado.setTitulo(tituloField.getText());
            libroSeleccionado.setAutor(autorComboBox.getSelectionModel().getSelectedItem());
            libroSeleccionado.setEditorial(editorialComboBox.getSelectionModel().getSelectedItem());
            libroSeleccionado.setAnioPublicacion(anioPublicacionPicker.getValue().getYear());
            libroSeleccionado.setISBN(isbnField.getText());

            libroDAO.escribirLibros(librosList);

            actualizarUI();
        } else {
            mostrarError("Por favor, seleccione un libro válido y complete todos los campos.");
        }
    }

    @FXML
    private void eliminarLibro() {
        Libro libroSeleccionado = libroTable.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            librosList.remove(libroSeleccionado);
            libroDAO.escribirLibros(librosList);
            actualizarUI();
        } else {
            mostrarError("Por favor, seleccione un libro para eliminar.");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean validarCampos() {
        if (tituloField.getText().isEmpty() || isbnField.getText().isEmpty() ||
                autorComboBox.getSelectionModel().getSelectedItem() == null ||
                editorialComboBox.getSelectionModel().getSelectedItem() == null ||
                anioPublicacionPicker.getValue() == null) {
            mostrarError("Todos los campos son obligatorios.");
            return false;
        }

        if (!isbnField.getText().matches("\\d+")) {
            mostrarError("El ISBN debe ser solo números.");
            return false;
        }

        return true;
    }

    public void actualizarUI() {
        libroTable.getItems().clear();
        ObservableList<Libro> librosObservableList = FXCollections.observableArrayList(librosList);
        libroTable.setItems(librosObservableList);

        tituloField.clear();
        autorComboBox.getSelectionModel().clearSelection();
        editorialComboBox.getSelectionModel().clearSelection();
        anioPublicacionPicker.setValue(null);
        isbnField.clear();

        agregarButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        isbnField.setDisable(false);
    }
}