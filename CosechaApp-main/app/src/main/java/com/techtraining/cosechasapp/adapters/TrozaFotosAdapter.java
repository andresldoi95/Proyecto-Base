package com.techtraining.cosechasapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtraining.cosechasapp.Helper;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.TrozaActivity;
import com.techtraining.cosechasapp.db.TrozaFotos;

import java.io.File;
import java.util.ArrayList;

public class TrozaFotosAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<TrozaFotos> currentFotos;
    private boolean isHistorial;

    private class ViewHolder {
        ImageView ivFoto;
        FloatingActionButton btnEliminar;

        public ViewHolder(View view) {
            ivFoto = view.findViewById(R.id.ivFotoTroza);
            btnEliminar = view.findViewById(R.id.btnEliminarFotoTroza);
        }
    }

    public TrozaFotosAdapter(ArrayList<TrozaFotos> data, Context context) {
        this.mContext=context;
        this.currentFotos=data;
        isHistorial = mContext.getSharedPreferences(Helper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(Helper.IS_HISTORIAL, false);
    }

    @Override
    public int getCount() {
        return currentFotos.size();
    }

    @Override
    public Object getItem(int position) {
        return currentFotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrozaFotos dataModel = (TrozaFotos) getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_troza_foto, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.confirm_delete_title))
                        .setMessage(mContext.getString(R.string.confirm_delete_body))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                currentFotos.remove(dataModel);
                                notifyDataSetChanged();
                                ((TrozaActivity)mContext).currentFotos = currentFotos;
                                ((TrozaActivity)mContext).adapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        File file = new File(dataModel.foto);
        viewHolder.ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        if(isHistorial){
            viewHolder.btnEliminar.setEnabled(false);
            viewHolder.btnEliminar.hide();
        }
        return convertView;
    }
}
