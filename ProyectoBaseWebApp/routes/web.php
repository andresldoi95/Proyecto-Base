<?php

use Illuminate\Support\Facades\Route;

Route::get('/despacho/{id}', 'DespachoController@show');
Route::get('/despachoview/{id}', 'DespachoController@showView');

Route::get('/despachoemail', 'DespachoController@sendEmailDespacho');

Route::get('/despachoWebServices/{id}', 'DespachoController@webServices');


Route::any('/{any}', function () {
    return view('welcome');
})->where('any', '.*');
