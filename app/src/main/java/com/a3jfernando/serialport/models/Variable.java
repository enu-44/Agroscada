package com.a3jfernando.serialport.models;

import java.util.Date;

/**
 * Created by admi on 22/03/17.
 */

public class Variable {

    //Atributes
    private long Id_Variable;
    private String Data_Variable;
    private String Fecha_Variable;
    private String Hora_Variable;



    private String Fecha_Hora_Variable;

    private long Id_Tipo_Variable;
    private boolean Estado_Sincronizacion_Variable;
    private String Id_Equipo_Remoto;


    //Tipo Variable
    String Nombre_Tipo_Variable ;
    String Descripcion_Tipo_Variable ;

    //Constructor
    public Variable() {
        this.Id_Variable = Id_Variable;
        this.Data_Variable = Data_Variable;
        this.Fecha_Variable = Fecha_Variable;
        this.Hora_Variable = Hora_Variable;
        this.Fecha_Hora_Variable= Fecha_Hora_Variable;
        this.Id_Tipo_Variable = Id_Tipo_Variable;
        this.Estado_Sincronizacion_Variable=Estado_Sincronizacion_Variable;
        this.Id_Equipo_Remoto=  Id_Equipo_Remoto;
        //Tipo Variable
        this.Descripcion_Tipo_Variable=Descripcion_Tipo_Variable;
        this.Nombre_Tipo_Variable=Nombre_Tipo_Variable;
    }

    //Methods Variables
    public long getId_Variable() {
        return Id_Variable;
    }

    public void setId_Variable(long id_Variable) {
        Id_Variable = id_Variable;
    }

    public String getData_Variable() {
        return Data_Variable;
    }

    public void setData_Variable(String data_Variable) {
        Data_Variable = data_Variable;
    }

    public String getFecha_Variable() {
        return Fecha_Variable;
    }

    public void setFecha_Variable(String fecha_Variable) {
        Fecha_Variable = fecha_Variable;
    }

    public String getHora_Variable() {
        return Hora_Variable;
    }

    public void setHora_Variable(String hora_Variable) {
        Hora_Variable = hora_Variable;
    }
    public String getFecha_Hora_Variable() {
        return Fecha_Hora_Variable;
    }

    public void setFecha_Hora_Variable(String fecha_Hora_Variable) {
        Fecha_Hora_Variable = fecha_Hora_Variable;
    }
    public long getId_Tipo_Variable() {
        return Id_Tipo_Variable;
    }

    public void setId_Tipo_Variable(long id_Tipo_Variable) {
        Id_Tipo_Variable = id_Tipo_Variable;
    }

    public boolean getEstado_Sincronizacion_Variable() {
        return Estado_Sincronizacion_Variable;
    }

    public void setEstado_Sincronizacion_Variable(boolean estado_Sincronizacion_Variable) {
        Estado_Sincronizacion_Variable = estado_Sincronizacion_Variable;
    }
    public String getId_Equipo_Remoto() {
        return Id_Equipo_Remoto;
    }

    public void setId_Equipo_Remoto(String id_Equipo_Remoto) {
        Id_Equipo_Remoto = id_Equipo_Remoto;
    }


    //Methods tipo Variable
    public String getNombre_Tipo_Variable() {
        return Nombre_Tipo_Variable;
    }

    public void setNombre_Tipo_Variable(String nombre_Tipo_Variable) {
        Nombre_Tipo_Variable = nombre_Tipo_Variable;
    }

    public String getDescripcion_Tipo_Variable() {
        return Descripcion_Tipo_Variable;
    }

    public void setDescripcion_Tipo_Variable(String descripcion_Variable) {
        Descripcion_Tipo_Variable = descripcion_Variable;
    }


}
