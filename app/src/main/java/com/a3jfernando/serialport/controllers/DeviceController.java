package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Device;

import java.util.ArrayList;

/**
 * Created by admi on 15/04/17.
 */

public class DeviceController {

    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;


    private static DeviceController _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public DeviceController() {
        _instance = this;
    }
    public static DeviceController getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new DeviceController();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/


    public DeviceController abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }

    ////Insertar Datos Equipo Maestro (Equipment master)
    public void insertarEquipmen(String Name_Device,String Description_Device,boolean Is_Active,long Id_Device_Master,String Image_Type_Device) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Name_Device, Name_Device);
        cv.put(DataSource.Description_Device, Description_Device);
        cv.put(DataSource.Is_Active, Is_Active);
        cv.put(DataSource.Id_Device_Master, Id_Device_Master);
        cv.put(DataSource.Image_Type_Device,Image_Type_Device);
        database.insert(DataSource.TABLE_DEVICE, null, cv);
    }


    /// Actualizar
    public  void updateInfoDevice(long Id_Device,String Name_Device,String Description_Device,boolean Is_Active,long Id_Device_Master, String Image_Type_Device){
        ContentValues cv = new ContentValues();

        cv.put(DataSource.Name_Device, Name_Device);
        cv.put(DataSource.Description_Device, Description_Device);
        cv.put(DataSource.Is_Active, Is_Active);
        cv.put(DataSource.Id_Device_Master, Id_Device_Master);
        cv.put(DataSource.Image_Type_Device,Image_Type_Device);
        database.update(DataSource.TABLE_DEVICE, cv,DataSource.ID_DEVICE+"="+ Id_Device, null);

    }

    /// Actualizar
    public  void eliminarDevice(long Id_Device){
        eliminarConfiguracionControlDevice(Id_Device);
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        database.delete(DataSource.TABLE_DEVICE,DataSource.ID_DEVICE+"="+ Id_Device, null);
    }


    public  void eliminarConfiguracionControlDevice(long Id_Device){
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        database.delete(DataSource.TABLE_CONFIGURACION_CONTROL_DEVICE,DataSource.Id_Device+"="+ Id_Device, null);
    }

    public ArrayList<Device> findAllEquipment() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        //String table = DataSource.TABLE_SINCRONIZACION;
        /// String[] columns = {DataSource.ID_SINCRONIZACION, DataSource.Usuario, DataSource.Sector_Predio, DataSource.Documento, DataSource.Direccion_predio, DataSource.Id_Predio_Usuario, DataSource.Codigo_Contador, DataSource.Ruta_Contador, DataSource.Id_Periodo, DataSource.Mes_Ano, DataSource.Id_Consumo_Contador, DataSource.Fecha_Lectura, DataSource.Consumo_Actual, DataSource.Consumo_Anterior, DataSource.Lectura_Actual, DataSource.Lectura_Anterior, DataSource.Estado_Lectura, DataSource.Estado_Consumo_Contador};
        String select = "select * from "+DataSource.TABLE_DEVICE;
        Cursor cursor = database.rawQuery(select, null);
        //Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Device> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Device devices = cursorToNote(cursor);
            list.add(devices);
        }
        return list;
    }

    /////Asifnar datos de la base de datos al metodos Set
    private Device cursorToNote(Cursor cursor) {
        Device device = new Device();
        device.setId_Device(cursor.getLong(0));
        device.setName_Device(cursor.getString(1));
        device.setDescription_Device(cursor.getString(2));
        device.setIs_Active(cursor.getInt(3)> 0);//Obtener el valor de un boolean
        device.setId_Device_Master(cursor.getLong(4));
        device.setImage_Type_Device(cursor.getString(5));
        return device;
    }




}
