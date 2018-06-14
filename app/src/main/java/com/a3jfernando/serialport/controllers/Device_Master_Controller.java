package com.a3jfernando.serialport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a3jfernando.serialport.conf.DataSource;
import com.a3jfernando.serialport.models.Device_Master;

import java.util.ArrayList;

/**
 * Created by admi on 15/04/17.
 */

public class Device_Master_Controller {

    private DataSource dbhelper;
    private static Context ourcontext;
    private SQLiteDatabase database;

    private static Device_Master_Controller _instance;

    ///Instance
    /*----------------------------------------------------------------------------------------------------------------*/
    public Device_Master_Controller() {
        _instance = this;
    }
    public static Device_Master_Controller getInstance(Context c) {
        if (_instance == null) {
            ourcontext = c;
            _instance = new Device_Master_Controller();
        }
        return _instance;
    }
    /*------------------------------------------------------------------------------------------------------------------*/

    public Device_Master_Controller abrirBaseDeDatos() throws SQLException {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }




    ////Insertar Datos Equipo Maestro (Equipment master)
    public void insertarDeviceMaster(String Remote_Ip_Address,String Android_Id,String Device_Id,String Software_Version,String Local_Ip_Address,String Android_Version, String MacAddr,String Device_Name) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Android_Id, Android_Id);
        cv.put(DataSource.Device_Id, Device_Id);
        cv.put(DataSource.Software_Version, Software_Version);
        cv.put(DataSource.Local_Ip_Address, Local_Ip_Address);
        cv.put(DataSource.Android_Version, Android_Version);
        cv.put(DataSource.MacAddr, MacAddr);
        cv.put(DataSource.Device_Name, Device_Name);
        cv.put(DataSource.Remote_Ip_Address, Remote_Ip_Address);
        database.insert(DataSource.TABLE_DEVICE_MASTER, null, cv);
    }

    public void actualizarDeviceMaster(long Id_Device_Master, String Remote_Ip_Address, String Android_Id, String Device_Id, String Software_Version, String Local_Ip_Address, String Android_Version, String MacAddr, String Device_Name) {
        ContentValues cv = new ContentValues();
        cv.put(DataSource.Android_Id, Android_Id);
        cv.put(DataSource.Device_Id, Device_Id);
        cv.put(DataSource.Software_Version, Software_Version);
        cv.put(DataSource.Local_Ip_Address, Local_Ip_Address);
        cv.put(DataSource.Android_Version, Android_Version);
        cv.put(DataSource.MacAddr, MacAddr);
        cv.put(DataSource.Device_Name, Device_Name);
        cv.put(DataSource.Remote_Ip_Address, Remote_Ip_Address);
        database.update(DataSource.TABLE_DEVICE_MASTER, cv,DataSource.ID_DEVICE_MASTER+"="+ Id_Device_Master, null);
    }


    //Verificate Registers In Sqlite
    public ArrayList VerificateDeatailDeviceMaster () {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        ///ArrayList<Sincronizacion> list = new ArrayList();
        ArrayList<Device_Master> detailMasterEquipment = new ArrayList<Device_Master>();
        /// String select = "select * from "+DataSource.TABLE_EQUIPMENT_MASTER+" ORDER BY "+DataSource.ID_EQUIPMENT_MASTER+" ASC limit 1";
        String select = "select * from "+DataSource.TABLE_DEVICE_MASTER;

        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                Device_Master getsetEquipmentMaster= new Device_Master();
                getsetEquipmentMaster.setId_Device_Master(cursor.getLong(0));
                // getsetsincronizaciones.add(getsetsincronizacion);
                ///  list.add("USUARIO: "+cursor.getString(1)+"\n"+"SECTOR: "+cursor.getString(2)+"\n"+"DIRECCION: "+cursor.getString(4)+"\n"+"CODGO CONTADOR: "+cursor.getString(6)+"\n"+"RUTA CONTADOR: "+cursor.getString(7)+"\n"+"PERIODO: "+cursor.getString(9)+"\n"+"CONSUMO ANTERIOR: "+cursor.getString(13)+"\n"+"LECTURA ANTERIOR: "+cursor.getString(15)+"\n");
                detailMasterEquipment.add(getsetEquipmentMaster);
            }while (cursor.moveToNext());
        }
        return detailMasterEquipment;
    }


    ////cargar total leidos
    public Device_Master getFirstDeviceMaster() {
        dbhelper = new DataSource(ourcontext);
        database = dbhelper.getWritableDatabase();
        Device_Master results = new Device_Master();
        String select = "select * from "+DataSource.TABLE_DEVICE_MASTER;
        Cursor cursor = database.rawQuery(select, null);
        //If Cursor is valid
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                // String conf = cursor.getString(0);
                results.setId_Device_Master(cursor.getLong(0));
                results.setAndroid_Id(cursor.getString(1));
                results.setDevice_Id(cursor.getString(2));
                results.setSoftware_Version(cursor.getString(3));
                results.setLocal_Ip_Address(cursor.getString(4));
                results.setRemote_Ip_Address(cursor.getString(5));
                results.setAndroid_Version(cursor.getString(6));
                results.setMacAddr(cursor.getString(7));
                results.setDevice_Name(cursor.getString(8));
            } while(cursor.moveToNext());
        }
        return results;
    }


}
