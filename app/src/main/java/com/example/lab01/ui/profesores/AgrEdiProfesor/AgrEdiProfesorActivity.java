package com.example.lab01.ui.profesores.AgrEdiProfesor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lab01.AccesoDatos.ModelData;
import com.example.lab01.Logica.Curso;
import com.example.lab01.Logica.Profesor;
import com.example.lab01.MenuPrincipal;
import com.example.lab01.R;
import com.example.lab01.ui.profesores.Profesor.ProfesorFragment;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class AgrEdiProfesorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editName;
    EditText editCedula;
    EditText editPhone;
    EditText editEmail;
    Spinner cursos_spinner;
    Button button;
    int flat_edit = -1;
    private AgrEdiProfesorViewModel agrEdiProfesorViewModel;

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
                    ArrayList<String> nombreCursos = new ArrayList<>();
                    String profesorCurso = profesor.getCurso();
                    int i = 0, selected = -1;
                    for (Curso curso : ModelData.getInstance().getCursoList()) {
                        if (profesorCurso.equals(curso.getCodigo()))
                            selected = i;
                        nombreCursos.add(curso.getNombre());
                        i++;
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(this, R.layout.spinner_item, nombreCursos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cursos_spinner.setAdapter(adapter);
                    cursos_spinner.setSelection(selected);
                }
            } else {
                setTitle("Agregar Profesor");
                ArrayList<String> nombreCursos = new ArrayList<>();
                for (Curso curso : ModelData.getInstance().getCursoList())
                    nombreCursos.add(curso.getNombre());
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(this, R.layout.spinner_item, nombreCursos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursos_spinner.setAdapter(adapter);
            }
        }
    }

    private void initAgrEdiProfesorActivity(){
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
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }

    private void guardarProfesor() {
        int cursoCodigo = cursos_spinner.getSelectedItemPosition();
        String codigo = ModelData.getInstance().getCursoList().get(cursoCodigo).getCodigo();
        Profesor profesorEditado = new Profesor(
                editCedula.getText().toString(),
                editName.getText().toString(),
                editEmail.getText().toString(),
                editPhone.getText().toString(),
                codigo
        );
        ModelData.getInstance().getProfesorList().add(profesorEditado);
    }

    private void editProfesor(){
        int cursoCodigo = cursos_spinner.getSelectedItemPosition();
        String codigo = ModelData.getInstance().getCursoList().get(cursoCodigo).getCodigo();
        Profesor profesorEditado = new Profesor(
                editCedula.getText().toString(),
                editName.getText().toString(),
                editEmail.getText().toString(),
                editPhone.getText().toString(),
                codigo
        );
        int position = getIntent().getIntExtra(ProfesorFragment.INDEX_PROFESOR, -1);
        ModelData.getInstance().getProfesorList().set(position, profesorEditado);
    }
}
