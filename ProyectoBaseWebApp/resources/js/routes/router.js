import Home from "../components/Home";
import Login from "../components/auth/Login";
import Admin from "../components/layouts/AdminPanel";
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
            name: "Admin",
            meta: {
                requiresAuth: true
            }
        }
    ]
};
