<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入企业名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系人" prop="linkMan">
        <el-input
          v-model="queryParams.linkMan"
          placeholder="请输入联系人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="企业类型" prop="companyType">
        <el-select
          v-model="queryParams.companyType"
          placeholder="请选择企业类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in companyTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['dictionary:company:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['dictionary:company:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['dictionary:company:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dictionary:company:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="companyList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="show" />
      <el-table-column label="企业名称" align="center" prop="companyName" />
      <el-table-column
        label="企业类型"
        align="center"
        prop="companyType"
        :formatter="czyFormat"
      />
      <el-table-column label="联系人" align="center" prop="linkMan" />
      <el-table-column label="联系方式" align="center" prop="linkPhone" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['dictionary:company:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dictionary:company:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改企业信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input
                v-model="form.companyName"
                placeholder="请输入企业名称"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="所属地区" prop="provinceName">
              <el-cascader
                v-model="form.field101"
                :options="field101Options"
                :props="field101Props"
                :style="{ width: '100%' }"
                placeholder="请选择所属地区"
                clearable
              ></el-cascader>
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="信用代码" prop="creditCode">
              <el-input
                v-model="form.creditCode"
                placeholder="请输入信用代码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系人" prop="linkMan">
              <el-input v-model="form.linkMan" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系方式" prop="linkPhone">
              <el-input v-model="form.linkPhone" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="开户行" prop="openingBank">
              <el-input v-model="form.openingBank" placeholder="请输入开户行" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开户账号" prop="openingAccount">
              <el-input
                v-model="form.openingAccount"
                placeholder="请输入开户账号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业类型" prop="companyType">
              <el-select
                v-model="form.companyType"
                placeholder="请选择企业类型"
              >
                <el-option
                  v-for="dict in companyTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="地址" prop="address">
              <el-input
                v-model="form.address"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCompany,
  getCompany,
  delCompany,
  addCompany,
  updateCompany,
  exportCompany,
  findAreaList,
} from "@/api/dictionary/company";

export default {
  name: "Company",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      show: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 企业信息表格数据
      companyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        creditCode: null,
        companyName: null,
        provinceId: null,
        provinceName: null,
        cityId: null,
        cityName: null,
        address: null,
        linkMan: null,
        linkPhone: null,
        openingBank: null,
        openingAccount: null,
        companyType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        companyName: [{
          required: true,
          message: '请输入企业名称',
        }],
        companyType: [{
          required: true,
          message: '请选择企业类型',
        }],
      },
      // 企业类型数据字典
      companyTypeOptions: [],
      field101Options: [],
      field101Props: {
        multiple: false,
      },
    };
  },
  created() {
    this.getList();
    //获取企业类型字典
    this.getDicts("company_type").then((response) => {
      this.companyTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询企业信息列表 */
    getList() {
      this.loading = true;
      listCompany(this.queryParams).then((response) => {
        this.companyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      findAreaList().then((response) => {
        this.field101Options = JSON.parse(response.data);
      });
    },
    czyFormat(row, column) {
      return this.selectDictLabel(this.companyTypeOptions, row.companyType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        creditCode: null,
        companyName: null,
        provinceId: null,
        provinceName: null,
        cityId: null,
        cityName: null,
        address: null,
        linkMan: null,
        linkPhone: null,
        openingBank: null,
        openingAccount: null,
        companyType: null,
        delFlag: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加企业信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCompany(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业信息";
      });
    },
    /** 提交按钮 */
    submitForm() {

      if(this.form.companyType == "2"){
        if(this.form.linkMan == null){
          this.msgSuccess("请输入联系人");
          this.isForbidden = false;
          return false;
        }
        if(this.form.linkPhone == null){
          this.msgSuccess("请输入联系方式");
          this.isForbidden = false;
          return false;
        }
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateCompany(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCompany(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const names = row.companyName;
      this.$confirm('是否确认删除企业名称为"' + names + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delCompany(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有企业信息数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportCompany(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
<style>
.el-textarea .el-textarea__inner {
  resize: none;
}
</style>
