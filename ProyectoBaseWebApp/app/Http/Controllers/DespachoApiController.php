<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\FilaDespacho;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class DespachoApiController extends Controller
{
    public function store(Request $request) {
        $request->validate([
            'id' => 'required|max:36|unique:despachos',
            'camion_id' => 'required|exists:camiones,id',
            'controlador_id' => 'required|exists:controladores,id',
            'destino_id' => 'required|exists:destinos,id',
            'aserrador_id' => 'required|exists:aserradores,id',
            'material_id' => 'required|exists:materiales',
            'tipo_madera_id' => 'required|exists:tipos_madera,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id',
            'formato_entrega_id' => 'required|exists:formatos_entrega,id',
            'codigo_po' => 'required|max:20',
            'fecha_tumba' => 'required|date',
            'fecha_despacho' => 'required|date',
            'dias_t2k' => 'required|integer',
            'guia_remision' => 'nullable|max:30',
            'guia_forestal' => 'nullable|max:30',
            'tipo_llenado' => 'required|max:1',
            'valor_flete' => 'required|numeric',
            'estado' => 'required|max:1',
            'filas' => 'required|array',
            'filas.*.tipo_bulto_id' => 'nullable|exists:tipos_bulto,id',
            'filas.*.indice' => 'required|integer',
            'filas.*.bft' => 'required|numeric',
            'filas.*.bultos' => 'required|numeric',
            'filas.*.estado' => 'required|max:1',
            'filas.*.tipo' => 'required|max:1',
            'filas.*.id' => 'required|max:36|distinct',
            'filas.*.sueltos' => 'nullable|array',
            'filas.*.sueltos.*.largo_id' => 'required|exists:largos,id',
            'filas.*.sueltos.*.espesor_id' => 'required|exists:espesores,id',
            'filas.*.sueltos.*.indice' => 'required|integer',
            'filas.*.sueltos.*.bft' => 'required|numeric',
            'filas.*.sueltos.*.bultos' => 'required|numeric'
        ]);
        return DB::transaction(function () use($request) {
            $despacho = Despacho::create([
                'id' => $request->input('despacho'),
                'camion_id' => $request->input('camion_id'),
                'controlador_id' => $request->input('controlador_id'),
                'destino_id' => $request->input('destino_id'),
                'aserrador_id' => $request->input('aserrador_id'),
                'material_id' => $request->input('material_id'),
                'tipo_madera_id' => $request->input('tipo_madera_id'),
                'origen_madera_id' => $request->input('origen_madera_id'),
                'formato_entrega_id' => $request->input('formato_entrega_id'),
                'codigo_po' => $request->input('codigo_po'),
                'fecha_tumba' => new Carbon($request->input('fecha_tumba')),
                'fecha_despacho' => new Carbon($request->input('fecha_despacho')),
                'dias_t2k' => $request->input('dias_t2k'),
                'guia_remision' => $request->input('guia_remision'),
                'guia_forestal' => $request->input('guia_forestal'),
                'tipo_llenado' => $request->input('tipo_llenado'),
                'valor_flete' => $request->input('valor_flete'),
                'estado' => $request->input('estado'),
                'usuario_id' => $request->user()->id
            ]);
            $filas = $request->input('filas');
            foreach ($filas as $fila) {
                $fila = $despacho->filas()->create([
                    'id' => $fila['id'],
                    'despacho_id' => $despacho->id,
                    'tipo_bulto_id' => $fila['tipo_bulto_id'],
                    'indice' => $fila['indice'],
                    'bft' => $fila['bft'],
                    'bultos' => $fila['bultos'],
                    'estado' => $fila['estado'],
                    'tipo' => $fila['tipo']
                ]);
                $sueltos = $fila['sueltos'];
                if (isset($sueltos)) {
                    foreach ($sueltos as $suelto) {
                        $fila->sueltos()->create([
                            'fila_id' => $suelto['fila_id'],
                            'largo_id' => $suelto['largo_id'],
                            'espesor_id' => $suelto['espesor_id'],
                            'indice' => $suelto['indice'],
                            'bft' => $suelto['bft'],
                            'bultos' => $suelto['bultos']
                        ]);
                    }
                }
            }
        });
    }
    public function subirFotos(Request $request) {
        $request->validate([
            'fila_id' => 'required|exists:filas_despacho,id',
            'fotos' => 'required|array'
        ]);
        return DB::transaction(function () use($request) {
            $fila = FilaDespacho::findOrFail($request->input('fila_id'));
            $fotos = $request->file('fotos');
            foreach ($fotos as $foto) {
                $fila->fotos()->create([
                    'path' => $foto->store('fotos')
                ]);
            }
        });
    }
}
