<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container" style="max-width:95%;">
        <h1 class="title">{{ $t('title.dashboard') }}</h1>
          <form @submit.prevent="submit">
            <div class="columns">
              <div class="column">
                
              </div>
              <div class="column">
                <b>Rango de Fecha</b>
                <b-datepicker :placeholder="$t('message.desde')" v-model="form.desde">
                </b-datepicker>
                <b-datepicker :placeholder="$t('message.hasta')" v-model="form.hasta">
                </b-datepicker>
                
              </div>
              <div class="column">
                  <b>Destino</b>
                  <div>
                    <multiselect v-model="form.destinos" tag-placeholder="Añade un Destino" placeholder="Busca o Añade un Destino" label="descripcion" track-by="id" :options="destinos" :multiple="true" :taggable="true" @tag="addTag"></multiselect>
                  </div>
              </div>
              <div class="column">
                <b>Volumen Total Despachado</b>
                <div id="volumen_total"></div>

                
              </div>
              <div class="control">
                <b-button native-type="submit" type="is-primary" icon-left="magnify"></b-button>
              </div>
              
            </div>
          </form>
          <div class="columns">
            <div class="column">
              <b>Volumen Despachado por Destino</b>
              <div class="hello" ref="chartdivDestino" style="width:100%; height:400px;"></div>
            </div>
            <div class="column">
              <b>Volumen Despachado por Espesores</b>
              <div class="hello" ref="chartdivEspesor" style="width:100%; height:400px;"></div>

            </div>
          </div>
          <div class="columns">
            <div class="column">
              <b>Volumen Despachado por Formato</b>
              <div class="hello" ref="chartdivFormato" style="width:100%; height:400px;"></div>
            </div>
            <div class="column">
              <b>Volumen Despachado por Hacienda</b>
              <div class="hello" ref="chartdivHacienda" style="width:100%; height:400px;"></div>
            </div>
          </div>
      </div>
    </div>
  </section>
</template>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>


<script>
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import Multiselect from 'vue-multiselect'


am4core.useTheme(am4themes_animated);


