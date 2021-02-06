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
use DB;


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
                try {
                $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
                $despacho = Despacho::findOrFail($id);      
                $correos = Correo::where('empresa_id', $empresa_id)->where('estado', 'A')->get();
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
                } catch (\Throwable $th) {
                    $despacho_update = Despacho::findOrFail($id);
                    $despacho_update->email_enviado = 0;                
                    $despacho_update->save();
                }
                



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

    public function webServices($id) {
        $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
        $despacho = Despacho::findOrFail($id);
        $espesores = Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get();
        $largos =Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get();
        $filas_despacho =FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get();
        $trozas =Troza::where('despacho_id', $id)->get();
        $troza_fotos =TrozaFotos::orderBy('troza_id')->get();
        $filas_suelto =FilaSuelto::orderBy('indice')->get();
        $tipos_bulto =TipoBulto::where('empresa_id', $empresa_id)->get();
        $fotos_fila =FotoFila::orderBy('fila_id')->get();
        $aserrador =Aserrador::findOrFail($despacho->aserrador_id);
        $material =Material::findOrFail($despacho->material_id);
        $users =User::where('empresa_id', $empresa_id)->get();
        $volumenEnviado = 0;
        $numeroPlantilla = 0;
        $fecha_tumba= "";
        $fecha_despacho = "";
        $guia_forestal = "";
        $enviar_web_service = "";
        if($trozas->count()>0){
            $volumenEnviado = $trozas->first()->volumen_estimado;
        }

        if($trozas->count()>0){
            $numeroPlantilla = number_format($trozas->first()->numero_trozas);

        }else{
            $numeroPlantilla =  number_format($despacho->filas()->sum('bultos'));
        }
        
        $guia_remision = str_replace('-', '', $despacho->guia_remision);

        $fecha_despacho = $despacho->fecha_despacho;
        $fecha_tumba= $despacho->fecha_tumba;
        $guia_forestal= $despacho->guia_forestal;

        if($guia_forestal==null){
            $guia_forestal = "";
        }


        
        $detallePlantillas_response = [
            'largo' => (int) 0,
            'espesor' => '',
            'ancho' => '',
            'numeroPlantilla' => (int) $numeroPlantilla
        ];

        $despachos_response = [
            'numeroDespacho' => $despacho->numero_documento,
            'volumenEnviado' => (int) $volumenEnviado,
            'haciendaCodigo' => $despacho->origenHacienda->descripcion,
            'aserradorVendor' => $aserrador->vendor,
            'fechaTumba' => $fecha_tumba,
            'fechaDespacho' => $fecha_despacho,
            'codSapMaterial' => $material->codigo,
            'po' => $despacho->codigo_po,
            'detallePlantillas' => $detallePlantillas_response
        ];

        $enviar_web_service =  response()->json([
            'plantaDestino' => $despacho->destino->descripcion,
            'placa' => $despacho->camion->placa,
            'guiaRemision' => $guia_remision,
            'guiaForestal' => $guia_forestal,
            'formatoMadera' => $despacho->formatoEntrega->descripcion,
            'volumenEnviado' => (int) $volumenEnviado,
            'controladorCedula' => $users->where('id',$despacho->usuario_id)->first()->identificacion,
            'despachos'=> $despachos_response
          ]);

        return $enviar_web_service;
    }
}
