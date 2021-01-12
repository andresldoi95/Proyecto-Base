package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Camion;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.FilaCosecha;

import java.util.List;

public class CosechasHistorialAdapter extends ArrayAdapter<Cosecha>  {
    private List<Cosecha> dataSet;
    Context mContext;
    private AppDatabase appDatabase;


    private static class ViewHolder {
        TextView tvCosecha;
        TextView tvFechaDespacho;
        TextView tvCamion;
        TextView tvVolumenEstimado;
        FloatingActionButton btnHistorial;
    }

    public CosechasHistorialAdapter(List<Cosecha> data, Context context) {
        super(context, R.layout.item_cosecha_historial, data);
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
            convertView = inflater.inflate(R.layout.item_cosecha_historial, parent, false);
            viewHolder.tvCosecha = (TextView) convertView.findViewById(R.id.tvCosecha);
            viewHolder.btnHistorial = (FloatingActionButton) convertView.findViewById(R.id.btnHistorial);
            viewHolder.tvFechaDespacho = (TextView) convertView.findViewById(R.id.tvFechaDespacho);
            viewHolder.tvCamion = (TextView) convertView.findViewById(R.id.tvCamion);
            viewHolder.tvVolumenEstimado = (TextView) convertView.findViewById(R.id.tvVolumenEstimado);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCosecha.setText(mContext.getString(R.string.id_despacho_text, String.valueOf(position + 1)));

        viewHolder.tvFechaDespacho.setText(mContext.getString(R.string.fecha_despacho_text, dataModel.fechaDespacho));
        viewHolder.btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(Helper.IS_HISTORIAL, true);
                editor.putString(Helper.CURRENT_COSECHA_ID_HISTORIAL, dataModel.id);
                editor.commit();
                Intent intentNuevaCosecha = new Intent(mContext, NuevaCosechaActivity.class);
                mContext.startActivity(intentNuevaCosecha);
            }
        });

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
