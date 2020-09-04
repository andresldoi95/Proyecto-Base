<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Largo;
use Illuminate\Support\Facades\DB;

class LargoApiController extends Controller
{
    public function all()
    {
        return Largo::all();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return Largo::where('empresa_id', $user->empresa_id)
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
        $request->validate([
            'descripcion' => 'required|max:255',
            'valor' => 'required|numeric'
        ]);
        $user = $request->user();
        Largo::create([
            'descripcion' => $request->input('descripcion'),
            'creador_id' => $user->id,
            'empresa_id' => $user->empresa_id,
            'valor' => $request->input('valor')
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'descripcion' => 'required|max:255',
            'valor' => 'required|numeric'
        ]);
        $largo = Largo::findOrFail($id);
        $largo->descripcion = $request->input('descripcion');
        $largo->valor = $request->input('valor');
        $largo->modificador_id = $request->user()->id;
        $largo->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'largos' => 'required|array'
        ]);
        $largos = $request->input('largos');
        Largo::whereIn('id', $largos)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
