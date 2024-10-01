<!--suppress JSCheckFunctionSignatures -->
<template>
  <a-select v-model:value="trainCode"
            show-search
            allowClear
            :filterOption="filterTrainCodeOption"
            @change="onChange"
            placeholder="请选择车次"
            :disabled="idDisabled"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code"
                     :label="item.code + item.start + item.end + item.type">
      {{ item.code }} &nbsp; | &nbsp; {{ item.start }} ~ {{ item.end }} &nbsp; | &nbsp; {{ item.type }}
    </a-select-option>
  </a-select>
</template>

<script>

import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import store from "@/store";

export default defineComponent({
  name: "train-select-view",
  props: ["modelValue", "width", "disabled"], // modelValue 是自带的参数，用于传递父组件的 v-model:value
  emits: ['update:modelValue', 'change'], // update:modelValue 是自带的事件，触发条件是 modelValue 变化
  setup(props, {emit}) {
    const trainCode = ref();
    const trains = ref([]);
    const localWidth = ref(props.width);
    const idDisabled = ref(false);
    if (Tool.isNotEmpty(props.width)) {
      localWidth.value = "100%";
    }

    /**
     * 利用 watch，动态获取父组件的值，如果放在 onMounted 或其它方法里，则只有第一次有效
     * 父组件的 v-model:value，会传到 props.modelValue 中来，被子组件接收到，传给自己的 trainCode
     */
    watch(() => props.modelValue, () => {
      console.log("props.modelValue", props.modelValue);
      trainCode.value = props.modelValue;
    }, {immediate: true});

    /**
     * isDisabled 不能在 onMounted 函数中赋值，否则只会赋值一次，
     * 比如我先打开 add，就只会将 isDisabled 赋值为 true，而之后再打开 edit，依然为 true
     * 解决方法就是像 props.modelValue 一样用 watch，这样只要父组件的 disabled 发生变化，这里的 isDisabled 就能马上修改
     */
    watch(() => props.disabled, () => {
      console.log("props.disabled", props.disabled);
      idDisabled.value = props.disabled;
    }, {immediate: true});

    /**
     * 将当前组件的值响应给父组件
     */
    const onChange = (value) => {
      console.log('onChange.value', value);
      emit('update:modelValue', value); // 将子组件选择的值 value，通过事件传给父组件
      // noinspection JSUnresolvedReference
      let train = trains.value.filter(item => item.code === value)[0];
      if (Tool.isEmpty(train)) {
        train = {};
      }
      // 自定义事件 change，可以把父组件选取的 train 传给父组件
      // 父组件的使用方法: @change=function(train)，可以看到，@ 后面跟的，就是我们主动暴露出去的事件，ant-design-vue 的组件中也有很多类似的事件
      // 将 train 的 “高铁” 转回 "G"
      let _train = Tool.copy(train); // 防止浅拷贝，导致修改了 trains 里面的元素 train
      window.TRAIN_TYPE_ARRAY.forEach((trainType) => {
        if (trainType.desc === _train.type) {
          _train.type = trainType.code;
        }
      });
      emit('change', _train);
    };

    /**
     * 查询所有的车次，用于车次下拉框
     */
    const queryAllTrain = () => {
      // WARNING: Session 缓存的缺陷，可能和真实的数据存在差异
      // 出现差异时记得刷新页面，清除 Session 缓存
      let list = store.state.SESSION_ALL_TRAIN;
      if (Tool.isNotEmpty(list)) {
        console.log("queryAllTrain 读取缓存", list);
        trains.value = list;
      } else {
        axios.get("/business/admin/train/query-all").then((response) => {
          let responseVo = response.data;
          if (responseVo.success) {
            trains.value = responseVo.data;
            console.log("queryAllTrain 保存缓存", trains);
            store.commit("setSessionAllTrain", trains);
          } else {
            notification.error({description: responseVo.message});
          }
        });
      }
    };

    /**
     * 车次下拉框筛选
     */
    const filterTrainCodeOption = (input, option) => {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    onMounted(() => {
      queryAllTrain();
    });

    return {
      trainCode,
      trains,
      localWidth,
      idDisabled,
      filterTrainCodeOption,
      onChange,
    };
  },
});
</script>
