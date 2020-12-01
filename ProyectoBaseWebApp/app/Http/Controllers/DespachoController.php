<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\Espesor;
use App\Largo;
use App\FilaDespacho;
use App\FilaSuelto;
use App\TipoBulto;
use App\User;







use Illuminate\Http\Request;
use PDF;

class DespachoController extends Controller
{
    public function show($id) {
        $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
        return PDF::loadView('despacho', [
            'despacho' => Despacho::findOrFail($id),
            'espesores' => Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'largos' =>Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'filas_despacho' =>FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get(),
            'filas_sueltos' =>FilaSuelto::orderBy('indice')->get(),
            'tipos_bulto' =>TipoBulto::where('empresa_id', $empresa_id)->get(),



        ])->stream();
    }
}
