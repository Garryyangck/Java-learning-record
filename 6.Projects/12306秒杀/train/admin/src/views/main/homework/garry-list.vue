<script>
import {defineComponent, ref} from "vue";
import GarryRow from "@/views/main/homework/garry-row.vue";

export default defineComponent({
  name: "garry-list",
  components: {GarryRow},
  props: ["messages"],
  emits: ["delete"],
  setup(props, {emit}) {

    const onDelete = (msg) => {
      emit("delete", msg);
    };

    return {
      onDelete,
    };
  },
});
</script>

<template>
  <a-divider/>
  <div>messages：{{ messages }}</div>
  <div v-for="message in messages" :key="message" class="message-item">
    <garry-row :message="message" @delete="onDelete"/>
  </div>
</template>

<style scoped>
.message-item {
  margin-bottom: 10px;
}
</style>