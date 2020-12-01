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
                    FO-AP-R6 <br>
                    Versión: 01
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    Madera FSC 100% NC-FM/COC-004235
                </td>
                <td>
                    Secuencia: <span class="numero_documento">{{ $despacho->numero_documento }}</span>
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
                    Año de cultivo: {{ $despacho->origenMadera->anio_cultivo }}
                </td>
                <td>
                    Fecha de tumba: {{ $despacho->fecha_tumba->format('d/m/Y') }}
                </td>
                <td>
                    Fecha de despacho: {{ $despacho->fecha_despacho->format('d/m/Y') }}
                </td>
            </tr>
            <tr>
                <td>
                    Días T2K: {{ $despacho->dias_t2k }}
                </td>
                <td>
                    Guía forestal Nº: {{ $despacho->guia_forestal }}
                </td>
                <td>
                    Guía de remisión: {{ $despacho->guia_remision }}
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
                    Valor del flete: {{ number_format($despacho->valor_flete, 2) }}
                </td>
            </tr>
        </tbody>
    </table>
    <table id="subheader" class="bordered">
        <tbody>
            <tr>
                <td>
                    Ancho de camión: {{ $despacho->camion->ancho }}
                </td>
                <td>
                    <strong class="upper">Madera rolliza</strong>
                </td>
                <td>
                    Cantidad de tucos: {{ number_format($despacho->filas()->sum('bultos')) }}
                </td>
                <td>
                    Volumen en MT3:
                </td>
            </tr>
        </tbody>
    </table>
    <h4 class="upper centered">Madera aserrada/espesor pulgadas</h4>
    <table id="filas" class="bordered">

        <tfoot>
            <tr>
                <td class="upper">
                    Total plantillas enviados: {{ number_format($despacho->filas()->sum('bultos')) }}
                </td>
                <td class="upper">
                    Total BFT enviados: {{ number_format($despacho->filas()->sum('bft'), 2) }}
                </td>
            </tr>
        </tfoot>
    </table>
    <table class="bordered" id="footer">
        <tr>
            <td colspan="3">
                <span class="upper">Observaciones:</span>
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
                <strong>Fecha de recepción: {{ date('d/m/Y') }}</strong><br><br>__________________<br>
                Recepción
            </td>
        </tr>
    </table>
</body>
</html>
