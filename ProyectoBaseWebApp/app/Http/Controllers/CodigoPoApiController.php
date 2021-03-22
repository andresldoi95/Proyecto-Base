<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\CodigoPo;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\DB;

class CodigoPoApiController extends Controller
{
    public function all()
    {
        return CodigoPo::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return CodigoPo::active()->orderBy('descripcion')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return CodigoPo::with('material', 'destino','origenMaderaAnio')->where('empresa_id', $user->empresa_id)
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
            'descripcion' => [
                'required', 'max:255', Rule::unique('codigos_po')->where('empresa_id', $user->empresa_id)
            ],
            'material_id' => 'required',
            'origen_madera_anio_id' => 'required|exists:origenes_madera_anios,id',

            'destino_id' => 'required|exists:destinos,id'
        ]);
        CodigoPo::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'material_id' => $request->input('material_id'),
            'origen_madera_anio_id' => $request->input('origen_madera_anio_id'),

            'destino_id' => $request->input('destino_id')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'descripcion' => [
                'required', 'max:255', Rule::unique('codigos_po')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'material_id' => 'required',
            'origen_madera_anio_id' => 'required|exists:origenes_madera_anios,id',
            'destino_id' => 'required|exists:destinos,id'
        ]);
        $material = CodigoPo::findOrFail($id);
        $material->descripcion = $request->input('descripcion');
        $material->modificador_id = $user->id;
        $material->material_id = $request->input('material_id');
        $material->origen_madera_anio_id = $request->input('origen_madera_anio_id');

        $material->destino_id = $request->input('destino_id');
        $material->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'codigos_po' => 'required|array'
        ]);
        $codigos_po = $request->input('codigos_po');
        CodigoPo::whereIn('id', $codigos_po)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
