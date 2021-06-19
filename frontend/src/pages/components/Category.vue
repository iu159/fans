<template>
  <div class="section" id="carousel">
    <div class="container">
      <div class="title">
        <h4>热门分类</h4>
      </div>
      <div class="row justify-content-center">
        <div class="row row-cols-1 row-cols-md-4">
          <div
            v-for="category in categories"
            :key="category.id"
            class="col mb-3"
          >
            <div class="card bg-dark text-white" @click="toCategory(category.id)">
              <img
                :src="'img/my/category/' + category.name + '.jpg'"
                class="card-img"
              />
              <div class="card-img-overlay">
                <h5 class="card-title">{{ category.name }}</h5>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      categories: [],
    };
  },
  methods: {
    toCategory(categoryId) {
      this.$router.push({
        path: "/show",
        query: {
          realPath: "/picture/category/"+categoryId
        },
      });
    },
  },
  created() {
    this.$get("/category").then((response) => {
      this.categories = response.data;
    });
  },
};
</script>
<style></style>
