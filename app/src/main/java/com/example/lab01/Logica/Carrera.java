package com.example.lab01.Logica;

import java.io.Serializable;

public class Carrera implements Serializable {
    private int id;
    private String codigo;
    private String nombre;

    public Carrera() {
        this.id = 0;
        this.codigo = "";
        this.nombre = "";
    }

    public Carrera(int id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
