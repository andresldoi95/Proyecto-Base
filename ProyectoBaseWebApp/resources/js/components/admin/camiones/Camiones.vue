<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.camiones') }}</h1>
        <masterForm
          :typeOptions="[
            {
                value: 'E',
                text: $t('message.delete'),
                visible: $store.getters.permiteAccion('eliminar_camiones')
            }
        ]"
          :createButton="$store.getters.permiteAccion('crear_camiones')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/camiones"
          :isPaginated="false"
          :columns="[
                {
                    label : $t('message.id'),
                    field : 'id',
                    sortable : true
                },
                {
                    label : $t('message.tipo_camion'),
                    field : 'descripcion_tipo_camion',
                    sortable : true
                },
                {
                    label : $t('message.nombre'),
                    field : 'camionero',
                    sortable : true
                },
                {
                    label : $t('message.identificacion'),
                    field : 'identificacion_camionero',
                    sortable : true
                },
                {
                    label : $t('message.placa'),
                    field : 'placa',
                    sortable : true
                },
                {
                    label : $t('message.alto'),
                    field : 'alto',
                    sortable : true
                },
                {
                    label : $t('message.ancho'),
                    field : 'ancho',
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
                :message="errores.camionero?errores.camionero[0]:''"
                :type="errores.camionero?'is-danger':''"
                :label="$t('message.nombre')"
              >
                <b-input v-model="form.camionero"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.identificacion_camionero?errores.identificacion_camionero[0]:''"
                :type="errores.identificacion_camionero?'is-danger':''"
                :label="$t('message.identificacion')"
              >
                <b-input v-model="form.identificacion_camionero"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.tipo_camion?errores.tipo_camion[0]:''"
                :type="errores.tipo_camion?'is-danger':''"
                :label="$t('message.tipo_camion')"
              >
                <b-select v-model="form.tipo_camion" expanded :placeholder="$t('title.seleccione')">
                  <option value="B">Bananero</option>
                  <option value="T">Trailer</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.placa?errores.placa[0]:''"
                :type="errores.placa?'is-danger':''"
                :label="$t('message.placa')"
              >
                <b-input v-model="form.placa"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.alto?errores.alto[0]:''"
                :type="errores.alto?'is-danger':''"
                :label="$t('message.alto')"
              >
                <b-input v-model="form.alto"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.ancho?errores.ancho[0]:''"
                :type="errores.ancho?'is-danger':''"
                :label="$t('message.ancho')"
              >
                <b-input v-model="form.ancho"></b-input>
              </b-field>
            </div>
          </div>
          <div class="columns">
              <div class="column">
                  <filas-camion :errores="errores" v-model="form.filas"></filas-camion>
              </div>
          </div>
        </masterForm>
      </div>
    </div>
  </section>
</template>

<script>
import MasterForm from "../../layouts/MasterForm";
import FilasCamion from "./FilasCamion";
export default {
  components: { MasterForm, FilasCamion },
  data: function () {
    return {
      form: {
        camionero: "",
        id: "",
        _method: undefined,
        identificacion_camionero: "",
        tipo_camion: "B",
        ancho: 0,
        alto: 0,
        placa: "",
        filas: []
      },
      acciones: [],
      errores: {},
    };
  },
  methods: {
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.camionero = "";
      this.form.identificacion_camionero = "";
      this.form.ancho = 0;
      this.form.alto = 0;
      this.form.tipo_camion = "B";
      this.form.placa = "";
      this.form.filas.splice(0, this.form.filas.length);
      this.limpiarErrores();
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, camiones) {
      if (type === "E") {
        let camionesId = [];
        for (let i = 0; i < camiones.length; i++)
          camionesId.push(camiones[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/camiones", {
            camiones: camionesId,
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
    editar: function (camion) {
      this.form.id = camion.id;
      this.form.camionero = camion.camionero;
      this.form.identificacion_camionero = camion.identificacion_camionero;
      this.form.tipo_camion = camion.tipo_camion;
      this.form.ancho = camion.ancho;
      this.form.alto = camion.alto;
      this.form.placa = camion.placa;
      this.form.filas.splice(0, this.form.filas.length);
        for (let i = 0; i < camion.filas.length; i++) {
            this.form.filas.push({
                filas: camion.filas[i].filas,
                columnas: camion.filas[i].columnas
            });
        }
    },
    limpiarErrores: function () {
      this.errores = {};
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/camiones";
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
            this.errores = response.data.errors;
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
