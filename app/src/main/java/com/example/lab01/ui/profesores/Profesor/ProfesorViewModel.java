package com.example.lab01.ui.profesores.Profesor;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import com.example.lab01.Logica.Profesor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfesorViewModel extends ViewModel {

    private ArrayList<Profesor> arrayTeachers;


    public ProfesorViewModel() {
        this.arrayTeachers = new ArrayList<>();
    }

    public ArrayList<Profesor> getArrayTeachers() {
        return arrayTeachers;
    }

}