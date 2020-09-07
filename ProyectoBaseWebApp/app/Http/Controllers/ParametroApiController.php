<?php

namespace App\Http\Controllers;

use App\Parametro;
use Illuminate\Http\Request;

class ParametroApiController extends Controller
{
    public function index(Request $request)
    {
        return [
            'parametros' => Parametro::where('empresa_id', $request->user()->empresa_id)
                ->first()
        ];
    }
    public function all()
    {
        return Parametro::all();
    }
    public function store(Request $request)
    {
        $request->validate([
            'constante' => 'required|numeric',
            'factor_hueco' => 'required|numeric',
            'ancho_bulto' => 'required|numeric'
        ]);
        $user = $request->user();
        $parametro = Parametro::where('empresa_id', $user->empresa_id)->first();
        if (isset($parametro)) {
            $parametro->factor_hueco = $request->input('factor_hueco');
            $parametro->ancho_bulto = $request->input('ancho_bulto');
            $parametro->constante = $request->input('constante');
            $parametro->save();
        } else {
            Parametro::create([
                'empresa_id' => $user->empresa_id,
                'factor_hueco' => $request->input('factor_hueco'),
                'ancho_bulto' => $request->input('ancho_bulto'),
                'constante' => $request->input('constante')
            ]);
        }
    }
}
