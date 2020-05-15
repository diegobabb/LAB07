package com.example.lab01.AccesoDatos;

import com.example.lab01.Logica.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26/03/2018.
 */

public class ModelData {

    private ArrayList<Curso> cursoList;
    private ArrayList<Profesor> profesorList;
    private ArrayList<Usuario> users;
    private ArrayList<Carrera> carreraList;

    private static ModelData instance = null;

    public static ModelData getInstance(){
        if ( instance == null){
            instance = new ModelData();
            return instance;
        }
        return instance;
    }

    private ModelData() {
        cursoList = new ArrayList<>();
        profesorList = new ArrayList<>();
        carreraList = new ArrayList<>();
        users = new ArrayList<>();
        prepareCarreraData();
        prepareCursoData();
        prepareProfesorData();
        prepareUsuarioData();
    }

    private void prepareCarreraData() {

        Carrera carrera = new Carrera(1, "EIF", "Ingenieria En Sistemas");
        carreraList.add(carrera);

        carrera = new Carrera(2, "ADM", "Administracion de Empresas");
        carreraList.add(carrera);

        carrera = new Carrera(3, "MAT", "Matematicas");
        carreraList.add(carrera);

        carrera = new Carrera(4, "LIX", "Ingles");
        carreraList.add(carrera);

        carrera = new Carrera(5, "EDU", "Educacion");
        carreraList.add(carrera);

    }

    private void prepareCursoData() {
        Curso curso = new Curso("ST", "Soporte", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("FD", "Fundamentos", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("PG1", "Programacion I", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("PG2", "Programacion II", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("PG3", "Programacion III", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("PG4", "Programacion IV", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("EDA", "Estructuras Datos", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("EDI", "Estructuras Discretas", 3, 4, 1);
        cursoList.add(curso);

        curso = new Curso("MV", "Moviles", 3, 4, 1);
        cursoList.add(curso);
        curso = new Curso("PP", "Paradigmas", 3, 4, 1);
        cursoList.add(curso);

        curso = new Curso("AQ", "Arquitectura", 3, 4, 1);
        cursoList.add(curso);

        curso = new Curso("RD", "Redes", 3, 4, 1);
        cursoList.add(curso);

        curso = new Curso("FAD", "Fundamentos de Administracion", 3, 4, 2);
        cursoList.add(curso);
        curso = new Curso("C1", "Contabilidad I", 3, 4, 3);
        cursoList.add(curso);

        curso = new Curso("FF", "Fundamentos de Fisica", 3, 4, 3);
        cursoList.add(curso);

        curso = new Curso("F1", "Fisica I", 3, 4, 3);
        cursoList.add(curso);

        curso = new Curso("FM", "Fundamentos de Matematica", 3, 4, 3);
        cursoList.add(curso);

        curso = new Curso("HB1", "Historia Basica I", 3, 4, 5);
        cursoList.add(curso);
    }

    private void prepareProfesorData() {
        profesorList.add(new Profesor("402400312", "Juan", "juan@juan.juan", "87656842", "PG1"));
        profesorList.add(new Profesor("402400312", "Mario", "mario@mario.mario", "78956418", "PG2"));
        profesorList.add(new Profesor("403010301", "Jesus", "jesus@jesus.jesus", "97854613", "PG4"));
        profesorList.add(new Profesor("402400818", "Pedro", "Pedro@Pedro.Pedro", "87656842", "PG1"));
        profesorList.add(new Profesor("402405512", "Diego", "Diego@Diego.Diego", "78956418", "PG2"));
        profesorList.add(new Profesor("403010333", "Rodrigo", "Rodrigo@Rodrigo.Rodrigo", "97854613", "PG4"));
    }

    private void prepareUsuarioData() {
        users.add(new Usuario("@admin", "402400313", "admin"));
        users.add(new Usuario("@admin2", "402400312", "admin2"));
        users.add(new Usuario("@admin3", "402400311", "admin3"));

    }

    public ArrayList<Curso> getCursoList() {
        return cursoList;
    }

    public ArrayList<Profesor> getProfesorList() {
        return profesorList;
    }

    public ArrayList<Usuario> getUsuariosList() {
        return users;
    }

    public void setCursoList(ArrayList<Curso> cursoList) {
        this.cursoList = cursoList;
    }

    public void setProfesorList(ArrayList<Profesor> profesorList) {
        this.profesorList = profesorList;
    }

    public void setUsuarioList(ArrayList<Usuario> usuarioList) {
        this.users = usuarioList;
    }

    public ArrayList<Carrera> getCarreraList() {
        return carreraList;
    }
}
