<template>
  <div>empty</div>
</template>

<script>
export default {
  created() {
    this.$router.replace(this.$route.query.realPath);
  },
};
</script>

<style>
</style>