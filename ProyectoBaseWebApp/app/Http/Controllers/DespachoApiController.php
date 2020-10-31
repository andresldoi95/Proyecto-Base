<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\FilaDespacho;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Pagination\Paginator;
use Illuminate\Support\Facades\DB;

class DespachoApiController extends Controller
{
    public function index(Request $request) {
        $request->validate([
            'desde' => 'required|date',
            'hasta' => 'required|date',
            'search' => 'nullable'
        ]);
        $desde = new Carbon($request->input('desde'));
        $hasta = new Carbon($request->input('hasta'));
        $user = $request->user();
        $despachos = Despacho::with('camion', 'destino', 'origenMadera', 'formatoEntrega', 'usuario')->whereHas('camion', function ($query) use($user) {
            return $query->where('empresa_id', $user->empresa_id);
        })->whereBetween('fecha_despacho', [
            $desde, $hasta
        ])->orderBy('fecha_despacho', 'desc');
        $search = $request->input('search');
        if (isset($search)) {
            $despachos->where(function ($query) use ($search) {
                return $query->where('numero_documento', 'like', "%$search");
            });
        }
        $currentPage = $request->input('current_page');
        Paginator::currentPageResolver(function () use ($currentPage) {
            return $currentPage;
        });
        return $despachos->paginate($request->input('per_page'));
    }
    public function store(Request $request) {
        $request->validate([
            'id' => 'required|max:36',
            'camionId' => 'required|exists:camiones,id',
            'controladorId' => 'required|exists:controladores,id',
            'destinoId' => 'required|exists:destinos,id',
            'aserradorId' => 'required|exists:aserradores,id',
            'materialId' => 'required|exists:materiales,id',
            'tipoMaderaId' => 'required|exists:tipos_madera,id',
            'origenMaderaId' => 'required|exists:origenes_madera,id',
            'formatoEntregaId' => 'required|exists:formatos_entrega,id',
            'codigoPo' => 'required|max:20',
            'fechaTumba' => 'required|date',
            'fechaDespacho' => 'required|date',
            'diasT2k' => 'required|integer',
            'guiaRemision' => 'nullable|max:30',
            'guiaForestal' => 'nullable|max:30',
            'tipoLlenado' => 'required|max:1',
            'valorFlete' => 'required|numeric',
            'estado' => 'required|max:1',
            'filas' => 'required|array',
            'filas.*.indice' => 'required|integer',
            'filas.*.bft' => 'required|numeric',
            'filas.*.bultos' => 'required|numeric',
            'filas.*.estado' => 'required|max:1',
            'filas.*.tipo' => 'required|max:1',
            'filas.*.id' => 'required|max:36|distinct',
            'origenHaciendaId' => 'required|exists:origenes_hacienda,id'
        ]);
        return DB::transaction(function () use($request) {
            $despacho = Despacho::find($request->input('id'));
            if (!isset($despacho)) {
                $ultimoDespacho = Despacho::orderByRaw('CAST(numero_documento AS INT) DESC')->select(['numero_documento'])->first();
                $numeroDocumento = isset($ultimoDespacho)?intval($ultimoDespacho->numero_documento) + 1:1;
                $despacho = Despacho::create([
                    'id' => $request->input('id'),
                    'camion_id' => $request->input('camionId'),
                    'controlador_id' => $request->input('controladorId'),
                    'destino_id' => $request->input('destinoId'),
                    'aserrador_id' => $request->input('aserradorId'),
                    'material_id' => $request->input('materialId'),
                    'tipo_madera_id' => $request->input('tipoMaderaId'),
                    'origen_madera_id' => $request->input('origenMaderaId'),
                    'formato_entrega_id' => $request->input('formatoEntregaId'),
                    'codigo_po' => $request->input('codigoPo'),
                    'fecha_tumba' => new Carbon($request->input('fechaTumba')),
                    'fecha_despacho' => new Carbon($request->input('fechaDespacho')),
                    'dias_t2k' => $request->input('diasT2k'),
                    'guia_remision' => $request->input('guiaRemision'),
                    'guia_forestal' => $request->input('guiaForestal'),
                    'tipo_llenado' => $request->input('tipoLlenado'),
                    'valor_flete' => $request->input('valorFlete'),
                    'estado' => $request->input('estado'),
                    'usuario_id' => $request->user()->id,
                    'origen_hacienda_id' => $request->input('origenHaciendaId'),
                    'numero_documento' => str_pad($numeroDocumento, 8, '0', STR_PAD_LEFT)
                ]);
                $filas = $request->input('filas');
                foreach ($filas as $fila) {
                    $f = $despacho->filas()->create([
                        'id' => $fila['id'],
                        'despacho_id' => $despacho->id,
                        'indice' => $fila['indice'],
                        'bft' => $fila['bft'],
                        'bultos' => $fila['bultos'],
                        'estado' => $fila['estado'],
                        'tipo' => $fila['tipo']
                    ]);
                    $sueltos = $fila['sueltos'];
                    foreach ($sueltos as $suelto) {
                        $f->sueltos()->create([
                            'largo_id' => intval($suelto['largoId']) > 0?$suelto['largoId']:null,
                            'espesor_id' => intval($suelto['espesorId']) > 0?$suelto['espesorId']:null,
                            'indice' => $suelto['indice'],
                            'bft' => $suelto['bft'],
                            'bultos' => $suelto['bultos'],
                            'tipo_bulto_id' => intval($suelto['tipoBultoId']) > 0?$suelto['tipoBultoId']:null,
                            'id' => $suelto['id']
                        ]);
                    }
                }
            }
            return [
                'numero_documento' => $despacho->numero_documento
            ];
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
