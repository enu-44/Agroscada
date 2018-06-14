package com.a3jfernando.serialport.conf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admi on 14/03/17.
 */

public class DataSource extends SQLiteOpenHelper {
    // información del a base de datos
    static final String DB_NAME = "iot.db";
    static final int DB_VERSION = 1;

    ////Tabla Usuarios (Users)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_USUARIOS = "Users";
    public static final String ID_USUARIOS = "id";
    ///Atributos Usuarios
    public static final String Nombre_Usuario = "Name_User";
    public static final String Apellido_Usuario = "Last_User";
    public static final String Email = "Email";
    public static final String Clave = "Clave";
    public static final String Estado_Sesion = "Status_Sesion";
    private static final String CREATE_TABLE_USUARIOS = "create table "
            + TABLE_USUARIOS + "(" + ID_USUARIOS
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Nombre_Usuario + " TEXT NULL,"
            + Apellido_Usuario + " TEXT NULL,"
            + Estado_Sesion + " TEXT NULL,"
            + Email + " TEXT NULL,"
            + Clave + " TEXT NULL);";



    ////Tabla  Equipo Master (Device_Master)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_DEVICE_MASTER = "Device_Master";
    public static final String ID_DEVICE_MASTER= "id";
    ///Atributos Equipo Master
    public static final String Android_Id = "Android_Id";
    public static final String Device_Id = "Device_Id";
    public static final String Software_Version = "Software_Version";
    public static final String Local_Ip_Address = "Local_Ip_Address";
    public static final String Remote_Ip_Address = "Remote_Ip_Address";
    public static final String Android_Version = "Android_Version";
    public static final String MacAddr = "MacAddr";
    public static final String Device_Name = "Device_Name";
    private static final String CREATE_TABLE_DEVICE_MASTER = "create table "
            + TABLE_DEVICE_MASTER + "(" + ID_DEVICE_MASTER
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Android_Id + " TEXT NULL,"
            + Device_Id + " TEXT NULL,"
            + Software_Version + " TEXT NULL,"
            + Local_Ip_Address + " TEXT NULL,"
            + Remote_Ip_Address + " TEXT NULL,"
            + Android_Version + " TEXT NULL,"
            + MacAddr + " TEXT NULL,"
            + Device_Name + " TEXT NULL);";

    /*-------------------------
    -------------------------------------------------------------------*/

    ////Tabla  Equipo  (Device)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_DEVICE = "Device";
    public static final String ID_DEVICE= "id";
    ///Atributos Equipo
    public static final String Name_Device = "Name_Device";
    public static final String Description_Device = "Description_Device";
    public static final String Image_Type_Device = "Image_Type_Device";
    public static final String Is_Active = "Is_Active";
    public static final String Id_Device_Master = "Id_Device_Master";
    private static final String CREATE_TABLE_DEVICE = "create table "
            + TABLE_DEVICE + "(" + ID_DEVICE
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Name_Device + " TEXT NULL,"
            + Description_Device + " TEXT NULL,"
            + Is_Active + " BOOLEAN NULL,"
            + Id_Device_Master + " INTEGER NULL,"
            + Image_Type_Device+ " TEXT NULL,"
            +"FOREIGN KEY("+Id_Device_Master+") REFERENCES "+TABLE_DEVICE_MASTER+"("+ID_DEVICE_MASTER+"));";

    /*--------------------------------------------------------------------------------------------*/


    ///Configuracion de control de equipos (Configuration)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_CONFIGURACION_CONTROL_DEVICE = "Configuration_Control_Device";
    public static final String ID_CONFIGURACION_CONTROL_DEVICE = "id";
    ///Atributos Usuarios
    public static final String SerialWriteOn = "SerialWriteOn";
    public static final String SerialWriteOff = "SerialWriteOff";
    public static final String Id_Device = "Id_Device";


    private static final String CREATE_TABLE_CONFIGURACION_CONTROL_DEVICE = "create table "
            + TABLE_CONFIGURACION_CONTROL_DEVICE + "(" + ID_CONFIGURACION_CONTROL_DEVICE
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SerialWriteOn + " TEXT NULL,"
            + SerialWriteOff + " TEXT NULL,"
            + Id_Device + " INTEGER NULL,"
            +"FOREIGN KEY("+Id_Device+") REFERENCES "+TABLE_DEVICE+"("+ID_DEVICE+"));";

     /*--------------------------------------------------------------------------------------------*/

    ////Tabla Tipo Variable  (Tipe_Variable)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_TIPOS_VARIABLE = "Tipe_Variable";
    public static final String ID_TIPO_VARIABLE = "id";
    ///Atributos Tipo Variable
    public static final String Nombre_Tipo_Variable = "Name_Tipe_Variable";
    public static final String Posicion_Variable = "Position_Variable";
    public static final String Descripcion_Variable = "Description_Variable";
    public static final String Estado_Variable = "Status_Variable";
    public static final String Unidad_Medida = "Unity_Measure";

