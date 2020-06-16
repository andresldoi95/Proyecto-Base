//Importando dependencias
import Vue from "vue";
import Buefy from "buefy";
import Vuex from "vuex";
import axios from "axios";
import VueAxios from "vue-axios";
import VueRouter from "vue-router";
import VueI18n from "vue-i18n";
import VueSession from "vue-session";
//Importando componentes globales
import App from "./components/App";
//Instalando plugins
Vue.use(Buefy);
Vue.use(Vuex);
Vue.use(VueAxios, axios);
Vue.use(VueRouter);
Vue.use(VueI18n);
Vue.use(VueSession);
//Importando configuraciones de los diferentes plugins
const store = new Vuex.Store(require("./plugins/store").default);
const routes = require("./routes/router").default;
const router = new VueRouter(routes);
const messages = require("./lang/translator").default;
const i18n = new VueI18n({
    locale: "es",
    messages,
});
//Instanciando VueJS en el proyecto con sus diferentes recursos
const app = new Vue({
    i18n,
    router,
    store,
    components: {
        App,
    },
    el: "#app",
});
