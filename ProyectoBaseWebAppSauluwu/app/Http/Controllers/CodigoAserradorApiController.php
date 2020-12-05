<?php

namespace App\Http\Controllers;

use App\CodigoAserrador;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class CodigoAserradorApiController extends Controller
{
    public function all()
    {
        return CodigoAserrador::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return CodigoAserrador::with('aserrador')->where('empresa_id', $user->empresa_id)
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
        $user = $request->user();
        $request->validate([
            'descripcion' => 'required|max:255',
            'codigo' => [
                'required', 'max:20', Rule::unique('codigos_aserradores')->where('empresa_id', $user->empresa_id)
            ],
            'aserrador_id' => 'required|exists:aserradores,id'
        ]);
        CodigoAserrador::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'codigo' => $request->input('codigo'),
            'aserrador_id' => $request->input('aserrador_id')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'descripcion' => 'required|max:255',
            'codigo' => [
                'required', 'max:20', Rule::unique('codigos_aserradores')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'aserrador_id' => 'required|exists:aserradores,id'
        ]);
        $codigoAserrador = CodigoAserrador::findOrFail($id);
        $codigoAserrador->descripcion = $request->input('descripcion');
        $codigoAserrador->codigo = $request->input('codigo');
        $codigoAserrador->modificador_id = $user->id;
        $codigoAserrador->aserrador_id = $request->input('aserrador_id');
        $codigoAserrador->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'codigosAserradores' => 'required|array'
        ]);
        $codigosAserradores = $request->input('codigosAserradores');
        CodigoAserrador::whereIn('id', $codigosAserradores)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
