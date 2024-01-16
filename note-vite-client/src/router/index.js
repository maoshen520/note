import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import ThingIndexView from "@/views/thing/IndexView.vue"


const routes = [
    {
        path:'/thing',
        component: ThingIndexView
    },
    {
        path:'/note',
        component: () => import('@/views/note/IndexView.vue'),
        children: [
            {
                path:'edit/:id',
                props:true,
                component: () => import('@/views/note/NoteEdit.vue'),
            }
        ]
    },
]


const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    history: createWebHashHistory(import.meta.env.VITE_PUBLIC_PATH),
    routes
})

export default router