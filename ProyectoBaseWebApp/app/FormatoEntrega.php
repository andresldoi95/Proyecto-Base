<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class FormatoEntrega extends Model
{
    protected $fillable = [
        'empresa_id', 'descripcion', 'estado', 'creador_id', 'modificador_id', 'factor_hueco_bultos', 'factor_hueco_sueltos'
    ];
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
    public function scopeCurrent($query, $empresaId)
    {
        return $query->where('empresa_id', $empresaId);
    }
    protected $table = 'formatos_entrega';
}
