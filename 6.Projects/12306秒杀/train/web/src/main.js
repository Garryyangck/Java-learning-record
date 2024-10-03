// noinspection JSCheckFunctionSignatures
import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd, {notification} from 'ant-design-vue' // 全局引入 ant-design-vue 的所有组件
import 'ant-design-vue/dist/antd.css' // 全局引入 ant-design-vue 的 css
import * as Icons from '@ant-design/icons-vue'; // 全局引入 ant-design-vue 的所有图标
import axios from "axios";
import './assets/js/enums'
import './assets/css/global.css'

const app = createApp(App);
app.use(store).use(router).use(Antd);

notification.config({
    duration: 1.5,  // 持续时间
    placement: 'topRight',  // 全局通知弹出的位置
});

/**
 * 全局注册图标组件
 */
Object.keys(Icons).forEach((key) => {
    app.component(key, Icons[key]);
});

app.mount('#app');

/**
 * axios 拦截器
 */
axios.interceptors.request.use(function (config) {
    // 给所有的请求加上token
    console.log(config.url + '请求参数: ', config);
    const token = store.state.member.token;
    if (token) {
        config.headers.token = token; /*必须写死token，因为网关就写死从headers里面获取"token"*/
        console.log('在' + config.url + '的 headers 增加 token: ' + token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    console.log(response.config.url + '返回结果: ', response);
    return response;
}, error => {
    const response = error.response;
    const status = response.status;
    if (status === 401) {
        // 特殊处理 401，权限不足，跳转到 /login
        console.log('未登录或登录超时，跳转到登录页面');
        notification.error({description: '未登录或登录超时'});
        router.push('/login');
    }
    console.log('返回错误: ', error);
    return Promise.reject(error);
});

/**
 * 打印启动环境日志，配置访问服务器的统一 Domain
 */
console.log('环境: ', process.env.NODE_ENV);
console.log('服务端: ', process.env.VUE_APP_SERVER);
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

