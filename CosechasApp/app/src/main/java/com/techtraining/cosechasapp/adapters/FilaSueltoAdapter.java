package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.ItemSueltosActivity;
import com.techtraining.cosechasapp.LlenadoSueltosActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.FilaSuelto;
import com.techtraining.cosechasapp.tasks.CargarDatosItemsSueltos;

import java.util.List;

public class FilaSueltoAdapter extends ArrayAdapter<FilaSuelto> {
    Context mContext;

    private static class ViewHolder {
        TextView tvBft;
        TextView tvIndice;
        TextView tvInformacion;
        FloatingActionButton btnEditar;
        FloatingActionButton btnFinalizar;
        FloatingActionButton btnFotos;
    }

    public FilaSueltoAdapter(List<FilaSuelto> data, Context context) {
        super(context, R.layout.item_fila_camion, data);
        this.mContext=context;

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
            viewHolder.btnFinalizar.hide();
            viewHolder.btnFotos.hide();
            viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString(Helper.CURRENT_ITEM_SUELTO_NAME, dataModel.id);
                    editor.commit();
                    Intent intent = new Intent(mContext, ItemSueltosActivity.class);
                    intent.putExtra(Helper.INDICE_NAME, dataModel.indice);
                    mContext.startActivity(intent);
                }
            });
            new CargarDatosItemsSueltos(mContext, dataModel, viewHolder.tvInformacion).execute();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilaSueltoAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvBft.setText(String.valueOf(dataModel.bft));
        viewHolder.tvIndice.setText(String.valueOf(dataModel.indice + 1));
        return convertView;
    }
}
