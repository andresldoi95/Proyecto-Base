<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AlterMaterialesIITable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if (Schema::hasColumn('materiales', 'formato_entrega_id')) {
            Schema::table('materiales', function (Blueprint $table) {
                $table->dropForeign('materiales_formato_entrega_id_foreign');
                $table->dropColumn('formato_entrega_id');
            });
        }
        if (!Schema::hasColumn('materiales', 'origen_madera_id')) {
            Schema::table('materiales', function (Blueprint $table) {
                $table->unsignedBigInteger('origen_madera_id')->nullable();
                $table->foreign('origen_madera_id')->references('id')->on('origenes_hacienda');
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
