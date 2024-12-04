package com.axear.proyecto_final_objetos.Objects;

public class Prestamo {
    private int idPrestamo;
    private String idUsuario, idLibro, fechaPrestamo, fechaDevolucion;
    private int estado;

    public Prestamo(int idPrestamo, String idUsuario, String idLibro, String fechaPrestamo, String fechaDevolucion, int estado) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public int getIdPrestamo() { return idPrestamo; }

    public String getIdUsuario() { return idUsuario; }

    public String getIdLibro() { return idLibro; }

    public String getFechaPrestamo() { return fechaPrestamo; }

    public String getFechaDevolucion() { return fechaDevolucion; }

    public int getEstado() { return estado; }

    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public void setEstado(int estado) { this.estado = estado; }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }
}
