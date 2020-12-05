<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\TipoBulto;
use Illuminate\Support\Facades\DB;

class TipoBultoApiController extends Controller
{
    public function all()
    {
        return TipoBulto::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return TipoBulto::where('empresa_id', $user->empresa_id)
            ->orderBy('codigo')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->where(function ($query) use ($search) {
                return $query->where('codigo', 'like', "%$search%")->orWhere('ancho', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $request->validate([
            'codigo' => 'required|max:255',
            'ancho' => 'required|numeric',
            'largo_id' => 'required|exists:largos,id',
            'espesor_id' => 'required|exists:espesores,id'
        ]);
        $user = $request->user();
        TipoBulto::create([
            'codigo' => $request->input('codigo'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'ancho' => $request->input('ancho'),
            'largo_id' => $request->input('largo_id'),
            'espesor_id' => $request->input('espesor_id')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'codigo' => 'required|max:255',
            'ancho' => 'required|numeric',
            'largo_id' => 'required|exists:largos,id',
            'espesor_id' => 'required|exists:espesores,id'
        ]);
        $tipoBulto = TipoBulto::findOrFail($id);
        $tipoBulto->codigo = $request->input('codigo');
        $tipoBulto->ancho = $request->input('ancho');
        $tipoBulto->modificador_id = $request->user()->id;
        $tipoBulto->espesor_id = $request->input('espesor_id');
        $tipoBulto->largo_id = $request->input('largo_id');
        $tipoBulto->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'tiposBulto' => 'required|array'
        ]);
        $tipoBultos = $request->input('tiposBulto');
        TipoBulto::whereIn('id', $tipoBultos)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
