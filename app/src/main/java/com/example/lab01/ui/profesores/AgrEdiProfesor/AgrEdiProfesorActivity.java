package com.example.lab01.ui.profesores.AgrEdiProfesor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.lab01.AccesoDatos.ModelData;
import com.example.lab01.Logica.Curso;
import com.example.lab01.Logica.Profesor;
import com.example.lab01.MenuPrincipal;
import com.example.lab01.R;
import com.example.lab01.ui.profesores.Profesor.ProfesorFragment;
import com.example.lab01.ui.profesores.Profesor.ProfesorViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

enum SERVICIOS_EDIT_SAVE_PROFESOR {
    EDITAR_PROFESOR,
    SAVE_PROFESOR,
    LISTAR_CURSOS
};

public class AgrEdiProfesorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editName, editCedula, editPhone, editEmail;
    Spinner cursos_spinner;
    Button button;
    ProgressBar loadingProgressBar, progressBarSpinner;
    int flat_edit = -1;
    private AgrEdiProfesorViewModel agrEdiProfesorViewModel;
    String profesorCurso;
    ArrayAdapter<String> adapter;
    ArrayList<Curso> cursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agr_edi_profesor);
        agrEdiProfesorViewModel = new AgrEdiProfesorViewModel();
        initAgrEdiProfesorActivity();
        agrEdiProfesorViewModel.getAgrEdiProfesorFormState().observe(this, new Observer<AgrEdiProfesorFormState>() {
            @Override
            public void onChanged(@Nullable AgrEdiProfesorFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                button.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    editName.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getCedulaError() != null) {
                    editCedula.setError(getString(loginFormState.getCedulaError()));
                }
                if (loginFormState.getPhoneError() != null) {
                    editPhone.setError(getString(loginFormState.getPhoneError()));
                }
                if (loginFormState.getEmailError() != null) {
                    editEmail.setError(getString(loginFormState.getEmailError()));
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                Profesor profesor = (Profesor) intent.getSerializableExtra(ProfesorFragment.EDIT_PROFESOR);
                if (profesor != null) {
                    setTitle("Editar Profesor");
                    flat_edit = 1;
                    editName.setText(profesor.getNombre());
                    editCedula.setText(profesor.getCedula());
                    editCedula.setEnabled(false);
                    editPhone.setText(profesor.getTelefono());
                    editEmail.setText(profesor.getEmail());
                    profesorCurso = profesor.getCurso();
                }
            } else {
                setTitle("Agregar Profesor");
                profesorCurso = "-1";
            }
        }
        solicitarServicio(SERVICIOS_EDIT_SAVE_PROFESOR.LISTAR_CURSOS, null);
    }

    private void initAgrEdiProfesorActivity(){
        loadingProgressBar = findViewById(R.id.progressBar);
        progressBarSpinner = findViewById(R.id.progressBarSpinner);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                agrEdiProfesorViewModel.AgrEdiProfesorDataChanged(
                        editName.getText().toString(),
                        editCedula.getText().toString(),
                        editPhone.getText().toString(),
                        editEmail.getText().toString()
                        );
            }
        };
        editName = findViewById(R.id.editTextName);
        editName.addTextChangedListener(afterTextChangedListener);
        editCedula = findViewById(R.id.editTextCedula);
        editCedula.addTextChangedListener(afterTextChangedListener);
        editPhone = findViewById(R.id.editPhone);
        editPhone.addTextChangedListener(afterTextChangedListener);
        editEmail = findViewById(R.id.editEmail);
        editEmail.addTextChangedListener(afterTextChangedListener);
        cursos_spinner = findViewById(R.id.cursos_spinner);
        button = findViewById(R.id.buttonGuardar);
        button.setOnClickListener(this);
        button.setEnabled(false);
        cursos = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<String>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cursos_spinner.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        if (flat_edit == 1)
            CustomIntent.customType(this, "left-to-right");
        else
            CustomIntent.customType(this, "up-to-bottom");

    }

    @Override
    public void onClick(View v) {
        if (flat_edit == 1)
            editProfesor();
        else
            guardarProfesor();
    }

    private void guardarProfesor() {
        String codigo = cursos.get(cursos_spinner.getSelectedItemPosition()).getCodigo();
        Profesor profesorEditado = new Profesor(
                editCedula.getText().toString(),
                editName.getText().toString(),
                editEmail.getText().toString(),
                editPhone.getText().toString(),
                codigo
        );
        solicitarServicio(SERVICIOS_EDIT_SAVE_PROFESOR.SAVE_PROFESOR, profesorEditado);
    }

    private void editProfesor(){
        String codigo = cursos.get(cursos_spinner.getSelectedItemPosition()).getCodigo();
        Profesor profesorEditado = new Profesor(
                editCedula.getText().toString(),
                editName.getText().toString(),
                editEmail.getText().toString(),
                editPhone.getText().toString(),
                codigo
        );
        solicitarServicio(SERVICIOS_EDIT_SAVE_PROFESOR.EDITAR_PROFESOR, profesorEditado);
    }

    private AsyncTask.Status solicitarServicio(SERVICIOS_EDIT_SAVE_PROFESOR servicio, Profesor profesor) {

        @SuppressLint("StaticFieldLeak")
        class MyAsyncTask extends AsyncTask<String, String, String> {

            private SERVICIOS_EDIT_SAVE_PROFESOR servicio;
            private String url;

            private MyAsyncTask(SERVICIOS_EDIT_SAVE_PROFESOR s, Profesor profesor) {
                super();
                this.servicio = s;
                if (s == SERVICIOS_EDIT_SAVE_PROFESOR.LISTAR_CURSOS) {
                    url = "http://10.0.2.2:8084/LAB01-WEB/ServletCurso/listar";
                } else {
                    url = String.format(ProfesorFragment.DIRECCION_SERVLET, String.format(((SERVICIOS_EDIT_SAVE_PROFESOR.EDITAR_PROFESOR == s) ? "modificar?%s" : "insertar?%s"), profesor.toString()));
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (servicio == SERVICIOS_EDIT_SAVE_PROFESOR.LISTAR_CURSOS) {
                    progressBarSpinner.setVisibility(View.VISIBLE);
                } else {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                // implement API in background and store the response in current variable
                String current = "";
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL(this.url);

                        urlConnection = (HttpURLConnection) url
                                .openConnection();

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
                if (servicio == SERVICIOS_EDIT_SAVE_PROFESOR.LISTAR_CURSOS) {
                    try {
                        JSONArray array = new JSONArray(s.toString());
                        int seleccionado = 0;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject curso_json = array.getJSONObject(i);
                            Curso curso = new Curso(
                                    curso_json.getString("codigo"),
                                    curso_json.getString("nombre"),
                                    curso_json.getInt("credito"),
                                    curso_json.getInt("horas"),
                                    curso_json.getInt("carrera")
                            );
                            if (curso_json.getString("codigo").equals(profesorCurso)) {
                                seleccionado = i;
                            }
                            adapter.add(curso_json.getString("nombre"));
                            cursos.add(curso);
                        }
                        adapter.notifyDataSetChanged();
                        cursos_spinner.setSelection(seleccionado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBarSpinner.setVisibility(View.GONE);
                } else
                    goToMenuPrincipal();

            }
        }

        MyAsyncTask task = new MyAsyncTask(servicio, profesor);
        task.execute();
        return task.getStatus();
    }

    void goToMenuPrincipal() {
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }


}
