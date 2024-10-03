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
  <a-table :dataSource="dailyTrainStations"
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
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="每日车站" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainStation" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainStation.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view
            v-model:value="dailyTrainStation.trainCode"
            :disabled="dailyTrainStation.id !== undefined"
        />
      </a-form-item>
      <a-form-item label="站序">
        <a-input v-model:value="dailyTrainStation.index"/>
      </a-form-item>
      <a-form-item label="站名">
        <station-select-view v-model:value="dailyTrainStation.name"/>
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="dailyTrainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="进站时间">
        <a-time-picker v-model:value="dailyTrainStation.inTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出站时间">
        <a-time-picker v-model:value="dailyTrainStation.outTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="停站时长">
        <a-time-picker v-model:value="dailyTrainStation.stopTime" valueFormat="HH:mm:ss" placeholder="请选择时间"
                       disabled/>
      </a-form-item>
      <a-form-item label="里程（公里）">
        <a-input v-model:value="dailyTrainStation.km"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelectView from "@/components/train-select.vue";
import {pinyin} from "pinyin-pro";
import StationSelectView from "@/components/station-select.vue";

export default defineComponent({
  name: "daily-train-station-view",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    const dailyTrainStation = reactive({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      index: undefined,
      name: undefined,
      namePinyin: undefined,
      inTime: undefined,
      outTime: undefined,
      stopTime: undefined,
      km: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainStations = ref([]);
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
        title: '站序',
        dataIndex: 'index',
        key: 'index',
      },
      {
        title: '站名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '站名拼音',
        dataIndex: 'namePinyin',
        key: 'namePinyin',
      },
      {
        title: '进站时间',
        dataIndex: 'inTime',
        key: 'inTime',
      },
      {
        title: '出站时间',
        dataIndex: 'outTime',
        key: 'outTime',
      },
      {
        title: '停站时长',
        dataIndex: 'stopTime',
        key: 'stopTime',
      },
      {
        title: '里程（公里）',
        dataIndex: 'km',
        key: 'km',
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ]);

    watch(() => dailyTrainStation.name, () => {
      if (Tool.isNotEmpty(dailyTrainStation.name)) {
        dailyTrainStation.namePinyin = pinyin(dailyTrainStation.name, {toneType: 'none'}).replaceAll(" ", "");
      } else {
        dailyTrainStation.namePinyin = undefined;
      }
    });

    function timeToMilliseconds(timeStr) {
      const [hours, minutes, seconds] = timeStr.split(':').map(Number);
      return hours * 3600000 + minutes * 60000 + seconds * 1000;
    }

    function formatMillisecondsToTime(ms) {
      const hours = Math.floor(ms / 3600000);
      const minutes = Math.floor((ms % 3600000) / 60000);
      const seconds = Math.floor((ms % 60000) / 1000);
      return [
        hours.toString().padStart(2, '0'),
        minutes.toString().padStart(2, '0'),
        seconds.toString().padStart(2, '0')
      ].join(':');
    }

    function handleStopTime() {
      let stopTime = timeToMilliseconds(dailyTrainStation.outTime) - timeToMilliseconds(dailyTrainStation.inTime);
      if (stopTime >= 0)
        dailyTrainStation.stopTime = formatMillisecondsToTime(Math.abs(stopTime));
      else {
        notification.error({description: '出站时间不能小于进站时间'});
        dailyTrainStation.stopTime = undefined;
      }
    }

    watch(() => dailyTrainStation.inTime, () => {
      if (Tool.isNotEmpty(dailyTrainStation.inTime) && Tool.isNotEmpty(dailyTrainStation.outTime)) {
        handleStopTime();
      }
    });

    watch(() => dailyTrainStation.outTime, () => {
      if (Tool.isNotEmpty(dailyTrainStation.inTime) && Tool.isNotEmpty(dailyTrainStation.outTime)) {
        handleStopTime();
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
      dailyTrainStation.id = undefined;
      dailyTrainStation.date = undefined;
      dailyTrainStation.trainCode = undefined;
      dailyTrainStation.index = undefined;
      dailyTrainStation.name = undefined;
      dailyTrainStation.namePinyin = undefined;
      dailyTrainStation.inTime = undefined;
      dailyTrainStation.outTime = undefined;
      dailyTrainStation.stopTime = undefined;
      dailyTrainStation.km = undefined;
      dailyTrainStation.createTime = undefined;
      dailyTrainStation.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrainStation.id = record.id;
      dailyTrainStation.date = record.date;
      dailyTrainStation.trainCode = record.trainCode;
      dailyTrainStation.index = record.index;
      dailyTrainStation.name = record.name;
      dailyTrainStation.namePinyin = record.namePinyin;
      dailyTrainStation.inTime = record.inTime;
      dailyTrainStation.outTime = record.outTime;
      dailyTrainStation.stopTime = record.stopTime;
      dailyTrainStation.km = record.km;
      dailyTrainStation.createTime = record.createTime;
      dailyTrainStation.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train-station/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/daily-train-station/save", dailyTrainStation).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (dailyTrainStation.id === undefined)
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
      axios.get("/business/admin/daily-train-station/query-list", {
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
          dailyTrainStations.value = responseVo.data.list;
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
      document.title = '每日车站';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      visible,
      dailyTrainStation,
      dailyTrainStations,
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
