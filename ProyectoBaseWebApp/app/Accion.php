<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Accion extends Model
{
    protected $fillable = [
        'id', 'descripcion', 'estado'
    ];
    public function scopeActive($query)
    {
        return $query->where('estado', 'A');
    }
    public function roles()
    {
        return $this->belongsToMany('App\Rol', 'permisos', 'accion_id', 'rol_id');
    }
    protected $table = 'acciones';
    public $incrementing = false;
}
