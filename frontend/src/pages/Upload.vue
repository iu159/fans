<template>
  <div>
    <div class="section" style="margin-top: 50px">
      <div class="container">
        <div class="row">
          <div class="col-md-7">
            <el-upload        
              action="http://116.62.65.154:8888/picture/upload"
              ref="uploads"
              class="avatar-uploader"
              name="image"
              :limit="parseInt(1)"
              :show-file-list="true"
              :auto-upload="false"             
              :on-change="onchange"
              :on-success="toggleCenterDialogVisible"
              :data="pictureParam"
              :with-credentials="true"
              multiple
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
          <div class="col-md-5">
            <div class="card">
              <div class="card-body">
                <el-form label-position="left" label-width="50px">
                  <el-form-item label="标题">
                    <el-input v-model.trim="pictureParam.title"></el-input>
                  </el-form-item>
                  <el-form-item label="描述">
                    <el-input
                      type="textarea"
                      autosize
                      v-model.trim="pictureParam.summary"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="分类">
                    <el-select
                      v-model="pictureParam.category"
                      placeholder="请选择"
                    >
                      <el-option
                        v-for="category in categories"
                        :key="category.id"
                        :label="category.name"
                        :value="category.id"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="标签">
                    <el-tag
                      :key="tag"
                      v-for="tag in pictureParam.tags"
                      closable
                      :disable-transitions="false"
                      type="success"
                      @close="handleClose(tag)"
                    >
                      {{ tag }}
                    </el-tag>
                    <el-input
                      class="input-new-tag"
                      v-if="inputVisible"
                      v-model.trim="inputValue"
                      ref="saveTagInput"
                      size="small"
                      @keyup.enter.native="handleInputConfirm"
                      @blur="handleInputConfirm"
                    >
                    </el-input>
                    <el-button
                      v-else
                      class="button-new-tag"
                      size="small"
                      @click="showInput"
                      >+ 标签</el-button
                    >
                  </el-form-item>
                </el-form>
              </div>
              <div class="card-footer text-center">
                <button id="mybutton" @click="submit">发布图片</button>
              </div>
            </div>
            <el-dialog :visible.sync="centerDialogVisible" width="15%">
              <span>上传成功，接下来要干嘛？</span>
              <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="toHome" class="pull-left"
                  >回到主页</el-button
                >
                <el-button type="primary" @click="continueUpload"
                  >继续上传</el-button
                >
              </span>
            </el-dialog>
          </div>
        </div>
      </div>
    </div>
    <main-footer></main-footer>
  </div>
</template>

<script>
import {
  Upload,
  Input,
  Tag,
  Button,
  Form,
  FormItem,
  Select,
  Option,
  Dialog,
} from "element-ui";
import MainFooter from "@/layout/MainFooter";
import axios from "axios";
export default {
  name: "upload-page",
  bodyClass: "upload-page",
  components: {
    MainFooter,
    [Upload.name]: Upload,
    [Input.name]: Input,
    [Tag.name]: Tag,
    [Button.name]: Button,
    [Form.name]: Form,
    [FormItem.name]: FormItem,
    [Select.name]: Select,
    [Option.name]: Option,
    [Dialog.name]: Dialog,
  },
  data() {
    return {
      centerDialogVisible: false,
      inputVisible: false,
      inputValue: "",
      imageUrl: "",
      categories: [],
      pictureParam: {
        title: "",
        summary: "",
        tags: ["fans"],
        category: 1,
      },
    };
  },
  methods: {
    onchange: function (file, filelist) {
      let fileName = file.name;
      let regex = /(.jpg|.jpeg|.gif|.png|.bmp)$/;
      if (regex.test(fileName.toLowerCase())) {
        this.imageUrl = URL.createObjectURL(file.raw);
      } else {
        this.$message.error("请选择图片文件");
      }
    },
    handleClose(tag) {
      this.pictureParam.tags.splice(this.pictureParam.tags.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.pictureParam.tags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
    submit() {
      if (
        this.pictureParam.title.length < 2 ||
        this.pictureParam.title.length > 20
      ) {
        this.$message.warning("标题长度需在2~20之间");
      } else if (this.imageUrl == "") {
        this.$message.warning("图片不能为空");
      } else {
        this.$refs.uploads.submit();
      }
    },  
    toggleCenterDialogVisible(){
      this.centerDialogVisible = true
    },
    toHome() {
      this.$router.push("/profile/" + this.$store.state.user.uid);
    },
    continueUpload() {
      this.$router.push({
        path: "/whitepage",
        query: {
          realPath: "/upload",
        },
      });
    },
  },
  created() {
    this.$get("/category").then((response) => {
      this.categories = response.data;
    });
  },
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  border: 1px dashed #d9d9d9;
  font-size: 28px;
  color: #8c939d;
  background-color: #f7f8fa;
  width: 500px;
  height: 400px;
  line-height: 400px;
  text-align: center;
}
.avatar {
  width: 600px;
  /* height: 178px; */
  display: block;
}

.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

#mybutton {
  margin: 10px;
  font-size: 33px;
  width: 90%;
  color: white;
  background-color: #67c23a;
  border-radius: 20px;
  border: none;
}
</style>