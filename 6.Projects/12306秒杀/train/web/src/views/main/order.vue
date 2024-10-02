<template>
  <div>{{dailyTrainTicket}}</div>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';

export default defineComponent({
  name: 'order-view',
  setup() {
    const dailyTrainTicket = SessionStorage.get('dailyTrainTicket') || {}; // 避免空指针异常

    return {
      dailyTrainTicket,
    };
  },
});
</script>

<style scoped>

</style>