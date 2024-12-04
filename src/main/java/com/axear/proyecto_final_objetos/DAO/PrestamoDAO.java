package com.axear.proyecto_final_objetos.DAO;

import com.axear.proyecto_final_objetos.Objects.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private final Connection connection;

    public PrestamoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Prestamo> leerPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                prestamos.add(new Prestamo(
                        resultSet.getInt("idPrestamo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("idLibro"),
                        resultSet.getString("FechaPrestamo"),
                        resultSet.getString("FechaDevolucion"),
                        resultSet.getInt("Estado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }

    public void escribirPrestamos(List<Prestamo> prestamos) {
        String deleteSql = "DELETE FROM Prestamos";
        String insertSql = "INSERT INTO Prestamos (idPrestamo, Usuario, Libro, FechaPrestamo, FechaDevolucion, Estado) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSql);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Prestamo prestamo : prestamos) {
                    preparedStatement.setInt(1, prestamo.getIdPrestamo());
                    preparedStatement.setString(2, prestamo.getIdUsuario());
                    preparedStatement.setString(3, prestamo.getIdLibro());
                    preparedStatement.setString(4, prestamo.getFechaPrestamo());
                    preparedStatement.setString(5, prestamo.getFechaDevolucion());
                    preparedStatement.setInt(6, prestamo.getEstado());

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