    private static final String CREATE_TABLE_TIPOS_VARIABLES = "create table "
            + TABLE_TIPOS_VARIABLE + "(" + ID_TIPO_VARIABLE
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Nombre_Tipo_Variable + " TEXT NULL,"
            + Posicion_Variable + " TEXT NULL,"
            + Descripcion_Variable + " TEXT NULL,"
            + Estado_Variable + " BOOLEAN NULL,"
            + Unidad_Medida + " TEXT NULL,"
            + Id_Device_Master + " INTEGER NULL,"
            +"FOREIGN KEY("+Id_Device_Master+") REFERENCES "+TABLE_DEVICE_MASTER+"("+ID_DEVICE_MASTER+"));";






    ////Tabla  Variable  (Variable)
    /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_VARIABLE = "Variable";
    public static final String ID_VARIABLE= "id";
    ///Atributos Variable
    public static final String Data_Variable = "Data_Variable";
    public static final String Fecha_Variable = "Date_Variable";
    public static final String Hora_Variable = "Hour_Variable";
    public static final String Fecha_Hora_Variable = "Date_Hour_Variable";
    public static final String Id_Tipo_Variable = "Id_Tipe_Variable";
    public static final String Estado_Sincronizacion_Variable = "Status_Sincronization_Variable";
    public static final String Id_Equipo_Remoto = "Id_Equipo_Remoto";
    private static final String CREATE_TABLE_VARIABLE = "create table "
            + TABLE_VARIABLE + "(" + ID_VARIABLE
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Data_Variable + " TEXT NULL,"
            + Fecha_Variable + " TEXT NULL,"
            + Hora_Variable + " TEXT NULL,"
            + Fecha_Hora_Variable + " TEXT NULL,"
            + Estado_Sincronizacion_Variable + " BOOLEAN NULL,"
            + Id_Equipo_Remoto + " TEXT NULL,"
            + Id_Tipo_Variable + " INTEGER NULL,"
            +"FOREIGN KEY("+Id_Tipo_Variable+") REFERENCES "+TABLE_TIPOS_VARIABLE+"("+ID_TIPO_VARIABLE+"));";
    /*--------------------------------------------------------------------------------------------*/







    ////Tabla Procesos  (Procesos)
     /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_PROCESS = "Process";
    public static final String ID_PROCESS = "id";
    ///Atributos Tipo Variable
    public static final String Name_Proceso = "Name_Proceso";
    public static final String Tiempo_Transcurrido = "Tiempo_Transcurrido";
    public static final String Fecha_Proceso = "Fecha_Proceso";
    public static final String Hora_Inicio_Proceso = "Hora_Inicio_Proceso";
    public static final String Hora_Fin_Proceso = "Hora_Fin_Proceso";
    public static final String Ended_Process = "Ended_Process";
    public static final String Tiempo_Transcurrido_Long= "Tiempo_Transcurrido_Long";

    private static final String CREATE_TABLE_PROCESS = "create table "
            + TABLE_PROCESS + "(" + ID_PROCESS
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Name_Proceso + " TEXT NULL,"
            + Tiempo_Transcurrido + " TEXT NULL,"
            + Tiempo_Transcurrido_Long + " INTEGER NULL,"
            + Fecha_Proceso + " TEXT NULL,"
            + Hora_Inicio_Proceso + " TEXT NULL,"
            + Hora_Fin_Proceso + " TEXT NULL,"
            + Ended_Process + " BOOLEAN NULL );";


    ////Tabla variableProcesos  (VariableProcesos)
     /*--------------------------------------------------------------------------------------------*/
    public static final String TABLE_VARIABLE_PROCESS = "VariableProcess";
    public static final String ID_VARIABLE_PROCESS = "id";
    ///Atributos Tipo Variable
    public static final String Valor_Deseado = "Valor_Deseado";
    public static final String Id_Proceso= "Id_Proceso";
    public static final String Fecha_Registro_Variable_Proceso= "Fecha_Registro_Variable_Proceso";
    public static final String Posicion_Variable_Proceso= "Posicion_Variable_Proceso";
    public static final String Nombre_Variable= "Nombre_Variable";


    private static final String CREATE_TABLE_VARIABLE_PROCESS = "create table "
            + TABLE_VARIABLE_PROCESS + "(" + ID_VARIABLE_PROCESS
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Nombre_Variable + " TEXT NULL,"
            + Name_Proceso + " TEXT NULL,"
            + Valor_Deseado + " INTEGER NULL,"
            + Posicion_Variable_Proceso + " INTEGER NULL,"
            + Fecha_Registro_Variable_Proceso + " TEXT NULL,"
            + Id_Proceso + " INTEGER NULL,"
            + Id_Tipo_Variable + " INTEGER NULL,"
            +"FOREIGN KEY("+Id_Proceso+") REFERENCES "+ID_PROCESS+"("+TABLE_PROCESS+")"
            +"FOREIGN KEY("+Id_Tipo_Variable+") REFERENCES "+ID_TIPO_VARIABLE+"("+TABLE_TIPOS_VARIABLE+"));";



    public DataSource(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEVICE_MASTER);
        db.execSQL(CREATE_TABLE_DEVICE);
        db.execSQL(CREATE_TABLE_CONFIGURACION_CONTROL_DEVICE);
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_TIPOS_VARIABLES);
        db.execSQL(CREATE_TABLE_VARIABLE);
        db.execSQL(CREATE_TABLE_PROCESS);

        db.execSQL(CREATE_TABLE_VARIABLE_PROCESS);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURACION_CONTROL_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_VARIABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROCESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLE_PROCESS);

        onCreate(db);
    }
}
