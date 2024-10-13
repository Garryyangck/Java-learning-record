import { createStore } from 'vuex'

const MEMBER = 'MEMBER';

export default createStore({
  state: { // 定义全局变量
    member: window.SessionStorage.get(MEMBER) || {},
    unreadNum: 0
  },
  getters: { // 定义全局变量的get方法

  },
  mutations: { // 定义全局变量的set方法
    setMember (state, member) {
      state.member = member;
      window.SessionStorage.set(MEMBER, member);
    },
  },
  actions: { // 定义异步方法

  },
  modules: { // 定义模块，一个模块里可以有 state, getters, mutations, actions 等参数

  }
})
