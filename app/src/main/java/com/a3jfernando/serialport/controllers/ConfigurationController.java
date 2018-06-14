package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Configuration;

/**
 * Created by admi on 14/03/17.
 */

public class ConfigurationController {

    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;


    private static ConfigurationController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public ConfigurationController() {
        _instance = this;
    }
    public static ConfigurationController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new ConfigurationController();
        }
        return _instance;
    }



    public ConfigurationController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }
    ////Insertar Datos Configuracion de Notificaciones
    public void insertarConfiguracion(long Id_Device,String SerialWriteOn,String SerialWriteOff) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.SerialWriteOn, SerialWriteOn);
        cv.put(DataSource.SerialWriteOff, SerialWriteOff);
        cv.put(DataSource.Id_Device, Id_Device);
        database.insert(DataSource.TABLE_CONFIGURACION_CONTROL_DEVICE, null, cv);
    }

    /// Actualizar
    public  void actualizarConfiguracion(long Id_Device,long id_configuracion,String SerialWriteOn,String SerialWriteOff){
        ContentValues cv = new ContentValues();
        cv.put(DataSource.SerialWriteOn, SerialWriteOn);
        cv.put(DataSource.SerialWriteOff, SerialWriteOff);
        cv.put(DataSource.Id_Device, Id_Device);
        database.update(DataSource.TABLE_CONFIGURACION_CONTROL_DEVICE, cv,DataSource.ID_CONFIGURACION_CONTROL_DEVICE+"="+ id_configuracion, null);

    }



    ////cargar total leidos
    public Configuration getFirstConfiguracion(long Id_Device) {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        Configuration results = new Configuration();
        String select = "select * from "+DataSource.TABLE_CONFIGURACION_CONTROL_DEVICE+" where "+ DataSource.Id_Device+"='"+Id_Device+"'";
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
               // String conf = cursor.getString(0);
                results.setIdconfiguration(cursor.getLong(0));
                results.setSerialWriteOn(cursor.getString(1));
                results.setSerialWriteOff(cursor.getString(2));

            } while(cursor.moveToNext());
        }
        return results;
    }



}
