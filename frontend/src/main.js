import Vue from 'vue';
import App from './App.vue';
// You can change this import to `import router from './starterRouter'` to quickly start development from a blank layout.
import router from './router';
import store from './store';
import NowUiKit from './plugins/now-ui-kit';
import qs from 'qs';
import { Message } from "element-ui";
import {post,get,patch,put,remove} from './network/request';
import infiniteScroll from 'vue-infinite-scroll';

//定义全局变量
Vue.prototype.$post=post;
Vue.prototype.$get=get;
Vue.prototype.$patch=patch;
Vue.prototype.$put=put;
Vue.prototype.$remove=remove;
Vue.prototype.$qs=qs;
Vue.prototype.$message=Message;
Vue.config.productionTip = false;

Vue.use(NowUiKit);
Vue.use(infiniteScroll);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
