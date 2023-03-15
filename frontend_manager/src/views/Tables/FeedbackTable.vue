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
          <th>id</th>
          <th>理由</th>
          <th>联系方式</th>
          <th>反馈时间</th>
          <th>操作</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.id }}
          </td>
          <td>
            {{ row.reason }}
          </td>
          <td>
            {{ row.contactInfo }}
          </td>
          <td>
            {{ row.createTime }}
          </td>
          <td>
            <base-button
              v-if="row.status == '0'"
              type="info"
              size="sm"
              @click="read(row.id)"
              >已读</base-button
            >
            <base-button
              v-if="row.status == '1'"
              type="info"
              size="sm"
              @click="reply(row.id)"
              >已回复</base-button
            >
            <base-button v-if="row.status == '2'" type="info" size="sm" disabled
              >已处理</base-button
            >
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
      this.$get("/admin/feedback?pageNo=" + val).then((response) => {
        this.tableData = response.records;
        this.total = response.total;
      });
    },
    read(id) {
      this.$put("/admin/feedback/read?id=" + id).then(() => {
        this.toPage(this.pageNo);
      });
    },
    reply(id) {
      this.$put("/admin/feedback/reply?id=" + id).then(() => {
        this.toPage(this.pageNo);
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
