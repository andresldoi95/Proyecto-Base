package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.FilaCosecha;

import java.util.List;

public class CosechasTerminadasAdapter extends ArrayAdapter<Cosecha>  {
    private List<Cosecha> dataSet;
    Context mContext;

    private static class ViewHolder {
        CheckBox chkSeleccionar;
        TextView tvCosecha;
        TextView tvFechaDespacho;
        TextView tvCamion;
        TextView tvVolumenEstimado;
    }

    public CosechasTerminadasAdapter(List<Cosecha> data, Context context) {
        super(context, R.layout.item_cosecha_terminada, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Cosecha dataModel = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_cosecha_terminada, parent, false);
            viewHolder.chkSeleccionar = (CheckBox) convertView.findViewById(R.id.chkSeleccionar);
            viewHolder.chkSeleccionar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dataModel.seleccionado = isChecked;
                }
            });
            convertView.setTag(viewHolder);
            viewHolder.tvCosecha = (TextView) convertView.findViewById(R.id.tvCosecha);
            viewHolder.tvFechaDespacho = (TextView) convertView.findViewById(R.id.tvFechaDespacho);
            viewHolder.tvCamion = (TextView) convertView.findViewById(R.id.tvCamion);
            viewHolder.tvVolumenEstimado = (TextView) convertView.findViewById(R.id.tvVolumenEstimado);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chkSeleccionar.setChecked(dataModel.seleccionado);
        //viewHolder.chkSeleccionar.setText(String.valueOf(position +1));
        viewHolder.tvCosecha.setText(mContext.getString(R.string.id_despacho_text, String.valueOf(position + 1)));

        viewHolder.tvFechaDespacho.setText(mContext.getString(R.string.fecha_despacho_text, dataModel.fechaDespacho));
        try{
            double valor_volumen = 0;

            for (int i = 0; i < dataModel.filas.size(); i++) {
                FilaCosecha fila = dataModel.filas.get(i);
                valor_volumen = valor_volumen+ fila.bft;
            }

            if(valor_volumen==0){
                valor_volumen= valor_volumen + dataModel.troza.volumenEstimado;

            }

            viewHolder.tvVolumenEstimado.setText(mContext.getString(R.string.volumen_text, String.valueOf(valor_volumen)));


        }catch (Exception e){
            viewHolder.tvVolumenEstimado.setText(mContext.getString(R.string.volumen_text, ""));
            Log.e( "Error volumen: ",e.toString() );
        }

        try{
            viewHolder.tvCamion.setText(mContext.getString(R.string.camion_placa_text, String.valueOf( dataModel.camionPlaca)));
        }catch (Exception e){
            viewHolder.tvCamion.setText(mContext.getString(R.string.camion_placa_text, ""));
            Log.e( "Error Camion: ",e.toString() );
        }
        return convertView;
    }
}
