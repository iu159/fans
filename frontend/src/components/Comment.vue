<template>
  <div>
    <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
      <el-avatar
        v-if="isLogin"
        class="header-img"
        :size="40"
        :src="myHeader"
      ></el-avatar>
      <div class="reply-info" v-show="isLogin">
        <div
          tabindex="0"
          contenteditable="true"
          id="replyInput"
          spellcheck="false"
          placeholder="输入评论..."
          class="reply-input"
          @focus="showReplyBtn"
          @input="onDivInput($event)"
        ></div>
      </div>
      <div class="reply-btn-box" v-show="btnShow">
        <n-button
          class="reply-btn"
          size="sm"
          @click="sendComment"
          type="info"
          >发表评论</n-button
        >
      </div>
    </div>
    <div
      v-for="(item, i) in comments"
      :key="i"
      class="author-title reply-father"
    >
      <el-avatar
        @click.native="toProfile(item.userId)"
        class="header-img"
        :size="40"
        :src="item.profilePictureUrl"
      ></el-avatar>
      <div class="author-info">
        <span class="author-name" @click="toProfile(item.userId)">{{
          item.username
        }}</span>
        <span class="author-time">{{ item.createTime }}</span>
      </div>
      <div class="icon-btn">
        <span>
          <i
            class="iconfont el-icon-s-comment"
            @click="showReplyInput(i, item.username, item.id)"
          ></i>
          <drop-down>
            <i class="iconfont el-icon-more" slot="title"></i>
            <button class="dropdown-item" @click="report(item.id, item.userId)">
              举报
            </button>
            <button
              v-if="myId == item.userId"
              class="dropdown-item"
              @click="prepareDel(item.id)"
            >
              删除
            </button>
          </drop-down>
        </span>
      </div>
      <div class="talk-box">
        <p>
          <span class="reply">{{ item.content }}</span>
        </p>
      </div>
      <div class="reply-box">
        <div v-for="(reply, j) in item.comments" :key="j" class="author-title">
          <el-avatar
            @click.native="toProfile(reply.userId)"
            class="header-img"
            :size="40"
            :src="reply.profilePictureUrl"
          ></el-avatar>
          <div class="author-info">
            <span class="author-name" @click="toProfile(reply.userId)">{{
              reply.username
            }}</span>
            <span class="author-time">{{ reply.createTime }}</span>
          </div>
          <div class="icon-btn">
            <drop-down>
              <i class="iconfont el-icon-more" slot="title"></i>
              <button
                class="dropdown-item"
                @click="report(reply.id, reply.userId)"
              >
                举报
              </button>

              <button
                v-if="myId == reply.userId"
                class="dropdown-item"
                @click="prepareDel(reply.id)"
              >
                删除
              </button>
            </drop-down>
          </div>
          <div class="talk-box">
            <p>
              <span class="reply">{{ reply.content }}</span>
            </p>
          </div>
          <div class="reply-box"></div>
        </div>
      </div>
      <div v-show="_inputShow(i)" class="my-reply my-comment-reply">
        <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
        <div class="reply-info">
          <div
            tabindex="0"
            contenteditable="true"
            spellcheck="false"
            placeholder="输入评论..."
            @input="onDivInput($event)"
            class="reply-input reply-comment-input"
          ></div>
        </div>
        <div class="reply-btn-box">
          <n-button
            class="reply-btn"
            size="sm"
            @click="sendCommentReply(i, j)"
            type="info"
            >发表评论</n-button
          >
        </div>
      </div>
      <modal
        :show.sync="del.deleteVisible"
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
        <p>确定要删除该评论嘛？</p>
        <template slot="footer">
          <n-button
            type="neutral"
            link
            @click.native="del.deleteVisible = false"
            >取消</n-button
          >
          <n-button type="neutral" link @click="deleteComment">确定</n-button>
        </template>
      </modal>
    </div>
  </div>
</template>

