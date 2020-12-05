<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterTablesGeneral extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (Schema::hasColumn('aserradores', 'procedencia_id'))
            Schema::table('aserradores', function (Blueprint $table) {
                $table->dropForeign('aserradores_procedencia_id_foreign');
                $table->dropColumn('procedencia_id');
            });
        Schema::table('formatos_entrega', function (Blueprint $table) {
            $table->decimal('factor_hueco_bultos', 10, 2)->nullable();
            $table->decimal('factor_hueco_sueltos', 10, 2)->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('formatos_entrega', function (Blueprint $table) {
            $table->dropColumn('factor_hueco_bultos');
            $table->dropColumn('factor_hueco_sueltos');
        });
    }
}
