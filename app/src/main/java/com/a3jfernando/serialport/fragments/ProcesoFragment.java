package com.a3jfernando.serialport.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.Adapters.listactuadores.AdapterListActuadores;
import com.a3jfernando.serialport.Adapters.listactuadores.OnItemClickListenerActuadores;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.controllers.LoginController;
import com.a3jfernando.serialport.controllers.ProcesoController;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.controllers.VariableProcessController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Device;
import com.a3jfernando.serialport.models.Proceso;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Usuario;
import com.a3jfernando.serialport.models.Variable;
import com.a3jfernando.serialport.models.VariableProceso;
import com.a3jfernando.serialport.pages.LoginActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcesoFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.textClock)
    TextClock textClock;

    @BindView(R.id.textClock24Hours)
    TextClock textClock24Hours;


    @BindView(R.id.btn_proceso)
    Button btn_cronometro;

    @BindView(R.id.chronometro)
    Chronometer cronometro;

    @BindView(R.id.txtFechaInicio)
    EditText txtFechaInicio;

    @BindView(R.id.txtHoraInicio)
    EditText txtHoraInicio;




    @BindView(R.id.txtNombreProceso)
    EditText txtNombreProceso;


    EditText txtEmail, txtPasword;

    @BindView(R.id.btn_restar)
    Button btn_restar;

    @BindView(R.id.btnAddVariables)
    FloatingActionButton btnAddVariables;

    @BindView(R.id.btnDeleteVariables)
    FloatingActionButton btnDeleteVariables;

    @BindView(R.id.btnEditVariables)
    FloatingActionButton btnEditVariables;


    @BindView(R.id.txtHourFormat12)
    TextView txtHourFormat12;

    @BindView(R.id.txtVariablesConfiguradas)
    TextView txtVariablesConfiguradas;








    @BindView(R.id.spinnerVariablesProcess)
    MaterialBetterSpinner spinnerVariablesProcess;



    static  String TramaAutomatico="";
    String VARIABLES_CONFIGURADAS="";
    static  String PROCESS_ON="ON";
    static  String PROCESS_OFF="OFF";


    static String PROCESS_NOW="";
    static  String PROCESS_RESTART="RESTART";



    private static final SimpleDateFormat FORMATTER_24HOUR = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat FORMATTER_12HOUR = new SimpleDateFormat("hh:mm aa");
    private static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat("MM-dd-yyyy");

    private boolean isRunning=false;
    private long time=0;
    private long timeStopped=0;


    ProgressDialog dialogProgress;

    //Globals

    ProcesoController procesoInstance;
    TipoVariableController tipoVariableControllerInstance;
    VariableProcessController variableProcessController;

    List<Tipo_Variable> tipoVariables = new ArrayList<>();
    List<VariableProceso> procesoVariables = new ArrayList<>();
    Tipo_Variable Tipo_VariableGlobal = new Tipo_Variable();
    VariableProceso variableProcesoGlobal= new VariableProceso();
    Proceso ProcesoGlobal = new Proceso();

    //Indica si el cronómetro ha sido iniciado
    private boolean iniciado=false;


    MaterialBetterSpinner spinnerVariables;
    EditText txtFechaInicioVariableProcess;
    EditText txtValorDeseado;


    private boolean Is_Update_Variable_Process=false;
    LoginController loginControllerInstance;


    //Dialog
    View viewDialogAddVariable;
    View viewDialogLogin;
    public ProcesoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proceso, container, false);
        unbinder = ButterKnife.bind(this, view);


        textClock24Hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                /*if(isRunning==false){
                    txtHoraInicio.setText(textClock24Hours.getText().toString());

                    try {
                        SimpleDateFormat sdfStart = new SimpleDateFormat("H:mm");
                        Date dateObjStart = sdfStart.parse(txtHoraInicio.getText().toString());
                        txtHourFormat12.setText(new SimpleDateFormat("KK:mm a").format(dateObjStart));

                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }

                }*/
            }
        });



        btn_cronometro.setOnClickListener(this);
        btn_restar.setOnClickListener(this);
        btnAddVariables.setOnClickListener(this);
        btnDeleteVariables.setOnClickListener(this);
        btnEditVariables.setOnClickListener(this);


        cronometro.setText("00:00:00");
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;
            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if(iniciado){
                    time= SystemClock.elapsedRealtime() - chrono.getBase();
                    setTime(time);
                }else{
                    time = getTime();
                }

                int h   = (int)(time /3600000);
                int m = (int)(time  - h*3600000)/60000;
                int s= (int)(time  - h*3600000 - m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chrono.setText(hh+":"+mm+":"+ss);

                /*if (isRunning()){
                    chrono.setText(hh+":"+mm+":"+ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh+":"+mm+":"+ss);
                    }else{
                        isPair = true;
                        //chrono.setText("");
                    }
                }*/


                if(ProcesoGlobal.getId_Proceso()>0){
                    procesoInstance.abrirBaseDeDatos();
                    procesoInstance.actualizarTimeProcesso(ProcesoGlobal.getId_Proceso(),
                            cronometro.getText().toString(),
                            getTime());
                    ProcesoGlobal= procesoInstance.getFirstProceso();
                    procesoInstance.cerrar();
                }
            }
        });

        loadInstance();


        return view;
    }




    private void loadInstance() {
        this.tipoVariableControllerInstance = TipoVariableController.getInstance(getActivity());
        this.procesoInstance = ProcesoController.getInstance(getActivity());
        this.variableProcessController=VariableProcessController.getInstance(getActivity());
        this.loginControllerInstance=LoginController.getInstance(getActivity());

        variableProcessController.abrirBaseDeDatos();
        procesoVariables= variableProcessController.listVariablesProceso();
        variableProcessController.cerrar();



        Date currentTime = Calendar.getInstance().getTime();
        txtHoraInicio.setText(FORMATTER_24HOUR.format(currentTime));
        txtFechaInicio.setText(FORMATTER_DATE.format(currentTime));


        procesoInstance.abrirBaseDeDatos();
        ProcesoGlobal= procesoInstance.getFirstProceso();
        if(ProcesoGlobal.getId_Proceso()>0){
            setTime(ProcesoGlobal.getTiempo_Transcurrido_Long());
            txtNombreProceso.setText(ProcesoGlobal.getName_Proceso());
            txtFechaInicio.setText(ProcesoGlobal.getFecha_Proceso());
            txtHoraInicio.setText(ProcesoGlobal.getHora_Inicio_Proceso());

            int h   = (int)(time /3600000);
            int m = (int)(time  - h*3600000)/60000;
            int s= (int)(time  - h*3600000 - m*60000)/1000 ;
            String hh = h < 10 ? "0"+h: h+"";
            String mm = m < 10 ? "0"+m: m+"";
            String ss = s < 10 ? "0"+s: s+"";
            cronometro.setText(hh+":"+mm+":"+ss);

           // cronometro.setBase(SystemClock.elapsedRealtime()-ProcesoGlobal.getTiempo_Transcurrido_Long());

        }else{
            procesoInstance.abrirBaseDeDatos();
            procesoInstance.insertarProcesso(
                    "Nombre Proceso",
                    "0",
                    getTime(),
                    txtFechaInicio.getText().toString(),
                    txtHoraInicio.getText().toString(),
                    "0",
                    true
            );
            ProcesoGlobal= procesoInstance.getFirstProceso();
            procesoInstance.cerrar();
        }


        try {
                SimpleDateFormat sdfStart = new SimpleDateFormat("H:mm");
                Date dateObjStart = sdfStart.parse(ProcesoGlobal.getHora_Inicio_Proceso());
                txtHourFormat12.setText(new SimpleDateFormat("KK:mm a").format(dateObjStart));
        } catch (final ParseException e) {
                e.printStackTrace();
        }




        procesoInstance.cerrar();
        loadVariablesProcess();
        Trama();
    }

    public void loadVariablesProcess(){
        spinnerVariablesProcess.setHint("Variables Configuradas");
        spinnerVariablesProcess.setText("");
        spinnerVariablesProcess.setAdapter(null);
        ArrayAdapter<VariableProceso>empresaArrayAdapter =
                new ArrayAdapter<VariableProceso>(getActivity(), android.R.layout.simple_spinner_dropdown_item, procesoVariables);
        spinnerVariablesProcess.setAdapter(empresaArrayAdapter);
        spinnerVariablesProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                variableProcesoGlobal=procesoVariables.get(position);
            }
        });


    }


    public void loadTipoVariables(){

        tipoVariableControllerInstance.abrirBaseDeDatos();
        tipoVariables.clear();
        tipoVariables= tipoVariableControllerInstance.findVariableActives();
        tipoVariableControllerInstance.cerrar();



        spinnerVariables.setAdapter(null);
        ArrayAdapter<Tipo_Variable>empresaArrayAdapter =
                new ArrayAdapter<Tipo_Variable>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tipoVariables);
        spinnerVariables.setAdapter(empresaArrayAdapter);
        spinnerVariables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Tipo_VariableGlobal=tipoVariables.get(position);
            }
        });





    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void setTimeStopped(long timeStopped) {
        this.timeStopped += timeStopped;
    }


    ///Escucha Los valores enviados por Serial Port desde Menu Activity
    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String tipo_variable = intent.getExtras().getParcelable("tipo_variable");
            //String data= intent.getStringExtra("data_medida");

            if(intent.getExtras().containsKey("is_running_process")){

                //Toast.makeText(getActivity(),"PROCESS",Toast.LENGTH_SHORT).show();

                String state_process= intent.getStringExtra("is_running_process");
                if(state_process.toLowerCase().contains(PROCESS_OFF.toLowerCase())){

                    if(ProcesoGlobal.isEnded_Process()==false){
                        procesoInstance.abrirBaseDeDatos();
                        procesoInstance.actualizarStateProcesso(
                                ProcesoGlobal.getId_Proceso(),
                                true
                        );
                        ProcesoGlobal= procesoInstance.getFirstProceso();
                        procesoInstance.cerrar();
                       // StopProcess();
                    }

                    if(dialogProgress!=null){
                        if(PROCESS_NOW.toLowerCase().contains(PROCESS_OFF.toLowerCase())){
                            StopProcess();
                            dialogProgress.dismiss();
                            PROCESS_NOW="";
                        }
                    }


                    if(PROCESS_NOW.toLowerCase().contains(PROCESS_RESTART.toLowerCase())){
                        if(dialogProgress!=null){
                            dialogProgress.dismiss();
                            PROCESS_NOW="";
                        }
                    }

                }else if(state_process.toLowerCase().contains(PROCESS_ON.toLowerCase())){
                    if(dialogProgress!=null){
                        if(PROCESS_NOW.toLowerCase().contains(PROCESS_ON.toLowerCase())){
                            StartProcess();
                            PROCESS_NOW="";
                            dialogProgress.dismiss();
                        }
                    }
                    if(isRunning==false){
                        RestoreProcess();
                    }
                }

            }
           // display.append(tipo_variable.getNombre_Tipo_Variable()+": "+data+"\n");
        }
    };


    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mNotificationReceiver, new IntentFilter("DATA_SERIAL"));
        getActivity().registerReceiver(mNotificationReceiver, new IntentFilter("DATA_SERIAL_PROCESO"));

    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNotificationReceiver);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //getActivity().unregisterReceiver(mNotificationReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ////getActivity().unregisterReceiver(mNotificationReceiver);
    }

    private void Trama(){
        TramaAutomatico="";
        int countvariable=0;
        VARIABLES_CONFIGURADAS="";
        for (VariableProceso item:procesoVariables){

            countvariable++;
            if(countvariable==procesoVariables.size()){
                TramaAutomatico+=String.valueOf(item.getValor_Deseado());
                VARIABLES_CONFIGURADAS+=item.getNombre_Variable();
                txtVariablesConfiguradas.setText(VARIABLES_CONFIGURADAS);

                if(procesoVariables.size()==1){
                    TramaAutomatico+=":00";
                }

            }else{
                TramaAutomatico+=String.valueOf(item.getValor_Deseado())+":";
                VARIABLES_CONFIGURADAS+=item.getNombre_Variable()+",";
                //spinnerVariablesProcess.setText(VARIABLES_CONFIGURADAS);
            }
        }


    }

    private  void RestarProcess(){
        cronometro.stop();
        iniciado=false;
        btn_cronometro.setText("Iniciar");
        cronometro.setText("00:00:00");

        procesoInstance.abrirBaseDeDatos();
        procesoInstance.actualizarProcesso(
                ProcesoGlobal.getId_Proceso(),
                ProcesoGlobal.getName_Proceso(),
                "",
                0,
                txtFechaInicio.getText().toString(),
                txtHoraInicio.getText().toString(),
                "0",
                false
        );
        ProcesoGlobal= procesoInstance.getFirstProceso();
        procesoInstance.cerrar();
        time=0;
        isRunning=false;

        Date currentTime = Calendar.getInstance().getTime();
        txtHoraInicio.setText(FORMATTER_24HOUR.format(currentTime));
        txtFechaInicio.setText(FORMATTER_DATE.format(currentTime));

        try {
            SimpleDateFormat sdfStart = new SimpleDateFormat("H:mm");
            Date dateObjStart = sdfStart.parse(txtHoraInicio.getText().toString());
            txtHourFormat12.setText(new SimpleDateFormat("KK:mm a").format(dateObjStart));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
    }

    private  void RestoreProcess(){
        cronometro.setBase(SystemClock.elapsedRealtime()-getTime());
        iniciado=true;
        isRunning=true;
        int h   = (int)(time /3600000);
        int m = (int)(time  - h*3600000)/60000;
        int s= (int)(time  - h*3600000 - m*60000)/1000 ;
        String hh = h < 10 ? "0"+h: h+"";
        String mm = m < 10 ? "0"+m: m+"";
        String ss = s < 10 ? "0"+s: s+"";
        cronometro.setText(hh+":"+mm+":"+ss);
        cronometro.start();
        btn_cronometro.setText("Detener");
    }

    private  void StartProcess(){
        cronometro.setBase(SystemClock.elapsedRealtime()-getTime());
        cronometro.start();
        iniciado=true;
        isRunning=true;
        btn_cronometro.setText("Detener");

        procesoInstance.abrirBaseDeDatos();
        procesoInstance.actualizarProcesso(
                ProcesoGlobal.getId_Proceso(),
                txtNombreProceso.getText().toString(),
                cronometro.getText().toString(),
                getTime(),
                txtFechaInicio.getText().toString(),
                txtHoraInicio.getText().toString(),
                "0",
                false
        );
        ProcesoGlobal= procesoInstance.getFirstProceso();
        procesoInstance.cerrar();
    }

    private  void   StopProcess(){
        cronometro.stop();
        iniciado=false;
        btn_cronometro.setText("Iniciar");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_proceso:
                if(((MenuControlActivity)getActivity()).getStateConectedUsb()==true){
                    if(isValidateCampos()==true){

                        String estado_proceso="";
                        if(!iniciado){
                            estado_proceso= "¿Esta seguro que desea iniciar el proceso de "+txtNombreProceso.getText().toString()+" ?";
                        }else{
                            estado_proceso= "¿Esta seguro que desea detener el proceso de "+txtNombreProceso.getText().toString()+" ?";
                        }

                        AlertDialog.Builder builderStart = new AlertDialog.Builder(getActivity());
                        builderStart.setTitle("Confirmacion");
                        builderStart.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialogos", "Confirmacion.");
                            }
                        });
                        builderStart.setMessage(estado_proceso);
                        builderStart.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(!iniciado){
                                    dialogProgress = ProgressDialog.show(getActivity(), "",
                                            "Iniciando Proceso. Porfavor Espere...", true);

                                    ((MenuControlActivity)getActivity()).setWriteSerial(TramaAutomatico+":"+PROCESS_ON+":a");
                                    PROCESS_NOW=PROCESS_ON;
                                }else{
                                    dialogProgress = ProgressDialog.show(getActivity(), "",
                                            "Deteniendo Proceso. Porfavor Espere...", true);

                                    ((MenuControlActivity)getActivity()).setWriteSerial(TramaAutomatico+":"+PROCESS_OFF+":z");
                                    PROCESS_NOW=PROCESS_OFF;
                                }


                                int myTimerr= 20000;
                                ///Mensage
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run(){
                                        try {
                                            dialogProgress.dismiss();
                                        }catch (Exception e){
                                        }
                                    }
                                }, myTimerr);
                            }
                        });
                        builderStart.setIcon(R.drawable.ledon);
                        builderStart.show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Verifique la conexion del puerto USB", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_restar:
                if(((MenuControlActivity)getActivity()).getStateConectedUsb()==true){
                    AlertDialog.Builder builderRestar = new AlertDialog.Builder(getActivity());
                    builderRestar.setTitle("Confirmacion");
                    builderRestar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Dialogos", "Confirmacion.");
                        }
                    });
                    builderRestar.setMessage("Seguro que desea restablecer el proceso");
                    builderRestar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialogProgress = ProgressDialog.show(getActivity(), "",
                                    "Restableciendo. Porfavor Espere...", true);

                            ((MenuControlActivity)getActivity()).setWriteSerial(TramaAutomatico+":"+PROCESS_OFF+":z");
                            PROCESS_NOW=PROCESS_RESTART;
                            RestarProcess();

                            int myTimerr= 20000;
                            ///Mensage
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    try {
                                        dialogProgress.dismiss();
                                        RestarProcess();
                                    }catch (Exception e){
                                    }
                                }
                            }, myTimerr);
                        }
                    });
                    builderRestar.setIcon(R.drawable.ledon);
                    builderRestar.show();

                }else{
                    Toast.makeText(getActivity(), "Verifique la conexion del puerto USB", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnAddVariables:

                //VALIDACION
                Is_Update_Variable_Process=false;
                boolean cancel = false;
                View focusView = null;
                if (txtNombreProceso.getText().toString().isEmpty()) {
                    txtNombreProceso.setError(getString(R.string.error_field_required));
                    focusView = txtNombreProceso;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    showCredentials();
                }




                break;
            case R.id.btnEditVariables:

                //VALIDACION
                Is_Update_Variable_Process=true;
                boolean cancelEdit = false;
                View focusViewEdit = null;
                if (txtNombreProceso.getText().toString().isEmpty()) {
                    txtNombreProceso.setError(getString(R.string.error_field_required));
                    focusViewEdit = txtNombreProceso;
                    cancelEdit = true;
                }else if (spinnerVariablesProcess.getText().toString().isEmpty()) {
                    spinnerVariablesProcess.setError(getString(R.string.error_field_required_variable_update));
                    focusViewEdit = spinnerVariablesProcess;
                    cancelEdit = true;

                }

                if (cancelEdit) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusViewEdit.requestFocus();
                } else {
                    showAddVariables();
                }


                break;



            case R.id.btnDeleteVariables:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmacion");
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialogos", "Confirmacion.");
                    }
                });
                builder.setMessage("¿Limpiar lista de variables?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        variableProcessController.abrirBaseDeDatos();
                        variableProcessController.deleteVariableProcess();
                        variableProcessController.cerrar();
                        procesoVariables.clear();
                        loadVariablesProcess();
                        txtVariablesConfiguradas.setText("");
                    }
                });
                builder.setIcon(R.drawable.ledon);
                builder.show();
                break;
        }


    }



    public Boolean isValidateCampos(){
        //VALIDACION
        boolean cancel = false;
        View focusView = null;
        if (procesoVariables.size()<=0) {
            spinnerVariablesProcess.setError(getString(R.string.error_field_variables_required));
            focusView = spinnerVariablesProcess;
            cancel = true;
        } else if (txtNombreProceso.getText().toString().isEmpty()) {
            txtNombreProceso.setError(getString(R.string.error_field_required));
            focusView = txtNombreProceso;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            return true;
        }
        return false;
    }


    public Boolean isValidateCamposAddVariable(){
        //VALIDACION
        boolean cancel = false;
        View focusView = null;
        if (spinnerVariables.getText().toString().isEmpty()) {
            spinnerVariables.setError(getString(R.string.error_field_required));
            focusView = spinnerVariables;
            cancel = true;
        } else if (txtNombreProceso.getText().toString().isEmpty()) {
            txtNombreProceso.setError(getString(R.string.error_field_required));
            focusView = txtNombreProceso;
            cancel = true;
        }
        else if (Long.parseLong(txtValorDeseado.getText().toString())==0) {
            txtValorDeseado.setError(getString(R.string.error_field_required_mayor_cero));
            focusView = txtValorDeseado;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            return true;
        }
        return false;
    }


    public Boolean isValidateLogin(){
        //VALIDACION
        //VALIDACION
        boolean cancel = false;
        View focusView = null;
        if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        } else if (txtPasword.getText().toString().isEmpty()) {
            txtPasword.setError(getString(R.string.error_field_required));
            focusView = txtPasword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            return true;
        }
        return false;
    }




    /*EDIT CONTROL*/
    /*------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------*/
    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public void showAddVariables() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        viewDialogAddVariable=inflater.inflate(R.layout.dialog_add_variable_process, null);



        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light);
            builder = new AlertDialog.Builder(getActivity());
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }


        //Instances Tools
        spinnerVariables= (MaterialBetterSpinner) viewDialogAddVariable.findViewById(R.id.spinnerVariables);
        txtFechaInicioVariableProcess= (EditText)viewDialogAddVariable.findViewById(R.id.txtFechaInicioVariableProcess);
        txtValorDeseado= (EditText)viewDialogAddVariable.findViewById(R.id.txtValorDeseado);

        loadTipoVariables();
        String message="";
        if(Is_Update_Variable_Process==true){
            message="Actualizar Variable";
            spinnerVariables.setDropDownHeight(0);
            txtValorDeseado.setText(String.valueOf(variableProcesoGlobal.getValor_Deseado()));
            spinnerVariables.setText(variableProcesoGlobal.getNombre_Variable());
            Tipo_VariableGlobal.setId_Tipo_Variable(variableProcesoGlobal.getIdTipo_Variable());
            Tipo_VariableGlobal.setNombre_Tipo_Variable(variableProcesoGlobal.getNombre_Variable());
            Tipo_VariableGlobal.setPosicion_Variable((int)variableProcesoGlobal.getPosicion_Variable_Proceso());
        }else{

            message="Agregar Variable al proceso";
            txtValorDeseado.setText(String.valueOf(0));
        }

        Date currentTime = Calendar.getInstance().getTime();
        txtFechaInicioVariableProcess.setText(FORMATTER_DATE.format(currentTime));
        //txtFechaInicio.setText(FORMATTER_DATE.format(currentTime));


        builder.setTitle("Variables-Proceso")
                .setMessage(message)
                .setView(viewDialogAddVariable)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setIcon(R.drawable.ledon);

        final AlertDialog alert = builder.create();


        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValidateCamposAddVariable()==true){

                            if(!Is_Update_Variable_Process){
                                variableProcessController.abrirBaseDeDatos();
                                variableProcessController.insertarVariableProcesso(
                                        Tipo_VariableGlobal.getNombre_Tipo_Variable(),
                                        txtNombreProceso.getText().toString(),
                                        Long.parseLong(txtValorDeseado.getText().toString()),
                                        Tipo_VariableGlobal.getPosicion_Variable(),
                                        txtFechaInicioVariableProcess.getText().toString(),
                                        ProcesoGlobal.getId_Proceso(),
                                        Tipo_VariableGlobal.getId_Tipo_Variable()
                                );
                            }else{
                                variableProcessController.abrirBaseDeDatos();
                                variableProcessController.actulizarVariableProcesso(
                                        variableProcesoGlobal.getIdProceso_Variable(),
                                        Tipo_VariableGlobal.getNombre_Tipo_Variable(),
                                        txtNombreProceso.getText().toString(),
                                        Long.parseLong(txtValorDeseado.getText().toString()),
                                        Tipo_VariableGlobal.getPosicion_Variable(),
                                        txtFechaInicioVariableProcess.getText().toString(),
                                        ProcesoGlobal.getId_Proceso(),
                                        Tipo_VariableGlobal.getId_Tipo_Variable()
                                );
                            }



                            procesoVariables.clear();
                            procesoVariables= variableProcessController.listVariablesProceso();
                            variableProcessController.cerrar();

                            Trama();


                            Toast.makeText(getActivity(),""+TramaAutomatico,Toast.LENGTH_LONG).show();


                            loadVariablesProcess();

                            dialog.dismiss();
                        }
                    }
                });
            }
        });


        builder.setCancelable(false);

        alert.setCancelable(false);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alert.show();
    }


    //LOGIN
    public void showCredentials() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        viewDialogLogin=inflater.inflate(R.layout.dialog_login, null);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light);
            builder = new AlertDialog.Builder(getActivity());
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }


        //Instances Tools
         txtEmail= (EditText) viewDialogLogin.findViewById(R.id.txtAccessEmail);
         txtPasword= (EditText)viewDialogLogin.findViewById(R.id.txtAccessPassword);

        String message="Ingesar Credenciales";



        builder.setTitle("Credenciales")
                .setMessage(message)
                .setView(viewDialogLogin)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setIcon(R.drawable.ledon);

        final AlertDialog alert = builder.create();


        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValidateLogin()==true){
                            Usuario user=loginControllerInstance.loginUser(txtEmail.getText().toString(),txtPasword.getText().toString());
                            if(user.getIdusuario()>0){
                                loginControllerInstance.cerrar();
                                dialog.dismiss();
                                showAddVariables();
                            }
                            else{
                                Toast.makeText(getActivity(),"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                                loginControllerInstance.cerrar();
                            }
                        }
                    }
                });
            }
        });


        builder.setCancelable(false);

        alert.setCancelable(false);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alert.show();
    }



}
