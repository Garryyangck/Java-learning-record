<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainStations"
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
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="火车车站" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="trainStation" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="车次编号">
        <train-select-view v-model:value="trainStation.trainCode"/>
      </a-form-item>
      <a-form-item label="站序">
        <a-input v-model:value="trainStation.index"/>
      </a-form-item>
      <a-form-item label="站名">
        <a-select
            v-model:value="trainStation.name"
            show-search
            :filter-option="filterTrainCodeOption">
          <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="trainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="进站时间">
        <a-time-picker v-model:value="trainStation.inTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出站时间">
        <a-time-picker v-model:value="trainStation.outTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="停站时长">
        <a-time-picker v-model:value="trainStation.stopTime" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="里程（公里）">
        <a-input v-model:value="trainStation.km"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import TrainSelectView from "@/components/train-select.vue";

export default defineComponent({
  name: "train-station-view",
  components: {TrainSelectView},
  setup() {
    const visible = ref(false);
    const trainStation = reactive({
      id: undefined,
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
    const trainStations = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = ref([
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

    watch(() => trainStation.name, () => {
      if (trainStation.name !== null && trainStation.name !== undefined) {
        trainStation.namePinyin = pinyin(trainStation.name, {toneType: 'none'}).replaceAll(" ", "");
      } else {
        trainStation.namePinyin = undefined;
      }
    });

    const onAdd = () => {
      trainStation.id = undefined;
      trainStation.trainCode = undefined;
      trainStation.index = undefined;
      trainStation.name = undefined;
      trainStation.namePinyin = undefined;
      trainStation.inTime = undefined;
      trainStation.outTime = undefined;
      trainStation.stopTime = undefined;
      trainStation.km = undefined;
      trainStation.createTime = undefined;
      trainStation.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      trainStation.id = record.id;
      trainStation.trainCode = record.trainCode;
      trainStation.index = record.index;
      trainStation.name = record.name;
      trainStation.namePinyin = record.namePinyin;
      trainStation.inTime = record.inTime;
      trainStation.outTime = record.outTime;
      trainStation.stopTime = record.stopTime;
      trainStation.km = record.km;
      trainStation.createTime = record.createTime;
      trainStation.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-station/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train-station/save", trainStation).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (trainStation.id === undefined)
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
      axios.get("/business/admin/train-station/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          trainStations.value = responseVo.data.list;
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

    //--------------------------------station 下拉框 start--------------------------------
    const stations = ref([]);

    const queryStation = () => {
      axios.get("/business/admin/station/query-all").then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          stations.value = responseVo.data;
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    /**
     * 车次编号下拉框筛选
     * input 和 option 位内置参数
     * option 中的 label 为我们自定义的 :label="item.code + item.start + item.end + item.type"
     *
     * option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0 意为:
     * input 为我们输入要查找的文字，如果 option.label 中有这个字符串，则保留；否则被过滤掉
     */
    const filterTrainCodeOption = (input, option) => {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    }
    //--------------------------------station 下拉框 end--------------------------------

    onMounted(() => {
      document.title = '火车车站';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
      queryStation();
    });

    return {
      visible,
      trainStation,
      trainStations,
      pagination,
      columns,
      loading,
      stations,
      onAdd,
      onEdit,
      onDelete,
      handleOk,
      handleQuery,
      handleTableChange,
      filterTrainCodeOption,
    };
  },
});
</script>

<style scoped>

</style>
