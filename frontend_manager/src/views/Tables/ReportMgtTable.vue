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
          <th>类型</th>
          <th>理由</th>
          <th>被举报人</th>
          <th>举报时间</th>
          <th>当前状态</th>
          <th>操作</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.type }}
          </td>
          <td>
            {{ row.reason }}
          </td>
          <td>
            {{ row.accusedId }}
          </td>
          <td>
            {{ row.createTime }}
          </td>
          <td>
            <badge
              class="badge-dot mr-4"
              type="warning"
              v-if="row.progress == '0'"
            >
              <i class="bg-warning"></i>
              <span class="status">未读</span>
            </badge>
            <badge
              class="badge-dot mr-4"
              type="info"
              v-else-if="row.progress == '1'"
            >
              <i class="bg-info"></i>
              <span class="status">已读</span>
            </badge>
            <badge class="badge-dot mr-4" type="success" v-else>
              <i class="bg-success"></i>
              <span class="status">已处理</span>
            </badge>
          </td>
          <td>
            <base-button v-if="row.progress !='1' && row.progress !='2'" type="warning" size="sm" @click="handler(row.id)"
              >处理</base-button
            >
            <modal
              :show.sync="modals"
              body-classes="p-0"
              modal-classes="modal-dialog-centered modal-bg"
            >
              <card
                type="secondary"
                shadow
                header-classes="bg-white pb-5"
                body-classes="px-lg-5 py-lg-5"
                class="border-0"
              >
                <template>
                  <div class="text-center text-muted mb-4">
                    <h4>{{ detail.id }}</h4>
                  </div>
                  <table class="table">
                    <tbody>
                      <tr>
                        <td>详情</td>
                        <td>{{ detail.targetId }} {{ detail.type }}</td>
                      </tr>
                      <tr>
                        <td>理由</td>
                        <td>{{ detail.reason }}</td>
                      </tr>
                      <tr>
                        <td>举报人</td>
                        <td>
                          {{ detail.accuserId }} {{ detail.accuser.username }}
                        </td>
                      </tr>
                      <tr>
                        <td>被举报人</td>
                        <td>
                          {{ detail.accusedId }} {{ detail.accused.username }}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                  <hr />
                  <textarea
                    class="form-control"
                    rows="2"
                    placeholder="回复..."
                    v-model="reply"
                  ></textarea>
                  <base-checkbox
                    class="mb-3"
                    v-model="isBlock"
                    style="margin-top: 15px"
                  >
                    封禁被举报用户？
                  </base-checkbox>
                  <template slot="footer">
                    <base-button
                      type="link"
                      class="ml-auto"
                      @click="modals = false"
                      >取消
                    </base-button>
                    <base-button
                      style="float: right; margin-right: 15px"
                      type="warning"
                      @click="updateHandle(detail.id ,detail.accuserId, detail.accusedId)"
                      >处理</base-button
                    >
                  </template>
                </template>
              </card>
            </modal>
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
      detail: {
        accuser: {},
        accused: {},
      },
      reply: "",
      isBlock: false,
    };
  },
  watch: {
    pageNo(val) {
      this.toPage(val);
    },
  },
  methods: {
    handler(id) {
      this.$get("/admin/report/detail/" + id).then((response) => {
        this.detail = response;
        this.modals = true;
      });
    },
    updateHandle(id, accuserId, accusedId) {
      this.$post("/admin/report/handle", {
        reply: this.reply,
        isBlock: this.isBlock,
        accuserId: accuserId,
        accusedId: accusedId,
        id: id
      }).then((response) => {
        console.log(response);
      });
    },
    toPage(val) {
      this.$get("/admin/report/list?pageNo=" + val).then((response) => {
        this.tableData = response.list;
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
