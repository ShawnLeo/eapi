import {setStore} from '../utils/storage';
import {USER_INFO} from '../utils/const';

const app = {
  state: {
    lang: '',
    user: {},
    projectId: '',
    project: {},
		systemDataModel: {},
		customDataModel: {}
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
    },
    PROJECT (state, project) {
      state.project = project;
      setStore('project', project);
    },
		SYSTEM_DATAMODEL (state, systemDataModel) {
      state.systemDataModel = systemDataModel;
      setStore('systemDataModel', systemDataModel);
    },
		CUSTOM_DATAMODEL (state, customDataModel) {
      state.customDataModel = customDataModel;
      setStore('customDataModel', customDataModel);
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
    },
    project: ({commit}, payload) => {
      commit('PROJECT', payload);
    },
		systemDataModel: ({commit}, payload) => {
      commit('SYSTEM_DATAMODEL', payload);
    },
		customDataModel: ({commit}, payload) => {
      commit('CUSTOM_DATAMODEL', payload);
    }
  }
};

export default app;
