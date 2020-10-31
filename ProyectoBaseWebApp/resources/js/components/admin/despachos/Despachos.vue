<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.despachos') }}</h1>
        <masterForm
            :noMostrarEdicion="true"
            @editar="editar"
            :checkable="false"
            :statusOptions="[]"
          :typeOptions="[]"
          :createButton="false"
          ref="masterForm"
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
                }
            ]"
        ></masterForm>
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
          desde: '',
          hasta: ''
      }
    };
  },
  methods: {
      editar: function (despacho) {
          window.open('/despacho/' + despacho.id, '_blank');
      }
  },
};
</script>
