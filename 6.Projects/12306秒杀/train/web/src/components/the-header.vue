<!--suppress JSCheckFunctionSignatures -->
<template>
  <a-layout-header>
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
          <HomeOutlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <EyeOutlined/> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/my-ticket">
        <router-link to="/my-ticket">
          <CreditCardOutlined /> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
    </a-menu>
    <div style="margin-left: auto; color: white;">
      <router-link to="/message">
        <a-badge :count="unreadNum" :overflowCount="999999">
          <a-button style="margin-right: 10px">
            <MessageOutlined/> &nbsp; 我的消息
          </a-button>
        </a-badge>
      </router-link>
      您好：{{ member.mobile }} &nbsp;&nbsp;
      <router-link to="/login" style="color: white;">
        <a-button type="danger">
          退出登录
        </a-button>
      </router-link>
    </div>
  </a-layout-header>
</template>

<script>
import {defineComponent, onMounted, ref, watch} from 'vue';
import store from "@/store";
import router from "@/router";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'the-header-view',
  setup() {
    let member = store.state.member;
    const selectedKeys = ref(['/welcome']);
    const unreadNum = ref(0);

    /**
     * 动态监视 router.currentRoute.value.path 的变化，变化时触发后面的函数
     */
    watch(() => router.currentRoute.value.path, (newValue) => {
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
      axios.get("/business/message/unread-number/" + member.id).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          store.state.unreadNum = responseVo.data;
        } else {
          notification.error({description: responseVo.msg});
        }
      });
    }, {immediate: true});

    watch(() => store.state.unreadNum, (newValue) => {
      if (newValue < 0) {
        unreadNum.value = 0;
      } else {
        unreadNum.value = newValue;
      }
    });

    const url = 'ws://localhost:9999/ws/message/' + member.id;
    const socket = ref(null);

    onMounted(() => {
      axios.get("/business/message/unread-number/" + member.id).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          store.state.unreadNum = responseVo.data;
        } else {
          notification.error({description: responseVo.msg});
        }
      });

      // 连接 websocket 服务器
      socket.value = new WebSocket(url);
      socket.value.onopen = () => {
        console.log('WebSocket连接成功', url);
      };
      socket.value.onmessage = (event) => {
        console.log('收到来自WebSocket服务器的消息', event);
        store.state.unreadNum = JSON.parse(event.data).unreadNum;
      };
      socket.value.onclose = () => {
        console.log('WebSocket断开连接', url);
      };
      socket.value.onerror = (error) => {
        console.error('WebSocket连接出错:', error);
      };
    });

    return {
      selectedKeys,
      member,
      unreadNum,
    };
  },
});
</script>

<style scoped> /*scoped修饰的css只在本页面生效*/

</style>
