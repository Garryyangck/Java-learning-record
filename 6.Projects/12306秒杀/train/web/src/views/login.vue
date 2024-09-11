<template>
  <a-row class="login">
    <a-col :span="8" :offset="8" class="login-main"> <!--登录框长度为8，offset即前面有8个格子-->
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
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </a-col>
  </a-row>
</template>

<script setup>
import {reactive, ref, onMounted} from 'vue';
import axios from 'axios';
import {notification} from 'ant-design-vue';


const countdown = ref(0); // 倒计时时间
const sendCodeLabel = ref('获取验证码'); // 按钮显示的文本
const counting = ref(false); // 是否正在倒计时
const sendCodeRef = ref(null);

const loginForm = reactive({
  mobile: '',
  code: '',
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

  axios.post("http://localhost:8080/member/member/send-code", {
    mobile: loginForm.mobile
  }).then(response => { // 这里也是 lambda 表达式，response 作参数
    let responseVo = response.data;
    if (responseVo.success) {
      notification.success({description: '验证码发送成功，请在5分钟内完成登录'});
      countdown.value = 60;
      setupTimer(60); // 短信发送成功，启动60秒计时器
    } else {
      notification.error({description: responseVo.msg});
      // 服务器异常，验证码发送失败，60秒倒计时，防止在服务器不正常的时候接收大量请求
      if (responseVo.code === 3) {
        setupTimer(60);
      } else { // 参数输入异常，3秒倒计时
        setupTimer(3);
      }
    }
  });
}

const login = () => {
  axios.post("http://localhost:8080/member/member/login", {
    mobile: loginForm.mobile,
    code: loginForm.code
  }).then(response => {
    let responseVo = response.data;
    if (responseVo.success) {
      notification.success({description: '登录成功'});
      // 跳转到用户主页

    } else {
      notification.error({description: responseVo.msg});
    }
  })
}
</script>

<style>
.login-main h1 { /*定义的是login-main下的h1标签，而不是所有的login-main和h1标签*/
  font-size: 25px;
  font-weight: bold;
}

.login-main {
  margin-top: 200px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
  transform: scale(1.2); /* 等比例放大 */
  transform-origin: center; /* 确保放大的基点是中心 */
}
</style>
