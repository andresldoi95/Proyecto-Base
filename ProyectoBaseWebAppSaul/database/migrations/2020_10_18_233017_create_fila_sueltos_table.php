<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFilaSueltosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('filas_sueltos'))
            Schema::create('filas_sueltos', function (Blueprint $table) {
                $table->char('id', 36)->primary();
                $table->char('fila_id', 36);
                $table->unsignedBigInteger('espesor_id');
                $table->unsignedBigInteger('largo_id');
                $table->integer('indice');
                $table->decimal('bft', 10, 2);
                $table->decimal('bultos', 10, 2);
                $table->timestamps();
                $table->foreign('fila_id')->references('id')->on('filas_despacho');
                $table->foreign('espesor_id')->references('id')->on('espesores');
                $table->foreign('largo_id')->references('id')->on('largos');
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('filas_sueltos');
    }
}
