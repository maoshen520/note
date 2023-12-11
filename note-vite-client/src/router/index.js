import {createRouter, createWebHistory} from "vue-router";
import ThingIndexView from "@/views/thing/IndexView.vue"


const routes = [
    {
        path:'/thing',
        component: ThingIndexView
    }
]


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router