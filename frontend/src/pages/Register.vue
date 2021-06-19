<template>
  <div class="page-header clear-filter" filter-color="white">
    <div
      class="page-header-image"
      style="background-image: url('img/bg7.jpg')"
    ></div>
    <div class="content">
      <div class="container">
        <div class="col-md-5 ml-auto mr-auto">
          <card type="login" plain>
            <div slot="header" class="logo-container">
              <img v-lazy="'img/now-logo.png'" alt="" />
            </div>
            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons users_circle-08"
              placeholder="账户名"
              v-model.trim="username"
            >
            </fg-input>

            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons objects_key-25"
              placeholder="密码"
              type="password"
              v-model.trim="password"
            >
            </fg-input>

            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons ui-1_email-85"
              placeholder="邮箱"
              v-model.trim="email"
            >
            </fg-input>

            <template slot="raw-content">
              <div class="card-footer text-center">
                <button
                  href="#pablo"
                  class="btn btn-primary btn-round btn-lg btn-block"
                  v-on:click="register"
                >
                  注册
                </button>
              </div>
              <div class="pull-left">
                <h6>
                  <a @click="toLogin" href="#pablo" class="link footer-link"
                    >去登录</a
                  >
                </h6>
              </div>
              <div class="pull-right">
                <h6>
                  <feedback />
                </h6>
              </div>
            </template>
          </card>
        </div>
      </div>
    </div>
    <main-footer></main-footer>
  </div>
</template>
<script>
import md5 from "js-md5";
import { Card, Button, FormGroupInput, Feedback } from "@/components";
import MainFooter from "@/layout/MainFooter";
export default {
  name: "login-page",
  bodyClass: "login-page",
  components: {
    Card,
    MainFooter,
    Feedback,
    [Button.name]: Button,
    [FormGroupInput.name]: FormGroupInput,
  },
  data() {
    return {
      username: "",
      password: "",
      email: "",
    };
  },
  methods: {
    register() {
      let reg = new RegExp(
        /(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$/
      );
      if (reg.test(this.password)) {
        const cryptPassword = md5(this.password);
        this.$post("/user/register", {
          username: this.username,
          password: cryptPassword,
          email: this.email,
        }).then(() => {
          this.$router.push("/login");
          this.$message.success("点击注册邮件链接激活吧~");
        });
      } else {
        this.$message.error("密码长度6 ~ 20 ，需同时包含字母和数字");
      }
    },
    toLogin() {
      this.$router.push("/login");
    },
  },
};
</script>
<style></style>
