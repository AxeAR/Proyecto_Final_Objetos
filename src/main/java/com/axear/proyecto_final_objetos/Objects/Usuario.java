package com.axear.proyecto_final_objetos.Objects;

import com.axear.proyecto_final_objetos.Persona;

public class Usuario extends Persona {
    private String Direccion, Telefono, FechaRegistro, email;

    public Usuario(String nombre, String APaterno, String AMaterno, String direccion, String telefono, String fechaRegistro, String email) {
        super(nombre, APaterno, AMaterno);
        Direccion = direccion;
        Telefono = telefono;
        FechaRegistro = fechaRegistro;
        this.email = email;
    }

    public String getDireccion() {
        return Direccion;
    }
    public String getTelefono() {
        return Telefono;
    }
    public String getFechaRegistro() {
        return FechaRegistro;
    }
    public String getEmail() {
        return email;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
    public void setTelefono(String telefono) {
        Telefono = telefono;
    }
    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }
}
