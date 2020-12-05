<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Tarifa extends Model
{
    protected $fillable = [
        'empresa_id', 'origen_madera_id', 'destino_id', 'valor', 'creador_id',
        'modificador_id'
    ];
    public function origenMadera() {
        return $this->belongsTo('App\OrigenMadera');
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
