<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class UsuarioApiController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        return $user;
    }
}
