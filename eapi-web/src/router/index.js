import Vue from 'vue';
import {getStore} from '../utils/storage';
import {USER_INFO} from '../utils/const';
import Router from 'vue-router';
import layout from '../components/layout/layout.vue';

Vue.use(Router);

const router = new Router({
  routes: [{
    path: '/',
    redirect: '/project/list',
    component: layout,
    children: [{
      path: '/project',
      name: 'project',
      component: () => import('../pages/index.vue'),
      children: [{
        // 	path: '/project/index',
        // 	name: 'projectIndex',
        // 	meta: {
        // 		menuActive: '/project/index'
        // 	},
        // 	component: () => import('../pages/project/index.vue')
        // }, {
        path: '/project/list',
        name: 'projectList',
        meta: {
          menuActive: '/project/list'
        },
        component: () => import('../pages/project/list.vue')
      }, {
        path: '/project/search',
        name: 'search',
        meta: {
          menuActive: '/project/search',
          subMenuActive: '/project/search',
          auth: true
        },
        component: () => import('../pages/project/search.vue')
      }, {
        path: '/project/project',
        component: () => import('../pages/project/project.vue'),
        children: [{
          path: '/project/interface',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/interface',
            auth: true
          },
          name: 'interface',
          component: () => import('../pages/project/interface/interface.vue')
        }, {
          path: '/project/interface/edit',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/interface',
            auth: true
          },
          name: 'interfaceEdit',
          component: () => import('../pages/project/interface/edit.vue')
        }, {
          path: '/project/datamodel',
          name: 'datamodel',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/datamodel',
            auth: true
          },
          component: () => import('../pages/project/datamodel/datamodel.vue')
        }, {
          //   path: '/project/datamodel/add',
          //   meta: {
          //     menu: '/project/list'
          //   },
          //   name: 'interface',
          //   component: () => import('../pages/project/datamodel/add.vue')
          // }, {
          path: '/project/datamodel/edit',
          name: 'datamodelEdit',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/datamodel',
            auth: true
          },
          component: () => import('../pages/project/datamodel/edit.vue')
        }, {
          path: '/project/tags',
          name: 'tags',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/tags',
            auth: true
          },
          component: () => import('../pages/project/tags/tags.vue')
        }, {
          path: '/project/regular',
          name: 'regular',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/regular',
            auth: true
          },
          component: () => import('../pages/project/regular/regular.vue')
        }, {
          path: '/project/settings',
          name: 'settings',
          meta: {
            menuActive: '/project/list',
            subMenuActive: '/project/settings',
            auth: true
          },
          component: () => import('../pages/project/settings/settings.vue')
        }]
      }]
    }, {
      path: '/user/login',
      name: 'login',
      component: () => import('../pages/user/login.vue')
    }, {
      path: '/user/register',
      name: 'register',
      component: () => import('../pages/user/register.vue')
    }, {
      path: '/user/active',
      name: 'active',
      meta: {
        menuActive: '/project/index' // 一级菜单激活 路由名称
        // subMenuActive: '/accountCenter/account' // 二级级菜单激活 路由名称
      },
      component: () => import('../pages/user/active.vue')
    }, {
      path: '/user/active/send',
      name: 'activeSend',
      meta: {
        // menuActive: '/otc/floor' // 一级菜单激活 路由名称
        // subMenuActive: '/accountCenter/account' // 二级级菜单激活 路由名称
      },
      component: () => import('../pages/user/send.vue')
    }, {
      path: '/user/forget',
      name: 'forget',
      meta: {
        // menuActive: '/otc/floor', // 一级菜单激活 路由名称
        // subMenuActive: '/accountCenter/account' // 二级级菜单激活 路由名称
      },
      component: () => import('../pages/user/forget.vue')
    }, {
      path: '/user/reset',
      name: 'reset',
      meta: {
        // menuActive: '/otc/floor', // 一级菜单激活 路由名称
        // subMenuActive: '/accountCenter/account' // 二级级菜单激活 路由名称
      },
      component: () => import('../pages/user/reset.vue')
    }, {
      path: '/user/reset/email',
      name: 'resetEmail',
      meta: {
        // menuActive: '/otc/floor', // 一级菜单激活 路由名称
        // subMenuActive: '/accountCenter/account' // 二级级菜单激活 路由名称
      },
      component: () => import('../pages/user/resetSend.vue')
    }, {
      path: '/code/generator',
      name: 'generator',
      redirect: '/code/generator/settings',
      component: () => import('../pages/generator/index.vue'),
      children: [{
        path: '/code/generator/settings',
        name: 'codeGeneratorSetting',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/settings'
        },
        component: () => import('../pages/generator/settings/settings.vue')
      }, {
        path: '/code/generator/mybatis',
        name: 'codeGeneratorMybatis',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/mybatis'
        },
        component: () => import('../pages/generator/mybatis/mybatis.vue')
      }, {
        path: '/code/generator/mybatis/table',
        name: 'codeGeneratorMybatisTable',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/mybatis'
        },
        component: () => import('../pages/generator/mybatis/table.vue')
      }, {
        path: '/code/generator/swagger',
        name: 'codeGeneratorSwagger',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/swagger'
        },
        component: () => import('../pages/generator/swagger/swagger.vue')
      }, {
        path: '/code/generator/swagger/interface',
        name: 'codeGeneratorSwaggerInterface',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/swagger'
        },
        component: () => import('../pages/generator/swagger/interface.vue')
      }, {
        path: '/code/generator/vue',
        name: 'codeGeneratorVue',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/vue'
        },
        component: () => import('../pages/generator/swagger/vue.vue')
      }, {
        path: '/code/generator/vuetest',
        name: 'codeGeneratorVueTest',
        meta: {
          menuActive: '/code/generator',
          subMenuActive: '/code/generator/vuetest'
        },
        component: () => import('../pages/generator/swagger/vueTest.vue')
      }]
    }, {
      path: '/template/list',
      name: 'template-list',
      meta: {
        menuActive: '/template/list'
        // subMenuActive: '/code/generator/template'
      },
      component: () => import('../pages/template/list.vue')
    }, {
      path: '/template/edit',
      name: 'template-edit',
      meta: {
        menuActive: '/template/list'
        // subMenuActive: '/code/generator/template'
      },
      component: () => import('../pages/template/edit.vue')
    }]
  }, {
    path: '/swagger',
    name: 'swaggerUI',
    component: () => import('../pages/swagger/ui.vue')
  }]
});

router.beforeEach((to, from, next) => {
  let user = getStore(USER_INFO);
  if (user && to.path === '/user/login') {
    next('/');
    return;
  }
  if (!to.meta.auth) {
    next();
    return;
  }
  if (user) {
    next();
  } else {
    next('/user/login');
  }
});

router.afterEach(() => {
  window.scrollTo(0, 0);
});

export default router;
