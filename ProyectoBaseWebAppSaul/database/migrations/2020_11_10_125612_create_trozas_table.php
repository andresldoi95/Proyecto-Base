<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTrozasTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('trozas'))
            Schema::create('trozas', function (Blueprint $table) {
                $table->char('id', 36)->primary();
                $table->char('despacho_id', 36);
                $table->string('observaciones', 1000)->nullable();
                $table->string('foto')->nullable();
                $table->decimal('numero_trozas', 10, 2);
                $table->decimal('volumen_estimado', 10, 2);
                $table->timestamps();
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('trozas');
    }
}
