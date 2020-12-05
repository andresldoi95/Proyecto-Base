<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.formatos_entrega") }}</h1>
        <masterForm
          :typeOptions="[
            {
              value: 'E',
              text: $t('message.delete'),
              visible: $store.getters.permiteAccion(
                'eliminar_formatos_entrega'
              ),
            },
          ]"
          :createButton="$store.getters.permiteAccion('crear_formatos_entrega')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/formatos-entrega"
          :isPaginated="false"
          :columns="[
            {
              label: $t('message.id'),
              field: 'id',
              sortable: true,
            },
            {
              label: $t('message.descripcion'),
              field: 'descripcion',
              sortable: true,
            },
            {
              label: $t('message.factor_hueco'),
              field: 'factor_hueco',
              sortable: true,
            },
            {
              label: $t('message.status'),
              field: 'estado',
              sortable: true,
            },
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
                :message="errores.descripcion ? errores.descripcion[0] : ''"
                :type="errores.descripcion ? 'is-danger' : ''"
                :label="$t('message.descripcion')"
              >
                <b-input v-model="form.descripcion"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.factor_hueco ? errores.factor_hueco[0] : ''"
                :type="errores.factor_hueco ? 'is-danger' : ''"
                :label="$t('message.factor_hueco')"
              >
                <b-input v-model="form.factor_hueco"></b-input>
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
        factor_hueco: 0
      },
      acciones: [],
      errores: {
        descripcion: undefined,
        factor_hueco: undefined
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
      this.form.factor_hueco = undefined;
      this.form.descripcion = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, formatos_entrega) {
      if (type === "E") {
        let formatos_entregaId = [];
        for (let i = 0; i < formatos_entrega.length; i++)
          formatos_entregaId.push(formatos_entrega[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/formatos-entrega", {
            formatosEntrega: formatos_entregaId,
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
    editar: function (formatoEntrega) {
      this.form.id = formatoEntrega.id;
      this.form.descripcion = formatoEntrega.descripcion;
      this.form.factor_hueco = formatoEntrega.factor_hueco;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.factor_hueco = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/formatos-entrega";
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
