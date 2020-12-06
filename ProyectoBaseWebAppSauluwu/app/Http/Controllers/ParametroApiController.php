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
            'factor_hueco_sueltos' => 'required|numeric',
            'factor_hueco_bultos' => 'required|numeric'
        ]);
        $user = $request->user();
        $parametro = Parametro::where('empresa_id', $user->empresa_id)->first();
        if (isset($parametro)) {
            $parametro->factor_hueco_bultos = $request->input('factor_hueco_bultos');
            $parametro->factor_hueco_sueltos = $request->input('factor_hueco_sueltos');
            $parametro->save();
        } else {
            Parametro::create([
                'empresa_id' => $user->empresa_id,
                'factor_hueco_bultos' => $request->input('factor_hueco_bultos'),
                'factor_hueco_sueltos' => $request->input('factor_hueco_sueltos')
            ]);
        }
    }
}
