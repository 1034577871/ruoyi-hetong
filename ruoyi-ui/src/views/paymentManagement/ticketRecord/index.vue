<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="项目编号" prop="proNo">
            <el-input v-model="queryParams.proNo" placeholder="请输入项目编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目名称" prop="proName">
            <el-input v-model="queryParams.proName" placeholder="请输入项目名称" clearable size="small" @keyup.enter.native="handleQuery" />
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
          <el-form-item label="对方单位" prop="incomeType">
            <el-select v-model="queryParams.oppositeName" placeholder="请选择对方单位" clearable size="small">
              <el-option v-for="dict in companyList" :key="dict.companyName" :label="dict.companyName" :value="dict.companyName" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="票据时间" prop="invoiceDate">
            <el-date-picker v-model="invoiceDate" type="daterange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
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
      <!-- <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['paymentManagement:ticketRecord:add']">新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['paymentManagement:ticketRecord:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['paymentManagement:ticketRecord:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['paymentManagement:ticketRecord:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="ticketRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目编号" align="center" prop="projectNo" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="合同编号" align="center" prop="cbcContractNo" />
      <el-table-column label="合同名称" align="center" prop="cbcContractName" />
      <el-table-column label="对方单位名称" align="center" prop="cbcOppositeName" />
      <el-table-column label="开票日期" align="center" prop="invoiceDate" />
      <el-table-column label="发票代码" align="center" prop="invoiceCode" />
      <el-table-column label="发票号码" align="center" prop="invoiceNo" />
      <el-table-column label="发票含税金额" align="center" prop="invoiceMoney" />
      <el-table-column label="发票不含税金额" align="center" prop="invoiceNomoney" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.cbcContractNo != null " size="mini" type="text" icon="el-icon-edit" @click="handleAdd1(scope.row)" v-hasPermi="['paymentManagement:ticketRecord:add']">新增</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-if="scope.row.status==0 || scope.row.status==3"
            v-hasPermi="['paymentManagement:ticketRecord:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-if="scope.row.status>0"
            v-hasPermi="['paymentManagement:ticketRecord:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0" @click="addOrUpdateHandle(scope.row)">审批详情
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0" @click="examineAndApproves(scope.row)">查看流程图</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status==1 || scope.row.status==3"
            @click="exitFlow(scope.row)">终止
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改收票记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-col :span="24" style="margin-top: 0;">
          <el-divider content-position="center">
            <el-link type="primary"><b>合同信息</b></el-link>
          </el-divider>
        </el-col>
        <el-row>
          <!-- <el-col :span="12">
						<el-form-item label="合同编号" prop="contractId">
							<el-input v-model="form.contractId" :readOnly="true" />
						</el-form-item>
					</el-col> -->
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" :readOnly="true" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label-width="180px" label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请选择合同名称" v-on:click.native="showContractSelves = true" />
              <selectContract v-if="showContractSelves" ref="contract" @ok="contractSelves"  :contractCategory='"2"' />
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方单位" prop="oppositeName">
              <el-input v-model="form.oppositeName" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方单位税号" prop="oppositeCreditCode">
              <el-input v-model="form.oppositeCreditCode" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方银行账户" prop="oppositeBank">
              <el-input v-model="form.oppositeBank" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方银行账号" prop="oppositeBankId">
              <el-input v-model="form.oppositeBankId" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方单位联系人" prop="oppositeContacts">
              <el-input v-model="form.oppositeContacts" :readOnly="true" />
            </el-form-item>
          </el-col>
          <!-- 					<el-col :span="12">
						<el-form-item label="对方单位联系电话" prop="oppositePhone">
							<el-input v-model="form.oppositePhone" :readOnly="true" />
						</el-form-item>
					</el-col> -->
          <el-col :span="12">
            <el-form-item label="对方单位联系地址" prop="oppositeAddress">
              <el-input v-model="form.oppositeAddress" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="totalMoney">
              <el-input v-model="form.totalMoney" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已付款金额" prop="paidMoney">
              <el-input v-model="form.paidMoney" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="待付款金额" prop="unpaidMoney">
              <el-input v-model="form.unpaidMoney" :readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前执行进度" prop="progress">
              <el-progress :text-inside="true" :stroke-width="26" :percentage="progress"></el-progress>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top: 0;">
            <el-divider content-position="center">
              <el-link type="primary"><b>收票信息</b></el-link>
            </el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票代码" prop="invoiceCode">
              <el-input v-model="form.invoiceCode" placeholder="请输入发票代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票号码" prop="invoiceNo">
              <el-input v-model="form.invoiceNo" placeholder="请输入发票号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开票日期" prop="invoiceDate">
              <el-date-picker v-model="form.invoiceDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
                placeholder="请选择日期" clearable></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="含税金额" prop="invoiceMoney">
              <el-input v-model="form.invoiceMoney" placeholder="请输入含税金额" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税率" prop="taxrate">
              <el-input v-model="form.taxrate" placeholder="请输入税率" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="不含税金额" prop="invoiceNomoney">
              <el-input v-model="form.invoiceNomoney" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税额" prop="taxAmount">
              <el-input v-model="form.taxAmount" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票据图片" prop="invoiceVoucher">
              <el-upload ref="field101" :file-list="invoiceVoucher" :action="field101Action" :headers="token"
                :on-success="invoiceVoucherSuccess" :on-remove="removeInvoiceVoucher" :auto-upload="autoUploadFalg"
                multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <!-- <div slot="tip" class="el-upload__tip">
					          只能上传jpg/png文件，且不超过500kb
					        </div> -->
              </el-upload>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="其他附件" prop="unitQualification">
              <el-upload ref="field101" :file-list="unitQualification" :action="field101Action" :headers="token"
                :on-success="unitQualificationSuccess" :on-remove="removeUnitQualification" :auto-upload="autoUploadFalg"
                multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <!-- <div slot="tip" class="el-upload__tip">
					          只能上传jpg/png文件，且不超过500kb
					        </div> -->
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-table ref="multipleTable" border v-loading="loading" :data="contractPayInfo" @selection-change="payInfoSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <!-- <el-table-column label="序号" width="50" align="center" prop=""/> -->
          <el-table-column label="预计付款时间" align="center" prop="estimateTime" />
          <el-table-column label="付款比例" width="50" align="center" prop="paymentRatio" />
          <el-table-column label="应付金额" align="center" prop="estimateMoney" />
          <!-- <el-table-column label="实际付款时间" align="center" prop="actualTime" /> -->
          <el-table-column label="发票含税金额" align="center" prop="invoiceMoney" />
          <el-table-column label="发票代码" align="center" prop="invoiceCode" />
          <el-table-column label="发票号码" align="center" prop="invoiceId" />
          <el-table-column label="开具时间" align="center" prop="invoiceTime" />
        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="success" :disabled="isForbidden" @click="submitForm(0)">保 存</el-button>
        <el-button type="primary" :disabled="isForbidden" @click="submitForm(1)">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <ticketRecordInfo v-if="addOrUpdateVisible" ref="ticketRecordInfo"></ticketRecordInfo>
  </div>
