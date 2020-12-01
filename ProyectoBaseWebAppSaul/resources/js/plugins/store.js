const urlApi = process.env.MIX_APP_URL_API;

export default {
    getters: {
        permiteAccion: state => id => {
            return state.acciones.find(accion => accion.id === id) != null;
        }
    },
    state: {
        empresas: [],
        acciones: [],
        usuario: {
            id: "",
            nombre: "",
            es_superadmin: "N"
        },
        empresa_actual_id: "",
        token: "",
        nombre_empresa_actual: "",
        globalKey: "0",
        locale: "es"
    },
    mutations: {
        loggedIn(state, token) {
            state.token = token;
        },
        setUserInfo(state, userInfo) {
            state.empresas = userInfo.empresas;
            state.usuario.id = userInfo.usuario.id;
            state.usuario.nombre = userInfo.usuario.name;
            state.usuario.es_superadmin = userInfo.usuario.es_superadmin;
        },
        cambiarEmpresaActual(state, data) {
            state.empresa_actual_id = data.id;
            state.nombre_empresa_actual = data.nombre;
        },
        reload(state, key) {
            state.globalKey = key;
        },
        setLang(state, locale) {
            state.locale = locale;
            this._vm.$cookies.set("locale", locale, Infinity);
            state.globalKey = locale;
        },
        setOnlyLang(state, locale) {
            state.locale = locale;
        },
        cambiarAcciones(state, data) {
            state.acciones = data.acciones;
        }
    },
    actions: {
        loggedIn(context, token) {
            context.commit("loggedIn", token);
            this._vm.$http.get(urlApi + "/usuario").then(({ data }) => {
                context.commit("setUserInfo", data);
                if (data.usuario.empresa !== null) {
                    context.commit("cambiarEmpresaActual", {
                        id: data.usuario.empresa.id,
                        nombre: data.usuario.empresa.nombre
                    });
                    this._vm.$http
                        .get(urlApi + "/acciones-por-usuario")
                        .then(({ data }) => {
                            context.commit("cambiarAcciones", {
                                acciones: data
                            });
                        });
                }
            });
        },
        loggedOut(context) {
            context.commit("loggedIn", "");
            context.commit("setUserInfo", {
                empresas: [],
                usuario: {
                    id: "",
                    name: "",
                    es_superadmin: "N"
                }
            });
        }
    }
};
