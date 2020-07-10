<?php

namespace App\Http\Controllers;

use App\Empresa;
use Illuminate\Http\Request;

class UsuarioApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $user->load('empresa');
        return [
            'usuario' => $user,
            'empresas' => Empresa::active()->orderBy('nombre')->get()
        ];
    }
    public function cambiarEmpresaActual(Request $request, $empresaId)
    {
        $user = $request->user();
        $user->empresa_id = $empresaId;
        $user->save();
    }
}
