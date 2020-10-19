<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class FilaDespacho extends Model
{
    protected $table = 'filas_despacho';
    protected $fillable = [
        'id', 'despacho_id', 'tipo_bulto_id', 'indice', 'bft',
        'bultos', 'estado', 'tipo'
    ];
    public function sueltos() {
        return $this->hasMany('App\FilaSuelto', 'fila_id');
    }
    public function fotos() {
        return $this->hasMany('App\FotoFila', 'fila_id');
    }
    public $incrementing = false;
}
