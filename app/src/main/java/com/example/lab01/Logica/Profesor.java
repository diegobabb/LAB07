package com.example.lab01.Logica;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return "Profesor{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono=" + telefono +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;

        Profesor profesor = (Profesor) o;

        if (!getTelefono().equals(profesor.getTelefono())) return false;
        if (getCedula() != null ? !getCedula().equals(profesor.getCedula()) : profesor.getCedula() != null)
            return false;
        if (getNombre() != null ? !getNombre().equals(profesor.getNombre()) : profesor.getNombre() != null)
            return false;
        return getEmail() != null ? getEmail().equals(profesor.getEmail()) : profesor.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getCedula() != null ? getCedula().hashCode() : 0;
        result = 31 * result + (getNombre() != null ? getNombre().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getTelefono() != null ? getTelefono().hashCode() : 0);
        return result;
    }
}
