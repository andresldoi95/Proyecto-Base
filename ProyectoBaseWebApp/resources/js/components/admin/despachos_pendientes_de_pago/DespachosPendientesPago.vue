<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.despachos_pendientes_de_pago') }}</h1>
        <masterForm
            @editar="editar"
            :checkable="false"
            @canceled="canceled"
            :statusOptions="[]"
          :typeOptions="[]"
          :createButton="false"
          ref="masterForm"
          @submitFormulario="submitFormulario"
          resource="/api/despachos_pendientes_de_pago"
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
                    label : $t('message.aserrador'),
                    field : 'aserrador.nombre',
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
                    label : $t('message.volumen'),
                    field : '',
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
                <label class="label" for="roles">{{ $t('etiqueta.pagos') }}</label>
                <b-checkbox v-model="form.pago_aserrado" >{{ $t('etiqueta.pagos_aserrado') }}</b-checkbox>
                <b-checkbox v-model="form.pago_transporte" >{{ $t('etiqueta.pagos_transporte') }}</b-checkbox>
            </div>

              
          </div>
        </masterForm>
      </div>
    </div>
  </section>
</template>

<script>
import MasterForm from "../../layouts/MasterFormDespacho";
import moment from 'moment'

export default {
  components: { MasterForm },
  data: function () {
    return {
      form: {
        id: "",
        numero_documento: "",
        _method: undefined,
      },
      acciones: [],
      errores: {
        
        pago_aserrado: undefined,
        pago_transporte: undefined,



      },
    };
  },
  methods: {

      canceled: function () {
        this.limpiar();
      },
      limpiar: function () {
        this.form.id = "";
        this.form.numero_documento  = "";
        this.form.pago_aserrado  = false;
        this.form.pago_transporte  = false;

        this.form._method = undefined;
       

      },

      editar: function (despacho) {
          this.form.id = despacho.id;
          this.form.numero_documento = despacho.numero_documento;
          if(despacho.pago_aserrado==1){
            this.form.pago_aserrado  = true;
          }
          if(despacho.pago_transporte==1){
            this.form.pago_transporte  = true;
          }
          

      },
      limpiarErrores: function () {
      this.errores.fecha_despacho = undefined;
      

      },
      submitFormulario: function () {
        this.limpiarErrores();
        let path = process.env.MIX_APP_URL_API + "/despachosPendientesPagoUpdate";
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
            this.limpiar();
          })
          .catch(({ response }) => {
            let status = response.status;
            if (status === 422) {
              this.errores.fecha_despacho = response.data.errors.fecha_despacho;
              
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
      
  },
};
</script>
