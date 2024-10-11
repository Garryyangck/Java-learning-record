<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-list :dataSource="messages"
          :pagination="pagination"
          @change="handleListChange"
          :loading="loading"
  >
    <template #renderItem="{ item }">
      <span v-for="type in MESSAGE_TYPE_ARRAY" :key="type.code">
        <span v-if="type.code === item.type">
          <li @click="onEdit(item)">
            <a-list-item>
              <a-list-item-meta
                :title="type.desc"
                :description="item.content">
              </a-list-item-meta>
            </a-list-item>
          </li>
        </span>
      </span>
    </template>
  </a-list>
  <a-modal v-model:visible="visible" title="消息详情" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="message" :label-col="{span: 4}" :wrapper-col="{span: 18}">
      <a-form-item label="发出者id，系统消息则为0">
        <a-input v-model:value="message.fromId"/>
      </a-form-item>
      <a-form-item label="接收者id">
        <a-input v-model:value="message.toId"/>
      </a-form-item>
      <a-form-item label="消息类型">
        <a-select v-model:value="message.type">
          <a-select-option v-for="item in MESSAGE_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="消息内容">
        <a-input v-model:value="message.content"/>
      </a-form-item>
      <a-form-item label="消息状态">
        <a-select v-model:value="message.status">
          <a-select-option v-for="item in MESSAGE_STATUS_ARRAY" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "message-view",
  setup() {
    const MESSAGE_TYPE_ARRAY = window.MESSAGE_TYPE_ARRAY;
    const MESSAGE_STATUS_ARRAY = window.MESSAGE_STATUS_ARRAY;
    const visible = ref(false);
    const message = reactive({
      id: undefined,
      fromId: undefined,
      toId: undefined,
      type: undefined,
      content: undefined,
      status: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const messages = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    // const columns = ref([
    //   {
    //     title: '发出者id',
    //     dataIndex: 'fromId',
    //     key: 'fromId',
    //   },
    //   {
    //     title: '接收者id',
    //     dataIndex: 'toId',
    //     key: 'toId',
    //   },
    //   {
    //     title: '消息类型',
    //     dataIndex: 'type',
    //     key: 'type',
    //   },
    //   {
    //     title: '消息内容',
    //     dataIndex: 'content',
    //     key: 'content',
    //   },
    //   {
    //     title: '消息状态',
    //     dataIndex: 'status',
    //     key: 'status',
    //   },
    //   {
    //     title: '操作',
    //     dataIndex: 'operation'
    //   }
    // ]);

    const onAdd = () => {
      message.id = undefined;
      message.fromId = undefined;
      message.toId = undefined;
      message.type = undefined;
      message.content = undefined;
      message.status = undefined;
      message.createTime = undefined;
      message.updateTime = undefined;
      visible.value = true;
    };

    const onEdit = (record) => {
      message.id = record.id;
      message.fromId = record.fromId;
      message.toId = record.toId;
      message.type = record.type;
      message.content = record.content;
      message.status = record.status;
      message.createTime = record.createTime;
      message.updateTime = record.updateTime;
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/message/delete/" + record.id).then((response) => {
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
      axios.post("/business/message/save", message).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
          if (message.id === undefined)
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
      axios.get("/business/message/query-list", {
        params: {
          pageNum: param.pageNum,
          pageSize: param.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        let responseVo = response.data;
        if (responseVo.success) {
          messages.value = responseVo.data.list;
          console.log('messages', messages);
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

    const handleListChange = (pagination) => {
      // handleListChange 自带一个 pagination 参数，含有 total，current，pageSize 三个属性
      handleQuery({
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    onMounted(() => {
      document.title = '我的消息';
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      MESSAGE_TYPE_ARRAY,
      MESSAGE_STATUS_ARRAY,
      visible,
      message,
      messages,
      pagination,
      loading,
      onAdd,
      onEdit,
      onDelete,
      handleOk,
      handleQuery,
      handleListChange,
    };
  },
});
</script>

<style scoped>

</style>
