package com.a3jfernando.serialport.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.UsbService;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Tipo_Variable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SerialMonitorFragment extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;

    //UI Elements
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


    //Listas
    ArrayList<Tipo_Variable> listaTipeVariables= new ArrayList<Tipo_Variable>();

    ///Services
    VariableController variableInstance;
    TipoVariableController tipoVariableControllerInstance;

    public SerialMonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serial_monitor,container,false);
        unbinder = ButterKnife.bind(this, view);
        loadInstance();
        loadVariables();
        return view;
    }

    private void loadVariables() {
        tipoVariableControllerInstance.abrirBaseDeDatos();
        listaTipeVariables=tipoVariableControllerInstance.findVariableActives();
        Toast.makeText(getActivity(), "Variables Cargadas "+String.valueOf(listaTipeVariables.size()),Toast.LENGTH_SHORT).show();
        tipoVariableControllerInstance.cerrar();
    }

    private void loadInstance() {
        this.variableInstance= VariableController.getInstance(getActivity());
        this.tipoVariableControllerInstance=TipoVariableController.getInstance(getActivity());
    }

    ///Escucha Los valores enviados por Serial Port desde Menu Activity
    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Tipo_Variable tipo_variable = intent.getExtras().getParcelable("tipo_variable");
            String data= intent.getStringExtra("data_medida");
            display.append(tipo_variable.getNombre_Tipo_Variable()+": "+data+"\n");
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

    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNotificationReceiver);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendWrite:
                ((MenuControlActivity)this.getActivity()).setWriteSerial(txtDigitalWrite.getText().toString());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /// getActivity().unregisterReceiver(mNotificationReceiver);
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ///getActivity().unregisterReceiver(mNotificationReceiver);
    }
}
