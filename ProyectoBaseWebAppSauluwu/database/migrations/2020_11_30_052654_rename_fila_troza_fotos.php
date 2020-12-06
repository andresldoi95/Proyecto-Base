<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class RenameFilaTrozaFotos extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('troza_fotos', function (Blueprint $table) {
            //
            $table->dropColumn('fila_id');
            $table->string('troza_id', 36);
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('troza_fotos', function (Blueprint $table) {
            //
        });
    }
}
