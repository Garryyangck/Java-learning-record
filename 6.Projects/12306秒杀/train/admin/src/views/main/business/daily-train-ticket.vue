<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "daily-train-ticket-view",
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
    const columns = ref([
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
      title: '出发站拼音',
      dataIndex: 'startPinyin',
      key: 'startPinyin',
    },
    {
      title: '出发时间',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: '出发站序',
      dataIndex: 'startIndex',
      key: 'startIndex',
    },
    {
      title: '到达站',
      dataIndex: 'end',
      key: 'end',
    },
    {
      title: '到达站拼音',
      dataIndex: 'endPinyin',
      key: 'endPinyin',
    },
    {
      title: '到站时间',
      dataIndex: 'endTime',
      key: 'endTime',
    },
    {
      title: '到站站序',
      dataIndex: 'endIndex',
      key: 'endIndex',
    },
    {
      title: '一等座余票',
      dataIndex: 'ydz',
      key: 'ydz',
    },
    {
      title: '一等座票价',
      dataIndex: 'ydzPrice',
      key: 'ydzPrice',
    },
    {
      title: '二等座余票',
      dataIndex: 'edz',
      key: 'edz',
    },
    {
      title: '二等座票价',
      dataIndex: 'edzPrice',
      key: 'edzPrice',
    },
    {
      title: '软卧余票',
      dataIndex: 'rw',
      key: 'rw',
    },
    {
      title: '软卧票价',
      dataIndex: 'rwPrice',
      key: 'rwPrice',
    },
    {
      title: '硬卧余票',
      dataIndex: 'yw',
      key: 'yw',
    },
    {
      title: '硬卧票价',
      dataIndex: 'ywPrice',
      key: 'ywPrice',
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
      axios.get("/business/admin/daily-train-ticket/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          dailyTrainTickets.value = responseVo.data.list;
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
      document.title = '余票信息';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      visible,
      dailyTrainTicket,
      dailyTrainTickets,
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
