<template>
  <div>
    <a @click="isOpen = true" href="#pablo" class="link footer-link"
      >遇到问题?</a
    >
    <el-dialog title="反馈" :visible.sync="isOpen" :modal="false" width="500px">
      <el-form :model="form">
        <el-form-item label="联系方式" label-width="80px">
          <el-input v-model="form.contactInfo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="原因" label-width="80px">
          <el-input
            type="textarea"
            autosize
            placeholder="请输入内容"
            v-model="form.reason"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isOpen = false">取 消</el-button>
        <el-button type="primary" @click="feedback">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Input, Button, Form, FormItem, Dialog } from "element-ui";
export default {
  name: "Feedback",
  components: {
    [Input.name]: Input,
    [Button.name]: Button,
    [Form.name]: Form,
    [FormItem.name]: FormItem,
    [Dialog.name]: Dialog,
  },
  data() {
    return {
      isOpen: false,
      form: {
        contactInfo: "",
        reason: "",
      },
    };
  },
  methods: {
    toggleFeedback() {
      this.isOpen = !this.isOpen;
    },
    feedback() {
      this.$post(
        "/feedback",
        this.$qs.stringify({
          contactInfo: this.form.contactInfo,
          reason: this.form.reason,
        })
      ).then(()=>{
        this.form.contactInfo = "";
        this.form.reason = "";
        this.isOpen = false;
        this.$message("已反馈，等待管理员处理吧~")
      });
    },
  },
};
</script>
<style>
</style>