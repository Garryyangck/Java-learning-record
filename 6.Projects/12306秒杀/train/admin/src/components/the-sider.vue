<template>
  <a-layout-sider width="15%" style="background: #fff; height: 100vh">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
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
      <a-menu-item key="/train-seat">
        <router-link to="/train-seat">
          <Car-outlined/> &nbsp; 火车座位
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";

export default defineComponent({
  name: 'the-sider-view',
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

<style scoped>

</style>