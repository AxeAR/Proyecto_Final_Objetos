package com.axear.proyecto_final_objetos.Objects;

public class Categoria {
    private int idCategoria;
    private String Descripcion;

    public Categoria(int idCategoria, String descripcion) {
        this.idCategoria = idCategoria;
        Descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
