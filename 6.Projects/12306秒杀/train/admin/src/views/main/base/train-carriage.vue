<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <train-select-view v-model:value="params.trainCode" style="width: 300px"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainCarriages"
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
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="火车车厢" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="trainCarriage" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="车次编号">
        <train-select-view v-model:value="trainCarriage.trainCode"/>
      </a-form-item>
      <a-form-item label="厢号">
        <a-input v-model:value="trainCarriage.index" />
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="trainCarriage.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位数">
        <a-input v-model:value="trainCarriage.seatCount" disabled />
      </a-form-item>
      <a-form-item label="排数">
        <a-input v-model:value="trainCarriage.rowCount" />
      </a-form-item>
      <a-form-item label="列数">
        <a-input v-model:value="trainCarriage.colCount" disabled />
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
  name: "train-carriage-view",
  components: {TrainSelectView},
  setup() {
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const visible = ref(false);
    const trainCarriage = reactive({
      id: undefined,
      trainCode: undefined,
      index: undefined,
      seatType: undefined,
      seatCount: undefined,
      rowCount: undefined,
      colCount: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trainCarriages = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null,
    });
    const columns = ref([
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: '厢号',
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

    watch(() => params.value.trainCode, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    watch(() => trainCarriage.seatType, () => {
      if (Tool.isNotEmpty(trainCarriage.seatType)) {
        const list = SEAT_COL_ARRAY.filter(seat => seat.type === trainCarriage.seatType);
        trainCarriage.colCount = list.length;
      }
    });

    watch(() => trainCarriage.rowCount, () => {
      if (Tool.isNotEmpty(trainCarriage.rowCount)
          && Tool.isNotEmpty(trainCarriage.colCount)) {
        trainCarriage.seatCount = trainCarriage.rowCount * trainCarriage.colCount;
      } else {
        trainCarriage.seatCount = 0;
      }
    });

    const onAdd = () => {
      trainCarriage.id = undefined;
      trainCarriage.trainCode = undefined;
      trainCarriage.index = undefined;
      trainCarriage.seatType = undefined;
      trainCarriage.seatCount = undefined;
      trainCarriage.rowCount = undefined;
      trainCarriage.colCount = undefined;
      trainCarriage.createTime = undefined;
      trainCarriage.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      trainCarriage.id = record.id;
      trainCarriage.trainCode = record.trainCode;
      trainCarriage.index = record.index;
      trainCarriage.seatType = record.seatType;
      trainCarriage.seatCount = record.seatCount;
      trainCarriage.rowCount = record.rowCount;
      trainCarriage.colCount = record.colCount;
      trainCarriage.createTime = record.createTime;
      trainCarriage.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-carriage/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train-carriage/save", trainCarriage).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (trainCarriage.id === undefined)
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
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/train-carriage/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          trainCode: params.value.trainCode,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          trainCarriages.value = responseVo.data.list;
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
      document.title = '火车车厢';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      SEAT_TYPE_ARRAY,
      visible,
      trainCarriage,
      trainCarriages,
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
