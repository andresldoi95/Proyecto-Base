<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CodigoAserrador extends Model
{
    protected $table = 'codigos_aserradores';
    protected $fillable = [
        'aserrador_id', 'codigo', 'empresa_id', 'descripcion', 'estado', 'creador_id',
        'modificador_id'
    ];
    public function aserrador()
    {
        return $this->belongsTo('App\Aserrador');
    }
    public function scopeActive($query)
    {
        return $query->where('estado', 'A');
    }
}
