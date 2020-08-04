<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Correo;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\DB;

class CorreoApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Correo::where('empresa_id', $user->empresa_id)->orderBy('nombre')
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
            'email' => [
                'required', 'email', 'max:255', Rule::unique('correos')->where('empresa_id', $user->empresa_id)
            ]
        ]);
        Correo::create([
            'nombre' => $request->input('nombre'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'email' => $request->input('email')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'nombre' => 'required|max:255',
            'email' => [
                'required', 'email', 'max:255', Rule::unique('correos')->where('empresa_id', $user->empresa_id)->ignore($id)
            ]
        ]);
        $correo = Correo::findOrFail($id);
        $correo->nombre = $request->input('nombre');
        $correo->email = $request->input('email');
        $correo->modificador_id = $user->id;
        $correo->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'correos' => 'required|array'
        ]);
        $correos = $request->input('correos');
        Correo::whereIn('id', $correos)
            ->update([
                'estado' => DB::raw('if(estado = "A", "I", "A")')
            ]);
    }
}
