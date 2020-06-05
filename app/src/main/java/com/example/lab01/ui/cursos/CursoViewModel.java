package com.example.lab01.ui.cursos;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab01.Logica.Curso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

enum SERVICIOS_CURSOS {
    LISTAR_CURSOS, ELIMINAR_CURSO, AGREGAR_CURSO, EDITAR_CURSO
};


public class CursoViewModel extends ViewModel {

    public AsyncTask.Status STATUS = null;
    private MutableLiveData<String> mText;
    private ArrayList<Curso> cursos;
    private ArrayList<Curso> cursosFiltrados;

    public CursoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el apartado de Cursos");
        this.cursos = this.cursosFiltrados = new ArrayList<>();
        solicitarServicio(SERVICIOS_CURSOS.LISTAR_CURSOS, null);
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public ArrayList<Curso> getCursosFiltrados() {
        return cursosFiltrados;
    }

    public void setCursosFiltrados(ArrayList<Curso> cursos) {
        this.cursosFiltrados = cursos;
    }

    public boolean agregarCurso(Curso curso) {
        if (solicitarServicio(SERVICIOS_CURSOS.AGREGAR_CURSO, curso) == 1) {
            cursos.add(curso);
            return true;
        } else
            return false;
    }

    public boolean eliminarCurso(Curso c) {
        if (solicitarServicio(SERVICIOS_CURSOS.ELIMINAR_CURSO, c) == 1) {
            cursos.remove(c);
            cursosFiltrados.remove(c);
            return true;
        } else
            return false;
    }

    public int editarCurso(Curso curso) {
        int index = 0;
        for (Curso curso_ite : cursos) {
            if (curso_ite.getCodigo().trim().toLowerCase().equals(curso.getCodigo().trim().toLowerCase())) {
                if (solicitarServicio(SERVICIOS_CURSOS.EDITAR_CURSO, curso) == 1) {
                    cursos.remove(curso_ite);
                    cursosFiltrados.remove(curso_ite);
                    cursos.add(index, curso);
                    return index;
                } else
                    return -1;
            }
            index++;
        }
        return -1;
    }

    public int solicitarServicio(SERVICIOS_CURSOS servicio, Curso c) {

        class MyAsyncTask extends AsyncTask<String, String, String> {

            SERVICIOS_CURSOS servicio;
            String url;
            int response = 1;

            public MyAsyncTask(SERVICIOS_CURSOS s, Curso c) throws UnsupportedEncodingException {
                super();
                this.servicio = s;
                switch (s) {
                    case LISTAR_CURSOS:
                        url = "http://10.0.2.2:8084/LAB01-WEB/ServletCurso";
                        break;
                    case ELIMINAR_CURSO:
                        url = "http://10.0.2.2:8084/LAB01-WEB/ServletCurso" + "?codigo=" + c.getCodigo();
                        break;
                    case AGREGAR_CURSO:
                        url = "http://10.0.2.2:8084/LAB01-WEB/ServletCurso" + c.toStringURLRequest();
                        Log.d("URL:", c.toStringURLRequest());
                        break;
                    case EDITAR_CURSO:
                        url = "http://10.0.2.2:8084/LAB01-WEB/ServletCurso" + c.toStringURLRequest();
                        Log.d("URL:", c.toStringURLRequest());
                        break;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                STATUS = Status.RUNNING;

                // implement API in background and store the response in current variable
                String current = "";
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL(this.url);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        urlConnection.setDoOutput(false);
                        //Request Method
                        switch (this.servicio) {
                            case LISTAR_CURSOS:
                                urlConnection.setRequestMethod("GET");
                                break;
                            case EDITAR_CURSO:
                                urlConnection.setRequestMethod("PUT");
                                break;
                            case ELIMINAR_CURSO:
                                urlConnection.setRequestMethod("DELETE");
                                break;
                            case AGREGAR_CURSO:
                                urlConnection.setRequestMethod("POST");
                                break;
                        }

                        InputStream in = urlConnection.getInputStream();
                        InputStreamReader isw = new InputStreamReader(in);
                        int data = isw.read();
                        while (data != -1) {
                            current += (char) data;
                            data = isw.read();
                            System.out.print(current);
                        }
                        // return the data to onPostExecute method
                        return current;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }

                return current;
            }

            @Override
            protected void onPostExecute(String s) {

                if (this.servicio == SERVICIOS_CURSOS.LISTAR_CURSOS) {
                    try {
                        JSONArray array = new JSONArray(s.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject curso_json = array.getJSONObject(i);
                            Curso curso = new Curso(
                                    curso_json.getString("codigo"),
                                    curso_json.getString("nombre"),
                                    curso_json.getInt("credito"),
                                    curso_json.getInt("horas"),
                                    curso_json.getInt("carrera")
                            );
                            cursos.add(curso);
                        }
                        cursosFiltrados = cursos;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    response = Integer.parseInt(s.trim());

                CursoFragment.getAdapter().notifyDataSetChanged();
                STATUS = Status.FINISHED;
            }
        }

        try {
            MyAsyncTask task = new MyAsyncTask(servicio, c);
            task.execute();
            return task.response;
        } catch (Exception e) {
            return 0;
        }

    }

}
