<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Troza extends Model
{
    protected $fillable = [
        'id', 'despacho_id', 'numero_trozas', 'volumen_estimado', 'observaciones'
    ];
    public $incrementing = false;
    public function despacho()  {
        return $this->belongsTo('App\Despacho');
    }
    public function fotos() {
        return $this->hasMany('App\TrozaFotos', 'troza_id');
    }
}
