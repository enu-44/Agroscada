package com.a3jfernando.serialport.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.Adapters.AdapterListDevices;
import com.a3jfernando.serialport.Adapters.listactuadores.AdapterListActuadores;
import com.a3jfernando.serialport.Adapters.listactuadores.OnItemClickListenerActuadores;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.IdentifyEquipo.Equipment_Identifier;
import com.a3jfernando.serialport.controllers.ConfigurationController;
import com.a3jfernando.serialport.controllers.DeviceController;
import com.a3jfernando.serialport.controllers.Device_Master_Controller;
import com.a3jfernando.serialport.controllers.LoginController;
import com.a3jfernando.serialport.controllers.UserController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.models.Device;
import com.a3jfernando.serialport.pages.DevicesInfo;
import com.a3jfernando.serialport.pages.control.ControlDeviceActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlDeviceFragment extends Fragment implements  View.OnClickListener,OnItemClickListenerActuadores {
    private Unbinder unbinder;
    //UI Elements
    @BindView(R.id.add_Equipment)
    FloatingActionButton add_Equipment;

    /*@BindView(R.id.listViewEquipments)
    ListView listViewEquipments;*/

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txtResults)
    TextView txtResults;


    //Instances
    DeviceController deviceControllerInstance;
    ConfigurationController configurationControllerInstance;
    //List
    public ArrayList<Device> getsetdevice;
    //Adapter
    AdapterListActuadores adapter;

    ///Atributes Device
    public long Id_Device_Master, Id_Device;
    String  Description_Device,Name_Device,Image_Type_Device;
    boolean Is_Active;

    //CONFIGURACION
    //Variables para validadr si se debe registrar o actualizar la configuracion
    Boolean process_register;
    Boolean process_update;
    EditText editOff, editOnn;
    FloatingActionButton add, edit;
    //Configuration Serial
    String SerialOn, SerialOff;
    boolean verificate;
    //Variables
    long id_configuracion;
    //Dialog
    View vDialogControl;


    static  String FormatSerial="00:00:OFF:";



    public ControlDeviceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_actuadores,container,false);
        unbinder = ButterKnife.bind(this, view);
        loadInstances();

        //loadAdapters();
        loadOnClickListeners();
        initAdapter();
        initRecyclerView();
        listEquipos();

        ///long Id=((MenuControlActivity)this.getActivity()).Id_Device_Master;
       // Toast.makeText(getActivity(),"Hola:"+String.valueOf(Id),Toast.LENGTH_SHORT).show();
        return view;
    }


    private void initAdapter() {
        if (adapter == null) {
            adapter = new AdapterListActuadores(getActivity(), new ArrayList<Device>(), this);
            resultsList(0);
        }
    }

    private void initRecyclerView() {


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);
    }


    /*
    private void loadAdapters() {
        AdapterListDevices adapterListDevices = new AdapterListDevices(getActivity(),getsetdevice);
        listViewEquipments.setAdapter(adapterListDevices);
    }
*/
    private void loadOnClickListeners() {
        //listViewEquipments.setOnItemClickListener(this);
        add_Equipment.setOnClickListener(this);
    }

    //Methods
    /*----------------------------------------------------------------------------------------------*/
    ///Get alll Equipos Registrados
    /*-------------------------------------------------------------------------------------------------------*/
    private void listEquipos() {
        deviceControllerInstance.abrirBaseDeDatos();
        getsetdevice = new ArrayList<Device>();
        ///Array list para Lista de vehiculos
        getsetdevice = deviceControllerInstance.findAllEquipment();
        adapter.clear();
        adapter.setItems(getsetdevice);
        resultsList(getsetdevice.size());
        ////For del listado para Sincronizacion
        /*for (Device devi : listado) {
            Device getsetdevices= new Device();
            getsetdevices.setId_Device(devi.getId_Device());
            getsetdevices.setName_Device(devi.getName_Device());
            getsetdevices.setDescription_Device(devi.getDescription_Device());
            getsetdevices.setId_Device_Master(devi.getId_Device_Master());
            getsetdevices.setIs_Active(devi.getIs_Active());
            getsetdevices.setImage_Type_Device(devi.getImage_Type_Device());
            getsetdevice.add(getsetdevices);
        }*/
        deviceControllerInstance.cerrar();
    }


    public void resultsList(Integer size) {
        String results = String.format(getString(R.string.results_global_search),
                size);
        txtResults.setText(results);
    }


    ///Load Instances
    /*---------------------------------------------------------------------------------------------*/
    private void loadInstances() {
        this.configurationControllerInstance = ConfigurationController.getInstance(getActivity());
        this.deviceControllerInstance = DeviceController.getInstance(getActivity());
    }

    //Events
    /*-----------------------------------------------------------------------------------------------*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_Equipment:
                Intent i = new Intent(getActivity(), DevicesInfo.class);
                i.putExtra("Id_Device_Master",((MenuControlActivity)this.getActivity()).Id_Device_Master);
                i.putExtra("Accion","create");
                startActivityForResult(i,1);
                break;



            case R.id.btn_digital_write_off:



                if (verificate == true) {
                    ///String data = configuracion.getSerialWriteOn();
                  //  usbService.write(SerialOn.getBytes());
                } else {
                    Toast.makeText(getActivity(), "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_digital_write_on:

                if (verificate == true) {
                    //usbService.write(SerialOff.getBytes());
                } else {
                    Toast.makeText(getActivity(), "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_configuracion:
                RegisterConfiguration();
                break;
            case R.id.edit_configuracion:
                verificarRegisterConfiguration();

                Update();
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listEquipos();
        if (resultCode == Activity.RESULT_OK) {
        }
    }
/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Toast.makeText(getActivity(), String.valueOf(getsetdevice.get(position).getId_Device()),Toast.LENGTH_LONG).show();
            Id_Device= getsetdevice.get(position).getId_Device();
            Id_Device_Master= getsetdevice.get(position).getId_Device_Master();
            Description_Device= getsetdevice.get(position).getDescription_Device();
            Name_Device= getsetdevice.get(position).getName_Device();
            Is_Active= getsetdevice.get(position).getIs_Active();
            Image_Type_Device= getsetdevice.get(position).getImage_Type_Device();
            DialogOptionsEquipo();
        }catch (Exception   ex){
            Toast.makeText(getActivity(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }_*/

    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public Dialog DialogOptionsEquipo() {
        final CharSequence[] items = {"Editar","Control","Salir"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("MENU")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent i = new Intent(getActivity(),DevicesInfo.class );
                                i.putExtra("Id_Device",Id_Device);
                                i.putExtra("Id_Device_Master",Id_Device_Master);
                                i.putExtra("Name_Device",Name_Device);
                                i.putExtra("Description_Device",Description_Device);
                                i.putExtra("Is_Active",Is_Active);
                                i.putExtra("Image_Type_Device",Image_Type_Device);
                                i.putExtra("Accion","update");
                                startActivityForResult(i,2);
                                break;
                            case 1:
                                Intent z = new Intent(getActivity(),ControlDeviceActivity.class );
                                z.putExtra("Id_Device",Id_Device);
                                startActivity(z);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.setIcon(R.drawable.ledon);
        return builder.show();
    }



    /*EDIT CONTROL*/
    /*------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------*/
    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public void DetailTipovariable() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        vDialogControl=inflater.inflate(R.layout.dialog_edit_configuration_control, null);






        //Instances Tools
        add = (FloatingActionButton) vDialogControl.findViewById(R.id.register_configuracion);
        edit = (FloatingActionButton) vDialogControl.findViewById(R.id.edit_configuracion);

        add.setOnClickListener(this);
        edit.setOnClickListener(this);


        editOff= (EditText)vDialogControl.findViewById(R.id.WriteOff);
        editOnn= (EditText)vDialogControl.findViewById(R.id.WriteOn);



        verificarRegisterConfiguration();


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light);
            builder = new AlertDialog.Builder(getActivity());


        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Actualizar Datos")
                .setMessage("¿Configurar ON/OFF?")
                .setView(vDialogControl)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .setIcon(R.drawable.ledon);
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alert.show();

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


    //Methods Actions
    /*----------------------------------------------------------------------------------------------------------------------*/
    public void Edit(){
        editOff.setEnabled(false);
        editOnn.setEnabled(false);
        edit.setEnabled(true);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        add.setEnabled(false);
        add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
    }


    /*----------------------------------------------------------------------------------------------------------------------*/
    public void Update(){
        editOff.setEnabled(true);
        editOnn.setEnabled(true);
        edit.setEnabled(false);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        add.setEnabled(true);
        add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
    }

    public  void hideKeyBoard(){
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void RegisterConfiguration() {
        String message;
        int color;
        boolean cancel = false;
        View focusView = null;
        if(editOnn.getText().toString().isEmpty()){
            editOnn.setError(getString(R.string.error_field_required));
            focusView = editOnn;
            cancel = true;
        }
        else if(editOnn.getText().toString().length()==0 || editOnn.getText().toString().length()>1){
            editOnn.setError(getString(R.string.error_field_required_caracteres));
            focusView = editOnn;
            cancel = true;
        }
        else if(editOff.getText().toString().isEmpty()){
            editOff.setError(getString(R.string.error_field_required));
            focusView = editOff;
            cancel = true;
        }
        else if(editOff.getText().toString().length()==0 || editOff.getText().toString().length()>1){
            editOff.setError(getString(R.string.error_field_required_caracteres));
            focusView = editOff;
            cancel = true;
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
                Snackbar snackbar = Snackbar.make(vDialogControl, message, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
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
                Snackbar snackbar = Snackbar.make(vDialogControl, message, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                // snackbar.setActionTextColor(color);
                snackbar.show();
                Edit();
                configurationControllerInstance.cerrar();

            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    //IMPLEMENTS ON CLICK LISTENER
    @Override
    public void onItemClick(Device device) {

        Id_Device= device.getId_Device();
        Id_Device_Master= device.getId_Device_Master();
        Description_Device= device.getDescription_Device();
        Name_Device= device.getName_Device();
        Is_Active= device.getIs_Active();
        Image_Type_Device= device.getImage_Type_Device();


        Intent i = new Intent(getActivity(),DevicesInfo.class );
        i.putExtra("Id_Device",Id_Device);
        i.putExtra("Id_Device_Master",Id_Device_Master);
        i.putExtra("Name_Device",Name_Device);
        i.putExtra("Description_Device",Description_Device);
        i.putExtra("Is_Active",Is_Active);
        i.putExtra("Image_Type_Device",Image_Type_Device);
        i.putExtra("Accion","update");
        startActivityForResult(i,2);
    }

    @Override
    public void onItemSerialOn(Device device) {

        configurationControllerInstance.abrirBaseDeDatos();
        Configuration configuracion=configurationControllerInstance.getFirstConfiguracion(device.getId_Device());
        if(configuracion.getIdconfiguration()>0){

            if(((MenuControlActivity)getActivity()).getStateConectedUsb()){
                // Toast.makeText(getActivity(), ""+configuracion.getSerialWriteOn(), Toast.LENGTH_SHORT).show();
                ((MenuControlActivity)this.getActivity()).setWriteSerial(FormatSerial+configuracion.getSerialWriteOn());
            }else{
                Toast.makeText(getActivity(), "Verifique la conexion del puerto USB", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getActivity(), "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
        }
        configurationControllerInstance.cerrar();


    }

    @Override
    public void onItemSerialOff(Device device) {


        configurationControllerInstance.abrirBaseDeDatos();
        Configuration configuracion=configurationControllerInstance.getFirstConfiguracion(device.getId_Device());
        if(configuracion.getIdconfiguration()>0){

            if(((MenuControlActivity)getActivity()).getStateConectedUsb()){
                //Toast.makeText(getActivity(), ""+configuracion.getSerialWriteOff(), Toast.LENGTH_SHORT).show();
                ((MenuControlActivity)this.getActivity()).setWriteSerial(FormatSerial+configuracion.getSerialWriteOff());
            }else{
                Toast.makeText(getActivity(), "Verifique la conexion del puerto USB", Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(getActivity(), "Recuerde configurar el dispositivo", Toast.LENGTH_SHORT).show();
        }
        configurationControllerInstance.cerrar();
    }

    @Override
    public void onItemEditControl(Device device) {

        Id_Device= device.getId_Device();
        Id_Device_Master= device.getId_Device_Master();
        Description_Device= device.getDescription_Device();
        Name_Device= device.getName_Device();
        Is_Active= device.getIs_Active();
        Image_Type_Device= device.getImage_Type_Device();


        Intent i = new Intent(getActivity(),DevicesInfo.class );
        i.putExtra("Id_Device",Id_Device);
        i.putExtra("Id_Device_Master",Id_Device_Master);
        i.putExtra("Name_Device",Name_Device);
        i.putExtra("Description_Device",Description_Device);
        i.putExtra("Is_Active",Is_Active);
        i.putExtra("Image_Type_Device",Image_Type_Device);
        i.putExtra("Accion","update");
        startActivityForResult(i,2);
    }

    @Override
    public void onItemConfiguracionControl(Device device) {
        Id_Device= device.getId_Device();
        Id_Device_Master= device.getId_Device_Master();
        Description_Device= device.getDescription_Device();
        Name_Device= device.getName_Device();
        Is_Active= device.getIs_Active();
        Image_Type_Device= device.getImage_Type_Device();

        DetailTipovariable();
    }
}
