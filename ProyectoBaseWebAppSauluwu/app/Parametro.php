<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Parametro extends Model
{
    protected $fillable = [
        'factor_hueco_bultos', 'factor_hueco_sueltos', 'empresa_id'
    ];
}
