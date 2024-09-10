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
              <a @click="sendCode">获取验证码</a>
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
import {reactive} from 'vue';
import axios from 'axios';
import {notification} from 'ant-design-vue';

const loginForm = reactive({
  mobile: '',
  code: '',
});

const sendCode = () => { // 注意，此处必须是 = () => {} 的 lambda 表达式的写法，而不能是 = {}，后者不是函数！
  axios.post("http://localhost:8080/member/member/send-code", {
    mobile: loginForm.mobile
  }).then(response => { // 这里也是 lambda 表达式，response 作参数
    let responseVo = response.data;
    if (responseVo.success) {
      notification.success({description: '验证码发送成功，请在5分钟内完成登录'});
    } else {
      notification.error({description: responseVo.msg});
    }
  });
}

const login = () => {
  axios.post("http://localhost:8080/member/member/login", {
    mobile: loginForm.mobile,
    code: loginForm.code
  }).then(response =>{
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
