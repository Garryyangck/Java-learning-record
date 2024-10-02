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
  <strong>选择要购票的乘客：</strong>&nbsp;
  <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions"/>
  <br/>
  <div class="order-tickets">
    <a-row class="order-tickets-header" v-if="tickets.length > 0">
      <a-col :span="2">乘客</a-col>
      <a-col :span="6">身份证</a-col>
      <a-col :span="4">票种</a-col>
      <a-col :span="4">座位类型</a-col>
    </a-row>
    <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
      <a-col :span="2">{{ticket.passengerName}}</a-col>
      <a-col :span="6">{{ticket.passengerIdCard}}</a-col>
      <a-col :span="4">
        <a-select v-model:value="ticket.passengerType" style="width: 100%">
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-col>
      <a-col :span="4">
        <a-select v-model:value="ticket.seatTypeCode" style="width: 100%">
          <a-select-option v-for="item in seatTypes" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
  </div>
  <div v-if="tickets.length > 0">
    <a-button type="primary" size="large" @click="finishCheckPassenger">提交订单</a-button>
  </div>

  <a-modal v-model:visible="visible" title="请核对以下信息"
           style="top: 50px; width: 800px"
           ok-text="确认" cancel-text="取消"
           @ok="showFirstImageCodeModal">
    <div class="order-tickets">
      <a-row class="order-tickets-header" v-if="tickets.length > 0">
        <a-col :span="3">乘客</a-col>
        <a-col :span="15">身份证</a-col>
        <a-col :span="3">票种</a-col>
        <a-col :span="3">座位类型</a-col>
      </a-row>
      <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
        <a-col :span="3">{{ticket.passengerName}}</a-col>
        <a-col :span="15">{{ticket.passengerIdCard}}</a-col>
        <a-col :span="3">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === ticket.passengerType">
              {{item.desc}}
            </span>
          </span>
        </a-col>
        <a-col :span="3">
          <span v-for="item in seatTypes" :key="item.code">
            <span v-if="item.code === ticket.seatTypeCode">
              {{item.desc}}
            </span>
          </span>
        </a-col>
      </a-row>
    </div>
  </a-modal>

</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'order-view',
  setup() {
    const passengers = ref([]);
    const passengerOptions = ref([]); // 绑定复选框，进行列表展示
    const passengerChecks = ref([]); // 勾选的结果，里面存储的是 passengerOptions.value，即整个 passengerQueryVo
    const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
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

    // 购票列表，用于界面展示，并传递到后端接口，用来描述：哪个乘客购买什么座位的票
    // {
    //   passengerId: 123,
    //   passengerType: "1",
    //   passengerName: "张三",
    //   passengerIdCard: "12323132132",
    //   seatTypeCode: "1",
    // }
    const tickets = ref([]);
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const visible = ref(false);

    // 勾选或去掉某个乘客时，在购票列表中加上或去掉一张表
    watch(() => passengerChecks.value, (newVal, oldVal) => {
      console.log("勾选乘客发生变化", oldVal, newVal);
      // 每次有变化时，把购票列表清空，重新构造列表
      tickets.value = [];
      passengerChecks.value.forEach((item) => tickets.value.push({
        passengerId: item.id,
        passengerType: item.type,
        seatTypeCode: seatTypes[0].code,
        passengerName: item.name,
        passengerIdCard: item.idCard,
      }));
    }, {immediate: true});

    const handleQueryPassenger = () => {
      axios.get('/member/passenger/query-all').then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          passengers.value = responseVo.data;
          passengers.value.forEach((item) => {
            passengerOptions.value.push({
              // label 和 value 是 <a-checkbox-group> 组件里规定的，不能改名
              label: item.name, // 看到的是 Garry
              value: item, // 实际的值为整个 passengerQueryVo，即 passengerChecks 存放的数据
            });
          })
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    const finishCheckPassenger = () => {
      console.log("购票列表：", tickets.value);

      if (tickets.value.length > 5) {
        notification.error({description: '最多只能购买5张车票'});
        return;
      }

      // 弹出确认界面
      visible.value = true;
    };

    onMounted(() => {
      handleQueryPassenger();
    });

    return {
      passengers,
      passengerOptions,
      passengerChecks,
      dailyTrainTicket,
      seatTypes,
      tickets,
      PASSENGER_TYPE_ARRAY,
      visible,
      finishCheckPassenger,
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

/* 定制复选框样式 */
:deep(.ant-checkbox-group) {
  display: flex;
  align-items: center; /* 垂直居中 */
  flex-wrap: nowrap; /* 禁止换行 */
}

:deep(.ant-checkbox-wrapper) {
  font-size: 16px;
  color: #333;
  margin-right: 20px;
  align-items: center;
  white-space: nowrap; /* 防止标签内的换行 */
}

:deep(.ant-checkbox-wrapper:hover) {
  color: #3498db;
}

:deep(.ant-checkbox-inner) {
  border-radius: 4px;
  border: 2px solid #3498db;
  background-color: #fff;
  width: 18px;
  height: 18px;
}

:deep(.ant-checkbox-checked .ant-checkbox-inner) {
  background-color: #3498db;
}

:deep(.ant-checkbox-inner::after) {
  border: 2px solid #fff;
  border-top: 0;
  border-left: 0;
  height: 8px;
  width: 4px;
  left: 5px;
  top: 1px;
  transform: rotate(45deg);
}

:deep(.ant-checkbox:hover .ant-checkbox-inner) {
  border-color: #2980b9;
}

:deep(.ant-checkbox-checked:hover .ant-checkbox-inner) {
  border-color: #2980b9;
}

:deep(.ant-checkbox-checked::after) {
  border-color: #fff;
}

:deep(.ant-checkbox-disabled .ant-checkbox-inner) {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
}

:deep(.ant-checkbox-disabled .ant-checkbox-inner::after) {
  border-color: #ccc;
}

/* 定制label样式 */
:deep(.ant-checkbox-wrapper) {
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

:deep(.ant-checkbox-wrapper:hover) {
  background-color: #ecf0f1;
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