<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t("title.tipos_bulto") }}</h1>
        <masterForm
          :typeOptions="[
            {
              value: 'E',
              text: $t('message.delete'),
              visible: $store.getters.permiteAccion('eliminar_tipos_bulto'),
            },
          ]"
          :createButton="$store.getters.permiteAccion('crear_tipos_bulto')"
          @adding="adding"
          @canceled="canceled"
          @realizarAccion="realizarAccion"
          @editar="editar"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/tipos-bulto"
          :isPaginated="false"
          :columns="[
            {
              label: $t('message.id'),
              field: 'id',
              sortable: true,
            },
            {
              label: $t('message.codigo'),
              field: 'codigo',
              sortable: true,
            },
            {
              label: $t('message.ancho'),
              field: 'ancho',
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
                :message="errores.codigo ? errores.codigo[0] : ''"
                :type="errores.codigo ? 'is-danger' : ''"
                :label="$t('message.codigo')"
              >
                <b-input v-model="form.codigo"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.ancho ? errores.ancho[0] : ''"
                :type="errores.ancho ? 'is-danger' : ''"
                :label="$t('message.ancho')"
              >
                <b-input v-model="form.ancho"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.espesor_id ? errores.espesor_id[0] : ''"
                :type="errores.espesor_id ? 'is-danger' : ''"
                :label="$t('message.espesor')"
              >
                <b-select
                  v-model="form.espesor_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    :value="espesor.id"
                    v-for="espesor in espesores"
                    :key="espesor.id"
                  >
                    {{ espesor.descripcion }}
                  </option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.largo_id ? errores.largo_id[0] : ''"
                :type="errores.largo_id ? 'is-danger' : ''"
                :label="$t('message.largo')"
              >
                <b-select
                  v-model="form.largo_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    :value="largo.id"
                    v-for="largo in largos"
                    :key="largo.id"
                  >
                    {{ largo.descripcion }}
                  </option>
                </b-select>
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
        ancho: "",
        codigo: "",
        id: "",
        _method: undefined,
        largo_id: "",
        espesor_id: "",
      },
      errores: {
        ancho: undefined,
        codigo: undefined,
        largo_id: undefined,
        espesor_id: undefined,
      },
      espesores: [],
      largos: [],
    };
  },
  methods: {
    cargarEspesores: function () {
      this.$http
        .get(process.env.MIX_APP_URL_API + "/espesores/listado")
        .then(({ data }) => {
          this.espesores = data;
        });
    },
    cargarLargos: function () {
      this.$http
        .get(process.env.MIX_APP_URL_API + "/largos/listado")
        .then(({ data }) => {
          this.largos = data;
        });
    },
    canceled: function () {
      this.limpiar();
    },
    limpiar: function () {
      this.form.id = "";
      this.form._method = undefined;
      this.form.ancho = "";
      this.form.codigo = "";
      this.form.largo_id = "";
      this.form.espesor_id = "";
    },
    adding: function () {
      this.limpiar();
    },
    realizarAccion: function (type, tipos_bulto) {
      if (type === "E") {
        let tipos_bultoId = [];
        for (let i = 0; i < tipos_bulto.length; i++)
          tipos_bultoId.push(tipos_bulto[i].id);
        this.$http
          .post(process.env.MIX_APP_URL_API + "/tipos-bulto", {
            tiposBulto: tipos_bultoId,
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
    editar: function (tipoBulto) {
      this.form.id = tipoBulto.id;
      this.form.ancho = tipoBulto.ancho;
      this.form.codigo = tipoBulto.codigo;
      this.form.espesor_id = tipoBulto.espesor_id;
      this.form.largo_id = tipoBulto.largo_id;
    },
    limpiarErrores: function () {
      this.errores.codigo = undefined;
      this.errores.ancho = undefined;
      this.errores.largo_id = undefined;
      this.errores.espesor_id = undefined;
    },
    submitFormulario: function () {
      this.limpiarErrores();
      let path = process.env.MIX_APP_URL_API + "/tipos-bulto";
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
            this.errores.ancho = response.data.errors.ancho;
            this.errores.codigo = response.data.errors.codigo;
            this.errores.largo_id = response.data.errors.largo_id;
            this.errores.espesor_id = response.data.errors.espesor_id;
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
    this.cargarEspesores();
    this.cargarLargos();
  },
};
</script>
