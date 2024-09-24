<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <train-select-view v-model:value="params.trainCode" style="width: 300px"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainSeats"
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
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col">
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
  <a-modal v-model:visible="visible" title="座位" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="trainSeat" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="车次编号">
        <train-select-view v-model:value="trainSeat.trainCode"/>
      </a-form-item>
      <a-form-item label="厢序">
        <a-input v-model:value="trainSeat.carriageIndex" />
      </a-form-item>
      <a-form-item label="排号">
        <a-input v-model:value="trainSeat.row" />
      </a-form-item>
      <a-form-item label="列号">
        <a-select v-model:value="trainSeat.col">
          <a-select-option v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="trainSeat.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="同车厢座序">
        <a-input v-model:value="trainSeat.carriageSeatIndex" />
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
  name: "train-seat-view",
  components: {TrainSelectView},
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    const trainSeat = reactive({
      id: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      row: undefined,
      col: undefined,
      seatType: undefined,
      carriageSeatIndex: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trainSeats = ref([]);
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
      title: '厢序',
      dataIndex: 'carriageIndex',
      key: 'carriageIndex',
    },
    {
      title: '排号',
      dataIndex: 'row',
      key: 'row',
    },
    {
      title: '列号',
      dataIndex: 'col',
      key: 'col',
    },
    {
      title: '座位类型',
      dataIndex: 'seatType',
      key: 'seatType',
    },
    {
      title: '同车厢座序',
      dataIndex: 'carriageSeatIndex',
      key: 'carriageSeatIndex',
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

    const onAdd = () => {
      trainSeat.id = undefined;
      trainSeat.trainCode = undefined;
      trainSeat.carriageIndex = undefined;
      trainSeat.row = undefined;
      trainSeat.col = undefined;
      trainSeat.seatType = undefined;
      trainSeat.carriageSeatIndex = undefined;
      trainSeat.createTime = undefined;
      trainSeat.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      trainSeat.id = record.id;
      trainSeat.trainCode = record.trainCode;
      trainSeat.carriageIndex = record.carriageIndex;
      trainSeat.row = record.row;
      trainSeat.col = record.col;
      trainSeat.seatType = record.seatType;
      trainSeat.carriageSeatIndex = record.carriageSeatIndex;
      trainSeat.createTime = record.createTime;
      trainSeat.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-seat/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train-seat/save", trainSeat).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (trainSeat.id === undefined)
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
      axios.get("/business/admin/train-seat/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          trainCode: params.value.trainCode,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          trainSeats.value = responseVo.data.list;
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
      document.title = '座位';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      SEAT_COL_ARRAY,
      SEAT_TYPE_ARRAY,
      visible,
      trainSeat,
      trainSeats,
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
