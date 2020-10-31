<?php

namespace App\Http\Controllers;

use App\Despacho;
use Illuminate\Http\Request;
use PDF;

class DespachoController extends Controller
{
    public function show($id) {
        return PDF::loadView('despacho', [
            'despacho' => Despacho::findOrFail($id)
        ])->stream();
    }
}
