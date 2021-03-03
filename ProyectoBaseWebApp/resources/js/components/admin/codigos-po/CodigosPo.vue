<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.codigos_po') }}</h1>
        <masterForm
          :typeOptions="[
                {
                    value: 'E',
                    text: $t('message.delete'),
                    visible: $store.getters.permiteAccion('eliminar_codigo_po')
                }
            ]"
          :createButton="$store.getters.permiteAccion('crear_codigo_po')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/codigos-po"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.codigo'),
                    field : 'descripcion',
                    sortable : true
                },
                {
                    label : $t('etiqueta.materiales'),
                    field : 'material.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.destino'),
                    field : 'destino.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.orden_hacienda'),
                    field : 'origen_madera_anio.codigo_hacienda',
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
                :message="errores.descripcion?errores.descripcion[0]:''"
                :type="errores.descripcion?'is-danger':''"
                :label="$t('message.codigo')"
              >
                <b-input v-model="form.descripcion"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.material_id?errores.material_id[0]:''"
                :type="errores.material_id?'is-danger':''"
                :label="$t('etiqueta.materiales')"
              >
                <b-select
                  v-model="form.material_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in materiales"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.destino_id?errores.destino_id[0]:''"
                :type="errores.destino_id?'is-danger':''"
                :label="$t('title.destinos')"
              >
                <b-select
                  v-model="form.destino_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in destinos"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.origen_madera_id?errores.origen_madera_id[0]:''"
                :type="errores.origen_madera_id?'is-danger':''"
                :label="$t('etiqueta.haciendas')"
                @change.native="consultarOrigenMaderaAnios"
              >
                <b-select
                  v-model="form.origen_madera_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in origenesMadera"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.origen_madera_anio_id?errores.origen_madera_anio_id[0]:''"
                :type="errores.origen_madera_anio_id?'is-danger':''"
                :label="$t('message.orden_hacienda')"
              >
                <b-select
                  id="select_origen_madera_anio_id"
                  v-model="form.origen_madera_anio_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in origenesMaderaAnios"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.codigo_hacienda }}</option>
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
      form: {
        descripcion: "",
        id: "",
        _method: undefined,
        material_id: '',
        destino_id: '',
        origen_madera_id: '',
        origen_madera_anio_id: ''
      },
      materiales: [],
      destinos: [],
      origenesMadera: [],
      origenesMaderaAnios: [],
      errores: {
        descripcion: undefined,
        material_id: undefined,
        destino_id: undefined,
        origen_madera_anio_id: undefined
      },
    };
  },
  methods: {
      consultarOrigenMaderaAnios: function () {
          let path = process.env.MIX_APP_URL_API + "/get-origenes-madera-anios";
          this.$http
            .post(path, this.form)
            .then(({data}) => {
                this.origenesMaderaAnios = data;

            })
            .catch(({ response }) => {
              
            });
          
      },
      cargarMateriales: function () {
          let path = process.env.MIX_APP_URL_API + "/materiales/listado";
          this.$http.get(path).then(({data}) => {
              this.materiales = data;
          });
      },
      cargarDestinos: function () {
          let path = process.env.MIX_APP_URL_API + "/destinos/listado";
          this.$http.get(path).then(({data}) => {
              this.destinos = data;
          });
      },
      cargarHacienda: function () {
          let path = process.env.MIX_APP_URL_API + "/origenes-madera/listado";
          this.$http.get(path).then(({data}) => {
              this.origenesMadera = data;
          });
      },
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.descripcion = "";
      this.form.destino_id = '';
      this.form.origen_madera_anio_id = '';
      this.form.origen_madera_id = '';
      this.form.material_id = '';
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, codigos_po) {
      if (type === "E") {
        let codigos_poId = [];
        for (let i = 0; i < codigos_po.length; i++)
          codigos_poId.push(codigos_po[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/codigos-po", {
            codigos_po: codigos_poId,
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
    editar: function (codigo_po) {
      this.form.id = codigo_po.id;
      this.form.destino_id = codigo_po.destino_id;
      this.form.material_id = codigo_po.material_id;
      this.form.descripcion = codigo_po.descripcion;
      this.form.origen_madera_id = codigo_po.origen_madera_anio.origen_madera_id;
      this.consultarOrigenMaderaAnios();
      setTimeout(function() {
        document.getElementById('select_origen_madera_anio_id').value = codigo_po.origen_madera_anio_id;

      }, 1000);
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.material_id = undefined;
      this.errores.destino_id = undefined;
      this.errores.origen_madera_anio_id = undefined;

    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/codigos-po";
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
            this.errores.descripcion = response.data.errors.descripcion;
            this.errores.destino_id = response.data.errors.destino_id;
            this.errores.origen_madera_anio_id = response.data.errors.origen_madera_anio_id;
            this.errores.material_id = response.data.material_id;
          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
  },
  mounted : function () {
      this.cargarMateriales();
      this.cargarDestinos();
      this.cargarHacienda();
  }
};
</script>