<script>
import { DropDown, Modal, Button } from "@/components";
import { Avatar, MessageBox } from "element-ui";
const clickoutside = {
  // 初始化指令
  bind(el, binding, vnode) {
    function documentHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false;
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e);
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler;
    document.addEventListener("click", documentHandler);
  },
  update() {},
  unbind(el, binding) {
    // 解除事件监听
    document.removeEventListener("click", el.vueClickOutside);
    delete el.vueClickOutside;
  },
};
export default {
  name: "ArticleComment",
  components: {
    DropDown,
    [Button.name]: Button,
    [Avatar.name]: Avatar,
    [MessageBox.name]: MessageBox,
    [Modal.name]: Modal,
  },
  created() {
    this.$get("/user/login").then((response) => {
      this.$store.commit("login", response);
      this.myName = response.username;
      this.myHeader = response.profilePictureUrl;
      this.myId = response.uid;
    });
    this.getComments();
  },
  data() {
    return {
      btnShow: false,
      index: "0",
      replyComment: "",
      myName: "",
      myHeader: "",
      myId: "",
      to: "",
      toId: -1,
      comments: [],
      del: {
        deleteVisible: false,
        id: "",
      },
    };
  },
  directives: { clickoutside },
  methods: {
    report(id, uid) {
      MessageBox.prompt("", "举报理由", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^[\u0000-\uFFFF\w]{6,}$/,
        inputErrorMessage: "理由应至少6位",
      })
        .then(({ value }) => {
          this.$post("/report", {
            type: "comment",
            targetId: id,
            accusedId: uid,
            reason: value,
          });
        })
        .catch(() => {});
    },
    toProfile(uid) {
      this.$router.push("/profile/" + uid);
    },
    getComments() {
      this.$get("/comment", { pid: this.pid }).then((response) => {
        this.comments = response.comments;
      });
    },
    inputFocus() {
      var replyInput = document.getElementById("replyInput");
      replyInput.style.padding = "8px 8px";
      replyInput.style.border = "2px solid blue";
      replyInput.focus();
    },
    showReplyBtn() {
      this.btnShow = true;
    },
    hideReplyBtn() {
      this.btnShow = false;
      replyInput.style.padding = "10px";
      replyInput.style.border = "none";
    },
    showReplyInput(i, name, id) {
      this.comments[this.index].inputShow = false;
      this.index = i;
      this.comments[i].inputShow = true;
      this.to = name;
      this.toId = id;
    },
    _inputShow(i) {
      return this.comments[i].inputShow;
    },
    sendComment() {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: "warning",
          message: "评论不能为空",
        });
      } else {
        let input = document.getElementById("replyInput");
        this.$post("/comment", {
          content: this.replyComment,
          pictureId: this.pid,
        }).then((response) => {
          if (response.status == 200) {
            this.replyComment = "";
            input.innerHTML = "";
            this.getComments();
          }
        });
      }
    },
    sendCommentReply(i, j) {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: "warning",
          message: "评论不能为空",
        });
      } else {
        this.$post("/comment", {
          content: this.replyComment,
          pictureId: this.pid,
          parentId: this.comments[i].id,
        }).then((response) => {
          if (response.status == 200) {
            this.replyComment = "";
            document.getElementsByClassName("reply-comment-input")[
              i
            ].innerHTML = "";
            this.getComments();
          }
        });
      }
    },
    onDivInput: function (e) {
      this.replyComment = e.target.innerText;
    },
    prepareDel(id) {
      this.del.id = id;
      this.del.deleteVisible = true;
    },
    deleteComment() {
      this.$remove("/comment?id=" + this.del.id).then(() => {
        this.getComments();
        this.del.deleteVisible = false;
      });
    },
  },
  props: {
    pid: {},
  },
  computed: {
    isLogin() {
      return (
        JSON.stringify(this.$store.state.user) != '""' &&
        JSON.stringify(this.$store.state.user) != "{}"
      );
    },
  },
};
</script>

<style lang="stylus" scoped>
.my-reply {
  padding: 10px;
  background-color: #fafbfc;

  .header-img {
    display: inline-block;
    vertical-align: top;
  }

  .reply-info {
    display: inline-block;
    margin-left: 5px;
    width: 90%;

    @media screen and (max-width: 1200px) {
      width: 80%;
    }

    .reply-input {
      min-height: 20px;
      line-height: 22px;
      padding: 10px 10px;
      color: #ccc;
      background-color: #fff;
      border-radius: 5px;

      &:empty:before {
        content: attr(placeholder);
      }

      &:focus:before {
        content: none;
      }

      &:focus {
        padding: 8px 8px;
        border: 2px solid blue;
        box-shadow: none;
        outline: none;
      }
    }
  }

  .reply-btn-box {
    height: 25px;
    margin: 10px 0;

    .reply-btn {
      position: relative;
      float: right;
      margin-right: 15px;
    }
  }
}

.my-comment-reply {
  margin-left: 50px;

  .reply-input {
    width: flex;
  }
}

.author-title:not(:last-child) {
  border-bottom: 1px solid rgba(178, 186, 194, 0.3);
}

.author-title {
  padding: 10px;

  .header-img {
    display: inline-block;
    vertical-align: top;
  }

  .author-info {
    display: inline-block;
    margin-left: 5px;
    width: 60%;
    height: 40px;
    line-height: 20px;

    >span {
      display: block;
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .author-name {
      color: #000;
      font-size: 18px;
      font-weight: bold;
    }

    .author-time {
      font-size: 14px;
    }
  }

  .icon-btn {
    width: 30%;
    padding: 0 !important;
    float: right;

    @media screen and (max-width: 1200px) {
      width: 20%;
      padding: 7px;
    }

    >span {
      cursor: pointer;
    }

    .dropdown {
      display: inline-block;
    }

    .iconfont {
      margin: 0 5px;
    }
  }

  .talk-box {
    margin: 0 50px;

    >p {
      margin: 0;
    }

    .reply {
      font-size: 16px;
      color: #000;
    }
  }

  .reply-box {
    margin: 10px 0 0 50px;
    background-color: #efefef;
  }
}
</style>
