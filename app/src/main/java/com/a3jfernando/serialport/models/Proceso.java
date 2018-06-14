package com.a3jfernando.serialport.models;

/**
 * Created by EnuarMunoz on 15/04/18.
 */

public class Proceso {

    //Atributes
    private long Id_Proceso;
    private String Name_Proceso;
    private String Tiempo_Transcurrido;
    private long Tiempo_Transcurrido_Long;
    private String Fecha_Proceso;
    private String Hora_Inicio_Proceso;
    private String Hora_Fin_Proceso;
    private boolean Ended_Process;



    public Proceso() {
        Id_Proceso = Id_Proceso;
        Name_Proceso = Name_Proceso;
        Tiempo_Transcurrido = Tiempo_Transcurrido;
        Tiempo_Transcurrido_Long = Tiempo_Transcurrido_Long;
        Fecha_Proceso = Fecha_Proceso;
        Hora_Inicio_Proceso = Hora_Inicio_Proceso;
        Hora_Fin_Proceso = Hora_Fin_Proceso;
        Ended_Process = Ended_Process;
    }

    public long getId_Proceso() {
        return Id_Proceso;
    }

    public void setId_Proceso(long id_Proceso) {
        Id_Proceso = id_Proceso;
    }

    public String getName_Proceso() {
        return Name_Proceso;
    }

    public void setName_Proceso(String name_Proceso) {
        Name_Proceso = name_Proceso;
    }

    public String getTiempo_Transcurrido() {
        return Tiempo_Transcurrido;
    }

    public void setTiempo_Transcurrido(String tiempo_Transcurrido) {
        Tiempo_Transcurrido = tiempo_Transcurrido;
    }

    public long getTiempo_Transcurrido_Long() {
        return Tiempo_Transcurrido_Long;
    }

    public void setTiempo_Transcurrido_Long(long tiempo_Transcurrido_Long) {
        Tiempo_Transcurrido_Long = tiempo_Transcurrido_Long;
    }

    public String getFecha_Proceso() {
        return Fecha_Proceso;
    }

    public void setFecha_Proceso(String fecha_Proceso) {
        Fecha_Proceso = fecha_Proceso;
    }

    public String getHora_Inicio_Proceso() {
        return Hora_Inicio_Proceso;
    }

    public void setHora_Inicio_Proceso(String hora_Inicio_Proceso) {
        Hora_Inicio_Proceso = hora_Inicio_Proceso;
    }

    public String getHora_Fin_Proceso() {
        return Hora_Fin_Proceso;
    }

    public void setHora_Fin_Proceso(String hora_Fin_Proceso) {
        Hora_Fin_Proceso = hora_Fin_Proceso;
    }

    public boolean isEnded_Process() {
        return Ended_Process;
    }

    public void setEnded_Process(boolean ended_Process) {
        Ended_Process = ended_Process;
    }

}
