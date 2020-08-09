<?php

use App\Modulo;
use Illuminate\Database\Seeder;

class ModuloSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Modulo::create([
            'id' => 'usuarios',
            'nombre' => 'Usuarios'
        ]);
        Modulo::create([
            'id' => 'roles',
            'nombre' => 'Roles de usuario'
        ]);
        Modulo::create([
            'id' => 'aserradores',
            'nombre' => 'Aserradores'
        ]);
        Modulo::create([
            'id' => 'camiones',
            'nombre' => 'Camiones'
        ]);
        Modulo::create([
            'id' => 'controladores',
            'nombre' => 'Controladores'
        ]);
        Modulo::create([
            'id' => 'correos',
            'nombre' => 'Correos'
        ]);
        Modulo::create([
            'id' => 'destinos',
            'nombre' => 'Destinos'
        ]);
        Modulo::create([
            'id' => 'espesores',
            'nombre' => 'Espesores'
        ]);
        Modulo::create([
            'id' => 'largos',
            'nombre' => 'Largos'
        ]);
        Modulo::create([
            'id' => 'materiales',
            'nombre' => 'Materiales'
        ]);
        Modulo::create([
            'id' => 'procedencias',
            'nombre' => 'Procedencias'
        ]);
    }
}
