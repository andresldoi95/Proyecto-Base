<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class TrozaFotos extends Model
{
    protected $fillable = [
        'foto', 'troza_id', 'id'
    ];
    protected $table = 'troza_fotos';
    public $timestamps = false;
    public $incrementing = false;
    protected $primaryKey = null;
}
