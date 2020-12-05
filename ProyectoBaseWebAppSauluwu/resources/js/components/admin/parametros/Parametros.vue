<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.parametros") }}</h1>
        <form @submit.prevent="submitFormulario">
          <div class="columns">
            <div class="column">
              <b-field
                :message="
                  errores.factor_hueco_bultos
                    ? errores.factor_hueco_bultos[0]
                    : ''
                "
                :type="errores.factor_hueco_bultos ? 'is-danger' : ''"
                :label="$t('message.factor_hueco_bultos')"
              >
                <b-input v-model="form.factor_hueco_bultos"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="
                  errores.factor_hueco_sueltos
                    ? errores.factor_hueco_sueltos[0]
                    : ''
                "
                :type="errores.factor_hueco_sueltos ? 'is-danger' : ''"
                :label="$t('message.factor_hueco_sueltos')"
              >
                <b-input v-model="form.factor_hueco_sueltos"></b-input>
              </b-field>
            </div>
          </div>
          <div class="buttons">
            <b-button native-type="submit" type="is-primary">{{
              $t("message.guardar")
            }}</b-button>
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
        factor_hueco_sueltos: "",
        factor_hueco_bultos: "",
      },
      acciones: [],
      errores: {
        factor_hueco_sueltos: undefined,
        factor_hueco_bultos: undefined,
      },
    };
  },
  methods: {
    limpiarErrores: function () {
      this.errores.factor_hueco_sueltos = undefined;
      this.errores.factor_hueco_bultos = undefined;
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
            this.errores.factor_hueco_bultos =
              response.data.errors.factor_hueco_bultos;
            this.errores.factor_hueco_sueltos =
              response.data.errors.factor_hueco_sueltos;
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
    this.$http.get(path).then(({ data }) => {
      if (data.parametros !== null) {
        this.form.factor_hueco_sueltos = data.parametros.factor_hueco_sueltos;
        this.form.factor_hueco_bultos = data.parametros.factor_hueco_bultos;
      }
    });
  },
};
</script>
