<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Aserrador;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class AserradorApiController extends Controller
{
    public function listado(Request $request)
    {
        return Aserrador::current($request->user()->empresa_id)
            ->active()
            ->orderBy('nombre')
            ->get();
    }
    public function all()
    {
        return Aserrador::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Aserrador::where('empresa_id', $user->empresa_id)
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
                'required', 'max:255', Rule::unique('aserradores')->where('empresa_id', $user->empresa_id)
            ]
        ]);
        Aserrador::create([
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
                'required', 'max:255', Rule::unique('aserradores')->where('empresa_id', $user->empresa_id)->ignore($id)
            ]
        ]);
        $aserrador = Aserrador::findOrFail($id);
        $aserrador->nombre = $request->input('nombre');
        $aserrador->identificacion = $request->input('identificacion');
        $aserrador->modificador_id = $user->id;
        $aserrador->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'aserradores' => 'required|array'
        ]);
        $aserradores = $request->input('aserradores');
        Aserrador::whereIn('id', $aserradores)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
