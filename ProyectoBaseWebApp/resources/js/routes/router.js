import Home from "../components/Home";
import Login from "../components/auth/Login";
import Admin from "../components/layouts/AdminPanel";
import Empresas from "../components/admin/empresas/Empresas";
import Dashboard from "../components/admin/Dashboard";
import Roles from "../components/admin/usuarios/Roles";
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
                }
            ]
        }
    ]
};
