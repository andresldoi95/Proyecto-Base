<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\User;
use App\Rol;
use App\Privilegio;
use App\Permiso;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;


class AuthController extends Controller
{
    

    public function login_app(Request $request) {
        $email = $request->input('username');
        $password = $request->input('password');

        $user =  User::where('email',$email)->first();
        if ($user!= NULL) {
            if (Hash::check($password, $user->password )) {

                $rol_id = Privilegio::where('usuario_id',$user->id)->first()->rol_id;
                $acciones =   Permiso::where('rol_id',$rol_id)->where('accion_id','ingreso_app_movil');

                if($acciones->count()>0){
                    return response()->json([
                        'status' => true,
                        'response' => 'Acceso Exitoso.'
                        ]);

                }else{
                    return response()->json([
                        'status' => false,
                        'response' => 'No tiene permisos para ingresar a la app. Contacte al Administrador.'
                        ]);
                }
            }else {
                return response()->json([
                'status' => false,
                'response' => 'Email o Contraseña incorrecta.'
                ]);
            }

        }else {
            return response()->json([
            'status' => false,
            'response' => 'Email o Contraseña incorrecta.'
            ]);
        }

    }
}