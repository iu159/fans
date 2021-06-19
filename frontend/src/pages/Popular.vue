<template>
  <div>
    <div class="main">
      <div class="container-fluid">
        <div
          class="row infinite-list"
          v-infinite-scroll="load"
          infinite-scroll-distance="30"
          infinite-scroll-immediate="false"
          id="myGallery"
        >
          <router-link
            target="_blank"
            v-for="picture in pictures"
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
import $ from "jquery";
export default {
  name: "popular",
  components: {},
  data() {
    return {
      pageNo: 1,
      pictures: [],
    };
  },
  methods: {
    showDetail() {
      this.$router.push({
        path: "/picture/" + this.picture.pid,
      });
    },
    load() {
      this.pageNo += 1;
      this.getPictures();
    },
    getPictures() {
      this.$get("/picture/listPicture", { pageNo: this.pageNo }).then(
        (response) => {
          this.pictures = [...this.pictures, ...response.records];
          this.$nextTick(() => {
            $("#myGallery").justifiedGallery({
              rowHeight: 350,
              margins: 6,
              border: 30,
            });
          });
        }
      );
    },
  },
  created() {
    this.getPictures();
  },
};
</script>

<style scoped>
</style>