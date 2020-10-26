<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Aserrador extends Model
{
    protected $table = 'aserradores';
    protected $fillable = [
        'empresa_id', 'nombre', 'identificacion', 'estado', 'creador_id', 'modificador_id'
    ];
    public function procedencia()
    {
        return $this->belongsTo('App\Procedencia');
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
