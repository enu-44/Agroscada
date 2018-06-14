package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Proceso;
import com.a3jfernando.serialport.models.VariableProceso;

import java.util.ArrayList;

/**
 * Created by EnuarMunoz on 17/04/18.
 */

public class VariableProcessController {


    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;

    private static VariableProcessController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public VariableProcessController() {
        _instance = this;
    }
    public static VariableProcessController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new VariableProcessController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/
    public VariableProcessController(Context c) {
        ourcontext = c;
    }

    public VariableProcessController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }



    ////Insertar Datos Tipo Variables
    public void insertarVariableProcesso(
                                String Nombre_Variable,
                                 String Nombre_Proceso,
                                 long Valor_Deseado,
                                 long Posicion_Variable_Proceso,
                                 String Fecha_Registro_Variable_Proceso,
                                 long Id_Proceso,
                                 long Id_Variable


    ) {



        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Variable, Nombre_Variable);
        cv.put(DataSource.Name_Proceso, Nombre_Proceso);
        cv.put(DataSource.Valor_Deseado, Valor_Deseado);
        cv.put(DataSource.Posicion_Variable_Proceso, Posicion_Variable_Proceso);
        cv.put(DataSource.Fecha_Registro_Variable_Proceso, Fecha_Registro_Variable_Proceso);
        cv.put(DataSource.Id_Proceso, Id_Proceso);
        cv.put(DataSource.Id_Tipo_Variable, Id_Variable);
        database.insert(DataSource.TABLE_VARIABLE_PROCESS, null, cv);
    }

    public void actulizarVariableProcesso(
            long Variable_Proceso_Id,
            String Nombre_Variable,
            String Nombre_Proceso,
            long Valor_Deseado,
            long Posicion_Variable_Proceso,
            String Fecha_Registro_Variable_Proceso,
            long Id_Proceso,
            long Id_Variable


    ) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Nombre_Variable, Nombre_Variable);
        cv.put(DataSource.Name_Proceso, Nombre_Proceso);
        cv.put(DataSource.Valor_Deseado, Valor_Deseado);
        cv.put(DataSource.Posicion_Variable_Proceso, Posicion_Variable_Proceso);
        cv.put(DataSource.Fecha_Registro_Variable_Proceso, Fecha_Registro_Variable_Proceso);
        cv.put(DataSource.Id_Proceso, Id_Proceso);
        cv.put(DataSource.Id_Tipo_Variable, Id_Variable);
        database.update(DataSource.TABLE_VARIABLE_PROCESS, cv,DataSource.ID_VARIABLE_PROCESS+"="+ Variable_Proceso_Id, null);
    }



    ////Verificar datos sincronizados
    public ArrayList<VariableProceso> listVariablesProceso() {
        int estado= 0;
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        String select = "select * from "+DataSource.TABLE_VARIABLE_PROCESS;
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        ArrayList<VariableProceso> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            VariableProceso variable = cursorToVariable
                    (cursor);
            list.add(variable);
        }
        return list;
    }





    /////Asifnar datos de la base de datos al metodos Set
    private VariableProceso cursorToVariable(Cursor cursor) {
        VariableProceso variable = new VariableProceso();
        variable.setIdProceso_Variable(cursor.getLong(0));
        variable.setNombre_Variable(cursor.getString(1));
        variable.setName_Proceso(cursor.getString(2));
        variable.setValor_Deseado(cursor.getLong(3));
        variable.setPosicion_Variable_Proceso(cursor.getLong(4));
        variable.setFecha_Registro_Variable_Proceso(cursor.getString(5));
        variable.setId_Proceso(cursor.getLong(6));
        variable.setIdTipo_Variable(cursor.getLong(7));

        return variable;
    }



    ///Eliminar datos bd
    public void deleteVariableProcess() {

        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        database.delete(DataSource.TABLE_VARIABLE_PROCESS, null, null);
        //String select = "delete from Sincronizacion";
        //database.execSQL(select);
    }
}
