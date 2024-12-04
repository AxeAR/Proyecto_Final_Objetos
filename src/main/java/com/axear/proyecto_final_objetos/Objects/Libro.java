package com.axear.proyecto_final_objetos.Objects;

public class Libro {
    String ISBN, Titulo;
    int Autor, Editorial, AnioPublicacion, Disponible;

    public Libro(String ISBN, String Titulo, int Autor, int Editorial, int AnioPublicacion, int Disponible) {
        this.ISBN = ISBN;
        this.Titulo = Titulo;
        this.Autor = Autor;
        this.Editorial = Editorial;
        this.AnioPublicacion = AnioPublicacion;
        this.Disponible = Disponible;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public int getAutor() {
        return Autor;
    }

    public void setAutor(int autor) {
        Autor = autor;
    }

    public int getEditorial() {
        return Editorial;
    }

    public void setEditorial(int editorial) {
        Editorial = editorial;
    }

    public int getAnioPublicacion() {
        return AnioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        AnioPublicacion = anioPublicacion;
    }

    public int getDisponible() {
        return Disponible;
    }
}
