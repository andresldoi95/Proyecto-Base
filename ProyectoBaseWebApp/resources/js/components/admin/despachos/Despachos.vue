<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">{{ $t('title.despachos') }}</h1>
        <masterForm
            noMostrarEdicion
            @editar="editar"
            :checkable="false"
            @editarDespacho="editarDespacho"
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
                },
                {
                    label: '',
                    field: '',
                    sortable: false,
                    button: true,
                    event: 'editarDespacho',
<<<<<<< HEAD
                    'icon-left': 'pencil'
=======
                    'icon-left': 'pencil',
                    type: 'is-info'
>>>>>>> a21c5e5c7c2b9d80928a25fdc9e2a0add294b1eb
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
      editarDespacho: function (despacho) {
          alert('Editar el despacho: ' + despacho.id);
      },
      editar: function (despacho) {
          window.open('/despacho/' + despacho.id, '_blank');
      }
  },
};
</script>
