<!--suppress JSCheckFunctionSignatures -->
<template>
  <div class="order-train">
    <span class="order-train-main">{{ dailyTrainTicket.date }}</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.trainCode }}</span>次&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.start }}</span>站
    <span class="order-train-main">({{ dailyTrainTicket.startTime }})</span>&nbsp;
    <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.end }}</span>站
    <span class="order-train-main">({{ dailyTrainTicket.endTime }})</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{ item.desc }}</span>：
        <span class="order-train-ticket-main">{{ item.price }}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{ item.count }}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
    </div>
  </div>

  <a-divider/>
  {{passengers}}
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'order-view',
  setup() {
    const passengers = ref([]);
    const dailyTrainTicket = SessionStorage.get('dailyTrainTicket') || {};
    console.log("下单的车次信息", dailyTrainTicket);

    const SEAT_TYPE = window.SEAT_TYPE; // 带 YDZ, EDZ, RW, YW
    // 本车次提供的座位类型(count >= 0) seatTypes，含票价，余票等信息，例：
    // {
    //   type: "YDZ",
    //   code: "1",
    //   desc: "一等座",
    //   count: "100",
    //   price: "50",
    // }
    // 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
    const seatTypes = [];
    for (let KEY in SEAT_TYPE) {
      let key = KEY.toLowerCase();
      if (dailyTrainTicket[key] >= 0) {
        seatTypes.push({
          type: KEY,
          code: SEAT_TYPE[KEY]["code"],
          desc: SEAT_TYPE[KEY]["desc"],
          count: dailyTrainTicket[key],
          price: dailyTrainTicket[key + 'Price'],
        })
      }
    }
    console.log("本车次提供的座位：", seatTypes);

    const handleQueryPassenger = () => {
      axios.get('/member/passenger/query-all').then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          passengers.value = responseVo.data;
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    onMounted(() => {
      handleQueryPassenger();
    });

    return {
      passengers,
      dailyTrainTicket,
      seatTypes,
    };
  },
});
</script>

<style scoped>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}

.order-train .order-train-ticket {
  margin-top: 15px;
}

.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}

.order-tickets {
  margin: 10px 0;
}

.order-tickets .ant-col {
  padding: 5px 10px;
}

.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 5px 0;
}

.order-tickets .order-tickets-row {
  border: solid 1px cornflowerblue;
  border-top: none;
  vertical-align: middle;
  line-height: 30px;
}

.order-tickets .choose-seat-item {
  margin: 5px 5px;
}
</style>