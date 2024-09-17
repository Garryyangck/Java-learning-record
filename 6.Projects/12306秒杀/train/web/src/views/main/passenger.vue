<!--suppress JSCheckFunctionSignatures -->
<template>
  <div>
    <p>
      <a-button type="primary" @click="handleQuery()">刷新</a-button> &nbsp;
      <a-button type="primary" @click="onAdd">新增</a-button>
    </p>
    <a-table :dataSource="passengers"
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
        <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{ item.desc }}
          </span>
        </span>
        </template>
      </template>
    </a-table>
    <a-modal v-model:visible="visible" title="乘车人" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :label-col="{span: 4}" :wrapper-col="{span: 14}">
        <a-form-item label="姓名">
          <a-input v-model:value="passenger.name"/>
        </a-form-item>
        <a-form-item label="身份证号">
          <a-input v-model:value="passenger.idCard"/>
        </a-form-item>
        <a-form-item label="乘客类型">
          <a-select v-model:value="passenger.type">
            <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :value="item.code">{{item.desc}}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'passenger-view',
  setup() {
    const visible = ref(false);
    const passengers = ref([]);
    const loading = ref(false);
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;

    const pagination = ref({ // 框架规定的属性名，不能改属性名！
      total: 0, /*所有的总数，list.total*/
      current: 1, /*list.pageNum*/
      pageSize: 10,
    });

    const columns = ref([
      {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '身份证号',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: '乘客类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '操作',
        dataIndex: 'operation',
      }
    ]);

    const passenger = reactive({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });

    /**
     * 新增乘客
     */
    const onAdd = () => {
      passenger.id = undefined; // 新增时必须手动设置 id = null，否则会被后端认为是修改请求
      passenger.name = undefined; // 并且输入的三个值要清空，不然打开新增表单时，还有遗留的数据
      passenger.idCard = undefined;
      passenger.type = undefined;
      visible.value = true;
    };

    /**
     * 编辑乘客
     */
    const onEdit = (record) => {
      console.log('record = ', record);
      passenger.id = record.id;
      passenger.name = record.name;
      passenger.idCard = record.idCard;
      passenger.type = record.type;
      visible.value = true;
    };

    /**
     * 删除乘客
     */
    const onDelete = (record) => {
      axios.delete('member/passenger/delete/' + record.id).then(response => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          notification.success({description: '删除成功'});
          visible.value = false;
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    /**
     * 处理插入请求
     */
    const handleOk = () => {
      axios.post('member/passenger/save', passenger).then(response => {
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

    /**
     * 处理查询请求
     * @param param {pageNum, pageSize}
     */
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
      axios.get('member/passenger/query-list', {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then(response => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          passengers.value = responseVo.data.list; // 直接将整个 PassengerQueryVo List 赋值给 passengers，因此元素含有 id 属性，可以用于 edit
          pagination.value.total = responseVo.data.total;
          pagination.value.current = responseVo.data.pageNum; // 设置当前的页码，如果不设置的话，就只会设置第二页的内容，但是页码依然是第一页
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

    /**
     * 表格发生改变的回调函数，点击页码也算改变
     * @param pagination
     */
    const handleTableChange = (pagination) => {
      // handleTableChange 自带一个 pagination 参数，含有 total，current，pageSize 三个属性
      handleQuery({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    /**
     * 页面初始化的触发函数
     */
    onMounted(() => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      visible,
      loading,
      passenger,
      passengers,
      pagination,
      columns,
      PASSENGER_TYPE_ARRAY,
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