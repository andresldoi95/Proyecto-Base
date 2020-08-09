<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Destino;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class DestinoApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Destino::where('empresa_id', $user->empresa_id)
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
                'required', 'max:20', Rule::unique('destinos')->where('empresa_id', $user->empresa_id)
            ],
            'email' => 'nullable|max:1000'
        ]);
        Destino::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'codigo' => $request->input('codigo'),
            'email' => $request->input('email')
        ]);
    }
    public function update(Request $request, $id)
    {
        $user = $request->user();
        $request->validate([
            'descripcion' => 'required|max:255',
            'codigo' => [
                'required', 'max:20', Rule::unique('destinos')->where('empresa_id', $user->empresa_id)->ignore($id)
            ],
            'email' => 'nullable|max:1000'
        ]);
        $destino = Destino::findOrFail($id);
        $destino->descripcion = $request->input('descripcion');
        $destino->codigo = $request->input('codigo');
        $destino->modificador_id = $user->id;
        $destino->email = $request->input('email');
        $destino->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'destinos' => 'required|array'
        ]);
        $destinos = $request->input('destinos');
        Destino::whereIn('id', $destinos)
            ->update([
                'estado' => DB::raw('if(estado = "A", "I", "A")')
            ]);
    }
}
