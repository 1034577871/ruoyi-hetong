<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <!-- <el-col :span="6">
          <el-form-item label="项目类别" prop="projectCategory">
            <el-select v-model="queryParams.projectCategory" placeholder="请选择项目类别" clearable size="small">
              <el-option v-for="dict in projectCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="6">
          <el-form-item label="项目编号" prop="projectNo">
            <el-input v-model="queryParams.projectNo" placeholder="请输入项目编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="queryParams.contractName" placeholder="请输入合同名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">

        <el-col :span="6">
          <el-form-item label="合同类别" prop="contractCategory">
            <el-select v-model="queryParams.contractCategory" placeholder="请选择合同类别" clearable size="small">
              <el-option v-for="dict in contractCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="付款方式" prop="payMethod">
            <el-select v-model="queryParams.payMethod" placeholder="请选择付款方式" clearable size="small">
              <el-option v-for="dict in payMethodList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="6">
          <el-form-item label="收入类型" prop="incomeType">
            <el-select v-model="queryParams.incomeType" placeholder="请选择收入类型" clearable size="small">
              <el-option v-for="dict in incomeTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="6">
          <el-form-item label="对方单位" prop="oppositeName">
            <el-input v-model="queryParams.oppositeName" placeholder="请输入对方单位名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="我方单位" prop="ourName">
            <el-input v-model="queryParams.ourName" placeholder="请输入我方单位名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">


        <el-col :span="8">
          <el-form-item label="合同签订日期" label-width="100px">
            <el-date-picker v-model="starttime" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
              range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="合同截至日期" label-width="100px">
            <el-date-picker v-model="endtime0" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
              range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="服务开始日期" label-width="100px">
            <el-date-picker v-model="contractServeStart" size="small" style="width: 240px" value-format="yyyy-MM-dd"
              type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="8">
          <el-form-item label="服务截至日期" label-width="100px">
            <el-date-picker v-model="contractServeEnd" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
              range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">


      </el-row>

    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['contract:contractPayinfo:add']">新增
        </el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['contract:contractPayinfo:edit']">编辑
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['contract:contractPayinfo:remove']">删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['paymentManagement:payinfo:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-dialog title="新增信息" v-if='dialogVisible' :close-on-click-modal="false" :visible.sync="dialogVisible" width="80%">
      <el-form ref="form" :model="form">
        <!-- <infoAdd ref='infoAdd'></infoAdd> -->
        <el-tabs v-model="activeName" v-on:tab-click="onTabClick()">
          <el-tab-pane label="基本信息" name="first">
            <basicInfo ref="basicInfo" :project="project"></basicInfo>
          </el-tab-pane>
          <el-tab-pane label="合同内容" name="second">
            <contentInfo ref="contentInfo"></contentInfo>
          </el-tab-pane>
          <el-tab-pane label="收/付款信息" name="third">
            <payInfo ref="payInfo"></payInfo>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="success" :disabled="isForbidden" @click="submitForm(0)">保 存</el-button>
        <el-button type="primary" :disabled="isForbidden" @click="submitForm(1)">提 交</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <el-dialog title="变更信息" v-if='dialogVisible1' :visible.sync="dialogVisible1" width="80%">
      <el-form ref="form" :model="form">
        <!-- <infoAdd ref='infoAdd'></infoAdd> -->
        <el-tabs v-model="activeName" v-on:tab-click="onTabClick()">
          <el-tab-pane label="基本信息" name="first">
            <basicInfo ref="basicInfo"></basicInfo>
          </el-tab-pane>
          <el-tab-pane label="合同内容" name="second">
            <contentInfo ref="contentInfo"></contentInfo>
          </el-tab-pane>
          <el-tab-pane label="收付款信息" name="third">
            <payInfo ref="payInfo"></payInfo>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="success" :disabled="isForbidden" @click="submitForm(0)">保 存</el-button>
        <el-button type="primary" :disabled="isForbidden" @click="submitForm(1)">提 交</el-button>
        <el-button @click="dialogVisible1 = false">取 消</el-button>
      </span>
    </el-dialog>

    <el-table v-loading="loading" ref="itsmDataTable" :data="contractList" @selection-change="handleSelectionChange"
      border>
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="备注信息" align="center" prop="id" /> -->
      <el-table-column label="项目编号" align="center" prop="cbanReqNo"  />
      <el-table-column label="项目名称" align="center" prop="cbanProName" />
      <!-- <el-table-column label="项目类别" align="center" prop="projectCategory" /> -->
      <el-table-column label="项目类别" align="center" prop="cbanProType" >
        <template v-slot="scope">
          <p v-if="scope.row.cbanProType == '9f30ebd46d3c428cbc5e54f7175eb4f2' ">集客项目</p>
          <p v-if="scope.row.cbanProType != '9f30ebd46d3c428cbc5e54f7175eb4f2'">非集客项目</p>
        </template>
      </el-table-column>
      <el-table-column label="合同编号" align="center" prop="contractNo"  />
      <!-- <el-table-column label="项目ID" align="center" prop="projectId" /> -->
      <el-table-column label="合同名称" align="center" prop="contractName"  />
      <el-table-column label="合同类别" align="center" prop="contractCategoryName"  />
      <!-- <el-table-column label="收入类型" align="center" prop="incomeType">
        <template v-slot="scope">
          <p v-if="scope.row.incomeType == 1">一次性</p>
          <p v-if="scope.row.incomeType == 2">分期</p>
        </template>
      </el-table-column> -->
      <el-table-column label="对方单位名称" align="center" prop="oppositeName" width="100"  />
      <el-table-column label="我方单位名称" align="center" prop="ourName" width="100"  />
      <el-table-column label="合同签订日期" align="center" prop="starttime" width="100" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="合同截止日期" align="center" prop="endtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同服务开始日期" align="center" prop="contractServeStart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractServeStart, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="服务期限" align="center" prop="serviceTerm" /> -->
      <!-- <el-table-column label="合同服务截止日期" align="center" prop="contractServeEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractServeEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="合同金额(元)" align="center" prop="totalMoney" width="90"  />
      <el-table-column label="税率(%)" align="center" prop="taxrate" />
      <el-table-column label="不含税金额(元)" align="center" prop="notTaxMoney" width="90"  />
      <el-table-column label="付款方式" align="center" prop="payMethodName" />
      <el-table-column label="经办部门" align="center" prop="manageDeptmentName"  />
      <el-table-column label="经办人" align="center" prop="manageUserName"  />
      <el-table-column label="状态说明" align="center" prop="statusPs"  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.cbanProName != null " size="mini" type="text" icon="el-icon-edit" @click="handleAdd1(scope.row)" v-hasPermi="['contract:contractPayinfo:add']">新增
          </el-button>
          <el-button v-if="
            scope.row.status==0 || scope.row.status==3" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button v-if="scope.row.status==2" size="mini" type="text" icon="el-icon-edit" @click="handleAlter(scope.row)">变更
          </el-button>
          <el-button v-if="scope.row.status>0" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0" @click="addOrUpdateHandle(scope.row)">审批详情
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0 && scope.row.instanceId != null" @click="examineAndApproves(scope.row)">查看流程图
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status==1 || scope.row.status==3"
            @click="exitFlow(scope.row)">终止
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <contract v-if="addOrUpdateVisible" ref="contract"></contract>
  </div>

