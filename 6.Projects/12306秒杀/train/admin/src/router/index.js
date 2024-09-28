import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        component: () => import('../views/main.vue'),
        children: [
            {
                path: 'welcome',
                component: () => import('../views/main/welcome.vue'),
            },
            {
                path: 'about',
                component: () => import('../views/main/about.vue'),
            },
            {
                path: 'base/',
                children: [
                    {
                        path: 'station',
                        component: () => import('../views/main/base/station.vue'),
                    },
                    {
                        path: 'train',
                        component: () => import('../views/main/base/train.vue'),
                    },
                    {
                        path: 'train-station',
                        component: () => import('../views/main/base/train-station.vue'),
                    },
                    {
                        path: 'train-carriage',
                        component: () => import('../views/main/base/train-carriage.vue'),
                    },
                    {
                        path: 'train-seat',
                        component: () => import('../views/main/base/train-seat.vue'),
                    },
                ]
            },
            {
                path: 'business/',
                children: [
                    {
                        path: 'train',
                        component: () => import('../views/main/business/daily-train.vue'),
                    },
                    // {
                    //     path: 'train-station',
                    //     component: () => import('../views/main/business/daily-train-station.vue'),
                    // },
                    // {
                    //     path: 'train-carriage',
                    //     component: () => import('../views/main/business/daily-train-carriage.vue'),
                    // },
                    // {
                    //     path: 'train-seat',
                    //     component: () => import('../views/main/business/daily-train-seat.vue'),
                    // },
                    // {
                    //     path: 'train-ticket',
                    //     component: () => import('../views/main/business/daily-train-ticket.vue'),
                    // },
                ]
            },
            {
                path: 'batch/',
                children: [
                    {
                        path: 'job',
                        component: () => import('../views/main/batch/job.vue'),
                    },
                ]
            }
        ]
    },
    {
        path: '',
        redirect: '/welcome',
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
})

export default router;
