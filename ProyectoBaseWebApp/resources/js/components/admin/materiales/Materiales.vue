<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.materiales') }}</h1>
        <masterForm
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
  data: function() {
    return {
      form: {
        codigo: "",
        descripcion: "",
        id: "",
        _method: undefined
      },
      acciones: [],
      errores: {
        codigo: undefined,
        descripcion: undefined
      }
    };
  },
  methods: {
    canceled: function() {
      this.limpiar();
    },
    limpiar: function() {
      this.form.id = "";
      this.form._method = undefined;
      this.form.codigo = "";
      this.form.descripcion = "";
    },
    adding: function() {
      this.limpiar();
    },
    realizarAccion: function(type, materiales) {
      if (type === "E") {
        let materialesId = [];
        for (let i = 0; i < materiales.length; i++)
          materialesId.push(materiales[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/materiales", {
            materiales: materialesId,
            _method: "DELETE"
          })
          .then(() => {
            this.$buefy.toast.open({
              message: this.$t("message.guardado_generico"),
              type: "is-success"
            });
            this.$refs.masterForm.submit();
          })
          .catch(() => {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger"
            });
          });
      }
    },
    editar: function(material) {
      this.form.id = material.id;
      this.form.codigo = material.codigo;
      this.form.descripcion = material.descripcion;
    },
    limpiarErrores: function() {
      this.errores.descripcion = undefined;
      this.errores.codigo = undefined;
    },
    submitFormulario: function() {
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
            type: "is-success"
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
              type: "is-danger"
            });
          }
        });
    }
  }
};
</script>
