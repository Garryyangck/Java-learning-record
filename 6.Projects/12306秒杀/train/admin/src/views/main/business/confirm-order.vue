<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      
    </a-space>
  </p>
  <a-table :dataSource="confirmOrders"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'status'">
        <span v-for="item in CONFIRM_ORDER_STATUS_ARRAY" :key="item.code">
          <span v-if="item.code === record.status">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-if="column.dataIndex === 'tickets'">
        <span v-for="item in record.tickets" :key="item.passengerId">
          <div>
            {{item.passengerName}}&nbsp;
            <span v-for="passengerType in PASSENGER_TYPE_ARRAY" :key="passengerType.code">
              <span v-if="passengerType.code === item.passengerType">
                {{passengerType.desc}}
              </span>
            </span>&nbsp;
            <span v-for="seatType in SEAT_TYPE_ARRAY" :key="seatType.code">
              <span v-if="seatType.code === item.seatTypeCode">
                {{seatType.desc}}
              </span>
            </span>&nbsp;
            {{item.seat}}
          </div>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "confirm-order-view",
  setup() {
    const CONFIRM_ORDER_STATUS_ARRAY = window.CONFIRM_ORDER_STATUS_ARRAY;
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const confirmOrder = reactive({
      id: undefined,
      memberId: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      end: undefined,
      dailyTrainTicketId: undefined,
      tickets: undefined,
      status: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const confirmOrders = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = ref([
    {
      title: '会员ID',
      dataIndex: 'memberId',
      key: 'memberId',
    },
    {
      title: '日期',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: '出发站',
      dataIndex: 'start',
      key: 'start',
    },
    {
      title: '到达站',
      dataIndex: 'end',
      key: 'end',
    },
    // {
    //   title: '余票ID',
    //   dataIndex: 'dailyTrainTicketId',
    //   key: 'dailyTrainTicketId',
    // },
    {
      title: '车票',
      dataIndex: 'tickets',
      key: 'tickets',
    },
    {
      title: '订单状态',
      dataIndex: 'status',
      key: 'status',
    },
    ]);


    const handleQuery = (param) => {
      let byRefresh = false;
      if (!param) {
        param = {
          pageNum: 1,
          pageSize: pagination.value.pageSize,
        };
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/confirm-order/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          confirmOrders.value = responseVo.data.list;
          confirmOrders.value.forEach(item => {
            item.tickets = JSON.parse(item.tickets); // 将每一个 confirm-order 的 tickets 从 JSONStr 转换为对象
          })
          pagination.value.total = responseVo.data.total;
          pagination.value.current = responseVo.data.pageNum;
          pagination.value.pageSize = responseVo.data.pageSize; // 让修改页面可行，否则即使修改为 50，查出来 50 条，还是只能显示 10 条
          if (byRefresh)
            notification.success({description: '刷新成功'});
        } else {
          let msgs = responseVo.msg.split('\n');
          for (const msg of msgs) {
            notification.error({description: msg});
          }
        }
      })
    };

    const handleTableChange = (pagination) => {
      // handleTableChange 自带一个 pagination 参数，含有 total，current，pageSize 三个属性
      handleQuery({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    onMounted(() => {
      document.title = '确认订单';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      CONFIRM_ORDER_STATUS_ARRAY,
      PASSENGER_TYPE_ARRAY,
      SEAT_TYPE_ARRAY,
      confirmOrder,
      confirmOrders,
      pagination,
      columns,
      loading,
      handleQuery,
      handleTableChange,
    };
  },
});
</script>

<style scoped>

</style>
