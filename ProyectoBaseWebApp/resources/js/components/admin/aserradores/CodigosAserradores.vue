<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.codigos-aserradores') }}</h1>
        <masterForm
          :typeOptions="[
            {
                value: 'E',
                text: $t('message.delete'),
                visible: $store.getters.permiteAccion('eliminar_codigos_aserradores')
            }
        ]"
          :createButton="$store.getters.permiteAccion('crear_codigos_aserradores')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/codigos-aserradores"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('etiqueta.aserrador'),
                    field : 'aserrador.nombre',
                    sortable : true
                },
                {
                    label : $t('message.descripcion'),
                    field : 'descripcion',
                    sortable : true
                },
                {
                    label : $t('message.codigo'),
                    field : 'codigo',
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
                :label="$t('message.descripcion')"
              >
                <b-input v-model="form.descripcion"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.aserrador_id?errores.aserrador_id[0]:''"
                :type="errores.aserrador_id?'is-danger':''"
                :label="$t('etiqueta.aserrador')"
              >
                <b-select
                  v-model="form.aserrador_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in aserradores"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.nombre }} ({{ option.identificacion }})</option>
                </b-select>
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
        codigo: "",
        aserrador_id: "",
      },
      acciones: [],
      errores: {
        descripcion: undefined,
        codigo: undefined,
        aserrador_id: undefined,
      },
      aserradores: [],
    };
  },
  methods: {
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.descripcion = "";
      this.form.codigo = "";
      this.form.aserrador_id = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, codigosAserradores) {
      if (type === "E") {
        let codigosAserradoresId = [];
        for (let i = 0; i < codigosAserradores.length; i++)
          codigosAserradoresId.push(codigosAserradores[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/codigos-aserradores", {
            codigosAserradores: codigosAserradoresId,
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
    editar: function (codigoAserrador) {
      this.form.id = codigoAserrador.id;
      this.form.descripcion = codigoAserrador.descripcion;
      this.form.codigo = codigoAserrador.codigo;
      this.form.aserrador_id = codigoAserrador.aserrador_id;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.codigo = undefined;
      this.errores.aserrador_id = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/codigos-aserradores";
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
            this.errores.aserrador_id = response.data.errors.aserrador_id;
          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
    cargarAserradores: function () {
      this.$http
        .get(process.env.MIX_APP_URL_API + "/aserradores/listado")
        .then(({ data }) => {
          this.aserradores = data;
        });
    },
  },
  mounted: function () {
    this.cargarAserradores();
  },
};
</script>
