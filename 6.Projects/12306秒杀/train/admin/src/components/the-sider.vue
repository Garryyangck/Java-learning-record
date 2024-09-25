<template>
  <a-layout-sider width="15%" :style="{background: '#ffffff', height: '100vh', overflow: 'auto'}">
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
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined />
            基础数据
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <ClockCircle-outlined/> &nbsp; 车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <Car-outlined/> &nbsp; 火车管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-station">
          <router-link to="/base/train-station">
            <Car-outlined/> &nbsp; 火车车站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-carriage">
          <router-link to="/base/train-carriage">
            <Car-outlined/> &nbsp; 火车车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-seat">
          <router-link to="/base/train-seat">
            <Car-outlined/> &nbsp; 火车座位
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined />
            跑批管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <ClockCircle-outlined/> &nbsp; 任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
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