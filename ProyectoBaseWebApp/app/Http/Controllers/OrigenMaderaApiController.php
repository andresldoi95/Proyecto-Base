<?php

namespace App\Http\Controllers;

use App\OrigenMadera;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;

class OrigenMaderaApiController extends Controller
{
    public function all()
    {
        return OrigenMadera::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return OrigenMadera::where('empresa_id', $user->empresa_id)
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
            'hectareas' => 'required|numeric',
            'volumen_inventario' => 'required|numeric',
            'anio_cultivo' => 'required|integer',
            'hectareas' => 'required|numeric'
        ]);
        $user = $request->user();
        OrigenMadera::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'hectareas' => $request->input('hectareas'),
            'volumen_inventario' => $request->input('volumen_inventario'),
            'anio_cultivo' => $request->input('anio_cultivo'),
            'hectareas' => $request->input('hectareas')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'hectareas' => 'required|numeric',
            'volumen_inventario' => 'required|numeric',
            'anio_cultivo' => 'required|integer',
            'hectareas' => 'required|numeric'
        ]);
        $origenMadera = OrigenMadera::findOrFail($id);
        $origenMadera->descripcion = $request->input('descripcion');
        $origenMadera->hectareas = $request->input('hectareas');
        $origenMadera->modificador_id = $request->user()->id;
        $origenMadera->anio_cultivo = $request->input('anio_cultivo');
        $origenMadera->hectareas = $request->input('hectareas');
        $origenMadera->volumen_inventario = $request->input('volumen_inventario');
        $origenMadera->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'origenesMadera' => 'required|array'
        ]);
        $origenMaderas = $request->input('origenesMadera');
        OrigenMadera::whereIn('id', $origenMaderas)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
