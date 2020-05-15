package com.example.lab01.Logica;

import java.io.Serializable;

/**
 * Created by Luis Carrillo Rodriguez on 26/3/2018.
 */

public class Usuario implements Serializable {

    private String cedula;
    private String clave;
    private String correo;

    public Usuario() {
        this.cedula = "";
        this.clave = "";
        this.correo = "";
    }

    public Usuario(String correo, String cedula, String clave) {
        this.correo = correo;
        this.clave = clave;
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", contraseña='" + clave + '\'' +
                ", cedula='" + cedula + '\'' +
                '}';
    }
}
