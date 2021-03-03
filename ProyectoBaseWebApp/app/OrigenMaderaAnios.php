<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class OrigenMaderaAnios extends Model
{
    protected $table = 'origenes_madera_anios';
    protected $fillable = [
        'origen_madera_id', 'anio_cultivo','codigo_hacienda','descripcion', 'estado', 'creador_id', 'modificador_id'
    ];
    public function origenMadera() {
        return $this->belongsTo('App\OrigenMadera');
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
