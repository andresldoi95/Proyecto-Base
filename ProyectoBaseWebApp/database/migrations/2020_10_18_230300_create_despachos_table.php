<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateDespachosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('despachos'))
            Schema::create('despachos', function (Blueprint $table) {
                $table->char('id', 36)->primary();
                $table->unsignedBigInteger('camion_id');
                $table->unsignedBigInteger('controlador_id');
                $table->unsignedBigInteger('destino_id');
                $table->unsignedBigInteger('aserrador_id');
                $table->unsignedBigInteger('material_id');
                $table->unsignedBigInteger('tipo_madera_id');
                $table->unsignedBigInteger('origen_madera_id');
                $table->unsignedBigInteger('formato_entrega_id');
                $table->string('codigo_po', 20);
                $table->date('fecha_tumba');
                $table->date('fecha_despacho');
                $table->integer('dias_t2k');
                $table->string('guia_remision', 30)->nullable();
                $table->string('guia_forestal', 30)->nullable();
                $table->char('tipo_llenado', 1)->default('B');
                $table->decimal('valor_flete', 10, 2);
                $table->char('estado', 1)->default('F');
                $table->unsignedBigInteger('usuario_id');
                $table->timestamps();
                $table->foreign('camion_id')->references('id')->on('camiones');
                $table->foreign('controlador_id')->references('id')->on('controladores');
                $table->foreign('destino_id')->references('id')->on('destinos');
                $table->foreign('aserrador_id')->references('id')->on('aserradores');
                $table->foreign('material_id')->references('id')->on('materiales');
                $table->foreign('tipo_madera_id')->references('id')->on('tipos_madera');
                $table->foreign('origen_madera_id')->references('id')->on('origenes_madera');
                $table->foreign('formato_entrega_id')->references('id')->on('formatos_entrega');
                $table->foreign('usuario_id')->references('id')->on('users');
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('despachos');
    }
}
