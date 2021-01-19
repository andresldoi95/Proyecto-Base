<?php

namespace App\Http\Controllers;

use App\Despacho;
use App\Espesor;
use App\Largo;
use App\FilaDespacho;
use App\FilaSuelto;
use App\TipoBulto;
use App\User;
use App\Troza;
use App\FotoFila;
use App\TrozaFotos;
use App\Aserrador;
use App\Material;
use Mail;
use PDF;
use App\Correo;
use Illuminate\Http\Request;

class DespachoController extends Controller
{
    public function show($id) {
        $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
        $despacho = Despacho::findOrFail($id);
        return PDF::loadView('despacho', [
            'despacho' => $despacho,
            'espesores' => Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'largos' =>Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'filas_despacho' =>FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get(),
            'trozas' =>Troza::where('despacho_id', $id)->get(),
            'troza_fotos' =>TrozaFotos::orderBy('troza_id')->get(),
            'filas_sueltos' =>FilaSuelto::orderBy('indice')->get(),
            'tipos_bulto' =>TipoBulto::where('empresa_id', $empresa_id)->get(),
            'fotos_fila' =>FotoFila::orderBy('fila_id')->get(),
            'aserrador' =>Aserrador::findOrFail($despacho->aserrador_id),
            'material' =>Material::findOrFail($despacho->material_id),
            'users' =>User::where('empresa_id', $empresa_id)->get(),




        ])->stream();
    }

    
    public function sendEmailDespacho(){
        try {
            $despachos_sin_enviar = Despacho::where('email_enviado', NULL)->get();

            foreach($despachos_sin_enviar as $despacho_sin_enviar){
                $id = $despacho_sin_enviar->id;
                $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
                $despacho = Despacho::findOrFail($id);      
                $correos = Correo::where('empresa_id', $empresa_id)->get();
                $pdf = PDF::loadView('despacho', [
                    'despacho' => $despacho,
                    'espesores' => Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
                    'largos' =>Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
                    'filas_despacho' =>FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get(),
                    'trozas' =>Troza::where('despacho_id', $id)->get(),
                    'troza_fotos' =>TrozaFotos::orderBy('troza_id')->get(),
                    'filas_sueltos' =>FilaSuelto::orderBy('indice')->get(),
                    'tipos_bulto' =>TipoBulto::where('empresa_id', $empresa_id)->get(),
                    'fotos_fila' =>FotoFila::orderBy('fila_id')->get(),
                    'aserrador' =>Aserrador::findOrFail($despacho->aserrador_id),
                    'material' =>Material::findOrFail($despacho->material_id),
                    'users' =>User::where('empresa_id', $empresa_id)->get(),

                ]);

                foreach($correos as $correo){
                    $data["email"] = $correo->email;
                    $data["title"] = "Nuevo Despacho - Tally Digital";
                    $data["body"] = "Estimado ".$correo->nombre.", Se ha exportado un nuevo despacho.";

                    Mail::send('emails.myTestMail', $data, function($message)use($data, $pdf) {
                        $message->to($data["email"], $data["email"])
                                ->subject($data["title"])
                                ->attachData($pdf->stream(), "nuevo_despacho.pdf");
                    });   
                }

                $despacho_update = Despacho::findOrFail($id);
                $despacho_update->email_enviado = 1;                
                $despacho_update->save();



            }

            
                

    } catch (\Throwable $th) {
        //throw $th;
    }

    }

    public function showView($id) {
        $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
        $despacho = Despacho::findOrFail($id);

        //return view('despacho');
        return view('despacho', [
            'despacho' => $despacho,
            'espesores' => Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'largos' =>Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get(),
            'filas_despacho' =>FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get(),
            'trozas' =>Troza::where('despacho_id', $id)->get(),
            'troza_fotos' =>TrozaFotos::orderBy('troza_id')->get(),
            'filas_sueltos' =>FilaSuelto::orderBy('indice')->get(),
            'tipos_bulto' =>TipoBulto::where('empresa_id', $empresa_id)->get(),
            'fotos_fila' =>FotoFila::orderBy('fila_id')->get(),
            'aserrador' =>Aserrador::findOrFail($despacho->aserrador_id),
            'material' =>Material::findOrFail($despacho->material_id),
            'users' =>User::where('empresa_id', $empresa_id)->get(),



        ]);
    }
}
