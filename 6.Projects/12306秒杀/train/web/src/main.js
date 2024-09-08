import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue' // 全局引入 ant-design-vue 的所有组件
import 'ant-design-vue/dist/antd.css' // 全局引入 ant-design-vue 的 css
import * as Icons from '@ant-design/icons-vue'; // 全局引入 ant-design-vue 的所有图标

const app = createApp(App);
app.use(store).use(router).use(Antd);

// // 全局使用所有的图标
// const icons = Icons;
// for (const i in icons) { // 遍历引入所有的图标
//     app.component(i, icons[i]);
// }

// 全局注册图标组件
Object.keys(Icons).forEach((key) => {
    app.component(key, Icons[key]);
});

app.mount('#app');
