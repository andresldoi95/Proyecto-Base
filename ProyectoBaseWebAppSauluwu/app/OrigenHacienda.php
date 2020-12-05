<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class OrigenHacienda extends Model
{
    protected $table = 'origenes_hacienda';
    protected $fillable = [
        'empresa_id', 'descripcion', 'estado', 'creador_id', 'modificador_id'
    ];
    public function haciendas() {
        return $this->belongsToMany('App\OrigenMadera', 'haciendas_madera', 'origen_madera_id' , 'hacienda_id');
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