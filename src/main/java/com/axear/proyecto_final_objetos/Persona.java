package com.axear.proyecto_final_objetos;

public class Persona {
    String Nombre, APaterno, AMaterno;

    public Persona(String nombre, String APaterno, String AMaterno) {
        Nombre = nombre;
        this.APaterno = APaterno;
        this.AMaterno = AMaterno;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getAPaterno() {
        return APaterno;
    }

    public void setAPaterno(String APaterno) {
        this.APaterno = APaterno;
    }

    public String getAMaterno() {
        return AMaterno;
    }

    public void setAMaterno(String AMaterno) {
        this.AMaterno = AMaterno;
    }
}
