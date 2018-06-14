package com.a3jfernando.serialport.models;

/**
 * Created by admi on 14/03/17.
 */

public class Configuration {

    ///Atributes
    private long idconfiguration;
    private String SerialWriteOn;
    private String SerialWriteOff;
    private long Id_Device;

    ///Constructor
    public Configuration() {
        this.idconfiguration = idconfiguration;
        this.SerialWriteOn=SerialWriteOn;
        this.SerialWriteOff=SerialWriteOff;
        this.Id_Device=Id_Device;

    }


    ///Methods
    public long getId_Device() {
        return Id_Device;
    }

    public void setId_Device(long id_Device) {
        Id_Device = id_Device;
    }

    public long getIdconfiguration() {
        return idconfiguration;
    }

    public void setIdconfiguration(long idconfiguration) {
        this.idconfiguration = idconfiguration;
    }

    public String getSerialWriteOn() {
        return SerialWriteOn;
    }

    public void setSerialWriteOn(String serialWriteOn) {
        SerialWriteOn = serialWriteOn;
    }

    public String getSerialWriteOff() {
        return SerialWriteOff;
    }

    public void setSerialWriteOff(String serialWriteOff) {
        SerialWriteOff = serialWriteOff;
    }



}
