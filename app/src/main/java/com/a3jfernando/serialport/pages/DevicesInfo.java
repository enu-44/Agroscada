package com.a3jfernando.serialport.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.controllers.DeviceController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesInfo extends AppCompatActivity implements View.OnClickListener{

    //Conections Services
    DeviceController deviceControllerInstance;

    //UI Elements
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinnerTipoEquipo)
    Spinner spinnerTipoEquipo;

    @BindView(R.id.btn_register_equipo)
    FloatingActionButton btnRegistrarEquipo;

    @BindView(R.id.btn_edit_equipo)
    FloatingActionButton btnEditarEquipo;

    @BindView(R.id.btn_delete_device)
    FloatingActionButton btnDeleteDevice;

    @BindView(R.id.txtName_Device)
    EditText txtNombre_Equipo;

    @BindView(R.id.txtDescripcionDevice)
    EditText txtDescripcionEquipo;

    @BindView(R.id.progressBarEquipo)
    ProgressBar progressBar;


    ///Atributes EquipmentMaster
    long Id_Device_Master,Id_Device;
    String Accion;

    String  Description_Device,Name_Device,Image_Type_Device;
    boolean Is_Active;

    private String opcionesImageTipoDevice[]=new String[]{"Ventilador","Motor","Luces","Motobomba","Otros"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_info);
        ButterKnife.bind(this);
        setToolbar();
        loadInstances();
        loadAdapter();
        //Revivir Datos
        reciveDatesIntent();
        eventsOnClickListeener();
        verficateAction();
        showProgress(false);
    }

    private void reciveDatesIntent() {
        Id_Device_Master = getIntent().getExtras().getLong("Id_Device_Master");
        Accion= getIntent().getExtras().getString("Accion");
    }

    private void eventsOnClickListeener() {
        btnRegistrarEquipo.setOnClickListener(this);
        btnEditarEquipo.setOnClickListener(this);
        btnDeleteDevice.setOnClickListener(this);
    }

    private void verficateAction() {
        //Verificar si se crea o actualiza informacion de un equipo
        if(Accion.equals("create")){
            Update();
        }else if(Accion.equals("update")){
            //Recivir datos para actualizacion
            Description_Device= getIntent().getExtras().getString("Description_Device");
            Name_Device= getIntent().getExtras().getString("Name_Device");
            Id_Device= getIntent().getExtras().getLong("Id_Device");
            //Set value sppinner
            Image_Type_Device= getIntent().getExtras().getString("Image_Type_Device");
            ArrayAdapter myAdap = (ArrayAdapter) spinnerTipoEquipo.getAdapter();
            int spinnerPosition = myAdap.getPosition(Image_Type_Device);
            //set the default according to value
            spinnerTipoEquipo.setSelection(spinnerPosition);

            //Asignar Informacion
            txtDescripcionEquipo.setText(Description_Device);
            txtNombre_Equipo.setText(Name_Device);
            ///Set enable
            txtDescripcionEquipo.setEnabled(false);
            txtNombre_Equipo.setEnabled(false);
            Edit();
        }
    }

    private void loadAdapter() {
        //Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, opcionesImageTipoDevice);
        spinnerTipoEquipo.setAdapter(adapter);
    }

    private void loadInstances() {
        this.deviceControllerInstance= DeviceController.getInstance(this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_equipo:
                showProgress(true);
                boolean cancel = false;
                View focusView = null;
                if(txtNombre_Equipo.getText().toString().isEmpty()){
                    txtNombre_Equipo.setError(getString(R.string.error_field_required));
                    focusView = txtNombre_Equipo;
                    cancel = true;
                    showProgress(false);
                }
                else if(txtDescripcionEquipo.getText().toString().isEmpty()){
                    txtDescripcionEquipo.setError(getString(R.string.error_field_required));
                    focusView = txtDescripcionEquipo;
                    cancel = true;
                    showProgress(false);
                }
                String selectedActuador="";

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    deviceControllerInstance.abrirBaseDeDatos();
                    selectedActuador=ImageActuador(spinnerTipoEquipo.getSelectedItem().toString());
                    String Name_Device=txtNombre_Equipo.getText().toString();
                    String Description_Device=txtDescripcionEquipo.getText().toString();
                    boolean Is_Active=true;
                    if(Accion.equals("create")){
                        deviceControllerInstance.insertarEquipmen( Name_Device, Description_Device, Is_Active, Id_Device_Master,selectedActuador);
                        Toast.makeText(DevicesInfo.this, "Se registro correctamente",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        LimpiarCampos();
                        deviceControllerInstance.cerrar();
                        finish();
                    }
                    else if(Accion.equals("update")){
                        selectedActuador=ImageActuador(spinnerTipoEquipo.getSelectedItem().toString());
                        deviceControllerInstance.updateInfoDevice(Id_Device, Name_Device, Description_Device, Is_Active, Id_Device_Master,selectedActuador);;
                        Toast.makeText(DevicesInfo.this, "Se actualizo correctamente",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        deviceControllerInstance.cerrar();
                        Edit();
                    }

                }
                break;
            case  R.id.btn_edit_equipo:
                Update();
                Toast.makeText(DevicesInfo.this, "Editar",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_delete_device:
                deviceControllerInstance.abrirBaseDeDatos();
                deviceControllerInstance.eliminarDevice(Id_Device);
                deviceControllerInstance.cerrar();
                finish();
                ////Toast.makeText(DevicesInfo.this, "Eliminado ",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void LimpiarCampos(){
        txtDescripcionEquipo.setText(null);
        txtNombre_Equipo.setText(null);
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
        spinnerTipoEquipo.setEnabled(false);
        txtNombre_Equipo.setEnabled(false);
        txtDescripcionEquipo.setEnabled(false);
        btnEditarEquipo.setEnabled(true);
        btnEditarEquipo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        btnRegistrarEquipo.setEnabled(false);
        btnRegistrarEquipo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        btnDeleteDevice.setEnabled(true);
        btnDeleteDevice.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent
        )));
    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    public void Update(){
        spinnerTipoEquipo.setEnabled(true);
        txtNombre_Equipo.setEnabled(true);
        txtDescripcionEquipo.setEnabled(true);
        btnEditarEquipo.setEnabled(false);
        btnEditarEquipo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        btnRegistrarEquipo.setEnabled(true);
        btnRegistrarEquipo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        btnDeleteDevice.setEnabled(false);
        btnDeleteDevice.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
    }

    //Get Image Type Actuador/Equipo
    public  String ImageActuador(String imageSelected){
        String imageReturn="";
        if(imageSelected.equals("Ventilador")){
            imageReturn="ic_icon_ventilador";
        }else if(imageSelected.equals("Motor")){
            imageReturn="ic_icon_motor";
        }else  if(imageSelected.equals("Luces")){
            imageReturn="ic_icon_bombillo";
        }else  if(imageSelected.equals("Motobomba")){
            imageReturn="ic_icon_motobomba";
        }else  if(imageSelected.equals("Otros")){
            imageReturn="ic_icon_actuador";
        }
       return imageReturn;
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
