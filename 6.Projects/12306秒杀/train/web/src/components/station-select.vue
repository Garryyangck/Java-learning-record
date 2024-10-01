<!--suppress JSCheckFunctionSignatures -->
<template>
  <a-select v-model:value="station"
            show-search
            allowClear
            :filterOption="filterStationOption"
            @change="onChange"
            placeholder="请选择车站"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
      {{ item.name }}
    </a-select-option>
  </a-select>
</template>

<script>

import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import store from "@/store";

export default defineComponent({
  name: "station-select-view",
  props: ["modelValue", "width"],
  emits: ['update:modelValue', 'change'],
  setup(props, {emit}) {
    const station = ref();
    const stations = ref([]);
    const localWidth = ref(props.width);
    if (Tool.isNotEmpty(props.width)) {
      localWidth.value = "100%";
    }

    /**
     * 利用 watch，动态获取父组件的值，如果放在 onMounted 或其它方法里，则只有第一次有效
     * 父组件的 v-model:value，会传到 props.modelValue 中来，被子组件接收到，传给自己的 trainCode
     */
    watch(() => props.modelValue, () => {
      console.log("props.modelValue", props.modelValue);
      station.value = props.modelValue;
    }, {immediate: true});

    /**
     * 将当前组件的值响应给父组件
     */
    const onChange = (value) => {
      console.log('onChange.value', value);
      emit('update:modelValue', value);
      // noinspection JSUnresolvedReference
      let _station = stations.value.filter(item => item.name === value)[0];
      if (Tool.isEmpty(_station)) {
        _station = {};
      }
      emit('change', _station);
    };

    /**
     * 查询所有的车站，用于车次下拉框
     */
    const queryAllStation = () => {
      // WARNING: Session 缓存的缺陷，可能和真实的数据存在差异
      // 出现差异时记得刷新页面，清除 Session 缓存
      let list = store.state.SESSION_ALL_STATION;
      if (Tool.isNotEmpty(list)) {
        console.log("queryAllStation 读取缓存", list);
        stations.value = list;
      } else {
        axios.get("/business/station/query-all").then((response) => {
          let responseVo = response.data;
          if (responseVo.success) {
            stations.value = responseVo.data;
            console.log("queryAllStation 保存缓存", stations);
            store.commit("setSessionAllStation", stations);
          } else {
            notification.error({description: responseVo.message});
          }
        });
      }
    };

    /**
     * 车次下拉框筛选
     */
    const filterStationOption = (input, option) => {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    onMounted(() => {
      queryAllStation();
    });

    return {
      station,
      stations,
      localWidth,
      filterStationOption,
      onChange,
    };
  },
});
</script>
