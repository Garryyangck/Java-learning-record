<!--suppress JSCheckFunctionSignatures -->
<template>
  <a-row class="login">
    <!--<a-col :span="8" :offset="8" class="login-main">--> <!--登录框长度为8，offset即前面有8个格子-->
    <a-col class="login-main">
      <h1 style="text-align: center">
        <rocket-two-tone/>
        Garry售票系统
      </h1>
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
      >
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号！' }]"
        >
          <a-input v-model:value="loginForm.mobile" placeholder="请输入手机号"/>
        </a-form-item> <!--label是输入框前面的说明文字，这里不需要-->

        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码！' }]"
        >
          <a-input v-model:value="loginForm.code" placeholder="请输入验证码">
            <template #addonAfter>
              <a ref="sendCodeRef" @click="sendCode">{{ sendCodeLabel }}</a>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block style="height: auto" @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </a-col>
  </a-row>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from 'axios';
import {notification} from 'ant-design-vue';
import {useRouter} from "vue-router";
import store from "@/store";

export default defineComponent({
  name: 'login-view',
  setup() {
    const countdown = ref(0); // 倒计时时间
    const sendCodeLabel = ref('获取验证码'); // 按钮显示的文本
    const counting = ref(false); // 是否正在倒计时
    const sendCodeRef = ref(null); // 发送验证码的样式
    const router = useRouter(); // 路由转发
    const loginForm = reactive({
      mobile: '17380672612',
      code: 'gary',
    });

    const setupTimer = (countdownSecond) => {
      // 设置倒计时参数
      countdown.value = countdownSecond;
      sendCodeLabel.value = `${countdown.value}秒`;
      counting.value = true;

      // 修改字体颜色
      sendCodeRef.value.style.color = '#808080';

      // 倒计时函数
      const intervalId = setInterval(() => {
        countdown.value--;
        sendCodeLabel.value = `${countdown.value}秒`;

        if (countdown.value === 0) {
          clearInterval(intervalId); // 清除定时器
          sendCodeLabel.value = '获取验证码'; // 重置按钮文本
          counting.value = false; // 标记倒计时结束
          sendCodeRef.value.style.color = '#1890ff'; // 字体恢复颜色
        }
      }, 1000);
    };

    const sendCode = () => { // 注意，此处必须是 = () => {} 的 lambda 表达式的写法，而不能是 = {}，后者不是函数！
      if (counting.value) { // 倒计时中直接return
        notification.error({description: '请勿频繁获取验证码'});
        return;
      }

      axios.post('/member/member/send-code', {
        mobile: loginForm.mobile
      }).then(response => { // 这里也是 lambda 表达式，response 作参数
        let responseVo = response.data;
        if (responseVo.success) {
          notification.success({description: '验证码发送成功，请在5分钟内完成登录'});
          countdown.value = 60;
          setupTimer(60); // 短信发送成功，启动60秒计时器
        } else {
          let msgs = responseVo.msg.split('\n');
          for (const msg of msgs) {
            notification.error({description: msg});
          }
          // 服务器异常，验证码发送失败，60秒倒计时，防止在服务器不正常的时候接收大量请求
          if (responseVo.code === 3) {
            setupTimer(60);
          } else { // 参数输入异常，3秒倒计时
            setupTimer(3);
          }
        }
      })
    }

    const login = () => {
      axios.post('/member/member/login', {
        mobile: loginForm.mobile,
        code: loginForm.code
      }).then(response => {
        let responseVo = response.data;
        if (responseVo.success) {
          notification.success({description: '登录成功'});
          // 跳转到欢迎页
          router.push('/welcome');
          // 将token存储到前端
          store.commit("setMember", responseVo.data);
        } else {
          let msgs = responseVo.msg.split('\n');
          for (const msg of msgs) {
            notification.error({description: msg});
          }
        }
      })
    }

    onMounted(() => {
      document.title = '登录';

      // 进入登录页面时，自动清空session中的member
      store.commit('setMember', {});
    });

    return { // 开放给template的组件
      loginForm,
      sendCodeLabel,
      sendCodeRef,
      sendCode,
      login,
    };
  },
});
</script>

<style scoped>
/* 定义登录页面的整体样式 */
.login {
  /* 设置背景颜色为明亮的天蓝色 */
  background-color: #e6f7ff;
  /* 设置页面居中 */
  display: flex;
  justify-content: center;
  align-items: center;
  /* 设置最小高度为视口的高度 */
  min-height: 100vh;
  /* 添加渐变背景 */
  background-image: linear-gradient(#e6f7ff, #ffffff);
}

/* 使用 :deep 伪元素穿透作用域，定义登录框的样式 */
.login :deep(.ant-col).login-main {
  /* 设置背景颜色为白色 */
  background-color: #ffffff;
  /* 设置阴影效果 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 设置圆角 */
  border-radius: 8px;
  /* 设置内边距 */
  padding: 20px;
  /* 设置宽度 */
  width: 100%;
  /* 设置最大宽度 */
  max-width: 500px; /* 放大登录框的尺寸 */
  /* 设置等比例放大的动画 */
  transition: transform 0.3s ease-in-out;
}

/* 定义动画效果 */
@keyframes scaleUp {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(1.1); /* 设置放大的比例 */
  }
}

/* 应用动画效果到登录框 */
.login :deep(.ant-col).login-main {
  /* 添加动画效果，使登录框在加载时等比例放大 */
  animation: scaleUp 2s ease-in-out forwards;
}

/* 登录框内的标题样式 */
.login :deep(.ant-col).login-main h1 {
  /* 设置字体颜色 */
  color: #0c73c2;
  /* 设置字体大小 */
  font-size: 24px;
  /* 设置字体加粗 */
  font-weight: bold;
  /* 设置动画效果 */
  animation: logoPulse 2s infinite;
}

/* 登录框内的表单样式 */
.login :deep(.ant-col).login-main .ant-form {
  /* 设置内边距 */
  padding: 20px 0;
}

/* 登录框内的表单项样式 */
.login :deep(.ant-col).login-main .ant-form-item {
  /* 设置内边距 */
  margin-bottom: 20px;
}

/* 登录框内的输入框样式 */
.login :deep(.ant-col).login-main .ant-input {
  /* 设置边框颜色 */
  border-color: #40a9ff;
  /* 设置圆角 */
  border-radius: 4px;
  /* 设置内边距 */
  padding: 12px;
  /* 设置字体大小 */
  font-size: 16px;
  /* 设置过渡动画 */
  transition: border-color 0.3s, box-shadow 0.3s;
}

/* 输入框聚焦时的样式 */
.login :deep(.ant-col).login-main .ant-input:focus {
  /* 设置边框颜色 */
  border-color: #1890ff;
  /* 添加阴影效果 */
  box-shadow: 0 0 0 2px rgba(64, 169, 255, 0.2);
}

/* 登录框内的按钮样式 */
.login :deep(.ant-col).login-main .ant-btn {
  /* 设置背景颜色 */
  background-color: #40a9ff;
  /* 设置边框颜色 */
  border-color: #40a9ff;
  /* 设置圆角 */
  border-radius: 4px;
  /* 设置字体大小 */
  font-size: 16px;
  /* 设置过渡动画 */
  transition: background-color 0.3s, border-color 0.3s;
}

/* 按钮悬停时的样式 */
.login :deep(.ant-col).login-main .ant-btn:hover {
  /* 设置背景颜色 */
  background-color: #1890ff;
  /* 设置边框颜色 */
  border-color: #1890ff;
}

/* 定义动画效果 */
@keyframes logoPulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}
</style>
