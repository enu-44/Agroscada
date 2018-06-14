package com.a3jfernando.serialport.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.controllers.ConfigurationController;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.pages.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationActivity extends AppCompatActivity {

    @BindView(R.id.WriteOff)
    EditText editOff;

    @BindView(R.id.WriteOn)
    EditText editOnn;

    @BindView(R.id.progressBarConfiguration)
    ProgressBar progressBar;

    @BindView(R.id.edit_configuracion)
    FloatingActionButton edit;

    @BindView(R.id.register_configuracion)
    FloatingActionButton add;

    long id_configuracion;
    //Variables para validadr si se debe registrar o actualizar la configuracion
    Boolean process_register;
    Boolean process_update;
    ///Conexion BD controladores
    ConfigurationController configurationControllerInstance;
   //// private String puerto[]=new String[]{"2600","900","1200","700"};
    //Id_Device
    long Id_Device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);
        loadInstances();
        Id_Device= getIntent().getExtras().getLong("Id_Device");
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, puerto);
        spinnerPuertos.setAdapter(adapter);*/
        showProgress(false);
        verificarRegisterConfiguration();
    }

    //EVENT CLICK
    /*---------------------------------------------------------------------------------------------*/
    @OnClick({R.id.register_configuracion, R.id.edit_configuracion})
    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.register_configuracion:
                registerConfiguration();
                break;
            case R.id.edit_configuracion:
                showProgress(true);
                Update();
                break;
        }
    }

    private void registerConfiguration() {
        showProgress(true);
        boolean cancel = false;
        View focusView = null;
        if(editOnn.getText().toString().isEmpty()){
            editOnn.setError(getString(R.string.error_field_required));
            focusView = editOnn;
            cancel = true;
            showProgress(false);
        }
        if(editOff.getText().toString().isEmpty()){
            editOff.setError(getString(R.string.error_field_required));
            focusView = editOff;
            cancel = true;
            showProgress(false);
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            configurationControllerInstance.abrirBaseDeDatos();
            if(process_register==true){
                configurationControllerInstance.insertarConfiguracion(Id_Device,editOnn.getText().toString(),editOff.getText().toString());
                showSnakBar(R.color.colorAccent,"Configuracion guardada");
                Edit();
                configurationControllerInstance.cerrar();
            }
            else if(process_update==true){
                configurationControllerInstance.actualizarConfiguracion(Id_Device,id_configuracion,editOnn.getText().toString(),editOff.getText().toString());
                showSnakBar(R.color.colorAccent,"Configuracion actualizada");
                ///editBaudrate.setText(spinnerPuertos.getSelectedItem().toString());
                Edit();
                configurationControllerInstance.cerrar();
            }
        }
    }

    private void loadInstances() {
        this.configurationControllerInstance= ConfigurationController.getInstance(this);
    }

    //Verificar si existe una confiracion registrada
    /*----------------------------------------------------------------------------------------------------------------------*/
    private void verificarRegisterConfiguration( ) {
        //Query
        configurationControllerInstance.abrirBaseDeDatos();
       Configuration configuracion=configurationControllerInstance.getFirstConfiguracion(Id_Device);
        if(configuracion.getIdconfiguration()>0){
            process_register=false;
            process_update=true;
            id_configuracion=configuracion.getIdconfiguration();
            editOff.setText(configuracion.getSerialWriteOff());
            editOnn.setText(configuracion.getSerialWriteOn());
            Edit();
            configurationControllerInstance.cerrar();
        }else{
            Update();
            process_register=true;
            process_update=false;
            configurationControllerInstance.cerrar();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    public void Edit(){
        showProgress(false);
        editOff.setEnabled(false);
        editOnn.setEnabled(false);
        edit.setEnabled(true);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        add.setEnabled(false);
        add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    public void Update(){
        showProgress(false);
        editOff.setEnabled(true);
        editOnn.setEnabled(true);
        edit.setEnabled(false);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        add.setEnabled(true);
        add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
    }

    //Mostrar Mensage Snackbar
    /*--------------------------------------------------------------------------------------------------------*/
    private void showSnakBar(int colorPrimary,String message) {
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.login_form), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this,colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_home_bar, 0, 0, 0);
        // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        textView.setTextColor(color);
        snackbar.show();
    }

    ///Metodo que permite no recargar la pagina al devolverse
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Comprobar si DetailActivity no se creó desde CourseActivity
                if (NavUtils.shouldUpRecreateTask(this, upIntent)
                        || this.isTaskRoot()) {

                    // Construir de nuevo la tarea para ligar ambas actividades
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    this.finishAfterTransition();
                    return true;
                }
                //Para versiones anterios a 5.x
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    onBackPressed();
                    return true;
                }
                // Dejar que el sistema maneje el comportamiento del up button
                return false;
        }
    }
}
