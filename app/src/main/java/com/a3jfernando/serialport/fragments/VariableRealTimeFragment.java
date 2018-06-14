package com.a3jfernando.serialport.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class VariableRealTimeFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;


    @BindView(R.id.linearLayouSpeedView)
    LinearLayout linearLayouSpeedView;

    ///SPEED VIEW REAL TIME
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.linearLayout5)
    LinearLayout linearLayout5;
    @BindView(R.id.linearLayout6)
    LinearLayout linearLayout6;
    @BindView(R.id.linearLayout7)
    LinearLayout linearLayout7;
    @BindView(R.id.linearLayout8)
    LinearLayout linearLayout8;
    @BindView(R.id.linearLayout9)
    LinearLayout linearLayout9;
    @BindView(R.id.linearLayout10)
    LinearLayout linearLayout10;

    //TextView
    @BindView(R.id.txtTitleMedidaPosition1)
    TextView txtTitleMedidaPosition1;
    @BindView(R.id.txtTitleMedidaPosition2)
    TextView txtTitleMedidaPosition2;
    @BindView(R.id.txtTitleMedidaPosition3)
    TextView txtTitleMedidaPosition3;
    @BindView(R.id.txtTitleMedidaPosition4)
    TextView txtTitleMedidaPosition4;
    @BindView(R.id.txtTitleMedidaPosition5)
    TextView txtTitleMedidaPosition5;
    @BindView(R.id.txtTitleMedidaPosition6)
    TextView txtTitleMedidaPosition6;
    @BindView(R.id.txtTitleMedidaPosition7)
    TextView txtTitleMedidaPosition7;
    @BindView(R.id.txtTitleMedidaPosition8)
    TextView txtTitleMedidaPosition8;
    @BindView(R.id.txtTitleMedidaPosition9)
    TextView txtTitleMedidaPosition9;
    @BindView(R.id.txtTitleMedidaPosition10)
    TextView txtTitleMedidaPosition10;

    ///Date time

    @BindView(R.id.txtHourMedida1)
    TextView txtHourMedida1;
    @BindView(R.id.txtHourMedida2)
    TextView txtHourMedida2;
    @BindView(R.id.txtHourMedida3)
    TextView txtHourMedida3;
    @BindView(R.id.txtHourMedida4)
    TextView txtHourMedida4;
    @BindView(R.id.txtHourMedida5)
    TextView txtHourMedida5;
    @BindView(R.id.txtHourMedida6)
    TextView txtHourMedida6;
    @BindView(R.id.txtHourMedida7)
    TextView txtHourMedida7;
    @BindView(R.id.txtHourMedida8)
    TextView txtHourMedida8;
    @BindView(R.id.txtHourMedida9)
    TextView txtHourMedida9;
    @BindView(R.id.txtHourMedida10)
    TextView txtHourMedida10;


    ///digit_speed_view_real5
    @BindView(R.id.digit_speed_view_real1)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real1;
    @BindView(R.id.digit_speed_view_real2)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real2;
    @BindView(R.id.digit_speed_view_real3)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real3;
    @BindView(R.id.digit_speed_view_real4)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real4;
    @BindView(R.id.digit_speed_view_real5)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real5;
    @BindView(R.id.digit_speed_view_real6)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real6;
    @BindView(R.id.digit_speed_view_real7)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real7;
    @BindView(R.id.digit_speed_view_real8)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real8;
    @BindView(R.id.digit_speed_view_real9)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real9;
    @BindView(R.id.digit_speed_view_real10)
    com.a3jfernando.serialport.libraries.DigitSpeedViewLibrary.DigitSpeedView digit_speed_view_real10;

    public VariableRealTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_variables,container,false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    ///Escucha Los valores enviados por Serial Port desde Menu Activity
    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Tipo_Variable tipo_variable = intent.getExtras().getParcelable("tipo_variable");
            String data= intent.getStringExtra("data_medida");
            boolean usb_conected = intent.getExtras().getBoolean("connected");
            String date_time_data= intent.getStringExtra("date_time_data");

            loadDataSpeedView(data,tipo_variable,usb_conected,date_time_data);
            ///display.append(tipo_variable.getNombre_Tipo_Variable()+": "+data+"\n");
        }
    };


    private void loadDataSpeedView(String data, Tipo_Variable tipo_variable,boolean usb_conected,String date_time_data) {
        if(!usb_conected){
            hideContainersVariables();
        }

        if(isParceableDouble(data)){
            double dataDouble= Double.parseDouble(data);;
            ////float speedKmFloat=(float)speedKm;
            //Maxima velocidad
            String stringData= String.format("%.0f", dataDouble);
            /// String velocidad= String.format("%.1f", speedKm);
            ///float speedKmFloat= Float.parseFloat(max_velo);
            int intData= Integer.parseInt(stringData);
            if(tipo_variable.getPosicion_Variable()==0){
                linearLayout1.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition1.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real1.updateSpeed(intData);
                txtHourMedida1.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==1){
                linearLayout2.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition2.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real2.updateSpeed(intData);
                txtHourMedida2.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==2){
                linearLayout3.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition3.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real3.updateSpeed(intData);
                txtHourMedida3.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==3){
                linearLayout4.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition4.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real4.updateSpeed(intData);
                txtHourMedida4.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==4){
                linearLayout5.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition5.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real5.updateSpeed(intData);
                txtHourMedida5.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==5){
                linearLayout6.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition6.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real6.updateSpeed(intData);
                txtHourMedida6.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==6){
                linearLayout7.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition7.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real7.updateSpeed(intData);
                txtHourMedida7.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==7){
                linearLayout8.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition8.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real8.updateSpeed(intData);
                txtHourMedida8.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==8){
                linearLayout9.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition9.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real9.updateSpeed(intData);
                txtHourMedida9.setText(date_time_data);
            }else if(tipo_variable.getPosicion_Variable()==9){
                linearLayout10.setVisibility(View.VISIBLE);
                txtTitleMedidaPosition10.setText(tipo_variable.getNombre_Tipo_Variable()+ " ("+tipo_variable.getUnidad_Medida()+")");
                digit_speed_view_real10.updateSpeed(intData);
                txtHourMedida10.setText(date_time_data);
            }
        }
    }

    private void hideContainersVariables() {
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        linearLayout3.setVisibility(View.GONE);
        linearLayout4.setVisibility(View.GONE);
        linearLayout5.setVisibility(View.GONE);
        linearLayout6.setVisibility(View.GONE);
        linearLayout7.setVisibility(View.GONE);
        linearLayout8.setVisibility(View.GONE);
        linearLayout9.setVisibility(View.GONE);
        linearLayout10.setVisibility(View.GONE);
    }

    //Verifica se puede parcear el valor
    private static boolean isParceableDouble(String cadena){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

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

    @Override
    public void onClick(View v) {

    }
}
