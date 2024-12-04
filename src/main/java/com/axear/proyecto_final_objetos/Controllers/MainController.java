package com.axear.proyecto_final_objetos.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab usuariosTab;
    @FXML
    private Tab librosTab;
    @FXML
    private Tab autoresTab;
    @FXML
    private Tab categoriasTab;
    @FXML
    private Tab editorialesTab;
    @FXML
    private Tab prestamosTab;

    private UsuariosController usuariosController;
    private LibrosController librosController;
    private AutoresController autoresController;
    private CategoriasController categoriasController;
    private EditorialesController editorialesController;
    private PrestamosController prestamosController;

    @FXML
    public void initialize() {
        loadTabContent("/com/axear/proyecto_final_objetos/usuariosTab.fxml", usuariosTab, "usuarios");
        loadTabContent("/com/axear/proyecto_final_objetos/librosTab.fxml", librosTab, "libros");
        loadTabContent("/com/axear/proyecto_final_objetos/autoresTab.fxml", autoresTab, "autores");
        loadTabContent("/com/axear/proyecto_final_objetos/categoriasTab.fxml", categoriasTab, "categorias");
        loadTabContent("/com/axear/proyecto_final_objetos/editorialesTab.fxml", editorialesTab, "editoriales");
        loadTabContent("/com/axear/proyecto_final_objetos/prestamosTab.fxml", prestamosTab, "prestamos");

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newTab) -> {
            // Detect deselection
//            if (oldValue != null) {
//                if (oldValue == usuariosTab) {
//                    UsuariosController.escribirBD();
//                    System.out.println("Deselected usuarios");
//                } else if (oldValue == librosTab) {
//                    // onTabDeselected("libros");
//                    System.out.println("Deselected libros");
//                } else if (oldValue == autoresTab) {
//                    AutoresController.escribirBD();
//                    System.out.println("Deselected autores");
//                } else if (oldValue == categoriasTab) {
//                    CategoriasController.escribirBD();
//                    System.out.println("Deselected categorias");
//                } else if (oldValue == editorialesTab) {
//                    EditorialesController.escribirBD();
//                    System.out.println("Deselected editoriales");
//                } else if (oldValue == prestamosTab) {
//                    System.out.println("Deselected prestamos");
//                }
//            }

            if (newTab == usuariosTab) {
                usuariosController.actualizarUI();
            } else if (newTab == librosTab) {
                System.out.println("libtab");
                librosController.actualizarUI();
            } else if (newTab == autoresTab) {
                autoresController.actualizarUI();
            } else if (newTab == categoriasTab) {
                categoriasController.actualizarUI();
            } else if (newTab == editorialesTab) {
                editorialesController.actualizarUI();
            } else if (newTab == prestamosTab) {
                prestamosController.actualizarUI();
            }
        });
    }

    private void loadTabContent(String fxmlPath, Tab tab, String controllerName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            StackPane tabContent = new StackPane();
            tabContent.getChildren().add(loader.load());

            switch (controllerName) {
                case "usuarios":
                    usuariosController = loader.getController();
                    break;
                case "libros":
                    librosController = loader.getController();
                    break;
                case "autores":
                    autoresController = loader.getController();
                    break;
                case "categorias":
                    categoriasController = loader.getController();
                    break;
                case "editoriales":
                    editorialesController = loader.getController();
                    break;
                case "prestamos":
                    prestamosController = loader.getController();
                    break;
            }

            tab.setContent(tabContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
