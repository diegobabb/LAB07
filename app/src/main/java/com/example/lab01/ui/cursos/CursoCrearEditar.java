package com.example.lab01.ui.cursos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.example.lab01.Logica.Carrera;
import com.example.lab01.Logica.Curso;
import com.example.lab01.MenuPrincipal;
import com.example.lab01.R;

import java.util.ArrayList;

public class CursoCrearEditar extends AppCompatActivity {

    private Button button;
    private EditText editText_codigo;
    private EditText editText_nombre;
    private EditText editText_creditos;
    private EditText editText_horas;
    private Spinner spinner_carrera;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_crear);

        // Inicialziacion de variables
        this.editText_codigo = (EditText) findViewById(R.id.curso_nuevo_codigo);
        this.editText_nombre = (EditText) findViewById(R.id.curso_nuevo_nombre);
        this.editText_creditos = (EditText) findViewById(R.id.curso_nuevo_creditos);
        this.editText_horas = (EditText) findViewById(R.id.curso_nuevo_horas);
        this.spinner_carrera = (Spinner) findViewById(R.id.curso_nuevo_carrera);
        this.button = (Button) findViewById(R.id.enviar_crear_curso);

        // Spinner con las carreras quemadas
        ArrayList<Carrera> arraySpinner = ModelData.getInstance().getCarreraList();
        Spinner s = (Spinner) findViewById(R.id.curso_nuevo_carrera);
        ArrayAdapter<Carrera> adapter = new ArrayAdapter<Carrera>(this, R.layout.spinner_item_curso, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        // acccion que llega del fragment. Puede ser editar o crear.
        String accion = (String) getIntent().getSerializableExtra("accion");

        // Manejo de errores en tiempo real
        listenersErrores();

        if (accion.equals("crear")) {
            setTitle("Agregar Curso");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Curso curso = obtenerCursoDeCampos();
                    if (curso != null) {
                        Intent intent = new Intent(CursoCrearEditar.this, MenuPrincipal.class);
                        CursoFragment.getAdapter().getModel().agregarCurso(curso);
                        CursoFragment.getAdapter().notifyItemInserted(CursoFragment.getAdapter().getItemCount());
                        intent.putExtra("vamos_a_cursos", "si");
                        startActivity(intent);
                    }
                }
            });
        } else if (accion.equals("editar")) {
            setTitle("Editar curso");
            button.setText("Guardar");
            this.editText_codigo.setFocusable(false);
            this.editText_codigo.setClickable(false);
            final Curso nuevo_curso = (Curso) getIntent().getSerializableExtra("curso_a_editar");
            llenarCampos(nuevo_curso);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Curso curso = obtenerCursoDeCampos();
                    if (curso != null) {
                        Intent intent = new Intent(CursoCrearEditar.this, MenuPrincipal.class);
                        int pos = CursoFragment.getAdapter().getModel().editarCurso(curso);
                        CursoFragment.getAdapter().notifyItemChanged(pos);
                        intent.putExtra("vamos_a_cursos", "si");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    // Muestra un cuadro de dialogo con titulo/mensaje
    private void mostrarMensaje(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("entendido!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }

    // Metodo que se ejecuta en caso de editar. LLena los campos con el curso de entrada.
    private void llenarCampos(Curso nuevo_curso) {
        this.editText_codigo.setText(nuevo_curso.getCodigo());
        this.editText_nombre.setText(nuevo_curso.getNombre());
        this.editText_creditos.setText(String.valueOf(nuevo_curso.getCreditos()));
        this.editText_horas.setText(String.valueOf(nuevo_curso.getHoras()));
        this.spinner_carrera.setSelection(nuevo_curso.getCarrera() - 1);
    }

    // Recupera un curso de los campos del form, en caso de que hayan incorrectos devuelve null/.
    private Curso obtenerCursoDeCampos() {

        String codigo = this.editText_codigo.getText().toString();
        String nombre = this.editText_nombre.getText().toString();
        String creditos = this.editText_creditos.getText().toString();
        String horas = this.editText_horas.getText().toString();
        int carrera = this.spinner_carrera.getSelectedItemPosition() + 1;
        int creditos_f = (creditos.isEmpty()) ? 0 : Integer.parseInt(creditos);
        int horas_f = (horas.isEmpty()) ? 0 : Integer.parseInt(horas);

        // VALIDACIONES
        if (codigo.isEmpty() || nombre.isEmpty() || creditos.isEmpty() || horas.isEmpty()) {
            mostrarMensaje("ERROR", "Se detectaron campos vacios");
            return null;
        }
        if (codigo.length() > 5) {
            mostrarMensaje("ERROR", "Codigo no puede ser mayor a 5 caracteres");
            return null;
        }
        if (codigo.length() < 2) {
            mostrarMensaje("ERROR", "Codigo no puede ser menor a 2 caracteres");
            return null;
        }
        if (nombre.length() < 3) {
            mostrarMensaje("ERROR", "Nombre con extension muy corta");
            return null;
        }
        if (!creditos.matches("[0-9]+")) {
            mostrarMensaje("ERROR", "Creditos invalidos");
            return null;
        }
        if (!horas.matches("[0-9]+")) {
            mostrarMensaje("ERROR", "Horas invalidas");
            return null;
        }
        if (creditos_f > 10 || creditos_f < 2) {
            mostrarMensaje("ERROR", "Creditos invalidos");
            return null;
        }
        if (horas_f > 20 || horas_f < 2) {
            mostrarMensaje("ERROR", "Horas invalidas");
            return null;
        }

        return new Curso(codigo, nombre, creditos_f, horas_f, carrera);
    }

    // Para mostrar mensajes de error al cambiar el texto de un input
    private void listenersErrores() {
        // Codigo
        this.editText_codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String codigo = editText_codigo.getText().toString();
                if (codigo.isEmpty()) {
                    editText_codigo.setError("Campo vacio");
                } else if (codigo.length() > 5 || codigo.length() < 2) {
                    editText_codigo.setError("Codigo Invalido");
                } else {
                    editText_codigo.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        // Nombre
        this.editText_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String nombre = editText_nombre.getText().toString();
                if (nombre.isEmpty()) {
                    editText_nombre.setError("Campo vacio");
                } else if (nombre.length() < 3) {
                    editText_nombre.setError("Nombre invalido");
                } else {
                    editText_nombre.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        // Creditos
        this.editText_creditos.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String creditos = editText_creditos.getText().toString();
                int c;
                if (creditos.isEmpty())
                    c = 0;
                else
                    c = Integer.parseInt(creditos);

                if (creditos.isEmpty()) {
                    editText_creditos.setError("Campo vacio");
                } else if (c < 2 || c > 10) {
                    editText_creditos.setError("Numero invalido");
                } else {
                    editText_creditos.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        // Horas
        this.editText_horas.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String horas = editText_horas.getText().toString();
                int h;
                if (horas.isEmpty())
                    h = 0;
                else
                    h = Integer.parseInt(horas);

                if (horas.isEmpty()) {
                    editText_horas.setError("Campo vacio");
                } else if (h < 2 || h > 20) {
                    editText_horas.setError("Numero invalido");
                } else {
                    editText_horas.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
}
