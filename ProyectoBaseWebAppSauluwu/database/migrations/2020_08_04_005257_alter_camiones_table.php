<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterCamionesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('camiones'))
            Schema::create('camiones', function (Blueprint $table) {
                $table->id();
                $table->timestamps();
            });
        Schema::table('camiones', function (Blueprint $table) {
            $table->unsignedBigInteger('empresa_id');
            $table->string('camionero');
            $table->string('placa', 10);
            $table->char('tipo_camion', 1);
            $table->string('identificacion_camionero');
            $table->decimal('ancho', 10, 2);
            $table->decimal('alto', 10, 2);
            $table->char('estado', 1)->default('A');
            $table->unsignedBigInteger('creador_id')->nullable();
            $table->unsignedBigInteger('modificador_id')->nullable();
            $table->foreign('creador_id')->references('id')->on('users');
            $table->foreign('modificador_id')->references('id')->on('users');
            $table->foreign('empresa_id')->references('id')->on('empresas');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('camiones');
    }
}
