<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateProcedenciasTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('procedencias'))
            Schema::create('procedencias', function (Blueprint $table) {
                $table->id();
                $table->unsignedBigInteger('empresa_id');
                $table->string('codigo', 20);
                $table->string('descripcion');
                $table->string('email', 1000)->nullable();
                $table->timestamps();
                $table->unsignedBigInteger('creador_id')->nullable();
                $table->unsignedBigInteger('modificador_id')->nullable();
                $table->char('estado', 1)->default('A');
                $table->unique([
                    'empresa_id', 'codigo'
                ]);
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
        Schema::dropIfExists('procedencias');
    }
}
