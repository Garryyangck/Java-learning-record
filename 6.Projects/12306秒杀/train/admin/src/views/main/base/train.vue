<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <train-select-view v-model:value="params.code" style="width: 300px"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trains"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-button size="small" @click="onEdit(record)">编辑</a-button>
          <a-popconfirm
              title="生成车站将删除已有的车站，确认生成?"
              @confirm="genTrainStation(record)"
              ok-text="确认" cancel-text="取消">
            <a-button size="small">生成车站</a-button>
          </a-popconfirm>
          <a-popconfirm
              title="生成座位将删除已有的座位，确认生成?"
              @confirm="genTrainSeat(record)"
              ok-text="确认" cancel-text="取消">
            <a-button size="small">生成座位</a-button>
          </a-popconfirm>
          <a-popconfirm
              title="同时删除该车次的所有数据，不可恢复"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="车次" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="train" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="车次类型">
        <a-select v-model:value="train.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <station-select-view v-model:value="train.start"/>
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="train.startPinyin" disabled />
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="train.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="终点站">
        <station-select-view v-model:value="train.end"/>
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="train.endPinyin" disabled />
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="train.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import StationSelectView from "@/components/station-select.vue";
import TrainSelectView from "@/components/train-select.vue";

export default defineComponent({
  name: "train-view",
  components: {TrainSelectView, StationSelectView},
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    const train = reactive({
      id: undefined,
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
    const trains = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      code: null,
    });
    const columns = ref([
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

    watch(() => train.start, () => {
      if (Tool.isNotEmpty(train.start)) {
        train.startPinyin = pinyin(train.start, {toneType: 'none'}).replaceAll(" ", "");
      } else {
        train.startPinyin = undefined;
      }
    });

    watch(() => train.end, () => {
      if (Tool.isNotEmpty(train.end)) {
        train.endPinyin = pinyin(train.end, {toneType: 'none'}).replaceAll(" ", "");
      } else {
        train.endPinyin = undefined;
      }
    });

    watch(() => params.value.code, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    const onAdd = () => {
      train.id = undefined;
      train.code = undefined;
      train.type = undefined;
      train.start = undefined;
      train.startPinyin = undefined;
      train.startTime = undefined;
      train.end = undefined;
      train.endPinyin = undefined;
      train.endTime = undefined;
      train.createTime = undefined;
      train.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      train.id = record.id;
      train.code = record.code;
      train.type = record.type;
      train.start = record.start;
      train.startPinyin = record.startPinyin;
      train.startTime = record.startTime;
      train.end = record.end;
      train.endPinyin = record.endPinyin;
      train.endTime = record.endTime;
      train.createTime = record.createTime;
      train.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train/delete/" + record.id).then((response) => {
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

    const genTrainSeat = (record) => {
      axios.post("/business/admin/train/gen-train-seat/" + record.code).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          notification.success({description: '生成座位成功'});
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    const genTrainStation = (record) => {
      axios.post("/business/admin/train/gen-train-station/" + record.code).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          notification.success({description: '生成车站成功'});
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    const handleOk = () => {
      axios.post("/business/admin/train/save", train).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (train.id === undefined)
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
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/train/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          code: params.value.code,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          trains.value = responseVo.data.list;
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
      document.title = '车次管理';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      TRAIN_TYPE_ARRAY,
      visible,
      train,
      trains,
      pagination,
      columns,
      loading,
      params,
      onAdd,
      onEdit,
      onDelete,
      genTrainSeat,
      genTrainStation,
      handleOk,
      handleQuery,
      handleTableChange,
    };
  },
});
</script>

<style scoped>

</style>
