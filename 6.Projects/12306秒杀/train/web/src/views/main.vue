<template>
  <a-layout>
    <the-header-view/>
    <a-layout>
      <the-sider-view/>
      <a-layout style="padding: 0 24px 24px; text-align: left">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item>List</a-breadcrumb-item>
          <a-breadcrumb-item>App</a-breadcrumb-item>
        </a-breadcrumb>
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          会员总数：{{count}}
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>
<script>
import {defineComponent, ref} from 'vue';
import TheHeaderView from "@/components/the-header.vue";
import TheSiderView from "@/components/the-sider.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'main-view',
  components: {
    TheSiderView,
    TheHeaderView,
  },
  setup() {
    const count = ref(0);

    axios.get('/member/member/count').then(response => {
      let responseVo = response.data;
      if (responseVo.success) {
        count.value = responseVo.data;
      } else {
        notification.error({description: responseVo.msg});
      }
    })

    return {
      count,
    }
  },
});
</script>
<style scoped>

</style>