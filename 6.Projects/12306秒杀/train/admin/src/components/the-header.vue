<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white;">
        GARRY
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <HomeOutlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <coffeeOutlined /> &nbsp; 关于
        </router-link>
      </a-menu-item>
      <a-menu-item key="/api">
        <router-link to="/api">
          <BranchesOutlined /> &nbsp; 项目接口
        </router-link>
      </a-menu-item>
    </a-menu>
    <div style="margin-left: auto; color: white;">
      欢迎使用管理控制台
    </div>
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

</style>
