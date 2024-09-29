<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      <train-select-view v-model:value="params.code" style="width: 300px"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
      <a-button type="danger" @click="genDaily()">重新生成所选日期的每日数据</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrains"
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
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{ item.desc }}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="每日车次" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrain" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrain.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view
            v-model:value="dailyTrain.code"
            @change="handleTrainSelectChange"
            :disabled="dailyTrain.id !== undefined"
        />
      </a-form-item>
      <a-form-item label="车次类型">
        <a-select v-model:value="dailyTrain.type" disabled>
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <a-input v-model:value="dailyTrain.start" disabled/>
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="dailyTrain.startPinyin" disabled/>
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="dailyTrain.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="终点站">
        <a-input v-model:value="dailyTrain.end" disabled/>
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="dailyTrain.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="dailyTrain.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
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
  name: "daily-train-view",
  components: {TrainSelectView},
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    const dailyTrain = reactive({
      id: undefined,
      date: undefined,
      code: undefined,
      type: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrains = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      code: null,
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
        dataIndex: 'code',
        key: 'code',
      },
      {
        title: '车次类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '始发站',
        dataIndex: 'start',
        key: 'start',
      },
      {
        title: '始发站拼音',
        dataIndex: 'startPinyin',
        key: 'startPinyin',
      },
      {
        title: '出发时间',
        dataIndex: 'startTime',
        key: 'startTime',
      },
      {
        title: '终点站',
        dataIndex: 'end',
        key: 'end',
      },
      {
        title: '终点站拼音',
        dataIndex: 'endPinyin',
        key: 'endPinyin',
      },
      {
        title: '到站时间',
        dataIndex: 'endTime',
        key: 'endTime',
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ]);

    watch(() => params.value.code, () => {
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
      dailyTrain.id = undefined;
      dailyTrain.date = undefined;
      dailyTrain.code = undefined;
      dailyTrain.type = undefined;
      dailyTrain.start = undefined;
      dailyTrain.startPinyin = undefined;
      dailyTrain.startTime = undefined;
      dailyTrain.end = undefined;
      dailyTrain.endPinyin = undefined;
      dailyTrain.endTime = undefined;
      dailyTrain.createTime = undefined;
      dailyTrain.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrain.id = record.id;
      dailyTrain.date = record.date;
      dailyTrain.code = record.code;
      dailyTrain.type = record.type;
      dailyTrain.start = record.start;
      dailyTrain.startPinyin = record.startPinyin;
      dailyTrain.startTime = record.startTime;
      dailyTrain.end = record.end;
      dailyTrain.endPinyin = record.endPinyin;
      dailyTrain.endTime = record.endTime;
      dailyTrain.createTime = record.createTime;
      dailyTrain.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/daily-train/save", dailyTrain).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (dailyTrain.id === undefined)
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
        params.value.code = null;
        params.value.date = null;
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/daily-train/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          code: params.value.code,
          date: params.value.date,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          dailyTrains.value = responseVo.data.list;
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

    const handleTrainSelectChange = (train) => {
      dailyTrain.type = train.type;
      dailyTrain.start = train.start;
      dailyTrain.startPinyin = train.startPinyin;
      dailyTrain.startTime = train.startTime;
      dailyTrain.end = train.end;
      dailyTrain.endPinyin = train.endPinyin;
      dailyTrain.endTime = train.endTime;
    };

    const genDaily = () => {
      axios.get("/business/admin/daily-train/gen-daily/" + params.value.date).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          notification.success({description: '重新生成成功成功'});
        } else {
          if (responseVo.code === 28) // API_ARGUMENT_MISMATCH(28, "接口参数不匹配")
            notification.error({description: '请先选择日期'});
          else
            notification.error({description: responseVo.msg});
        }
      })
    };

    onMounted(() => {
      document.title = '每日车次';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      TRAIN_TYPE_ARRAY,
      visible,
      dailyTrain,
      dailyTrains,
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
      handleTrainSelectChange,
      genDaily,
    };
  },
});
</script>

<style scoped>

</style>
