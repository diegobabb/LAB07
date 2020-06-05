package com.example.lab01.ui.cursos;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab01.Logica.Curso;
import com.example.lab01.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CursoFragment extends Fragment implements CursoAdapter.CursoAdapterListener {

    private CursoViewModel cursoViewModel;
    private RecyclerView recyclerView;
    private static CursoAdapter adapter;
    private SearchView busqueda;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cursoViewModel = ViewModelProviders.of(this).get(CursoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cursos, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.lista_cursos);
        setHasOptionsMenu(true);

        // Boton flotante
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCurso(view);
            }
        });

        // Elementos para el recicle view
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration hItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        hItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.divider)));
        recyclerView.addItemDecoration(hItemDecoration);

        // Creando el adapter
        adapter = new CursoAdapter(this);

        // Asignando el adapter
        recyclerView.setAdapter(adapter);

        waitForIt();

        return root;
    }

    private void crearCurso(View view) {
        Intent intent = new Intent(getActivity(), CursoCrearEditar.class);
        intent.putExtra("accion", "crear");
        startActivity(intent);
    }

    @Override
    public void eliminar(final int pos) {
        final Curso curso = adapter.getModel().getCursosFiltrados().get(pos);
        final int pos2 = adapter.getModel().getCursos().indexOf(curso);
        if (adapter.getModel().eliminarCurso(curso)) {
            adapter.notifyItemRemoved(pos);
            Snackbar.make(recyclerView, curso.getNombre(), Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                private boolean flag = true;

                @Override
                public void onClick(View view) {
                    if (flag) {
                        adapter.getModel().getCursos().add(pos2, curso);
                        if (adapter.getModel().getCursos() != adapter.getModel().getCursosFiltrados())
                            adapter.getModel().getCursosFiltrados().add(pos, curso);
                        cursoViewModel.solicitarServicio(SERVICIOS_CURSOS.AGREGAR_CURSO, curso);
                        adapter.notifyItemInserted(pos);
                    }
                    flag = false;
                }
            }).show();
        } else {
            mostrarMensaje("ERROR", "Ocurrio un error inesperado.");
        }
    }

    @Override
    public void editar(int pos) {
        final Curso curso = adapter.getModel().getCursosFiltrados().get(pos);
        Intent intent = new Intent(getActivity(), CursoCrearEditar.class);
        intent.putExtra("accion", "editar");
        intent.putExtra("curso_a_editar", curso);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // FILTRO
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    public static CursoAdapter getAdapter() {
        return adapter;
    }

    private void waitForIt() {
        class Wait extends AsyncTask<String, String, Boolean> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // display a progress dialog for good user experiance
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                while (cursoViewModel.STATUS == Status.RUNNING) ;
                return true;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                progressDialog.dismiss();
            }

        }

        Wait myAsyncTasks = new Wait();
        myAsyncTasks.execute();
    }


    // Muestra un cuadro de dialogo con titulo/mensaje
    private void mostrarMensaje(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("entendido!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }
}
