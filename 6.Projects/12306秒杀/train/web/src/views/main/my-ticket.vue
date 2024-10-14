<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="tickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'seat'">
        <span>{{record.carriageIndex}}号车厢，</span>
        <span>
          {{record.seatRow}}行
          <span v-for="col in SEAT_COL_ARRAY" :key="col.code">
            <span v-if="col.code === record.seatCol && col.type === record.seatType">
              {{col.desc}}列，
            </span>
          </span>
        </span>
        <span>
          <span v-for="type in SEAT_TYPE_ARRAY" :key="type.code">
            <span v-if="type.code === record.seatType">
              {{type.desc}}
            </span>
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        <div>
          出发：{{record.startStation}}&nbsp;&nbsp;&nbsp;&nbsp;发车：{{record.startTime}}
        </div>
        <div>
          到达：{{record.endStation}}&nbsp;&nbsp;&nbsp;&nbsp;到站：{{record.endTime}}
        </div>
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "ticket-view",
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    const ticket = reactive({
      id: undefined,
      memberId: undefined,
      passengerId: undefined,
      passengerName: undefined,
      trainDate: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      seatRow: undefined,
      seatCol: undefined,
      startStation: undefined,
      startTime: undefined,
      endStation: undefined,
      endTime: undefined,
      seatType: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const tickets = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = ref([
      // {
      //   title: '会员id',
      //   dataIndex: 'memberId',
      //   key: 'memberId',
      // },
      // {
      //   title: '乘客id',
      //   dataIndex: 'passengerId',
      //   key: 'passengerId',
      // },
      {
        title: '乘客',
        dataIndex: 'passengerName',
        key: 'passengerName',
      },
      {
        title: '日期',
        dataIndex: 'trainDate',
        key: 'trainDate',
      },
      {
        title: '车次',
        dataIndex: 'trainCode',
        key: 'trainCode',
      },
      {
        title: '座位',
        dataIndex: 'seat',
        key: 'seat',
      },
      // {
      //   title: '箱序',
      //   dataIndex: 'carriageIndex',
      //   key: 'carriageIndex',
      // },
      // {
      //   title: '排号',
      //   dataIndex: 'seatRow',
      //   key: 'seatRow',
      // },
      // {
      //   title: '列号',
      //   dataIndex: 'seatCol',
      //   key: 'seatCol',
      // },
      // {
      //   title: '座位类型',
      //   dataIndex: 'seatType',
      //   key: 'seatType',
      // },
      {
        title: '始末站',
        dataIndex: 'station',
        key: 'station',
      },
      // {
      //   title: '出发站',
      //   dataIndex: 'startStation',
      //   key: 'startStation',
      // },
      // {
      //   title: '出发时间',
      //   dataIndex: 'startTime',
      //   key: 'startTime',
      // },
      // {
      //   title: '到达站',
      //   dataIndex: 'endStation',
      //   key: 'endStation',
      // },
      // {
      //   title: '到站时间',
      //   dataIndex: 'endTime',
      //   key: 'endTime',
      // },
      {
        title: '操作',
        dataIndex: 'operation'
      }
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
      axios.get("/member/ticket/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          tickets.value = responseVo.data.list;
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
      document.title = '我的车票';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      SEAT_COL_ARRAY,
      SEAT_TYPE_ARRAY,
      visible,
      ticket,
      tickets,
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
