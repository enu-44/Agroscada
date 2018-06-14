package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Usuario;

import java.util.ArrayList;

/**
 * Created by admi on 14/03/17.
 */

public class LoginController {
    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;

    private static LoginController _instance;


    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public LoginController() {
        _instance = this;
    }
    public static LoginController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new LoginController();
        }
        return _instance;
    }




    public LoginController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }



    ////Login Usuario
    public Usuario loginUser(String Email, String Clave ) {

        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();

        String select = "select * from "+DataSource.TABLE_USUARIOS+" where "+DataSource.Email+" = '"+Email+"' AND "+DataSource.Clave+"='"+Clave+"'";
        Cursor cursor = database.rawQuery(select, null);
        ///Cursor cursor = database.query(table, columns, null, null, null, null, null);
        Usuario user = new Usuario();
        /*
        while(cursor.moveToNext()) {
            Usuario usuario = cursorToNote(cursor);
            list.add(usuario);
        }*/

        if (cursor.moveToFirst()){
            do {

                user.setIdusuario(cursor.getLong(0));
                user.setNombre_Usuario(cursor.getString(1));
                user.setApellido_Usuario(cursor.getString(2));
                user.setEstado_Sesion(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setClave(cursor.getString(5));

            }while (cursor.moveToNext());
        }


        return user;
    }




    /////Listar todos los datos de la entidad usuarios
    public ArrayList<Usuario> findAllUser() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        //String table = DataSource.TABLE_SINCRONIZACION;
        /// String[] columns = {DataSource.ID_SINCRONIZACION, DataSource.Usuario, DataSource.Sector_Predio, DataSource.Documento, DataSource.Direccion_predio, DataSource.Id_Predio_Usuario, DataSource.Codigo_Contador, DataSource.Ruta_Contador, DataSource.Id_Periodo, DataSource.Mes_Ano, DataSource.Id_Consumo_Contador, DataSource.Fecha_Lectura, DataSource.Consumo_Actual, DataSource.Consumo_Anterior, DataSource.Lectura_Actual, DataSource.Lectura_Anterior, DataSource.Estado_Lectura, DataSource.Estado_Consumo_Contador};
        String select = "select * from "+DataSource.TABLE_USUARIOS;
        Cursor cursor = database.rawQuery(select, null);
        //Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Usuario> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Usuario usuario = cursorToNote(cursor);
            list.add(usuario);
        }
        return list;
    }

    /////Asifnar datos de la base de datos al metodos Set
    private Usuario cursorToNote(Cursor cursor) {
        Usuario usuario = new Usuario();
        usuario.setIdusuario(cursor.getLong(0));
        usuario.setNombre_Usuario(cursor.getString(1));
        usuario.setApellido_Usuario(cursor.getString(2));
        usuario.setEstado_Sesion(cursor.getString(3));
        usuario.setEmail(cursor.getString(4));
        usuario.setClave(cursor.getString(5));
        return usuario;
    }






    /// Actualizar
    public  void actualizarEstadoSesion(Long idusuario, String Estado_Sesion){
        ContentValues cv = new ContentValues();

        cv.put(DataSource.Estado_Sesion, Estado_Sesion);
        database.update(DataSource.TABLE_USUARIOS, cv,DataSource.ID_USUARIOS+"="+ idusuario, null);

    }




}
