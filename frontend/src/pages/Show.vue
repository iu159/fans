<template>
  <div>
    <div class="main">
      <div class="container-fluid">
        <div id="myGallery">
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
  name: "show",
  components: {},
  data() {
    return {
      pictures: [],
      params: this.$route.query.params,
    };
  },
  methods: {
    showDetail() {
      this.$router.push({
        path: "/picture/" + this.picture.pid,
      });
    },
    getPictures() {
      this.$get(this.$route.query.realPath, this.params).then((response) => {
        this.pictures = response;
        this.$nextTick(() => {
          $("#myGallery").justifiedGallery({
            rowHeight: 350,
            margins: 6,
            border: 30,
          });
        });
      });
    },
  },
  watch: {
    $route(to, from) {
      this.params = this.$route.query.params;
      this.getPictures();
    },
  },
  created() {
    this.getPictures();
  },
};
</script>

<style scoped>
</style>