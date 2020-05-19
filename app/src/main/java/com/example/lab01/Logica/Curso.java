package com.example.lab01.Logica;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by User on 21/03/2018.
 */

public class Curso implements Serializable {

    private String codigo;
    private String nombre;
    private int creditos;
    private int horas;
    private int carrera;

    public Curso() {
        this.codigo = "";
        this.nombre = "";
        this.creditos = 0;
        this.horas = 0;
        this.carrera = 0;
    }

    public Curso(String codigo, String nombre, int creditos, int horas, int carrera) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horas = horas;
        this.carrera = carrera;
    }

    public Curso(String codigo, String nombre, int creditos, int horas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horas = horas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getCarrera() {
        return this.carrera;
    }

    public void setCarrera(int carrera) {
        this.carrera = carrera;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return nombre;
        /*
        return "Curso{" +
                "nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", creditos=" + creditos +
                ", horas=" + horas +
                '}';
                */
    }

    public String toStringURLRequest() throws UnsupportedEncodingException {
        return "?" +
                "nombre=" + URLEncoder.encode(this.nombre , "UTF-8") +
                "&codigo=" + URLEncoder.encode(this.codigo , "UTF-8") +
                "&credito=" + this.creditos +
                "&horas=" +  this.horas +
                "&carrera=" + this.carrera;
    }


}
