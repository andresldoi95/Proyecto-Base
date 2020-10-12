package com.techtraining.cosechasapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemCosechaActivity;
import com.techtraining.cosechasapp.LlenadoCamion;
import com.techtraining.cosechasapp.LlenadoSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.tasks.CargarDatosFila;
import com.techtraining.cosechasapp.tasks.FinalizarFila;

import java.util.List;

public class FilasCosechasAdapter extends ArrayAdapter<FilaCosecha> {
    Context mContext;
    Cosecha cosecha;

    private static class ViewHolder {
        TextView tvBft;
        TextView tvIndice;
        TextView tvInformacion;
        FloatingActionButton btnEditar;
        FloatingActionButton btnFinalizar;
        FloatingActionButton btnFotos;
    }

    public FilasCosechasAdapter(List<FilaCosecha> data, Context context, Cosecha cosecha) {
        super(context, R.layout.item_fila_camion, data);
        this.mContext=context;
        this.cosecha = cosecha;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FilaCosecha dataModel = getItem(position);
        FilasCosechasAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            final LlenadoCamion activity = (LlenadoCamion) mContext;
            viewHolder = new FilasCosechasAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fila_camion, parent, false);
            viewHolder.tvBft = convertView.findViewById(R.id.tvBft);
            viewHolder.tvIndice = convertView.findViewById(R.id.tvIndice);
            viewHolder.tvInformacion = convertView.findViewById(R.id.tvInformacion);
            viewHolder.btnEditar = convertView.findViewById(R.id.btnEditar);
            viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences.Editor editor = activity.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString(Helper.CURRENT_FILA_NAME, dataModel.id);
                    editor.commit();
                    if (cosecha.tipoLlenado.equals("B")) {
                        Intent intent = new Intent(activity, ItemCosechaActivity.class);
                        editor.putString(Helper.CURRENT_LLENADO_NAME, "B");
                        editor.commit();
                        activity.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(activity, LlenadoSueltosActivity.class);
                        editor.putString(Helper.CURRENT_LLENADO_NAME, "S");
                        editor.commit();
                        activity.startActivity(intent);
                    }
                }
            });
            viewHolder.btnFotos = convertView.findViewById(R.id.btnFotos);
            viewHolder.btnFinalizar = convertView.findViewById(R.id.btnFinalizar);
            if (dataModel.estado.equals("F")) {
                viewHolder.btnEditar.hide();
                viewHolder.btnFinalizar.hide();
            }
            viewHolder.btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext).setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(mContext.getString(R.string.desea_finalizar_fila)).setMessage(mContext.getString(R.string.desc_finalizar_fila))
                            .setPositiveButton(mContext.getString(R.string.si), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new FinalizarFila(mContext, dataModel).execute();
                                }
                            }).setNegativeButton(mContext.getString(R.string.no), null).show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilasCosechasAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvBft.setText(String.valueOf(dataModel.bft));
        viewHolder.tvIndice.setText(String.valueOf(dataModel.indice + 1));
        new CargarDatosFila(mContext, dataModel, viewHolder.tvInformacion).execute();
        return convertView;
    }
}
