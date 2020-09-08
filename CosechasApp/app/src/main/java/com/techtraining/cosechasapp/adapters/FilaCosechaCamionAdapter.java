package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.ItemFilaCosecha;

import java.util.List;

public class FilaCosechaCamionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<ItemFilaCosecha> items;
    public FilaCosechaCamionAdapter(Context context, List<ItemFilaCosecha> items) {
        this.context = context;
        this.items = items;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_fila_cosecha_camion, null);
        TextView tvFila = (TextView) view.findViewById(R.id.tvFila);
        TextView tvColumna = (TextView) view.findViewById(R.id.tvColumna);
        TextView tvBft = (TextView) view.findViewById(R.id.tvBft);
        ItemFilaCosecha itemFilaCosecha = items.get(i);
        tvFila.setText(String.valueOf(itemFilaCosecha.fila));
        tvColumna.setText(String.valueOf(itemFilaCosecha.columna));
        tvBft.setText(String.valueOf(itemFilaCosecha.bft));
        return view;
    }
}
