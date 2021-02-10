<?php

namespace App\Http\Controllers;

use App\TarifaTipoCamion;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;

class TarifaTipoCamionApiController extends Controller
{
    public function all()
    {
        return TarifaTipoCamion::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return TarifaTipoCamion::active()->orderBy('valor')->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        $tarifa_id_filter = $request->input('tarifa_id_filter');
        $tarifaTipoCamion= TarifaTipoCamion::with('tarifas')->orderBy('valor')
        ->where(function ($query) use ($status) {
            if ($status !== 'T')
                return $query->where('estado', $status);
            else
                return $query;
        })
        ->where(function ($query) use ($tarifa_id_filter) {
            return $query->where('tarifa_id', $tarifa_id_filter);
        })
        ->where(function ($query) use ($search) {
            return $query->where('valor', 'like', "%$search%")->orWhere('valor', 'like', "%$search%");
        })
        ->get();

        


        return $tarifaTipoCamion;
    }
    public function store(Request $request)
    {
        $request->validate([
            'valor' => 'required|numeric',
            'tarifa_id' => 'required'
        ]);
        $user = $request->user();
        TarifaTipoCamion::create([
            'tipo_camion' => $request->input('tipo_camion'),
            'tarifa_id' => $request->input('tarifa_id'),
            'valor' => $request->input('valor'),
            'creador_id' => $user->id
            
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'valor' => 'required|max:255',
            'tarifa_id' => 'required'
        ]);
        $TarifaTipoCamion = TarifaTipoCamion::findOrFail($id);
        $TarifaTipoCamion->valor = $request->input('valor');
        $TarifaTipoCamion->tipo_camion = $request->input('tipo_camion');
        $TarifaTipoCamion->tarifa_id = $request->input('tarifa_id');
        $TarifaTipoCamion->modificador_id = $request->user()->id;
        $TarifaTipoCamion->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'tarifas' => 'required|array'
        ]);
        $TarifaTipoCamions = $request->input('tarifas');
        TarifaTipoCamion::whereIn('id', $TarifaTipoCamions)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
