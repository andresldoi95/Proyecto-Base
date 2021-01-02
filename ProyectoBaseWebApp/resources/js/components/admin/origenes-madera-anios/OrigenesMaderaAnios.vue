<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.origenes_madera_anios") }}</h1>
        <masterForm
          :typeOptions="[
            {
              value: 'E',
              text: $t('message.delete'),
              visible: $store.getters.permiteAccion('eliminar_origenes_madera'),
            },
          ]"
          :createButton="$store.getters.permiteAccion('crear_origenes_madera')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/origenes-madera-anios"
          :isPaginated="false"
          :columns="[
            {
              label: $t('message.id'),
              field: 'id',
              sortable: true,
            },
            {
              label : $t('message.origen_madera'),
              field : 'origen_madera.descripcion',
              sortable : true
            },
            {
              label: $t('message.anio_cultivo'),
              field: 'anio_cultivo',
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
                :message="errores.origen_madera_id?errores.origen_madera_id[0]:''"
                :type="errores.origen_madera_id?'is-danger':''"
                :label="$t('message.origen_madera')"
              >
                <b-select
                  v-model="form.origen_madera_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in origenes_madera"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.anio_cultivo ? errores.anio_cultivo[0] : ''"
                :type="errores.anio_cultivo ? 'is-danger' : ''"
                :label="$t('message.anio_cultivo')"
              >
                <b-input v-model="form.anio_cultivo"></b-input>
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
        anio_cultivo: "",
        origen_madera_id: "",
        id: "",
        _method: undefined,
      },
      acciones: [],
      origenes_madera: [],
      errores: {
        anio_cultivo: undefined,
        origen_madera_id: undefined,

      },
    };
  },
  methods: {
    cargarOrigenesMadera: function () {
          let path = process.env.MIX_APP_URL_API + "/origenes-madera/listado";
          this.$http.get(path).then(({data}) => {
              this.origenes_madera = data;
          });
      },
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.origen_madera_id = '';
      this.form.anio_cultivo = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, origenes_madera) {
      if (type === "E") {
        let origenes_maderaId = [];
        for (let i = 0; i < origenes_madera.length; i++)
          origenes_maderaId.push(origenes_madera[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/origenes-madera-anios", {
            origenesMadera: origenes_maderaId,
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
    editar: function (origenMadera) {
      this.form.id = origenMadera.id;
      this.form.origen_madera_id = origenMadera.origen_madera_id;
      this.form.anio_cultivo = origenMadera.anio_cultivo;
    },
    limpiarErrores: function () {
      this.errores.anio_cultivo = undefined;
      this.errores.origen_madera_id = undefined;

    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/origenes-madera-anios";
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
            this.errores.anio_cultivo = response.data.errors.anio_cultivo;
            this.errores.origen_madera_id = response.data.errors.origen_madera_id;

          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
  },
  mounted : function () {
      this.cargarOrigenesMadera();
  },
};
</script>
