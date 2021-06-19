<template>
  <div>
    <div class="page-header clear-filter" filter-color="white">
      <parallax
        class="page-header-image"
        style="background-image: url('img/header.jpg')"
      >
      </parallax>
      <div class="container">
        <div class="content-center brand">
          <img class="n-logo" src="img/now-logo.png" alt="" />
          <h1 class="h1-seo">FANS.</h1>
          <h3 style="letter-spacing: 5px">
            {{ countUser }} 位用户为本站贡献了 {{ countPicture }} 张图片
          </h3>
        </div>
        <h6 class="category category-absolute">
          Designed by
          <a href="https://github.com/iu159" target="_blank">
            <img src="img/invision-white-slim.png" class="invision-logo" /> </a
          >.
        </h6>
      </div>
    </div>
    <category></category>
    <carouselSection></carouselSection>
    <div class="section">
      <div class="container text-center">
        <div class="row justify-content-md-center">
          <div class="col-md-12 col-lg-8">
            <h2 class="title">关于 FANS</h2>
            <h5 class="description">
              Fans是一个为武夷学院打造的摄影交流社区，也是本人的毕设项目，前后投入
              了将近两个月的时间。作为本人第一个开发的较为完整的项目，使用过程中难
              免出现一些问题，发现了请及时反馈哦~~~
            </h5>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Parallax } from "@/components";
import CarouselSection from "./components/CarouselSection";
import Category from "./components/Category";

export default {
  name: "index",
  bodyClass: "index-page",
  components: {
    Parallax,
    Category,
    CarouselSection,
  },
  data() {
    return {
      countUser: "",
      countPicture: "",
    };
  },
  created() {
    this.$get("/user/countUser").then((response) => {
      this.countUser = response;
    });
    this.$get("/picture/countPicture").then((response) => {
      this.countPicture = response;
    });
  },
};
</script>
<style></style>
