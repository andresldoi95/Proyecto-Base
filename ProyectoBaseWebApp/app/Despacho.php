<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;


class Despacho extends Model
{
    protected $fillable = [
        'id', 'camion_id', 'destino_id', 'aserrador_id', 'material_id', 'tipo_madera_id',
        'origen_madera_id','origen_madera_anio_id', 'formato_entrega_id', 'codigo_po', 'fecha_tumba', 'fecha_despacho',
        'dias_t2k', 'guia_remision', 'guia_forestal', 'guia_forestal', 'tipo_llenado',
        'valor_flete', 'estado', 'usuario_id', 'origen_hacienda_id', 'numero_documento','volumen'
    ];
    public function camion() {
        return $this->belongsTo('App\Camion');
    }
    public function destino() {
        return $this->belongsTo('App\Destino');
    }
    public function aserrador() {
        return $this->belongsTo('App\Aserrador');
    }
    public function material() {
        return $this->belongsTo('App\Material');
    }
    public function tipoMadera() {
        return $this->belongsTo('App\TipoMadera');
    }
    public function origenMadera() {
        return $this->belongsTo('App\OrigenMadera');
    }
    public function origenMaderaAnio() {
        return $this->belongsTo('App\OrigenMaderaAnios');
    }
    public function origenHacienda() {
        return $this->belongsTo('App\OrigenHacienda');
    }
    public function usuario() {
        return $this->belongsTo('App\User');
    }
    public function formatoEntrega() {
        return $this->belongsTo('App\FormatoEntrega');
    }
    public $incrementing = false;
    protected $dates = [
        'fecha_tumba', 'fecha_despacho'
    ];
    public function filas() {
        return $this->hasMany('App\FilaDespacho');
    }

    public function getFechaTumbaAttribute($value) {
        return (new Carbon($value))->format('Y-m-d');
    }
    public function getFechaDespachoAttribute($value) {
        return (new Carbon($value))->format('Y-m-d');
    }

    /*public function getVolumenAttribute($value) {
        return "123";
    }*/

    public function getGuiaRemisionAttribute($value) {
        return substr($value,0,3)."-".substr($value,3,3)."-".substr($value,6,9);
    }
}
