<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12 picture_wrapper">
          <el-image
            :src="picture.url"
            :preview-src-list="srcList"
            style="margin: 0 auto"
          ></el-image>
          <h1 id="goBack" @click="goBack">×</h1>
        </div>
      </div>
    </div>
    <div class="container-fluid">
      <div class="row justify-content-md-center">
        <div class="col-md-5 picture_info_wrapper">
          <div class="row">
            <img
              src="img/my/unlike.png"
              alt=""
              v-show="picture.liked == false"
              @click="like"
            />
            <img
              src="img/my/like.png"
              alt=""
              v-show="picture.liked"
              @click="like"
            />
            <img src="img/my/share.png" @click="share" alt="" />
            <drop-down>
              <img src="img/my/etc.png" alt="" slot="title" />
              <button class="dropdown-item" @click="report">举报</button>
              <button
                v-if="this.$store.state.user.uid == picture.userId"
                class="dropdown-item"
                @click="deleteVisible=true"
              >
                删除
              </button>
            </drop-down>
          </div>
          <modal
            :show.sync="deleteVisible"
            class="modal-primary"
            :show-close="false"
            header-classes="justify-content-center"
            type="mini"
          >
            <div
              slot="header"
              class="modal-profile d-flex justify-content-center align-items-center"
            >
              <i class="now-ui-icons travel_info"></i>
            </div>
            <p>确定要删除该图片嘛？</p>
            <template slot="footer">
              <n-button
                type="neutral"
                link
                @click.native="deleteVisible = false"
                >取消</n-button
              >
              <n-button type="neutral" link @click="deletePicture"
                >确定</n-button
              >
            </template>
          </modal>
          <div class="row">
            <div class="col-md-2">
              <el-avatar
                @click.native="toProvideProfile"
                :size="80"
                :src="picture.provider.profilePictureUrl"
              ></el-avatar>
            </div>
            <div class="col-md-10">
              <h2 style="margin-bottom: 10px">{{ picture.title }}</h2>
              by {{ picture.provider.username }}
            </div>
          </div>
          <div class="row">创建于 {{ picture.createTime }}</div>
          <div class="row">{{ picture.summary }}</div>
          <div class="row">
            {{ picture.viewCount }} 人浏览了该图片，
            <a
              style="color: #3c90f7; cursor: pointer"
              @click="likesVisible = true"
              >{{ likesCount }}人给出了喜欢></a
            >
          </div>
          <el-dialog
            title="点赞列表"
            width="25%"
            :modal="false"
            :visible.sync="likesVisible"
          >
            <ul class="list-group list-group-flush" style="max-height: 300px;overflow: auto;">
              <li
                class="list-group-item"
                v-for="like in picture.likes"
                :key="like.uid"
                style="padding: 20px"
              >
                <el-avatar
                  class="pull-left"
                  :size="40"
                  :src="like.profilePictureUrl"
                  @click.native="toProfile(like.uid)"
                ></el-avatar>
                <h2
                  @click="toProfile(like.uid)"
                  class="pull-left"
                  style="margin-left: 16px; margin-bottom: 10px"
                >
                  {{ like.username }}
                </h2>
              </li>
            </ul>
          </el-dialog>
          <div class="row">
            <span v-for="(category, i) in categories" :key="i">
              <h4 v-if="category.id == picture.category">
                分类：<em style="cursor: pointer">{{ category.name }}</em>
              </h4>
            </span>
          </div>
          <div class="row">
            <el-tag
              style="margin-right: 10px; color: black"
              v-for="item in tags"
              :key="item.label"
              type="info"
              effect="plain"
            >
              {{ item }}
            </el-tag>
          </div>
        </div>
        <div class="col-md-4 picture_info_wrapper">
          <comment :pid="pid"></comment>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueClipboard from "vue-clipboard2";
import { Image, Avatar, Tag, Dialog, MessageBox } from "element-ui";
import Comment from "../components/Comment";
import { DropDown, Modal, Button } from "@/components";
import axios from "axios";
Vue.use(VueClipboard);
export default {
  name: "picture-detail",
  components: {
    DropDown,
    Comment,
    [Image.name]: Image,
    [Avatar.name]: Avatar,
    [Tag.name]: Tag,
    [Dialog.name]: Dialog,
    [MessageBox.name]: MessageBox,
    [Modal.name]: Modal,
    [Button.name]: Button,
  },
  data() {
    return {
      deleteVisible: false,
      likesVisible: false,
      categories: [],
      srcList: [],
      picture: {
        tags: "",
        provider: {},
        likes: [],
      },
    };
  },
  methods: {
    report() {
      MessageBox.prompt("", "举报理由", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^[\u0000-\uFFFF\w]{6,}$/,
        inputErrorMessage: "理由应至少6位",
      })
        .then(({ value }) => {
          this.$post("/report", {
            type: "picture",
            targetId: this.pid,
            accusedId: this.picture.userId,
            reason: value,
          });
        })
        .catch(() => {});
    },
    toProvideProfile() {
      this.$router.push("/profile/" + this.picture.userId);
    },
    toProfile(uid) {
      this.$router.push({
        path: "/profile/" + uid,
      });
    },
    like() {
      this.picture.liked = this.picture.liked == true ? false : true;
      if (this.picture.liked) {
        this.$get("/like", { pid: this.pid });
      } else {
        this.$get("/like/unLike/" + this.pid);
      }
    },
    goBack() {
      window.close();
    },
    deletePicture() {
      this.deleteVisible = false;
      this.$remove("/picture/delete/" + this.pid).then(() => {
        this.goBack();
      });
    },
    share() {
      // window.location.href;
      this.$copyText(window.location.href).then(
        (res) => {
          this.$message.success(
            "链接已复制到剪贴板啦，分享给你想分享的人吧~~~"
          );
        },
        (err) => {
          this.$message.error("复制失败");
        }
      );
    },
  },
  computed: {
    tags() {
      return this.picture.tags.split("-");
    },
    likesCount() {
      return this.picture.likes.length;
    },
  },
  created() {
    this.$get("/picture/getInfo/" + this.pid).then((response) => {
      this.picture = response;
      this.srcList = [response.url];
    });
    this.$get("/category").then((response) => {
      this.categories = response.data;
    });
  },
  props: {
    pid: {},
  },
};
</script>

<style scoped>
img {
  margin-right: 30px;
}
.row {
  margin-bottom: 10px;
}
.picture_info_wrapper {
  margin: 10px;
  padding: 30px;
  background-color: #ffffff;
}
.content {
  background-color: #f7f8fa;
}
#goBack {
  color: aliceblue;
  position: absolute;
  top: 20px;
  right: 50px;
  cursor: pointer;
}
.picture_wrapper {
  background-color: #222222;
  padding: 10px;
  text-align: center;
  margin-top: 30px;
}
.page-header {
  margin-top: -200px;
}
</style>
