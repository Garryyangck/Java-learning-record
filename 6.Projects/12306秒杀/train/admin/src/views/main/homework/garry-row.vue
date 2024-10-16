<script>
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: "garry-row",
  props: ["message"],
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
{{ message }}<a-button type="danger" size="small" @click="onDelete(message)" style="margin-left: 15px">删除</a-button>
</template>

<style scoped>

</style>