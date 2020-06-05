package com.example.lab01.AccesoDatos;

import com.example.lab01.Logica.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26/03/2018.
 */

public class ModelData {

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
        carreraList = new ArrayList<>();
        users = new ArrayList<>();
        prepareCarreraData();
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


    private void prepareUsuarioData() {
        users.add(new Usuario("@admin", "402400313", "admin"));
        users.add(new Usuario("@admin2", "402400312", "admin2"));
        users.add(new Usuario("@admin3", "402400311", "admin3"));

    }

    public ArrayList<Carrera> getCarreraList() {
        return carreraList;
    }
}
