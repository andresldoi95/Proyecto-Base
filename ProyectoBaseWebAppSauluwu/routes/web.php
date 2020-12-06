<?php

use Illuminate\Support\Facades\Route;

Route::get('/despacho/{id}', 'DespachoController@show');
Route::get('/despachoview/{id}', 'DespachoController@showView');


Route::any('/{any}', function () {
    return view('welcome');
})->where('any', '.*');
