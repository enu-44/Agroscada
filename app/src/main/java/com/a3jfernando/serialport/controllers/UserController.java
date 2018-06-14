package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.models.Usuario;

/**
 * Created by admi on 14/03/17.
 */

public class UserController {
    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;
    private static UserController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public UserController() {
        _instance = this;
    }
    public static UserController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new UserController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/


    public UserController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }
    ////Insertar Datos Usuarios
    public void insertarUsuario(String Nombre_Usuario,String Apellido_Usuario,String Estado_Sesion,String Clave,String Email) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Usuario, Nombre_Usuario);
        cv.put(DataSource.Apellido_Usuario, Apellido_Usuario);
        cv.put(DataSource.Estado_Sesion, Estado_Sesion);
        cv.put(DataSource.Clave, Clave);
        cv.put(DataSource.Email, Email);
        database.insert(DataSource.TABLE_USUARIOS, null, cv);
    }




    /// update
    public  void updateUser(long idusuario, String Nombre_Usuario,String Apellido_Usuario,String Estado_Sesion,String Clave,String Email){
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Usuario, Nombre_Usuario);
        cv.put(DataSource.Apellido_Usuario, Apellido_Usuario);
        cv.put(DataSource.Estado_Sesion, Estado_Sesion);
        cv.put(DataSource.Clave, Clave);
        cv.put(DataSource.Email, Email);
        database.update(DataSource.TABLE_USUARIOS, cv,DataSource.ID_USUARIOS+"="+ idusuario, null);

    }




    ///Eliminar datos bd
    public void deleteUsers() {

        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        database.delete(DataSource.TABLE_USUARIOS, null, null);
        //String select = "delete from Sincronizacion";
        //database.execSQL(select);
    }
    ////cargar total leidos
    public Usuario getFirstUser() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        Usuario results = new Usuario();
        String select = "select * from "+DataSource.TABLE_USUARIOS;
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                // String conf = cursor.getString(0);
                results.setIdusuario(cursor.getLong(0));
                results.setNombre_Usuario(cursor.getString(1));
                results.setApellido_Usuario(cursor.getString(2));
                results.setEstado_Sesion(cursor.getString(3));
                results.setEmail(cursor.getString(4));
                results.setClave(cursor.getString(5));

            } while(cursor.moveToNext());
        }
        return results;
    }

}
