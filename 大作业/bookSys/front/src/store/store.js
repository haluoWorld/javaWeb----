// store.js (Vuex store)

import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false,
    userRole: null, // 用户角色：'reader' 或 'admin'
  },
  mutations: {
    SET_LOGGED_IN(state, status) {
      state.isLoggedIn = status;
    },
    SET_USER_ROLE(state, role) {
      state.userRole = role;
    },
  },
  actions: {
    login({ commit }, { token, role }) {
      localStorage.setItem('token', token);
      commit('SET_LOGGED_IN', true);
      commit('SET_USER_ROLE', role);
    },
    logout({ commit }) {
      localStorage.removeItem('token');
      commit('SET_LOGGED_IN', false);
      commit('SET_USER_ROLE', null);
    },
  },
  modules: {},
  getters: {
    isAuthenticated(state) {
      return state.isLoggedIn;
    },
  },
});
