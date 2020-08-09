<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.espesores') }}</h1>
        <masterForm
          :typeOptions="[
                {
                    value: 'E',
                    text: $t('message.delete'),
                    visible: $store.getters.permiteAccion('eliminar_espesores')
                }
            ]"
          :createButton="$store.getters.permiteAccion('crear_espesores')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/espesores"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.descripcion'),
                    field : 'descripcion',
                    sortable : true
                },
                {
                    label : $t('message.valor'),
                    field : 'valor',
                    sortable : true
                },
                {
                    label : $t('message.color'),
                    field : 'color',
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
                :message="errores.valor?errores.valor[0]:''"
                :type="errores.valor?'is-danger':''"
                :label="$t('message.valor')"
              >
                <b-input v-model="form.valor"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.color?errores.color[0]:''"
                :type="errores.color?'is-danger':''"
                :label="$t('message.color')"
              >
                <b-input v-model="form.color"></b-input>
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
        valor: "",
        descripcion: "",
        id: "",
        _method: undefined,
        color: "",
      },
      acciones: [],
      errores: {
        valor: undefined,
        descripcion: undefined,
        color: undefined,
      },
    };
  },
  methods: {
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.valor = "";
      this.form.descripcion = "";
      this.form.color = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, espesores) {
      if (type === "E") {
        let espesoresId = [];
        for (let i = 0; i < espesores.length; i++)
          espesoresId.push(espesores[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/espesores", {
            espesores: espesoresId,
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
    editar: function (espesor) {
      this.form.id = espesor.id;
      this.form.valor = espesor.valor;
      this.form.descripcion = espesor.descripcion;
      this.form.color = espesor.color;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.valor = undefined;
      this.errores.color = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/espesores";
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
            this.errores.valor = response.data.errors.valor;
            this.errores.descripcion = response.data.errors.descripcion;
            this.errores.color = response.data.errors.color;
          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
  },
};
</script>
