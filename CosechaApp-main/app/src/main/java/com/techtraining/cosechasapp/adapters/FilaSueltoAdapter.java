package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.tasks.CargarDatosItemsSueltos;
import com.techtraining.cosechasapp.tasks.EliminarSuelto;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FilaSueltoAdapter extends ArrayAdapter<FilaSuelto> {
    private final boolean isHistorial;
    Context mContext;

    private static class ViewHolder {
        TextView tvBft;
        TextView tvIndice;
        TextView tvInformacion;
        FloatingActionButton btnEditar;
        FloatingActionButton btnFinalizar;
        FloatingActionButton btnFotos;
        FloatingActionButton btnEliminar;
    }

    public FilaSueltoAdapter(List<FilaSuelto> data, Context context) {
        super(context, R.layout.item_fila_camion, data);
        this.mContext=context;
        isHistorial = context.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FilaSuelto dataModel = getItem(position);
        FilaSueltoAdapter.ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new FilaSueltoAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fila_camion, parent, false);
            viewHolder.tvBft = convertView.findViewById(R.id.tvBft);
            viewHolder.tvIndice = convertView.findViewById(R.id.tvIndice);
            viewHolder.tvInformacion = convertView.findViewById(R.id.tvInformacion);
            viewHolder.btnEditar = convertView.findViewById(R.id.btnEditar);
            viewHolder.btnFotos = convertView.findViewById(R.id.btnFotos);
            viewHolder.btnFinalizar = convertView.findViewById(R.id.btnFinalizar);
            viewHolder.btnEliminar = convertView.findViewById(R.id.btnEliminar);
            viewHolder.btnEliminar.show();
            viewHolder.btnFinalizar.hide();
            viewHolder.btnFotos.hide();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilaSueltoAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.confirm_delete_title))
                        .setMessage(mContext.getString(R.string.confirm_delete_body))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new EliminarSuelto(
                                        mContext,
                                        dataModel,
                                        FilaSueltoAdapter.this
                                ).execute();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(Helper.CURRENT_ITEM_SUELTO_NAME, dataModel.id);
                editor.commit();
                String currentLlenado = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Helper.CURRENT_LLENADO_NAME, null);
                Intent intent;
                if ("S".equals(currentLlenado))
                    intent = new Intent(mContext, ItemSueltosActivity.class);
                else
                    intent = new Intent(mContext, ItemCosechaActivity.class);
                intent.putExtra(Helper.INDICE_NAME, dataModel.indice);
                mContext.startActivity(intent);
            }
        });

        viewHolder.tvBft.setText(String.valueOf(dataModel.bft));
        viewHolder.tvIndice.setText(String.valueOf(dataModel.indice + 1));

        if(isHistorial){
            viewHolder.btnFinalizar.hide();
            viewHolder.btnEditar.hide();
            viewHolder.btnEliminar.hide();
        }

        new CargarDatosItemsSueltos(mContext, dataModel, viewHolder.tvInformacion).execute();
        return convertView;
    }
}
