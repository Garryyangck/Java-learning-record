import {createRouter, createWebHistory} from 'vue-router'
import {notification} from "ant-design-vue";
import store from "@/store";

const routes = [
    {
        path: '/login',
        component: () => import('../views/login.vue'),
    },
    {
        path: '/',
        component: () => import('../views/main.vue'),
        meta: {
            loginRequire: true // 自定义的meta和loginRequired
        },
        children: [
            {
                path: 'welcome',
                component: () => import('../views/main/welcome.vue'),
            },
            {
                path: 'passenger',
                component: () => import('../views/main/passenger.vue'),
            },
            {
                path: 'ticket',
                component: () => import('../views/ticket.vue'),
            },
            {
                path: 'order',
                component: () => import('../views/main/order.vue'),
            },
            {
                path: 'message',
                component: () => import('../views/main/message.vue'),
            },
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

// 统一路由拦截校验
router.beforeEach((to, from, next) => {
    // 要不要对meta.loginRequire属性做监控拦截
    if (to.matched.some((item) => {
        console.log("\"" + item.path + "\"是否需要登录校验：", item.meta.loginRequire || false);
        return item.meta.loginRequire;
    })) {
        const member = store.state.member;
        console.log("member = ", member);
        if (!member || !member.token) {
            console.log("未登录或登录超时，跳转到登录页面");
            notification.error({description: "未登录或登录超时"});
            next('/login');
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
