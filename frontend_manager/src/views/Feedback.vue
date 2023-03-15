<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <feedback-table title="反馈查看"></feedback-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import FeedbackTable from "./Tables/FeedbackTable";
export default {
  name: "Feedback",
  components: {
    FeedbackTable,
  },
};
</script>
<style></style>
