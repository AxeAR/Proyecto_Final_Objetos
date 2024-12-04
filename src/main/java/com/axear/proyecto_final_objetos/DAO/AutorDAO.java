package com.axear.proyecto_final_objetos.DAO;

import com.axear.proyecto_final_objetos.Objects.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    private final Connection connection;

    public AutorDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Autor> leerAutores() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM Autores";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                autores.add(new Autor(
                        resultSet.getString("Nombre"),
                        resultSet.getString("APaterno"),
                        resultSet.getString("AMaterno"),
                        resultSet.getInt("idAutor")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autores;
    }

    public void escribirAutores(List<Autor> autores) {
        String deleteSql = "DELETE FROM Autores";
        String insertSql = "INSERT INTO Autores (Nombre, APaterno, AMaterno, idAutor) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Autor autor : autores) {
                    preparedStatement.setString(1, autor.getNombre());
                    preparedStatement.setString(2, autor.getAPaterno());
                    preparedStatement.setString(3, autor.getAMaterno());
                    preparedStatement.setInt(4, autor.getIdAutor());
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
    }
}
