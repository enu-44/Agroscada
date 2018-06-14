package com.a3jfernando.serialport.pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.ConectionInternet.ConnectivityReceiver;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.Utils.Const;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.controllers.LoginController;
import com.a3jfernando.serialport.controllers.UserController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.UserData;
import com.a3jfernando.serialport.models.Usuario;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

    //Conections
    LoginController loginControllerInstance;
    UserController userControllerInstance;

    //Buton
    @BindView(R.id.email_sign_in_button)
    Button mEmailSignInButton;

    // UI references.
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    private ProgressDialog pDialog;

    ///Login
    private  String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loadInstances();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        VerificarEstadoSesion();
        registerUser();
        checkConnection();
    }

    ///Load Instances
    /*---------------------------------------------------------------------------------------------*/
    private void loadInstances() {
        this.loginControllerInstance= LoginController.getInstance(this);
        this.userControllerInstance= UserController.getInstance(this);
    }

    ///User Register Local
    private void registerUser() {
        userControllerInstance.abrirBaseDeDatos();
        Usuario user=userControllerInstance.getFirstUser();
        if(user.getIdusuario()>0){
            userControllerInstance.cerrar();
        }else{
            Usuario  userData= new Usuario();
            userData= UserData.getUser();
            userControllerInstance.deleteUsers();
            userControllerInstance.insertarUsuario(userData.getNombre_Usuario(),
                    userData.getApellido_Usuario(),
                    userData.getEstado_Sesion(),
                    userData.getClave(),
                    userData.getEmail());
            userControllerInstance.cerrar();
        }
    }

    @OnClick(R.id.email_sign_in_button)
    public void handleClickSubmit() {
        attemptLogin();
        checkConnection();
    }

    ///Verified Estado Session
    private void VerificarEstadoSesion() {
        userControllerInstance.abrirBaseDeDatos();
        Usuario user=userControllerInstance.getFirstUser();
        if(user.getIdusuario()>0){
            String estadosesion=user.getEstado_Sesion();
            if(estadosesion.equals("1")){
                LoginUserLocal(user.getEmail().toString(),user.getClave().toString());
                userControllerInstance.cerrar();
            }else{
                userControllerInstance.cerrar();
            }
        }else{
            userControllerInstance.cerrar();
        }
    }

    //Dialog Progres
    /*--------------------------------------------------------------------------------------*/
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
     /*--------------------------------------------------------------------------------------*/
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
         email = mEmailView.getText().toString();
         password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        /*if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
           //Verificar conexion a internet
            showProgressDialog();
           /// checkConnection();
            if(checkConnection())
            {
               //// LoginUserTask(email, password);
                LoginUserLocal(email, password);

            }else
            {
                LoginUserLocal(email, password);
            }
        }
    }

    //Validations
    /*--------------------------------------------------------------------------------------*/
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void LoginUserTask(final String Nombre_Usuario, final String Clave) {
            try{
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

                                    String Nombre_Usuario=resultsLogin.getNombre_Usuario().toString();
                                    String Apellido_Usuario=resultsLogin.getNombre_Usuario().toString();
                                    String Estado_Sesion="1";
                                    String Clave=resultsLogin.getClave().toString();
                                    String Email=resultsLogin.getNombre_Usuario().toString();

                                    userControllerInstance.deleteUsers();
                                    userControllerInstance.insertarUsuario(Nombre_Usuario,Apellido_Usuario,Estado_Sesion,Clave,Email);
                                    LoginUserLocal(Email,Clave);

                                    hideProgressDialog();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    hideProgressDialog();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        hideProgressDialog();
                        /*if (error.networkResponse.statusCode == 404) {
                            Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }*/
                    }
                })
                {
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
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this,"Error: "+e,Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }


    //Login SQlite
    /*------------------------------------------------------------------------------*/
    private void LoginUserLocal(String email, String password){
        loginControllerInstance.abrirBaseDeDatos();
        ///Verificar si se ha registrado un usuario en sqlite
       ArrayList<Usuario> UserRegisted=loginControllerInstance.findAllUser();
        if(UserRegisted.size()!=0){
            ///Verificar credenciales
            Usuario user=loginControllerInstance.loginUser(email,password);
            String estadosesion="1";
            loginControllerInstance.actualizarEstadoSesion(user.getIdusuario(), estadosesion);
            if(user.getIdusuario()>0){
                hideProgressDialog();
                Intent i = new Intent(LoginActivity.this,MenuControlActivity.class);
                startActivity(i);
                loginControllerInstance.cerrar();
            }
            else{
                hideProgressDialog();
                Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                loginControllerInstance.cerrar();
            }
        }else{
            hideProgressDialog();
            Toast.makeText(LoginActivity.this,"No existe un usuario registrado",Toast.LENGTH_SHORT).show();
            loginControllerInstance.cerrar();
           /// Intent i = new Intent(LoginActivity.this,MenuControlActivity.class);
            /////startActivity(i);
        }
    }


    ////Verificar conexiones a internet
    /*------------------------------------------------------------------------------
    public Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }
    public  boolean isConnectedMobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    public  boolean isConnectedWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

*/
    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connectado a Internet";
            color = R.color.GreenColor;
            showSnakBar(color,message);
        } else {
            message = "Sorry! Sin conexion a internet";
            color = R.color.DarkGrayColor;
           //// LoginUserLocal(email, password);
            showSnakBar(color,message);
        }
    }

    //Mostrar Mensage Snackbar
    /*--------------------------------------------------------------------------------------------------------*/
    private void showSnakBar(int colorPrimary,String message) {
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.email_sign_in_button), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this,colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_home_bar, 0, 0, 0);
        // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }
    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}

