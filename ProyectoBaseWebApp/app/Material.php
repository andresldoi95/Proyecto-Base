<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Material extends Model
{
    protected $table = 'materiales';
    protected $fillable = [
        'empresa_id', 'descripcion', 'codigo', 'estado', 'creador_id', 'modificador_id', 'tipo_madera_id',
        'formato_entrega_id'
    ];
    public function tipoMadera() {
        return $this->belongsTo('App\TipoMadera', 'tipo_madera_id');
    }
    public function formatoEntrega() {
        return $this->belongsTo('App\FormatoEntrega', 'formato_entrega_id');
    }
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
}
