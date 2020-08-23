<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Controlador;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\DB;

class ControladorApiController extends Controller
{
    public function all()
    {
        return Controlador::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Controlador::where('empresa_id', $user->empresa_id)
            ->orderBy('nombre')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->where(function ($query) use ($search) {
                return $query->where('nombre', 'like', "%$search%")->orWhere('nombre', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $user = $request->user();
        $request->validate([
            'nombre' => 'required|max:255',
            'identificacion' => [
                'required', 'max:255', Rule::unique('controladores')->where('empresa_id', $user->empresa_id)
            ]
        ]);
        Controlador::create([
            'nombre' => $request->input('nombre'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'identificacion' => $request->input('identificacion')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'nombre' => 'required|max:255',
            'identificacion' => [
                'required', 'max:255', Rule::unique('controladores')->where('empresa_id', $user->empresa_id)->ignore($id)
            ]
        ]);
        $controlador = Controlador::findOrFail($id);
        $controlador->nombre = $request->input('nombre');
        $controlador->identificacion = $request->input('identificacion');
        $controlador->modificador_id = $user->id;
        $controlador->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'controladores' => 'required|array'
        ]);
        $controladores = $request->input('controladores');
        Controlador::whereIn('id', $controladores)
            ->update([
                'estado' => DB::raw('if(estado = "A", "I", "A")')
            ]);
    }
}
