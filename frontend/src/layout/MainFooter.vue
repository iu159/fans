<template>
  <footer
    class="footer"
    :class="{ [`footer-${type}`]: type }"
    :data-background-color="backgroundColor"
  >
    <div class="container">
      <nav>
        <ul>
          <li>
            <a href="https://github.com/iu159"> github </a>
          </li>
          <li>
            <a @click="contactUs" href="javascript:void(0);"> 联系我们 </a>
          </li>
        </ul>
      </nav>
      <div class="copyright">
        <feedback />
      </div>
    </div>
  </footer>
</template>
<script>
import { Feedback } from "@/components";
export default {
  props: {
    backgroundColor: String,
    type: String,
  },
  data() {
    return {
      year: new Date().getFullYear(),
    };
  },
  components: {
    Feedback,
  },
  methods: {
    contactUs() {
      this.$message({
        showClose: true,
        message: "发邮件至< wuyi_edu@163.com >",
        duration: 10000,
        type: "success",
      });
    },
  },
};
</script>
<style></style>
