package com.a3jfernando.serialport.pages;

import android.Manifest;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.ConectionInternet.ConnectivityReceiver;
import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.IdentifyEquipo.Equipment_Identifier;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.controllers.Device_Master_Controller;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Device;
import com.a3jfernando.serialport.models.Device_Master;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceMasterActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    //Services
    Device_Master_Controller device_Master_ControllerInstance;
    Equipment_Identifier equipment_Identifier;

    //Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtAndroid_Id)
    TextView txtAndroid_Id;

    @BindView(R.id.txtDevice_Id)
    TextView txtDevice_Id;

    @BindView(R.id.txtSoftware_Version)
    TextView txtSoftware_Version;

    @BindView(R.id.txtLocal_Ip_Address)
    TextView txtLocal_Ip_Address;

    @BindView(R.id.txtAndroidVersion)
    TextView txtAndroidVersion;

    @BindView(R.id.txtMacAddr)
    TextView txtMacAddr;

    @BindView(R.id.txtDeviceName)
    TextView txtDeviceName;

    @BindView(R.id.txtIpRemote)
    TextView txtIpRemote;

    //Permission
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    //Locals
    long Id_Device_Master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_master);

        ButterKnife.bind(this);
        setToolbar();
        loadInstances();
        Id_Device_Master= getIntent().getExtras().getLong("Id_Device_Master");
        loadPropertiesDeviceMaster();
    }

    private void loadInstances() {
        this.device_Master_ControllerInstance= Device_Master_Controller.getInstance(this);
        equipment_Identifier = new Equipment_Identifier(this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadPropertiesDeviceMaster() {
        device_Master_ControllerInstance.abrirBaseDeDatos();
        Device_Master properties_device_master= device_Master_ControllerInstance.getFirstDeviceMaster();
        txtAndroid_Id.setText(properties_device_master.getAndroid_Id());
        txtDevice_Id.setText(properties_device_master.getDevice_Id());
        txtSoftware_Version.setText(properties_device_master.getSoftware_Version());
        txtLocal_Ip_Address.setText(properties_device_master.getLocal_Ip_Address());
        txtAndroidVersion.setText(properties_device_master.getAndroid_Version());
        txtMacAddr.setText(properties_device_master.getMacAddr());
        txtDeviceName.setText(properties_device_master.getDevice_Name());
        txtIpRemote.setText(properties_device_master.getRemote_Ip_Address());
        device_Master_ControllerInstance.cerrar();
    }



    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    /*Conection Internet*/
    /*---------------------------------------------------------------------------------------------*/
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
                .make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this,colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_home_bar, 0, 0, 0);
        // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }
    /*----------------------------------------------------------------------------------------------*/
    ////Opciones Menu
       /*Opciones del menu principal*/
    /*--------------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_device_master,menu);
        return super.onCreateOptionsMenu(menu);
    }

    ///Eventos del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_update_info_device_master:
                getPropertiesEquipoMasterPermission();
                break;
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
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*----------------------------------------------------------------------------------------------------------------------*/

     ///Update Properties Device Master
     ///Permiso para obtener informacion del dispositivo
    /*-------------------------------------------------------------------------------------------------------*/
     public void getPropertiesEquipoMasterPermission() {
         // Check if the READ_PHONE_STATE permission is already available.
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                 != PackageManager.PERMISSION_GRANTED) {
             // READ_PHONE_STATE permission has not been granted.
             requestReadPhoneStatePermission();
         } else {
             // READ_PHONE_STATE permission is already been granted.
             doPermissionGrantedStuffs();
         }
     }

    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(DeviceMasterActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Permiso")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(DeviceMasterActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                            doPermissionGrantedStuffs();
                        }
                    })
                    .setIcon(R.drawable.ledon)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // Now read the desired content to a textview.
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String Device_Id= equipment_Identifier.getDeviceID(telephonyManager,DeviceMasterActivity.this);
        String Android_Id= equipment_Identifier.getDeviceUniqueID(DeviceMasterActivity.this);
        /// String SIMSerialNumber=tm.getSimSerialNumber();
        String Software_Version=tm.getDeviceSoftwareVersion();
        /*
        String SIMCountryISO=tm.getSimCountryIso();*/
        String Remote_Ip_Address=equipment_Identifier.NetwordDetect();
        String Local_Ip_Address=equipment_Identifier.getLocalIpAddress();
        String Android_Version=equipment_Identifier.getAndroidVersion();
        String MacAddr=equipment_Identifier.getMacAddr();
        String Device_Name=equipment_Identifier.getDeviceName();
        ////Insertar Datos Equipo Maestro (Equipment master)
        device_Master_ControllerInstance.abrirBaseDeDatos();
        device_Master_ControllerInstance.actualizarDeviceMaster(Id_Device_Master,Remote_Ip_Address,Android_Id,Device_Id, Software_Version, Local_Ip_Address, Android_Version,  MacAddr, Device_Name);
        ///Obtener properties updates device master
        device_Master_ControllerInstance.cerrar();
        loadPropertiesDeviceMaster();

        Toast.makeText(DeviceMasterActivity.this, "Se actualizo correctamente",Toast.LENGTH_SHORT).show();
    }
}
