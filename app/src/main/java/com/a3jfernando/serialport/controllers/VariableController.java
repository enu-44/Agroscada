package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Variable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admi on 23/03/17.
 */

public class VariableController {


    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;


    private static VariableController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public VariableController() {
        _instance = this;
    }
    public static VariableController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new VariableController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/

    public VariableController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }


    ///Create Variables
    public void createVariables(String fecha_hora_variable,String data_variable, String fecha_variable, String hora_variable, boolean estado_sincronizacion_variable, String id_equipo_remoto, long id_tipo_variable) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Data_Variable, data_variable);
        cv.put(DataSource.Fecha_Variable, fecha_variable);
        cv.put(DataSource.Hora_Variable, hora_variable);
        cv.put(DataSource.Fecha_Hora_Variable,fecha_hora_variable);
        cv.put(DataSource.Estado_Sincronizacion_Variable, estado_sincronizacion_variable);
        cv.put(DataSource.Id_Equipo_Remoto, id_equipo_remoto);
        cv.put(DataSource.Id_Tipo_Variable, id_tipo_variable);
        database.insert(DataSource.TABLE_VARIABLE, null, cv);
    }


    ///Update state sincronizartion variable
    /// Actualizar
    public  void actualizarStateSincronizarionVariables(long ID_VARIABLE,boolean Estado_Sincronizacion_Variable){
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Estado_Sincronizacion_Variable, Estado_Sincronizacion_Variable);
        database.update(DataSource.TABLE_VARIABLE, cv,DataSource.ID_VARIABLE+"="+ ID_VARIABLE, null);
    }

    ////Verificar datos sincronizados
    public ArrayList<Variable> verificateSincronizacionVariables() {
        int estado= 0;
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        String select = "select * from "+DataSource.TABLE_VARIABLE+" where "+ DataSource.Estado_Sincronizacion_Variable+"='"+estado+"'";
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        ArrayList<Variable> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Variable variable = cursorToVariable
                    (cursor);
            list.add(variable);
        }
        return list;
    }

    ///Eliminar datos bd
    public void deleteVariables() {

        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        database.delete(DataSource.TABLE_VARIABLE, null, null);
        //String select = "delete from Sincronizacion";
        //database.execSQL(select);
    }

    //Get variables by fecha and tipe of variables.
    public ArrayList<Variable> findVariablesByDateAndTipe(String Fecha_Variable_Inicial,String Fecha_Variable_Final, long Id_Tipo_Variable) {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        //String table = DataSource.TABLE_SINCRONIZACION;
        /// String[] columns = {DataSource.ID_SINCRONIZACION, DataSource.Usuario, DataSource.Sector_Predio, DataSource.Documento, DataSource.Direccion_predio, DataSource.Id_Predio_Usuario, DataSource.Codigo_Contador, DataSource.Ruta_Contador, DataSource.Id_Periodo, DataSource.Mes_Ano, DataSource.Id_Consumo_Contador, DataSource.Fecha_Lectura, DataSource.Consumo_Actual, DataSource.Consumo_Anterior, DataSource.Lectura_Actual, DataSource.Lectura_Anterior, DataSource.Estado_Lectura, DataSource.Estado_Consumo_Contador};
      //  String queryExample = "select * from "+DataSource.TABLE_VARIABLE+" as v inner join "+DataSource.TABLE_TIPOS_VARIABLE+" as tp on v."+DataSource.Id_Tipo_Variable+" = tp."+DataSource.ID_TIPO_VARIABLE+" where v."+DataSource.Fecha_Variable+"='"+Fecha_Variable_Inicial+"' AND "+DataSource.Id_Tipo_Variable+"='"+Id_Tipo_Variable+"'";
        String select ="select * from " + DataSource.TABLE_VARIABLE +" as v inner join "+DataSource.TABLE_TIPOS_VARIABLE+" as tp on v."+DataSource.Id_Tipo_Variable+" = tp."+DataSource.ID_TIPO_VARIABLE+" where "+DataSource.Id_Tipo_Variable+"='"+Id_Tipo_Variable+"' AND v."+DataSource.Fecha_Variable+" BETWEEN '" + Fecha_Variable_Inicial + "' AND '" + Fecha_Variable_Final + "' ORDER BY "+"v."+DataSource.ID_VARIABLE+" DESC";
        //SELECT * FROM test WHERE date BETWEEN "11/1/2011" AND "11/8/2011";
       //// String select = "select * from "+DataSource.TABLE_VARIABLE+" as v inner join "+DataSource.TABLE_TIPOS_VARIABLE+" as tp on v."+DataSource.Id_Tipo_Variable+" = tp."+DataSource.ID_TIPO_VARIABLE+" where "+DataSource.Id_Tipo_Variable+"='"+Id_Tipo_Variable+"' AND v."+DataSource.Fecha_Variable+" BETWEEN '"+Fecha_Variable_Inicial+"' AND '"+Fecha_Variable_Final+"'";
        Cursor cursor = database.rawQuery(select, null);

        //Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Variable> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Variable variable = cursorToNote(cursor);
            list.add(variable);
        }
        return list;
    }

    /////Asifnar datos de la base de datos al metodos Set
    private Variable cursorToNote(Cursor cursor) {
        Variable variable = new Variable();
        variable.setId_Variable(cursor.getLong(0));
        variable.setData_Variable(cursor.getString(1));
        variable.setFecha_Variable(cursor.getString(2));
        variable.setHora_Variable(cursor.getString(3));
        variable.setFecha_Hora_Variable(cursor.getString(4));
        variable.setEstado_Sincronizacion_Variable(cursor.getInt(5) > 0);
        variable.setId_Equipo_Remoto(cursor.getString(6));
        variable.setId_Tipo_Variable(cursor.getLong(7));
        //Tipo variable
        variable.setNombre_Tipo_Variable(cursor.getString(9));
        variable.setDescripcion_Tipo_Variable(cursor.getString(11));
        return variable;
    }


    /////Asifnar datos de la base de datos al metodos Set
    private Variable cursorToVariable(Cursor cursor) {
        Variable variable = new Variable();
        variable.setId_Variable(cursor.getLong(0));
        variable.setData_Variable(cursor.getString(1));
        variable.setFecha_Variable(cursor.getString(2));
        variable.setHora_Variable(cursor.getString(3));
        variable.setFecha_Hora_Variable(cursor.getString(4));
        variable.setEstado_Sincronizacion_Variable(cursor.getInt(5) > 0);
        variable.setId_Equipo_Remoto(cursor.getString(6));
        variable.setId_Tipo_Variable(cursor.getLong(7));

        return variable;
    }


}
