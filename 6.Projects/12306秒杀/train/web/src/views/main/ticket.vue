<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="tickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'seatCol'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatCol">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="车票" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="ticket" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="会员id">
        <a-input v-model:value="ticket.memberId" />
      </a-form-item>
      <a-form-item label="乘客id">
        <a-input v-model:value="ticket.passengerId" />
      </a-form-item>
      <a-form-item label="乘客姓名">
        <a-input v-model:value="ticket.passengerName" />
      </a-form-item>
      <a-form-item label="日期">
        <a-date-picker v-model:value="ticket.trainDate" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      </a-form-item>
      <a-form-item label="车次编号">
        <a-input v-model:value="ticket.trainCode" />
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="ticket.carriageIndex" />
      </a-form-item>
      <a-form-item label="排号">
        <a-input v-model:value="ticket.seatRow" />
      </a-form-item>
      <a-form-item label="列号">
        <a-select v-model:value="ticket.seatCol">
          <a-select-option v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="出发站">
        <a-input v-model:value="ticket.startStation" />
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="ticket.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="到达站">
        <a-input v-model:value="ticket.endStation" />
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="ticket.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="ticket.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
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
    {
      title: '会员id',
      dataIndex: 'memberId',
      key: 'memberId',
    },
    {
      title: '乘客id',
      dataIndex: 'passengerId',
      key: 'passengerId',
    },
    {
      title: '乘客姓名',
      dataIndex: 'passengerName',
      key: 'passengerName',
    },
    {
      title: '日期',
      dataIndex: 'trainDate',
      key: 'trainDate',
    },
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: '箱序',
      dataIndex: 'carriageIndex',
      key: 'carriageIndex',
    },
    {
      title: '排号',
      dataIndex: 'seatRow',
      key: 'seatRow',
    },
    {
      title: '列号',
      dataIndex: 'seatCol',
      key: 'seatCol',
    },
    {
      title: '出发站',
      dataIndex: 'startStation',
      key: 'startStation',
    },
    {
      title: '出发时间',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: '到达站',
      dataIndex: 'endStation',
      key: 'endStation',
    },
    {
      title: '到站时间',
      dataIndex: 'endTime',
      key: 'endTime',
    },
    {
      title: '座位类型',
      dataIndex: 'seatType',
      key: 'seatType',
    },
    {
      title: '操作',
      dataIndex: 'operation'
    }
    ]);

    const onAdd = () => {
      ticket.id = undefined;
      ticket.memberId = undefined;
      ticket.passengerId = undefined;
      ticket.passengerName = undefined;
      ticket.trainDate = undefined;
      ticket.trainCode = undefined;
      ticket.carriageIndex = undefined;
      ticket.seatRow = undefined;
      ticket.seatCol = undefined;
      ticket.startStation = undefined;
      ticket.startTime = undefined;
      ticket.endStation = undefined;
      ticket.endTime = undefined;
      ticket.seatType = undefined;
      ticket.createTime = undefined;
      ticket.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      ticket.id = record.id;
      ticket.memberId = record.memberId;
      ticket.passengerId = record.passengerId;
      ticket.passengerName = record.passengerName;
      ticket.trainDate = record.trainDate;
      ticket.trainCode = record.trainCode;
      ticket.carriageIndex = record.carriageIndex;
      ticket.seatRow = record.seatRow;
      ticket.seatCol = record.seatCol;
      ticket.startStation = record.startStation;
      ticket.startTime = record.startTime;
      ticket.endStation = record.endStation;
      ticket.endTime = record.endTime;
      ticket.seatType = record.seatType;
      ticket.createTime = record.createTime;
      ticket.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/member/ticket/delete/" + record.id).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          notification.success({description: '删除成功'});
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    const handleOk = () => {
      axios.post("/member/ticket/save", ticket).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (passenger.id === undefined)
            notification.success({description: '新增成功'});
          else
            notification.success({description: '修改成功'});
          visible.value = false;
        } else {
          let msgs = responseVo.msg.split('\n');
          for (const msg of msgs) {
            notification.error({description: msg});
          }
        }
      })
    };

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
      onAdd,
      onEdit,
      onDelete,
      handleOk,
      handleQuery,
      handleTableChange,
    };
  },
});
</script>

<style scoped>

</style>
