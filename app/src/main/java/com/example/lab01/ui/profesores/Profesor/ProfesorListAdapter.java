package com.example.lab01.ui.profesores.Profesor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab01.Logica.Profesor;
import com.example.lab01.R;

import java.util.ArrayList;

public class ProfesorListAdapter extends RecyclerView.Adapter<ProfesorListAdapter.MyViewHolder> {

    private ArrayList<Profesor> mDataset;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView cedula;
        MyViewHolder(View c) {
            super(c);
            name = c.findViewById(R.id.name);
            cedula = c.findViewById(R.id.cedula);
        }
    }

    ProfesorListAdapter(ArrayList<Profesor> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_profesor, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getNombre());
        holder.cedula.setText(mDataset.get(position).getCedula());
    }

    public ArrayList<Profesor> getmDataset() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(ArrayList<Profesor> nuevaLista){
        this.mDataset = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
    }
}