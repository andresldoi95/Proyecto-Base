package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.FilaSuelto;

import java.util.List;

public class FilaSueltoAdapter extends ArrayAdapter<FilaSuelto> {
    Context mContext;

    private static class ViewHolder {
        TextView tvBft;
        TextView tvIndice;
    }

    public FilaSueltoAdapter(List<FilaSuelto> data, Context context) {
        super(context, R.layout.item_fila_camion, data);
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilaSuelto dataModel = getItem(position);
        FilaSueltoAdapter.ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new FilaSueltoAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fila_camion, parent, false);
            viewHolder.tvBft = convertView.findViewById(R.id.tvBft);
            viewHolder.tvIndice = convertView.findViewById(R.id.tvIndice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilaSueltoAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvBft.setText(String.valueOf(dataModel.bft));
        viewHolder.tvIndice.setText(String.valueOf(dataModel.indice + 1));
        return convertView;
    }
}
