<template>
  <footer class="footer px-4">
    <div class="row align-items-center justify-content-lg-between">
      <div class="col-lg-6">
        <div class="copyright text-center text-lg-left text-muted">
          © {{ year }}
          <a
            href="https://github.com/iu159"
            class="font-weight-bold ml-1"
            target="_blank"
            >FANS</a
          >
        </div>
      </div>
      <div class="col-lg-6">
        <ul
          class="nav nav-footer justify-content-center justify-content-lg-end"
        >
          <li class="nav-item">
            <a @click="contactUs" class="nav-link" href="javascript:void(0);">
              联系我们
            </a>
          </li>
        </ul>
      </div>
    </div>
  </footer>
</template>
<script>
export default {
  data() {
    return {
      year: new Date().getFullYear(),
    };
  },
  methods: {
    contactUs() {
      this.$notify("发邮件至< wuyi_edu@163.com >");
    },
  },
};
</script>
<style></style>
