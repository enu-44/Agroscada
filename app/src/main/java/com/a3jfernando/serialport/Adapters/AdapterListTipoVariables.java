package com.a3jfernando.serialport.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Configuration;
import com.a3jfernando.serialport.models.Tipo_Variable;

import java.util.ArrayList;

/**
 * Created by admi on 22/03/17.
 */

public class AdapterListTipoVariables  extends BaseAdapter {
    ///Declaramos lo siguinte
    protected Activity activity;
    ///Dentro del array list colocamos la clase Notificacion
    protected ArrayList<Tipo_Variable> items;
    ///Metodo constructor de la clase

    public AdapterListTipoVariables(Activity activity,ArrayList<Tipo_Variable> items){
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
        return items.get(position).getId_Tipo_Variable();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ///Generamos una convertView por motivos de eficiencia
        View v= convertView;
        ///Asociamos el layout de la lista que hemos creado
        if(convertView==null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inf.inflate(R.layout.formato_list_tipo_variable, null);
        }
        ///Creamos un Objeto de la clase Vehiculo
        final Tipo_Variable tipovariable= items.get(position);
        ///Rellenamos informacion tiponotificacion
        boolean tipocontenido= tipovariable.getEstado_Variable();



        TextView txtPosition= (TextView)v.findViewById(R.id.txtPosicion);
        int posicionint= tipovariable.getPosicion_Variable();
        String posicionString= String.valueOf(posicionint);
        txtPosition.setText(posicionString);

        TextView txtDescripcion= (TextView)v.findViewById(R.id.txtDescripcionTipoVariable);
        txtDescripcion.setText(tipovariable.getDescripcion_Variable());

        TextView txtUnidadMedida= (TextView)v.findViewById(R.id.txtUnidadMedida);
        txtUnidadMedida.setText(tipovariable.getUnidad_Medida());
        //final Switch switchEstadovariable= (Switch)v.findViewById(R.id.switchEstadoTipoVariable);
        /*
        switchEstadovariable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(parent.getContext(), "Hola" +tipovariable.getId_Tipo_Variable(),Toast.LENGTH_SHORT).show();

            }
        });
*/
        if(!tipocontenido){
            TextView txtNombreTipoVariable= (TextView)v.findViewById(R.id.txtNombreTipoVariable);
            //imageEstado.setImageResource(R.drawable.falta_lectura2);
            ImageView imageTipoVariable= (ImageView)v.findViewById(R.id.imageTipoVariable);
            ImageView imageEstado= (ImageView)v.findViewById(R.id.imageViewEstado);
            txtNombreTipoVariable.setText(tipovariable.getNombre_Tipo_Variable());
            txtNombreTipoVariable.setTextColor(Color.rgb(0,135,115));
            imageTipoVariable.setImageResource(R.drawable.ledonoff);
            imageEstado.setImageResource(R.drawable.ic_launcher_inactive);
            ///switchEstadovariable.setChecked(false);
           /// boolean swi=tipovariable.getEstado_Variable();
          //  String swiString= String.valueOf(swi);
          ///  switchEstadovariable.setText(swiString);
        }else{
            TextView txtNombreTipoVariable= (TextView)v.findViewById(R.id.txtNombreTipoVariable);
            ImageView imageTipoVariable= (ImageView)v.findViewById(R.id.imageTipoVariable);
            ImageView imageEstado= (ImageView)v.findViewById(R.id.imageViewEstado);
            txtNombreTipoVariable.setText(tipovariable.getNombre_Tipo_Variable());
            txtNombreTipoVariable.setTextColor(Color.rgb(191,1,20));
            imageTipoVariable.setImageResource(R.drawable.ledon);
            imageEstado.setImageResource(R.drawable.ic_launcher_active);
           /// switchEstadovariable.setChecked(true);
           /// boolean swi=tipovariable.getEstado_Variable();
           /// String swiString= String.valueOf(swi);
            //switchEstadovariable.setText(swiString);
        }


        ///Rellenamos informacion  contenido_notificacion
      //  TextView contenido_notificacion= (TextView)v.findViewById(R.id.txtContenidoNotificacion);
     //   contenido_notificacion.setText(notificacion.getContenido_Notificacion());
        //Rellenamos la vista
        return v;
    }
}
