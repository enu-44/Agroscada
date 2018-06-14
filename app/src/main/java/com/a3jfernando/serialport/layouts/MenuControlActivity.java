package com.a3jfernando.serialport.layouts;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.Adapters.AdapterFragmentsMenu;
import com.a3jfernando.serialport.Adapters.AdapterListDevices;
import com.a3jfernando.serialport.ConectionInternet.ConnectivityReceiver;
import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.Services.Utils.Const;
import com.a3jfernando.serialport.SincronizarInformacionActivity;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.controllers.UserController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.fragments.ControlDeviceFragment;
import com.a3jfernando.serialport.fragments.ProcesoFragment;
import com.a3jfernando.serialport.fragments.SerialMonitorFragment;
import com.a3jfernando.serialport.fragments.VariableRealTimeFragment;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Usuario;
import com.a3jfernando.serialport.models.Variable;
import com.a3jfernando.serialport.pages.AccountActivity;
import com.a3jfernando.serialport.pages.control.ControlDeviceActivity;
import com.a3jfernando.serialport.Services.UsbService;
import com.a3jfernando.serialport.controllers.LoginController;
import com.a3jfernando.serialport.pages.ChartActivity;
import com.a3jfernando.serialport.pages.ChartLineRealTime;
import com.a3jfernando.serialport.pages.DeviceMasterActivity;
import com.a3jfernando.serialport.pages.DevicesInfo;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.IdentifyEquipo.Equipment_Identifier;
import com.a3jfernando.serialport.controllers.DeviceController;
import com.a3jfernando.serialport.controllers.Device_Master_Controller;
import com.a3jfernando.serialport.models.Device;
import com.a3jfernando.serialport.models.Device_Master;
import com.a3jfernando.serialport.pages.ConfigurationActivity;
import com.a3jfernando.serialport.pages.EquipoActivity;
import com.a3jfernando.serialport.pages.LoginActivity;
import com.a3jfernando.serialport.pages.ReportVariables;
import com.a3jfernando.serialport.pages.TiposVariableActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuControlActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.container)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.txtDataTrama)
    EditText txtDataTrama;

    private int[] tabIcons = {
            R.drawable.ic_launcher_icon_medidas,
            R.drawable.ic_launcher_icon_actuadores,
            R.drawable.ic_launcher_icon_monitor_serial,
            R.drawable.ic_settings_input_composite
    };

    private static final String TAG = "";


    private static int COUNT_SAVE_VARIABLE = 0;
    //Permission
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    ///Services
    Equipment_Identifier equipmentIdentifier;
    Device_Master_Controller deviceMasterController;
    DeviceController deviceControllerInstance;
    VariableController variableControllerInstance;
    UserController userControllerInstance;
    LoginController loginControllerInstance;
    TipoVariableController tipoVariableControllerInstance;

    public ArrayList<Device> getsetdevice;
    //Listas
    ArrayList<Tipo_Variable> listaTipeVariables = new ArrayList<Tipo_Variable>();

    ArrayList<Tipo_Variable> listaTipeVariablesRecorrida = new ArrayList<Tipo_Variable>();


    ///Atributes Device
    public long Id_Device_Master;

    //Atributes
    private long id_usuario_logued;


    ///Progres bar Horizontal
    private Handler mHandlerDialogLoad = new Handler();
    private ProgressDialog pDialog;
    int count;
    int size;
    int progres;
    double progresDouble;
    double progresDoubleSum;

    //USB SERIAL
    //Atributes
    private boolean usbConcted;
    private boolean usbPermision;
    private UsbService usbService;
    private MyHandler mHandler;
    private Handler mHandlerTask;
    boolean IsRecording;

    //Datos de sincronizacion
    public ArrayList<Variable> getsetvariablesincronizacion;

    /*
    * Notifications from UsbService will be received here.
    */
    /*----------------------------------------------------------------------------------------------------------------------*/
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    usbPermision = true;
                    usbConcted = true;
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    usbPermision = false;
                    usbConcted = false;
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    usbConcted = false;
                    usbConcted = false;
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    usbConcted = false;
                    usbPermision = false;
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    usbConcted = false;
                    usbPermision = false;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_control);
        ButterKnife.bind(this);
        // toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        loadInstances();
        verificateUserLogued();
        navigationPage();
        getInformationDeviceMaster();
        setupAdapter();
        loadVariables();

        mHandler = new MyHandler(this);
        mHandlerTask = new Handler();


        ///tareaSaveVariables();
    }


    /*
    private void tareaSaveVariables() {
        mHandlerTask.postDelayed(new Runnable() {
            public void run() {
                if(listaTipeVariablesRecorrida.size()>0){
                    saveData(listaTipeVariablesRecorrida);
                    Toast.makeText(MenuControlActivity.this, "Guardando Datos... "+String.valueOf(listaTipeVariablesRecorrida.size()), Toast.LENGTH_SHORT).show();
                    listaTipeVariablesRecorrida.clear();
                }

                mHandlerTask.postDelayed(this,5000);

            }
        }, 5000); //Every 120000 ms (2 minutes)
    }

    */


    //region ADAPTER FRAGMENTS
    private void setupAdapter() {
        Fragment[] fragments = new Fragment[]{new VariableRealTimeFragment(), new ControlDeviceFragment(), new SerialMonitorFragment(), new ProcesoFragment()};
        String[] titles = new String[]{getString(R.string.title_fragment_variables), getString(R.string.title_fragment_control), getString(R.string.title_fragment_monitor_serial), getString(R.string.title_fragment_process)};
        AdapterFragmentsMenu adapter = new AdapterFragmentsMenu(getSupportFragmentManager(), titles, fragments);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
        tabs.getTabAt(3).setIcon(tabIcons[3]);
    }
    //endregion


    public void getInformationDeviceMaster() {
        deviceMasterController.abrirBaseDeDatos();
        //Verificar registro de propieddades del equipo master
        ArrayList resultDeviceMaster = deviceMasterController.VerificateDeatailDeviceMaster();
        if (resultDeviceMaster.size() != 0) {
            ///Obtener Id Master Device
            Device_Master equipment_Master = deviceMasterController.getFirstDeviceMaster();
            Id_Device_Master = equipment_Master.getId_Device_Master();
            deviceMasterController.cerrar();
        } else {
            getPropertiesEquipoMasterPermission();
        }
    }

    private void navigationPage() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void verificateUserLogued() {
        userControllerInstance.abrirBaseDeDatos();
        Usuario user = userControllerInstance.getFirstUser();
        if (user.getIdusuario() > 0) {
            id_usuario_logued = user.getIdusuario();
            userControllerInstance.cerrar();
        } else {
            userControllerInstance.cerrar();
        }
    }

    private void loadVariables() {
        tipoVariableControllerInstance.abrirBaseDeDatos();
        listaTipeVariables = tipoVariableControllerInstance.findVariableActives();
        Toast.makeText(MenuControlActivity.this, "Variables Cargadas: " + String.valueOf(listaTipeVariables.size()), Toast.LENGTH_SHORT).show();
        tipoVariableControllerInstance.cerrar();
    }

    ///Load Instances
    /*---------------------------------------------------------------------------------------------*/
    private void loadInstances() {
        this.loginControllerInstance = LoginController.getInstance(this);
        this.userControllerInstance = UserController.getInstance(this);
        this.deviceMasterController = Device_Master_Controller.getInstance(this);
        this.deviceControllerInstance = DeviceController.getInstance(this);
        this.variableControllerInstance = VariableController.getInstance(this);
        equipmentIdentifier = new Equipment_Identifier(this);
        this.tipoVariableControllerInstance = TipoVariableController.getInstance(this);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmacion");
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Dialogos", "Confirmacion Cancelada.");
            }
        });
        builder.setMessage("¿Serrar Sesion?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cerraSesion();
                finish();
            }
        });
        builder.setIcon(R.drawable.ledon);
        builder.show();
    }

    ///Menu
    /*Opciones del menu derecha*/
    /*--------------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar_sesion:
                cerraSesion();
                finish();
                break;
            case R.id.historial_medidas:
                startActivityForResult(new Intent(MenuControlActivity.this, ReportVariables.class), 1);
                // startActivityForResult(i,1);
                break;
            case R.id.tipo_variable:
                startActivityForResult(new Intent(MenuControlActivity.this, TiposVariableActivity.class), 1);
                break;
            default:
                break;
        }
        if (item.getItemId() == R.id.action_device_master) {
            Intent i = new Intent(MenuControlActivity.this, DeviceMasterActivity.class);
            i.putExtra("Id_Device_Master", Id_Device_Master);
            startActivity(i);
        }
        if (item.getItemId() == R.id.updateVariablesRealTime) {
            loadVariables();
        }


        return super.onOptionsItemSelected(item);
    }

    private void cerraSesion() {
        String Estado_Sesion = "0";
        loginControllerInstance.abrirBaseDeDatos();
        loginControllerInstance.actualizarEstadoSesion(id_usuario_logued, Estado_Sesion);
        /////Se dela  siguiente formap para cerrar todos  los activitys
        startActivity(new Intent(getBaseContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        loginControllerInstance.cerrar();
    }
    /*--------------------------------------------------------------------------------------------------------*/


    ///Opciones del menu lateral
    /*---------------------------------------------------------------------------------------------------------*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sincronize) {
            Intent i = new Intent(MenuControlActivity.this, SincronizarInformacionActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_ic_account) {
            Intent i = new Intent(MenuControlActivity.this, AccountActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*---------------------------------------------------------------------------------------------------------*/

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

    //Permisos
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            new AlertDialog.Builder(MenuControlActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Permiso")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MenuControlActivity.this,
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
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // Now read the desired content to a textview.
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String Device_Id = equipmentIdentifier.getDeviceID(telephonyManager, MenuControlActivity.this);
        String Android_Id = equipmentIdentifier.getDeviceUniqueID(MenuControlActivity.this);
        /// String SIMSerialNumber=tm.getSimSerialNumber();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //return;
        }
        String Software_Version = tm.getDeviceSoftwareVersion();
        /*
        String SIMCountryISO=tm.getSimCountryIso();*/
        String Remote_Ip_Address=equipmentIdentifier.NetwordDetect();
        String Local_Ip_Address=equipmentIdentifier.getLocalIpAddress();
        String Android_Version=equipmentIdentifier.getAndroidVersion();
        String MacAddr=equipmentIdentifier.getMacAddr();
        String Device_Name=equipmentIdentifier.getDeviceName();
        ////Insertar Datos Equipo Maestro (Equipment master)
        deviceMasterController.abrirBaseDeDatos();
        deviceMasterController.insertarDeviceMaster(Remote_Ip_Address,Android_Id,Device_Id, Software_Version, Local_Ip_Address, Android_Version,  MacAddr, Device_Name);
        Device_Master device_Master=deviceMasterController.getFirstDeviceMaster();
        ///Obtener Id Master Device
        Id_Device_Master=device_Master.getId_Device_Master();
        Toast.makeText(MenuControlActivity.this, "Se registro correctamente",Toast.LENGTH_SHORT).show();
        deviceMasterController.cerrar();

    }
    //// onActivityResult
    /*-------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    ////CHECK CONECTION INTERNET
    /*---------------------------------------------------------------------------------------------*/

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
            color = Color.WHITE;
            ////verificateSincronizacionVariable();
        } else {
            message = "Sorry! Sin conexion a internet";
            color = Color.RED;
        }

        if(isConnected){
            showSnakBar(R.color.colorAccent,message);
        }else{
            showSnakBar(R.color.colorGray,message);
        }
    }


    //Mostrar Mensage Snackbar
    /*--------------------------------------------------------------------------------------------------------*/
    private void showSnakBar(int colorPrimary,String message) {
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this,colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_home_bar, 0, 0, 0);
        // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        textView.setTextColor(color);
        snackbar.show();
    }



    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    /*---------------------------------------------------------------------------------------------*/


    ///USB SERIAL
    /*---------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it

    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
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
    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    /*----------------------------------------------------------------------------------------------------------------------*/
    public static class MyHandler extends Handler {

        static int count=0;
        static String Hour="";
        static int MinuteOld=0;
        private static final SimpleDateFormat FORMATTER_24HOUR = new SimpleDateFormat("HH:mm");

        private WeakReference<MenuControlActivity> mActivity;
        public MyHandler(MenuControlActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            try{
                switch (msg.what) {
                    case UsbService.MESSAGE_FROM_SERIAL_PORT:
                        String data = (String) msg.obj; //Extract the string from the Message34.98   ->  4.98
                        mActivity.get().txtDataTrama.append(data);
                        final Date currentTime = Calendar.getInstance().getTime();
                        final String HourNow= FORMATTER_24HOUR.format(currentTime);
                        final int MinuteNow= currentTime.getMinutes();
                        int myTimerr= 500;
                        ///Mensage
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                try {
                                    ////Intent permite enviar a una nueva ventana
                                    String splitdata=  mActivity.get().txtDataTrama.getText().toString();
                                    String[] posicion;
                                    posicion = splitdata.split(":");
                                    //SEND ESTADO PROCESO
                                    Intent retIntent = new Intent("DATA_SERIAL_PROCESO");
                                    retIntent.putExtra("is_running_process", posicion[0]);
                                    mActivity.get().sendBroadcast(retIntent);
                                   /// count= count+1;



                                    mActivity.get().listaTipeVariablesRecorrida  = new ArrayList<Tipo_Variable>();
                                    int i=0;
                                    int savedata_each=2;
                                    int tamano=mActivity.get().listaTipeVariables.size();
                                    for(Tipo_Variable item:mActivity.get().listaTipeVariables){
                                        i++;
                                        int positionVariable = item.getPosicion_Variable();
                                        String data_medida=posicion[positionVariable];
                                        mActivity.get().sendBroadcastIntent(data_medida,item);
                                        item.setValor(data_medida);
                                        mActivity.get().listaTipeVariablesRecorrida.add(item);
                                        if(MinuteOld==0){
                                            if(MinuteNow>0  && MinuteOld==0){
                                                mActivity.get().saveDataItem(item);
                                            }
                                            if(i==tamano){
                                                MinuteOld=MinuteNow;
                                            }
                                        }else if((MinuteNow-MinuteOld)==savedata_each){
                                            mActivity.get().saveDataItem(item);
                                            if(i==tamano){
                                                MinuteOld=MinuteNow;
                                            }
                                        }
                                        else if((MinuteNow-MinuteOld)>savedata_each){
                                            mActivity.get().saveDataItem(item);
                                            if(i==tamano){
                                                MinuteOld=MinuteNow;
                                            }
                                        }

                                        else if((MinuteNow-MinuteOld)<0){
                                            mActivity.get().saveDataItem(item);
                                            if(i==tamano){
                                                MinuteOld=MinuteNow;
                                            }
                                        }
                                        else if(MinuteNow==0){
                                            MinuteOld=0;
                                        }
                                        /*if(count==60){
                                            mActivity.get().saveDataItem(item);
                                            if(i==tamano){
                                                count=0;
                                            }
                                        }
                                        else if(count==0){
                                            mActivity.get().saveDataItem(item);
                                            if(i==tamano){
                                                count++;
                                            }
                                        }

                                        else{
                                            if(i==tamano){
                                                count++;
                                            }
                                        }*/


                                        /*
                                         if(!Hour.equals(HourNow)){
                                                mActivity.get().saveDataItem(item);
                                                if(i==tamano){
                                                    Hour=HourNow;
                                                }
                                          }*/
                                    }

                                    /*
                                    Toast.makeText(mActivity.get(), "Leyendo... ", Toast.LENGTH_SHORT).show();
                                    if(count==5){
                                        Toast.makeText(mActivity.get(), "Guardando 2... ", Toast.LENGTH_SHORT).show();
                                        mActivity.get().saveData(mActivity.get().listaTipeVariablesRecorrida);
                                        count=0;
                                    }
                                    */
                                    mActivity.get().txtDataTrama.setText("");
                                }catch (Exception e){
                                }
                            }
                        }, myTimerr);


                       //Toast.makeText(mActivity.get(), "TareaPuertoMenu...", Toast.LENGTH_SHORT).show();
                        break;
                    case UsbService.CTS_CHANGE:
                        Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                        break;
                    case UsbService.DSR_CHANGE:
                        Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                        break;
                    case UsbService.CLOSE_CHANGE:
                        Toast.makeText(mActivity.get(), "CONEXION CERRADA",Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception Ex){
                Toast.makeText(mActivity.get(), "CONEXION CERRADA",Toast.LENGTH_LONG).show();
            }
        }

    }


    private void saveDataItem(Tipo_Variable data_variable) {


        if(isParceableDouble(data_variable.getValor())){
            double valor = Double.parseDouble(data_variable.getValor());
            if(valor>=0){
                //COUNT_SAVE_VARIABLE=COUNT_SAVE_VARIABLE+1;
                //if(COUNT_SAVE_VARIABLE==10){

                    COUNT_SAVE_VARIABLE=0;
                    DateFormat dateFormatFecha = new SimpleDateFormat("MM-dd-yyyy");
                    Date dateFecha = new Date();
                    String Fecha_Variable = dateFormatFecha.format(dateFecha);
                    //Obtener Hora
                    DateFormat dateFormatHora = new SimpleDateFormat("HH:mm:ss");
                    Date dateHora = new Date();
                    String Hora_Variable = dateFormatHora.format(dateHora);

                    ///Obtener Fecha Hora
                    DateFormat dateFormatFechaHora = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Date dateFH = new Date();
                    String FH_Variable = dateFormatFechaHora.format(dateFH);

                    boolean Estado_Sincronizacion_Variable= false;
                    String Id_Equipo_Remoto= "230035000547353138383138";
                    variableControllerInstance.abrirBaseDeDatos();
                    variableControllerInstance.createVariables(FH_Variable,data_variable.getValor(),Fecha_Variable,Hora_Variable,Estado_Sincronizacion_Variable,Id_Equipo_Remoto,data_variable.getId_Tipo_Variable());
                    variableControllerInstance.cerrar();

                    //Toast.makeText(this, "Guardando...", Toast.LENGTH_SHORT).show();

                //}


            }

        }


            ///String Data_Variable= String.valueOf(data);
            ////Toast.makeText(this, "Guardando..."+data_variable.getNombre_Tipo_Variable()+" Data:"+data_variable.getValor()+" ID: "+data_variable.getId_Tipo_Variable(), Toast.LENGTH_SHORT).show();

            //Obtener FechaHora


    }

    private void saveData(ArrayList<Tipo_Variable> listaTipeVariablesRecorrida) {
           for(Tipo_Variable data_variable:listaTipeVariablesRecorrida){
               ///String Data_Variable= String.valueOf(data);
               ////Toast.makeText(this, "Guardando..."+data_variable.getNombre_Tipo_Variable()+" Data:"+data_variable.getValor()+" ID: "+data_variable.getId_Tipo_Variable(), Toast.LENGTH_SHORT).show();

               //Obtener FechaHora
               DateFormat dateFormatFecha = new SimpleDateFormat("MM-dd-yyyy");
               Date dateFecha = new Date();
               String Fecha_Variable = dateFormatFecha.format(dateFecha);
               //Obtener Hora
               DateFormat dateFormatHora = new SimpleDateFormat("HH:mm:ss");
               Date dateHora = new Date();
               String Hora_Variable = dateFormatHora.format(dateHora);

               ///Obtener Fecha Hora
               DateFormat dateFormatFechaHora = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
               Date dateFH = new Date();
               String FH_Variable = dateFormatFechaHora.format(dateFH);

               boolean Estado_Sincronizacion_Variable= false;
               String Id_Equipo_Remoto= "230035000547353138383138";
               variableControllerInstance.abrirBaseDeDatos();
               variableControllerInstance.createVariables(FH_Variable,data_variable.getValor(),Fecha_Variable,Hora_Variable,Estado_Sincronizacion_Variable,Id_Equipo_Remoto,data_variable.getId_Tipo_Variable());
               variableControllerInstance.cerrar();
           }

    }


    public  void setWriteSerial(String writeSerial){
        Toast.makeText(this, "MENU: "+writeSerial, Toast.LENGTH_SHORT).show();
        usbService.write(writeSerial.getBytes());
    }

    public  Boolean getStateConectedUsb(){
        return usbConcted;
    }

    private void sendBroadcastIntent(String data_medida, Tipo_Variable item) {

       /// Toast.makeText(this, "Posicion: "+item.getPosicion_Variable(), Toast.LENGTH_SHORT).show();
        DateFormat dateFormatFechaHora = new SimpleDateFormat("dd-MM-yyyy KK:mm:ss a");
        Date dateFH = new Date();
        String date_time_data = dateFormatFechaHora.format(dateFH);
        Intent retIntent = new Intent("DATA_SERIAL");
        retIntent.putExtra("connected", usbConcted);
        retIntent.putExtra("data_medida", data_medida);
        retIntent.putExtra("date_time_data", date_time_data);
        retIntent.putExtra("tipo_variable", item);
        sendBroadcast(retIntent);
    }

    private static boolean isParceableDouble(String cadena){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }





    /*ASYNC DIALOG TASK*/
    /*----------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/

     /*//Para usarlo se debe llamar en OnCreate
    private void loadProgressBar() {
         //PROGRESS DIALOG
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sincronizando Informacion...");
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.setProgressStyle(pDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
    }*/

    //Dialog Progres
    /*--------------------------------------------------------------------------------------*/
    /*private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }*/
    ///SERVICIOS REST
    /*Sincronizacion de datos a la web*/
    /*--------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------------------*/
    ////Verificate sincronizacion Variables
    /*
    public void SincronizationVariablesTask(final String data, final String ttl,final String published_at,final String coreid, final String equipo_id,final String  name, final String hora_publicacion,final String time_published, final String fecha_publicacion) {
        try{
            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                    Const.URL_REGISTER_VARIABLES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                String status=obj.getString("status");
                                /// hideProgressDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //// hideProgressDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
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
                    params.put("data", data);
                    params.put("ttl", ttl);
                    params.put("published_at", published_at);
                    params.put("coreid", coreid);
                    params.put("equipo_id", equipo_id);
                    params.put("name", name);
                    params.put("hora_publicacion", hora_publicacion);
                    params.put("time_published", time_published);
                    params.put("fecha_publicacion",fecha_publicacion);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonObjRequest);
        } catch (Exception e) {
            Toast.makeText(MenuControlActivity.this,"Error: "+e,Toast.LENGTH_SHORT).show();
            ////hideProgressDialog();
        }
    }


    private void verificateSincronizacionVariable() {
        variableControllerInstance.abrirBaseDeDatos();
        getsetvariablesincronizacion = new ArrayList<Variable>();
        ArrayList<Variable> listado=variableControllerInstance.verificateSincronizacionVariables();
        if(listado.size()>0){
            for (Variable resultlist : listado) {
                Variable getsetvar= new Variable();
                getsetvar.setId_Variable(resultlist.getId_Variable());
                getsetvar.setData_Variable(resultlist.getData_Variable());
                getsetvar.setFecha_Variable(resultlist.getFecha_Variable());
                getsetvar.setHora_Variable(resultlist.getHora_Variable());
                getsetvar.setFecha_Hora_Variable(resultlist.getFecha_Hora_Variable());
                getsetvar.setEstado_Sincronizacion_Variable(resultlist.getEstado_Sincronizacion_Variable());
                getsetvar.setId_Tipo_Variable(resultlist.getId_Tipo_Variable());
                getsetvar.setId_Equipo_Remoto(resultlist.getId_Equipo_Remoto());
                getsetvariablesincronizacion.add(getsetvar);
            }
            DialogOptionsSincronizarVariable();
        }
        variableControllerInstance.cerrar();
    }
    */

    ///Dialog sincronizar datos
    /*------------------------------------------------------------------------------------------------------------*/
   /* public Dialog DialogOptionsSincronizarVariable() {
        final CharSequence[] items = {"Sincronizar Ahora", "Mas tarde"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sincronizacion de informacion pendiente?")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                new SincronizacionDialogTask().execute();
                                break;
                            case 1:
                                break;
                            default:
                                 break;
                        }
                    }
                });
        builder.setIcon(R.drawable.ledon);
        return builder.show();
    }*/
    /*
    public class SincronizacionDialogTask extends AsyncTask<Void, Void, Boolean> {

        public  SincronizacionDialogTask(){
        }

        public void onPreExecute() {
            showProgressDialog();
            // progress.show();
            //aquí se puede colocar código a ejecutarse previo
            //a la operación
        }
        public void onPostExecute(final Boolean success) {
            if (success) {
               hideProgressDialog();
                Toast.makeText(MenuControlActivity.this, "True", Toast.LENGTH_SHORT).show();
            } else {
                hideProgressDialog();
                Toast.makeText(MenuControlActivity.this, "False", Toast.LENGTH_SHORT).show();
            }

            ///workProgress();
            //aquí se puede colocar código que
            //se ejecutará tras finalizar
           // progress.dismiss();
        }

        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(500);
                //realizar la operación aquí
                size =0;
                size= getsetvariablesincronizacion.size();
                count=0;
                for (Variable resultlist : getsetvariablesincronizacion) {
                   /// DecimalFormat df = new DecimalFormat("0.000");
                    progresDouble= 100/(double)size;
                  ///  df.format(100/size);
                    if(progresDouble<1){
                        progresDoubleSum=progresDoubleSum+progresDouble;
                        progres=0;
                        if (progresDoubleSum>=1) {
                            progres=1;
                            progresDoubleSum=0;
                        }
                    }else{
                        progres= 100/size;
                    }

                    count= count+1;
                    int completarProgres= size-count;
                    if(completarProgres==0){
                        tareaLarga();
                        mHandlerDialogLoad.post(new Runnable() {
                            int completarP=size-progres;
                            public void run() {
                                pDialog.setMessage("Total Datos: " + String.valueOf(size) + " Sincronizados:" + String.valueOf(count));
                                pDialog.incrementProgressBy(completarP);
                            }
                        });
                    }else {
                       tareaLarga();
                        mHandlerDialogLoad.post(new Runnable() {
                            public void run() {
                                pDialog.setMessage("Total Datos: " + String.valueOf(size) + " Sincronizados:" + String.valueOf(count));
                                pDialog.incrementProgressBy(progres);
                            }
                        });
                    }
                    ////  pDialog.setProgress(progres);
                    // dbconeccion_variable_sincronization.actualizarStateSincronizarionVariables(resultlist.getId_Variable(),true);
                    //  int progres=100/size;
                    //// ProgresSincronizacion=String.valueOf("45%");
                    ///  SincronizationVariablesTask(resultlist.getData_Variable(),"60",resultlist.getFecha_Variable(),resultlist.getId_Equipo_Remoto(), resultlist.getId_Equipo_Remoto(),"HU",resultlist.getHora_Variable(),resultlist.getFecha_Hora_Variable(),resultlist.getFecha_Variable());
                }
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }
    }


    private void tareaLarga()
    {
        try {
            Thread.sleep(20);
        } catch(InterruptedException e) {}
    }

    */
}
