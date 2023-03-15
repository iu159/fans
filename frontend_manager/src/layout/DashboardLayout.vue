<template>
  <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
    <side-bar
      :background-color="sidebarBackground"
      short-title="Argon"
      title="Argon"
    >
      <template slot="links">
        <sidebar-item
          :link="{
            name: '用户管理',
            icon: 'ni ni-single-02 text-yellow',
            path: '/userMgt',
          }"
        /> 
        <sidebar-item
          :link="{
            name: '图片管理',
            icon: 'ni ni-image text-blue',
            path: '/pictureMgt',
          }"
        />
        <sidebar-item
          :link="{
            name: '评论管理',
            icon: 'ni ni-chat-round text-blue',
            path: '/commentMgt',
          }"
        />
        <sidebar-item
          :link="{
            name: '举报管理',
            icon: 'ni ni-air-baloon text-red',
            path: '/reportMgt',
          }"
        />
        <sidebar-item
          :link="{
            name: '日志查看',
            icon: 'ni ni-align-left-2 text-blue',
            path: '/log',
          }"
        />
        <sidebar-item
          :link="{
            name: '反馈查看',
            icon: 'ni ni-ungroup text-blue',
            path: '/feedback',
          }"
        />
      </template>
    </side-bar>
    <div class="main-content" :data="sidebarBackground">
      <dashboard-navbar></dashboard-navbar>

      <div @click="toggleSidebar">
        <fade-transition :duration="200" origin="center top" mode="out-in">
          <!-- your content here -->
          <router-view></router-view>
        </fade-transition>
        <content-footer v-if="!$route.meta.hideFooter"></content-footer>
      </div>
    </div>
  </div>
</template>
<script>
import DashboardNavbar from "./DashboardNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import { FadeTransition } from "vue2-transitions";

export default {
  components: {
    DashboardNavbar,
    ContentFooter,
    FadeTransition,
  },
  data() {
    return {
      sidebarBackground: "vue", //vue|blue|orange|green|red|primary
    };
  },
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
  },
};
</script>
<style lang="scss">
</style>
