package com.a3jfernando.serialport;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.ConectionInternet.ConnectivityReceiver;
import com.a3jfernando.serialport.Services.Utils.Const;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Device;
import com.a3jfernando.serialport.models.Variable;
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

public class SincronizarInformacionActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    ///SERVICES
    VariableController variableControllerInstance;

    //Datos de sincronizacion
    public ArrayList<Variable> getsetvariablesincronizacion;

    //UI Elements
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnSincronization)
    Button btnSincronization;

    ///Progres bar Horizontal
    ///Progres bar Horizontal
    private Handler mHandlerDialogLoad = new Handler();
    private ProgressDialog pDialog;
    int count;
    int size;
    int progres;

    double progresDouble;
    double progresDoubleSum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar_informacion);
        ButterKnife.bind(this);
        setToolbar();

        loadInstance();

        loadProgressDialog();

        loadEventClick();

        //Check connection Intenert
        checkConnection();
    }

    private void loadProgressDialog() {
        ///PROGRES DIALOG
       /*PROGRESS DIALOG*/
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sincronizando Informacion...");
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.setProgressStyle(pDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
    }

    private void loadEventClick() {
        btnSincronization.setOnClickListener(this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadInstance() {
        this.variableControllerInstance=VariableController.getInstance(this);
    }

    ///SINCRONIZAR INFORMACION
    /*---------------------------------------------------------------------------------------------*/
    ////Verificate sincronizacion Variables
    private void verificateSincronizacionVariable() {
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
    }

    ///Dialog sincronizar datos
    /*------------------------------------------------------------------------------------------------------------*/
    public Dialog DialogOptionsSincronizarVariable() {
        final CharSequence[] items = {"Sincronizar Ahora", "Mas tarde"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    }


    /*
    private void sincronizarDatosVariables() {
        int count =0;
        int size= getsetvariablesincronizacion.size();
            try {
                Thread.sleep(3000);
                int progres=100/size;
                ////
                for (Variable resultlist : getsetvariablesincronizacion) {
                    count++;
                  //  dbconeccion_variable_sincronization.actualizarStateSincronizarionVariables(resultlist.getId_Variable(),true);
                   // workProgress(progres);
                   //// SincronizationVariablesTask(resultlist.getData_Variable(),"60",resultlist.getFecha_Variable(),resultlist.getId_Equipo_Remoto(), resultlist.getId_Equipo_Remoto(),"HU",resultlist.getHora_Variable(),resultlist.getFecha_Hora_Variable(),resultlist.getFecha_Variable());
                    // Retardo de 1 segundo en la iteración
                 ////
                }
                //Show progres bar
                //workProgress(progres);
                ///progressbar.setIndeterminate(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       /// layoutProgressBar.setVisibility(View.GONE);
         ///   ocultarProgress();
        ////  layoutProgressBar.setVisibility(View.GONE);
    }*/
    /*
    private void workProgress() {
       //// progressStatus=progres;
        // Set the progress status zero on each button click
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i = 1; i <= 100; i++) {
                    ///progressStatus= progressStatus+progres;
                    progressStatus++;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(progressStatus);
                            // Show the progress on TextView
                            txtProgresBar.setText(progressStatus +" % "+"/"+progressbar.getMax()+" %");
                        }
                    });try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               // while(progressStatus < 100)
              //  {

                //}
            }
        }).start();
    }*/
    /*
    ///Ocultar progress bar
    private void ocultarProgress() {
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Try to sleep the thread for 20 milliseconds
                try{
                    progressbar.setIndeterminate(false);
                    Thread.sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                // Update the progress bar
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start(); // Start the operation
    }*/


    /*---------------------------------------------------------------------------------------------*/
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
                .make(findViewById(R.id.linearSincronizarionDatos), message, Snackbar.LENGTH_LONG);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSincronization:
                 checkConnection();
                break;
        }
    }
    /*---------------------------------------------------------------------------------------------*/

    /*Toolbar */
    /*---------------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
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
        return  true;
    }


    ///SERVICIOS REST
    /*Sincronizacion de datos a la web*/
    /*--------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------------------*/
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
            Toast.makeText(SincronizarInformacionActivity.this,"Error: "+e,Toast.LENGTH_SHORT).show();
            ////hideProgressDialog();
        }
    }




    //DIALOG
    /*-----------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------*/
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

    /*ASYNC DIALOG TASK*/
    /*----------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/
    public class SincronizacionDialogTask extends AsyncTask<Void, Void, Boolean> {
        /*
        ProgressDialog progress;
        MenuControlActivity act;
        public MyTask(ProgressDialog progress, MenuControlActivity act) {
            this.progress = progress;
            this.act = act;
        }*/
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
                Toast.makeText(SincronizarInformacionActivity.this, "True", Toast.LENGTH_SHORT).show();
            } else {
                hideProgressDialog();
                Toast.makeText(SincronizarInformacionActivity.this, "False", Toast.LENGTH_SHORT).show();
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
                //realizar la operación aquí
                size =0;
                size= getsetvariablesincronizacion.size();
                count=0;
                for (Variable resultlist : getsetvariablesincronizacion) {

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
                    //dbconeccion_variable_sincronization.actualizarStateSincronizarionVariables(resultlist.getId_Variable(),true);
                    //  int progres=100/size;
                    //// ProgresSincronizacion=String.valueOf("45%");
                    ///  SincronizationVariablesTask(resultlist.getData_Variable(),"60",resultlist.getFecha_Variable(),resultlist.getId_Equipo_Remoto(), resultlist.getId_Equipo_Remoto(),"HU",resultlist.getHora_Variable(),resultlist.getFecha_Hora_Variable(),resultlist.getFecha_Variable());
                }
                ///Thread.sleep(2000);
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




    /*----------------------------------------------------------------------------------------------------------------------*/

}
