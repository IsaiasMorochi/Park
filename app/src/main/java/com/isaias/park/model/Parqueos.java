package com.isaias.park.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Parqueos {

    public String NOMBRE;
    public String DIRECCION;
    public String TELEFONO;
    public String HORARIO;
    public String TIPO;
    public String CAPACIDAD;
    public String PrecioxHor;
    public String PrecioxDia;
    public String PrecioxMes;
    public String Estado;
    public String xlongitud;
    public String ylatitud;



    public Parqueos(){
        // Default constructor required for calls to DataSnapshot.getValue(Parqueos.class)
    }


    public Parqueos(String NOMBRE,String DIRECCION,String TELEFONO,String HORARIO,String TIPO, String CAPACIDAD,
                    String PrecioxHor, String PrecioxDia, String PrecioxMes, String Estado, String xlongitud, String ylatitud){
        this.NOMBRE = NOMBRE;
        this.DIRECCION = DIRECCION;
        this.TELEFONO = TELEFONO;
        this.HORARIO = HORARIO;
        this.TIPO = TIPO;
        this.CAPACIDAD = CAPACIDAD;
        this.PrecioxHor = PrecioxHor;
        this.PrecioxDia = PrecioxDia;
        this.PrecioxMes = PrecioxMes;
        this.Estado = Estado;
        this.xlongitud = xlongitud;
        this.ylatitud = ylatitud;
    }


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("NOMBRE", NOMBRE);
        result.put("DIRECCION", DIRECCION);
        result.put("TELEFONO", TELEFONO);
        result.put("HORARIO", HORARIO);
        result.put("TIPO", TIPO);
        result.put("CAPACIDAD", CAPACIDAD);
        result.put("PrecioxHor", PrecioxHor);
        result.put("PrecioxDia", PrecioxDia);
        result.put("PrecioxMes", PrecioxMes);
        result.put("Estado", Estado);
        result.put("xlongitud", xlongitud);
        result.put("ylatitud", ylatitud);

        return result;
    }


}
