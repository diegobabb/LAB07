package com.example.lab01.Logica;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by User on 23/03/2018.
 */

public class Profesor implements Serializable {

    private String cedula;
    private String nombre;
    private String email;
    private String telefono;
    private String curso;

    public Profesor(String cedula, String nombre, String email, String telefono, String curso) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.curso = curso;
    }

    public Profesor(String cedula, String nombre, String email, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.curso = "";
    }

    public Profesor() {
        this.cedula = "";
        this.nombre = "";
        this.email = "";
        this.telefono = "";
        this.curso = "";
    }


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("cedula=%s&nombre=%s&correo=%s&telefono=%s&curso=%s", cedula, nombre, email, telefono, curso);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;
        Profesor profesor = (Profesor) o;
        return getCedula().equals(profesor.getCedula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCedula());
    }
}
