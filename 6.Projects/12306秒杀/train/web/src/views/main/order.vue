<!--suppress JSCheckFunctionSignatures -->
<template>
  <div class="order-train">
    <span class="order-train-main">{{ dailyTrainTicket.date }}</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.trainCode }} 次</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.start }}站</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.startTime }}</span>&nbsp;
    <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.end }}站</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.endTime }}</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{ item.desc }}</span>：
        <span class="order-train-ticket-main">{{ item.price }}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{ item.count }}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
    </div>
  </div>

  <a-divider/>
  <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions"/>
  <br/>
  <div class="order-tickets">
    <a-row class="order-tickets-header" v-if="tickets.length > 0">
      <a-col :span="3">乘客</a-col>
      <a-col :span="9">身份证</a-col>
      <a-col :span="6">票种</a-col>
      <a-col :span="6">座位类型</a-col>
    </a-row>
    <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
      <a-col :span="3">{{ticket.passengerName}}</a-col>
      <a-col :span="9">{{ticket.passengerIdCard}}</a-col>
      <a-col :span="6">
        <a-select v-model:value="ticket.passengerType" style="width: 100%">
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-col>
      <a-col :span="6">
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
        <a-col :span="6">乘客</a-col>
        <a-col :span="8">身份证</a-col>
        <a-col :span="5">票种</a-col>
        <a-col :span="5">座位类型</a-col>
      </a-row>
      <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
        <a-col :span="6">{{ticket.passengerName}}</a-col>
        <a-col :span="8">{{ticket.passengerIdCard}}</a-col>
        <a-col :span="5">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === ticket.passengerType">
              {{item.desc}}
            </span>
          </span>
        </a-col>
        <a-col :span="5">
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
/* ------------------------- order-train ------------------------- */
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
  color: #333; /* 更深的文本颜色 */
  line-height: 1.3; /* 增加行高以提高可读性 */
  margin-bottom: 10px; /* 添加底部边距 */
  font-family: 'Arial', sans-serif; /* 设置字体族 */
  background-color: #f0f0f0; /* 轻微的背景颜色 */
  padding: 8px 15px; /* 添加内边距 */
  border-radius: 5px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  display: inline-block; /* 使背景和阴影应用在文本周围 */
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.order-train .order-train-main:hover {
  background-color: #e0e0e0; /* 鼠标悬停时背景颜色变深 */
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.15); /* 鼠标悬停时阴影效果增强 */
  transform: translateY(-2px); /* 鼠标悬停时轻微上移 */
}

/* 添加文本阴影以增强视觉效果 */
.order-train .order-train-main {
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

/* 如果需要，可以添加一个边框 */
.order-train .order-train-main {
  border: 1px solid transparent; /* 透明边框，以便在添加边框样式时不影响布局 */
  border-radius: 5px; /* 圆角边框 */
}

.order-train .order-train-main:hover {
  border-color: #d0d0d0; /* 鼠标悬停时显示边框 */
}

.order-train .order-train-ticket {
  margin-top: 15px;
  padding: 20px; /* 添加内边距 */
  background-color: #f9f9f9; /* 轻微的背景颜色 */
  background-image: linear-gradient(to bottom, #ffffff, #f9f9f9); /* 添加渐变背景 */
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  transition: transform 0.3s ease, box-shadow 0.3s ease; /* 添加变换和阴影的过渡效果 */
  will-change: transform, box-shadow; /* 优化动画性能 */
}

.order-train .order-train-ticket:hover {
  transform: translateY(-5px); /* 鼠标悬停时轻微上移 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 鼠标悬停时阴影效果增强 */
}

.order-train .order-train-ticket .order-train-ticket-main {
  color: #e53e3e; /* 更鲜艳的红色，增加了透明度渐变 */
  font-size: 18px;
  font-weight: bold;
  transition: color 0.3s ease; /* 添加颜色变化过渡效果 */
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加文本阴影 */
}

.order-train .order-train-ticket .order-train-ticket-main:hover {
  color: #c12f2f; /* 鼠标悬停时颜色变深 */
}

/* 添加一个轻微的动画效果，使文本在悬停时有跳动感 */
.order-train .order-train-ticket .order-train-ticket-main {
  position: relative; /* 设置相对定位 */
  display: inline-block; /* 使动画效果应用于行内块级元素 */
}

.order-train .order-train-ticket .order-train-ticket-main:hover {
  animation: jump 0.6s ease-in-out infinite alternate; /* 应用跳动动画 */
}

/* 定义跳动动画 */
@keyframes jump {
  from {
    transform: translateY(0px);
  }
  to {
    transform: translateY(-3px);
  }
}

/* ------------------------- order-tickets ------------------------- */
.order-tickets {
  margin: 10px 0;
  background-color: #f8f9fa; /* 轻微的背景颜色 */
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.order-tickets .ant-col {
  padding: 10px; /* 增加内边距 */
  border-right: 1px solid #e9ecef; /* 添加列之间的分隔线 */
}

.order-tickets .ant-col:last-child {
  border-right: none; /* 最后一列不加右边界 */
}

.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 10px 0;
  text-align: center; /* 文本居中 */
  font-weight: bold; /* 字体加粗 */
}

.order-tickets .order-tickets-row {
  border: 1px solid #e9ecef; /* 统一边框颜色 */
  border-top: none;
  vertical-align: middle;
  line-height: 1.5; /* 增加行高 */
  padding: 10px 0; /* 增加上下内边距 */
}

.order-tickets .order-tickets-row:first-child {
  border-top: 1px solid #e9ecef; /* 第一行添加顶部边框 */
}

.order-tickets .choose-seat-item {
  margin: 5px 0; /* 调整为上下边距 */
  padding: 5px 10px; /* 增加内边距 */
  background-color: #ffffff; /* 白色背景 */
  border: 1px solid #dee2e6; /* 边框颜色 */
  border-radius: 4px; /* 圆角边框 */
  cursor: pointer; /* 鼠标悬停时显示指针 */
  transition: all 0.3s ease; /* 过渡效果 */
}

.order-tickets .choose-seat-item:hover {
  background-color: #e9ecef; /* 鼠标悬停时背景颜色 */
  border-color: #adb5bd; /* 鼠标悬停时边框颜色 */
}

/* 为表头和行添加间隙和圆角 */
.order-tickets .order-tickets-row:last-child,
.order-tickets .order-tickets-header:last-child {
  border-bottom-left-radius: 8px; /* 左下角圆角 */
  border-bottom-right-radius: 8px; /* 右下角圆角 */
}

.order-tickets .order-tickets-row:first-child,
.order-tickets .order-tickets-header:first-child {
  border-top-left-radius: 8px; /* 左上角圆角 */
  border-top-right-radius: 8px; /* 右上角圆角 */
}
</style>