<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.origenes_hacienda") }}</h1>
        <masterForm
          :typeOptions="[
            {
              value: 'E',
              text: $t('message.delete'),
              visible: $store.getters.permiteAccion('eliminar_origenes_hacienda'),
            },
          ]"
          :createButton="$store.getters.permiteAccion('crear_origenes_hacienda')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/origenes-hacienda"
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
        _method: undefined
      },
      acciones: [],
      errores: {
        descripcion: undefined
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
      this.form.descripcion = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, origenes_hacienda) {
      if (type === "E") {
        let origenes_haciendaId = [];
        for (let i = 0; i < origenes_hacienda.length; i++)
          origenes_haciendaId.push(origenes_hacienda[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/origenes-hacienda", {
            origenesHacienda: origenes_haciendaId,
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
    editar: function (origenHacienda) {
      this.form.id = origenHacienda.id;
      this.form.descripcion = origenHacienda.descripcion;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/origenes-hacienda";
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
