<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white;">
        GARRY
      </router-link>
    </div>
    <div style="float: right; color: white;">
      欢迎使用管理控制台
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined/> &nbsp; 关于
        </router-link>
      </a-menu-item>
      <a-menu-item key="/station">
        <router-link to="/station">
          <ClockCircle-outlined/> &nbsp; 车站管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/train">
        <router-link to="/train">
          <Car-outlined/> &nbsp; 火车管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/train-station">
        <router-link to="/train-station">
          <Car-outlined/> &nbsp; 火车车站
        </router-link>
      </a-menu-item>
      <a-menu-item key="/train-carriage">
        <router-link to="/train-carriage">
          <Car-outlined/> &nbsp; 火车车厢
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import store from "@/store";
import router from "@/router";

export default defineComponent({
  name: 'the-header-view',
  setup() {
    const selectedKeys = ref(['/welcome']);

    /**
     * 动态监视 router.currentRoute.value.path 的变化，变化时触发后面的函数
     */
    watch(() => router.currentRoute.value.path, (newValue) => {
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});

    return {
      selectedKeys,
    };
  },
});
</script>

<style scoped> /*scoped修饰的css只在本页面生效*/
.logo {
  float: left;
  height: 32px;
  width: 150px;
  color: white; /* 设置文字颜色为白色 */
  font-weight: bold; /* 设置文字加粗 */
  font-size: 20px;
}
</style>
