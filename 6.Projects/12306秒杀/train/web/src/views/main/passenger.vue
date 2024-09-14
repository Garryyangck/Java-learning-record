<template>
  <div>
    <a-button type="primary" @click="showModal">新增</a-button>
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
            <a-select-option value="1">成人</a-select-option>
            <a-select-option value="2">儿童</a-select-option>
            <a-select-option value="3">学生</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import {defineComponent, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  setup() {
    const visible = ref(false);

    const passenger = reactive({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    })

    const showModal = () => {
      visible.value = true;
    };

    const handleOk = e => {
      axios.post('member/passenger/save', passenger).then(response => {
        let responseVo = response.data;
        if (responseVo.success) {
          notification.success({description: '保存成功'});
          visible.value = false;
        } else {
          notification.error({description: responseVo.msg});
        }
      })
    };

    return {
      visible,
      passenger,
      showModal,
      handleOk,
    };
  },
});
</script>

<style scoped>

</style>