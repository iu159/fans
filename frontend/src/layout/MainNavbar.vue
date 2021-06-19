<template>
  <navbar
    position="fixed"
    type="white"
    :transparent="transparent"
    :color-on-scroll="colorOnScroll"
    menu-classes="ml-auto"
  >
    <template>
      <router-link v-popover:popover1 class="navbar-brand" to="/">
        fans
      </router-link>
      <router-link v-popover:popover1 class="navbar-brand" to="/popular">
        热门
      </router-link>
      <router-link v-popover:popover1 class="navbar-brand" to="/fresh">
        新作
      </router-link>
    </template>
    <template slot="navbar-menu">
      <form slot="after-menu" class="form-inline ml-auto">
        <fg-input
          placeholder="搜图..."
          v-model.trim="keyword"
          @keyup.enter="search"
        ></fg-input>
      </form>
      <li v-if="!isLogin" class="nav-item">
        <router-link class="nav-link" to="/login">
          <i class="now-ui-icons users_circle-08"></i> 登录
        </router-link>
      </li>
      <li v-if="!isLogin" class="nav-item">
        <router-link class="nav-link" to="/register">
          <i class="now-ui-icons users_circle-08"></i> 注册
        </router-link>
      </li>

      <drop-down
        v-if="isLogin"
        tag="li"
        title="我的"
        icon="now-ui-icons users_single-02"
        class="nav-item"
      >
        <button @click="toHome" class="dropdown-item">
          <i class="now-ui-icons users_single-02"></i> 首页
        </button>
        <button @click="myLike" class="dropdown-item">
          <i class="now-ui-icons ui-2_favourite-28"></i> 喜欢
        </button>
        <button @click="logout" class="dropdown-item">
          <i class="now-ui-icons sport_user-run"></i> 退出
        </button>
      </drop-down>

      <li class="nav-item" v-if="isLogin">
        <a class="nav-link">
          <el-popover width="300">
            <ul class="list-group list-group-flush">
              <li
                class="list-group-item"
                v-for="(notification, i) in notifications"
                :key="i"
              >
                <router-link
                  v-if="notification.sender != null"
                  :to="'/profile/' + notification.sender.uid"
                  style="color: #888888"
                  >{{ notification.sender.username }}</router-link
                >
                <span
                  style="color: #c9c0b3; margin-left: 5px; margin-right: 5px"
                  >{{ notification.content }}</span
                >
                <router-link
                  :to="'/picture/' + notification.pictureId"
                  style="color: #888888"
                >
                  {{ notification.title }}
                </router-link>
              </li>
            </ul>
            <el-badge
              :value="notificationSize"
              type="danger"
              class="item"
              slot="reference"
              :hidden="hiddenNotification"
            >
              <div @click="getNotification">
                <i class="now-ui-icons ui-1_bell-53"></i> 通知
              </div>
            </el-badge>
          </el-popover>
        </a>
      </li>
      <li class="nav-item" v-if="isLogin">
        <router-link class="nav-link" to="/upload">
          <i class="now-ui-icons arrows-1_cloud-upload-94"></i> 上传
        </router-link>
      </li>
    </template>
  </navbar>
</template>

<script>
import { Badge, Popover } from "element-ui";
import { Navbar, FormGroupInput, DropDown } from "@/components";
export default {
  name: "main-navbar",
  props: {
    transparent: Boolean,
    colorOnScroll: Number,
  },
  data() {
    return {
      keyword: "",
      notifications: [],
      notificationSize: "",
    };
  },
  methods: {
    search() {
      if (this.keyword.length == 0) {
        this.$message.warning("搜索内容不能为空");
      } else {
        this.$router.push({
          path: "/show",
          query: {
            realPath: "/picture/search",
            params: { keyword: this.keyword },
          },
        });
      }
    },
    getNotificationSize() {
      this.$get("/notificationSize").then((response) => {
        this.notificationSize = response;
      });
    },
    getNotification() {
      this.$get("/notification").then((response) => {
        this.notifications = response;
      });
    },
    logout() {
      this.$get("/user/logout").then((response) => {
        this.$store.commit("login", {});
        this.$router.push({
          path: "/whitepage",
          query: {
            realPath: "/",
          },
        });
      });
    },
    myLike() {
      this.$router.push({
        path: "/show",
        query: {
          realPath: "/like/myLikes"
        },
      });
    },
    toHome() {
      this.$router.push({
        path: "/whitepage",
        query: {
          realPath: "/profile/" + this.$store.state.user.uid,
        },
      });
    },
  },
  computed: {
    isLogin() {
      return (
        JSON.stringify(this.$store.state.user) != '""' &&
        JSON.stringify(this.$store.state.user) != "{}"
      );
    },
    hiddenNotification() {
      return this.notificationSize == 0;
    },
  },
  components: {
    Navbar,
    DropDown,
    [FormGroupInput.name]: FormGroupInput,
    [Badge.name]: Badge,
    [Popover.name]: Popover,
  },
  created() {
    this.$get("/user/login").then((response) => {
      this.$store.commit("login", response);
      if (this.isLogin) {
        this.getNotificationSize();
      }
    });
  },
};
</script>

<style scoped>
</style>
