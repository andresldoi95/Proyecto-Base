<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\OrigenHacienda;
use Illuminate\Support\Facades\DB;

class OrigenHaciendaApiController extends Controller
{
    public function all()
    {
        return OrigenHacienda::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return OrigenHacienda::active()->orderBy('descripcion')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return OrigenHacienda::where('empresa_id', $user->empresa_id)
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
            'descripcion' => 'required|max:255'
        ]);
        $user = $request->user();
        OrigenHacienda::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255'
        ]);
        $origenMadera = OrigenHacienda::findOrFail($id);
        $origenMadera->descripcion = $request->input('descripcion');
        $origenMadera->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'origenesHacienda' => 'required|array'
        ]);
        $origenesHacienda = $request->input('origenesHacienda');
        OrigenHacienda::whereIn('id', $origenesHacienda)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
