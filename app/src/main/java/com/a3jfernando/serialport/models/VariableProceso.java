package com.a3jfernando.serialport.models;

/**
 * Created by EnuarMunoz on 16/04/18.
 */

public class VariableProceso {

    private long IdProceso_Variable;
    private String Nombre_Variable;
    private String Name_Proceso;
    private long Valor_Deseado;
    private long Posicion_Variable_Proceso;
    private String Fecha_Registro_Variable_Proceso;
    private long Id_Proceso;
    private long Id_Tipo_Variable;


    public VariableProceso() {
        IdProceso_Variable=IdProceso_Variable;
        Nombre_Variable = Nombre_Variable;
        Name_Proceso = Name_Proceso;
        Valor_Deseado = Valor_Deseado;
        Posicion_Variable_Proceso = Posicion_Variable_Proceso;
        Fecha_Registro_Variable_Proceso = Fecha_Registro_Variable_Proceso;
        Id_Proceso = Id_Proceso;
        Id_Tipo_Variable = Id_Tipo_Variable;
    }

    public long getIdProceso_Variable() {
        return IdProceso_Variable;
    }

    public void setIdProceso_Variable(long idProceso_Variable) {
        IdProceso_Variable = idProceso_Variable;
    }

    public String getNombre_Variable() {
        return Nombre_Variable;
    }

    public void setNombre_Variable(String nombre_Variable) {
        Nombre_Variable = nombre_Variable;
    }

    public String getName_Proceso() {
        return Name_Proceso;
    }

    public void setName_Proceso(String name_Proceso) {
        Name_Proceso = name_Proceso;
    }

    public long getValor_Deseado() {
        return Valor_Deseado;
    }

    public void setValor_Deseado(long valor_Deseado) {
        Valor_Deseado = valor_Deseado;
    }

    public long getPosicion_Variable_Proceso() {
        return Posicion_Variable_Proceso;
    }

    public void setPosicion_Variable_Proceso(long posicion_Variable_Proceso) {
        Posicion_Variable_Proceso = posicion_Variable_Proceso;
    }

    public String getFecha_Registro_Variable_Proceso() {
        return Fecha_Registro_Variable_Proceso;
    }

    public void setFecha_Registro_Variable_Proceso(String fecha_Registro_Variable_Proceso) {
        Fecha_Registro_Variable_Proceso = fecha_Registro_Variable_Proceso;
    }

    public long getId_Proceso() {
        return Id_Proceso;
    }

    public void setId_Proceso(long id_Proceso) {
        Id_Proceso = id_Proceso;
    }

    public long getIdTipo_Variable() {
        return Id_Tipo_Variable;
    }

    public void setIdTipo_Variable(long id_Variable) {
        Id_Tipo_Variable = id_Variable;
    }

    @Override
    public String toString() {
        return Nombre_Variable+" ( "+Valor_Deseado+" )";

    }
}
