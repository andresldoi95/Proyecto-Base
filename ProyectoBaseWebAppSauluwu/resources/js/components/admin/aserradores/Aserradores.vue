<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.aserradores') }}</h1>
        <masterForm
          :typeOptions="[
            {
                value: 'E',
                text: $t('message.delete'),
                visible: $store.getters.permiteAccion('eliminar_aserradores')
            }
        ]"
          :createButton="$store.getters.permiteAccion('crear_aserradores')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/aserradores"
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
                    label : $t('message.identificacion'),
                    field : 'identificacion',
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
                :message="errores.identificacion?errores.identificacion[0]:''"
                :type="errores.identificacion?'is-danger':''"
                :label="$t('message.identificacion')"
              >
                <b-input v-model="form.identificacion"></b-input>
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
        identificacion: ""
      },
      acciones: [],
      errores: {
        nombre: undefined,
        identificacion: undefined
      }
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
      this.form.identificacion = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, aserradores) {
      if (type === "E") {
        let aserradoresId = [];
        for (let i = 0; i < aserradores.length; i++)
          aserradoresId.push(aserradores[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/aserradores", {
            aserradores: aserradoresId,
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
    editar: function (aserrador) {
      this.form.id = aserrador.id;
      this.form.nombre = aserrador.nombre;
      this.form.identificacion = aserrador.identificacion;
    },
    limpiarErrores: function () {
      this.errores.nombre = undefined;
      this.errores.identificacion = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/aserradores";
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
            this.errores.identificacion = response.data.errors.identificacion;
            this.errores.nombre = response.data.errors.nombre;
          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    }
  }
};
</script>
