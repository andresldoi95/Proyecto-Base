<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.tarifas') }}</h1>
        <masterForm
          :typeOptions="[
                {
                    value: 'E',
                    text: $t('message.delete'),
                    visible: $store.getters.permiteAccion('eliminar_tarifas')
                }
            ]"
          :createButton="$store.getters.permiteAccion('crear_tarifas')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/tarifas"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.tipo_camion'),
                    field : 'descripcion_tipo_camion',
                    sortable : true
                },
                 {
                    label : $t('message.destino'),
                    field : 'destino.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.valor'),
                    field : 'valor',
                    sortable : true
                },
                {
                    label : $t('message.origen_madera'),
                    field : 'origen_madera.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.status'),
                    field : 'estado',
                    sortable : true
                }
            ]"
        >
          <div class="columns">
            <div class="column">
              <b-field :label="$t('message.id')">
                <b-input readonly v-model="form.id"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.valor?errores.valor[0]:''"
                :type="errores.valor?'is-danger':''"
                :label="$t('message.valor')"
              >
                <b-input v-model="form.valor"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.tipo_camion?errores.tipo_camion[0]:''"
                :type="errores.tipo_camion?'is-danger':''"
                :label="$t('message.tipo_camion')"
              >
                <b-select v-model="form.tipo_camion" expanded :placeholder="$t('title.seleccione')">
                  <option value="B">Bananero</option>
                  <option value="T">Trailer</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.origen_madera_id?errores.origen_madera_id[0]:''"
                :type="errores.origen_madera_id?'is-danger':''"
                :label="$t('message.origen_madera')"
              >
                <b-select v-model="form.origen_madera_id" expanded :placeholder="$t('title.seleccione')">
                  <option v-for="origenMadera in origenesMadera" :key="origenMadera.id" :value="origenMadera.id">
                      {{ origenMadera.descripcion }}
                  </option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.destino_id?errores.destino_id[0]:''"
                :type="errores.destino_id?'is-danger':''"
                :label="$t('message.destino')"
              >
                <b-select v-model="form.destino_id" expanded :placeholder="$t('title.seleccione')">
                  <option v-for="destino in destinos" :key="destino.id" :value="destino.id">
                      {{ destino.descripcion }}
                  </option>
                </b-select>
              </b-field>
            </div>
          </div>
        </masterForm>
      </div>
    </div>
  </section>
</template>

<script>
import MasterForm from "../../layouts/MasterForm";
export default {
  components: { MasterForm },
  data: function () {
    return {
        origenesMadera: [],
        destinos: [],
      form: {
        destino_id: "",
        valor: "",
        id: "",
        tipo_camion: "B",
        _method: undefined,
        origen_madera_id: ""
      },
      acciones: [],
      errores: {
        destino_id: undefined,
        valor: undefined,
        origen_madera_id: undefined
      }
    };
  },
  methods: {
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.destino_id = "";
      this.form.valor = "";
      this.form.tipo_camion = "B";
      this.form.origen_madera_id = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, tarifas) {
      if (type === "E") {
        let tarifasId = [];
        for (let i = 0; i < tarifas.length; i++)
          tarifasId.push(tarifas[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/tarifas", {
            tarifas: tarifasId,
            _method: "DELETE",
          })
          .then(() => {
            this.$buefy.toast.open({
              message: this.$t("message.guardado_generico"),
              type: "is-success",
            });
            this.$refs.masterForm.submit();
          })
          .catch(() => {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          });
      }
    },
    editar: function (procedencia) {
      this.form.id = procedencia.id;
      this.form.destino_id = procedencia.destino_id;
      this.form.valor = procedencia.valor;
      this.form.tipo_camion = procedencia.tipo_camion;
      this.form.origen_madera_id = procedencia.origen_madera_id;
    },
    limpiarErrores: function () {
      this.errores.valor = undefined;
      this.errores.destino_id = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/tarifas";
      if (this.form.id !== "") {
        path += "/" + this.form.id;
        this.form._method = "PUT";
      } else this.form._method = undefined;
      this.$http
        .post(path, this.form)
        .then(() => {
          this.$buefy.toast.open({
            message: this.$t("message.guardado_generico"),
            type: "is-success",
          });
          this.$refs.masterForm.submit();
        })
        .catch(({ response }) => {
          let status = response.status;
          if (status === 422) {
            this.errores.destino_id = response.data.errors.destino_id;
            this.errores.valor = response.data.errors.valor;
          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
  },
  mounted: function () {
      this.$http.get(process.env.MIX_APP_URL_API + '/origenes-madera/listado').then(({data}) => {
          this.origenesMadera = data;
      });
      this.$http.get(process.env.MIX_APP_URL_API + '/destinos/listado').then(({data}) => {
          this.destinos = data;
      });
  }
};
</script>
