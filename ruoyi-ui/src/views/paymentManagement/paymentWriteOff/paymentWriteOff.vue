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
        <!-- <el-col :span="6">
          <el-form-item label="申请状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
              <el-option label="请选择字典生成" value="" />
            </el-select>
          </el-form-item>
        </el-col> -->
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

    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="payinfoList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" width="55" align="center" type="index" />
      <el-table-column label="项目编号" align="center" prop="proNo" />
      <el-table-column label="项目名称" align="center" prop="proName" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="合同名称" align="center" prop="contractName" />
      <el-table-column label="对方单位" align="center" prop="oppositeName" />
      <el-table-column label="付款金额" align="center" prop="appliedAmount" />
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
          <el-button v-if="scope.row.status==2" size="mini" type="text" icon="el-icon-edit" @click="handlePaymentWriteOff(scope.row)">核销</el-button>
        </template>
      </el-table-column>
    </el-table>


    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


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
  </div>
</template>

<script>
  import {
    listPayinfo,
    getPayinfo,
    delPayinfo,
    addPayinfo,
    updatePayinfo,
    exportPayinfo,
    editRemovePayinfo,
    paymentWriteOff
  } from "@/api/paymentManagement/payinfo";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPidPtype,
    downloadfileById
  } from "@/api/utils";
  import {
    getUserInfo
  } from "@/api/collection/common";
  import {
    getToken
  } from "@/utils/auth";
  import {
    listTicketRecord,
    getTicketRecord,
    delTicketRecord,
    addTicketRecord,
    updateTicketRecord,
    exportTicketRecord
  } from "@/api/paymentManagement/ticketRecord";
  export default {
    name: "Payinfo",
    data() {
      return {
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

        payinfoList: [], // 付款申请单主表格数据
        billInfoList: [], //发票付款信息表格

        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        open1: false,
        applicationTime: [],


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
        billInfoVisible: false,
        field101Action: this.$fieldPathAction,
        imageData: [],
        paymentEvidence: [],
        autoUploadFalg: true,
        // 表单参数
        form: {
          applicant: null,
          ticketRecordList: []
        },
        token: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询付款申请单主列表 */
      getList() {
        this.loading = true;
        this.queryParams.status = 2;
        listPayinfo(this.addDateRange(this.queryParams, this.applicationTime)).then(response => {
          this.payinfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel1() {
        this.open1 = false;
        this.reset();
      },
      // 表单重置
      reset() {
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


      /** 付款核销 */
      handlePaymentWriteOff(row) {
        this.reset();
        const id = row.id || this.ids[0]
        this.form.id = id;
        this.paymentEvidence = [];
        this.open1 = true;
        this.title = "付款核销";
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
        /** 获取文件列表 **/
        // findFileListByPidPtype(id, 2).then(response => {
        // 	this.paymentEvidence = response.data;
        // });
      },

      /**核销按钮 */
      submitForm1() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            paymentWriteOff(this.form).then(response => {
              this.msgSuccess("核销成功");
              this.open1 = false;
              this.getList();
            });
          }
        });
      },

      /** 文件上传检测 **/
      field101BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 20;
        if (!isRightSize) {
          this.$message.error('文件大小超过 20MB');
        }
        return isRightSize;
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

    }
  };
</script>
