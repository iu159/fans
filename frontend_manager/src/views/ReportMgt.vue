<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <report-mgt-table title="举报管理"></report-mgt-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ReportMgtTable from "./Tables/ReportMgtTable";
export default {
  name: "reportMgt",
  components: {
    ReportMgtTable,
  },
};
</script>

<style>
</style>