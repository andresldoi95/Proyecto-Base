<template>
  <section>
    <b-field grouped group-multiline>
        <div class="control">
            <b-button @click="agregar" icon-left="plus" native-type="button" type="is-success">
                {{ $t('message.agregar_fila') }}
            </b-button>
        </div>
    </b-field>
    <div class="b-table">
        <div class="table-wrapper">
            <table class="table is-striped is-narrow">
                <thead>
                    <tr>
                        <th>
                            {{ $t('message.filas') }}
                        </th> 
                        <th>
                            {{ $t('message.columnas') }}
                        </th> 
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-show="modelValue.length === 0">
                        <td colspan="3">
                            <strong>
                                {{ $t('message.sinDatos') }}
                            </strong>
                        </td>
                    </tr>
                    <tr :key="index" v-for="(fila, index) in modelValue">
                        <td>
                            <b-field
                                :message="errores.hasOwnProperty('filas.' + index + '.filas')?errores['filas.' + index + '.filas'][0]:''"
                                :type="errores.hasOwnProperty('filas.' + index + '.filas')?'is-danger':''"
                            >
                                <b-input v-model="fila.filas"></b-input>
                            </b-field>
                        </td>
                        <td>
                            <b-field
                                :message="errores.hasOwnProperty('filas.' + index + '.columnas')?errores['filas.' + index + '.columnas'][0]:''"
                                :type="errores.hasOwnProperty('filas.' + index + '.columnas')?'is-danger':''"
                            >
                                <b-input v-model="fila.columnas"></b-input>
                            </b-field>
                        </td>
                        <td>
                            <b-button @click="eliminar(index)" icon-left="delete" native-type="button" type="is-danger"></b-button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
  </section>
</template>

<script>
export default {
  props: {
      errores: {
          type: Object,
          required: false,
          default: function () {
              return {};
          }
      },
    value: {
      type: Array,
      required: false,
      default: function () {
        return [];
      },
    },
  },
  data: function () {
    return {
      modelValue: this.value,
    };
  },
  methods: {
    agregar: function () {
        this.modelValue.push({
            filas: 0,
            columnas: 0
        });
    },
    eliminar: function (index) {
        this.modelValue.splice(index, 1);
    }
  }
};
</script>
