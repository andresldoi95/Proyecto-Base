<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterCamionesTable2 extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (Schema::hasTable('camiones')) {
            Schema::table('camiones', function (Blueprint $table) {
                $table->string('codigo_vendor', 50)->nullable();
                $table->integer('filas')->nullable();
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
        Schema::table('camiones', function (Blueprint $table) {
            $table->dropColumn('codigo_vendor');
            $table->dropColumn('filas');
        });
    }
}
