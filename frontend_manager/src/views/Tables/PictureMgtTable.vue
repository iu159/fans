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
          <th>图片ID</th>
          <th>图片</th>
          <th>作者</th>
          <th>分类</th>
          <th>状态</th>
          <th>浏览数</th>
          <th>操作</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.pid }}
          </td>
          <th scope="row">
            <div class="media align-items-center">
              <a href="#" class="rounded-circle mr-3">
                <img height="60px" alt="Image placeholder" :src="row.smUrl" />
              </a>
              <div class="media-body">
                <span class="name mb-0 text-sm">{{ row.title }}</span>
              </div>
            </div>
          </th>
          <td>
            {{ row.userId }}
          </td>
          <td>
            <div v-for="category in categories" :key="category.id">
              <div v-if="category.id == row.category">{{ category.name }}</div>
            </div>
          </td>
          <td>
            <badge
              class="badge-dot mr-4"
              type="danger"
              v-if="row.status == '0'"
            >
              <i class="bg-danger"></i>
              <span class="status">已删除</span>
            </badge>
            <badge
              class="badge-dot mr-4"
              type="success"
              v-else-if="row.status == '1'"
            >
              <i class="bg-success"></i>
              <span class="status">正常</span>
            </badge>
          </td>
          <td>
            {{ row.viewCount }}
          </td>

          <td>
            <base-button
              type="danger"
              size="sm"
              @click="remove(row.pid)"
              v-if="row.status == '1'"
              >删除</base-button
            >
            <base-button
              type="warning"
              size="sm"
              @click="recover(row.pid)"
              v-if="row.status == '0'"
              >恢复</base-button
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
    remove(pid) {
      this.$remove("/admin/picture/delete/" + pid).then(() => {
        this.toPage(this.pageNo);
      });
    },
    recover(pid) {
      this.$put("/admin/picture/recover/" + pid).then(() => {
        this.toPage(this.pageNo);
      });
    },
    toPage(val) {
      this.$get("/admin/picture/listPicture?pageNo=" + val).then((response) => {
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
