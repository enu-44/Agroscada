package com.a3jfernando.serialport.Adapters.listactuadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.models.Device;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by EnuarMunoz on 17/04/18.
 */




public class AdapterListActuadores extends RecyclerView.Adapter<AdapterListActuadores.CableElementViewHolder> {

    public List<Device> dataset;
    public OnItemClickListenerActuadores onItemClickListener;
    public Context context;


    public AdapterListActuadores(Context context, List<Device> dataset, OnItemClickListenerActuadores onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void setItems(List<Device> newItems) {
        dataset.addAll(newItems);
        notifyDataSetChanged();

    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public AdapterListActuadores.CableElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_control, parent, false);
        return new AdapterListActuadores.CableElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterListActuadores.CableElementViewHolder holder, final int position) {
        Device list = dataset.get(position);



        HashMap<String, Integer> images = new HashMap<String, Integer>();
        images.put( "ic_icon_ventilador", Integer.valueOf( R.drawable.ic_icon_ventilador ) );
        images.put( "ic_icon_motor", Integer.valueOf( R.drawable.ic_icon_motor ) );
        images.put( "ic_icon_bombillo", Integer.valueOf( R.drawable.ic_icon_bombillo ) );
        images.put( "ic_icon_motobomba", Integer.valueOf( R.drawable.ic_icon_motobomba ) );
        images.put( "ic_icon_actuador", Integer.valueOf( R.drawable.ic_icon_actuador ) );
        String correctAnswer = list.getImage_Type_Device();
        holder.contentIcon.setImageResource(images.get( correctAnswer ).intValue() );



        holder.txtTitle.setText(list.getName_Device());
        holder.txtDescription.setText(list.getDescription_Device());

        holder.radionBtnOff.setEnabled(false);
        holder.radionBtnOn.setEnabled(true);


        holder.setOnItemClickListener(list, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class CableElementViewHolder extends RecyclerView.ViewHolder {
        //se especifica lo que existe dentro del ViewHolder


        @BindView(R.id.txtDescription)
        TextView txtDescription;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.contentIcon)
        ImageView contentIcon;


        @BindView(R.id.btnAction1)
        ImageButton configuracionControl;


        @BindView(R.id.btnAction2)
        ImageButton btnEditarActuador;


        @BindView(R.id.radioButtonOn)
        RadioButton radionBtnOn;

        @BindView(R.id.radioButtonOff)
        RadioButton radionBtnOff;

        @BindView(R.id.radioGroupActuadores)
        RadioGroup radioGroupActuadores;


        private View view;

        public CableElementViewHolder(View v) {
            super(v);
            this.view = v;
            ButterKnife.bind(this, v);
        }

        public void setOnItemClickListener(final Device device, final OnItemClickListenerActuadores onItemClickListener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(device);
                }
            });

          /*  btnEditCable.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClickEdit(responseNotify);
                }
            });*/

            radionBtnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroupActuadores.check(R.id.radioButtonOn);
                    radionBtnOff.setEnabled(false);
                    radionBtnOn.setEnabled(true);

                    onItemClickListener.onItemSerialOff(device);

                }
            });


            radionBtnOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radionBtnOff.setEnabled(true);
                    radionBtnOn.setEnabled(false);
                    radioGroupActuadores.check(R.id.radioButtonOff);
                    onItemClickListener.onItemSerialOn(device);
                }
            });


            configuracionControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemConfiguracionControl(device);
                }
            });

            btnEditarActuador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemEditControl(device);
                }
            });
        }
    }
}