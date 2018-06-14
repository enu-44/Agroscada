package com.a3jfernando.serialport.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Device;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admi on 15/04/17.
 */

public class AdapterListDevices extends BaseAdapter {
    ///Declaramos lo siguinte
    protected Activity activity;
    ///Dentro del array list colocamos la clase Notificacion
    protected ArrayList<Device> items;
    ///Metodo constructor de la clase

    public AdapterListDevices(Activity activity, ArrayList<Device> items){
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
        return items.get(position).getId_Device();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ///Generamos una convertView por motivos de eficiencia
        View v= convertView;
        ///Asociamos el layout de la lista que hemos creado
        if(convertView==null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inf.inflate(R.layout.formato_list_devices, null);
        }

        ///Creamos un Objeto de la clase Vehiculo
        final Device equipo= items.get(position);
        ///Rellenamos informacion tiponotificacion
        long IdDevice= equipo.getId_Device();
        String Name_Equipment= equipo.getName_Device();
        String Description_Equipment= equipo.getDescription_Device();

        ImageView imageViewDevice= (ImageView)v.findViewById(R.id.imageViewDevice);
       /// imageViewDevice.setImageResource(R.drawable.ledon);



        HashMap<String, Integer> images = new HashMap<String, Integer>();
        images.put( "ic_icon_ventilador", Integer.valueOf( R.drawable.ic_icon_ventilador ) );
        images.put( "ic_icon_motor", Integer.valueOf( R.drawable.ic_icon_motor ) );
        images.put( "ic_icon_bombillo", Integer.valueOf( R.drawable.ic_icon_bombillo ) );
        images.put( "ic_icon_motobomba", Integer.valueOf( R.drawable.ic_icon_motobomba ) );
        images.put( "ic_icon_actuador", Integer.valueOf( R.drawable.ic_icon_actuador ) );
        String correctAnswer = equipo.getImage_Type_Device();
        imageViewDevice.setImageResource( images.get( correctAnswer ).intValue() );


        TextView txtName_Equipment= (TextView)v.findViewById(R.id.txtNombreEquipment);
        TextView txtDescription_Equipment= (TextView)v.findViewById(R.id.txtDescripcionEquipment);
        txtName_Equipment.setText(Name_Equipment);
        txtDescription_Equipment.setText(Description_Equipment);

        return v;
    }
}
