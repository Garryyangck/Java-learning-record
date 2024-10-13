<!--suppress JSCheckFunctionSignatures -->
<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="readAll()">全部标为已读</a-button>
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
          <li>
            <a-list-item>
              <a-list-item-meta style="margin-left: 10px" @click="showModal(item)">
                <template #title>
                  {{ type.desc }}
                  <span v-if="MESSAGE_STATUS.UNREAD.code === item.status"
                        style="color: red; font-size: small"
                  >
                    【未读】
                  </span>
                  <span style="color: #8c8c8c; font-size: x-small">
                    {{ item.updateTime }}
                  </span>
                </template>
                <template #description>
                  <div style="font-weight: bold">
                    {{ item.content }}
                  </div>
                </template>
              </a-list-item-meta>
              <div style="float: right; margin-right: 50px;">
                <span style="color: #8c8c8c; font-size: x-small">
                  操作：
                </span>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    @confirm="onDelete(item)"
                    ok-text="确认" cancel-text="取消">
                      <a-button type="danger" size="small">
                        删除
                      </a-button>
                </a-popconfirm>
              </div>
            </a-list-item>
          </li>
        </span>
      </span>
    </template>
  </a-list>
  <a-modal v-model:visible="visible" title="消息详情" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="message" :label-col="{span: 6}" :wrapper-col="{span: 18}">
      <a-form-item>
        <template #label>
          <a-row>
            发出者
          </a-row>
        </template>
        <a-row style="margin: 0">
          <span v-if="MESSAGE_TYPE.SYSTEM_MESSAGE.code === message.type">
            系统
          </span>
          <span v-else>
            {{ message.fromId }}
          </span>
        </a-row>
      </a-form-item>
      <a-form-item>
        <template #label>
          <a-row>
            消息类型
          </a-row>
        </template>
        <a-row style="margin: 0">
          <span v-for="item in MESSAGE_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === message.type">
              {{ item.desc }}
            </span>
          </span>
        </a-row>
      </a-form-item>
      <a-form-item>
        <template #label>
          <a-row>
            消息状态
          </a-row>
        </template>
        <a-row style="margin: 0">
          <span v-for="item in MESSAGE_STATUS_ARRAY" :key="item.code">
            <span v-if="item.code === message.status">
              {{ item.desc }}
            </span>
          </span>
        </a-row>
      </a-form-item>
      <a-form-item>
        <template #label>
          <a-row>
            消息内容
          </a-row>
        </template>
        <a-row style="margin: 0">
          {{ message.content }}
        </a-row>
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
    const MESSAGE_TYPE = window.MESSAGE_TYPE;
    const MESSAGE_STATUS_ARRAY = window.MESSAGE_STATUS_ARRAY;
    const MESSAGE_STATUS = window.MESSAGE_STATUS;
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
    const pagination = ref({ // TODO 分页功能无效
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

    // const onAdd = () => {
    //   message.id = undefined;
    //   message.fromId = undefined;
    //   message.toId = undefined;
    //   message.type = undefined;
    //   message.content = undefined;
    //   message.status = undefined;
    //   message.createTime = undefined;
    //   message.updateTime = undefined;
    //   visible.value = true;
    // };

    const showModal = (record) => {
      message.id = record.id;
      message.fromId = record.fromId;
      message.toId = record.toId;
      message.type = record.type;
      message.content = record.content;
      message.status = record.status;
      message.createTime = record.createTime;
      message.updateTime = record.updateTime;
      visible.value = true;
      read(record.id);
    };

    const read = (id) => {
      axios.post("/business/message/read/" + id).then((response) => {
        let responseVo = response.data;
        if (responseVo.success) {
          handleQuery({
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          });
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    const readAll = () => {
      messages.value.forEach((item) => {
        read(item.id);
      });
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
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
      visible.value = false;
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
      MESSAGE_TYPE,
      MESSAGE_STATUS_ARRAY,
      MESSAGE_STATUS,
      visible,
      message,
      messages,
      pagination,
      loading,
      showModal,
      readAll,
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
