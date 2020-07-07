const urlApi = process.env.MIX_APP_URL_API;
import { i18n } from "vue-i18n";

export default {
    state: {
        empresas: [],
        acciones: [],
        usuario: {
            id: "",
            nombre: "",
            es_superadmin: "N"
        },
        empresa_actual_id: "",
        token: ""
    },
    mutations: {
        loggedIn(state, token) {
            state.token = token;
        },
        setUserInfo(state, userInfo) {
            state.empresas = userInfo.empresas;
            state.usuario.id = userInfo.id;
            state.usuario.nombre = userInfo.name;
            state.usuario.es_superadmin = userInfo.es_superadmin;
        }
    },
    actions: {
        loggedIn(context, token) {
            context.commit("loggedIn", token);
            this._vm.$http.get(urlApi + "/usuario").then(({ data }) => {
                context.commit("setUserInfo", data);
            });
        },
        loggedOut(context) {
            context.commit("loggedIn", "");
            context.commit("setUserInfo", {
                empresas: [],
                id: "",
                name: "",
                es_superadmin: "N"
            });
        }
    }
};
