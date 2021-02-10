<?php

namespace App\Http\Controllers;

use App\Tarifa;
use App\TarifaTipoCamion;
use App\Camion;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class TarifaApiController extends Controller
{
    public function all()
    {
        return Tarifa::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return Tarifa::active()->orderBy('valor')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Tarifa::with('origenMadera', 'destino')->where('empresa_id', $user->empresa_id)
            ->orderBy('valor')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->get();
    }
    public function store(Request $request)
    {
        $user = $request->user();
        $request->validate([
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id'
        ]);
        Tarifa::create([
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'destino_id' => $request->input('destino_id'),
            'origen_madera_id' => $request->input('origen_madera_id')
        ]);
    }
    public function getTarifaFlete(Request $request)
    {
        $tarifa_flete = "";
        try {
            $camion = Camion::findOrFail($request->input('camion_id'));

            if($camion!=NULL){
                $TarifaModel = Tarifa::where('destino_id', $request->input('destino_id') )->where('origen_madera_id', $request->input('origen_madera_id') )->get()->first();
                $tarifa_new = TarifaTipoCamion::where('tarifa_id', $TarifaModel->id )->where('tipo_camion', $camion->tipo_camion)->get()->first();

                if( $tarifa_new){
                    $tarifa_flete = $tarifa_new->valor;
                }
            }

            
            
        } catch (\Throwable $th) {
            //throw $th;
        }

        return $tarifa_flete;
    }

    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id'
        ]);
        $tarifa = Tarifa::findOrFail($id);
        $tarifa->origen_madera_id = $request->input('origen_madera_id');
        $tarifa->destino_id = $request->input('destino_id');
        $tarifa->modificador_id = $user->id;
        $tarifa->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'tarifas' => 'required|array'
        ]);
        $tarifas = $request->input('tarifas');
        Tarifa::whereIn('id', $tarifas)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
