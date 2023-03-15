import Vue from 'vue'
import Router from 'vue-router'
import DashboardLayout from '@/layout/DashboardLayout'
import AuthLayout from '@/layout/AuthLayout'
import store from './utils/store';
Vue.use(Router)

const router = new Router({
  linkExactActiveClass: 'active',
  routes: [{
      path: '/',
      redirect: 'dashboard',
      component: DashboardLayout,
      children: [    
        {
          path: '/userMgt',
          name: 'userMgt',
          component: () => import('./views/UserMgt.vue')
        },
        {
          path: '/pictureMgt',
          name: 'pictureMgt',
          component: () => import('./views/PictureMgt.vue')
        },
        {
          path: '/reportMgt',
          name: 'ReportMgt',
          component: () => import('./views/ReportMgt.vue')
        },
        {
          path: '/commentMgt',
          name: 'CommentMgt',
          component: () => import('./views/CommentMgt.vue')
        },
        {
          path: '/log',
          name: 'Log',
          component: () => import('./views/Log.vue')
        },
        {
          path: '/feedback',
          name: 'Feedback',
          component: () => import('./views/Feedback.vue')
        }    
      ]
    },
    {
      path: '/',
      redirect: 'login',
      component: AuthLayout,
      children: [{
        path: '/login',
        name: 'login',
        component: () => import( /* webpackChunkName: "demo" */ './views/Login.vue')
      }]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // to要跳转到的路径
  // from从哪个路径来
  // next往下执行的回调
  // 在localStorage中获取token
  if (JSON.stringify(store.state.user) != '""' &&
    JSON.stringify(store.state.user) != "{}") {
    next()
  } else if(to.name == 'login'){
    next()
  } else {
    router.push("/login");
  }
})

export default router