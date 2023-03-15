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
          <th>用户ID</th>
          <th>用户</th>
          <th>邮箱</th>
          <th>电话</th>
          <th>用户状态</th>
          <th>上次登陆时间</th>
          <th>操作</th>
        </template>

        <template slot-scope="{ row }">
          <td>
            {{ row.uid }}
          </td>
          <th scope="row">
            <div class="media align-items-center">
              <a href="#" class="avatar rounded-circle mr-3">
                <img alt="Image placeholder" :src="row.profilePictureUrl" />
              </a>
              <div class="media-body">
                <span class="name mb-0 text-sm">{{ row.username }}</span>
              </div>
            </div>
          </th>
          <td>
            {{ row.email }}
          </td>
          <td>
            {{ row.telephone }}
          </td>
          <td>
            <badge
              class="badge-dot mr-4"
              type="Default"
              v-if="row.userStatus == '0'"
            >
              <i class="bg-default"></i>
              <span class="status">未激活</span>
            </badge>
            <badge
              class="badge-dot mr-4"
              type="success"
              v-else-if="row.userStatus == '1'"
            >
              <i class="bg-success"></i>
              <span class="status">正常</span>
            </badge>
            <badge class="badge-dot mr-4" type="danger" v-else>
              <i class="bg-danger"></i>
              <span class="status">封禁</span>
            </badge>
          </td>
          <td>
            {{ row.updateTime }}
          </td>

          <td>
            <base-button type="warning" size="sm" @click="changeInfo(row)"
              >修改</base-button
            >
            <modal
              :show.sync="modals"
              body-classes="p-0"
              modal-classes="modal-dialog-centered modal-sm"
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
                    <h4>{{ current.uid }}</h4>
                  </div>
                  <form role="form">
                    <base-input
                      alternative
                      class="mb-3"
                      placeholder="用户名..."
                      addon-left-icon="ni ni-single-02"
                      v-model="current.username"
                    >
                    </base-input>
                    <base-input
                      alternative
                      class="mb-3"
                      type="password"
                      placeholder="密码..."
                      addon-left-icon="ni ni-lock-circle-open"
                      v-model="current.password"
                    >
                    </base-input>
                    <base-input
                      alternative
                      class="mb-3"
                      placeholder="邮箱..."
                      addon-left-icon="ni ni-email-83"
                      v-model="current.email"
                    >
                    </base-input>
                    <base-input
                      alternative
                      class="mb-3"
                      placeholder="电话..."
                      addon-left-icon="ni ni-mobile-button"
                      v-model="current.telephone"
                    >
                    </base-input>

                    <label>用户状态</label>
                    <select class="form-control" v-model="current.userStatus">
                      <option value="0">未激活</option>
                      <option value="1">正常</option>
                      <option value="4">封禁</option>
                    </select>
                    <label>个性签名</label>
                    <textarea
                      class="form-control"
                      rows="2"
                      placeholder="个性签名..."
                      v-model="current.description"
                    ></textarea>
                  </form>
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
                      @click="updateInfo"
                      >确认修改</base-button
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
      current: {},
    };
  },
  watch: {
    pageNo(val) {
      this.toPage(val);
    },
  },
  methods: {
    changeInfo(user) {
      this.current = user;
      this.modals = true;
    },
    updateInfo() {
      this.$put("/admin/user/edit", this.current).then((response) => {
        console.log(response);
      });
    },
    toPage(val) {
      this.$get("/admin/user/listUser?pageNo="+val).then((response) => {
        console.log(response);
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
