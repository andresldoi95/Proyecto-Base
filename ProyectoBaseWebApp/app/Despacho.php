<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Despacho extends Model
{
    protected $fillable = [
        'id', 'camion_id', 'controlador_id', 'destino_id', 'aserrador_id', 'material_id', 'tipo_madera_id',
        'origen_madera_id', 'formato_entrega_id', 'codigo_po', 'fecha_tumba', 'fecha_despacho',
        'dias_t2k', 'guia_remision', 'guia_forestal', 'guia_forestal', 'tipo_llenado',
        'valor_flete', 'estado', 'usuario_id', 'origen_hacienda_id'
    ];
    public $incrementing = false;
    protected $dates = [
        'fecha_tumba', 'fecha_despacho'
    ];
    public function filas() {
        return $this->hasMany('App\FilaDespacho');
    }
}
