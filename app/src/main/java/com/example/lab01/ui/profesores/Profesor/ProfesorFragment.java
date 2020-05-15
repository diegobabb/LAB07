package com.example.lab01.ui.profesores.Profesor;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.lab01.AccesoDatos.ModelData;
import com.example.lab01.Logica.Profesor;
import com.example.lab01.R;
import com.example.lab01.ui.MySwipeHelper;
import com.example.lab01.ui.profesores.AgrEdiProfesor.AgrEdiProfesorActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class ProfesorFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private ProfesorViewModel profesorViewModel;
    private ProfesorListAdapter adapter;
    public static String EDIT_PROFESOR = "edit";
    public static String INDEX_PROFESOR = "index";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profesores, container, false);
        super.onCreate(savedInstanceState);
        profesorViewModel = ViewModelProviders.of(this).get(ProfesorViewModel.class);

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

        adapter = new ProfesorListAdapter(ModelData.getInstance().getProfesorList());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        MySwipeHelper simpleCallback =
                new MySwipeHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, adapter, recyclerView, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setHasOptionsMenu(true);
        return root;
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
}


