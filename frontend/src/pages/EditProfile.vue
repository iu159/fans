<template>
  <div class="page-header clear-filter" filter-color="white">
    <div
      class="page-header-image"
      style="background-image: url('img/bg1.jpg')"
    ></div>
    <div class="content">
      <div class="container">
        <div class="col-md-5 ml-auto mr-auto">
          <card type="login" plain>
            <ImgCutter
              @cutDown="updateProfilePicture"
              :tool="false"
              rate="1:1"
              name="profilePicture"
            >
              <img
                id="profile_picture"
                slot="open"
                :src="profile.profilePictureUrl"
                title="修改个人信息"
              />
            </ImgCutter>

            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons users_circle-08"
              placeholder="账户名..."
              v-model.trim="profile.username"
            >
            </fg-input>

            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons business_badge"
              placeholder="个人简介..."
              v-model.trim="profile.description"
            >
            </fg-input>

            <fg-input
              class="no-border input-lg"
              addon-left-icon="now-ui-icons tech_mobile"
              placeholder="电话..."
              v-model.trim="profile.telephone"
            >
            </fg-input>

            <template slot="raw-content">
              <div class="card-footer text-center">
                <button
                  href="#pablo"
                  class="btn btn-primary btn-round btn-lg btn-block"
                  @click="updateVisible = true"
                >
                  修改
                </button>
              </div>
            </template>
          </card>
          <modal
            :show.sync="updateVisible"
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
            <p>确定要修改个人信息嘛？</p>
            <template slot="footer">
              <n-button
                type="neutral"
                link
                @click.native="updateVisible = false"
                >取消</n-button
              >
              <n-button type="neutral" link @click="editProfile">确定</n-button>
            </template>
          </modal>
        </div>
      </div>
    </div>
    <main-footer></main-footer>
  </div>
</template>

<script>
import MainFooter from "@/layout/MainFooter";
import { Card, FormGroupInput, Modal, Button } from "@/components";
import ImgCutter from "vue-img-cutter";
import axios from "axios";
export default {
  name: "edit-profile-page",
  bodyClass: "login-page",
  components: {
    Card,
    ImgCutter,
    [FormGroupInput.name]: FormGroupInput,
    [Modal.name]: Modal,
    [Button.name]: Button,
    MainFooter,
  },
  data() {
    return {
      profile: {
        username: "",
        description: "",
        telephone: "",
      },
      updateVisible: false,
    };
  },
  methods: {
    updateProfilePicture(fileName) {
      var formData = new FormData();
      formData.append("profilePicture", fileName.file);
      axios
        .post("http://116.62.65.154:8888/user/profile", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(() => {
          this.getProfile();
        });
    },
    editProfile() {
      if (this.profile.username.trim().length < 2) {
        this.$message.error("用户名长度至少为 2 ");
      } else {
        this.$put("/user/profile", this.profile).then((response) => {
          this.$message.success(response.message);
          this.updateVisible = false;
        });
      }
    },
    getProfile() {
      this.$get("/user/profile/" + this.$store.state.user.uid).then(
        (response) => {
          this.profile = response;
        }
      );
    },
  },
  created() {
    this.getProfile();
  },
};
</script>

<style scoped>
#profile_picture {
  width: 100px;
  height: 100px;
  margin-bottom: 30px;
}
</style>