<template>
  <a-layout>
    <the-header-view class="fixed-header"/> <!--导航页-->
    <a-layout>
      <the-sider-view class="fixed-sider"/> <!--左侧边栏-->
      <a-layout  class="layout"> <!--右边，路由导航-->
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
          <router-view/>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>
<script>
import {defineComponent} from 'vue';
import TheHeaderView from "@/components/the-header.vue";
import TheSiderView from "@/components/the-sider.vue";

export default defineComponent({
  name: 'main-view',
  components: {
    TheSiderView,
    TheHeaderView,
  },
  setup() {

    return {
    }
  },
});
</script>
<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.fixed-sider {
  position: fixed;
  top: 60px; /* css 中定义导航栏高度 60px */
  left: 0;
  height: calc(100vh - 60px);
  z-index: 1000;
}

.layout {
  padding-left: 250px; /* the-sider 中定义侧边栏宽度 250px */
  padding-top: 60px; /* css 中定义导航栏高度 60px */
  text-align: left;
}
</style>