<script>
import {defineComponent, ref} from "vue";
import GarryList from "@/views/main/homework/garry-list.vue";

export default defineComponent({
  name: "garry-homework",
  components: {GarryList},
  setup() {
    const message = ref(undefined);
    const messages = ref([]);
    const count = ref(0);

    const onAdd = (msg) => {
      if (msg === undefined || msg === null || msg === "" || msg === '')
        return
      messages.value.push(msg);
      count.value++;
      message.value = undefined;
    };

    const onDelete = (msg) => {
      messages.value.forEach((_msg, index) => {
        if (msg === _msg) {
          messages.value.splice(index, 1); // 删除找到的元素
        }
      });
      count.value--;
    };

    return {
      message,
      messages,
      count,
      onAdd,
      onDelete,
    };
  },
});
</script>

<template>
  <div>消息总数：{{ count }}</div>
  <div>messages：{{ messages }}</div>
  <div style="margin-top: 10px">
    <a-input v-model:value="message" style="width: 15%"/>&nbsp;
    <a-button @click="onAdd(message)">添加</a-button>
  </div>
  <div style="margin-top: 10px">
    <garry-list :messages="messages" @delete="onDelete"/>
  </div>
</template>

<style scoped>

</style>