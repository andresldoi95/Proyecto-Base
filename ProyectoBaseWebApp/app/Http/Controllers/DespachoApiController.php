<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\FilaDespacho;
use App\FotoFila;
use App\Troza;
use App\TrozaFotos;
use App\Tarifa;
use Carbon\Carbon;
use App\Espesor;
use App\Largo;
use App\FilaSuelto;
use App\TipoBulto;
use App\User;
use App\Aserrador;
use App\Material;
use Mail;
use PDF;
use App\Correo;
use Illuminate\Http\Request;
use Illuminate\Pagination\Paginator;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Collection;
use Image;


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
        $camion_filter_id = $request->input('camion_filter_id');
        $destino_filter_id = $request->input('destino_filter_id');
        $origen_madera_filter_id = $request->input('origen_madera_filter_id');

        $user = $request->user();
        $despachos = Despacho::with('aserrador','camion', 'destino', 'origenMadera', 'formatoEntrega', 'usuario'
        )->whereHas('camion', function ($query) use($user) {
            return $query->where('empresa_id', $user->empresa_id);
        })->whereHas('aserrador', function ($query){
            return $query->where('estado', 'A');
        })->whereBetween('fecha_despacho', [
            $desde, $hasta
        ])->orderBy('numero_documento', 'desc')->orderBy('fecha_despacho', 'desc');
        $search = $request->input('search');
        if (isset($search)) {
            $despachos->where(function ($query) use ($search) {
                return $query->where('numero_documento', 'like', "%$search");
            });
        }
        if (isset($camion_filter_id)) {
            $despachos->where(function ($query) use ($camion_filter_id) {
                return $query->where('camion_id', $camion_filter_id);
            });
        }
        if (isset($destino_filter_id)) {
            $despachos->where(function ($query) use ($destino_filter_id) {
                return $query->where('destino_id', $destino_filter_id);
            });
        }
        if (isset($origen_madera_filter_id)) {
            $despachos->where(function ($query) use ($origen_madera_filter_id) {
                return $query->where('origen_madera_id', $origen_madera_filter_id);
            });
        }
        $currentPage = $request->input('current_page');
        Paginator::currentPageResolver(function () use ($currentPage) {
            return $currentPage;
        });

        $despachos = $despachos->paginate($request->input('per_page'));

        foreach($despachos as $despacho){
            $trozas = Troza::where('despacho_id', $despacho->id)->get();
            if($trozas->count()>0){
                $despacho->volumen = number_format($trozas->first()->volumen_estimado,2)." M3";

            }else{
                $despacho->volumen = number_format($despacho->filas()->sum('bft'),2)." BFT";
            }
        }

        return $despachos;
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'fecha_tumba' => 'required|date',
            'fecha_despacho' => 'required|date',
            'camion_id' => 'required|exists:camiones,id',
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id',
            'origen_hacienda_id' => 'required|exists:origenes_hacienda,id'
        ]);

        $tarifa_new_valor = NULL;



        try {
            $tarifa_new = Tarifa::where('destino_id', $request->input('destino_id') )->where('origen_madera_id', $request->input('origen_madera_id') )->get()->first();
            if($tarifa_new){
                $tarifa_new_valor = $tarifa_new->valor;
            }
        } catch (\Throwable $th) {
            //throw $th;
        }
        
        $despacho = Despacho::findOrFail($id);
        $despacho->fecha_despacho = $request->input('fecha_despacho');
        $despacho->fecha_tumba = $request->input('fecha_tumba');
        $despacho->camion_id = $request->input('camion_id');
        $despacho->destino_id = $request->input('destino_id');
        $despacho->origen_madera_id = $request->input('origen_madera_id');
        $despacho->origen_hacienda_id = $request->input('origen_hacienda_id');
        $despacho->valor_flete = $tarifa_new_valor;

        $despacho->save();
    }
    public function store(Request $request) {

        try {
            $request->validate([
                'id' => 'required|max:36',
                'camionId' => 'required|exists:camiones,id',
                'destinoId' => 'required|exists:destinos,id',
                'aserradorId' => 'required|exists:aserradores,id',
                'materialId' => 'required|exists:materiales,id',
                'tipoMaderaId' => 'required|exists:tipos_madera,id',
                'origenMaderaAnioId' => 'required|exists:origenes_madera_anios,id',
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
                        'destino_id' => $request->input('destinoId'),
                        'aserrador_id' => $request->input('aserradorId'),
                        'material_id' => $request->input('materialId'),
                        'tipo_madera_id' => $request->input('tipoMaderaId'),
                        'origen_madera_id' => $request->input('origenMaderaId'),
                        'origen_madera_anio_id' => $request->input('origenMaderaAnioId'),
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
                }
                return [
                    'status' => true,
                    'numero_documento' => $despacho->numero_documento
                ];
            });
        } catch(\Exception $e){
            $error = $e->getMessage();
            return [
                'status' => false,
                'response' => 'No se ha subido el Despacho.',
                'error' => $error
            ];
        }
        
    }
    public function historialUpdate(Request $request) {
        try {
            $request->validate([
                'id' => 'required|max:36',
                'camionId' => 'required|exists:camiones,id',
                'destinoId' => 'required|exists:destinos,id',
                'aserradorId' => 'required|exists:aserradores,id',
                'materialId' => 'required|exists:materiales,id',
                'tipoMaderaId' => 'required|exists:tipos_madera,id',
                'origenMaderaAnioId' => 'required|exists:origenes_madera_anios,id',
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
                        'destino_id' => $request->input('destinoId'),
                        'aserrador_id' => $request->input('aserradorId'),
                        'material_id' => $request->input('materialId'),
                        'tipo_madera_id' => $request->input('tipoMaderaId'),
                        'origen_madera_id' => $request->input('origenMaderaId'),
                        'origen_madera_anio_id' => $request->input('origenMaderaAnioId'),
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
                }else{
    
                    $despacho = Despacho::find($request->input('id'));
                    $despacho->camion_id = $request->input('camionId');
                    $despacho->destino_id = $request->input('destinoId');
                    $despacho->aserrador_id = $request->input('aserradorId');
                    $despacho->material_id = $request->input('materialId');
                    $despacho->tipo_madera_id = $request->input('tipoMaderaId');
                    $despacho->origen_madera_id = $request->input('origenMaderaId');
                    $despacho->origen_madera_anio_id = $request->input('origenMaderaAnioId');
                    $despacho->formato_entrega_id = $request->input('formatoEntregaId');
                    $despacho->codigo_po = $request->input('codigoPo');
                    $despacho->fecha_tumba = new Carbon($request->input('fechaTumba'));
                    $despacho->fecha_despacho = new Carbon($request->input('fechaDespacho'));
                    $despacho->dias_t2k = $request->input('diasT2k');
                    $despacho->guia_remision = $request->input('guiaRemision');
                    $despacho->guia_forestal = $request->input('guiaForestal');
                    $despacho->tipo_llenado = $request->input('tipoLlenado');
                    $despacho->valor_flete = $request->input('valorFlete');
                    $despacho->estado = $request->input('estado');
                    $despacho->usuario_id = $request->user()->id;
                    $despacho->origen_hacienda_id = $request->input('origenHaciendaId');
                    $despacho->save();
    
                    $filas = $request->input('filas');
                    foreach ($filas as $fila) {
    
                        $filaDesp = FilaDespacho::find($fila['id']);
    
                        if (!isset($filaDesp)){
                            $f = $despacho->filas()->create([
                                'id' => $fila['id'],
                                'despacho_id' => $despacho->id,
                                'indice' => $fila['indice'],
                                'bft' => $fila['bft'],
                                'bultos' => $fila['bultos'],
                                'estado' => $fila['estado'],
                                'tipo' => $fila['tipo']
                            ]);
                        }else{
                            $filaDesp->despacho_id = $despacho->id;
                            $filaDesp->indice = $fila['indice'];
                            $filaDesp->bft = $fila['bft'];
                            $filaDesp->bultos = $fila['bultos'];
                            $filaDesp->estado = $fila['estado'];
                            $filaDesp->tipo = $fila['tipo'];
                            $filaDesp->save();
                        }
                        
                        $sueltos = $fila['sueltos'];
                        foreach ($sueltos as $suelto) {
                            $sueltoDesp = FilaSuelto::find($suelto['id']);
    
                            if (!isset($sueltoDesp)){
                                $f->sueltos()->create([
                                    'largo_id' => intval($suelto['largoId']) > 0?$suelto['largoId']:null,
                                    'espesor_id' => intval($suelto['espesorId']) > 0?$suelto['espesorId']:null,
                                    'indice' => $suelto['indice'],
                                    'bft' => $suelto['bft'],
                                    'bultos' => $suelto['bultos'],
                                    'tipo_bulto_id' => intval($suelto['tipoBultoId']) > 0?$suelto['tipoBultoId']:null,
                                    'id' => $suelto['id']
                                ]);
                            }else{
                                $sueltoDesp->largo_id = intval($suelto['largoId']) > 0?$suelto['largoId']:null;
                                $sueltoDesp->espesor_id = intval($suelto['espesorId']) > 0?$suelto['espesorId']:null;
                                $sueltoDesp->indice = $fila['indice'];
                                $sueltoDesp->bft = $fila['bft'];
                                $sueltoDesp->bultos = $fila['bultos'];
                                $sueltoDesp->tipo_bulto_id = intval($suelto['tipoBultoId']) > 0?$suelto['tipoBultoId']:null;
                                $sueltoDesp->save();
    
                            }
                            
                        }
                    }
                    //GPUIG GUARDAR PATH FOTOS TROZAS
                    $troza = $request->input('troza');
                    if (isset($troza)) {
                        $uploadPath = NULL;
    
                        $trozaDesp = Troza::find($troza['id']);
    
                            if (!isset($trozaDesp)){
                                $troza_creada = Troza::create([
                                    'despacho_id' => $despacho->id,
                                    'numero_trozas' => $troza['numeroTrozas'],
                                    'volumen_estimado' => $troza['volumenEstimado'],
                                    'foto' => $uploadPath,
                                    'observaciones' => $troza['observaciones'],
                                    'id' => $troza['id']
                                ]); 
                            }else{
                                $trozaDesp->despacho_id = $despacho->id;
                                $trozaDesp->numero_trozas =  $troza['numeroTrozas'];
                                $trozaDesp->volumen_estimado = $troza['volumenEstimado'];
                                $trozaDesp->foto = $uploadPath;
                                $trozaDesp->observaciones = $troza['observaciones'];
                                $trozaDesp->save();
                            }
                         
                    } 
    
    
                }
                return [
                    'status' => true,
                    'numero_documento' => $despacho->numero_documento
                ];
            });
        } catch(\Exception $e){
            $error = $e->getMessage();
            return [
                'status' => false,
                'response' => 'No se ha subido el Despacho.',
                'error' => $error
            ];
        }
        
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
                /*$file = $foto;
                $path = storage_path('app/public').'/imgs';
                $fileName = uniqid().".jpg";
                $fullpath = $path.'/'. $fileName;*/

                /*$moved=  Image::make($foto)
                ->resize(720, null, function ($constraint) {
                    $constraint->aspectRatio();
                    $constraint->upsize();
                })
                ->save($path.'/'.$fileName, 75);*/

                /*$moved=  Image::make($foto)
                ->save($path.'/'.$fileName, 25);

                if ($moved) {
                    $fila->fotos()->create([
                        'path' => $fullpath
                    ]);
                    
                }*/
                
            }
        });
    }
    public function subirFotosUpdate(Request $request) {
        $request->validate([
            'fila_id' => 'required|exists:filas_despacho,id',
        ]);
        return DB::transaction(function () use($request) {
            $fila = FilaDespacho::findOrFail($request->input('fila_id'));
            $fotos = $request->file('fotos');
            foreach ($fotos as $foto) {
                $path = Storage::disk('local')->put('public/imgs', $foto);
                $fullpath = Storage::disk('local')->path($path);
                $fotoFila = FotoFila::where('path', $fullpath)->get();

                if($fotoFila->count()==0){
                    $fila->fotos()->create([
                        'path' => $fullpath
                    ]);  
                }                            
            }

        });
        
    }
    public function subirTrozaFotos(Request $request) {
        $request->validate([
            'troza_id' => 'required',
        ]);
        return DB::transaction(function () use($request) {
                $troza = Troza::findOrFail($request->input('troza_id'));
                $fotos = $request->file('fotos');
                foreach ($fotos as $foto) {
                    $path = Storage::disk('local')->put('public/imgs', $foto);
                    $fullpath = Storage::disk('local')->path($path);
                    $troza->fotos()->create([
                        'foto' => $fullpath,
                        'id' => $request->input('id')
                    ]);
                    /*$file = $foto;
                    $path = storage_path('app/public').'/imgs';
                    $fileName = $request->input('id')."-".uniqid().".jpg";
                    $fullpath = $path.'/'. $fileName;*/

                    /*$moved=  Image::make($foto)
                    ->resize(720, null, function ($constraint) {
                        $constraint->aspectRatio();
                        $constraint->upsize();
                    })
                    ->save($path.'/'.$fileName, 75);*/

                    /*$moved=  Image::make($foto)
                    ->save($path.'/'.$fileName, 25);

                    if ($moved) {
                        $troza->fotos()->create([
                            'foto' => $fullpath,
                            'id' => $request->input('id')
                        ]);
                        
                    }*/                   
                }
        });
    }
    public function subirTrozaFotosUpdate(Request $request) {
        $request->validate([
            'troza_id' => 'required',
        ]);
        return DB::transaction(function () use($request) {
                $troza = Troza::findOrFail($request->input('troza_id'));
                $fotos = $request->file('fotos');
                foreach ($fotos as $foto) {
                    $path = Storage::disk('local')->put('public/imgs', $foto);
                    $fullpath = Storage::disk('local')->path($path);

                    $fotoTroza = TrozaFotos::where('id', $foto->id)->get();

                    if($fotoTroza->count()==0){
                        $troza->fotos()->create([
                            'foto' => $fullpath,
                            'id' => $request->input('id')
                        ]);
                    }    
                    
               
                }
        });
    }
}
