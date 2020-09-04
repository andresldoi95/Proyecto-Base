<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.parametros') }}</h1>
        <form @submit.prevent="submitFormulario">
          <div class="columns">
            <div class="column">
              <b-field
                :message="errores.factor_hueco?errores.factor_hueco[0]:''"
                :type="errores.factor_hueco?'is-danger':''"
                :label="$t('message.factor_hueco')"
              >
                <b-input v-model="form.factor_hueco"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.constante?errores.constante[0]:''"
                :type="errores.constante?'is-danger':''"
                :label="$t('message.constante')"
              >
                <b-input v-model="form.constante"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.ancho_bulto?errores.ancho_bulto[0]:''"
                :type="errores.ancho_bulto?'is-danger':''"
                :label="$t('message.ancho_bulto')"
              >
                <b-input v-model="form.ancho_bulto"></b-input>
              </b-field>
            </div>
          </div>
          <div class="buttons">
            <b-button native-type="submit" type="is-primary">{{ $t('message.guardar') }}</b-button>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data: function () {
    return {
      form: {
        ancho_bulto: "",
        constante: "",
        factor_hueco: "",
      },
      acciones: [],
      errores: {
        ancho_bulto: undefined,
        constante: undefined,
        factor_hueco: undefined,
      },
    };
  },
  methods: {
    limpiarErrores: function () {
      this.errores.ancho_bulto = undefined;
      this.errores.constante = undefined;
      this.errores.factor_hueco = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/parametros";
      this.$http
        .post(path, this.form)
        .then(() => {
          this.$buefy.toast.open({
            message: this.$t("message.guardado_generico"),
            type: "is-success",
          });
        })
        .catch(({ response }) => {
          let status = response.status;
          if (status === 422) {
            this.errores.constante = response.data.errors.constante;
            this.errores.ancho_bulto = response.data.errors.ancho_bulto;
            this.errores.factor_hueco = response.data.errors.factor_hueco;
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
      let path = process.env.MIX_APP_URL_API + "/parametros";
      this.$http.get(path).then(({data}) => {
          if (data.parametros !== null) {
              this.form.ancho_bulto = data.parametros.ancho_bulto;
              this.form.constante = data.parametros.constante;
              this.form.factor_hueco = data.parametros.factor_hueco;
          }
      });
  }
};
</script>
