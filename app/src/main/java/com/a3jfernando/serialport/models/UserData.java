package com.a3jfernando.serialport.models;

import java.util.Arrays;
import java.util.List;

/**
 * Created by EnuarMunoz on 16/08/2017.
 */

public class UserData {

    /**
     * Obtiene como lista todos los cursos de prueba
     *
     * @return Lista de cursos
     */
    public static Usuario getUser() {
        Usuario user= new Usuario();
        user.setNombre_Usuario("admin");
        user.setApellido_Usuario("admin");
        user.setEstado_Sesion("0");
        user.setEmail("admin");
        user.setClave("12345");
        return user;
    }
}
