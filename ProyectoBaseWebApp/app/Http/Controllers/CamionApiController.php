<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Camion;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\DB;

class CamionApiController extends Controller
{
    public function all()
    {
        return Camion::all();
    }
    public function listado(Request $request) {
        $user = $request->user();
        return Camion::active()->orderBy('placa')->where('empresa_id', $user->empresa_id)->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Camion::where('empresa_id', $user->empresa_id)
            ->orderBy('camionero')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->where(function ($query) use ($search) {
                return $query->where('camionero', 'like', "%$search%")->orWhere('camionero', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $user = $request->user();
        $request->validate([
            'camionero' => 'required|max:255',
            'identificacion_camionero' => [
                'required', 'max:255', Rule::unique('camiones')->where('empresa_id', $user->empresa_id)
            ],
            'tipo_camion' => 'required|max:1',
            'alto' => 'required|numeric',
            'ancho' => 'required|numeric',
            'placa' => 'required|max:10',
            'filas.*.filas' => 'required|integer',
            'filas.*.columnas' => 'required|integer',
            'codigo_vendor' => 'required|max:50'
        ]);
        $camion = Camion::create([
            'tipo_camion' => $request->input('tipo_camion'),
            'camionero' => $request->input('camionero'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'identificacion_camionero' => $request->input('identificacion_camionero'),
            'ancho' => $request->input('ancho'),
            'alto' => $request->input('alto'),
            'placa' => $request->input('placa'),
            'codigo_vendor' => $request->input('codigo_vendor'),
            'filas' => $request->input('filas')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'camionero' => 'required|max:255',
            'identificacion_camionero' => [
                'required', 'max:255', Rule::unique('camiones')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'tipo_camion' => 'required|max:1',
            'alto' => 'required|numeric',
            'ancho' => 'required|numeric',
            'placa' => 'required|max:10',
            'filas' => 'required|integer',
            'codigo_vendor' => 'required|max:50'
        ]);
        $camion = Camion::findOrFail($id);
        $camion->camionero = $request->input('camionero');
        $camion->identificacion_camionero = $request->input('identificacion_camionero');
        $camion->modificador_id = $user->id;
        $camion->tipo_camion = $request->input('tipo_camion');
        $camion->ancho = $request->input('ancho');
        $camion->alto = $request->input('alto');
        $camion->codigo_vendor = $request->input('codigo_vendor');
        $camion->placa = $request->input('placa');
        $camion->filas = $request->input('filas');
        $camion->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'camiones' => 'required|array'
        ]);
        $camiones = $request->input('camiones');
        Camion::whereIn('id', $camiones)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
