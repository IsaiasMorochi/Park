package com.isaias.park;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isaias.park.model.Parqueos;

import java.util.ArrayList;
import java.util.List;


public class ParqueoActivity extends AppCompatActivity {

    List<Parqueos> parqueList;

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Parqueos,ParqueoAdapter.ViewHolder> adapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parqueo);

        createData();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        //crear el linearLayoutManager y recibe su contexto
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //definimos la orientacion
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //definimos el linearLayoutManager
        recyclerView.setLayoutManager(linearLayoutManager);

        //definimos el adapter y recibe la lista
//        adapter = new ParqueoAdapter.ViewHolder(this, parqueList);

        //ahora recibe el reclyadapter de firebase
        /**
         * Pasamos lo que necesita
         * 1 la clase
         * 2 el xml, la estructura
         * 3 el viewHolder, la clase base
         * 4 cual es la referencia a buscar en firebase, trae todos los nodos hijos
         */
        adapter = new FirebaseRecyclerAdapter<Parqueos, ParqueoAdapter.ViewHolder>(
                Parqueos.class,
                R.layout.parqueos,
                ParqueoAdapter.ViewHolder.class,
                databaseReference.child("Parqueos")
        ) {
            @Override
            protected void populateViewHolder(ParqueoAdapter.ViewHolder viewHolder, Parqueos model, int position) {
                Log.e("Error", String.valueOf(model));
                viewHolder.nombre.setText(model.getNombre());
                viewHolder.direccion.setText(model.getDireccion());
                viewHolder.horario.setText(model.getHorario());
            }
        };

        recyclerView.setAdapter(adapter);

    }


    /**
     * definir la estructura de los datos en la lista de tal forma
     */
    public void createData(){
        parqueList = new ArrayList<>();
        parqueList.add(new Parqueos("Italia","Av. Uruguay al frente del Supermecado Fidalga","8:00 20:00"));
        parqueList.add(new Parqueos("Autopezas Contreras","Calle Warnes","7:00 18:00"));
        parqueList.add(new Parqueos("S/N","Esquina Bolivar","7:00 18:00"));
        parqueList.add(new Parqueos("Suarez","Calle seoane #28","6:00 22:00"));
        parqueList.add(new Parqueos("S/N","Al frente de la plaza estudiante","9:00 18:00"));
        parqueList.add(new Parqueos("Burger King","2do Anillo Av. Cristobal de Mendoza","7:00 20:00"));

    }
}
