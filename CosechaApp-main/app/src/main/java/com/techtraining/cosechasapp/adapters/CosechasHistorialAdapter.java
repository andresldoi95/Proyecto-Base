package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.MainActivity;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.Cosecha;

import java.util.List;

public class CosechasHistorialAdapter extends ArrayAdapter<Cosecha>  {
    private List<Cosecha> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvCosecha;
        TextView tvFechaDespacho;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCosecha.setText(mContext.getString(R.string.codigo_po_text, dataModel.codigoPo));
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
        return convertView;
    }
}
