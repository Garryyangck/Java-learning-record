<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-select v-model:value="params.apiMethod" placeholder="请选择接口类型" allowClear style="width: 100% ">
        <a-select-option v-for="item in apiMethod_enum" :key="item.value" :value="item.value">
          {{item.value}}
        </a-select-option>
      </a-select>
      <a-select v-model:value="params.moduleName" placeholder="请选择模块" allowClear style="width: 100%">
        <a-select-option v-for="item in moduleName_enum" :key="item.value" :value="item.value">
          {{item.value}}
        </a-select-option>
      </a-select>
      <a-select v-model:value="params.sortBy" placeholder="请选择排序字段" allowClear style="width: 100%">
        <a-select-option v-for="item in sortBy_enum" :key="item.value" :value="item.value">
          {{item.desc}}
        </a-select-option>
      </a-select>
      <a-switch v-model:checked="params.isAsc" :checked-children="'正序'" :un-checked-children="'倒序'"/>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="apiDetails"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
  </a-table>
</template>

<script>
import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "api-detail-view",
  setup() {
    const apiDetail = ref({});
    const apiDetails = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      apiMethod: null,
      moduleName: null,
      sortBy: null,
      isAsc: null,
    });
    const apiMethod_enum = ref([
      {
        value: "GET",
      },
      {
        value: "POST",
      },
      {
        value: "PUT",
      },
      {
        value: "DELETE",
      },
    ]);
    const moduleName_enum = ref([
      {
        value: "member",
      },
      {
        value: "business",
      },
    ]);
    const sortBy_enum = ref([
      {
        value: "callTimes",
        desc: "调用次数",
      },
      {
        value: "successTimes",
        desc: "成功调用次数",
      },
      {
        value: "successRatio",
        desc: "成功比例",
      },
      {
        value: "maxExecuteMills",
        desc: "最长执行时间",
      },
      {
        value: "minExecuteMills",
        desc: "最短执行时间",
      },
      {
        value: "executeMills",
        desc: "执行总时间",
      },
      {
        value: "successExecuteMills",
        desc: "成功执行总时间",
      },
      {
        value: "avgExecuteMills",
        desc: "平均执行时间",
      },
      {
        value: "avgSuccessExecuteMills",
        desc: "成功平均执行时间",
      },
    ]);
    const columns = ref([
      {
        title: '接口全路径',
        dataIndex: 'fullApiPath',
        key: 'fullApiPath',
      },
      {
        title: '类型',
        dataIndex: 'apiMethod',
        key: 'apiMethod',
      },
      {
        title: '模块',
        dataIndex: 'moduleName',
        key: 'moduleName',
      },
      {
        title: '调用次数',
        dataIndex: 'callTimes',
        key: 'callTimes',
      },
      {
        title: '成功调用次数',
        dataIndex: 'successTimes',
        key: 'successTimes',
      },
      {
        title: '成功比例',
        dataIndex: 'successRatio',
        key: 'successRatio',
      },
      {
        title: '最长执行时间(ms)',
        dataIndex: 'maxExecuteMills',
        key: 'maxExecuteMills',
      },
      {
        title: '最短执行时间(ms)',
        dataIndex: 'minExecuteMills',
        key: 'minExecuteMills',
      },
      {
        title: '执行总时间(ms)',
        dataIndex: 'executeMills',
        key: 'executeMills',
      },
      {
        title: '成功执行总时间(ms)',
        dataIndex: 'successExecuteMills',
        key: 'successExecuteMills',
      },
      {
        title: '平均执行时间(ms)',
        dataIndex: 'avgExecuteMills',
        key: 'avgExecuteMills',
      },
      {
        title: '成功平均执行时间(ms)',
        dataIndex: 'avgSuccessExecuteMills',
        key: 'avgSuccessExecuteMills',
      },
    ]);

    watch(() => params.value.apiMethod, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    watch(() => params.value.moduleName, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    watch(() => params.value.sortBy, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    watch(() => params.value.isAsc, () => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

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
      axios.get("/business/admin/api-detail/query-all-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
          apiMethod: params.value.apiMethod,
          moduleName: params.value.moduleName,
          sortBy: params.value.sortBy,
          isAsc: params.value.isAsc,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          apiDetails.value = responseVo.data.list;
          pagination.value.total = responseVo.data.total;
          pagination.value.current = responseVo.data.pageNum;
          pagination.value.pageSize = responseVo.data.pageSize; // 让修改页面可行，否则即使修改为 50，查出来 50 条，还是只能显示 10 条
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
      document.title = '接口详情';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      apiDetail,
      apiDetails,
      pagination,
      columns,
      loading,
      params,
      apiMethod_enum,
      moduleName_enum,
      sortBy_enum,
      handleQuery,
      handleTableChange,
    };
  },
});
</script>

<style scoped>

</style>
