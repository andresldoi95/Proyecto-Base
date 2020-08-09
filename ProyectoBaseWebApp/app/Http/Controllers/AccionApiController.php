<?php

namespace App\Http\Controllers;

use App\Accion;
use Illuminate\Http\Request;

class AccionApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        if ($user->empresa !== null) {
            return Accion::orderBy('descripcion')->whereIn('modulo_id', $user->empresa->modulos->pluck('id')->all())->get();
        } else
            return Accion::orderBy('descripcion')->get();
    }
    public function accionesPorUsuario(Request $request)
    {
        $user = $request->user();
        if ($user->empresa !== null) {
            $acciones = Accion::whereIn('modulo_id', $user->empresa->modulos->pluck('id')->all())->active();
            if ($user->es_superadmin !== 'S')
                $acciones->whereHas('roles', function ($query) use ($user) {
                    $query->whereIn('id', $user->roles()->where('empresa_id', $user->empresa_id)->get()->pluck('id')->all());
                });
            return $acciones->get();
        } else
            return [];
    }
}
