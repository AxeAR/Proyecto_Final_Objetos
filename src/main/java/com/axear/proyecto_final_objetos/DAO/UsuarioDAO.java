package com.axear.proyecto_final_objetos.DAO;
import com.axear.proyecto_final_objetos.Objects.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Usuario> leerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                usuarios.add(new Usuario(
                        resultSet.getString("Nombre"),
                        resultSet.getString("APaterno"),
                        resultSet.getString("AMaterno"),
                        resultSet.getString("Direccion"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("FechaRegistro"),
                        resultSet.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void escribirUsuarios(List<Usuario> usuarios) {
        String deleteSql = "DELETE FROM Usuario";
        String sql = "INSERT INTO Usuario (Nombre, APaterno, AMaterno, Direccion, Telefono, FechaRegistro, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Usuario usuario : usuarios) {
                    preparedStatement.setString(1, usuario.getNombre());
                    preparedStatement.setString(2, usuario.getAPaterno());
                    preparedStatement.setString(3, usuario.getAMaterno());
                    preparedStatement.setString(4, usuario.getDireccion());
                    preparedStatement.setString(5, usuario.getTelefono());
                    preparedStatement.setString(6, usuario.getFechaRegistro());
                    preparedStatement.setString(7, usuario.getEmail());

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}