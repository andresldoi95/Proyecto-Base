<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.correos') }}</h1>
        <masterForm
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/correos"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.nombre'),
                    field : 'nombre',
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
                :message="errores.nombre?errores.nombre[0]:''"
                :type="errores.nombre?'is-danger':''"
                :label="$t('message.nombre')"
              >
                <b-input v-model="form.nombre"></b-input>
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
        nombre: "",
        id: "",
        _method: undefined,
        email: "",
      },
      acciones: [],
      errores: {
        nombre: undefined,
        email: undefined,
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
      this.form.nombre = "";
      this.form.email = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, correos) {
      if (type === "E") {
        let correosId = [];
        for (let i = 0; i < correos.length; i++) correosId.push(correos[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/correos", {
            correos: correosId,
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
    editar: function (correo) {
      this.form.id = correo.id;
      this.form.nombre = correo.nombre;
      this.form.email = correo.email;
    },
    limpiarErrores: function () {
      this.errores.nombre = undefined;
      this.errores.email = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/correos";
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
            this.errores.email = response.data.errors.email;
            this.errores.nombre = response.data.errors.nombre;
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
