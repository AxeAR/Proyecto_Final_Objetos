package com.axear.proyecto_final_objetos.DAO;

import com.axear.proyecto_final_objetos.Objects.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private final Connection connection;

    public LibroDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Libro> leerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libro";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                libros.add(new Libro(
                        resultSet.getString("ISBN"),
                        resultSet.getString("Titulo"),
                        resultSet.getInt("Autor"),
                        resultSet.getInt("Editorial"),
                        resultSet.getInt("AnioPublicacion"),
                        resultSet.getInt("Disponible")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    public void escribirLibros(List<Libro> libros) {
        String deleteSql = "DELETE FROM Libro";
        String insertSql = "INSERT INTO Libro (ISBN, Titulo, Autor, Editorial, AnioPublicacion, Disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Libro libro : libros) {
                    preparedStatement.setString(1, libro.getISBN());
                    preparedStatement.setString(2, libro.getTitulo());
                    preparedStatement.setInt(3, libro.getAutor());
                    preparedStatement.setInt(4, libro.getEditorial());
                    preparedStatement.setInt(5, libro.getAnioPublicacion());
                    preparedStatement.setInt(6, libro.getDisponible());

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}