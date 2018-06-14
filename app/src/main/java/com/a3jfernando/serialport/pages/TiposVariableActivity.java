package com.a3jfernando.serialport.pages;

import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.a3jfernando.serialport.Adapters.AdapterListTipoVariables;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.controllers.ConfigurationController;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.pages.control.ControlDeviceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TiposVariableActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ////Registrar Tipo Variable
    private String Nombre_Tipo_Variable[]=new String[]{"Humedad","Oxigeno","Temperatura Suelo" ,"","","","","","","","","","","","","","","","",""};
    private int Posicion_Variable[]=new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    private String Descripcion_Variable[]=new String[]{"","","" ,"","","","","","","","","","","","","","","","",""};
    private boolean Estado_Variable[]=new boolean[]{true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
    private String Unidad_Medida[]=new String[]{"%","O˝","C˚" ,"","","","","","","","","","","","","","","","",""};


    ///Variables Globales Tipo variables
    private String Nombre_Tipo_Variable_,Descripcion_Variable_,Unidad_Medida_;
    private long Posicion_Variable_,Id_Tipo_Variable;
    private boolean Estado_Variable_;


    ///Conexion a la base de datos
    TipoVariableController dbconeccion_tipo_variable;
    FloatingActionButton btn_save_changes_variable;
    ListView listViewTipoVariables;
    public ArrayList<Tipo_Variable> getsettipovariable;
    ///private lo verificacion_notificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos_variable);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            listViewTipoVariables = (ListView) findViewById(R.id.listViewtipoVariables);

            ///Se abren las conexiones a la bd
            dbconeccion_tipo_variable = new TipoVariableController(this);
            dbconeccion_tipo_variable.abrirBaseDeDatos();

            validarRegisterTipoVariable();
            listTipoVariables();
            AdapterListTipoVariables adapternotificaciones= new AdapterListTipoVariables(this,getsettipovariable);


            listViewTipoVariables.setAdapter(adapternotificaciones);
            listViewTipoVariables.setOnItemClickListener(this);

        }catch (Exception Ex){

            Toast.makeText(this,Ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }


    }

    ///Obtener detalle de los datos ListView
    /*------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ///Llenar variables
        Nombre_Tipo_Variable_= getsettipovariable.get(position).getNombre_Tipo_Variable();
        Descripcion_Variable_= getsettipovariable.get(position).getDescripcion_Variable();
        Unidad_Medida_= getsettipovariable.get(position).getUnidad_Medida();
        Posicion_Variable_= getsettipovariable.get(position).getPosicion_Variable();
        Id_Tipo_Variable= getsettipovariable.get(position).getId_Tipo_Variable();
        Estado_Variable_=getsettipovariable.get(position).getEstado_Variable();
        ///Abrir dialog que contiene el detalle
        DialogOptionsVariable();



    }

    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public Dialog  DetailTipovariable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View v;
        v=inflater.inflate(R.layout.dialog_edit_tipo_variable, null);
        builder.setView(v);
        builder.setTitle("Actualizar Datos");


        //Campos detalle
        final EditText editNombreTipoVariable=(EditText)v.findViewById(R.id.editNombreTipoVariable);
        final EditText editUnidadMedida=(EditText)v.findViewById(R.id.editUnidadMedida);
        final EditText editDescripciontipovariable=(EditText)v.findViewById(R.id.editDescripciontipovariable);
        EditText editPosicion=(EditText)v.findViewById(R.id.editPosicion);
        final Switch editSwitchEstadoTipovariable=(Switch)v.findViewById(R.id.editSwitchEstadoTipovariable);

        ///Asignar
        editNombreTipoVariable.setText(Nombre_Tipo_Variable_);
        editDescripciontipovariable.setText(Descripcion_Variable_);
        editUnidadMedida.setText(Unidad_Medida_);
        editPosicion.setText(String.valueOf(Posicion_Variable_));
        editSwitchEstadoTipovariable.setChecked(Estado_Variable_);

        btn_save_changes_variable= (FloatingActionButton)v.findViewById(R.id.btn_save_changes_variable);

        btn_save_changes_variable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean estadoVariable;
                if(editSwitchEstadoTipovariable.isChecked()){
                    estadoVariable=true;
                }else{
                    estadoVariable=false;
                }
                registerChangesTipoVariable(estadoVariable,Id_Tipo_Variable,editNombreTipoVariable.getText().toString(),editDescripciontipovariable.getText().toString(),estadoVariable,editUnidadMedida.getText().toString());

            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(v, "No se realizaron cambios", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });
        builder.setMessage("¿Actualizar Informacion?");

        /*builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {



                boolean estadoVariable;
                if(editSwitchEstadoTipovariable.isChecked()){
                    estadoVariable=true;
                }else{
                    estadoVariable=false;
                }



               // dbconeccion_tipo_variable.actualizarTipoVariable(Id_Tipo_Variable,editNombreTipoVariable.getText().toString(),editDescripciontipovariable.getText().toString(),estadoVariable,editUnidadMedida.getText().toString());
               /// actulizarList();

            }
        });*/

        builder.setIcon(R.drawable.ledon);
        return builder.show();



    }


    private void registerChangesTipoVariable(boolean estadoVariable, long id_tipo_variable, String editNombreTipoVariable, String editDescripciontipovariable, boolean estadoVariable1, String editUnidadMedida) {
        dbconeccion_tipo_variable.actualizarTipoVariable(Id_Tipo_Variable,editNombreTipoVariable,editDescripciontipovariable,estadoVariable,editUnidadMedida);
        actulizarList();
    }
    ///Dialog detail ListView
    /*------------------------------------------------------------------------------------------------------------*/
    public Dialog DialogOptionsVariable() {
        final CharSequence[] items = {"Editar", "Real Time","Salir"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MENU")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                DetailTipovariable();
                                break;
                            case 1:

                                Intent y = new Intent(TiposVariableActivity.this,ChartLineRealTime.class );
                                y.putExtra("Posicion_Variable",Posicion_Variable_);
                                y.putExtra("Nombre_Tipo_Variable",Nombre_Tipo_Variable_);
                                y.putExtra("Estado_Variable",Estado_Variable_);
                                y.putExtra("Id_Tipo_Variable",Id_Tipo_Variable);


                                startActivity(y);
                                break;
                            default:
                                /// Toast.makeText(MenuControlActivity.this, "DEFAULT: ",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        /// builder.create();
        builder.setIcon(R.drawable.ledon);
        return builder.show();
    }

    ///Actualizar lista
     /*------------------------------------------------------------------------------------------------------------*/
    private void actulizarList() {
        listTipoVariables();
        AdapterListTipoVariables adapternotificaciones= new AdapterListTipoVariables(TiposVariableActivity.this,getsettipovariable);
        listViewTipoVariables.setAdapter(adapternotificaciones);
    }




    ///Se valida que ya se halla registrado de lo contrario se registra
    /*------------------------------------------------------------------------------------------------------------*/
    private void validarRegisterTipoVariable() {
        Tipo_Variable tipo_Variable=dbconeccion_tipo_variable.getFirstTipoVariable();
        if(tipo_Variable.getId_Tipo_Variable()>0){
            Toast.makeText(this, "Ya se encuentran registrados los tipos de variables",Toast.LENGTH_SHORT).show();
        }else{
            loadTipoVariable();
        }
    }


    //metodo que registra
    /*------------------------------------------------------------------------------------------------------------*/
    private void loadTipoVariable() {
        try {
        JSONArray jsonArray= TiposVariableJonObject(Nombre_Tipo_Variable,Posicion_Variable,Descripcion_Variable,Estado_Variable,Unidad_Medida,20);
        /// Toast.makeText(this, "JSON: "+jsonArray,Toast.LENGTH_LONG).show();
        for(int i=0; i<jsonArray.length(); i++)
        {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String Nombre_Tipo_Variable=   obj.getString("Nombre_Tipo_Variable");
                int Posicion_Variable=   obj.getInt("Posicion_Variable");
                String Descripcion_Variable=   obj.getString("Descripcion_Variable");
                boolean Estado_Variable=   obj.getBoolean("Estado_Variable");
                String Unidad_Medida=   obj.getString("Unidad_Medida");
                dbconeccion_tipo_variable.insertarTipoVariables(Nombre_Tipo_Variable,Posicion_Variable,Descripcion_Variable,Estado_Variable,Unidad_Medida);
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "Se registro correctamente ",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    ///Metodo para crear Array de datos Tipo de Variables
    /*------------------------------------------------------------------------------------------------------------*/
    public JSONArray TiposVariableJonObject(String Nombre_Tipo_Variable[], int Posicion_Variable[], String Descripcion_Variable[], boolean Estado_Variable[], String Unidad_Medida[], int numero_tipos)
            throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < numero_tipos; i++) {
            obj = new JSONObject();
            try {
                obj.put("Nombre_Tipo_Variable", Nombre_Tipo_Variable[i]);
                obj.put("Posicion_Variable", Posicion_Variable[i]);
                obj.put("Descripcion_Variable", Descripcion_Variable[i]);
                obj.put("Estado_Variable", Estado_Variable[i]);
                obj.put("Unidad_Medida", Unidad_Medida[i]);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }
        /// JSONObject finalobject = new JSONObject();
        //  finalobject.put("vehiculos", jsonArray);
        return jsonArray;
    }

    //Cargar Listas Notificaciones
    ///*------------------------------------------------------------------------------------------------------------*/
    private void listTipoVariables() {
        getsettipovariable = new ArrayList<Tipo_Variable>();
        ///Array list para Lista de vehiculos
        ArrayList<Tipo_Variable> listado = dbconeccion_tipo_variable.findAllTipoVariable();

        ////For del listado para Sincronizacion
        for (Tipo_Variable tipo : listado) {
            Tipo_Variable getsettipovar= new Tipo_Variable();
            getsettipovar.setId_Tipo_Variable(tipo.getId_Tipo_Variable());
            getsettipovar.setNombre_Tipo_Variable(tipo.getNombre_Tipo_Variable());
            getsettipovar.setPosicion_Variable(tipo.getPosicion_Variable());
            getsettipovar.setDescripcion_Variable(tipo.getDescripcion_Variable());
            getsettipovar.setEstado_Variable(tipo.getEstado_Variable());
            getsettipovar.setUnidad_Medida(tipo.getUnidad_Medida());

            getsettipovariable.add(getsettipovar);
        }
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
