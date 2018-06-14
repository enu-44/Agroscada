package com.a3jfernando.serialport.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Variable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Enuar Munoz on 26/04/2017.
 */

public class AdapterReportVariable extends BaseAdapter {
    ///Declaramos lo siguinte
    protected Activity activity;
    ///Dentro del array list colocamos la clase Notificacion
    protected ArrayList<Variable> items;
    ///Metodo constructor de la clase

    public AdapterReportVariable(Activity activity,ArrayList<Variable> items){
        this.activity=activity;
        this.items=items;
    }

    @Override
    public int getCount() {
        ////Obtenemos el tamaño del items
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        //Obtenemos posicion de los items
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId_Variable();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ///Generamos una convertView por motivos de eficiencia
        View v= convertView;
        ///Asociamos el layout de la lista que hemos creado
        if(convertView==null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inf.inflate(R.layout.formato_list_variables, null);
        }
        ///Creamos un Objeto de la clase Vehiculo
        final Variable variables= items.get(position);
        TextView txtNombreTipoVariable= (TextView)v.findViewById(R.id.txtNombreTipoVariableReport);
        txtNombreTipoVariable.setText(variables.getNombre_Tipo_Variable());

        TextView txtDescripciomTipovariable= (TextView)v.findViewById(R.id.txtDescripcionTipoVariableReport);
        txtDescripciomTipovariable.setText(variables.getDescripcion_Tipo_Variable());

        TextView txtDatavariable= (TextView)v.findViewById(R.id.txtDataVariable);
        txtDatavariable.setText(variables.getData_Variable());


        SimpleDateFormat sdfEnd = new SimpleDateFormat("H:mm");
        Date dateObjEnd = null;
        try {
            dateObjEnd = sdfEnd.parse(variables.getHora_Variable());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView txtHoraVariable= (TextView)v.findViewById(R.id.txtHoraVariable);
        txtHoraVariable.setText(new SimpleDateFormat("KK:mm a").format(dateObjEnd));

        TextView txtFechaVariable= (TextView)v.findViewById(R.id.txtFechaVariable);
        txtFechaVariable.setText(variables.getFecha_Variable());
        ImageView imgUploadVariables= (ImageView)v.findViewById(R.id.imgUploadVariable);
        if(!variables.getEstado_Sincronizacion_Variable()){
            imgUploadVariables.setImageResource(R.drawable.ic_launcher_upload_cloud);
        }else
            imgUploadVariables.setImageResource(R.drawable.ic_launcher_uploaded_cloud);
        return v;
    }
}
