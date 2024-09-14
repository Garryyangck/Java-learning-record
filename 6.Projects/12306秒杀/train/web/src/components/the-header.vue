<template>
  <a-layout-header class="header">
    <div class="logo">GARRY</div>
    <div style="float: right; color: white;">
      您好：{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login" style="color: white;">
        退出登录
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
          <coffee-outlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/> &nbsp; 乘车
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
    let member = store.state.member;

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
      member,
    };
  },
});
</script>

<style scoped> /*scoped修饰的css只在本页面生效*/
.logo {
  float: left;
  width: 5%;
  height: 32px;
  margin: 16px 24px 16px -2%;
  background: rgba(255, 255, 255, 0.3);
  text-align: center;
  color: white; /* 设置文字颜色为白色 */
  font-weight: bold; /* 设置文字加粗 */
  line-height: 32px; /* 使文字垂直居中 */
}
</style>
