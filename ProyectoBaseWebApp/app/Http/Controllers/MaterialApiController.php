<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Material;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\DB;

class MaterialApiController extends Controller
{
    public function all()
    {
        return Material::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Material::where('empresa_id', $user->empresa_id)
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
                'required', 'max:20', Rule::unique('materiales')->where('empresa_id', $user->empresa_id)
            ],
            'email' => 'nullable|max:1000'
        ]);
        Material::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'codigo' => $request->input('codigo')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'descripcion' => 'required|max:255',
            'codigo' => [
                'required', 'max:20', Rule::unique('materiales')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'email' => 'nullable|max:1000'
        ]);
        $material = Material::findOrFail($id);
        $material->descripcion = $request->input('descripcion');
        $material->codigo = $request->input('codigo');
        $material->modificador_id = $user->id;
        $material->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'materiales' => 'required|array'
        ]);
        $materiales = $request->input('materiales');
        Material::whereIn('id', $materiales)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
