<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
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
        <a-select
            v-model:value="trainSeat.trainCode"
            show-search
            :filter-option="filterTrainCodeOption">
          <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :label="item.code + item.start + item.end + item.type">
            {{ item.code }} | {{ item.start }} ~ {{ item.end }} | {{ item.type }}
          </a-select-option>
        </a-select>
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
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "train-seat-view",
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
        byRefresh = true;
      }
      loading.value = true;
      axios.get("/business/admin/train-seat/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
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

    //--------------------------------车次编号下拉框 start--------------------------------
    const trains = ref([]);

    /**
     * 获取车次编号下拉框筛选所需参数
     */
    const queryTrainCode = () => {
      axios.get("/business/admin/train/query-all").then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          trains.value = responseVo.data;
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
    //--------------------------------车次编号下拉框 end--------------------------------

    onMounted(() => {
      document.title = '座位';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
      queryTrainCode();
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
      trains,
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
