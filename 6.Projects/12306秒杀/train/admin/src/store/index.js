import { createStore } from 'vuex'

const SESSION_ALL_TRAIN = 'SESSION_ALL_TRAIN';
const SESSION_ALL_STATION = 'SESSION_ALL_STATION';

export default createStore({
  state: { // 定义全局变量
    SESSION_ALL_TRAIN: window.SessionStorage.get(SESSION_ALL_TRAIN) || {},
    SESSION_ALL_STATION: window.SessionStorage.get(SESSION_ALL_STATION) || {}
  },
  getters: { // 定义全局变量的get方法

  },
  mutations: { // 定义全局变量的set方法
    setSessionAllTrain (state, trains) {
      state.SESSION_ALL_TRAIN = trains;
      window.SessionStorage.set(SESSION_ALL_TRAIN, trains);
    },
    setSessionAllStation (state, stations) {
      state.SESSION_ALL_STATION = stations;
      window.SessionStorage.set(SESSION_ALL_STATION, stations);
    },
  },
  actions: { // 定义异步方法

  },
  modules: { // 定义模块，一个模块里可以有 state, getters, mutations, actions 等参数

  }
})
