package com.axear.proyecto_final_objetos.DAO;

import com.axear.proyecto_final_objetos.Objects.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private final Connection connection;

    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Categoria> leerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categorias";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                categorias.add(new Categoria(
                        resultSet.getInt("idCategoria"),
                        resultSet.getString("Descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public void escribirCategorias(List<Categoria> categorias) {
        String deleteSql = "DELETE FROM Categorias";
        String insertSql = "INSERT INTO Categorias (idCategoria, Descripcion) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Categoria categoria : categorias) {
                    preparedStatement.setInt(1, categoria.getIdCategoria());
                    preparedStatement.setString(2, categoria.getDescripcion());
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
