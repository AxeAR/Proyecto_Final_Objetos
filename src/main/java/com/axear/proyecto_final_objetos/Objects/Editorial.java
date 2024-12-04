package com.axear.proyecto_final_objetos.Objects;

public class Editorial {
    private int idEditorial;
    private String Descripcion;

    public Editorial(int idEditorial, String descripcion) {
        this.idEditorial = idEditorial;
        Descripcion = descripcion;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
