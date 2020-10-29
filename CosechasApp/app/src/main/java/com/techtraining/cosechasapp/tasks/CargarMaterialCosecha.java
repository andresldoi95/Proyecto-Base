package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.NuevaCosechaActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;
import com.techtraining.cosechasapp.db.Espesor;
import com.techtraining.cosechasapp.db.FilaCosecha;
import com.techtraining.cosechasapp.db.Largo;
import com.techtraining.cosechasapp.db.Material;
import com.techtraining.cosechasapp.db.Tarifa;
import com.techtraining.cosechasapp.db.TipoBulto;

public class CargarMaterialCosecha extends AsyncTask<Void, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private int tipoMaderaId;
    private int origenMaderaId;
    private TextView tvInformacion;
    private Espesor espesor;
    private Largo largo;
    private Material material;
    public CargarMaterialCosecha(Context context, int tipoMaderaId, int origenMaderaId) {
        this.context = context;
        this.tipoMaderaId = tipoMaderaId;
        this.origenMaderaId = origenMaderaId;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        appDatabase = DBManager.getInstance(context);
        material = appDatabase.materialDao().loadByIdTipos(tipoMaderaId, origenMaderaId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        NuevaCosechaActivity activity = (NuevaCosechaActivity) context;
        if (material != null) {
            activity.etMaterial.setText(material.descripcion);
            activity.materialId = material.id;
        }
        else {
            activity.etMaterial.setText("");
            activity.materialId = -1;
        }
    }
}