export default {
  name: 'HelloWorld',
  components: {
    Multiselect
  },
  data: function () {
    return {
      form: {
        _method: undefined,
        destinos: [],
        desde: new Date(),
        hasta: new Date()
      },
      destinos: [],
    };
  },
  methods: {
    submit: function () {
      this.cargarDespachosTotal();
      this.cargarDespachosPorDestino();
      this.cargarDespachosPorHacienda();
      this.cargarDespachosPorFormato();
      this.cargarDespachosPorEspesor();      
    },
    addTag (newTag) {
      //this.form.destinos.push(newTag.id)
    },
    cargarDestinos: function () {
        let path = process.env.MIX_APP_URL_API + "/destinos/listado";
        this.$http.get(path).then(({data}) => {
            this.destinos = data;
            this.form.destinos = data;
        });
    },
    cargarDespachosPorDestino: function () {            
        let path = process.env.MIX_APP_URL_API + "/despachos_por_destino";
        this.$http
        .post(path, this.form)
        .then(({data}) => {
          
            let chart = am4core.create(this.$refs.chartdivDestino, am4charts.XYChart); 
            chart.data = data;
            // Create axes
            let categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis());
            categoryAxis.title.text = "Destino";
            categoryAxis.title.fontWeight = 800;
            categoryAxis.dataFields.category = "destino";
            categoryAxis.numberFormatter.numberFormat = "#";
            categoryAxis.renderer.inversed = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.cellStartLocation = 0.2;
            categoryAxis.renderer.cellEndLocation = 0.8;

            let  valueAxis = chart.xAxes.push(new am4charts.ValueAxis()); 
            valueAxis.title.text = "Volumen Enviado";
            valueAxis.title.fontWeight = 800;
            // Create series
            function createSeries(field, name) {
              let series = chart.series.push(new am4charts.ColumnSeries());
              series.dataFields.valueX = field;
              series.dataFields.categoryY = "destino";
              series.name = name;
              series.columns.template.tooltipText = "{name}: [bold]{valueX}[/]";
              series.columns.template.height = am4core.percent(100);
              series.sequencedInterpolation = true;

            }
            createSeries("volumen", "Volumen");

            this.chart = chart;
        });
    },

    cargarDespachosPorHacienda: function () {
        let path = process.env.MIX_APP_URL_API + "/despachos_por_hacienda";
        this.$http
        .post(path, this.form)
        .then(({data}) => {
            //console.log(data);
            let chart = am4core.create(this.$refs.chartdivHacienda, am4charts.XYChart); 
            chart.data = data;
            chart.cursor = new am4charts.XYCursor();
            chart.scrollbarX = new am4core.Scrollbar();
            // Create axes
            let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
            categoryAxis.title.text = "Hacienda";
            categoryAxis.title.fontWeight = 800;
            categoryAxis.dataFields.category = "hacienda";
            categoryAxis.numberFormatter.numberFormat = "#";
            categoryAxis.renderer.inversed = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.cellStartLocation = 0.2;
            categoryAxis.renderer.cellEndLocation = 0.8;

            let  valueAxis = chart.yAxes.push(new am4charts.ValueAxis()); 
            valueAxis.title.text = "Volumen Enviado";
            valueAxis.title.fontWeight = 800;
            // Create series
            function createSeries(field, name) {
              let series = chart.series.push(new am4charts.ColumnSeries());
              series.dataFields.valueY = field;
              series.dataFields.categoryX = "hacienda";
              series.name = name;
              series.columns.template.tooltipText = "{name}: [bold]{valueY}[/]";
              series.columns.template.height = am4core.percent(100);
              series.sequencedInterpolation = true;

            }
            createSeries("volumen", "Volumen");

            this.chart = chart;
        });
    },
    cargarDespachosPorEspesor: function () {
        let path = process.env.MIX_APP_URL_API + "/despachos_por_espesor";
        this.$http
        .post(path, this.form)
        .then(({data}) => {
            //console.log(data);
            let chart = am4core.create(this.$refs.chartdivEspesor, am4charts.XYChart); 
            chart.data = data;
            // Create axes
            let categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis());
            categoryAxis.title.text = "Espesor";
            categoryAxis.title.fontWeight = 800;
            categoryAxis.dataFields.category = "espesor";
            categoryAxis.numberFormatter.numberFormat = "#";
            categoryAxis.renderer.inversed = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.cellStartLocation = 0.2;
            categoryAxis.renderer.cellEndLocation = 0.8;

            let  valueAxis = chart.xAxes.push(new am4charts.ValueAxis()); 
            valueAxis.title.text = "Volumen Enviado";
            valueAxis.title.fontWeight = 800;
            // Create series
            function createSeries(field, name) {
              let series = chart.series.push(new am4charts.ColumnSeries());
              series.dataFields.valueX = field;
              series.dataFields.categoryY = "espesor";
              series.name = name;
              series.columns.template.tooltipText = "{name}: [bold]{valueX}[/]";
              series.columns.template.height = am4core.percent(100);
              series.sequencedInterpolation = true;

            }
            createSeries("volumen", "Volumen");

            this.chart = chart;
        });
    },
    cargarDespachosPorFormato: function () {
        let path = process.env.MIX_APP_URL_API + "/despachos_por_formato";
        this.$http
        .post(path, this.form)
        .then(({data}) => {
            //console.log(data);
            let chart = am4core.create(this.$refs.chartdivFormato, am4charts.XYChart); 
            chart.data = data;
            // Create axes
            let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
            categoryAxis.title.text = "Formato de Entrega";
            categoryAxis.title.fontWeight = 800;
            categoryAxis.dataFields.category = "formato";
            categoryAxis.numberFormatter.numberFormat = "#";
            categoryAxis.renderer.inversed = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.cellStartLocation = 0.2;
            categoryAxis.renderer.cellEndLocation = 0.8;
            

            let  valueAxis = chart.yAxes.push(new am4charts.ValueAxis()); 
            valueAxis.title.text = "Volumen Enviado";
            valueAxis.title.fontWeight = 800;
            // Create series
            function createSeries(field, name) {
              let series = chart.series.push(new am4charts.ColumnSeries());
              series.dataFields.valueY = field;
              series.dataFields.categoryX = "formato";
              series.name = name;
              series.columns.template.tooltipText = "{name}: [bold]{valueY}[/]";
              series.columns.template.height = am4core.percent(100);
              series.sequencedInterpolation = true;

            }
            createSeries("volumen", "Volumen");
            this.chart = chart;
        });
    },
    cargarDespachosTotal: function () {
        let path = process.env.MIX_APP_URL_API + "/despachos_total";
        this.$http
        .post(path, this.form)
        .then(({data}) => {
            console.log(data);
            document.getElementById('volumen_total').innerHTML = '<h1 class="title" >'+data+'</h1>';
        });
    },

  },
  mounted() {
    this.cargarDestinos();
    this.submit();
  },

  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  }
}
</script>