package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.Cosecha;

import java.util.List;

public class CosechasTerminadasAdapter extends ArrayAdapter<Cosecha>  {
    private List<Cosecha> dataSet;
    Context mContext;

    private static class ViewHolder {
        CheckBox chkSeleccionar;
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
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chkSeleccionar.setChecked(dataModel.seleccionado);
        viewHolder.chkSeleccionar.setText(dataModel.codigoPo);
        return convertView;
    }
}
