<template>
  <div class="section" id="carousel">
    <div class="container">
      <div class="title">
        <h4>FANS推荐</h4>
      </div>
      <div class="row justify-content-center">
        <div class="col-12">
          <el-carousel height="500px">
            <el-carousel-item v-for="picture in pictures" :key="picture.pid">
              <img
                class="d-block"
                :src="picture.url"
                @click="showDetail(picture.pid)"
              />
              <div class="carousel-caption d-none d-md-block">
                <h5>{{ picture.title }}</h5>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Carousel, CarouselItem } from "element-ui";

export default {
  components: {
    [Carousel.name]: Carousel,
    [CarouselItem.name]: CarouselItem,
  },
  data() {
    return {
      pictures: [],
    };
  },
  methods: {
    showDetail(pid) {
      let routeData = this.$router.resolve({
        path: "/picture/" + pid,
      });
      window.open(routeData.href, "_blank");
    },
  },
  created() {
    this.$get("/picture/recommend").then((response) => {
      this.pictures = response;
    });
  },
};
</script>
<style></style>
