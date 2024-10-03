<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      <train-select-view v-model:value="params.trainCode" style="width: 300px"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainCarriages"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-button size="small" @click="onEdit(record)">编辑</a-button>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{ item.desc }}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="每日车厢" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainCarriage" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainCarriage.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view
            v-model:value="dailyTrainCarriage.trainCode"
            :disabled="dailyTrainCarriage.id !== undefined"
        />
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="dailyTrainCarriage.index"/>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainCarriage.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位数">
        <a-input v-model:value="dailyTrainCarriage.seatCount" disabled/>
      </a-form-item>
      <a-form-item label="排数">
        <a-input v-model:value="dailyTrainCarriage.rowCount"/>
      </a-form-item>
      <a-form-item label="列数">
        <a-input v-model:value="dailyTrainCarriage.colCount" disabled/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelectView from "@/components/train-select.vue";

export default defineComponent({
  name: "daily-train-carriage-view",
  components: {TrainSelectView},
  setup() {
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const visible = ref(false);
    const dailyTrainCarriage = reactive({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      index: undefined,
      seatType: undefined,
      seatCount: undefined,
      rowCount: undefined,
      colCount: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainCarriages = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null,
      date: null,
    });
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
        title: '箱序',
        dataIndex: 'index',
        key: 'index',
      },
      {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
      },
      {
        title: '座位数',
        dataIndex: 'seatCount',
        key: 'seatCount',
      },
      {
        title: '排数',
        dataIndex: 'rowCount',
        key: 'rowCount',
      },
      {
        title: '列数',
        dataIndex: 'colCount',
        key: 'colCount',
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ]);

    watch(() => dailyTrainCarriage.seatType, () => {
      if (Tool.isNotEmpty(dailyTrainCarriage.seatType)) {
        const list = SEAT_COL_ARRAY.filter(seat => seat.type === dailyTrainCarriage.seatType);
        dailyTrainCarriage.colCount = list.length;
      }
    });

    watch(() => dailyTrainCarriage.rowCount, () => {
      if (Tool.isNotEmpty(dailyTrainCarriage.rowCount)
          && Tool.isNotEmpty(dailyTrainCarriage.colCount)) {
        dailyTrainCarriage.seatCount = dailyTrainCarriage.rowCount * dailyTrainCarriage.colCount;
      } else {
        dailyTrainCarriage.seatCount = 0;
      }
    });

    watch(() => params.value.trainCode, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    watch(() => params.value.date, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    const onAdd = () => {
      dailyTrainCarriage.id = undefined;
      dailyTrainCarriage.date = undefined;
      dailyTrainCarriage.trainCode = undefined;
      dailyTrainCarriage.index = undefined;
      dailyTrainCarriage.seatType = undefined;
      dailyTrainCarriage.seatCount = undefined;
      dailyTrainCarriage.rowCount = undefined;
      dailyTrainCarriage.colCount = undefined;
      dailyTrainCarriage.createTime = undefined;
      dailyTrainCarriage.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrainCarriage.id = record.id;
      dailyTrainCarriage.date = record.date;
      dailyTrainCarriage.trainCode = record.trainCode;
      dailyTrainCarriage.index = record.index;
      dailyTrainCarriage.seatType = record.seatType;
      dailyTrainCarriage.seatCount = record.seatCount;
      dailyTrainCarriage.rowCount = record.rowCount;
      dailyTrainCarriage.colCount = record.colCount;
      dailyTrainCarriage.createTime = record.createTime;
      dailyTrainCarriage.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train-carriage/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/daily-train-carriage/save", dailyTrainCarriage).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (dailyTrainCarriage.id === undefined)
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
        params.value.trainCode = null;
        params.value.date = null;
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/daily-train-carriage/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          trainCode: params.value.trainCode,
          date: params.value.date,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          dailyTrainCarriages.value = responseVo.data.list;
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
      document.title = '每日车厢';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      SEAT_TYPE_ARRAY,
      visible,
      dailyTrainCarriage,
      dailyTrainCarriages,
      pagination,
      columns,
      loading,
      params,
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
