<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFilaCamionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('filas_camion'))
            Schema::create('filas_camion', function (Blueprint $table) {
                $table->char('id', 36)->primary();
                $table->unsignedBigInteger('camion_id');
                $table->integer('filas');
                $table->integer('columnas');
                $table->timestamps();
                $table->foreign('camion_id')->references('id')->on('camiones');
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('filas_camion');
    }
}
