import {setStore} from '../utils/storage';
import {USER_INFO} from '../utils/const';

const app = {
  state: {
    lang: '',
    user: {},
    projectId: ''
  },
  mutations: {
    SWITCH_LANG (state, lang) {
      state.lang = lang;
    },
    USER_INIT (state, user) {
      state.user = user;
			setStore(USER_INFO, user);
    },
    PROJECT_ID (state, projectId) {
      state.projectId = projectId;
      setStore('projectId', projectId);
    }
  },
  actions: {
    switchLang: ({commit}, payload) => {
      commit('SWITCH_LANG', payload);
    },
    userInit: ({commit}, payload) => {
      commit('USER_INIT', payload);
    },
    projectId: ({commit}, payload) => {
      commit('PROJECT_ID', payload);
    }
  }
};

export default app;
