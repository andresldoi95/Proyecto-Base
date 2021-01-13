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

class DespachoPendientePagoApiController extends Controller
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
        $despachos->where(function ($query) use ($origen_madera_filter_id) {
            return $query->where('pago_aserrado',NULL)->orWhere('pago_transporte', NULL);
        });

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
        



        
        
        $despacho = Despacho::findOrFail($id);
        $despacho->pago_aserrado = $request->input('pago_aserrado');
        $despacho->pago_transporte = $request->input('pago_transporte');
        
        $despacho->save();
    }

}
