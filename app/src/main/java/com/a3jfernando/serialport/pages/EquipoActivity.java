package com.a3jfernando.serialport.pages;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Configuration;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.Objects;
/*
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;*/

public class EquipoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
  //  Socket socket;
    TextView txtConversation;
    EditText txtMensage;
    Switch switchEstado;
    String accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtConversation=(TextView)findViewById(R.id.txtConversation);
        txtMensage=(EditText) findViewById(R.id.editTextMessage);

        switchEstado=(Switch)findViewById(R.id.switchEstado);
        switchEstado.setOnCheckedChangeListener(this);
/*
        try {
            socket = IO.socket("http://iot.bitnamiapp.com:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on("newaccion", onNewMessage);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {


                JSONObject obj = new JSONObject();

                try {
                    obj.put("author", "server");
                    obj.put("text", "WP");
                    socket.emit("new-message", obj);
                    ///socket.disconnect();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               // String json = "{'author':'N95','text':'WP'}";

              //  try {
              //      JSONObject objs = new JSONObject(json);
              //      socket.emit("new-message", objs);
              //      socket.disconnect();
              //  } catch (JSONException e) {
               //     e.printStackTrace();
               // }




            }




        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {


            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        socket.connect();
        */

    }

    public  void  enviar(View view){


    }
/*
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String accion;

                    try {
                        accion = data.getString("data");
                        if(accion.equals("on")){
                            switchEstado.setChecked(true);
                        }else{
                            switchEstado.setChecked(false);
                        }

                    } catch (JSONException e) {

                        return;
                    }

                    Toast.makeText(getApplicationContext(),"Estado: "+accion,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    private Emitter.Listener onNewAccion = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = new JSONObject();

                    try {
                        data.put("data", accion);

                        socket.emit("accion", data);
                        ///socket.disconnect();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

*/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*
        try {
            if (isChecked) {

               // SwitLet.setText("Apagar");
                accion="on";
               ///// socket.emit("accion", onNewAccion);
                JSONObject data = new JSONObject();

                try {
                    data.put("data", "on");

                    socket.emit("accion", data);
                    ///socket.disconnect();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                accion="off";
                //SwitLet.setText("Encender");
               //// socket.emit("accion", onNewAccion);

                JSONObject data = new JSONObject();

                try {
                    data.put("data", "off");

                    socket.emit("accion", data);
                    ///socket.disconnect();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){

        }
        */
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return  true;
    }
}
