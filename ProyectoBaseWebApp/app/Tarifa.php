<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Tarifa extends Model
{
    protected $fillable = [
        'tipo_camion','empresa_id', 'origen_madera_id', 'destino_id', 'valor', 'creador_id',
        'modificador_id'
    ];
    protected $appends = [
        'descripcion_tipo_camion'
    ];
    public function origenMadera() {
        return $this->belongsTo('App\OrigenMadera');
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
    public function destino() {
        return $this->belongsTo('App\Destino');
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
