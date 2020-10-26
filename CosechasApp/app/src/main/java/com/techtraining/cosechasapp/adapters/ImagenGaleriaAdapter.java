package com.techtraining.cosechasapp.adapters;

import android.content.Context;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.techtraining.cosechasapp.ImagePreviewActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.ImagenFila;

import java.util.List;

public class ImagenGaleriaAdapter extends ArrayAdapter<ImagenFila> {
    Context mContext;

    public static class ViewHolder {
        ImageView imgGaleria;
        CheckBox chkImagenGaleria;
    }

    public ImagenGaleriaAdapter(List<ImagenFila> data, Context context) {
        super(context, R.layout.item_galeria, data);
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImagenFila dataModel = getItem(position);
        ImagenGaleriaAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ImagenGaleriaAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_galeria, parent, false);
            viewHolder.imgGaleria = convertView.findViewById(R.id.imgGaleria);
            viewHolder.chkImagenGaleria = convertView.findViewById(R.id.chkImagenGaleria);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImagenGaleriaAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.imgGaleria.setImageBitmap(BitmapFactory.decodeFile(dataModel.path));
        viewHolder.chkImagenGaleria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.selected = isChecked;
            }
        });
        viewHolder.imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.IMAGE_NAME, dataModel.path);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
