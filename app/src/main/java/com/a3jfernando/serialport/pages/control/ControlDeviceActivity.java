package com.a3jfernando.serialport.pages.control;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.UsbService;
import com.a3jfernando.serialport.controllers.ConfigurationController;
import com.a3jfernando.serialport.models.Configuration;

import java.lang.ref.WeakReference;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ControlDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_digital_write_off)
    FloatingActionButton btnSerialOff;

    @BindView(R.id.btn_digital_write_on)
    FloatingActionButton btnSerialOn;
    /*
    @BindView(R.id.btnSendSerialControl)
    Button btnSendSerialControl;

    @BindView(R.id.etDigitalWriteSerialControlSend)
    EditText etDigitalSerialControl;*/
    //ID_Device
    long Id_Device;

    ///Services USB
    private boolean usbConected;
    private boolean usbPermision;
    private UsbService usbService;
    private MyHandler mHandler;

    //Configuration Serial
    String SerialOn, SerialOff;
    boolean verificate;

    ///Conexion BD controladores
    ConfigurationController configurationControllerInstance;

    //*EDIT CONFIGURATION CONTROL*/
    //Buttons
    FloatingActionButton add, edit;
    ProgressBar progressBar;
    //Variables para validadr si se debe registrar o actualizar la configuracion
    Boolean process_register;
    Boolean process_update;
    EditText editOff, editOnn;
    //Variables
    long id_configuracion;
    //Dialog
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);
        ButterKnife.bind(this);
        setToolbar();
        loadInstances();
        mHandler = new MyHandler(this);
        Id_Device = getIntent().getExtras().getLong("Id_Device");
        loadEventClickListener();
        verificarConf();
        verificateConnectionSerial();
    }

    private void loadEventClickListener() {
        btnSerialOn.setOnClickListener(this);
        btnSerialOff.setOnClickListener(this);
        //States Buttons
        btnSerialOff.setImageResource(R.drawable.pressedon);
        btnSerialOn.setImageResource(R.drawable.pressedoff);
    }

    private void loadInstances() {
        this.configurationControllerInstance = ConfigurationController.getInstance(this);
    }

    //Toolbar
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
   * Notifications from UsbService will be received here.
   */
    /*----------------------------------------------------------------------------------------------------------------------*/
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    usbPermision=true;

                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    usbPermision=false;
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    usbPermision=false;
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    ///Servicio de conexion uerto erial USB
    /*----------------------------------------------------------------------------------------------------------------------*/
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    /*----------------------------------------------------------------------------------------------------------------------*/


    ///Funtions USB Serial
    /*--------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------------*/
     /*
     * Notifications from UsbService will be received here.
     */
    /*----------------------------------------------------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_digital_write_off:

                btnSerialOff.setImageResource(R.drawable.pressedon);
                btnSerialOn.setImageResource(R.drawable.pressedoff);

                if (verificate == true) {
                    ///String data = configuracion.getSerialWriteOn();
                    usbService.write(SerialOn.getBytes());
                } else {
                    Toast.makeText(ControlDeviceActivity.this, "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_digital_write_on:
                btnSerialOn.setImageResource(R.drawable.pressedon);
                btnSerialOff.setImageResource(R.drawable.pressedoff);
                if (verificate == true) {
                    usbService.write(SerialOff.getBytes());
                } else {
                    Toast.makeText(ControlDeviceActivity.this, "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_configuracion:
                RegisterConfiguration();
                break;
            case R.id.edit_configuracion:
                verificarRegisterConfiguration();
                showProgress(true);
                Update();
                break;
            /*case R.id.btnSendSerialControl:
                String value;
                boolean cancel = false;
                View focusView = null;
                // Reset errors.
                etDigitalSerialControl.setError(null);
                // Store values at the time of the login attempt.
                value = etDigitalSerialControl.getText().toString();
               //// Toast.makeText(this,"Texto"+value,Toast.LENGTH_SHORT).show();
                // Check for a valid email address.
                if (TextUtils.isEmpty(value)) {
                    etDigitalSerialControl.setError(getString(R.string.error_field_required));
                    focusView = etDigitalSerialControl;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    String data = etDigitalSerialControl.getText().toString();
                    if(usbPermision==true && usbConected==true){
                        usbService.write(data.getBytes());
                    }else{
                        int color = Color.WHITE;
                        String message = "Verifique la conexion puerto serial";
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.contentControlLayout), message, Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(color);
                        snackbar.show();
                    }
                }

                break;*/
        }
    }


    public void verificateConnectionSerial() {

        if (usbPermision == true && usbConected == true) {
            btnSerialOff.setEnabled(true);
            btnSerialOn.setEnabled(true);
            btnSerialOff.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            btnSerialOn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else {
            btnSerialOff.setEnabled(true);
            btnSerialOn.setEnabled(true);
            btnSerialOff.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
            btnSerialOn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
            Toast.makeText(ControlDeviceActivity.this, "No USB connected", Toast.LENGTH_SHORT).show();
        }
    }


    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        try {

            super.onResume();
            setFilters();  // Start listening notifications from UsbService
            startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
        } catch (Exception e) {

        }


    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onPause() {

        try {

            super.onPause();
            unregisterReceiver(mUsbReceiver);
            unbindService(usbConnection);
        } catch (Exception e) {

        }

    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {

        try {
            if (!UsbService.SERVICE_CONNECTED) {
                Intent startService = new Intent(this, service);
                if (extras != null && !extras.isEmpty()) {
                    Set<String> keys = extras.keySet();
                    for (String key : keys) {
                        String extra = extras.getString(key);
                        startService.putExtra(key, extra);
                    }
                }
                startService(startService);
            }
            Intent bindingIntent = new Intent(this, service);
            bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {

        }
    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    private void setFilters() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
            filter.addAction(UsbService.ACTION_NO_USB);
            filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
            filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
            filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
            registerReceiver(mUsbReceiver, filter);
        } catch (Exception e) {

        }

    }


    /*
    * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
    */
    /*----------------------------------------------------------------------------------------------------------------------*/
    public static class MyHandler extends Handler {
        private WeakReference<ControlDeviceActivity> mActivity;
        public MyHandler(ControlDeviceActivity activity) {
            try {
                mActivity = new WeakReference<>(activity);
            } catch (Exception e) {

            }
        }
        @Override
        public void handleMessage(Message msg) {

            try {
                switch (msg.what) {
                    case UsbService.MESSAGE_FROM_SERIAL_PORT:
                        String data = (String) msg.obj; //Extract the string from the Message
                        break;
                    case UsbService.CTS_CHANGE:
                        Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                        break;
                    case UsbService.DSR_CHANGE:
                        Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception e) {
            }
        }
    }


    ////Opciones Menu
    ////*Opciones del menu principal*/
    /*--------------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_control, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_actions_control:
                DetailTipovariable();
                break;
            ///Metodo que permite no recargar la pagina al devolverse
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Comprobar si DetailActivity no se creó desde CourseActivity
                if (NavUtils.shouldUpRecreateTask(this, upIntent)
                        || this.isTaskRoot()) {

                    // Construir de nuevo la tarea para ligar ambas actividades
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(upIntent)
                                .startActivities();
                    }
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








    /*EDIT CONTROL*/
    /*------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------*/
    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public Dialog DetailTipovariable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        v=inflater.inflate(R.layout.dialog_edit_configuration_control, null);
        builder.setView(v);
        builder.setTitle("Actualizar Datos");

        //Instances Tools
        add = (FloatingActionButton) v.findViewById(R.id.register_configuracion);
        edit = (FloatingActionButton) v.findViewById(R.id.edit_configuracion);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBarConfiguration);

        showProgress(false);
        add.setOnClickListener(this);
        edit.setOnClickListener(this);


        editOff= (EditText)v.findViewById(R.id.WriteOff);
        editOnn= (EditText)v.findViewById(R.id.WriteOn);


        verificarRegisterConfiguration();

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(v, "No se realizaron cambios", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        builder.setMessage("¿Configurar ON/OFF?");
        builder.setIcon(R.drawable.ledon);
        return builder.show();
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
            verificate=true;

            ///Set Values Serial
            SerialOff=configuracion.getSerialWriteOff();
            SerialOn=configuracion.getSerialWriteOn();
            configurationControllerInstance.cerrar();
        }else{
            ///Set Values Serial
            Update();
            process_register=true;
            process_update=false;
            verificate=false;
            ///Set Values Serial
            SerialOff="";
            SerialOn="";
            configurationControllerInstance.cerrar();
        }
    }



    //Verificate register configuration initial
    private void verificarConf( ) {
        //Query
        configurationControllerInstance.abrirBaseDeDatos();
        Configuration configuracion=configurationControllerInstance.getFirstConfiguracion(Id_Device);
        if(configuracion.getIdconfiguration()>0){
            verificate=true;
            SerialOff=configuracion.getSerialWriteOff();
            SerialOn=configuracion.getSerialWriteOn();
            configurationControllerInstance.cerrar();

        }else{
            verificate=false;
            SerialOff="";
            SerialOn="";
            configurationControllerInstance.cerrar();
        }
    }


    //Methods Actions
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


    private void RegisterConfiguration() {
        showProgress(true);
        String message;
        int color;
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
            if(process_register==true) {
                configurationControllerInstance.abrirBaseDeDatos();
                configurationControllerInstance.insertarConfiguracion(Id_Device, editOnn.getText().toString(), editOff.getText().toString());
                //Set Values Serial
                SerialOff=editOff.getText().toString();
                SerialOn=editOnn.getText().toString();
                verificate=true;

                //Message
                message = "Configuracion guardada";
                color = Color.WHITE;
                Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                // snackbar.setActionTextColor(color);
                snackbar.show();
                Edit();
                //Click snackbar
               /* snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainScreen.this, "snackbar OK clicked", Toast.LENGTH_LONG).show();
                    }
                });*/
               configurationControllerInstance.cerrar();
            }
            else if(process_update==true){
                configurationControllerInstance.abrirBaseDeDatos();
                configurationControllerInstance.actualizarConfiguracion(Id_Device,id_configuracion,editOnn.getText().toString(),editOff.getText().toString());
                //Set Values Serial
                SerialOff=editOff.getText().toString();
                SerialOn=editOnn.getText().toString();
                verificate=true;

                //Message
                message = "Configuracion actualizada";
                color = Color.WHITE;
                Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
               // snackbar.setActionTextColor(color);
                snackbar.show();
                Edit();
                configurationControllerInstance.cerrar();

            }
        }
    }

    //*DIALOG PERSONALITY
    /*dialog_share = new Dialog(MainScreen.this, R.style.DialogTheme);
dialog_share.requestWindowFeature(Window.FEATURE_NO_TITLE);
    LayoutInflater inflater = this.getLayoutInflater();
    mDialogView = inflater.inflate(R.layout.dialog_share, null);
dialog_share.setContentView(mDialogView);
dialog_share.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
dialog_share.show();

    public void ShowSnackBarNoInternetOverDialog() {
        Snackbar snackbar = Snackbar.make(mDialogView, getString(R.string.checkinternet), Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.CYAN);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainScreen.this, "snackbar OK clicked", Toast.LENGTH_LONG).show();
            }
        });
        snackbar.show();
    }

*/
}
