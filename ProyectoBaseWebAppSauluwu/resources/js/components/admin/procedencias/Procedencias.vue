<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.procedencias') }}</h1>
        <masterForm
          :typeOptions="[
                {
                    value: 'E',
                    text: $t('message.delete'),
                    visible: $store.getters.permiteAccion('eliminar_procedencias')
                }
            ]"
          :createButton="$store.getters.permiteAccion('crear_procedencias')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/procedencias"
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
                    label : $t('message.email'),
                    field : 'email',
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
                :message="errores.email?errores.email[0]:''"
                :type="errores.email?'is-danger':''"
                :label="$t('message.email')"
              >
                <b-input v-model="form.email"></b-input>
              </b-field>
            </div>
            <div class="column">
                <label class="label" for="materiales">{{ $t('etiqueta.materiales') }}</label>
                <div v-for="material in materiales" :key="material.id" class="field">
                    <b-checkbox
                    v-model="form.materiales"
                    :native-value="material.id"
                    >{{ material.descripcion }}</b-checkbox>
                </div>
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
        email: "",
        materiales: []
      },
      acciones: [],
      errores: {
        codigo: undefined,
        descripcion: undefined,
        email: undefined
      },
      materiales: []
    };
  },
  methods: {
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.codigo = "";
      this.form.descripcion = "";
      this.form.email = "";
      this.form.materiales.splice(0, this.form.materiales.length);
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, procedencias) {
      if (type === "E") {
        let procedenciasId = [];
        for (let i = 0; i < procedencias.length; i++)
          procedenciasId.push(procedencias[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/procedencias", {
            procedencias: procedenciasId,
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
      this.form.codigo = procedencia.codigo;
      this.form.descripcion = procedencia.descripcion;
      this.form.email = procedencia.email;
      this.form.materiales.splice(0, this.form.materiales.length);
      for (let i = 0; i < procedencia.materiales.length; i++)
        this.form.materiales.push(procedencia.materiales[i].id);
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.codigo = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/procedencias";
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
      this.$http.get(process.env.MIX_APP_URL_API + '/materiales/listado').then(({data}) => {
          this.materiales = data;
      });
  }
};
</script>
