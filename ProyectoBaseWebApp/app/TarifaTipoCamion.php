<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class TarifaTipoCamion extends Model
{
    protected $table = 'tarifa_tipo_camion';
    protected $fillable = [
        'valor','tarifa_id', 'tipo_camion', 'estado', 'creador_id', 'modificador_id'
    ];
    protected $appends = [
        'descripcion_tipo_camion'
    ];
    public function tarifas() {
        return $this->belongsTo('App\Tarifa');
    }
    public function getDescripcionTipoCamionAttribute()
    {
        switch ($this->tipo_camion) {
            case 'B':
                return 'Bananero';
            case 'T':
                return 'Trailer';
            default:
                return 'No definido';
        }
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
}
