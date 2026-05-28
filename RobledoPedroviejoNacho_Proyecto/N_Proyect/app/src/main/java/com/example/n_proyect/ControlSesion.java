package com.example.n_proyect;

import android.content.Context;
import android.content.SharedPreferences;

public class ControlSesion {

    private static final String PREF_NOMBRE = "sesion_usuario";
    private static final String CLAVE_CORREO = "correo";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ControlSesion(Context context) {
        prefs = context.getSharedPreferences(PREF_NOMBRE, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void guardarSesion(String correo) {
        editor.putString(CLAVE_CORREO, correo);
        editor.apply();
    }


    public String obtenerCorreo() {
        return prefs.getString(CLAVE_CORREO, null);
    }


    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }
}