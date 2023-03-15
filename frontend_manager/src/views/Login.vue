<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-body px-lg-5 py-lg-5">
          <div class="text-center text-muted mb-4">
            <small>登录</small>
          </div>
          <form role="form">
            <base-input
              class="input-group-alternative mb-3"
              placeholder="账户名..."
              addon-left-icon="ni ni-single-02"
              v-model="username"
            >
            </base-input>

            <base-input
              class="input-group-alternative"
              placeholder="密码..."
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="password"
              @keyup.enter="login"
            >
            </base-input>
            <div class="text-center">
              <base-button type="primary" class="my-4" @click="login"
                >登录</base-button
              >
            </div>
          </form>
        </div>
      </div>     
    </div>
  </div>
</template>
<script>
import md5 from "js-md5";
export default {
  name: "login",
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    login() {
      let reg = new RegExp(
        /(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$/
      );
      if (reg.test(this.password)) {
        const cryptPassword = md5(this.password);
        this.$post("/admin/user/login", {
          username: this.username,
          password: cryptPassword,
        }).then((response) => {
          this.$store.commit("login", response.data);
          this.$router.push("/userMgt");
        });
      } else {
        this.$notify({
          type: "danger",
          title: "密码长度6 ~ 20 ，需同时包含字母和数字",
        });
      }
    },
  },
};
</script>
<style>
</style>
