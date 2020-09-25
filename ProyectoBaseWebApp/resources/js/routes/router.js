import Home from "../components/Home";
import Login from "../components/auth/Login";
import Admin from "../components/layouts/AdminPanel";
import Empresas from "../components/admin/empresas/Empresas";
import Dashboard from "../components/admin/Dashboard";
import Roles from "../components/admin/usuarios/Roles";
import Usuarios from "../components/admin/usuarios/Usuarios";
import ModificarCuenta from "../components/auth/ModificarCuenta";
import SetPassword from "../components/auth/SetPassword";
import RecuperarCuenta from "../components/auth/RecuperarCuenta";
import Largos from "../components/admin/largos/Largos";
import Espesores from "../components/admin/espesores/Espesores";
import Procedencias from "../components/admin/procedencias/Procedencias";
import Destinos from "../components/admin/destinos/Destinos";
import Materiales from "../components/admin/materiales/Materiales";
import Correos from "../components/admin/correos/Correos";
import Controladores from "../components/admin/controladores/Controladores";
import Aserradores from "../components/admin/aserradores/Aserradores";
import Camiones from "../components/admin/camiones/Camiones";
import CodigosAserradores from "../components/admin/aserradores/CodigosAserradores";
import Parametros from "../components/admin/parametros/Parametros";
import TiposMadera from "../components/admin/tipos-madera/TiposMadera";
import FormatosEntrega from "../components/admin/formatos-entrega/FormatosEntrega";
export default {
    mode: "history",
    routes: [
        {
            path: "/",
            component: Home,
            name: "Home",
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/perfil",
            component: ModificarCuenta,
            name: "Perfil",
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/recuperar",
            component: RecuperarCuenta,
            name: "Recuperar",
            meta: {
                guest: true
            }
        },
        {
            path: "/set-password/:token",
            component: SetPassword,
            name: "SetPassword",
            meta: {
                guest: true
            }
        },
        {
            path: "/login",
            component: Login,
            name: "Login",
            meta: {
                guest: true
            }
        },
        {
            path: "/admin",
            component: Admin,
            meta: {
                requiresAuth: true
            },
            children: [
                {
                    name: "Dashboard",
                    path: "",
                    component: Dashboard
                },
                {
                    component: Empresas,
                    path: "empresas",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Empresas"
                },
                {
                    component: Roles,
                    path: "roles",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Roles"
                },
                {
                    component: Usuarios,
                    path: "usuarios",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Usuarios"
                },
                {
                    component: Largos,
                    path: "largos",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Largos"
                },
                {
                    component: Espesores,
                    path: "espesores",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Espesores"
                },
                {
                    component: Procedencias,
                    path: "procedencias",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Procedencias"
                },
                {
                    component: Destinos,
                    path: "destinos",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Destinos"
                },
                {
                    component: Materiales,
                    path: "materiales",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Materiales"
                },
                {
                    component: Correos,
                    path: "correos",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Correos"
                },
                {
                    component: Controladores,
                    path: "controladores",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Controladores"
                },
                {
                    component: Aserradores,
                    path: "aserradores",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Aserradores"
                },
                {
                    component: CodigosAserradores,
                    path: "codigos-aserradores",
                    meta: {
                        requiresAuth: true
                    },
                    name: "CodigosAserradores"
                },
                {
                    component: Camiones,
                    path: "camiones",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Camiones"
                },
                {
                    component: Parametros,
                    path: "parametros",
                    meta: {
                        requiresAuth: true
                    },
                    name: "Parametros"
                },
                {
                    component: TiposMadera,
                    path: "tipos-madera",
                    meta: {
                        requiresAuth: true
                    },
                    name: "TiposMadera"
                },
                {
                    component: FormatosEntrega,
                    path: "formatos-entrega",
                    meta: {
                        requiresAuth: true
                    },
                    name: "FormatosEntrega"
                }
            ]
        }
    ]
};
