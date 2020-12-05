<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFilaDespachosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('filas_despacho'))
            Schema::create('filas_despacho', function (Blueprint $table) {
                $table->char('id', 36)->primary();
                $table->char('despacho_id', 36);
                $table->unsignedBigInteger('tipo_bulto_id')->nullable();
                $table->integer('indice');
                $table->decimal('bft', 10, 2);
                $table->decimal('bultos', 10, 2);
                $table->char('estado', 1)->default('F');
                $table->char('tipo', 1)->default('N');
                $table->timestamps();
                $table->foreign('despacho_id')->references('id')->on('despachos');
                $table->foreign('tipo_bulto_id')->references('id')->on('tipos_bulto');
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('filas_despacho');
    }
}
