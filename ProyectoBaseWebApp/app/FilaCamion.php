<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class FilaCamion extends Model
{
    public $incrementing = false;
    protected $table = 'filas_camion';
    protected $fillable = [
        'filas', 'columnas', 'camion_id', 'id'
    ];
    public function camion()
    {
        return $this->belongsTo('App\Camion');
    }
}
