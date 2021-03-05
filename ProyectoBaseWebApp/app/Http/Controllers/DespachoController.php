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
use App\OrigenMaderaAnios;


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

        try {
            $empresa_id = User::findOrFail(Despacho::findOrFail($id)->usuario_id)->empresa_id; 
            $despacho = Despacho::findOrFail($id);
            $espesores = Espesor::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get();
            $largos =Largo::active()->orderBy('descripcion')->where('empresa_id', $empresa_id)->get();
            $filas_despacho =FilaDespacho::orderBy('indice')->where('despacho_id', $id)->get();
            $trozas =Troza::where('despacho_id', $id)->get();
            $troza_fotos =TrozaFotos::orderBy('troza_id')->get();
            $filas_sueltos =FilaSuelto::orderBy('indice')->get();
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
            $codigo_po = "";
            $codigo_hacienda = "";
            $fechaTumba = "";
            $fechaDespacho = "";

            $numero_documento = ltrim($despacho->numero_documento, "0");

            if($despacho->codigo_po!=NULL){
                $codigo_po = $despacho->codigo_po;
            }else{
                $codigo_po = $despacho->codigoPo->descripcion;
            }
            if($despacho->origenMaderaAnio->codigo_hacienda!=NULL){
                $codigo_hacienda = trim($despacho->origenMaderaAnio->codigo_hacienda);
            }



            if($despacho->tipo_llenado =='B' || $despacho->tipo_llenado =='S' || $despacho->tipo_llenado =='H'){
                if($trozas->count()>0){
                    $volumenEnviado = $trozas->first()->volumen_estimado;
                }else{
                    $volumenEnviado = $despacho->filas()->sum('bft');
                }
                if($trozas->count()>0){
                    $numeroPlantilla = $trozas->first()->numero_trozas;
                }else{
                    $numeroPlantilla =  $despacho->filas()->sum('bultos');
                }

                $guia_remision = str_replace('-', '', $despacho->guia_remision);

                $fecha_despacho = $despacho->fecha_despacho."T00:00:00.000Z";
                $fecha_tumba= $despacho->fecha_tumba."T00:00:00.000Z";
                $guia_forestal= $despacho->guia_forestal;

                if($guia_forestal==null){
                    $guia_forestal = "";
                }

                $detallePlantillas_response = [];

                foreach($largos as $largo){
                    foreach($espesores as $espesor){
                        $suma_fila_despacho_bulto=0;

                        foreach ($filas_despacho as $fila_despacho) {
                            if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0){
                                $suma_fila_despacho_bulto= $suma_fila_despacho_bulto + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->sum('bultos');
                            }else{
                                if($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0){
                                    foreach($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id) as $tipo_bulto){
                                        if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->count() > 0){
                                            $suma_fila_despacho_bulto= $suma_fila_despacho_bulto + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->sum('bultos');
                                        }
                                    }
                                }
                            }
        
                        }
                        if($suma_fila_despacho_bulto>0){

                            $tipo_bulto =TipoBulto::where('largo_id', $largo->id)->where('espesor_id', $espesor->id)->get()->first();

                            $other = [
                                'largo' => (int) $largo->valor,
                                'espesor' => $espesor->descripcion,
                                'ancho' => (int) $tipo_bulto->ancho,
                                'numeroPlantilla' => (int) $suma_fila_despacho_bulto
                            ];
                            array_push($detallePlantillas_response, $other);
                        }
                    }
                }

                

                $despachos_response = [
                    'numeroDespacho' => $numero_documento,
                    'volumenEnviado' => (int) $volumenEnviado,
                    'haciendaCodigo' => $codigo_hacienda,
                    'aserradorVendor' => $aserrador->vendor,
                    'fechaTumba' => $fecha_despacho,
                    'fechaDespacho' => $fecha_despacho,
                    'codSapMaterial' => $material->codigo,
                    'po' => $codigo_po,
                    'detallePlantillas' => $detallePlantillas_response
                ];

                $enviar_web_service =  [
                    'plantaDestino' => $despacho->destino->codigo,
                    'placa' => strtolower($despacho->camion->placa),
                    'guiaRemision' => $guia_remision,
                    'guiaForestal' => $guia_forestal,
                    'formatoMadera' => strtolower($despacho->formatoEntrega->descripcion),
                    'volumenEnviado' => (int) $volumenEnviado,
                    'controladorCedula' => $users->where('id',$despacho->usuario_id)->first()->identificacion,
                    'despachos'=> [$despachos_response]
                ];

                $yourjson = json_encode($enviar_web_service);

                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, "http://api.scm-test.dts-ec.com:90/api/tallydigital/tallyadd");
                curl_setopt($ch, CURLOPT_HTTPHEADER, array(
                    'Content-Type: application/json'
                ));
                curl_setopt($ch, CURLOPT_POST, 1);
                curl_setopt($ch, CURLOPT_POSTFIELDS, $yourjson);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                $response = curl_exec($ch);
                curl_close($ch);

                $respuesta = json_decode($response, true);

                if($respuesta['isSuccess']==true){
                    try{
                        $despacho_save = Despacho::findOrFail($id);
                        $despacho_save->web_service = 1;
                        $despacho_save->save();
                    }catch (\Exception $e) {

                    }

                }else{
                    try{
                        $despacho_save = Despacho::findOrFail($id);
                        $despacho_save->web_service = 0;
                        $despacho_save->save();
                    }catch (\Exception $e) {

                    }
                }
                return $respuesta;


            }
            
            
        } catch (\Exception $e) {
            $error = $e->getMessage();
            return response()->json([
              'status' => false,
              'response' => $error
            ]);
        }
        
    }
}
