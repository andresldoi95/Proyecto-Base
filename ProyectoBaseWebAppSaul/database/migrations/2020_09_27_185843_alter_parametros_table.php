<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterParametrosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (Schema::hasTable('parametros')) {
            Schema::table('parametros', function (Blueprint $table) {
                $table->dropColumn('factor_hueco');
                $table->dropColumn('constante');
                $table->dropColumn('ancho_bulto');
                $table->decimal('factor_hueco_bultos', 10, 2)->nullable();
                $table->decimal('factor_hueco_sueltos', 10, 2)->nullable();
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
        Schema::table('parametros', function (Blueprint $table) {
            $table->dropColumn('factor_hueco_bultos');
            $table->dropColumn('factor_hueco_sueltos');
            $table->decimal('factor_hueco', 10, 2)->nullable();
            $table->decimal('constante', 10, 2)->nullable();
            $table->decimal('ancho_bultos', 10, 2)->nullable();
        });
    }
}
