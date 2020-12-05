<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.materiales') }}</h1>
        <masterForm
          :typeOptions="[
                {
                    value: 'E',
                    text: $t('message.delete'),
                    visible: $store.getters.permiteAccion('eliminar_materiales')
                }
            ]"
          :createButton="$store.getters.permiteAccion('crear_materiales')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/materiales"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.codigo'),
                    field : 'codigo',
                    sortable : true
                },
                {
                    label : $t('message.descripcion'),
                    field : 'descripcion',
                    sortable : true
                },
                {
                    label : $t('etiqueta.tipo_madera'),
                    field : 'tipo_madera.descripcion',
                    sortable : true
                },
                {
                    label : $t('etiqueta.origen_hacienda'),
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
                :message="errores.codigo?errores.codigo[0]:''"
                :type="errores.codigo?'is-danger':''"
                :label="$t('message.codigo')"
              >
                <b-input v-model="form.codigo"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.descripcion?errores.descripcion[0]:''"
                :type="errores.descripcion?'is-danger':''"
                :label="$t('message.descripcion')"
              >
                <b-input v-model="form.descripcion"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.tipo_madera_id?errores.tipo_madera_id[0]:''"
                :type="errores.tipo_madera_id?'is-danger':''"
                :label="$t('etiqueta.tipo_madera')"
              >
                <b-select
                  v-model="form.tipo_madera_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in tiposMadera"
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
                :label="$t('etiqueta.origen_hacienda')"
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
        codigo: "",
        descripcion: "",
        id: "",
        _method: undefined,
        tipo_madera_id: '',
        origen_madera_id: ''
      },
      tiposMadera: [],
      origenesMadera: [],
      errores: {
        codigo: undefined,
        descripcion: undefined,
        tipo_madera_id: undefined,
        origen_madera_id: undefined
      },
    };
  },
  methods: {
      cargarTiposMadera: function () {
          let path = process.env.MIX_APP_URL_API + "/tipos-madera/listado";
          this.$http.get(path).then(({data}) => {
              this.tiposMadera = data;
          });
      },
      cargarOrigenesMadera: function () {
          let path = process.env.MIX_APP_URL_API + "/origenes-hacienda/listado";
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
      this.form.codigo = "";
      this.form.descripcion = "";
      this.form.origen_madera_id = '';
      this.form.tipo_madera_id = '';
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, materiales) {
      if (type === "E") {
        let materialesId = [];
        for (let i = 0; i < materiales.length; i++)
          materialesId.push(materiales[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/materiales", {
            materiales: materialesId,
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
    editar: function (material) {
      this.form.id = material.id;
      this.form.codigo = material.codigo;
      this.form.origen_madera_id = material.origen_madera_id;
      this.form.tipo_madera_id = material.tipo_madera_id;
      this.form.descripcion = material.descripcion;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.codigo = undefined;
      this.errores.tipo_madera_id = undefined;
      this.errores.origen_madera_id = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/materiales";
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
            this.errores.codigo = response.data.errors.codigo;
            this.errores.descripcion = response.data.errors.descripcion;
            this.errores.origen_madera_id = response.data.errors.origen_madera_id;
            this.errores.tipo_madera_id = response.data.tipo_madera_id;
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
      this.cargarTiposMadera();
      this.cargarOrigenesMadera();
  }
};
</script>
