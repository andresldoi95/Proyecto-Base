<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterDespachosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (!Schema::hasColumn('despachos', 'origen_hacienda_id')) {
            Schema::table('despachos', function (Blueprint $table) {
                $table->unsignedBigInteger('origen_hacienda_id')->nullable();
                $table->foreign('origen_hacienda_id')->references('id')->on('origenes_hacienda');
            });
        }
        if (!Schema::hasColumn('filas_sueltos', 'tipo_bulto_id')) {
            Schema::table('filas_sueltos', function (Blueprint $table) {
                $table->unsignedBigInteger('tipo_bulto_id')->nullable();
                $table->foreign('tipo_bulto_id')->references('id')->on('tipos_bulto');
            });
        }
        Schema::table('filas_sueltos', function (Blueprint $table) {
            $table->dropForeign('filas_sueltos_espesor_id_foreign');
            $table->dropForeign('filas_sueltos_largo_id_foreign');
            $table->dropColumn('espesor_id');
            $table->dropColumn('largo_id');
        });
        Schema::table('filas_sueltos', function (Blueprint $table) {
            $table->unsignedBigInteger('espesor_id')->nullable();
            $table->unsignedBigInteger('largo_id')->nullable();
        });
        if (!Schema::hasColumn('despachos', 'numero_documento')) {
            Schema::table('despachos', function (Blueprint $table) {
                $table->char('numero_documento', 8)->unique();
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

    }
}
