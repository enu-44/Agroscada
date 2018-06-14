package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.models.Tipo_Variable;

import java.util.ArrayList;

/**
 * Created by admi on 22/03/17.
 */

public class TipoVariableController {
    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;

    private static TipoVariableController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public TipoVariableController() {
        _instance = this;
    }
    public static TipoVariableController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new TipoVariableController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/



    public TipoVariableController(Context c) {
        ourcontext = c;
    }

    public TipoVariableController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }


    ////Insertar Datos Tipo Variables
    public void insertarTipoVariables(String Nombre_Tipo_Variable, int Posicion_Variable, String Descripcion_Variable, boolean Estado_Variable, String Unidad_Medida) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Tipo_Variable, Nombre_Tipo_Variable);
        cv.put(DataSource.Posicion_Variable, Posicion_Variable);
        cv.put(DataSource.Descripcion_Variable, Descripcion_Variable);
        cv.put(DataSource.Estado_Variable, Estado_Variable);
        cv.put(DataSource.Unidad_Medida, Unidad_Medida);
        database.insert(DataSource.TABLE_TIPOS_VARIABLE, null, cv);
    }

    /// Actualizar
    public  void actualizarTipoVariable(long Id_Tipo_variable,String Nombre_Tipo_Variable,String Descripcion_Variable,boolean Estado_Variable,String Unidad_Medida){
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Tipo_Variable, Nombre_Tipo_Variable);
        cv.put(DataSource.Descripcion_Variable, Descripcion_Variable);
        cv.put(DataSource.Estado_Variable, Estado_Variable);
        cv.put(DataSource.Unidad_Medida, Unidad_Medida);

        database.update(DataSource.TABLE_TIPOS_VARIABLE, cv,DataSource.ID_TIPO_VARIABLE+"="+ Id_Tipo_variable, null);

    }


    ////cargar first tipo variable
    public Tipo_Variable getFirstTipoVariable() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        Tipo_Variable results = new Tipo_Variable();
        String select = "select * from "+DataSource.TABLE_TIPOS_VARIABLE;
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                // String conf = cursor.getString(0);
                results.setId_Tipo_Variable(cursor.getLong(0));
                results.setNombre_Tipo_Variable(cursor.getString(1));
                results.setPosicion_Variable(cursor.getInt(2));
                results.setDescripcion_Variable(cursor.getString(3));
                results.setEstado_Variable(cursor.getInt(4) > 0);
                results.setUnidad_Medida(cursor.getString(5));

            } while(cursor.moveToNext());
        }
        return results;
    }



    public ArrayList<Tipo_Variable> findAllTipoVariable() {

        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        //String table = DataSource.TABLE_SINCRONIZACION;
        /// String[] columns = {DataSource.ID_SINCRONIZACION, DataSource.Usuario, DataSource.Sector_Predio, DataSource.Documento, DataSource.Direccion_predio, DataSource.Id_Predio_Usuario, DataSource.Codigo_Contador, DataSource.Ruta_Contador, DataSource.Id_Periodo, DataSource.Mes_Ano, DataSource.Id_Consumo_Contador, DataSource.Fecha_Lectura, DataSource.Consumo_Actual, DataSource.Consumo_Anterior, DataSource.Lectura_Actual, DataSource.Lectura_Anterior, DataSource.Estado_Lectura, DataSource.Estado_Consumo_Contador};
        String select = "select * from "+DataSource.TABLE_TIPOS_VARIABLE;
        Cursor cursor = database.rawQuery(select, null);
        //Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Tipo_Variable> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Tipo_Variable notificacion = cursorToNote(cursor);
            list.add(notificacion);
        }
        return list;
    }




    public ArrayList<Tipo_Variable> findVariableActives() {
        ///Loas boolean
        int estado=1;
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        //String table = DataSource.TABLE_SINCRONIZACION;
        /// String[] columns = {DataSource.ID_SINCRONIZACION, DataSource.Usuario, DataSource.Sector_Predio, DataSource.Documento, DataSource.Direccion_predio, DataSource.Id_Predio_Usuario, DataSource.Codigo_Contador, DataSource.Ruta_Contador, DataSource.Id_Periodo, DataSource.Mes_Ano, DataSource.Id_Consumo_Contador, DataSource.Fecha_Lectura, DataSource.Consumo_Actual, DataSource.Consumo_Anterior, DataSource.Lectura_Actual, DataSource.Lectura_Anterior, DataSource.Estado_Lectura, DataSource.Estado_Consumo_Contador};
        String select = "select * from "+DataSource.TABLE_TIPOS_VARIABLE+" where "+ DataSource.Estado_Variable+"='"+estado+"'";;
        Cursor cursor = database.rawQuery(select, null);
        //Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Tipo_Variable> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Tipo_Variable notificacion = cursorToNote(cursor);
            list.add(notificacion);
        }
        return list;
    }

    /////Asifnar datos de la base de datos al metodos Set
    private Tipo_Variable cursorToNote(Cursor cursor) {
        Tipo_Variable notificacion = new Tipo_Variable();
        notificacion.setId_Tipo_Variable(cursor.getLong(0));
        notificacion.setNombre_Tipo_Variable(cursor.getString(1));
        notificacion.setPosicion_Variable(cursor.getInt(2));
        notificacion.setDescripcion_Variable(cursor.getString(3));
        notificacion.setEstado_Variable(cursor.getInt(4) > 0);
        notificacion.setUnidad_Medida(cursor.getString(5));

        return notificacion;
    }

}
