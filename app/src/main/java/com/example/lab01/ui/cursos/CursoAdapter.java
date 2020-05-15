package com.example.lab01.ui.cursos;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab01.Logica.Curso;
import com.example.lab01.R;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.MyViewHolder> implements Filterable {

    private CursoViewModel model;
    private CursoAdapterListener listener;  // Para eventos
    private ItemTouchHelper.SimpleCallback simpleCallback;
    private ItemTouchHelper itemTouchHelper;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView codigo;
        private TextView nombre;
        private TextView creditos;
        private TextView horas;
        private TextView carrera;

        MyViewHolder(View v) {
            super(v);
            codigo = v.findViewById(R.id.codigo_curso);
            nombre = v.findViewById(R.id.nombre_curso);
            creditos = v.findViewById(R.id.creditos_curso);
            horas = v.findViewById(R.id.horas_curso);
            carrera = v.findViewById(R.id.carrera_curso);
        }
    }

    public CursoAdapter(final CursoAdapterListener listener) {
        this.model = new CursoViewModel();
        this.listener = listener;
        this.simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Curso curso = getModel().getCursosFiltrados().get(position);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        listener.eliminar(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        listener.editar(position);
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY
                    , int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.parseColor("#7A1712"))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_24dp)
                        .addSwipeRightBackgroundColor(Color.parseColor("#7986CB"))
                        .addSwipeRightActionIcon(R.drawable.ic_edit_24dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public CursoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CursoAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_curso, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CursoAdapter.MyViewHolder holder, int position) {
        holder.nombre.setText(model.getCursosFiltrados().get(position).getNombre());
        holder.codigo.setText("NRC: " + model.getCursosFiltrados().get(position).getCodigo());
        holder.creditos.setText(model.getCursosFiltrados().get(position).getCreditos() + " creditos");
        holder.horas.setText(model.getCursosFiltrados().get(position).getHoras() + " horas");
        holder.carrera.setText(getCarreraQuemada(model.getCursosFiltrados().get(position).getCarrera()));
    }

    private String getCarreraQuemada(int id) {
        switch (id) {
            case 1:
                return "Ingenieria en Sistemas";
            case 2:
                return "Administracion de Empresas";
            case 3:
                return "Matematicas";
            case 4:
                return "Ingles";
            case 5:
                return "Educacion";
            default:
                return "Indefinido";
        }
    }
    @Override
    public int getItemCount() {
        return model.getCursosFiltrados().size();
    }

    public interface CursoAdapterListener {
        void eliminar(int pos);
        void editar(int pos);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    model.setCursosFiltrados(model.getCursos());
                } else {
                    ArrayList<Curso> filteredList = new ArrayList<>();
                    for (Curso row : model.getCursos()) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    model.setCursosFiltrados(filteredList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = model.getCursosFiltrados();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                model.setCursosFiltrados((ArrayList<Curso>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public CursoViewModel getModel() {
        return model;
    }

}