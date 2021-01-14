<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Despacho {{ $despacho->numero_documento }}</title>
    <style>
        .upper {
            text-transform: uppercase;
        }
        .numero_documento {
            color: brown;
            font-size: 18px;
        }
        body {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 11px;
        }
        .bordered td, .bordered tr, .bordered th {
            border: 1px solid;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table td, table th {
            padding: 5px;
        }
        #subheader {
            margin-top: 10px;
        }
        .centered {
            text-align: center;
        }
    </style>
</head>
<body>
    <table class="bordered">
        <tbody>
            <tr>
                <td>

                </td>
                <td colspan="2">
                    <h3 class="upper">Guía de tranporte y despacho de madera suelta</h3>
                </td>
                <td>
                N°: <span class="numero_documento">{{ $despacho->numero_documento }}</span>
                    
                </td>
            </tr>
            <tr>
                <td>
                    Origen de madera: {{ $despacho->origenHacienda->descripcion }}
                </td>
                <td>
                    Procedencia de traslado: {{ $despacho->origenMadera->descripcion }}
                </td>
                <td>
                    Destino de traslado: {{ $despacho->destino->descripcion }}
                </td>
                <td rowspan="2">
                    Tipo de madera: {{ $despacho->tipoMadera->descripcion }}
                </td>
            </tr>
            <tr>
                <td>
                    Año de cultivo: 
                    @if($despacho->origenMaderaAnio)
                        {{ $despacho->origenMaderaAnio->anio_cultivo }}
                    @endif
                </td>
                <td>
                    Fecha de tumba:<br> {{ $despacho->fecha_tumba }}
                </td>
                <td>
                    Fecha de despacho:<br> {{ $despacho->fecha_despacho }}
                </td>
            </tr>
            <tr>
                <td>
                    Días T2K: {{ $despacho->dias_t2k }}
                </td>
                <td>
                    Guía forestal Nº: <br>{{ $despacho->guia_forestal }}
                </td>
                <td>
                    Guía de remisión: <br>{{ $despacho->guia_remision }}
                </td>
                <td rowspan="2">
                    Formato de entrega: {{ $despacho->formatoEntrega->descripcion }}
                </td>
            </tr>
            <tr>
                <td>
                    Chofer: {{ $despacho->camion->camionero }}
                </td>
                <td>
                    Placa Nº: {{ $despacho->camion->placa }}
                </td>
                <td>
                    Valor del flete: 
                    @if($trozas->count()>0)
                        {{ $despacho->valor_flete }}
                    @else
                        {{ $despacho->valor_flete }}
                    @endif
                </td>
            </tr>
        </tbody>
    </table>
    <table id="subheader" class="bordered">
        <tbody>
            <tr>
                <td>
                <strong class="upper">Ancho de camión:</strong> {{ $despacho->camion->ancho }}
                </td>
                <td>
                    <strong class="upper">Material:</strong> {{ $material->descripcion }}
                </td>
                <td>
                    <strong class="upper">Código PO:</strong> {{ $despacho->codigo_po }}
                </td>
                
            </tr>
        </tbody>
    </table>
    <h4 class="upper centered">Madera aserrada/espesor pulgadas</h4>
    <table id="filas" class="bordered">
    @if($despacho->tipo_llenado =='B' || $despacho->tipo_llenado =='S')
        <tbody>
            <tr>
                <td class="centered">
                    <b>LARGO PIES</b>
                </td>
                @foreach($espesores as $espesor)
                    <td class="centered">
                    {{ $espesor->descripcion }} "
                    </td>
                @endforeach
                <td class="centered">
                   <b>RESUMEN</b>
                </td>
            </tr>
            @foreach($largos as $largo)
            <?php
                $resumen_bultos=0;
                $resumen_bft=0;
            ?>

            <tr>
                <td class="centered">
                    <b># de Plantilla ( {{ $largo->descripcion }} FT) </b>
                </td>
                @foreach($espesores as $espesor)
                    <td class="centered">
                    <?php
                            $suma_fila_despacho_bulto=0;
                            $suma_fila_despacho_bft=0;
                        ?>
                    @foreach($filas_despacho as $fila_despacho)

                        @if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0)
                          <?php $suma_fila_despacho_bulto= $suma_fila_despacho_bulto + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->sum('bultos');?>
                        @else
                            @if($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0)
                                @foreach($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id) as $tipo_bulto)
                                    @if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->count() > 0)
                                        <?php $suma_fila_despacho_bulto= $suma_fila_despacho_bulto + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->sum('bultos');?>
                                    @endif
                                @endforeach

                            @endif

                        @endif


                    @endforeach
                    <?php $resumen_bultos= $resumen_bultos + $suma_fila_despacho_bulto;?>

                    @if($suma_fila_despacho_bulto>0)
                     {{ $suma_fila_despacho_bulto }}
                    @endif



                    </td>
                @endforeach
                <td class="centered">
                   <b>{{ $resumen_bultos }}</b>
                </td>
            </tr>
            <tr>
                <td class="centered">
                    <b>Valor en bft</b>
                </td>
                @foreach($espesores as $espesor)
                    <td class="centered">
                    <?php
                            $suma_fila_despacho_bft=0;
                    ?>
                    @foreach($filas_despacho as $fila_despacho)

                        @if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0)
                          <?php $suma_fila_despacho_bft= $suma_fila_despacho_bft + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->sum('bft');?>
                        @else
                            @if($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id)->count() > 0)
                                @foreach($tipos_bulto->where('espesor_id',$espesor->id)->where('largo_id',$largo->id) as $tipo_bulto)
                                    @if($filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->count() > 0)
                                        <?php $suma_fila_despacho_bft= $suma_fila_despacho_bft + $filas_sueltos->where('fila_id',$fila_despacho->id)->where('tipo_bulto_id',$tipo_bulto->id)->sum('bft');?>
                                    @endif
                                @endforeach

                            @endif

                        @endif


                    @endforeach
                    <?php $resumen_bft= $resumen_bft + $suma_fila_despacho_bft;?>

                    @if($suma_fila_despacho_bft>0)
                     {{ $suma_fila_despacho_bft }}
                    @endif

                    </td>
                @endforeach
                <td class="centered">
                   <b>{{ $resumen_bft }}</b>
                </td>
            </tr>
            @endforeach
        </tbody>
        @else
        <tbody>
            <tr>
                <td  class="centered" colspan="{{ (($espesores->count() + 2)) }}">
                @if($trozas->count()>0)
                    <?php $contador=0;?>
                        @foreach($trozas as $troza)
                            @foreach($troza_fotos->where('troza_id', $troza->id) as $troza_foto)
                                    <?php if($contador==0){echo"<h2>Fotos</h2><br><br>";}?>

                                    <img src="{{$troza_foto->foto}}" alt="" style="width: auto; height: 175px;padding: 15px;">
                                    <?php $contador++;if($contador%2==0 && $contador>1){echo"<br>";}?>
                            @endforeach
                        @endforeach                    
                @endif
                </td>
            </tr>
        </tbody>

        @endif
        <tfoot>
            <tr>
            @if($trozas->count()>0)
                <td class="upper" colspan="4">
               
                    <strong>Total plantillas enviados:</strong> {{ $trozas->first()->numero_trozas }}                
                </td>
                <td class="upper" colspan="2">
                    <strong>Total BFT enviados:</strong> {{ number_format($despacho->filas()->sum('bft'), 2) }}
                </td>
                <td class="upper" colspan="2">
                    <strong>Volumen Estimado:</strong> {{ $trozas->first()->volumen_estimado}} M3
                </td>
                @else
                <td class="upper" colspan="{{ (($espesores->count() + 2) / 2) }}">               
                    <strong>Total plantillas enviados:</strong> {{ number_format($despacho->filas()->sum('bultos')) }}
                </td>
                <td class="upper" colspan="{{ (($espesores->count() + 2) / 2) }}">
                    <strong>Total BFT enviados:</strong> {{ number_format($despacho->filas()->sum('bft'), 2) }}
                </td>
                @endif
            </tr>
        </tfoot>
    </table>
    <table class="bordered" id="footer">
        <tr>
            <td colspan="3">
                @if($trozas->count()>0)
                    <span class="upper"><strong>Observaciones:</strong> {{$trozas->first()->observaciones }}</span>
                    <br>
                @endif
                <span class="upper"><strong>Aserrador: </strong>{{$aserrador->nombre }}</span>
                <br>
                <span class="upper"><strong>Vendor: </strong>{{$aserrador->vendor }}</span>
                <br> 
            </td>
        </tr>
        <tr>
            <td class="centered">
                <br><br>__________________<br>
                Despacho
            </td>
            <td class="centered">
                <br><br>__________________<br>
                Transporte
            </td>
            <td class="centered">
                <strong>Fecha de recepción: </strong><br><br>__________________<br>
                Recepción
            </td>
        </tr>
    </table>
    <br>
    <br>
    
        @if($filas_despacho->count()>0 && $trozas->count()==0)
        <div style="page-break-after:always;"></div>
            <center>
            <table>
            <?php $contador=0;?>
            @foreach($filas_despacho as $fila_despacho)
                @foreach($fotos_fila->where('fila_id', $fila_despacho->id) as $foto_fila) 
                    <?php if($contador==0){echo"<tr><td class='centered' colspan='2'><h2>Fotos</h2></td></tr>";}?>
                    
                    <?php if(($contador%2==0 )|| ($contador==0)){echo"<tr>";}?>

                    <td class='centered'>
                        <span>Fila {{ $fila_despacho->indice + 1 }}</span>
                        <br>
                        <img src="{{$foto_fila->path }}" alt="" style="width: auto;height: 175px;padding: 25px;">
                    </td>
                    <?php $contador++;if($contador%2==0 && $contador>1){echo"</tr>";}?>
                @endforeach
            @endforeach
            </table>
            </center>                    
        @endif
    
</body>
</html>
