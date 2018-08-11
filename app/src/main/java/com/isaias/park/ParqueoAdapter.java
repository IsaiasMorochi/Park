package com.isaias.park;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.isaias.park.model.Parqueos;

import java.util.List;

public class ParqueoAdapter extends RecyclerView.Adapter<ParqueoAdapter.ViewHolder>{

    List<Parqueos> parqueo;
    Context context;
    ImageView imagen;

    /**
     * Constructor
     * @param contex
     * @param parqueo
     */
    public ParqueoAdapter(Context contex,List<Parqueos> parqueo){
        this.context = contex;
        this.parqueo = parqueo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parqueos,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(parqueo.get(position).NOMBRE);
        holder.direccion.setText(parqueo.get(position).DIRECCION);
        holder.horario.setText(parqueo.get(position).HORARIO);
    }

    @Override
    public int getItemCount() {
        return parqueo.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nombre,direccion,horario;
        ImageView imagen;

        public ViewHolder(View view){
            super(view);

            cardView = view.findViewById(R.id.cardview);
            nombre = view.findViewById(R.id.nombre);
            direccion = view.findViewById(R.id.direccion);
            horario = view.findViewById(R.id.horario);
            imagen = view.findViewById(R.id.imagen);
        }
    }
}
