<template>
  <div>
    <div class="page-header clear-filter" filter-color="white">
      <!-- <parallax class="page-header-image" style="background-image: url('img/bg5.jpg')"> -->
      <parallax
        class="page-header-image"
        :style="'background-image: url(' + profile.profilePictureUrl + ')'"
      >
      </parallax>
      <div class="container">
        <div class="photo-container">
          <img :src="profile.profilePictureUrl" alt="" />
        </div>
        <h3 class="title">{{ profile.username }}</h3>
        <p class="category">{{ profile.description }}</p>
        <div class="content">
          <div class="social-description" @click="followerVisible = true">
            <h2>{{ followerCount }}</h2>
            <p>关注</p>
          </div>
          <el-dialog
            title="我的关注"
            width="25%"
            :modal="false"
            :visible.sync="followerVisible"
          >
            <ul
              class="list-group list-group-flush"
              style="max-height: 300px; overflow: auto"
            >
              <li
                class="list-group-item"
                v-for="(follow, i) in followers"
                :key="i"
                style="padding: 20px"
              >
                <el-avatar
                  class="pull-left"
                  :size="40"
                  :src="follow.profilePictureUrl"
                  @click.native="toProfile(follow.uid)"
                ></el-avatar>
                <h2
                  @click="toProfile(follow.uid)"
                  class="pull-left"
                  style="margin-left: 16px; margin-bottom: 0"
                >
                  {{ follow.username }}
                </h2>
                <el-button
                  v-if="isMyhome"
                  @click="unFollow(follow.uid, i)"
                  class="pull-right"
                  plain
                  >取关</el-button
                >
              </li>
            </ul>
          </el-dialog>
          <div class="social-description" @click="fansVisible = true">
            <h2>{{ fansCount }}</h2>
            <p>粉丝</p>
          </div>
          <el-dialog
            title="我的粉丝"
            width="25%"
            :modal="false"
            :visible.sync="fansVisible"
          >
            <ul
              class="list-group list-group-flush"
              style="max-height: 300px; overflow: auto"
            >
              <li
                class="list-group-item"
                v-for="fan in fans"
                :key="fan.uid"
                style="padding: 20px"
              >
                <el-avatar
                  class="pull-left"
                  :size="40"
                  :src="fan.profilePictureUrl"
                  @click.native="toProfile(fan.uid)"
                ></el-avatar>
                <h2
                  @click="toProfile(fan.uid)"
                  class="pull-left"
                  style="margin-left: 16px; margin-bottom: 0"
                >
                  {{ fan.username }}
                </h2>
              </li>
            </ul>
          </el-dialog>
        </div>
      </div>
    </div>
    <div class="section">
      <div class="container-fluid">
        <div class="button-container">
          <button
            v-if="isFollowed == false && isMyhome == false"
            @click="follow"
            class="btn btn-round btn-lg"
            style="background-color: #fafafa"
          >
            <div style="color: black">关注</div>
          </button>
          <el-popconfirm title="确定不再关注嘛？" @confirm="unfollowMain"   v-if="isFollowed">       
            <button
              slot="reference"                    
              class="btn btn-round btn-lg"
              style="background-color: #fafafa"
            >
              <div style="color: black">取关</div>
            </button>
          </el-popconfirm>

          <router-link
            v-if="isMyhome"
            to="/editProfile"
            class="btn btn-default btn-round btn-lg btn-icon"
            title="修改个人信息"
            style="background-color: #fafafa"
          >
            <i class="now-ui-icons el-icon-edit" style="color: black"></i>
          </router-link>

          <drop-down
            class="btn btn-round btn-lg btn-icon"
            style="background-color: #fafafa; overflow: visible"
          >
            <i
              class="now-ui-icons el-icon-more"
              style="color: black"
              slot="title"
            ></i>
            <button class="dropdown-item" @click="report">举报</button>
          </drop-down>
        </div>

        <div class="row" id="myGallery">
          <router-link
            target="_blank"
            v-for="picture in myPictures"
            :key="picture.pid"
            :to="'/picture/' + picture.pid"
          >
            <img :alt="picture.title" :src="picture.smUrl" />
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "justifiedGallery";
import "justifiedGallery/dist/css/justifiedGallery.css";
import $ from "jquery";
import { Dialog, Avatar, Button, MessageBox, Popconfirm } from "element-ui";
import { DropDown } from "@/components";
export default {
  name: "profile",
  bodyClass: "profile-page",
  props: {
    uid: {},
  },
  components: {
    [Dialog.name]: Dialog,
    [Avatar.name]: Avatar,
    [Button.name]: Button,
    [DropDown.name]: DropDown,
    [MessageBox.name]: MessageBox,
    [Popconfirm.name]: Popconfirm,
  },
  data() {
    return {
      profile: {
        uid: "",
        username: "",
        description: "",
        profilePictureUrl: "",
      },
      isFollowed: "",
      followerVisible: false,
      fansVisible: false,
      myPictures: {},
      fans: [],
      followers: [],
    };
  },
  methods: {
    follow() {
      this.$get("/follow/" + this.uid).then(() => {
        this.isFollowed = true;
      });
    },
    unFollow(uid, i) {
      this.$get("/unfollow/" + uid).then(() => {
        this.followers.splice(i, 1);
      });
    },
    unfollowMain() {
      this.$get("/unfollow/" + this.uid).then(() => {
        this.isFollowed = false;
      });
    },
    toProfile(uid) {
      this.$router.push({
        path: "/profile/" + uid,
      });
      this.$nextTick(() => {
        this.followerVisible = false;
        this.fansVisible = false;
        this.getProfile();
      });
    },
    showDetail() {
      this.$router.push({
        path: "/picture/" + this.picture.pid,
      });
    },
    getProfile() {
      this.$get("/user/profile/" + this.uid).then((response) => {
        this.profile.uid = response.uid;
        this.profile.username = response.username;
        this.profile.description = response.description;
        this.profile.profilePictureUrl = response.profilePictureUrl;
        this.myPictures = response.myPictures;
        this.fans = response.fans;
        this.followers = response.followers;
        this.isFollowed = response.isFollowed;
        this.$nextTick(() => {
          $("#myGallery").justifiedGallery({
            rowHeight: 350,
            margins: 6,
            border: 30,
          });
        });
      });
    },
    report() {
      MessageBox.prompt("", "举报理由", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^[\u0000-\uFFFF\w]{6,}$/,
        inputErrorMessage: "理由应至少6位",
      })
        .then(({ value }) => {
          this.$post("/report", {
            type: "user",
            targetId: this.uid,
            accusedId: this.uid,
            reason: value,
          });
        })
        .catch(() => {});
    },
  },
  created() {
    this.getProfile();
  },
  computed: {
    isMyhome() {
      return this.$store.state.user.uid == this.uid;
    },
    followerCount() {
      return this.followers.length;
    },
    fansCount() {
      return this.fans.length;
    },
  },
};
</script>
<style scoped>
</style>
