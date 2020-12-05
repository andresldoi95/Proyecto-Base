<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Camion extends Model
{
    protected $table = 'camiones';
    protected $fillable = [
        'tipo_camion', 'ancho', 'placa', 'alto', 'empresa_id', 'camionero', 'identificacion_camionero', 'estado', 'creador_id', 'modificador_id',
        'codigo_vendor', 'filas'
    ];
    protected $appends = [
        'descripcion_tipo_camion'
    ];
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
    public function scopeCurrent($query, $empresaId)
    {
        return $query->where('empresa_id', $empresaId);
    }
}
