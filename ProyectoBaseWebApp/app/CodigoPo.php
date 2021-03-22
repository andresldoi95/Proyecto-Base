<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use App\Material;

class CodigoPo extends Model
{
    protected $table = 'codigos_po';
    protected $fillable = [
        'empresa_id', 'descripcion', 'destino_id', 'material_id', 'origen_madera_anio_id', 'estado', 'creador_id', 'modificador_id'
    ];

    protected $appends = [
        'descripcion_material'
    ];

    public function getDescripcionMaterialAttribute()
    {
        if($this->material_id==0){
            return 'Todos';
        }elseif($this->material_id>0){

            $material_descripcion = "";

            try {
                $material = Material::findOrFail($this->material_id);
                $material_descripcion = $material->descripcion;
            } catch (\Throwable $th) {
            }
            return $material_descripcion;

        }else{
            return 'No definido';

        }
    }
    public function material() {
        return $this->belongsTo('App\Material', 'material_id');
    }
    public function destino() {
        return $this->belongsTo('App\Destino', 'destino_id');
    }
    public function origenMaderaAnio() {
        return $this->belongsTo('App\OrigenMaderaAnios', 'origen_madera_anio_id');
    }
    public function scopeActive($query)
    {
        return $query->where('estado', 'A');
    }
    public function creador()
    {
        return $this->belongsTo('App\User');
    }
    public function modificador()
    {
        return $this->belongsTo('App\User');
    }
    public function scopeCurrent($query, $empresaId)
    {
        return $query->where('empresa_id', $empresaId);
    }
}
