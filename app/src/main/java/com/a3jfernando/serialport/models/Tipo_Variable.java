package com.a3jfernando.serialport.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admi on 22/03/17.
 */
public class Tipo_Variable implements Parcelable {
    //Atributes
    long Id_Tipo_Variable;
    String Nombre_Tipo_Variable ;
    int Posicion_Variable;
    String Descripcion_Variable ;
    boolean Estado_Variable;
    String Unidad_Medida;
    String Valor;



    //Constructor
    public Tipo_Variable() {
        this.Id_Tipo_Variable = Id_Tipo_Variable;
        this.Nombre_Tipo_Variable = Nombre_Tipo_Variable;
        this.Posicion_Variable = Posicion_Variable;
        this.Descripcion_Variable = Descripcion_Variable;
        this.Estado_Variable = Estado_Variable;
        this.Unidad_Medida = Unidad_Medida;
        this.Valor= Valor;
    }

    //Methods
    public long getId_Tipo_Variable() {
        return Id_Tipo_Variable;
    }

    public void setId_Tipo_Variable(long id_Tipo_Variable) {
        Id_Tipo_Variable = id_Tipo_Variable;
    }

    public String getNombre_Tipo_Variable() {
        return Nombre_Tipo_Variable;
    }

    public void setNombre_Tipo_Variable(String nombre_Tipo_Variable) {
        Nombre_Tipo_Variable = nombre_Tipo_Variable;
    }

    public int getPosicion_Variable() {
        return Posicion_Variable;
    }

    public void setPosicion_Variable(int posicion_Variable) {
        Posicion_Variable = posicion_Variable;
    }

    public String getDescripcion_Variable() {
        return Descripcion_Variable;
    }

    public void setDescripcion_Variable(String descripcion_Variable) {
        Descripcion_Variable = descripcion_Variable;
    }

    public boolean getEstado_Variable() {
        return Estado_Variable;
    }

    public void setEstado_Variable(boolean estado_Variable) {
        Estado_Variable = estado_Variable;
    }

    public String getUnidad_Medida() {
        return Unidad_Medida;
    }

    public void setUnidad_Medida(String unidad_Medida) {
        Unidad_Medida = unidad_Medida;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }


    @Override
    public String toString() {
        return Nombre_Tipo_Variable;
    }


    protected Tipo_Variable(Parcel in) {
        Id_Tipo_Variable = in.readLong();
        Nombre_Tipo_Variable = in.readString();
        Posicion_Variable = in.readInt();
        Descripcion_Variable = in.readString();
        Estado_Variable = in.readByte() != 0x00;
        Unidad_Medida = in.readString();
        Valor= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id_Tipo_Variable);
        dest.writeString(Nombre_Tipo_Variable);
        dest.writeInt(Posicion_Variable);
        dest.writeString(Descripcion_Variable);
        dest.writeByte((byte) (Estado_Variable ? 0x01 : 0x00));
        dest.writeString(Unidad_Medida);
        dest.writeString(Valor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tipo_Variable> CREATOR = new Parcelable.Creator<Tipo_Variable>() {
        @Override
        public Tipo_Variable createFromParcel(Parcel in) {
            return new Tipo_Variable(in);
        }

        @Override
        public Tipo_Variable[] newArray(int size) {
            return new Tipo_Variable[size];
        }
    };
}
