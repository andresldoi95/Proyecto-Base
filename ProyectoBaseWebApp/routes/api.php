<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group(['middleware' => 'auth:api'], function () {
    Route::group(['prefix' => 'usuario'], function () {
        Route::put('/{empresaId}', 'UsuarioApiController@cambiarEmpresaActual');
        Route::delete('/', 'UsuarioApiController@destroy');
        Route::post('/perfil', 'UsuarioApiController@perfil');
        Route::get('/datos', 'UsuarioApiController@datos');
    });
    Route::group(['prefix' => 'usuarios'], function () {
        Route::get('/', 'UsuarioApiController@search');
    });
    Route::resource('usuario', 'UsuarioApiController', [
        'except' => [
            'create', 'edit'
        ]
    ]);
    Route::resource('empresas', 'EmpresaApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'empresas'], function () {
        Route::delete('/', 'EmpresaApiController@destroy');
        Route::get('all', 'EmpresaApiController@all');
    });

    Route::group(['prefix' => 'roles'], function () {
        Route::get('/listado', 'RolApiController@listado');
    });

    Route::group(['prefix' => 'procedencias'], function () {
        Route::get('/listado', 'ProcedenciaApiController@listado');
        Route::get('/all', 'ProcedenciaApiController@all');
    });

    Route::resource('roles', 'RolApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'largos'], function () {
        Route::delete('/', 'LargoApiController@destroy');
        Route::get('/all', 'LargoApiController@all');
    });
    Route::resource('largos', 'LargoApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'procedencias'], function () {
        Route::delete('/', 'ProcedenciaApiController@destroy');
    });
    Route::resource('procedencias', 'ProcedenciaApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'destinos'], function () {
        Route::delete('/', 'DestinoApiController@destroy');
        Route::get('/all', 'DestinoApiController@all');
    });
    Route::resource('destinos', 'DestinoApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'materiales'], function () {
        Route::delete('/', 'MaterialApiController@destroy');
        Route::get('/all', 'MaterialApiController@all');
    });
    Route::resource('materiales', 'MaterialApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'correos'], function () {
        Route::delete('/', 'CorreoApiController@destroy');
    });
    Route::resource('correos', 'CorreoApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'controladores'], function () {
        Route::delete('/', 'ControladorApiController@destroy');
        Route::get('/all', 'ControladorApiController@all');
    });
    Route::resource('controladores', 'ControladorApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'espesores'], function () {
        Route::delete('/', 'EspesorApiController@destroy');
        Route::get('/all', 'EspesorApiController@all');
    });
    Route::resource('espesores', 'EspesorApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'aserradores'], function () {
        Route::delete('/', 'AserradorApiController@destroy');
        Route::get('/all', 'AserradorApiController@all');
    });
    Route::resource('aserradores', 'AserradorApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'camiones'], function () {
        Route::delete('/', 'CamionApiController@destroy');
        Route::get('/all', 'CamionApiController@all');
    });
    Route::resource('camiones', 'CamionApiController', [
        'except' => [
            'create', 'edit', 'show'
        ]
    ]);
    Route::group(['prefix' => 'roles'], function () {
        Route::delete('/', 'RolApiController@destroy');
    });

    Route::get('modulos', 'ModuloApiController@index');

    Route::get('acciones', 'AccionApiController@index');

    Route::get('acciones-por-usuario', 'AccionApiController@accionesPorUsuario');
});

Route::post('/reset', 'UsuarioApiController@reset');
Route::post('/set-password', 'UsuarioApiController@setPassword');
