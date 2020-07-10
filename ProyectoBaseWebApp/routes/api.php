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
    Route::resource('usuario', 'UsuarioApiController', [
        'except' => [
            'create', 'edit'
        ]
    ]);
    Route::resource('empresas', 'EmpresaApiController', [
        'except' => [
            'create', 'edit'
        ]
    ]);
    Route::group(['prefix' => 'empresas'], function () {
        Route::delete('/', 'EmpresaApiController@destroy');
    });

    Route::get('modulos', 'ModuloApiController@index');
});
