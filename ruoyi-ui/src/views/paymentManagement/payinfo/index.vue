<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="项目编号" prop="proNo">
            <el-input v-model="queryParams.proNo" placeholder="请输入项目编号" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目名称" prop="proName">
            <el-input v-model="queryParams.proName" placeholder="请输入项目名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable size="small"
              @keyup.enter.native="handleQuery" />
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
          <el-form-item label="申请状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
              <el-option label="保存" value="0" />
              <el-option label="提交" value="1" />
              <el-option label="审核通过" value="2" />
              <el-option label="审核驳回" value="3" />
              <el-option label="已核销" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="申请时间" prop="applicationTime">
            <el-date-picker v-model="applicationTime" type="daterange" range-separator="至" start-placeholder="开始日期"
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
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['paymentManagement:payinfo:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['paymentManagement:payinfo:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['paymentManagement:payinfo:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['paymentManagement:payinfo:export']">导出</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handlePaymentWriteOff"
          v-hasPermi="['paymentManagement:payinfo:paymentWriteOff']"
        >核销</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="payinfoList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" width="55" align="center" type="index" />
      <el-table-column label="项目编号" align="center" prop="projectNo" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="合同编号" align="center" prop="cbcContractNo" />
      <el-table-column label="合同名称" align="center" prop="cbcContractName" />
      <el-table-column label="对方单位" align="center" prop="cbcOppositeName" />
      <el-table-column label="申请金额" align="center" prop="appliedAmount" />
      <el-table-column label="申请日期" align="center" prop="applicationTime" />
      <el-table-column label="付款类型" align="center" prop="paytype">
        <template v-slot="scope">
          <p v-if="scope.row.paytype == 1">发票付款</p>
          <p v-if="scope.row.paytype == 2">预付款</p>
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="applicant" />
      <el-table-column label="当前状态" align="center" prop="statusps" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.cbcContractNo != null" size="mini" type="text" icon="el-icon-edit"
            @click="handleAdd1(scope.row)" v-hasPermi="['paymentManagement:ticketRecord:add']">新增</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-if="scope.row.status==0 || scope.row.status==3" v-hasPermi="['paymentManagement:ticketRecord:edit']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-if="scope.row.status>0" v-hasPermi="['paymentManagement:ticketRecord:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0"
            @click="addOrUpdateHandle(scope.row)">审批详情
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0"
            @click="examineAndApproves(scope.row)">查看流程图</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status==1 || scope.row.status==3"
            @click="exitFlow(scope.row)">终止
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改付款申请单主对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form :model="form" ref="form" label-width="90px">
        <el-row :gutter="15">
          <!-- <el-col :span="8">
            <el-form-item  label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请选择合同" v-on:click.native="showContractSelves = true" />
              <selectContract v-if="showContractSelves" ref="contract" @ok="contractSelves"  :contractCategory='"2"' />
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="合同名称" clearable size="small"
                @keyup.enter.native="handleQuery" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" placeholder="合同编号" clearable size="small"
                @keyup.enter.native="handleQuery" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="项目名称" prop="proName">
              <el-input v-model="form.proName" placeholder="项目名称" clearable size="small"
                @keyup.enter.native="handleQuery" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目编号" prop="proNo">
              <el-input v-model="form.proNo" placeholder="项目编号" clearable size="small" @keyup.enter.native="handleQuery"
                :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-divider content-position="center">
        <el-link type="primary"><b>申请信息</b></el-link>
      </el-divider>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="申请人" prop="applicant">
              <el-input v-model="form.applicant" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请日期" prop="applicationTime">
              <el-date-picker :style="{width:'100%'}" v-model="form.applicationTime" type="date" :disabled="true">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="已申请金额" prop="haveAppliedAmount">
              <el-input v-model="form.haveAppliedAmount" placeholder="请输入" :disabled="true" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="付款类型" prop="paytype">
              <el-select v-model="form.paytype" @change="payTypeChange" :style="{width:'100%'}">
                <el-option value="1" label="发票付款">发票付款</el-option>
                <el-option value="2" label="预付款">预付款</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请金额" prop="appliedAmount">
              <el-input v-model="form.appliedAmount" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="附件" prop="imageData">
              <el-upload ref="field101" :file-list="imageData" :action="field101Action" :headers="token"
                :on-success="imageDataSuccess" :on-remove="removeImageData" :auto-upload="autoUploadFalg" multiple
                :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="billInfoList" ref="billInfo" @selection-change="handleSelectionChange1" v-if="billInfoVisible"
          border>
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="序号" align="center" type="index" width="50" />
          <el-table-column label="项目编号" align="center" prop="proNo" />
          <el-table-column label="项目名称" align="center" prop="proName" />
          <el-table-column label="合同编号" align="center" prop="contractNo" />
          <el-table-column label="合同名称" align="center" prop="contractName" />
          <el-table-column label="开票日期" align="center" prop="invoiceDate" />
          <el-table-column label="发票代码" align="center" prop="invoiceCode" />
          <el-table-column label="发票号码" align="center" prop="invoiceNo" />
          <el-table-column label="发票金额(含税)" align="center" prop="invoiceMoney" />
          <el-table-column label="发票金额(不含税)" align="center" prop="invoiceNomoney" />
        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <div slot="footer" class="dialog-footer">
          <el-button type="success" :disabled="isForbidden" @click="submitForm(0)">保 存</el-button>
          <el-button type="primary" :disabled="isForbidden" @click="submitForm(1)">提 交</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open1" width="80%" append-to-body>
      <el-form :model="form" ref="form" label-width="90px">
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="付款人">
              <el-input v-model="form.payer" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款日期">
              <el-date-picker :style="{width:'100%'}" v-model="form.paymentTime" type="date">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款凭证" prop="paymentEvidence">
              <el-upload ref="field101" :file-list="paymentEvidence" :action="field101Action" :headers="token"
                :on-success="paymentEvidenceSuccess" :on-remove="removePaymentEvidence" :auto-upload="autoUploadFalg"
                multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <div slot="footer" class="dialog-footer">
          <el-button type="success" @click="submitForm1">核销</el-button>
          <el-button @click="cancel1">取 消</el-button>
        </div>
      </div>
    </el-dialog>
    <payInfo v-if="addOrUpdateVisible" ref="payInfo"></payInfo>
  </div>
</template>

<script>
  import {
    listPayinfo,
    listPayinfo1,
    getPayinfo,
    delPayinfo,
    addPayinfo,
    updatePayinfo,
    exportPayinfo,
    editRemovePayinfo,
    paymentWriteOff,
    countAppliedAmountByContractIdPayment
  } from "@/api/paymentManagement/payinfo";
  import {
    selectCtBuContractPayinfoListConcat,
    listContractPayinfo,
    getContractPayinfo
  } from '../../../api/contract/contractPayinfo.js';
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPidPtype,
    downloadfileById
  } from "@/api/utils";
  import selectContract from '../ticketRecord/selectContract.vue';
  import {
    listContract,
    getContract
  } from '../../../api/contract/contractInfo.js';
  import payInfo from './payInfo.vue';
  import {
    getUserInfo
  } from "@/api/collection/common";
  import {
    getToken
  } from "@/utils/auth";
  import {
    exitFlow
  } from "@/api/projectApproval/collectProject";
  import {
    listTicketRecord,
    getTicketRecord,
    delTicketRecord,
    addTicketRecord,
    updateTicketRecord,
    exportTicketRecord
  } from "@/api/paymentManagement/ticketRecord";
  export default {
    components: {
      selectContract,
      payInfo
    },
    name: "Payinfo",
    data() {
      return {
        addOrUpdateVisible: false,
        showContractSelves: false,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        invoiceMoneyList: [],
        invoiceMoneys: 0,
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        //提交按钮是否禁用
        isForbidden: true,
        payinfoList: [], // 付款申请单主表格数据
        billInfoList: [], //发票付款信息表格

        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        open1: false,
        applicationTime: [],
        aa: "",



        // 查询参数
        queryParams1: {
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

        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          proNo: null,
          proName: null,
          contractId: null,
          contractNo: null,
          contractName: null,
          payMoney: null,
          ntaxMoney: null,
          taxMoney: null,
          createTime: null,
          oppositeName: null,
          oppositeBank: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          imageData: null,
          status: null,
          statusps: null,
          writeOffUserid: null,
          writeOffUsername: null,
          instanceId: null
        },
        // addQueryParams: {
        //   proNo: null,
        //   proName: null,
        //   contractNo: null,
        //   contractName: null,
        // },
        // addForm:{
        //   createBy:null,
        //   createTime:new Date(),
        //   payMoney:null,
        //   remark:null,
        //   appendixFiles:null,
        //   payType:null,
        //   //payType: [{value:'1',label:'预付款'},{value:'2',label:'发票付款'}],
        // },
        billInfoVisible: false,
        field101Action: this.$fieldPathAction,
        imageData: [],
        paymentEvidence: [],
        autoUploadFalg: true,
        // 表单参数
        form: {
          applicant: null,
          ticketRecordList: [],
          appliedAmount: "",
          haveAppliedAmount: null,
        },
        token: {},
        // 表单校验
        rules: {
          contractNo: [{
            required: true,
            message: '合同编号不能为空',
            trigger: 'change'
          }],
          contractName: [{
            required: true,
            message: '合同名称不能为空',
            trigger: 'change'
          }],
          appliedAmount: [{
            required: true,
            message: '请输入申请金额',
            trigger: 'change'
          }],
          paytype: [{
            required: true,
            message: '请选择付款类型',
            trigger: 'change'
          }],

        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询付款申请单主列表 */
      getList() {
        this.loading = true;
        listPayinfo1(this.addDateRange(this.queryParams, this.applicationTime)).then(response => {
          this.payinfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 取消按钮
      cancel1() {
        this.open1 = false;
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
          paymoney: null,
          ntaxmoney: null,
          taxmoney: null,
          oppositeName: null,
          oppositeBank: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          imageData: null,
          status: 0,
          statusps: null,
          delFlag: null,
          remark: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          writeOffUserid: null,
          writeOffUsername: null,
          instanceId: null,
          applicationTime: null,
          payer: null
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
        this.applicationTime = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      // 收票记录多选框选中数据
      handleSelectionChange1(selection) {
        this.invoiceMoneys = 0;
        this.ids = selection.map(item => item.id)
        this.invoiceMoneyList = selection.map(item => item.invoiceMoney)
        if (this.invoiceMoneyList != null && this.invoiceMoneyList.length > 0) {
          for (let invoice of this.invoiceMoneyList) {
            this.invoiceMoneys = this.invoiceMoneys + parseFloat(invoice)
          }
        }
        this.$set(this.form, 'appliedAmount', this.invoiceMoneys)
        // this.form.appliedAmount = this.invoiceMoneys;
        this.aa = this.invoiceMoneys;

      },
      /** 审批按钮操作 */
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.payInfo.init(id);
        })
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加付款申请单";
        this.imageData = [];
        getUserInfo().then(response => {
          this.form.applicant = response.data.user.nickName;
          this.form.applicationTime = new Date();
          this.billInfoList = [];
          this.billInfoVisible = false;
        });
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
      },
      handleAdd1(row) {
        this.reset();
        this.open = true;
        this.title = "添加付款申请单主";
        this.imageData = [];
        this.form.proNo = row.projectNo;
        this.form.proName = row.projectName;
        this.form.contractId = row.cbcId;
        this.form.contractNo = row.cbcContractNo;
        this.form.contractName = row.cbcContractName;
        getContract(this.form.contractId).then(response => {
          this.form.oppositeName = response.data.oppositeName;
          this.form.oppositeBank = response.data.oppositeBank;
          this.form.oppositeBankId = response.data.oppositeBankId;
          this.form.oppositeContacts = response.data.oppositeContacts;
          this.form.oppositePhone = response.data.oppositePhone;
          this.form.oppositeAddress = response.data.oppositeAddress;

        });
        getUserInfo().then(response => {
          this.form.applicant = response.data.user.nickName;
          this.form.applicationTime = new Date();
          this.billInfoVisible = false;
        });
        countAppliedAmountByContractIdPayment(this.form.contractId).then(response1 => {
          this.form.haveAppliedAmount = response1;
        });
        this.billInfoList = [];
        this.queryParams1.contractId = row.cbcId;
        this.queryParams1.status = 2;
        listTicketRecord(this.queryParams1).then((response) => {
          this.billInfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
        console.log(this.form)
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        let deleSta = 0;
        for (let item of id) {
          if (item == null) {
            deleSta = 1;
            break;
          }
        }
        if (deleSta == 1) {
          this.msgSuccess("请修改有付款申请信息的记录");
          return false;
        }
        getPayinfo(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改付款申请单主";

          if (this.form.paytype == "1") {
            this.billInfoVisible = true;
          }
          this.queryParams1.contractId = this.form.contractId;
          this.queryParams1.status = 2;
          let mapListIds = this.form.ticketRecordList.map(item => item.id)
          listTicketRecord(this.queryParams1).then((response) => {
            this.billInfoList = response.rows;
            this.total = response.total;
            this.loading = false;
            this.billInfoList.map(res => {
              mapListIds.map(item => {
                if (item == res.id) {
                  this.$nextTick(() => {
                    this.$refs.billInfo.toggleRowSelection(res, true);
                  })
                }
              })
            })
          });
        });
        /** 获取文件列表 **/
        findFileListByPidPtype(id, 1).then(response => {
          this.imageData = response.data;
        });
      },

      /** 付款核销 */
      handlePaymentWriteOff(row) {
        this.reset();
        const id = row.id || this.ids[0]
        this.form.id = id;
        this.paymentEvidence = [];
        this.open1 = true;
        this.title = "付款核销";
        /** 获取文件列表 **/
        // findFileListByPidPtype(id, 2).then(response => {
        // 	this.paymentEvidence = response.data;
        // });
      },
      /** 提交按钮 */
      submitForm(number) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.isForbidden = true;
            this.form.status = number;
            if (this.$refs.billInfo != null && this.$refs.billInfo.length != 0) {
              const _selectData = this.$refs.billInfo.selection
              this.form.ticketRecordList = _selectData;
            }
            if (this.form.id != null) {
              updatePayinfo(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.isForbidden = false;
                this.getList();
              });
            } else {
              addPayinfo(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.isForbidden = false;
                this.getList();
              });
            }
          }
        });
      },

      /**核销按钮 */
      submitForm1() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            paymentWriteOff(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open1 = false;
              this.getList();
            });
          }
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
          this.msgSuccess("请删除有付款申请信息的记录");
          return false;
        }
        this.$confirm('是否确认删除付款申请数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return editRemovePayinfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
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
      //付款类型改变时显示/隐藏表格
      payTypeChange() {
        if (this.form.paytype === "1")
          this.billInfoVisible = true;
        else
          this.billInfoVisible = false;
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
          // this.form.taxrate = taxrate;
          //   this.form.oppositeCreditCode = oppositeCreditCode;
          //   this.form.totalMoney = totalMoney;
          //   this.form.paidMoney = receiveAmount;
          //   this.form.unpaidMoney = outstandingAmount;
          // this.progress = (this.form.paidMoney / this.form.totalMoney) * 100;
          this.$set(this.form, 'contractName', contractName);
        }
        this.queryParams1.contractId = this.form.contractId;
        this.queryParams1.status = 2;
        listTicketRecord(this.queryParams1).then((response) => {
          this.billInfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });

        this.showContractSelves = false
      },

      /** 文件上传检测 **/
      // field101BeforeUpload(file) {
      // 	let isRightSize = file.size / 1024 / 1024 < 2;
      // 	if (!isRightSize) {
      // 		this.$message.error('文件大小超过 2MB');
      // 	}
      // 	return isRightSize;
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
      /** 上传附件资料成功操作 **/
      imageDataSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.imageData) {
          this.form.imageData += res.msg + ",";
        } else {
          this.form.imageData = res.msg + ",";
        }
      },
      /** 删除附件 **/
      removeImageData(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.imageData = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.imageData) {
                this.form.imageData += element.uid + ",";
              } else {
                this.form.imageData = element.uid + ",";
              }
            }
          }
        });
      },

      /** 上传付款凭证成功操作 **/
      paymentEvidenceSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.paymentEvidence) {
          this.form.paymentEvidence += res.msg + ",";
        } else {
          this.form.paymentEvidence = res.msg + ",";
        }
      },

      /** 删除付款凭证 **/
      removePaymentEvidence(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.paymentEvidence = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.paymentEvidence) {
                this.form.paymentEvidence += element.uid + ",";
              } else {
                this.form.paymentEvidence = element.uid + ",";
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
          exitFlow(instanceId, 'payinfoTermination').then(response => {
            this.getList();
            this.msgSuccess("终止成功");
          });
        })
      },

    }
  };
</script>
