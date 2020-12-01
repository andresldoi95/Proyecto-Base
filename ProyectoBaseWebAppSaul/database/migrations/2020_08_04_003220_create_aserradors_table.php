<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateAserradorsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('aserradores'))
            Schema::create('aserradores', function (Blueprint $table) {
                $table->id();
                $table->unsignedBigInteger('empresa_id');
                $table->unsignedBigInteger('procedencia_id');
                $table->string('identificacion', 20);
                $table->string('nombre');
                $table->timestamps();
                $table->char('estado', 1)->default('A');
                $table->unsignedBigInteger('creador_id')->nullable();
                $table->unsignedBigInteger('modificador_id')->nullable();
                $table->foreign('creador_id')->references('id')->on('users');
                $table->foreign('modificador_id')->references('id')->on('users');
                $table->foreign('empresa_id')->references('id')->on('empresas');
                $table->foreign('procedencia_id')->references('id')->on('procedencias');
                $table->unique([
                    'empresa_id', 'identificacion'
                ]);
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('aserradores');
    }
}
