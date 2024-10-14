<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      <station-select-view v-model:value="params.start" placeholder="请选择出发站" style="width: 150px"/>
      <station-select-view v-model:value="params.end" placeholder="请选择到达站" style="width: 150px"/>
      <a-button type="primary" @click="handleQuery()">查询</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-button type="primary" @click="toOrder(record)" size="middle">
            预定
          </a-button>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        出发站：{{ record.start }}<br/>
        到达站：{{ record.end }}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        出发：{{ record.startTime }}<br/>
        到达：{{ record.endTime }}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{ calDuration(record.startTime, record.endTime) }}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          余票：{{ record.ydz }}<br/>
          票价：{{ record.ydzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          余票：{{ record.edz }}<br/>
          票价：{{ record.edzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          余票：{{ record.rw }}<br/>
          票价：{{ record.rwPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          余票：{{ record.yw }}<br/>
          票价：{{ record.ywPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import StationSelectView from "@/components/station-select.vue";
import dayjs from "dayjs";
import router from "@/router";

export default defineComponent({
  name: "ticket-view",
  components: {StationSelectView},
  setup() {
    const visible = ref(false);
    const dailyTrainTicket = reactive({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      ydz: undefined,
      ydzPrice: undefined,
      edz: undefined,
      edzPrice: undefined,
      rw: undefined,
      rwPrice: undefined,
      yw: undefined,
      ywPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainTickets = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      date: null,
      start: null,
      end: null,
    });
    const columns = ref([
      {
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
      },
      {
        title: '车站',
        dataIndex: 'station',
      },
      {
        title: '时间',
        dataIndex: 'time',
      },
      {
        title: '历时',
        dataIndex: 'duration',
      },
      {
        title: '一等座',
        dataIndex: 'ydz',
        key: 'ydz',
      },
      {
        title: '二等座',
        dataIndex: 'edz',
        key: 'edz',
      },
      {
        title: '软卧',
        dataIndex: 'rw',
        key: 'rw',
      },
      {
        title: '硬卧',
        dataIndex: 'yw',
        key: 'yw',
      },
      {
        title: '操作',
        dataIndex: 'operation',
        key: 'operation',
      },
    ]);

    const toOrder = (record) => {
      SessionStorage.set(SESSION_ORDER, record);
      router.push('/order');
    };

    const handleQuery = (param) => {
      if (Tool.isEmpty(params.value.date)) {
        notification.error({description: '请选择日期'});
        return;
      }
      let bySearch = false;
      if (!param) {
        param = {
          pageNum: 1,
          pageSize: pagination.value.pageSize,
        };
        bySearch = true;
      }
      loading.value = true;
      axios.get("/business/daily-train-ticket/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          date: params.value.date,
          start: params.value.start,
          end: params.value.end,
        }
      }).then((response) => {
        loading.value = false;
        SessionStorage.set(SESSION_TICKET_PARAM, params.value);
        let responseVo = response.data;
        if (responseVo.success) {
          dailyTrainTickets.value = responseVo.data.list;
          pagination.value.total = responseVo.data.total;
          pagination.value.current = responseVo.data.pageNum;
          pagination.value.pageSize = responseVo.data.pageSize; // 让修改页面可行，否则即使修改为 50，查出来 50 条，还是只能显示 10 条
          if (bySearch)
            notification.success({description: '查询成功'});
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

    /**
     * 计算 endTime - startTime，如果 endTime < startTime，则会返回 24:00:00 - (startTime - endTime)
     */
    const calDuration = (startTime, endTime) => {
      let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
      return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    };

    onMounted(() => {
      document.title = '余票查询';
      params.value = SessionStorage.get(SESSION_TICKET_PARAM) || {};
      if (Tool.isNotEmpty(params.value.date)) {
        handleQuery({
          pageNum: 1,
          pageSize: pagination.value.pageSize,
        });
      }
    });

    return {
      visible,
      dailyTrainTicket,
      dailyTrainTickets,
      pagination,
      columns,
      loading,
      params,
      toOrder,
      handleQuery,
      handleTableChange,
      calDuration,
    };
  },
});
</script>

<style scoped>

</style>
