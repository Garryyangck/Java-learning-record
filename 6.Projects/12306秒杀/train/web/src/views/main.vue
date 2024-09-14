<template>
  <a-layout>
    <the-header-view/> <!--导航页-->
    <a-layout>
      <the-sider-view/> <!--左侧边栏-->
      <a-layout style="padding: 0 0 0 24px; text-align: left"> <!--右边，路由导航-->
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
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

</style>