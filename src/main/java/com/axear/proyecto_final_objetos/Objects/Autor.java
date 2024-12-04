package com.axear.proyecto_final_objetos.Objects;

import com.axear.proyecto_final_objetos.Persona;

public class Autor extends Persona {
    private int idAutor;

    public Autor(String nombre, String APaterno, String AMaterno, int idAutor) {
        super(nombre, APaterno, AMaterno);
        this.idAutor = idAutor;
    }

    public int getIdAutor() {
        return idAutor;
    }
}
