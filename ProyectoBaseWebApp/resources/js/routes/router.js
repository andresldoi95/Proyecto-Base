import Home from "../components/Home";
import Login from "../components/auth/Login";
export default {
    mode: "history",
    routes: [
        { path: "/", component: Home, name: "Home" },
        { path: "/login", component: Login, name: "Login" }
    ]
};
