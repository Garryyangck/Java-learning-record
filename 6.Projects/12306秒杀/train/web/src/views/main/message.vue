<template>
  <a-list
    item-layout="horizontal"
    :data-source="messages"
    class="messages-list"
  >
    <template #renderItem="{ item }">
      <li @click="showModal(item)">
        <a-list-item>
          <a-list-item-meta
            :title="item.title"
            :description="item.content"
          />
        </a-list-item>
      </li>
    </template>
  </a-list>

  <a-modal
    v-model:visible="isVisible"
    title="系统消息详情"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <p>{{ selectedMessage.content }}</p>
  </a-modal>
</template>

<script setup>
import { ref } from 'vue';

const messages = ref([
  {
    id: '1',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  {
    id: '2',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  {
    id: '3',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  {
    id: '4',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  {
    id: '5',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  {
    id: '6',
    title: '系统更新',
    content: '系统将于今晚进行更新，届时将有短暂不可用，请提前保存工作。',
  },
  // ... 更多消息
]);

const isVisible = ref(false);
const selectedMessage = ref({});

const showModal = (message) => {
  selectedMessage.value = message;
  isVisible.value = true;
};

const handleOk = () => {
  isVisible.value = false;
};

const handleCancel = () => {
  isVisible.value = false;
};
</script>

<style scoped>
.messages-list {
  width: 100%;
  max-width: 600px;
  margin: auto;
}
</style>