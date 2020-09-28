<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMaterialesProcedenciaTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasTable('materiales_procedencia'))
            Schema::create('materiales_procedencia', function (Blueprint $table) {
                $table->unsignedBigInteger('material_id');
                $table->unsignedBigInteger('procedencia_id');
                $table->foreign('material_id')->references('id')->on('materiales');
                $table->foreign('procedencia_id')->references('id')->on('procedencias');
                $table->primary(['material_id', 'procedencia_id']);
            });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('materiales_procedencia');
    }
}
