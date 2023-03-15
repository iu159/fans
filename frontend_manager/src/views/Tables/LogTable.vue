<template>
  <div class="card shadow" :class="type === 'dark' ? 'bg-default' : ''">
    <div
      class="card-header border-0"
      :class="type === 'dark' ? 'bg-transparent' : ''"
    >
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white' : ''">
            {{ title }}
          </h3>
        </div>     
      </div>
    </div>

    <div class="table-responsive">
      <base-table
        class="table align-items-center table-flush"
        :class="type === 'dark' ? 'table-dark' : ''"
        :thead-classes="type === 'dark' ? 'thead-dark' : 'thead-light'"
        tbody-classes="list"
        :data="tableData"
      >
        <template slot="columns">
          <th>ID</th>
          <th>用户ID</th>
          <th>操作</th>
          <th>IP</th>
          <th>等级</th>
          <th>时间</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.id }}
          </td>
          <td>
            {{ row.userId }}
          </td>
          <td>
            {{ row.content }}
          </td>
          <td>
            {{ row.ip }}
          </td>
          <td>
            <badge
              class="badge-dot mr-4"
              type="info"
              v-if="row.level == '1'"
            >
              <i class="bg-info"></i>
              <span class="status">info</span>
            </badge>
            <badge
              class="badge-dot mr-4"
              type="warning"
              v-else-if="row.level == '2'"
            >
              <i class="bg-warning"></i>
              <span class="status">warning</span>
            </badge>
            <badge class="badge-dot mr-4" type="danger" v-else>
              <i class="bg-danger"></i>
              <span class="status">danger</span>
            </badge>
          </td>
          <td>
            {{ row.createTime }}
          </td>
        </template>
      </base-table>
    </div>
    <div
      class="card-footer d-flex justify-content-end"
      :class="type === 'dark' ? 'bg-transparent' : ''"
    >
      <base-pagination
        :total="Number(total)"
        v-model="pageNo"
      ></base-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: "projects-table",
  data() {
    return {
      pageNo: 1,
      total: "",
      modals: false,
      tableData: [],
    };
  },
  watch: {
    pageNo(val) {
      this.toPage(val);
    },
  },
  methods: {
    toPage(val) {
      this.$get("/admin/common/logs?pageNo=" + val).then((response) => {
        this.tableData = response.records;
        this.total = response.total;
      });
    },
  },
  created() {
    this.toPage(1);
  },
  props: {
    type: {
      type: String,
    },
    title: String,
  },
};
</script>
<style>
</style>
