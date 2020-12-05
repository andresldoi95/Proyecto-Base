<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateHaciendasMaderaTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('haciendas_madera'))
            Schema::create('haciendas_madera', function (Blueprint $table) {
                $table->unsignedBigInteger('origen_madera_id');
                $table->unsignedBigInteger('hacienda_id');
                $table->foreign('origen_madera_id')->references('id')->on('origenes_hacienda');
                $table->foreign('hacienda_id')->references('id')->on('origenes_madera');
                $table->primary(['origen_madera_id', 'hacienda_id']);
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('haciendas_madera');
    }
}
