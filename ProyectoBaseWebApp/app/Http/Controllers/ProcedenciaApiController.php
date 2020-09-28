<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Procedencia;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class ProcedenciaApiController extends Controller
{
    public function all()
    {
        return Procedencia::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return Procedencia::with('materiales')->active()->orderBy('descripcion')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Procedencia::with('materiales')->where('empresa_id', $user->empresa_id)
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
                'required', 'max:20', Rule::unique('procedencias')->where('empresa_id', $user->empresa_id)
            ],
            'email' => 'nullable|max:1000',
            'materiales' => 'nullable|array'
        ]);
        $procedencia = Procedencia::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'codigo' => $request->input('codigo'),
            'email' => $request->input('email')
        ]);
        $procedencia->materiales()->sync($request->input('materiales'));
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'descripcion' => 'required|max:255',
            'codigo' => [
                'required', 'max:20', Rule::unique('procedencias')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'email' => 'nullable|max:1000',
            'materiales' => 'nullable|array'
        ]);
        $procedencia = Procedencia::findOrFail($id);
        $procedencia->descripcion = $request->input('descripcion');
        $procedencia->codigo = $request->input('codigo');
        $procedencia->modificador_id = $user->id;
        $procedencia->email = $request->input('email');
        $procedencia->save();
        $procedencia->materiales()->sync($request->input('materiales'));
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'procedencias' => 'required|array'
        ]);
        $procedencias = $request->input('procedencias');
        Procedencia::whereIn('id', $procedencias)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
