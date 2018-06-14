package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Proceso;

/**
 * Created by EnuarMunoz on 15/04/18.
 */

public class ProcesoController {
    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;

    private static ProcesoController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public ProcesoController() {
        _instance = this;
    }
    public static ProcesoController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new ProcesoController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/
    public ProcesoController(Context c) {
        ourcontext = c;
    }

    public ProcesoController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }



    ////Insertar Datos Tipo Variables
    public void insertarProcesso( String Nombre_Proceso,
                                  String Tiempo_Transcrurrido,
                                  long Tiempo_Transcrurrido_Long,
                                  String Fecha_Proceso,
                                  String Hora_Inicio,
                                  String Hora_Fin,
                                  boolean Proceso_Termiando

                                 ) {

        ContentValues cv = new ContentValues();
        cv.put(DataSource.Name_Proceso, Nombre_Proceso);
        cv.put(DataSource.Tiempo_Transcurrido, Tiempo_Transcrurrido);
        cv.put(DataSource.Tiempo_Transcurrido_Long, Tiempo_Transcrurrido_Long);
        cv.put(DataSource.Fecha_Proceso, Fecha_Proceso);
        cv.put(DataSource.Hora_Inicio_Proceso, Hora_Inicio);
        cv.put(DataSource.Hora_Fin_Proceso, Hora_Fin);
        cv.put(DataSource.Ended_Process, Proceso_Termiando);

        database.insert(DataSource.TABLE_PROCESS, null, cv);
    }

    public void actualizarProcesso(
                                long Process_Id,
                                 String Nombre_Proceso,
                                 String Tiempo_Transcrurrido,
                                 long Tiempo_Transcrurrido_Long,
                                 String Fecha_Proceso,
                                 String Hora_Inicio,
                                 String Hora_Fin,
                                 boolean Proceso_Termiando

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Name_Proceso, Nombre_Proceso);
        cv.put(DataSource.Tiempo_Transcurrido, Tiempo_Transcrurrido);
        cv.put(DataSource.Tiempo_Transcurrido_Long, Tiempo_Transcrurrido_Long);
        cv.put(DataSource.Fecha_Proceso, Fecha_Proceso);
        cv.put(DataSource.Hora_Inicio_Proceso, Hora_Inicio);
        cv.put(DataSource.Hora_Fin_Proceso, Hora_Fin);
        cv.put(DataSource.Ended_Process, Proceso_Termiando);


        database.update(DataSource.TABLE_PROCESS, cv,DataSource.ID_PROCESS+"="+ Process_Id, null);

    }


    public void actualizarTimeProcesso(
            long Process_Id,
            String Tiempo_Transcrurrido,
            long Tiempo_Transcrurrido_Long

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Tiempo_Transcurrido, Tiempo_Transcrurrido);
        cv.put(DataSource.Tiempo_Transcurrido_Long, Tiempo_Transcrurrido_Long);
        database.update(DataSource.TABLE_PROCESS, cv,DataSource.ID_PROCESS+"="+ Process_Id, null);
    }


    public void actualizarStateProcesso(
            long Process_Id,
            boolean Ended_Process


    ) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Ended_Process, Ended_Process);

        database.update(DataSource.TABLE_PROCESS, cv,DataSource.ID_PROCESS+"="+ Process_Id, null);
    }



    ////cargar total leidos
    public Proceso getFirstProceso() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        Proceso results = new Proceso();
        String select = "select * from "+DataSource.TABLE_PROCESS;
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                // String conf = cursor.getString(0);
                results.setId_Proceso(cursor.getLong(0));
                results.setName_Proceso(cursor.getString(1));
                results.setTiempo_Transcurrido(cursor.getString(2));
                results.setTiempo_Transcurrido_Long(cursor.getLong(3));
                results.setFecha_Proceso(cursor.getString(4));
                results.setHora_Inicio_Proceso(cursor.getString(5));
                results.setHora_Fin_Proceso(cursor.getString(6));
                results.setEnded_Process(cursor.getInt(7) > 0);



            } while(cursor.moveToNext());
        }
        return results;
    }



}
