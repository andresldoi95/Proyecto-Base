<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Parametro extends Model
{
    protected $fillable = [
        'factor_hueco', 'constante', 'ancho_bulto', 'empresa_id'
    ];
}
