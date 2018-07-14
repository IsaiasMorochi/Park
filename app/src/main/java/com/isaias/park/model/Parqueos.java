package com.isaias.park.model;

import android.graphics.Paint;

import com.google.android.gms.maps.model.LatLng;

public class Parqueos {

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

    }


    public Parqueos(String NOMBRE,String DIRECCION,String TELEFONO,String HORARIO,String Tipo, String Capacidad,
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



    public String NOMBRE;

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getHORARIO() {
        return HORARIO;
    }

    public void setHORARIO(String HORARIO) {
        this.HORARIO = HORARIO;
    }

    public String getTIPO() {
        return TIPO;
    }

    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    public String getCAPACIDAD() {
        return CAPACIDAD;
    }

    public void setCAPACIDAD(String CAPACIDAD) {
        this.CAPACIDAD = CAPACIDAD;
    }

    public String getPrecioxHor() {
        return PrecioxHor;
    }

    public void setPrecioxHor(String precioxHor) {
        PrecioxHor = precioxHor;
    }

    public String getPrecioxDia() {
        return PrecioxDia;
    }

    public void setPrecioxDia(String precioxDia) {
        PrecioxDia = precioxDia;
    }

    public String getPrecioxMes() {
        return PrecioxMes;
    }

    public void setPrecioxMes(String precioxMes) {
        PrecioxMes = precioxMes;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getXlongitud() {
        return xlongitud;
    }

    public void setXlongitud(String xlongitud) {
        this.xlongitud = xlongitud;
    }

    public String getYlatitud() {
        return ylatitud;
    }

    public void setYlatitud(String ylatitud) {
        this.ylatitud = ylatitud;
    }


}
