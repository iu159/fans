<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <picture-mgt-table title="图片管理"></picture-mgt-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import PictureMgtTable from "./Tables/PictureMgtTable";
export default {
  name: "pictureMgt",
  components: {
    PictureMgtTable,
  },
};
</script>
<style></style>
