<template>
  <h1>欢迎来到 <b>Garry售票系统</b></h1>
</template>

<script>
import {defineComponent, onMounted} from 'vue';

export default defineComponent({
  name: 'welcome-view',
  setup() {
    onMounted(() => {
      document.title = '欢迎';
    });

    return {

    };
  },
});
</script>

<style scoped>

</style>