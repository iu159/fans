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
          <th>内容</th>
          <th>用户ID</th>
          <th>图片ID</th>
          <th>父级评论ID</th>
          <th>当前状态</th>
          <th>评论时间</th>
          <th>操作</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.id }}
          </td>
          <td>
            {{ row.content }}
          </td>
          <td>
            {{ row.userId }}
          </td>
          <td>
            {{ row.pictureId }}
          </td>
          <td>
            {{ row.parentId }}
          </td>
          <td>
            {{ row.isDeleted }}
          </td>
          <td>
            {{ row.createTime }}
          </td>
          <td>
            <base-button type="danger" size="sm" @click="block(row.userId)"
              >封禁评论者</base-button
            >
            <base-button
              type="warning"
              size="sm"
              @click="remove(row.id)"
              v-if="row.isDeleted == 1"
              >删除</base-button
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
      current: {},
      categories: [],
    };
  },
  watch: {
    pageNo(val) {
      this.toPage(val);
    },
  },
  methods: {
    block(uid) {
      this.$put("/admin/user/block/" + uid);
    },
    remove(id) {
      this.$remove("/admin/comment?id=" + id);
    },
    toPage(val) {
      this.$get("/admin/comment?pageNo=" + val).then((response) => {
        console.log(response);
        this.tableData = response.list;
        this.total = response.total;
      });
    },
  },
  created() {
    this.toPage(1);
    this.$get("/category").then((response) => {
      this.categories = response.data;
    });
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
