import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import ThingIndexView from "@/views/thing/IndexView.vue";  //预加载
import noteEditView from '@/views/note/NoteEdit.vue'
import RecycleIndexView from '@/views/recycle/IndexView.vue'


const routes = [
    {
        path:'/',
        redirect:'/recently'  //重定向
    },
    {
        path:'/recently',
        component: () => import('@/views/recently/IndexView.vue'),  //按需加载
        meta:{title:'最近操作页'},
    },
    {
        path:'/thing',
        component: ThingIndexView,
        meta:{title:'小记页'}
    },
    {
        path:'/note',
        component: () => import('@/views/note/IndexView.vue'),  //按需加载
        meta:{title:'笔记页'},
        children: [
            {
                path:'edit/:id',
                props:true,
                component: noteEditView,
                meta:{title:'笔记编辑页'}
            }
        ]
    },
    {
        path:'/recycle',
        component: RecycleIndexView,
        meta:{title:'回收站'}
    },
]


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    // history: createWebHashHistory(import.meta.env.VITE_PUBLIC_PATH),
    routes
})

router.beforeEach((to, from, next) => {
    if(to.meta.title) document.title = to.meta.title;

    next();
}) 

export default router