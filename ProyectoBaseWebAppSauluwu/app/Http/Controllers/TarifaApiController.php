<?php

namespace App\Http\Controllers;

use App\Tarifa;
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
            ->where(function ($query) use ($search) {
                return $query->where('valor', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $user = $request->user();
        $request->validate([
            'valor' => 'required|numeric',
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id'
        ]);
        Tarifa::create([
            'valor' => $request->input('valor'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'destino_id' => $request->input('destino_id'),
            'origen_madera_id' => $request->input('origen_madera_id')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'valor' => 'required|numeric',
            'destino_id' => 'required|exists:destinos,id',
            'origen_madera_id' => 'required|exists:origenes_madera,id'
        ]);
        $tarifa = Tarifa::findOrFail($id);
        $tarifa->valor = $request->input('valor');
        $tarifa->origen_madera_id = $request->input('origen_madera_id');
        $tarifa->destino_id = $request->input('destino_id');
        $tarifa->modificador_id = $user->id;
        $tarifa->origen_madera_id = $request->input('origen_madera_id');
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
