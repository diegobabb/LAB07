package com.example.lab01.ui.profesores.Profesor;

import androidx.lifecycle.ViewModel;

import com.example.lab01.AccesoDatos.ModelData;
import com.example.lab01.Logica.Profesor;

import java.util.ArrayList;

public class ProfesorViewModel extends ViewModel {

    public ArrayList<Profesor> arrayTeachers;

    public ProfesorViewModel() {
        this.arrayTeachers = ModelData.getInstance().getProfesorList();
    }

    public ArrayList<Profesor> getArrayTeachers() {
        return arrayTeachers;
    }
}