<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\TipoMadera;
use Illuminate\Support\Facades\DB;

class TipoMaderaApiController extends Controller
{
    public function all()
    {
        return TipoMadera::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return TipoMadera::where('empresa_id', $user->empresa_id)
            ->orderBy('descripcion')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->where(function ($query) use ($search) {
                return $query->where('descripcion', 'like', "%$search%")->orWhere('descripcion', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'valor' => 'required|numeric'
        ]);
        $user = $request->user();
        TipoMadera::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'valor' => $request->input('valor')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'valor' => 'required|numeric'
        ]);
        $tipoMadera = TipoMadera::findOrFail($id);
        $tipoMadera->descripcion = $request->input('descripcion');
        $tipoMadera->valor = $request->input('valor');
        $tipoMadera->modificador_id = $request->user()->id;
        $tipoMadera->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'tiposMadera' => 'required|array'
        ]);
        $tipoMaderas = $request->input('tiposMadera');
        TipoMadera::whereIn('id', $tipoMaderas)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
