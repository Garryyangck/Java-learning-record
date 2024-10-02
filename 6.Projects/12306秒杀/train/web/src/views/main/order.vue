<template>
  <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
  <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次&nbsp;
  <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
  <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
  <span class="order-train-main">——</span>&nbsp;
  <span class="order-train-main">{{dailyTrainTicket.end}}</span>站
  <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;
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
.order-train-main {
  font-size: 18px;
  font-weight: bold;
}
</style>