<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class OrigenMadera extends Model
{
    protected $table = 'origenes_madera';
    protected $fillable = [
        'empresa_id', 'descripcion', 'anio_cultivo', 'volumen_inventario', 'hectareas', 'estado', 'creador_id', 'modificador_id'
    ];
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