import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'main',
        component: () => import('../views/main.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/login.vue')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
