<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container" style="max-width:95%;">
        <h1 class="title">{{ $t('title.dashboard') }}</h1>
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
                <div v-for="destino in destinos" :key="destino.id" class="field">
                    <b-checkbox v-model="form.destinos" :native-value="destino.id">{{ destino.descripcion }}</b-checkbox>
                </div>
            </div>
            <div class="column">
              <b>Volumen Total Despachado</b>
              <div id="volumen_total"></div>

              
            </div>
            
          </div>
          <div class="columns">
            <div class="column">
              <b>Volumen Despachado por Destino</b>
              <div class="hello" ref="chartdivDestino" style="width:100%; height:400px;"></div>
            </div>
            <div class="column">
              <b>Volumen Despachado por Espesores</b>
              <div class="hello" ref="chartdivEspesor" style="width:100%; height:400px;"></div>

            </div>
            <div class="column">
              <b>Volumen Despachado por Formato</b>
              <div class="hello" ref="chartdivFormato" style="width:100%; height:400px;"></div>
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <b>Volumen Despachado por Hacienda</b>
              <div class="hello" ref="chartdivHacienda" style="width:100%; height:400px;"></div>
            </div>
          </div>
      </div>
    </div>
  </section>
</template>

<script>
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";

am4core.useTheme(am4themes_animated);


export default {
  name: 'HelloWorld',
  data: function () {
    return {
      form: {
        _method: undefined,
        destinos: []
      },
      destinos: [],
    };
  },
  methods: {
    cargarDestinos: function () {
        let path = process.env.MIX_APP_URL_API + "/destinos/listado";
        this.$http.get(path).then(({data}) => {
            this.destinos = data;
        });
    },
    cargarDespachosPorDestino: function () {
        let path = process.env.MIX_APP_URL_API + "/despachos_por_destino";
        this.$http.get(path).then(({data}) => {
            //console.log(data);
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
            categoryAxis.renderer.cellStartLocation = 0.1;
            categoryAxis.renderer.cellEndLocation = 0.9;

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
        this.$http.get(path).then(({data}) => {
            //console.log(data);
            let chart = am4core.create(this.$refs.chartdivHacienda, am4charts.XYChart); 
            chart.data = data;
            // Create axes
            let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
            categoryAxis.title.text = "Hacienda";
            categoryAxis.title.fontWeight = 800;
            categoryAxis.dataFields.category = "hacienda";
            categoryAxis.numberFormatter.numberFormat = "#";
            categoryAxis.renderer.inversed = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.cellStartLocation = 0.1;
            categoryAxis.renderer.cellEndLocation = 0.9;

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
        this.$http.get(path).then(({data}) => {
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
            categoryAxis.renderer.cellStartLocation = 0.1;
            categoryAxis.renderer.cellEndLocation = 0.9;

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
        this.$http.get(path).then(({data}) => {
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
            categoryAxis.renderer.cellStartLocation = 0.1;
            categoryAxis.renderer.cellEndLocation = 0.9;
            

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
        this.$http.get(path).then(({data}) => {
            console.log(data);
            document.getElementById('volumen_total').innerHTML = '<h1 class="title" >'+data+'</h1>';
        });
    },

  },
  mounted() {
    this.cargarDestinos();
    this.cargarDespachosTotal();
    this.cargarDespachosPorDestino();
    this.cargarDespachosPorHacienda();
    this.cargarDespachosPorFormato();
    this.cargarDespachosPorEspesor();
  },

  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  }
}
</script>