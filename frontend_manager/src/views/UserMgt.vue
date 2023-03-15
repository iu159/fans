<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col">
          <user-mgt-table title="用户管理"></user-mgt-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import UserMgtTable from "./Tables/UserMgtTable";
export default {
  name: "tables",
  data(){
    return{
      modals :false
    }
  },
  components: {
    UserMgtTable,
  },
};
</script>
<style></style>
