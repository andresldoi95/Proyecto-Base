<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class FilaSuelto extends Model
{
    protected $table = 'filas_sueltos';
    protected $fillable = [
        'id', 'fila_id', 'espesor_id', 'largo_id', 'indice', 'bft',
        'bultos'
    ];
    public $incrementing = false;
}
