<template>
  <div>
    <el-form style="padding: 1.25rem" :model="queryParams" ref="queryForm"  v-show="showSearch" label-width="90px">
    <el-col :span="6">
      <el-form-item label="项目类别" prop="projectCategory">
        <el-select v-model="queryParams.projectCategory" placeholder="请选择项目类别" clearable size="small">
          <el-option v-for="dict in projectCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="6">  
      <el-form-item label="项目编号" prop="projectNo">
        <el-input v-model="queryParams.projectNo" placeholder="请输入项目编号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
    </el-col>
    <el-col :span="6"> 
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
    </el-col>
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
    <el-col :span="6"> 
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-col>

    </el-form>
    <el-dialog title="新增信息" :visible.sync="dialogVisible" width="80%">
      <el-form ref="form" :model="form">
        <!-- <infoAdd ref='infoAdd'></infoAdd> -->
        <el-tabs v-model="activeName" v-on:tab-click="onTabClick()">
          <el-tab-pane label="付款信息" name="third" ><payInfo ref="payInfo"></payInfo></el-tab-pane>
        </el-tabs>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm(0)">保存</el-button>
        <el-button type="primary" @click="submitForm(1)">提交</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </span>
    </el-dialog>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="项目类别" align="center" prop="projectCategory" />
      <el-table-column label="项目编号" align="center" prop="projectNo" />
      <el-table-column label="项目名称" align="center" prop="projectName" />

      <el-table-column label="合同名称" align="center" prop="contractName" />
      <el-table-column label="状态说明" align="center" prop="statusPs" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == 0" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['contract:contractInfo:edit']">添加</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { ticketListContract, getContract, delContract, addContract, updateTicketContract, exportContract } from '@/api/contract/contractInfo';
import payInfo from './payInfo.vue';

export default {
  name: 'Contract',
  components: {
    payInfo
  },

  data() {
    return {
      activeName: 'third',

      dialogVisible: false,
      // 合同信息录入主表格数据
      contractList: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      //项目类型
      projectCategoryTypeList: [],
      //合同类别
      contractCategoryTypeList: [],
      //收入类型
      incomeTypeList: [],
      //付款方式
      payMethodList: [],
      //合同状态
      contractStatusList: [],

      contentTypeList: [],
      // 日期范围
      starttime: [],
      endtime: [],
      contractServeStart: [],
      contractServeEnd: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectCategory: null,
        contentType: null,
        projectId: null,
        projectNo: null,
        projectName: null,
        contractNo: null,
        contractName: null,
        month: null,
        contractCategory: null,
        incomeType: null,
        oppositeName: null,
        oppositeBank: null,
        oppositeBankId: null,
        oppositeContacts: null,
        oppositePhone: null,
        oppositeAddress: null,
        ourName: null,
        ourContacts: null,
        ourPhone: null,
        ourBank: null,
        ourBankId: null,
        ourAddress: null,
        starttime: null,
        endtime: null,
        contractServeStart: null,
        serviceTerm: null,
        contractServeEnd: null,
        totalMoney: null,
        taxrate: null,
        notTaxMoney: null,
        payMethod: null,
        archiveFiles: null,
        manageDeptment: null,
        manageUser: null,
        originalId: null,
        status: null,
        statusPs: null,
        contractScanningFile: null,
        contractApprovalFile: null,
        otherFiles: null,
        remarks: null,
        sum: '' //统计
      },

      form: {
        id: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        projectCategory: null,
        projectId: null,
        projectNo: null,
        projectName: null,
        contractNo: null,
        contractName: null,
        month: null,
        contractCategory: null,
        incomeType: null,
        oppositeName: null,
        oppositeBank: null,
        oppositeBankId: null,
        oppositeContacts: null,
        oppositePhone: null,
        oppositeAddress: null,
        ourName: null,
        ourContacts: null,
        ourPhone: null,
        ourBank: null,
        ourBankId: null,
        ourAddress: null,
        starttime: null,
        endtime: null,
        contractServeStart: null,
        serviceTerm: null,
        contractServeEnd: null,
        totalMoney: null,
        taxrate: null,
        notTaxMoney: null,
        payMethod: null,
        archiveFiles: null,
        manageDeptment: null,
        manageUser: null,
        originalId: null,
        status: 0,
        statusPs: null,
        contractScanningFile: null,
        contractApprovalFile: null,
        otherFiles: null,
        delFlag: null,
        remarks: null,
        ctBuContractPayinfoList: [],
        ctBuContractContentList: []
      }
    };
  },
  created() {
    this.getList();
    //项目类型列表
    this.getDicts('project_category_type').then(response => {
      this.projectCategoryTypeList = response.data;
    });
    //合同类别
    this.getDicts('contract_category_type').then(response => {
      this.contractCategoryTypeList = response.data;
    });
    //收入类型
    this.getDicts('income_type').then(response => {
      this.incomeTypeList = response.data;
    });
    //付款方式
    this.getDicts('pay_method').then(response => {
      this.payMethodList = response.data;
    });
    //合同状态
    this.getDicts('contract_status').then(response => {
      this.contractStatusList = response.data;
    });
  },
  methods: {

    /** 查询合同信息录入主列表 */
    getList() {
      this.loading = true;
      ticketListContract(this.addDateRange(this.queryParams, this.contractServeStart)).then(response => {
        this.contractList = response.rows;
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
        projectCategory: null,

        projectId: null,
        projectNo: null,
        projectName: null,
        contractNo: null,
        contractName: null,
        month: null,
        contractCategory: null,
        incomeType: null,
        oppositeName: null,
        oppositeBank: null,
        oppositeBankId: null,
        oppositeContacts: null,
        oppositePhone: null,
        oppositeAddress: null,
        ourName: null,
        ourContacts: null,
        ourPhone: null,
        ourBank: null,
        ourBankId: null,
        ourAddress: null,
        starttime: null,
        endtime: null,
        contractServeStart: null,
        serviceTerm: null,
        contractServeEnd: null,
        totalMoney: null,
        taxrate: null,
        notTaxMoney: null,
        payMethod: null,
        archiveFiles: null,
        manageDeptment: null,
        manageUser: null,
        originalId: null,
        status: 0,
        statusPs: null,
        contractScanningFile: null,
        contractApprovalFile: null,
        otherFiles: null,
        delFlag: null,
        remarks: null,
        ctBuContractPayinfoList: [],
        ctBuContractContent: []
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
      this.title = '添加合同信息录入主';
    },
    /** 修改按钮操作 */
    handleUpdate(row) {

      this.reset();
      this.dialogVisible = true
      const id = row.id || this.ids;
      getContract(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = '修改合同信息录入主';
        this.$refs.payInfo.dataList = this.form.ctBuContractPayinfoList

      });
    },
    /** 提交按钮 */
    submitForm(number) {
      this.form.ctBuContractPayinfoList = this.$refs.payInfo.dataList;
      this.$refs.form.validate(valid => {
        if (valid) {


          if (this.form.id != null && this.form.id.length != 0) {

            updateTicketContract(this.form).then(response => {
              this.msgSuccess('修改成功');
              this.dialogVisible = false
              this.getList();
            });
          } else {
            this.form.status = number

            addContract(this.form).then(response => {

              this.msgSuccess('新增成功');
              this.dialogVisible = false
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除合同信息录入主编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return delContract(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess('删除成功');
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有合同信息录入主数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportContract(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        });
    }
  }
};
</script>
