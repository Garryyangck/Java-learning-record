<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <#if !readOnly><a-button type="primary" @click="onAdd">新增</a-button></#if>
    </a-space>
  </p>
  <a-table :dataSource="${domain}s"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <#if !readOnly>
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
        </#if>
      </template>
      <#list fieldList as field>
        <#if field.enums>
      <template v-else-if="column.dataIndex === '${field.nameHump}'">
        <span v-for="item in ${field.enumsConst}_ARRAY" :key="item.code">
          <span v-if="item.code === record.${field.nameHump}">
            {{item.desc}}
          </span>
        </span>
      </template>
        </#if>
      </#list>
    </template>
  </a-table>
  <#if !readOnly>
  <a-modal v-model:visible="visible" title="${tableNameCn}" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="${domain}" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <#list fieldList as field>
        <#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
      <a-form-item label="${field.nameCn}">
        <#if field.enums>
        <a-select v-model:value="${domain}.${field.nameHump}">
          <a-select-option v-for="item in ${field.enumsConst}_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
        <#elseif field.javaType=='Date'>
          <#if field.type=='time'>
        <a-time-picker v-model:value="${domain}.${field.nameHump}" valueFormat="HH:mm:ss" placeholder="请选择时间" />
          <#elseif field.type=='date'>
        <a-date-picker v-model:value="${domain}.${field.nameHump}" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
          <#else>
        <a-date-picker v-model:value="${domain}.${field.nameHump}" valueFormat="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择日期" />
          </#if>
        <#else>
        <a-input v-model:value="${domain}.${field.nameHump}" />
        </#if>
      </a-form-item>
        </#if>
      </#list>
    </a-form>
  </a-modal>
  </#if>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "${do_main}-view",
  setup() {
    <#list fieldList as field>
    <#if field.enums>
    const ${field.enumsConst}_ARRAY = window.${field.enumsConst}_ARRAY;
    </#if>
    </#list>
    const visible = ref(false);
    const ${domain} = reactive({
      <#list fieldList as field>
      ${field.nameHump}: undefined,
      </#list>
    });
    const ${domain}s = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
    <#list fieldList as field>
      <#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
    {
      title: '${field.nameCn}',
      dataIndex: '${field.nameHump}',
      key: '${field.nameHump}',
    },
      </#if>
    </#list>
    <#if !readOnly>
    {
      title: '操作',
      dataIndex: 'operation'
    }
    </#if>
    ];

    <#if !readOnly>
    const onAdd = () => {
      <#list fieldList as field>
      ${domain}.${field.nameHump} = undefined;
      </#list>
      visible.value = true;
    };

    const onEdit = (record) => {
      <#list fieldList as field>
      ${domain}.${field.nameHump} = record.${field.nameHump};
      </#list>
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/${module}/admin/${do_main}/delete/" + record.id).then((response) => {
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
      axios.post("/${module}/admin/${do_main}/save", ${domain}).then((response) => {
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
    </#if>

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
      axios.get("/${module}/admin/${do_main}/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          ${domain}s.value = responseVo.data.list;
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

    const handleTableChange = (page) => {
      // handleTableChange 自带一个 pagination 参数，含有 total，current，pageSize 三个属性
      handleQuery({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    onMounted(() => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
      return {
        <#list fieldList as field>
        <#if field.enums>
        ${field.enumsConst}_ARRAY,
        </#if>
        </#list>
        visible,
        ${domain},
        ${domain}s,
        pagination,
        columns,
        loading,
        <#if !readOnly>
        onAdd,
        onEdit,
        onDelete,
        handleOk
        </#if>
        handleQuery,
        handleTableChange,
      };
    });
  },
});
</script>

<style scoped>

</style>
