<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class FotoFila extends Model
{
    protected $fillable = [
        'path', 'fila_id'
    ];
    protected $table = 'fotos_fila';
    public $timestamps = false;
    public $incrementing = false;
    protected $primaryKey = null;
}
