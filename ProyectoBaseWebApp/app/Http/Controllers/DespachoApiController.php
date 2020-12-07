<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\FilaDespacho;
use App\FotoFila;
use App\Troza;
use App\TrozaFotos;
use App\Tarifa;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Pagination\Paginator;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;

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
        ])->orderBy('numero_documento', 'desc')->orderBy('fecha_despacho', 'desc');
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
    public function update(Request $request, $id)
    {
        $request->validate([
            'fecha_tumba' => 'required|date',
            'fecha_despacho' => 'required|date',
            'camion_id' => 'required|exists:camiones,id',
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id',
            'formato_entrega_id' => 'required|exists:formatos_entrega,id'
        ]);

        try {
            $tarifa_new = Tarifa::where('destino_id', $request->input('destino_id') )->where('origen_madera_id', $request->input('origen_madera_id') )->get()->first();
        } catch (\Throwable $th) {
            //throw $th;
        }
        
        $despacho = Despacho::findOrFail($id);
        $despacho->fecha_despacho = $request->input('fecha_despacho');
        $despacho->fecha_tumba = $request->input('fecha_tumba');
        $despacho->camion_id = $request->input('camion_id');
        $despacho->destino_id = $request->input('destino_id');
        $despacho->formato_entrega_id = $request->input('formato_entrega_id');
        $despacho->origen_madera_id = $request->input('origen_madera_id');
        $despacho->valor_flete = $tarifa_new->valor;

        $despacho->save();
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
                     //GPUIG GUARDAR PATH FOTOS FILAS
                    /*$fotos_filas = $fila['fotos'];
                    foreach ($fotos_filas as $fotos_fila) {
                        // The final filename.
                        $fileName = $fotos_fila['id'].".jpg";
                        // Upload path
                        $uploadPath = public_path().'/imagenes/filas/' . $fileName;

                        if(isset($fotos_fila['path'])){
                            // Decode your image/file
                            $decodedImagen = base64_decode($fotos_fila['path']);
                            // Upload the decoded file/image
                            if(!file_put_contents($uploadPath , $decodedImagen)){
                                $uploadPath = NULL;   
                            }
                        }else{
                            $uploadPath = NULL;
                        }

                        $fotos_fila_create = FotoFila::create([
                            'fila_id' => $fotos_fila['filaId'],
                            'path' => $uploadPath
                            
                        ]);  
                        
                    }*/
                }
                //GPUIG GUARDAR PATH FOTOS TROZAS
                $troza = $request->input('troza');
                if (isset($troza)) {
                    $uploadPath = NULL;
                    $troza_creada = Troza::create([
                        'despacho_id' => $despacho->id,
                        'numero_trozas' => $troza['numeroTrozas'],
                        'volumen_estimado' => $troza['volumenEstimado'],
                        'foto' => $uploadPath,
                        'observaciones' => $troza['observaciones'],
                        'id' => $troza['id']
                    ]);  
                }
                //GPUIG GUARDAR PATH FOTOS TROZAS
                /*$troza_fotos = $request->input('troza_fotos');
                foreach ($troza_fotos as $troza_foto) {
                    // The final filename.
                    $fileName = $troza_foto['id'].".jpg";
                    // Upload path
                    $uploadPath = public_path().'/imagenes/trozas/' . $fileName;

                    if(isset($troza_foto['foto'])){
                        // Decode your image/file
                        $decodedImagen = base64_decode($troza_foto['foto']);
                        // Upload the decoded file/image
                        if(!file_put_contents($uploadPath , $decodedImagen)){
                            $uploadPath = NULL;   
                        }
                    }else{
                        $uploadPath = NULL;
                    }

                    $trozas_foto_create = TrozaFotos::create([
                        'troza_id' => $troza_foto['trozaId'],
                        'id' => $troza_foto['id'],
                        'foto' => $uploadPath
                        
                    ]);  
                    
                }*/
            }
            return [
                'numero_documento' => $despacho->numero_documento
            ];
        });
    }
    public function subirFotos(Request $request) {
        $request->validate([
            'fila_id' => 'required|exists:filas_despacho,id',
        ]);
        return DB::transaction(function () use($request) {
            $fila = FilaDespacho::findOrFail($request->input('fila_id'));
            $fotos = $request->file('fotos');
            foreach ($fotos as $foto) {
                $path = Storage::disk('local')->put('public/imgs', $foto);
                $fullpath = Storage::disk('local')->path($path);
                $fila->fotos()->create([
                    'path' => $fullpath
                ]);
            }
        });
    }
    public function subirTrozaFotos(Request $request) {
        $request->validate([
            'troza_id' => 'required',
        ]);
        return DB::transaction(function () use($request) {
            try{
                $troza = Troza::findOrFail($request->input('troza_id'));
                $fotos = $request->file('fotos');
                foreach ($fotos as $foto) {
                    $path = Storage::disk('local')->put('public/imgs', $foto);
                    $fullpath = Storage::disk('local')->path($path);
                    $troza->fotos()->create([
                        'foto' => $fullpath,
                        'id' => $request->input('id')
                    ]);
                }
            }catch(\Exception $e){
                echo $e;
            }
        });
    }
}
