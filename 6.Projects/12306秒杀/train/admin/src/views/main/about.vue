<template>
  <h1>关于</h1>
</template>

<script>
import {defineComponent, onMounted} from 'vue';

export default defineComponent({
  name: 'about-view',
  setup() {
    onMounted(() => {
      document.title = '关于';
    });

    return {

    };
  },
});
</script>

<style scoped>

</style>