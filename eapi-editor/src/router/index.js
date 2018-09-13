import Vue from 'vue';
import Router from 'vue-router';
import layout from '../components/layout/layout.vue';

Vue.use(Router);

const router = new Router({
  routes: [{
    path: '/',
    redirect: '/project/index',
    component: layout,
    children: [{
      path: '/project',
      name: 'project',
      component: () => import('../pages/index.vue'),
      children: [{
        path: '/project/list',
        name: 'interface',
        component: () => import('../pages/project/list.vue')
      }, {
        path: '/project/index',
        name: 'projectIndex',
        component: () => import('../pages/project/list.vue')
      }, {
        path: '/project/project',
        component: () => import('../pages/project/project.vue'),
        children: [{
          path: '/project/interface',
          meta: {
            subMenuActive: '/project/interface'
          },
          name: 'interface',
          component: () => import('../pages/project/interface/interface.vue')
        }, {
          path: '/project/interface/edit',
          meta: {
            subMenuActive: '/project/interface'
          },
          name: 'interface',
          component: () => import('../pages/project/interface/edit.vue')
        }, {
          path: '/project/datamodel',
          name: 'datamodel',
          meta: {
            subMenuActive: '/project/datamodel'
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
            subMenuActive: '/project/datamodel'
          },
          component: () => import('../pages/project/datamodel/edit.vue')
        }, {
          path: '/project/tags',
          name: 'tags',
          meta: {
            subMenuActive: '/project/tags'
          },
          component: () => import('../pages/project/tags/tags.vue')
        }, {
          path: '/project/regular',
          name: 'regular',
          meta: {
            subMenuActive: '/project/regular'
          },
          component: () => import('../pages/project/regular/regular.vue')
        }, {
          path: '/project/settings',
          name: 'settings',
          meta: {
            subMenuActive: '/project/settings'
          },
          component: () => import('../pages/project/settings/settings.vue')
        }]
      }]
    }]
  }, {
    path: '/swagger',
    component: () => import('../pages/swagger/ui.vue')
  }]
});
// router.beforeEach((to, from, next) => {
// });

router.afterEach(() => {
  window.scrollTo(0, 0);
});

export default router;
