<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class DropFactoresHueco extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (Schema::hasColumn('formatos_entrega', 'factor_hueco_bultos')) {
            Schema::table('formatos_entrega', function (Blueprint $table) {
                $table->dropColumn('factor_hueco_bultos');
            });
        }
        if (Schema::hasColumn('formatos_entrega', 'factor_hueco_sueltos')) {
            Schema::table('formatos_entrega', function (Blueprint $table) {
                $table->dropColumn('factor_hueco_sueltos');
            });
        }
        Schema::table('formatos_entrega', function (Blueprint $table) {
            $table->decimal('factor_hueco', 10, 2)->nullable();
        });
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
