<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Espesor;
use Illuminate\Support\Facades\DB;

class EspesorApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Espesor::where('empresa_id', $user->empresa_id)
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
            'valor' => 'required|numeric',
            'color' => 'required|max:7'
        ]);
        $user = $request->user();
        Espesor::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'valor' => $request->input('valor'),
            'color' => $request->input('color')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'valor' => 'required|numeric',
            'color' => 'required|max:7'
        ]);
        $espesor = Espesor::findOrFail($id);
        $espesor->descripcion = $request->input('descripcion');
        $espesor->valor = $request->input('valor');
        $espesor->modificador_id = $request->user()->id;
        $espesor->color = $request->input('color');
        $espesor->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'espesores' => 'required|array'
        ]);
        $espesores = $request->input('espesores');
        Espesor::whereIn('id', $espesores)
            ->update([
                'estado' => DB::raw('if(estado = "A", "I", "A")')
            ]);
    }
}
