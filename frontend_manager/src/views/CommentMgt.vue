<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <comment-mgt-table title="评论管理"></comment-mgt-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import CommentMgtTable from "./Tables/CommentMgtTable";
export default {
  name: "commentMgt",
  components: {
    CommentMgtTable,
  },
};
</script>
<style></style>
