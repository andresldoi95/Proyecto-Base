package com.techtraining.cosechasapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.techtraining.cosechasapp.Helper;

@Database(entities = {User.class, Empresa.class, Camion.class, Controlador.class, Procedencia.class, Aserrador.class, Destino.class,
Espesor.class, Largo.class, Material.class, CodigoAserrador.class, Cosecha.class,
Parametro.class, FilaCosecha.class, ItemFilaCosecha.class, MaterialProcedencia.class,
TipoMadera.class, FormatoEntrega.class, OrigenMadera.class, TipoBulto.class, FilaSuelto.class,
Tarifa.class}, version = Helper.DB_VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EmpresaDao empresaDao();
    public abstract CamionDao camionDao();
    public abstract ControladorDao controladorDao();
    public abstract ProcedenciaDao procedenciaDao();
    public abstract AserradorDao aserradorDao();
    public abstract DestinoDao destinoDao();
    public abstract EspesorDao espesorDao();
    public abstract LargoDao largoDao();
    public abstract MaterialDao materialDao();
    public abstract CodigoAserradorDao codigoAserradorDao();
    public abstract CosechaDao cosechaDao();
    public abstract ParametroDao parametroDao();
    public abstract FilaCosechaDao filaCosechaDao();
    public abstract ItemFilaCosechaDao itemFilaCosechaDao();
    public abstract MaterialProcedenciaDao materialProcedenciaDao();
    public abstract TipoMaderaDao tipoMaderaDao();
    public abstract FormatoEntregaDao formatoEntregaDao();
    public abstract OrigenMaderaDao origenMaderaDao();
    public abstract TipoBultoDao tipoBultoDao();
    public abstract FilaSueltoDao filaSueltoDao();
    public abstract TarifaDao tarifaDao();
}
