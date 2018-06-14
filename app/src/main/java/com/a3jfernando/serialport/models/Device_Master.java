package com.a3jfernando.serialport.models;

/**
 * Created by admi on 15/04/17.
 */

public class Device_Master {
    //Atributes
    private long Id_Device_Master;
    private String Android_Id;
    private String Device_Id;
    private String Software_Version;
    private String Local_Ip_Address;
    private String Remote_Ip_Address;
    private String Android_Version;
    private String MacAddr;
    private String Device_Name;


    ///Construtor
    public Device_Master() {
        this.Id_Device_Master = Id_Device_Master;
        this.Android_Id = Android_Id;
        this.Device_Id = Device_Id;
        this.Software_Version=Software_Version;
        this.Local_Ip_Address=Local_Ip_Address;
        this.Android_Version=Android_Version;
        this.MacAddr=MacAddr;
        this.Device_Name=Device_Name;

    }

    //Methods


    public long getId_Device_Master() {
        return Id_Device_Master;
    }

    public void setId_Device_Master(long id_Device_Master) {
        Id_Device_Master = id_Device_Master;
    }

    public String getAndroid_Id() {
        return Android_Id;
    }

    public void setAndroid_Id(String android_Id) {
        Android_Id = android_Id;
    }

    public String getDevice_Id() {
        return Device_Id;
    }

    public void setDevice_Id(String device_Id) {
        Device_Id = device_Id;
    }

    public String getSoftware_Version() {
        return Software_Version;
    }

    public void setSoftware_Version(String software_Version) {
        Software_Version = software_Version;
    }

    public String getLocal_Ip_Address() {
        return Local_Ip_Address;
    }

    public void setLocal_Ip_Address(String local_Ip_Address) {
        Local_Ip_Address = local_Ip_Address;
    }

    public String getRemote_Ip_Address() {
        return Remote_Ip_Address;
    }

    public void setRemote_Ip_Address(String remote_Ip_Address) {
        Remote_Ip_Address = remote_Ip_Address;
    }

    public String getAndroid_Version() {
        return Android_Version;
    }

    public void setAndroid_Version(String android_Version) {
        Android_Version = android_Version;
    }

    public String getMacAddr() {
        return MacAddr;
    }

    public void setMacAddr(String macAddr) {
        MacAddr = macAddr;
    }

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String device_Name) {
        Device_Name = device_Name;
    }

}
