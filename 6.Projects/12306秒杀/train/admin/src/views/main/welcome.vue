<!--suppress JSCheckFunctionSignatures -->
<template>
  <div>
    <div v-html="htmlContent" class="markdown-content"></div>
  </div>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'welcome-view',
  setup() {
    const htmlContent = ref(undefined);
    const MARKDOWN = window.MARKDOWN;

    onMounted(() => {
      document.title = '欢迎';

      axios.get("business/markdown/page", {
        params: {
          mdPageCode: MARKDOWN.WELCOME.code,
        }
      }).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          htmlContent.value = responseVo.data;
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    });

    return {
      htmlContent,
    };
  },
});
</script>

<style scoped>
.markdown-content {
  margin-top: 20px;
  border: 1px solid #ccc;
  padding: 10px;
}
</style>