</template>

<script>
  import {
    listContract,
    listContract1,
    getContract,
    delContract,
    addContract,
    updateContract,
    exportContract
  } from '@/api/contract/contractInfo';

  import basicInfo from './basicInfo.vue';
  import contentInfo from './contentInfo.vue';
  import payInfo from './payInfo.vue';
  import {
    getToken
  } from "@/utils/auth";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById,
    findLoginbyCompany
  } from "@/api/utils";
  import contract from "./contractInfo.vue";
  import {
    getUserInfo
  } from "@/api/collection/common";
  import {
    exitFlow
  } from "@/api/projectApproval/collectProject";

  export default {
    name: 'Contract',
    components: {
      basicInfo,
      contentInfo,
      payInfo,
      contract
    },

    data() {
      return {
        //提交按钮是否禁用
        isForbidden: true,
        project: {},
        addOrUpdateVisible: false,
        activeName: 'first',
        formData: [],
        dialogVisible: false,
        dialogVisible1: false,
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
        showSearch: false,
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
        endtime0: [],
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
          // endtime: null,
          contractServeStart: null,
          serviceTerm: null,
          contractServeEnd: null,
          totalMoney: null,
          taxrate: null,
          notTaxMoney: null,
          payMethod: null,
          archiveFiles: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: null,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          remarks: null,
          sum: '', //统计
          // 日期范围
          // starttime: [],
          endtime1: '',
          endtime2: '',
          contractServeStart1: '',
          contractServeStart2: '',
          contractServeEnd1: '',
          contractServeEnd2: '',

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
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: 0,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          delFlag: null,
          remarks: null,
          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
          ctBuContractIncomeTypeList: [],
          field101fileList: [],
          pid: null,
          ptype: null
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
      //合同标的额传值给第三页面的总额
      onTabClick() {
        this.$refs.payInfo.number = this.$refs.basicInfo.formData.totalMoney;
      },

      /** 查询合同信息录入主列表 */
      getList() {
        this.loading = true;
        listContract1(this.addDateRange(this.queryParams, this.starttime)).then(response => {
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
        this.isForbidden = false;
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
          manageCompany: null,
          manageCompanyName: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: 0,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          delFlag: null,
          remarks: null,
          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
          contentInfo: null,
          payInfo: null,

        };
        this.resetForm('form');
      },
      /** 搜索按钮操作 */
      handleQuery() {
        let startDate = this.endtime0.toString();
        this.queryParams.endtime1 = startDate.substring(0, 10);
        this.queryParams.endtime2 = startDate.substring(11);
        let startDate1 = this.contractServeStart.toString();
        this.queryParams.contractServeStart1 = startDate1.substring(0, 10);
        this.queryParams.contractServeStart2 = startDate1.substring(11);
        let startDate2 = this.contractServeEnd.toString();
        this.queryParams.contractServeEnd1 = startDate2.substring(0, 10);
        this.queryParams.contractServeEnd2 = startDate2.substring(11);
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.starttime = [];
        this.endtime0 = [];
        this.contractServeStart = [];
        this.contractServeEnd = [];
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
        this.dialogVisible = true
        this.reset();
        this.open = true;
        this.title = '添加合同信息录入主';
        /** 加入请求头 **/
        // this.$refs.basicInfo.token = { Authorization: getToken() };

        // this.$refs.contentInfo.dataList = [];
        // this.$refs.payInfo.dataList = [];
        // this.$refs.basicInfo.archiveFiles = [];
        // this.$refs.basicInfo.contractScanningFile = [];
        // this.$refs.basicInfo.contractApprovalFile = [];
        // this.$refs.basicInfo.otherFiles = [];
        getUserInfo().then(response => {
          this.form.manageDeptment = response.data.user.dept.deptId;
          this.form.manageDeptmentName = response.data.user.dept.deptName;
          this.form.manageUser = response.data.user.userId;
          this.form.manageUserName = response.data.user.nickName;
          this.form.manageCompany = response.data.user.companyId;
        });
        findLoginbyCompany().then(response => {
          this.form.manageCompanyName = response.msg;
        });
        // this.$refs.basicInfo.formData = this.form;

      },

      handleAdd1(row) {
        this.dialogVisible = true
        this.reset();
        this.open = true;
        this.title = '添加合同信息录入主';
        /** 加入请求头 **/
        // this.$refs.basicInfo.token = { Authorization: getToken() };
        row.isOldCon = 2;
        this.project = row;
        getUserInfo().then(response => {
          this.form.manageDeptment = response.data.user.dept.deptId;
          this.form.manageDeptmentName = response.data.user.dept.deptName;
          this.form.manageUser = response.data.user.userId;
          this.form.manageUserName = response.data.user.nickName;
          this.form.manageCompany = response.data.user.companyId;
        });
        findLoginbyCompany().then(response => {
          this.form.manageCompanyName = response.msg;
        });
        setTimeout(()=>{
           this.$refs.basicInfo.loadMore();
          },10)

      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        const id = row.id || this.ids;
        let deleSta1 = 0;
        for (let item of id) {
          if (item == null) {
            deleSta1 = 1;
            break;
          }
        }
        if (deleSta1 == 1) {
          this.msgSuccess("请修改有合同信息的记录");
          return false;
        }
        const _selectData = this.$refs.itsmDataTable.selection
        let deleSta = 0;
        if (_selectData != '') {
          const flag = false;
          for (let item of _selectData) {
            if (item.status == 1 || item.status == 2) {
              deleSta = 1;
              break;
            }
          }
          if (deleSta != 0) {
            this.msgSuccess("请修改未提交的数据");
            return false;
          }
        }
        this.reset();
        this.dialogVisible = true;

        getContract(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = '修改合同信息录入主';
          this.form.projectCategory = this.form.projectCategory + '';
          // this.form.incomeType = this.form.incomeType + '';
          /** 加入请求头 **/
          this.$refs.basicInfo.token = {
            Authorization: getToken()
          };
          this.$refs.basicInfo.formData = this.form;
          this.$refs.basicInfo.formData.contractCategory = this.form.contractCategory;
          this.$set(this.$refs.basicInfo.formData, 'dataList1', [])
          this.$refs.basicInfo.formData.dataList1 = this.form.ctBuContractIncomeTypeList;
          this.$refs.contentInfo.dataList = this.form.ctBuContractContentList;
          this.$refs.payInfo.dataList = this.form.ctBuContractPayinfoList;


        });
        /** 获取文件列表 **/
        findFileListByPidPtype(id, 1).then(response => {
          this.$refs.basicInfo.archiveFiles = response.data;
        });
        findFileListByPidPtype(id, 2).then(response => {
          this.$refs.basicInfo.contractScanningFile = response.data;
        });
        findFileListByPidPtype(id, 3).then(response => {
          this.$refs.basicInfo.contractApprovalFile = response.data;
        });
        findFileListByPidPtype(id, 4).then(response => {
          this.$refs.basicInfo.otherFiles = response.data;
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有付款申请单主数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPayinfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
      },

      /** 变更按钮操作 */
      handleAlter(row) {
        const id = row.id || this.ids;

        this.reset();
        this.dialogVisible1 = true;

        getContract(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = '变更合同信息录入主';
          this.form.projectCategory = this.form.projectCategory + '';
          this.form.incomeType = this.form.incomeType + '';
          /** 加入请求头 **/
          this.$refs.basicInfo.token = {
            Authorization: getToken()
          };
          this.$refs.basicInfo.formData = this.form;
          this.$set(this.$refs.basicInfo.formData, 'dataList1', [])
          this.$refs.basicInfo.formData.dataList1 = this.form.ctBuContractIncomeTypeList;
          this.$refs.contentInfo.dataList = this.form.ctBuContractContentList;
          this.$refs.payInfo.dataList = this.form.ctBuContractPayinfoList;
          this.form.originalId = this.form.id;
          this.form.id = null;

        });
        /** 获取文件列表 **/
        findFileListByPidPtype(id, 1).then(response => {
          this.$refs.basicInfo.archiveFiles = response.data;
        });
        findFileListByPidPtype(id, 2).then(response => {
          this.$refs.basicInfo.contractScanningFile = response.data;
        });
        findFileListByPidPtype(id, 3).then(response => {
          this.$refs.basicInfo.contractApprovalFile = response.data;
        });
        findFileListByPidPtype(id, 4).then(response => {
          this.$refs.basicInfo.otherFiles = response.data;
        });
      },
      /** 提交按钮 */
      submitForm(number) {
        this.isForbidden = true;
        this.form = this.$refs.basicInfo.formData;
        this.form.ctBuContractContentList = this.$refs.contentInfo.dataList;
        this.form.ctBuContractPayinfoList = this.$refs.payInfo.dataList;
        this.form.ctBuContractIncomeTypeList = this.$refs.basicInfo.formData.dataList1;
        // if(this.$refs.basicInfo.submitForm() ){

        // } else{
        //   return false
        // }
        if(this.form.isOldCon == null){
          this.msgSuccess("请选择是否老合同");
          this.isForbidden = false;
          return false;
        }
        if(this.form.contractNo1 == null || this.form.contractNo2 == null || this.form.contractNo3 == null ||this.form.contractNo4 == null){
          this.msgSuccess("请完善合同编号");
          this.isForbidden = false;
          return false;
        }
        this.form.contractNo = this.form.contractNo1.concat(this.form.contractNo2,this.form.contractNo3,this.form.contractNo4);
        // if(this.form.contractNo == null){
        //   this.msgSuccess("请输入合同编号");
        //   this.isForbidden = false;
        //   return false;
        // }
        if(this.form.contractName == null){
          this.msgSuccess("请输入合同名称");
          this.isForbidden = false;
          return false;
        }
        if(this.form.contractCategory == null){
          this.msgSuccess("请选择合同类别");
          this.isForbidden = false;
          return false;
        }
        if (this.form.ctBuContractIncomeTypeList == null || this.form.ctBuContractIncomeTypeList.length == 0) {
          // this.form.ctBuContractIncomeTypeList = this.$refs.basicInfo.formData.dataList1;
          this.msgSuccess("请完善收支类型与类型金额");
            this.isForbidden = false;
            return false;
        }

        if(this.form.contractNo == null){
          this.msgSuccess("请填写合同编号");
          this.isForbidden = false;
          return false;
        }
        if(this.form.contractName == null){
          this.msgSuccess("请填写合同名称");
          this.isForbidden = false;
          return false;
        }
        if (this.form.contractCategory == "1") {
          if(this.form.ourBank == null || this.form.ourBankId == null){
            this.msgSuccess("请完善我方单位信息");
            this.isForbidden = false;
            return false;
          }

        }
        if (this.form.contractCategory == "2") {
          this.form.incomeType = null;
          if(this.form.oppositeBank == null || this.form.oppositeBankId == null){
            this.msgSuccess("请完善对方单位信息");
            this.isForbidden = false;
            return false;
          }
        }

        if (this.form.contractCategory == "3") {
          this.form.incomeType = null;
          if(this.form.oppositeBank == null || this.form.oppositeBankId == null || this.form.ourBank == null || this.form.ourBankId == null){
            this.msgSuccess("请完善双方单位信息");
            this.isForbidden = false;
            return false;
          }
        }
        if(this.form.ourContacts == null){
          this.msgSuccess("请填写我方单位联系人");
          this.isForbidden = false;
          return false;
        }
        if(this.form.ourPhone == null){
          this.msgSuccess("请填写我方单位联系方式");
          this.isForbidden = false;
          return false;
        }
        if(this.form.starttime == null){
          this.msgSuccess("选择合同签订日期");
          this.isForbidden = false;
          return false;
        }
        if(this.form.endtime == null){
          this.msgSuccess("请选择合同截止日期");
          this.isForbidden = false;
          return false;
        }
        if(this.form.contractServeStart == null){
          this.msgSuccess("选择合同服务开始日期");
          this.isForbidden = false;
          return false;
        }
        if(this.form.serviceTerm == null){
          this.msgSuccess("请填写服务期限");
          this.isForbidden = false;
          return false;
        }
        if(this.form.taxrate == null){
          this.msgSuccess("请填写合同税率");
          this.isForbidden = false;
          return false;
        }


        // if (this.form.contractCategory == "2") {
        //   this.form.incomeType = null;
        // }
        if(this.form.isVirtual == 2){
          let estimateMoneySum = 0;
          if (this.form.ctBuContractPayinfoList != null && this.form.ctBuContractPayinfoList.length > 0) {
            for (let item of this.form.ctBuContractPayinfoList) {
              estimateMoneySum = (estimateMoneySum) + parseFloat(item.estimateMoney);
            }
          }
          if (this.form.totalMoney != estimateMoneySum) {
            this.msgSuccess("合同金额与应收/付款金额不一致，请修改后提交保存");
            this.isForbidden = false;
            return false;
          }
          let intaxAmountSum = 0;
          if (this.form.ctBuContractContentList != null && this.form.ctBuContractContentList.length > 0) {
            for (let item of this.form.ctBuContractContentList) {
              intaxAmountSum = (intaxAmountSum) + parseFloat(item.intaxAmount);
            }
          }
          if (this.form.totalMoney != intaxAmountSum) {
            this.msgSuccess("合同金额与合同内容金额不一致，请修改后提交保存");
            this.isForbidden = false;
            return false;
          }
          if(this.form.id == null || this.form.id == ""){
            if(this.form.archiveFiles == null){
              this.msgSuccess("请上传合同归档资料");
              this.isForbidden = false;
              return false;
            }
            if(this.form.contractScanningFile == null){
              this.msgSuccess("请上传合同扫描件");
              this.isForbidden = false;
              return false;
            }
            if(this.form.contractApprovalFile == null){
              this.msgSuccess("请上传合同签订审批流程附件");
              this.isForbidden = false;
              return false;
            }
          }

          if(this.form.payMethod == null){
            this.msgSuccess("请选择付款方式");
            this.isForbidden = false;
            return false;
          }

        }

        // if (this.form.starttime > this.form.contractServeStart) {
        //   this.msgSuccess("合同服务开始日期不得早于合同签订日期，请修改后再提交保存");
        //   this.isForbidden = false;
        //   return false;
        // }

        this.$refs.form.validate(valid => {
          if (valid) {
            this.isForbidden = true;
            this.form.status = number;
            if (this.form.id != null && this.form.id.length != 0) {
              updateContract(this.form).then(response => {
                this.msgSuccess('修改成功');
                this.dialogVisible = false
                this.open = false;
                this.isForbidden = false;
                this.getList();
              });
            } else {
              addContract(this.form).then(response => {
                this.msgSuccess('新增成功');
                this.dialogVisible = false
                this.open = false;
                this.isForbidden = false;
                this.getList();
              });
            }
          }
        });


      },


      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        let deleSta1 = 0;
        for (let item of ids) {
          if (item == null) {
            deleSta1 = 1;
            break;
          }
        }
        if (deleSta1 == 1) {
          this.msgSuccess("请删除有合同信息的记录");
          return false;
        }
        const _selectData = this.$refs.itsmDataTable.selection
        let deleSta = 0;
        if (_selectData != '') {
          const flag = false;
          for (let item of _selectData) {
            if (item.status > 0) {
              deleSta = 1;
              break;
            }
          }
          if (deleSta == 1) {
            this.msgSuccess("请删除未提交的数据");
            return false;
          }
        }
        this.$confirm('是否确认删除合同信息?', '警告', {
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
      },
      /** 审批按钮操作 */
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.contract.init(id);
        })
      },
      /** 查看流程图 */
      examineAndApproves(row) {
        this.reset();
        let url = window.location.hostname;
        if (url.indexOf("http") < 0) {
          url = "http://" + url;
        }
        let hosturl = url + ":9001/task/exportImage?processInstanceId=" + row.instanceId;
        window.open(hosturl, "_blank");
      },
      //流程终止
      exitFlow(row) {
        let instanceId = row.instanceId;
        this.$confirm('是否确认终止本条数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then((obj) => {
          //exitFlow(instanceId);
          exitFlow(instanceId, 'contractTermination').then(response => {
            this.getList();
            this.msgSuccess("终止成功");
          });
        })
      },


    }
  };
</script>
<style scoped lang="scss">
  .el-dialog__body {
    padding: 0px 20px;
  }
</style>
