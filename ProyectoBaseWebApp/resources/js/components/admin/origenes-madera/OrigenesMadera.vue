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
                :message="errores.descripcion ? errores.descripcion[0] : ''"
                :type="errores.descripcion ? 'is-danger' : ''"
                :label="$t('message.descripcion')"
              >
                <b-input v-model="form.descripcion"></b-input>
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
        <hr>
          <div id="add_anio_div" style="display:none;">
            <h1 class="title">{{ $t("title.origenes_madera_anios") }}</h1>
            <b-input  v-model="NeworigenMaderas.id" style="display:none;" id ="origen_madera_id_filter_2"></b-input>
            <masterFormOrigenesMaderaAnios
              :typeOptions="[
                {
                  value: 'E',
                  text: $t('message.delete'),
                  visible: $store.getters.permiteAccion('eliminar_origenes_madera'),
                },
              ]"
              :createButton="$store.getters.permiteAccion('crear_origenes_madera')"
              @adding="addingAnio"
              @canceled="canceledAnio"
              @realizarAccion="realizarAccionAnio"
              @editar="editarAnio"
              ref="masterFormOrigenesMaderaAnios"
              @submitFormulario="submitFormularioAnio"
              resource="/api/origenes-madera-anios"
              :isPaginated="false"
              :columns="[
                
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
                  label: 'Código de Hacienda',
                  field: 'codigo_hacienda',
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
                <div class="column" style="display:none;">
                  <b-field :label="$t('message.id')">
                    <b-input readonly v-model="form2.id"></b-input>
                  </b-field>
                </div>
                <b-input v-model="form2.origen_madera_id" style="display:none;" ></b-input>
                <div class="column">
                  <b-field
                    :label="$t('message.origen_madera')"
                  >
                    <b-input v-model="form2.origen_madera_name" readonly></b-input>
                  </b-field>
                </div>
                <div class="column">
                  <b-field
                    :message="errores.anio_cultivo ? errores.anio_cultivo[0] : ''"
                    :type="errores.anio_cultivo ? 'is-danger' : ''"
                    :label="$t('message.anio_cultivo')"
                  >
                    <b-input v-model="form2.anio_cultivo"></b-input>
                  </b-field>
                </div>
                <div class="column">
                  <b-field
                    :message="errores.codigo_hacienda ? errores.codigo_hacienda[0] : ''"
                    :type="errores.codigo_hacienda ? 'is-danger' : ''"
                    :label="$t('message.codigo_hacienda')"
                  >
                    <b-input v-model="form2.codigo_hacienda"></b-input>
                  </b-field>
                </div>
                
              </div>
            </masterFormOrigenesMaderaAnios>

          </div>
      </div>
    </div>
  </section>
</template>

<script>
import MasterForm from "../../layouts/MasterFormOrigenesMadera";
import MasterFormOrigenesMaderaAnios from "../../layouts/MasterFormOrigenesMaderaAnios";

export default {
  components: { MasterForm,MasterFormOrigenesMaderaAnios },
  data: function () {
    return {
      form: {
        hectareas: "",
        descripcion: "",
        id: "",
        _method: undefined,
        volumen_inventario: "",
      },
      form2: {
        origen_madera_id: "",
        anio_cultivo: "",
        codigo_hacienda: "",
        id: "",
        origen_madera_name: "",
        _method: undefined,
      },
      NeworigenMaderas: {
        id: '',
      },
      acciones: [],
      errores: {
        hectareas: undefined,
        descripcion: undefined,
        volumen_inventario: undefined,
        origen_madera_id: undefined,
        anio_cultivo: undefined,
        codigo_hacienda: undefined,


      },
    };
  },
  methods: {
    canceledAnio: function () {
      this.limpiarAnio();
    },
    limpiarAnio: function () {
      this.form2.id = "";
      this.form2._method = undefined;
      this.form2.anio_cultivo = "";
      this.form2.codigo_hacienda = "";

    },
    addingAnio: function () {
      this.limpiarAnio();
    },
    realizarAccionAnio: function (type, origenes_madera) {
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
    editarAnio: function (origenMaderaAnio) {
      this.form2.id = origenMaderaAnio.id;
      this.form2.origen_madera_id = origenMaderaAnio.origen_madera_id;
      this.form2.anio_cultivo = origenMaderaAnio.anio_cultivo;
      this.form2.codigo_hacienda = origenMaderaAnio.codigo_hacienda;

    },
    limpiarErroresAnio: function () {
      this.errores.anio_cultivo = undefined;
      this.errores.codigo_hacienda = undefined;
    },
    submitFormularioAnio: function () {
      this.limpiarErroresAnio();
      let path = process.env.MIX_APP_URL_API + "/origenes-madera-anios";
      if (this.form2.id !== "") {
        path += "/" + this.form2.id;
        this.form2._method = "PUT";
      } else this.form2._method = undefined;
      this.$http
        .post(path, this.form2)
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
            this.errores.codigo_hacienda = response.data.errors.codigo_hacienda;
            this.errores.origen_madera_id = response.data.errors.origen_madera_id;

          } else {
            this.$buefy.toast.open({
              message: this.$t("message.generic_error"),
              type: "is-danger",
            });
          }
        });
    },
    canceled: function () {
      this.limpiar();
      document.getElementById("add_anio_div").style.display = "none";

    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.hectareas = "";
      this.form.descripcion = "";
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
      this.form.volumen_inventario = origenMadera.volumen_inventario;

      document.getElementById("add_anio_div").style.display = "block";
      this.form2.origen_madera_id = origenMadera.id;
      this.form2.origen_madera_name = origenMadera.descripcion;
      this.NeworigenMaderas.id = origenMadera.id;
      setTimeout(function() {
        document.getElementById("enviar_busqueda").click();
      }, 500);
    },
    limpiarErrores: function () {
      this.errores.descripcion = undefined;
      this.errores.hectareas = undefined;
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
                document.getElementById("add_anio_div").style.display = "none";

          this.$refs.masterForm.submit();
        })
        .catch(({ response }) => {
          let status = response.status;
          if (status === 422) {
            this.errores.hectareas = response.data.errors.hectareas;
            this.errores.volumen_inventario =
              response.data.errors.volumen_inventario;
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
