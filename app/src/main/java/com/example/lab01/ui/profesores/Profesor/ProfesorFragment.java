package com.example.lab01.ui.profesores.Profesor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lab01.AccesoDatos.ModelData;
import com.example.lab01.Logica.Profesor;
import com.example.lab01.R;
import com.example.lab01.ui.MySwipeHelper;
import com.example.lab01.ui.profesores.AgrEdiProfesor.AgrEdiProfesorActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

enum SERVICIOS_PROFESORES {
    LISTAR_PROFESORES,
    ELIMINAR_PROFESOR
};

public class ProfesorFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private static ProfesorListAdapter adapter;
    private ProfesorViewModel profesorViewModel;
    public static String EDIT_PROFESOR = "edit";
    public static String INDEX_PROFESOR = "index";
    private SwipeRefreshLayout swipeRefreshLayout;
    public static String DIRECCION_SERVLET = "http://10.0.2.2:8084/LAB01-WEB/ServletProfesor/%s";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profesores, container, false);
        super.onCreate(savedInstanceState);
        profesorViewModel = ViewModelProviders.of(this).get(ProfesorViewModel.class);

        swipeRefreshLayout = root.findViewById(R.id.swiperefreshlayout);

        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                solicitarServicio(SERVICIOS_PROFESORES.LISTAR_PROFESORES, null);
            }
        };

        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list_profesor);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration hItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        hItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.divider)));
        recyclerView.addItemDecoration(hItemDecoration);

        adapter = new ProfesorListAdapter(profesorViewModel.getArrayTeachers());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        MySwipeHelper simpleCallback =
                new MySwipeHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, adapter, recyclerView, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setHasOptionsMenu(true);
        refreshListener.onRefresh();
        return root;
    }

    public ProfesorViewModel getProfesorViewModel() {
        return profesorViewModel;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView profesorSearchView = (SearchView) search.getActionView();
        profesorSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Profesor> arrayTeachers = new ArrayList<>();
        for (Profesor teacher : profesorViewModel.getArrayTeachers()) {
            if (teacher.getNombre().toLowerCase().contains(userInput)||teacher.getCedula().toLowerCase().contains(userInput)) {
                arrayTeachers.add(teacher);
            }
        }
        adapter.updateList(arrayTeachers);
        return true;
    }

    @SuppressLint("ResourceType")
    public void moveToAgrEdiProfesorActivity(Profesor arg, int index) {
        Intent intent = new Intent(getActivity(), AgrEdiProfesorActivity.class);
        intent.putExtra(ProfesorFragment.EDIT_PROFESOR, arg);
        intent.putExtra(ProfesorFragment.INDEX_PROFESOR, index);
        startActivity(intent);
        CustomIntent.customType(getActivity(), "right-to-left");
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AgrEdiProfesorActivity.class);
        startActivity(intent);
        CustomIntent.customType(getActivity(), "bottom-to-up");
    }

    private AsyncTask.Status solicitarServicio(SERVICIOS_PROFESORES servicio, Profesor profesor) {

        @SuppressLint("StaticFieldLeak")
        class MyAsyncTask extends AsyncTask<String, String, String> {

            private SERVICIOS_PROFESORES servicio;
            private String url;

            private MyAsyncTask(SERVICIOS_PROFESORES s, Profesor profesor) {
                super();
                this.servicio = s;
                switch (s) {
                    case LISTAR_PROFESORES:
                        url = String.format(DIRECCION_SERVLET, "listar");
                        break;
                    case ELIMINAR_PROFESOR:
                        url = String.format(DIRECCION_SERVLET, String.format("eliminar?cedula=%s", profesor.getCedula()));
                        break;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                switch (this.servicio) {
                    case LISTAR_PROFESORES: {
                        try {
                            JSONArray array = new JSONArray(s.toString());
                            profesorViewModel.getArrayTeachers().clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject curso_json = array.getJSONObject(i);
                                profesorViewModel.getArrayTeachers().add(new Profesor(
                                        curso_json.getString("cedula"),
                                        curso_json.getString("nombre"),
                                        curso_json.getString("email"),
                                        curso_json.getString("telefono"),
                                        curso_json.getString("curso")
                                ));
                            }
                            adapter.updateList(profesorViewModel.getArrayTeachers());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    break;
                    case ELIMINAR_PROFESOR:
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        MyAsyncTask task;
        switch (servicio) {
            case LISTAR_PROFESORES:
                task = new MyAsyncTask(servicio, null);
                break;
            case ELIMINAR_PROFESOR:
                task = new MyAsyncTask(servicio, profesor);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + servicio);
        }
        task.execute();
        return task.getStatus();
    }

    public void removeProfesor(Profesor aux) {
        solicitarServicio(SERVICIOS_PROFESORES.ELIMINAR_PROFESOR, aux);
    }
}


