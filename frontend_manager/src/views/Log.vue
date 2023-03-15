<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <log-table title="日志查看"></log-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LogTable from "./Tables/LogTable";
export default {
  name: "Log",
  components: {
    LogTable,
  },
};
</script>

<style>
</style>