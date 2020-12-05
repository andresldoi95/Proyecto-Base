<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.origenes_madera") }}</h1>
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
          resource="/api/origenes-madera"
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
              label: $t('message.anio_cultivo'),
              field: 'anio_cultivo',
              sortable: true,
            },
            {
              label: $t('message.hectareas'),
              field: 'hectareas',
              sortable: true,
            },
            {
              label: $t('message.volumen_inventario'),
              field: 'volumen_inventario',
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
                :message="errores.anio_cultivo ? errores.anio_cultivo[0] : ''"
                :type="errores.anio_cultivo ? 'is-danger' : ''"
                :label="$t('message.anio_cultivo')"
              >
                <b-input v-model="form.anio_cultivo"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="
                  errores.volumen_inventario
                    ? errores.volumen_inventario[0]
                    : ''
                "
                :type="errores.volumen_inventario ? 'is-danger' : ''"
                :label="$t('message.volumen_inventario')"
              >
                <b-input v-model="form.volumen_inventario"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.hectareas ? errores.hectareas[0] : ''"
                :type="errores.hectareas ? 'is-danger' : ''"
                :label="$t('message.hectareas')"
              >
                <b-input v-model="form.hectareas"></b-input>
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
        hectareas: "",
        descripcion: "",
        id: "",
        _method: undefined,
        anio_cultivo: "",
        volumen_inventario: "",
      },
      acciones: [],
      errores: {
        hectareas: undefined,
        descripcion: undefined,
        anio_cultivo: undefined,
        volumen_inventario: undefined,
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
      this.form.hectareas = "";
      this.form.descripcion = "";
      this.form.anio_cultivo = "";
      this.form.volumen_inventario = "";
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
          .post(process.env.MIX_APP_URL_API + "/origenes-madera", {
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
      this.form.hectareas = origenMadera.hectareas;
      this.form.descripcion = origenMadera.descripcion;
      this.form.anio_cultivo = origenMadera.anio_cultivo;
      this.form.volumen_inventario = origenMadera.volumen_inventario;
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.hectareas = undefined;
      this.errores.anio_cultivo = undefined;
      this.errores.volumen_inventario = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/origenes-madera";
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
            this.errores.hectareas = response.data.errors.hectareas;
            this.errores.volumen_inventario =
              response.data.errors.volumen_inventario;
            this.errores.anio_cultivo = response.data.errors.anio_cultivo;
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
