package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.FilaCosecha;

import java.util.List;

public class FilasCosechasAdapter extends ArrayAdapter<FilaCosecha> {
    private List<FilaCosecha> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvFilas;
        TextView tvColumnas;
        TextView tvBft;
        TextView tvIndice;
    }

    public FilasCosechasAdapter(List<FilaCosecha> data, Context context) {
        super(context, R.layout.item_fila_camion, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilaCosecha dataModel = getItem(position);
        FilasCosechasAdapter.ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new FilasCosechasAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fila_camion, parent, false);
            viewHolder.tvFilas = (TextView) convertView.findViewById(R.id.tvFilas);
            viewHolder.tvColumnas = (TextView) convertView.findViewById(R.id.tvColumnas);
            viewHolder.tvBft = (TextView) convertView.findViewById(R.id.tvBft);
            viewHolder.tvIndice = (TextView) convertView.findViewById(R.id.tvIndice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilasCosechasAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFilas.setText(String.valueOf(dataModel.filas));
        viewHolder.tvColumnas.setText(String.valueOf(dataModel.columnas));
        viewHolder.tvBft.setText(String.valueOf(dataModel.bft));
        viewHolder.tvIndice.setText(String.valueOf(dataModel.indice));
        return convertView;
    }
}
