<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="queryParams.contractName" placeholder="请输入合同名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
      <!-- <el-form-item label="经办部门" prop="manageDeptment">
        <el-input
          v-model="queryParams.manageDeptment"
          placeholder="请输入经办部门"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
        <el-col :span="6">
          <el-form-item label="经办人" prop="manageUser">
            <el-input v-model="queryParams.manageUser" placeholder="请输入经办人" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
<!--      <el-form-item label="应付款日期" prop="estimateTime">
        <el-date-picker
          clearable
          size="small"
          style="width: 200px"
          v-model="queryParams.estimateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择应付款日期"
        ></el-date-picker>
      </el-form-item> -->
        <el-col :span="6">
          <el-form-item label="是否处理" prop="handleFlag">
            <el-select v-model="queryParams.handleFlag" placeholder="请选择是否处理" clearable size="small">
              <el-option v-for="dict in proTypeNameList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="8">
          <el-form-item label="应付款起止日期" label-width="150px">
            <el-date-picker
              v-model="estimateTime"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5"><el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['contract:warning:add']">新增</el-button></el-col> -->
     <!-- <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['contract:warning:edit']">修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['contract:warning:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-upload2" size="mini" @click="handleExport" v-hasPermi="['contract:warning:export']">
          导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="warningList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预警ID" align="center" prop="id" v-if="show" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="合同名称" align="center" prop="contractName" />
      <el-table-column label="经办部门" align="center" prop="manageDeptment" />
      <el-table-column label="经办人" align="center" prop="manageUser" />
      <el-table-column label="应付款日期" align="center" prop="estimateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.estimateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应付款金额" align="center" prop="estimateMoney" />
      <el-table-column label="实付款金额" align="center" prop="actualMoney" />
      <el-table-column label="是否处理" align="center" prop="handleFlag">
        <template slot-scope="s">
        	<p v-if="s.row.handleFlag == 0">未处理</p>
        	<p v-if="s.row.handleFlag == 1">已处理</p>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['contract:warning:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['contract:warning:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改合同预警对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="40%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
            <el-col :span="12">
              <el-form-item label="合同ID" prop="contractId"><el-input v-model="form.contractId" placeholder="请输入合同ID" /></el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同编号" prop="contractNo"><el-input v-model="form.contractNo" placeholder="请输入合同编号" /></el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item label="合同名称" prop="contractName"><el-input v-model="form.contractName" placeholder="请输入合同名称" /></el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="经办部门" prop="manageDeptment"><el-input v-model="form.manageDeptment" placeholder="请输入经办部门" /></el-form-item>
                  </el-col>
        </el-row>
        <!-- <el-col :span="12"> -->
        <el-form-item label="经办人" prop="manageUser"><el-input v-model="form.manageUser" placeholder="请输入经办人" /></el-form-item>
        <el-form-item label="应付款日期" prop="estimateTime">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="form.estimateTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择应付款日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="应付款金额" prop="estimateMoney"><el-input v-model="form.estimateMoney" placeholder="请输入应付款金额" /></el-form-item>
        <el-form-item label="实付款金额" prop="actualMoney"><el-input v-model="form.actualMoney" placeholder="请输入实付款金额" /></el-form-item>
        <el-form-item label="是否处理(0未处理 1已处理)" prop="handleFlag"><el-input v-model="form.handleFlag" placeholder="请输入是否处理(0未处理 1已处理)" /></el-form-item>
        <el-form-item label="删除标志" prop="delFlag"><el-input v-model="form.delFlag" placeholder="请输入删除标志" /></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" placeholder="请输入内容" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWarning, getWarning, delWarning, addWarning, updateWarning, exportWarning } from '@/api/contract/warning';

export default {
  name: 'Warning',
  data() {
    return {
      // 遮罩层
      loading: true,
      show: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // 合同预警表格数据
      warningList: [],
      proTypeNameList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      //日期范围
      estimateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractId: null,
        contractNo: null,
        contractName: null,
        manageDeptment: null,
        manageUser: null,
        estimateTime: null,
        estimateMoney: null,
        actualMoney: null,
        handleFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getDicts('warning_status').then(response => {
      this.proTypeNameList = response.data;
    });
  },
  methods: {
    /** 查询合同预警列表 */
    getList() {
      this.loading = true;
      listWarning(this.addDateRange(this.queryParams, this.estimateTime)).then(response => {
        this.warningList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        contractId: null,
        contractNo: null,
        contractName: null,
        manageDeptment: null,
        manageUser: null,
        estimateTime: null,
        estimateMoney: null,
        actualMoney: null,
        handleFlag: null,
        delFlag: null,
        remark: null
      };
      this.resetForm('form');
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm');
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '添加合同预警';
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWarning(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = '修改合同预警';
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWarning(this.form).then(response => {
              this.msgSuccess('修改成功');
              this.open = false;
              this.getList();
            });
          } else {
            addWarning(this.form).then(response => {
              this.msgSuccess('新增成功');
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
      this.$confirm('是否确认删除合同预警编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return delWarning(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess('删除成功');
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有合同预警数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportWarning(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        });
    }
  }
};
</script>
