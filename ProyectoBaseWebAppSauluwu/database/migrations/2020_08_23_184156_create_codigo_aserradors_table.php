<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCodigoAserradorsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('codigos_aserradores'))
            Schema::create('codigos_aserradores', function (Blueprint $table) {
                $table->id();
                $table->unsignedBigInteger('aserrador_id');
                $table->string('codigo', 40);
                $table->unsignedBigInteger('empresa_id');
                $table->string('descripcion');
                $table->timestamps();
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
        Schema::dropIfExists('codigos_aserradores');
    }
}
