package com.a3jfernando.serialport.Services.ApiService;


import android.content.Context;

import com.a3jfernando.serialport.Services.Utils.Const;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.models.Usuario;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admi on 17/03/17.
 */

public class LoginService  {
    private Context ourcontext;
    public LoginService(Context c) {
        ourcontext = c;
    }



    public Usuario LoginUser(final String Nombre_Usuario, final String Clave) {
        final Usuario resultsLogin = new Usuario();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                Const.URL_,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            resultsLogin.setEstado_Sesion(obj.getString("_id"));
                            resultsLogin.setNombre_Usuario(obj.getString("nombre_usuario"));
                            resultsLogin.setClave(obj.getString("clave"));
                            resultsLogin.setApellido_Usuario(obj.getString("nombre_completo_admin"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre_usuario", Nombre_Usuario);
                params.put("clave", Clave);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjRequest);



        return resultsLogin;

    }
}