</template>

<script>
  import {
    listTicketRecord,
    listTicketRecord1,
    getTicketRecord,
    delTicketRecord,
    addTicketRecord,
    updateTicketRecord,
    exportTicketRecord,
    editRemove
  } from "@/api/paymentManagement/ticketRecord";
  import selectContract from './selectContract.vue';
  import selectContractPayInfo from './selectContractPayInfo.vue';
  import ticketRecordInfo from './ticketRecordInfo.vue';
  import {
    selectCtBuContractPayinfoListConcat,
    listContractPayinfo,
    getContractPayinfo
  } from '../../../api/contract/contractPayinfo.js';
  import {
    getToken
  } from "@/utils/auth";
  import {
    listContract,
    getContract
  } from '../../../api/contract/contractInfo.js';
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById
  } from "@/api/utils";
  import {
    listCompany,
    findAreaList
  } from '../../../api/dictionary/company.js';
  import {
    exitFlow
  } from "@/api/projectApproval/collectProject";
  export default {
    components: {
      selectContract,
      selectContractPayInfo,
      ticketRecordInfo
    },
    name: "TicketRecord",
    data() {
      return {
        addOrUpdateVisible: false,
        multipleSelection: [],
        showContractSelves: false,
        showContractPayInfoSelves: false,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        //提交按钮是否禁用
        isForbidden: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 收票记录表格数据
        ticketRecordList: [],

        contractPayInfo: [],

        invoiceDate: [],
        //对方企业列表
        companyList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        progress: 50, //进度条值
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          proNo: null,
          proName: null,
          contractId: null,
          contractNo: null,
          contractName: null,
          paymentPeriod: null,
          invoiceDate: null,
          invoiceCode: null,
          invoiceNo: null,
          invoiceMoney: null,
          taxrate: null,
          invoiceNomoney: null,
          oppositeName: null,
          oppositeBank: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          invoiceVoucher: null,
          unitQualification: null,

          status: null,
          statusps: null,
        },
        // 查询参数
        queryParams1: {
          pageNum: 1,
          pageSize: 10,
          contractId: null,
          estimateTime: null,
          conditionInfo: null,
          paymentRatio: null,
          estimateMoney: null,
          actualTime: null,
          actualMoney: null,
          invoiceId: null,
          invoiceMoney: null,
          invoiceNomoney: null,
          invoiceCode: null,
          invoiceTime: null,
          remarks: null,
        },
        // 查询参数
        queryParams2: {
          pageNum: 1,
          pageSize: 10,
          companyType: null
        },
        // 表单参数
        form: {
          totalMoney: 0,
          actualMoney: 0,
        },
        // 表单校验
        rules: {
          contractName: [{ required: true, message: '合同名称不能为空', trigger: 'change' }],
          contractNo: [{ required: true, message: '合同编号不能为空', trigger: 'change' }],
          invoiceCode: [{ required: true, message: '请输入发票代码', trigger: 'change' }],
          invoiceNo: [{ required: true, message: '请输入发票号码', trigger: 'change' }],
          invoiceDate: [{ required: true, message: '请选中开票日期', trigger: 'change' }],
          invoiceMoney: [{ required: true, message: '请输入含税金额', trigger: 'change' }],
        },
        token: {},
        field101Action: this.$fieldPathAction,
        invoiceVoucher: [],
        unitQualification: [],
        autoUploadFalg: true,
      };
    },
    created() {
      this.getList();
      this.getCompanyList();
    },
    methods: {
      /** 查询收票记录列表 */
      getList() {
        this.loading = true;
        listTicketRecord1(this.addDateRange(this.queryParams, this.invoiceDate)).then(response => {
          this.ticketRecordList = response.rows;
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
          proNo: null,
          proName: null,
          contractId: null,
          contractNo: null,
          contractName: null,
          payinfoid: null,
          paymentPeriod: null,
          invoiceDate: null,
          invoiceCode: null,
          invoiceNo: null,
          invoiceMoney: null,
          taxrate: null,
          invoiceNomoney: null,
          oppositeName: null,
          oppositeBank: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          invoiceVoucher: null,
          unitQualification: null,
          status: 0,
          statusps: null,
          delFlag: null,
          remark: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          taxAmount: 0,
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
        this.queryParams.oppositeName = "";
        this.invoiceDate = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加收票记录";
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
        this.invoiceVoucher = [];
        this.unitQualification = [];
        this.contractPayInfo = [];
      },
      handleAdd1(row) {
        this.reset();
        const cbcId = row.cbcId
        this.open = true;
        this.title = "添加收票记录";
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
        this.invoiceVoucher = [];
        this.unitQualification = [];
        this.contractPayInfo = [];
        getContract(cbcId).then((response) => {
          console.log(response.data)
          // this.contractCategoryList = response.rows;
          // this.total = response.total;
          // this.loading = false;
          this.form.proNo = response.data.projectNo;
          this.form.proName = response.data.projectName;
          this.form.contractId = response.data.id;
          this.form.contractNo = response.data.contractNo;
          this.form.contractName = response.data.contractName;
          this.form.oppositeName = response.data.oppositeName;
          this.form.oppositeBank = response.data.oppositeBank;
          this.form.oppositeBankId = response.data.oppositeBankId;
          this.form.oppositeContacts = response.data.oppositeContacts;
          this.form.oppositePhone = response.data.oppositePhone;
          this.form.oppositeAddress = response.data.oppositeAddress;
          this.form.taxrate = response.data.taxrate;
          this.form.oppositeCreditCode = response.data.oppositeCreditCode;
          this.form.totalMoney = response.data.totalMoney;
          this.form.paidMoney = response.data.receiveAmount;
          this.form.unpaidMoney = response.data.outstandingAmount;
          this.progress = (this.form.paidMoney / this.form.totalMoney) * 100;

          this.queryParams1.contractId = this.form.contractId;
          selectCtBuContractPayinfoListConcat(this.queryParams1).then((response) => {
            this.contractPayInfo = response.rows;
            console.log(this.contractPayInfo)
            this.total = response.total;
            this.loading = false;
          });
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        console.log(id)
        let deleSta = 0;
        for (let item of id) {
          if (item == null) {
            deleSta = 1;
            break;
          }
        }
        if (deleSta == 1) {
          this.msgSuccess("请修改有收票信息的记录");
          return false;
        }
        getTicketRecord(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改收票记录";
          this.form.taxAmount = this.formatPrice(
            this.form.invoiceMoney - this.form.invoiceNomoney
          );

          this.queryParams1.contractId = this.form.contractId;
          selectCtBuContractPayinfoListConcat(this.queryParams1).then((response) => {
            this.contractPayInfo = response.rows;
            this.total = response.total;
            this.loading = false;

            this.contractPayInfo.map(res => {
              if (this.form.payinfoid == res.id) {
                this.$nextTick(() => {
                  this.$refs.multipleTable.toggleRowSelection(res, true);
                })
              }
            })
          });

        });
        /** 获取文件列表 **/
        findFileListByPidPtype(id, 1).then(response => {
          this.invoiceVoucher = response.data;
        });
        findFileListByPidPtype(id, 2).then(response => {
          this.unitQualification = response.data;
        });
      },
      /** 审批按钮操作 */
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.ticketRecordInfo.init(id);
        })
      },
      /** 提交按钮 */
      submitForm(number) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.isForbidden = true;
            console.log(this.isForbidden)
            this.form.status = number;
            if (this.form.id != null) {
              updateTicketRecord(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
                this.isForbidden = false;
              });
            } else {
              addTicketRecord(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.isForbidden = false;
              });
            }
            console.log(this.isForbidden)
          }
          // this.isForbidden = false;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        let deleSta = 0;
        for (let item of ids) {
          if (item == null) {
            deleSta = 1;
            break;
          }
        }
        if (deleSta == 1) {
          this.msgSuccess("请删除有收票信息的记录");
          return false;
        }
        this.$confirm('是否确认删除收票记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return editRemove(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有收票记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTicketRecord(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
      },
      //选择合同
      contractSelves(id, projectId, projectNo, projectName, contractNo, contractName, oppositeName, oppositeBank,
        oppositeBankId, oppositeContacts, oppositePhone, oppositeAddress, taxrate, oppositeCreditCode, totalMoney,
        receiveAmount, outstandingAmount) {

        if (contractName != "") {
          this.form.proNo = projectNo;
          this.form.proName = projectName;
          this.form.contractId = id;
          this.form.contractNo = contractNo;
          this.form.contractName = contractName;
          this.form.oppositeName = oppositeName;
          this.form.oppositeBank = oppositeBank;
          this.form.oppositeBankId = oppositeBankId;
          this.form.oppositeContacts = oppositeContacts;
          this.form.oppositePhone = oppositePhone;
          this.form.oppositeAddress = oppositeAddress;
          this.form.taxrate = taxrate;
          this.form.oppositeCreditCode = oppositeCreditCode;
          this.form.totalMoney = totalMoney;
          this.form.paidMoney = receiveAmount;
          this.form.unpaidMoney = outstandingAmount;
          this.progress = (this.form.paidMoney / this.form.totalMoney) * 100;
          this.$set(this.form, 'contractName', contractName);
        }
        this.queryParams1.contractId = this.form.contractId;
        selectCtBuContractPayinfoListConcat(this.queryParams1).then((response) => {
          this.contractPayInfo = response.rows;
          console.log(this.contractPayInfo)
          this.total = response.total;
          this.loading = false;
        });

        this.showContractSelves = false
      },
      //选择合同收付款
      // contractPayInfoSelves(id, contractId, estimateTime, conditionInfo, paymentRatio, estimateMoney) {

      // 	if (id != "") {
      // 		this.form.payinfoid = id;
      // 		this.form.paymentPeriod = estimateTime;
      // 		this.$set(this.form, 'conditionInfo', conditionInfo);
      // 	}

      // 	this.showContractPayInfoSelves = false
      // },
      //选择付款周期
      singleElectionPayInfo(row, event, column) {
        this.$refs.multipleTable.toggleRowSelection(row);
        this.form.payinfoid = row.id;
        this.form.paymentPeriod = row.estimateTime;
      },
      //付款周期单选
      payInfoSelectionChange(val) {
        if (val.length > 1) {
          this.$refs.multipleTable.clearSelection()
          this.$refs.multipleTable.toggleRowSelection(val.pop())
          this.form.payinfoid = val[0].id;
          this.form.paymentPeriod = val[0].estimateTime;
        } else {
          this.multipleSelection = val.pop()
          if (this.multipleSelection != null) {
            this.form.payinfoid = this.multipleSelection.id;
            this.form.paymentPeriod = this.multipleSelection.estimateTime;
          } else {
            this.form.payinfoid = null;
            this.form.paymentPeriod = null;
          }
        }
      },
      /** 文件上传检测 **/
      // field101BeforeUpload(file) {
      //   let isRightSize = file.size / 1024 / 1024 < 2;
      //   if (!isRightSize) {
      //     this.$message.error('文件大小超过 2MB');
      //   }
      //   return isRightSize;
      // },
      /** 文件上传检测 **/
      field101BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 20;
        if (!isRightSize) {
          this.$message.error('文件大小超过 20MB');
        }
        let testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
        if (isRightSize) {
          if (testmsg == 'jpg' || testmsg == 'JPG' || testmsg == 'PNG' || testmsg == 'png' || testmsg == 'gif' ||
            testmsg == 'GIF' ||
            testmsg == 'jpeg' || testmsg == 'JPEG' || testmsg == 'pdf' || testmsg == 'PDF') {

          } else {
            isRightSize = false;
            this.$message.error('只允许上传图片和pdf');
          }
        }
        return isRightSize;
        // 只能上传jpg/png/jpeg/gif/pdf文件，且不超过500kb
      },
      /** 上传合同归档资料成功操作 **/
      invoiceVoucherSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.invoiceVoucher) {
          this.form.invoiceVoucher += res.msg + ",";
        } else {
          this.form.invoiceVoucher = res.msg + ",";
        }
      },
      /** 删除合同归档资料操作 **/
      removeInvoiceVoucher(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.invoiceVoucher = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.invoiceVoucher) {
                this.form.invoiceVoucher += element.uid + ",";
              } else {
                this.form.invoiceVoucher = element.uid + ",";
              }
            }
          }
        });
      },
      /** 上传合同归档资料成功操作 **/
      unitQualificationSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.unitQualification) {
          this.form.unitQualification += res.msg + ",";
        } else {
          this.form.unitQualification = res.msg + ",";
        }
      },
      /** 删除合同归档资料操作 **/
      removeUnitQualification(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.unitQualification = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.unitQualification) {
                this.form.unitQualification += element.uid + ",";
              } else {
                this.form.unitQualification = element.uid + ",";
              }
            }
          }
        });
      },
      handlePictureCardPreview(file) { //点击放大图片
        if ((file.url).indexOf("profile") >= 0) {
          let files = (file.url).split("profile");
          let url = window.location.hostname;
          if (url.indexOf("http") < 0) {
            url = "http://" + url;
          }
          var url_ = url + ":9001/profile" + files[1];
          window.open(url + ":9001/dictionary/fileManage/dwonLoadFile/" + file.uid + '/' + file.ptype);
        }
      },
      /** 格式化金额 **/
      formatPrice(value) {
        let tempVal = parseFloat(value).toFixed(3);
        let realVal = tempVal.substring(0, tempVal.length - 1);
        return realVal;
      },
      changeNum() {
        let invoiceMoney = this.form.invoiceMoney;
        let taxrate = this.form.taxrate;
        if (invoiceMoney && taxrate) {
          /** 税率转换 **/
          let taxrateStr = taxrate / 100;
          this.form.invoiceNomoney = this.formatPrice(
            invoiceMoney / (1 + taxrateStr)
          );
          this.form.taxAmount = this.formatPrice(
            invoiceMoney - this.form.invoiceNomoney
          );
        }
      },

      /** 查询企业信息列表 */
      getCompanyList() {
        this.queryParams2.companyType = 2;
        listCompany(this.queryParams2).then((response) => {
          this.companyList = response.rows;
        });
      },
      /** 查看流程图 */
      examineAndApproves(row) {
        this.reset();
        const id = row.id || this.ids
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
          exitFlow(instanceId, 'ticketRecordTermination').then(response => {
            this.getList();
            this.msgSuccess("终止成功");
          });
        })
      },
    }
  };
</script>
<style scoped lang="scss">
  ::v-deep .el-dialog {
    .el-dialog__body {
      padding: 0px 20px !important;
    }
  }
</style>
