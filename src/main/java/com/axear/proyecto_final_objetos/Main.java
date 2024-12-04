package com.axear.proyecto_final_objetos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Administrador biblioteca");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:libreria.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            System.out.printf("Error al inicializar BD");
            System.exit(1);
        }

        launch();
    }
}