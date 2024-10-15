<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
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
import {defineComponent, onMounted, reactive, ref} from 'vue';
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
      handleQuery,
      handleTableChange,
    };
  },
});
</script>

<style scoped>

</style>
