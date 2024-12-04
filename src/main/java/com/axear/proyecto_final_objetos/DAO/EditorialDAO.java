package com.axear.proyecto_final_objetos.DAO;

import com.axear.proyecto_final_objetos.Objects.Editorial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {
    private final Connection connection;

    public EditorialDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Editorial> leerEditoriales() {
        List<Editorial> editoriales = new ArrayList<>();
        String sql = "SELECT * FROM Editoriales";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                editoriales.add(new Editorial(
                        resultSet.getInt("idEditorial"),
                        resultSet.getString("Descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return editoriales;
    }

    public void escribirEditoriales(List<Editorial> editoriales) {
        String deleteSql = "DELETE FROM Editoriales";
        String insertSql = "INSERT INTO Editoriales (idEditorial, Descripcion) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Editorial editorial : editoriales) {
                    preparedStatement.setInt(1, editorial.getIdEditorial());
                    preparedStatement.setString(2, editorial.getDescripcion());
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