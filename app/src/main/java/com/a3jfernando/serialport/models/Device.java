package com.a3jfernando.serialport.models;

/**
 * Created by admi on 15/04/17.
 */

public class Device {
    //Atributes

    private long Id_Device;
    private String Name_Device;
    private String Description_Device;
    private boolean Is_Active;
    private long Id_Device_Master;



    private String Image_Type_Device;

    ///Construtor
    public Device() {
        this.Id_Device = Id_Device;
        this.Name_Device=Name_Device;
        this.Description_Device=Description_Device;
        this.Is_Active=Is_Active;
        this.Id_Device_Master=Id_Device_Master;
        this.Image_Type_Device= Image_Type_Device;

    }

    ///Methods

    public long getId_Device() {
        return Id_Device;
    }

    public void setId_Device(long id_Device) {
        Id_Device = id_Device;
    }

    public String getName_Device() {
        return Name_Device;
    }

    public void setName_Device(String name_Device) {
        Name_Device = name_Device;
    }

    public String getDescription_Device() {
        return Description_Device;
    }

    public void setDescription_Device(String description_Device) {
        Description_Device = description_Device;
    }

    public boolean getIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        Is_Active = is_Active;
    }

    public long getId_Device_Master() {
        return Id_Device_Master;
    }

    public void setId_Device_Master(long id_Device_Master) {
        Id_Device_Master = id_Device_Master;
    }

    public String getImage_Type_Device() {
        return Image_Type_Device;
    }

    public void setImage_Type_Device(String image_Type_Device) {
        Image_Type_Device = image_Type_Device;
    }


}
