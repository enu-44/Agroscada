package com.a3jfernando.serialport.models;

/**
 * Created by admi on 14/03/17.
 */

public class Usuario {

    private long idusuario;
    private String Nombre_Usuario;
    private String Apellido_Usuario;
    private String Estado_Sesion;
    private String Email;
    private String Clave;


    //Constructor


    public Usuario(String nombre_Usuario, String apellido_Usuario, String estado_Sesion, String email, String clave) {
        Nombre_Usuario = nombre_Usuario;
        Apellido_Usuario = apellido_Usuario;
        Estado_Sesion = estado_Sesion;
        Email = email;
        Clave = clave;
    }

    public Usuario() {
        this.idusuario = idusuario;
        Nombre_Usuario = Nombre_Usuario;
        Apellido_Usuario = Apellido_Usuario;
        Estado_Sesion = Estado_Sesion;
        Email = Email;
        Clave = Clave;
    }

    //Methods

    public long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(long idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre_Usuario() {
        return Nombre_Usuario;
    }

    public void setNombre_Usuario(String nombre_Usuario) {
        Nombre_Usuario = nombre_Usuario;
    }

    public String getApellido_Usuario() {
        return Apellido_Usuario;
    }

    public void setApellido_Usuario(String apellido_Usuario) {
        Apellido_Usuario = apellido_Usuario;
    }

    public String getEstado_Sesion() {
        return Estado_Sesion;
    }

    public void setEstado_Sesion(String estado_Sesion) {
        Estado_Sesion = estado_Sesion;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }






}
