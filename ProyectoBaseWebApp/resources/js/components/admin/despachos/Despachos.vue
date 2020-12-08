<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.despachos') }}</h1>
        <masterForm
            @editar="editar"
            :checkable="false"
            @abrirPDF="abrirPDF"
            @canceled="canceled"
            :statusOptions="[]"
          :typeOptions="[]"
          :createButton="false"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/despachos"
          :columns="[
                {
                    label : $t('message.numero_documento'),
                    field : 'numero_documento',
                    sortable : true
                },
                {
                    label : $t('message.camion'),
                    field : 'camion.placa',
                    sortable : true
                },
                {
                    label : $t('message.fecha_despacho'),
                    field : 'fecha_despacho',
                    sortable : true
                },
                {
                    label : $t('message.fecha_tumba'),
                    field : 'fecha_tumba',
                    sortable : true
                },
                {
                    label : $t('message.destino'),
                    field : 'destino.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.formato_entrega'),
                    field : 'formato_entrega.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.origen_madera'),
                    field : 'origen_madera.descripcion',
                    sortable : true
                },
                {
                    label : $t('message.usuario'),
                    field : 'usuario.name',
                    sortable : true
                },
                {
                    label: '',
                    field: '',
                    sortable: false,
                    button: true,
                    event: 'abrirPDF',
                    'icon-left': 'file-pdf',
                    type: 'is-danger'
                }
            ]"
        >
        <div class="columns">
            <div class="column" style="display:none;">
              <b-field :label="$t('message.id')">
                <b-input readonly v-model="form.id"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field :label="$t('message.numero_documento')">
                <b-input readonly v-model="form.numero_documento"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.camion_id?errores.camion_id[0]:''"
                :type="errores.camion_id?'is-danger':''"
                :label="$t('message.camion')"
              >
                <b-select
                  v-model="form.camion_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in camiones"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.placa }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.fecha_despacho ? errores.fecha_despacho[0] : ''"
                :type="errores.fecha_despacho ? 'is-danger' : ''"
                :label="$t('message.fecha_despacho')"
              >
                <b-input v-model="form.fecha_despacho"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.fecha_tumba ? errores.fecha_tumba[0] : ''"
                :type="errores.fecha_tumba ? 'is-danger' : ''"
                :label="$t('message.fecha_tumba')"
              >
                <b-input v-model="form.fecha_tumba"></b-input>
              </b-field>
            </div>
            <div class="column">
              <b-field
                :message="errores.destino_id?errores.destino_id[0]:''"
                :type="errores.destino_id?'is-danger':''"
                :label="$t('message.destino')"
              >
                <b-select
                  v-model="form.destino_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                  @change.native="consultarValorFlete"
                >
                  <option
                    v-for="option in destinos"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
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
                  @change.native="consultarValorFlete"
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
                :message="errores.origen_hacienda_id?errores.origen_hacienda_id[0]:''"
                :type="errores.origen_hacienda_id?'is-danger':''"
                :label="$t('message.origen_hacienda')"
              >
                <b-select
                  v-model="form.origen_hacienda_id"
                  expanded
                  :placeholder="$t('title.seleccione')"
                >
                  <option
                    v-for="option in origenes_hacienda"
                    :value="option.id"
                    :key="option.id"
                  >{{ option.descripcion }}</option>
                </b-select>
              </b-field>
            </div>
            <div class="column">
              <b-field :label="$t('message.valor_flete')">
                <b-input id="valor_flete_new" readonly v-model="form.valor_flete"></b-input>
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
      /*form: {
          desde: '',
          hasta: ''
      },*/
      form: {
        fecha_despacho: "",
        fecha_tumba: "",
        id: "",
        camion_id: "",
        destino_id: "",
        origen_madera_id: "",
        origen_hacienda_id: "",
        numero_documento: "",
        _method: undefined,
      },
      acciones: [],
      camiones: [],
      destinos: [],
      formatos_entregas: [],
      origenes_madera: [],
      origenes_hacienda: [],
      errores: {
        fecha_despacho: undefined,
        fecha_tumba: undefined,
        camion_id: undefined,
        destino_id: undefined,
        origen_madera_id: undefined,
        origen_hacienda_id: undefined,



      },
    };
  },
  methods: {
      consultarValorFlete: function () {
          let path = process.env.MIX_APP_URL_API + "/getTarifaFlete";
          this.$http
            .post(path, this.form)
            .then(({data}) => {
              this.form.valor_flete = data;
              document.getElementById('valor_flete_new').value = data;
            })
            .catch(({ response }) => {
              
            });
          
      },
      cargarCamiones: function () {
          let path = process.env.MIX_APP_URL_API + "/camiones/listado";
          this.$http.get(path).then(({data}) => {
              this.camiones = data;
          });
      },
      cargarDestino: function () {
          let path = process.env.MIX_APP_URL_API + "/destinos/listado";
          this.$http.get(path).then(({data}) => {
              this.destinos = data;
          });
      },
      cargarFormatoDeEntregas: function () {
          let path = process.env.MIX_APP_URL_API + "/formatos-entrega/listado";
          this.$http.get(path).then(({data}) => {
              this.formatos_entregas = data;
          });
      },
      cargarOrigenesMadera: function () {
          let path = process.env.MIX_APP_URL_API + "/origenes-madera/listado";
          this.$http.get(path).then(({data}) => {
              this.origenes_madera = data;
          });
      },
      cargarOrigenesHacienda: function () {
          let path = process.env.MIX_APP_URL_API + "/origenes-hacienda/listado";
          this.$http.get(path).then(({data}) => {
              this.origenes_hacienda = data;
          });
      },
      canceled: function () {
        this.limpiar();
      },
      limpiar: function () {
        this.form.id = "";
        this.form.numero_documento  = "";
        this.form._method = undefined;
        this.form.fecha_despacho = "";
        this.form.fecha_tumba = "";
        this.form.camion_id = '';
        this.form.destino_id = '';
        this.form.origen_madera_id = '';
        this.form.origen_hacienda_id = '';


      },
      abrirPDF: function (despacho) {
          window.open('/despacho/' + despacho.id, '_blank');
      },
      editar: function (despacho) {
          this.form.id = despacho.id;
          this.form.numero_documento = despacho.numero_documento;
          this.form.camion_id = despacho.camion_id;
          this.form.destino_id = despacho.destino_id;
          this.form.origen_madera_id = despacho.origen_madera_id;
          this.form.origen_hacienda_id = despacho.origen_hacienda_id;
          this.form.fecha_despacho = despacho.fecha_despacho;
          this.form.fecha_tumba = despacho.fecha_tumba;
          this.form.valor_flete = despacho.valor_flete;

      },
      limpiarErrores: function () {
      this.errores.fecha_despacho = undefined;
      this.errores.fecha_tumba = undefined;
      this.errores.camion_id = undefined;
      this.errores.destino_id = undefined;
      this.errores.origen_madera_id = undefined;
      this.errores.origen_hacienda_id = undefined;

      },
      submitFormulario: function () {
        this.limpiarErrores();
        let path = process.env.MIX_APP_URL_API + "/despachosUpdate";
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
              this.errores.fecha_despacho = response.data.errors.fecha_despacho;
              this.errores.fecha_tumba = response.data.errors.fecha_tumba;
              this.errores.camion_id = response.data.errors.camion_id;
              this.errores.destino_id = response.data.errors.destino_id;
              this.errores.origen_madera_id = response.data.errors.origen_madera_id;
              this.errores.origen_hacienda_id = response.data.errors.origen_hacienda_id;
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
      this.cargarCamiones();
      this.cargarDestino();
      this.cargarFormatoDeEntregas();
      this.cargarOrigenesMadera();
      this.cargarOrigenesHacienda();
  },
};
</script>
