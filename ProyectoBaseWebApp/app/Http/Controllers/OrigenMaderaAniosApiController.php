<?php

namespace App\Http\Controllers;

use App\OrigenMaderaAnios;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;

class OrigenMaderaAniosApiController extends Controller
{
    public function all()
    {
        return OrigenMaderaAnios::all();
    }
    public function listado(Request $request)
    {
        $user = $request->user();
        return OrigenMaderaAnios::active()->orderBy('anio_cultivo')->get();
    }
    public function index(Request $request)
    {
        $user = $request->user();
        $status = $request->input('status');
        $search = $request->input('search');
        return OrigenMaderaAnios::with('origenMadera')->orderBy('anio_cultivo')
            ->where(function ($query) use ($status) {
                if ($status !== 'T')
                    return $query->where('estado', $status);
                else
                    return $query;
            })
            ->where(function ($query) use ($search) {
                return $query->where('anio_cultivo', 'like', "%$search%")->orWhere('anio_cultivo', 'like', "%$search%");
            })
            ->get();
    }
    public function store(Request $request)
    {
        $request->validate([
            'anio_cultivo' => 'required|max:255',
            'origen_madera_id' => 'required'
        ]);
        $user = $request->user();
        OrigenMaderaAnios::create([
            'anio_cultivo' => $request->input('anio_cultivo'),
            'origen_madera_id' => $request->input('origen_madera_id'),
            'creador_id' => $user->id
            
        ]);
    }
    public function update(Request $request, $id)
    {
        $request->validate([
            'anio_cultivo' => 'required|max:255',
            'origen_madera_id' => 'required'
        ]);
        $OrigenMaderaAnios = OrigenMaderaAnios::findOrFail($id);
        $OrigenMaderaAnios->anio_cultivo = $request->input('anio_cultivo');
        $OrigenMaderaAnios->origen_madera_id = $request->input('origen_madera_id');
        $OrigenMaderaAnios->modificador_id = $request->user()->id;
        $OrigenMaderaAnios->save();
    }
    public function destroy(Request $request)
    {
        $request->validate([
            'origenesMadera' => 'required|array'
        ]);
        $OrigenMaderaAnioss = $request->input('origenesMadera');
        OrigenMaderaAnios::whereIn('id', $OrigenMaderaAnioss)
            ->update([
                'estado' => DB::raw("iif(estado = 'A', 'I', 'A')")
            ]);
    }
}
