<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterMaterialesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasColumn('materiales', 'tipo_madera_id')) {
            Schema::table('materiales', function (Blueprint $table) {
                $table->unsignedBigInteger('tipo_madera_id')->nullable();
                $table->foreign('tipo_madera_id')->references('id')->on('tipos_madera');
            });
        }
        if (!Schema::hasColumn('materiales', 'formato_entrega_id')) {
            Schema::table('materiales', function (Blueprint $table) {
                $table->unsignedBigInteger('formato_entrega_id')->nullable();
                $table->foreign('formato_entrega_id')->references('id')->on('formatos_entrega');
            });
        }
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
