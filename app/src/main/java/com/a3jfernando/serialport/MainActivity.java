package com.a3jfernando.serialport;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import com.a3jfernando.serialport.Services.UsbService;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Variable;
import com.a3jfernando.serialport.pages.LoginActivity;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    ///UI Elements
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textViewConsole)
    TextView display;

    @BindView(R.id.txtDigitalWrite)
    EditText txtDigitalWrite;

    @BindView(R.id.btnSendWrite)
    Button btnDigitalWrite;

    @BindView(R.id.txtDataTrama)
    EditText txtDataTrama;


    //Atributes
    private long id_usuario_logued;
    private boolean usbConcted;
    private boolean usbPermision;
    private UsbService usbService;
    private MyHandler mHandler;
    private  Handler mHandlerTask;
    boolean IsRecording;

    //Listas
    ArrayList<Tipo_Variable> listaTipeVariables= new ArrayList<Tipo_Variable>();

    ///Services
    VariableController variableInstance;
    TipoVariableController tipoVariableControllerInstance;
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
                        usbConcted=true;
                        Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                        break;
                    case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                        Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                        usbPermision=false;
                        break;
                    case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                        usbConcted=false;
                        Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                        break;
                    case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                        usbConcted=false;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar();
        loadInstance();

        loadVariables();
        try{
            //// id_usuario_logued = getIntent().getExtras().getLong("id_usuario_logued");
            mHandler = new MyHandler(this);
            mHandlerTask= new Handler();
            btnDigitalWrite.setOnClickListener(this);
            IsRecording=true;
           ///tareaVariablesTemp();
           ///mHandlerTask.post(tareaVariableHumedad);
           ///txtDigitalWrite.setInputType(InputType.TYPE_NULL);
        }catch (Exception Ex){
            Toast.makeText(this,""+Ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVariables() {
        tipoVariableControllerInstance.abrirBaseDeDatos();
        listaTipeVariables=tipoVariableControllerInstance.findVariableActives();
        Toast.makeText(MainActivity.this, "Variables Cargadas "+String.valueOf(listaTipeVariables.size()),Toast.LENGTH_SHORT).show();
        tipoVariableControllerInstance.cerrar();
    }

    private void loadInstance() {
        this.variableInstance= VariableController.getInstance(this);
        this.tipoVariableControllerInstance=TipoVariableController.getInstance(this);
    }

    //Toolbar
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private Runnable tareaVariableHumedad= new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "Ejecutando Tarea",Toast.LENGTH_LONG).show();
            Random r = new Random();
            int data = r.nextInt(80 - 65) + 65;
            String Data_Variable= String.valueOf(data);
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

            long Id_Tipo_Variable=1;
            variableInstance.abrirBaseDeDatos();
            variableInstance.createVariables(FH_Variable,Data_Variable,Fecha_Variable,Hora_Variable,Estado_Sincronizacion_Variable,Id_Equipo_Remoto,Id_Tipo_Variable);
            mHandlerTask.postDelayed(this,8000);
            variableInstance.cerrar();
        }
    };

    private void tareaVariablesTemp() {
        mHandlerTask.postDelayed(new Runnable() {
            public void run() {

                if(IsRecording){
                    Toast.makeText(MainActivity.this, "Ejecutando Tarea",Toast.LENGTH_LONG).show();
                    Random r = new Random();
                    int data = r.nextInt(80 - 65) + 65;
                    String Data_Variable= String.valueOf(data);
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

                    long Id_Tipo_Variable=1;
                    variableInstance.abrirBaseDeDatos();
                    variableInstance.createVariables(FH_Variable,Data_Variable,Fecha_Variable,Hora_Variable,Estado_Sincronizacion_Variable,Id_Equipo_Remoto,Id_Tipo_Variable);
                    mHandlerTask.postDelayed(this,1000);
                    variableInstance.cerrar();
                }else{

                }
            }
        }, 1000); //Every 120000 ms (2 minutes)
    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onResume() {
            super.onResume();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendWrite:
                if (!txtDigitalWrite.getText().toString().equals("")) {
                    String data = txtDigitalWrite.getText().toString();
                    if (usbService != null) { // if UsbService was correctly binded, Send data
                        usbService.write(data.getBytes());
                    }
                }else
                    Toast.makeText(MainActivity.this,"Debes digitar algo",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    /*----------------------------------------------------------------------------------------------------------------------*/
    public static  class MyHandler extends Handler {

        private  WeakReference<MainActivity> mActivity;
        public MyHandler(MainActivity activity) {
                mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            try{
                switch (msg.what) {
                    case UsbService.MESSAGE_FROM_SERIAL_PORT:
                        String data = (String) msg.obj; //Extract the string from the Message
                        mActivity.get().txtDataTrama.append(data);
                        int myTimerr= 1000;
                        ///Mensage
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                try {
                                    ////Intent permite enviar a una nueva ventana
                                    String splitdata=  mActivity.get().txtDataTrama.getText().toString();
                                    String[] posicion;
                                    posicion = splitdata.split(":");
                                    for(Tipo_Variable item:mActivity.get().listaTipeVariables){
                                        int positionVariable = item.getPosicion_Variable();
                                        String variable=posicion[positionVariable];
                                        mActivity.get().display.append(item.getNombre_Tipo_Variable()+": "+variable+"\n");
                                    }
                                    mActivity.get().txtDataTrama.setText("");
                                }catch (Exception e){
                                }
                            }
                        }, myTimerr);


                       /// Toast.makeText(mActivity.get(), "TareaPuerto...", Toast.LENGTH_SHORT).show();

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

    //Cambiar estado Encendido/Apagado Bombillo Aplican
    /*----------------------------------------------------------------------------------------------------------------------*/
    /*
    public static void changeestado(String estado){
        /////Toast.makeText(this, "DATO: "+estado_equipo,Toast.LENGTH_SHORT).show();
           if(estado.equals("ON")){
               SwitLet.setChecked(true);
              // Toast.makeText(this, "Encendido",Toast.LENGTH_SHORT).show();
           }
           else if(estado.equals("OFF")){
               SwitLet.setChecked(false);
              /// Toast.makeText(this, "Apagado",Toast.LENGTH_SHORT).show();
           }else{
               SwitLet.setChecked(false);
           }
    }*/

    ///Enviar Orden al equipo Reset
    /*----------------------------------------------------------------------------------------------------------------------*/
   // public  void resetequipo(View v){
        /*
        try {
            if(!VerificacionDispositivo()){
                Configuration configuracion=dbconeccion_configuration.getFirstConfiguracion();
                if(configuracion.getIdconfiguration()>0){
                    String data = configuracion.getSerialWritereset();
                    Toast.makeText(MainActivity.this, "Reseteando " +data,Toast.LENGTH_SHORT).show();
                    usbService.write(data.getBytes());
                }else{
                    Toast.makeText(MainActivity.this, "Recuerde configurar su sistema",Toast.LENGTH_SHORT).show();
                    SwitLet.setChecked(false);
                }
            }else{
                return;
            }
        }catch (Exception e){
        }
*/
 ///   }
    //Validaion Coneccion de equipos
   /* public boolean VerificacionDispositivo()
    {
        boolean cancel = false;
        View focusView = null;
        if(!usbConcted){
            Toast.makeText(MainActivity.this, "Conecte un dispositivo",Toast.LENGTH_SHORT).show();
            SwitLet.setChecked(false);
            cancel=true;
        }
        else if(!usbPermision){
            Toast.makeText(MainActivity.this, "Recuerde seleccionar el permiso",Toast.LENGTH_SHORT).show();
            SwitLet.setChecked(false);
            cancel=true;
        }
        return cancel;
    }*/


    @Override
    public void onBackPressed() {
       ///IsRecording=false;
        super.onBackPressed();
        ///finishAffinity();
        /// finishActivityFromChild(ChartLineRealTime.this,1);
        mHandlerTask.removeCallbacksAndMessages(null);
        endHandlerP();
        mHandlerTask.removeCallbacks(tareaVariableHumedad);
      ///  Toast.makeText(MainActivity.this, "Menu",Toast.LENGTH_SHORT).show();
        /////Se dela  siguiente formap para cerrar todos  los activitys
       /// startActivity(new Intent(getBaseContext(), MenuControlActivity.class)
                     ///   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        ////finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerTask.removeCallbacksAndMessages(null);
        endHandlerP();
        mHandlerTask.removeCallbacks(tareaVariableHumedad);
    }

    private  void endHandlerP() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.removeMessages(3);
    }
}
