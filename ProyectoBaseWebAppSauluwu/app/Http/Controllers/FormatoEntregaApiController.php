<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\FormatoEntrega;
use Illuminate\Support\Facades\DB;

class FormatoEntregaApiController extends Controller
{
    public function all()
    {
        return FormatoEntrega::all();
    }
    public function listado(Request $request) {
        $user = $request->user();
        return FormatoEntrega::active()->orderBy('descripcion')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return FormatoEntrega::where('empresa_id', $user->empresa_id)
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
            'factor_hueco' => 'required|numeric'
        ]);
        $user = $request->user();
        FormatoEntrega::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'factor_hueco' => $request->input('factor_hueco')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'factor_hueco' => 'required|numeric'
        ]);
        $formatoEntrega = FormatoEntrega::findOrFail($id);
        $formatoEntrega->descripcion = $request->input('descripcion');
        $formatoEntrega->modificador_id = $request->user()->id;
        $formatoEntrega->factor_hueco = $request->input('factor_hueco');
        $formatoEntrega->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'formatosEntrega' => 'required|array'
        ]);
        $formatoEntregas = $request->input('formatosEntrega');
        FormatoEntrega::whereIn('id', $formatoEntregas)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
