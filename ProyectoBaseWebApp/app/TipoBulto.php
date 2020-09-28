<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class TipoBulto extends Model
{
    protected $table = 'tipos_bulto';
    protected $fillable = [
        'empresa_id', 'codigo', 'ancho', 'largo_id', 'espesor_id', 'estado', 'creador_id', 'modificador_id'
    ];

    public function largo()
    {
        return $this->belongsTo('App\Largo');
    }

    public function espesor()
    {
        return $this->belongsTo('App\Espesor');
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
