<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
         <el-col :span="6">
           <el-form-item label="项目名称" prop="proName">
             <el-input
               v-model="queryParams.proName"
               placeholder="请输入项目名称"
               clearable
               size="small"
               @keyup.enter.native="handleQuery"
             />
           </el-form-item>
          </el-col>
          <el-col :span="6">
           <el-form-item label="合同名称" prop="contractName">
             <el-input
               v-model="queryParams.contractName"
               placeholder="请输入申请单号"
               clearable
               size="small"
               @keyup.enter.native="handleQuery"
             />
           </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="发票代码" prop="invoiceCode">
              <el-input
                v-model="queryParams.invoiceCode"
                placeholder="请输入发票代码"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="发票号码" prop="invoiceNo">
              <el-input
                v-model="queryParams.invoiceNo"
                placeholder="请输入发票号码"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
      </el-row>
      <el-row :gutter="15">
         <el-col :span="6">
          <el-form-item label="开具日期" prop="invoiceDate">
            <el-date-picker clearable size="small" :style="{ width: '100%' }"
              v-model="queryParams.invoiceDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择发票开具日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="开票人" prop="invoiceDrawer">
            <el-input
              v-model="queryParams.invoiceDrawer"
              placeholder="请输入开票人"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="凭证号" prop="voucherNo">
              <el-input
                v-model="queryParams.voucherNo"
                placeholder="请输入凭证号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
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
<!--      v-hasPermi="['collection:financial:edit']"-->
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"

        >凭证单录入</el-button>
      </el-col>


	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

      <el-table v-loading="loading" ref="financialList" border :data="financialList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="show"/>
        <el-table-column label="主键" align="center" v-if="show" prop="ctBuInvoiceReq.instanceId" />
       <el-table-column label="项目名称" align="center" width="150px" prop="ctBuInvoiceReq.proName" :show-overflow-tooltip="true"/>
       <el-table-column label="合同名称" align="center" width="150px" prop="ctBuInvoiceReq.contractName" :show-overflow-tooltip="true"/>
       <el-table-column label="发票类型" align="center" prop="ctBuInvoiceReq.invoiceType" :show-overflow-tooltip="true">
        <template slot-scope="s">
           <p v-if="s.row.ctBuInvoiceReq.invoiceType == 1">增值税专票</p >
           <p v-if="s.row.ctBuInvoiceReq.invoiceType == 2">增值税普票</p >
          </template>
        </el-table-column>
       <el-table-column label="申请部门" align="center" prop="ctBuInvoiceReq.reqDeptname" :show-overflow-tooltip="true"/>
       <el-table-column label="申请人" align="center" prop="ctBuInvoiceReq.reqUsername" />
       <el-table-column label="申请日期"  width="100px" align="center" prop="ctBuInvoiceReq.reqDate" :show-overflow-tooltip="true"/>
       <el-table-column label="付款单位名称" align="center"   prop="ctBuInvoiceReq.payerName" :show-overflow-tooltip="true"/>
       <el-table-column label="开票金额(元)" align="center" prop="ctBuInvoiceReq.invoiceAmountTax" />

        <el-table-column label="发票代码" align="center" prop="invoiceCode" />
        <el-table-column label="发票号码" align="center" prop="invoiceNo" />
        <el-table-column label="发票开具日期" align="center" prop="invoiceDate" width="100" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.invoiceDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开票人" align="center" prop="invoiceDrawer" />
        <el-table-column label="复核人" align="center" prop="invoiceReviewer" />
        <el-table-column label="收款人" align="center" prop="invoicePayee" />
        <el-table-column label="发票金额(元)" align="center" prop="invoiceAmountTax" />
        <el-table-column label="金额(元)" align="center" prop="invoiceAmountNtax" />
        <el-table-column label="移交人" align="center" prop="handoverUsername" />
        <el-table-column label="移交时间" align="center" prop="handoverTime" width="100" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.handoverTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="确认人" align="center" prop="confirmerUsername" />
        <el-table-column label="确认时间" align="center" prop="confirmerTime" width="100" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.confirmerTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="凭证号" align="center" prop="voucherNo" />
        <el-table-column label="凭证号录入人" width="150px" align="center" prop="voucherUsername" />
        <el-table-column label="凭证号录入时间" align="center" prop="voucherTime" width="100" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.voucherTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="状态" align="center" prop="status" /> -->
        <!-- <el-table-column label="状态" align="center" prop="status"> -->
          <el-table-column label="状态">
                <template slot-scope="s">
                 <p v-if="s.row.status == 1">发票生成</p >
                 <p v-if="s.row.status == 2">发票移交</p >
                 <p v-if="s.row.status == 3">发票移交确认</p >
                 <p v-if="s.row.status == 4">凭证号录入</p >
                 <p v-if="s.row.status == 5">收款单录入</p >
                 <p v-if="s.row.status == 6">收款单审核通过</p >
                 <p v-if="s.row.status == 7">收款单审核不通过</p >
                 <p v-if="s.row.status == 8">发票核销</p >
                </template>
               </el-table-column>
      </el-table>

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


<!-- 添加或修改财务开票对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-input v-model="form.id" placeholder="" v-if="show"/>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同类别" prop="contractCategory">
              <el-input v-model="form.contractCategoryName" placeholder="请输入合同类别"  readOnly="true" />
              <el-input v-model="form.contractCategory" :readonly="true" placeholder="请输入申请单号"  v-if="show"/>
            </el-form-item>
          </el-col>
          <el-col :span="12"   v-if="show">
            <el-form-item label="申请单号" prop="reqId">
              <el-input v-model="form.reqId" placeholder="请输入申请单号"  v-if="show"  readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票代码" prop="invoiceCode" label-width="120px">
              <el-input v-model="form.invoiceCode" placeholder="请输入发票代码"  readOnly="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发票号码" prop="invoiceNo">
              <el-input v-model="form.invoiceNo" placeholder="请输入发票号码"  readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票开具日期" prop="invoiceDate"  label-width="120px">
              <el-date-picker clearable size="small" :style="{ width: '100%' }"
                              v-model="form.invoiceDate"
                              type="date"
                              value-format="yyyy-MM-dd"

                              placeholder="选择发票开具日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" >
            <el-form-item label="开票人" prop="invoiceDrawer">
              <el-input v-model="form.invoiceDrawer" :readonly="true" placeholder="请输入开票人"  readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="复核人" prop="invoiceReviewer" label-width="120px">
              <el-input v-model="form.invoiceReviewer" placeholder="请输入复核人"  readOnly="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款人" prop="invoicePayee">
              <el-input v-model="form.invoicePayee" placeholder="请输入收款人"  readOnly="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票金额(元)" prop="invoiceAmountTax" label-width="120px">
              <el-input v-model="form.invoiceAmountTax" :readonly="true"   placeholder="请输入发票金额"  readOnly="true" />

              <!-- <el-input v-model="form.invoiceAmountTax" :readonly="true"  v-on:input="changeNum()" oninput="value=value.replace(/[^0-9.]/g,'')" placeholder="请输入发票金额" /> -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="税率" prop="invoiceTaxrate">
              <el-input v-model="form.invoiceTaxrate" :readonly="true"  placeholder="请输入税率"  readOnly="true" />

              <!-- <el-input v-model="form.invoiceTaxrate" :readonly="true" @focus="getSlLists" oninput="value=value.replace(/[^0-9.]/g,'')" placeholder="请输入税率" /> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额（不含税）" prop="invoiceAmountNtax" label-width="120px">
              <el-input v-model="form.invoiceAmountNtax"  ::readonly="true" placeholder="请输入金额"  readOnly="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" >
            <el-form-item  label="发票图片" prop="invoiceImg">
              <el-upload
                :disabled="true"
                ref="field112"
                :file-list="invoiceImg"
                :action="field111Action"
                :headers="token"
                :on-success="invoiceImgFileSuccess"
                :on-remove="removeInvoiceImglFile"
                :auto-upload="autoUploadFalg"
                multiple
                :limit="20"
                :before-upload="field111BeforeUpload"
                :on-preview="handlePictureCardPreview"
              >
                <el-button slot="trigger"  size="small"  type="primary" :disabled="true">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png/gif文件
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="企业资质图片" prop="companyImg" label-width="120px">
              <el-upload
                :disabled="true"
                ref="field115"
                :file-list="companyImg"
                :action="field111Action"
                :headers="token"
                :on-success="fianlApprovalFileSuccess"
                :on-remove="removeContractApprovalFile"
                :auto-upload="autoUploadFalg"
                multiple
                :limit="20"
                :before-upload="field111BeforeUpload"
                :on-preview="handlePictureCardPreview"
              >
                <el-button slot="trigger"  size="small" type="primary" :disabled="true">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png/gif文件
                </div>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible"  append-to-body>
                <img width="100%" :src="dialogImageUrl" alt />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>




        <el-row>
          <el-col :span="12">
            <el-form-item label="所属分公司" prop="voucherDept"  label-width="120px">
              <el-select
                style="width:100%;"
                v-model="form.voucherDept"
                placeholder="请输入所属分公司"
                clearable
                size="small"
              >
                <el-option
                  v-for="dict in belongCompanyNameList"
                  :key="dict.deptId"
                  :label="dict.deptName"
                  :value="dict.deptId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月份" prop="voucherMonth" label-width="120px">
              <el-select
                style="width:100%;"
                v-model="form.voucherMonth"
                placeholder="请选中月份"
                clearable
                size="small"
              >
                <el-option
                  v-for="dict in options"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="凭证号" prop="voucherNo" label-width="120px">
              <el-input type="textarea" v-model="form.voucherNo" placeholder="请输入凭证号" />
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
import { listFinancial, getFinancial, delFinancial, moveFinancial,addFinancial, updateFinancial, exportFinancial } from "@/api/collection/voucher";
import { getToken } from "@/utils/auth";
import { findProjectType, findBranchOffice, delFileById, findFileListByPid, findFileListByPidPtype, downloadfileById} from "@/api/utils";

export default {
  name: "Financial",
  data() {
    return {
      options: [{
        value: '01',
        label: '01'
      }, {
        value: '02',
        label: '02'
      }, {
        value: '03',
        label: '03'
      }, {
        value: '04',
        label: '04'
      }, {
        value: '05',
        label: '05'
      }, {
        value: '06',
        label: '06'
      }, {
        value: '07',
        label: '07'
      }, {
        value: '08',
        label: '08'
      }, {
        value: '09',
        label: '09'
      }, {
        value: '10',
        label: '10'
      }, {
        value: '11',
        label: '11'
      }, {
        value: '12',
        label: '12'
      }],
      belongCompanyNameList: [],
      uploadVis:true,
      dialogVisible:false,
      dialogImageUrl:null,
      authenStatus:false,
      autoUploadFalg: true,
      invoiceImg:[],
      companyImg:[],
      fd :[],
      autoUploadFalg: true,
      field111Action: this.$fieldPathAction,

      token: {},
      showBtn:true,
      show:false,
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
      // 财务开票表格数据
      financialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractCategory: null,
        reqId: null,
        invoiceCode: null,
        invoiceNo: null,
        invoiceDate: null,
        invoiceDrawer: null,
        invoiceReviewer: null,
        invoicePayee: null,
        invoiceAmountTax: null,
        invoiceTaxrate: null,
        invoiceAmountNtax: null,
        invoiceImg: null,
        companyImg: null,
        handoverUserid: null,
        handoverUsername: null,
        handoverTime: null,
        confirmerUserid: null,
        confirmerUsername: null,
        confirmerTime: null,
        voucherNo: null,
        voucherDept:null,
        voucherUserid: null,
        voucherUsername: null,
        voucherTime: null,
        status: null,
        statusps: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        invoiceDate: [
          { required: true, message: "发票开具日期不能为空", trigger: "blur" }
        ],
        voucherMonth: [
          { required: true, message: "月份不能为空", trigger: "blur" }
        ],
        voucherDept: [
          { required: true, message: "请选择单位", trigger: "blur" }
        ],
        voucherNo: [
          { required: true, message: "凭证号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    findBranchOffice().then(response => {
      this.belongCompanyNameList = response.data;
    });
  },
  methods: {
    handlePictureCardPreview(file) {//点击放大图片
      if((file.url).indexOf("profile")>=0){
        let files = (file.url).split("profile");
        let url = window.location.hostname;
        if(url.indexOf("http")<0){
          url = "http://"+url;
        }
        this.dialogImageUrl = url+":9001/profile"+files[1];
        this.dialogVisible = true;
        this.loadings = true;
      }
      // this.dialogVisible = true;
    },
    /** 查询财务开票列表 */
    getList() {
      this.loading = true;
      listFinancial(this.queryParams).then(response => {
        this.financialList = response.rows;
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
      this.uploadVis=true;
      this.dialogVisible=false;
      this.dialogImageUrl=null;
      this.authenStatus=false;
      this.autoUploadFalg= true;
      this.invoiceImg=[];
      this.companyImg=[];
      this.fd =[];
      this.autoUploadFalg= true;
      this.form = {
        id: null,
        contractCategory: null,
        reqId: null,
        invoiceCode: null,
        invoiceNo: null,
        invoiceDate: null,
        invoiceDrawer: null,
        invoiceReviewer: null,
        invoicePayee: null,
        invoiceAmountTax: null,
        invoiceTaxrate: null,
        invoiceAmountNtax: null,
        invoiceImg: null,
        companyImg: null,
        handoverUserid: null,
        handoverUsername: null,
        handoverTime: null,
        confirmerUserid: null,
        confirmerUsername: null,
        confirmerTime: null,
        voucherNo: null,
        voucherDept:null,
        voucherMonth:null,
        voucherUserid: null,
        voucherUsername: null,
        voucherTime: null,
        status: 0,
        statusps: null,
        delFlag: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加财务开票";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const _selectData = this.$refs.financialList.selection;
      if(_selectData[0].status>=4){
       this.showBtn = false;

      }else{
        this.showBtn = true;
      }
      //测试
     let rid = "xxxxxxxxxxx";
      getFinancial(_selectData[0].id,rid).then(response => {
        this.form = response.data;
        if(this.form.id==null || this.form.id==''){
          this.form.id = _selectData[0].id;
        }
        this.form.voucherDept = parseInt(response.data.voucherDept);
        this.open = true;
        this.title = "凭证单录入";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.voucherNo==null || this.form.voucherNo ==''){
            this.msgSuccess("凭证号不能为空");
            return false;
          }
          if(this.form.voucherMonth==null || this.form.voucherMonth ==''){
            this.msgSuccess("月份不能为空");
            return false;
          }
          if(this.form.voucherDept==null || this.form.voucherDept ==''){
            this.msgSuccess("请选择单位");
            return false;
          }
          if(this.form.status>4){
            this.form.status='100';
          }else{
            this.form.status = 4;
          }

          let  id_ = this.form.id;

          let no_=this.form.voucherNo;
          //let dates=this.form.voucherTime;
          let voucherMonth=this.form.voucherMonth;
          let voucherDept=this.form.voucherDept;
          // this.$confirm('是否确认录入内容,录入后将不能修改?', "警告", {
          //     confirmButtonText: "确定",
          //     cancelButtonText: "取消",
          //     type: "warning"
          //   }).then(function() {
          //     return moveFinancial(id_,no_,voucherMonth,voucherDept);
          //   }).then(() => {
          //     this.open = false;
          //     this.getList();
          //     this.msgSuccess("录入成功");
          //   })
          moveFinancial(id_,no_,voucherMonth,voucherDept,this.form.status).then(response => {
            this.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除选中的数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFinancial(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 移交按钮操作 */
    handleMove(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认移交选中数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return moveFinancial(ids,"0");
        }).then(() => {
          this.getList();
          this.msgSuccess("移交成功");
        })
    },
    /** 移交按钮操作 */
    handleConfirm(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认移交选中数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return moveFinancial(ids,"1");
        }).then(() => {
          this.getList();
          this.msgSuccess("确认成功");
        })
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有财务开票数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportFinancial(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    selectInvoice(row) {
      let rid = row.ctBuInvoiceReq.id;
      const id = row.id || this.ids
      let ids = '';
      if(id=='' || id==null){
        ids = 'xxxxxxxxxx';
      }
      getFinancial(id==''?ids:id,rid).then(response => {
        this.token = { Authorization: getToken() };
        this.form = response.data;
        this.open = true;
        this.title = "财务开票";
        let createName = "";
        if(id!=null && id!=''){
          findFileListByPidPtype(id,1).then(response => {
            this.companyImg= response.data;
          });
          findFileListByPidPtype(id,2).then(response => {
            this.invoiceImg= response.data;
          });
        }else{
          this.$nextTick(() => {
            this.$refs.field115.clearFiles();
            this.$refs.field112.clearFiles();
          })
        }
        // this.$set(this.form,'invoicePayee',createName)
      });


    },
    /** 文件上传检测 **/
    field111BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 20;
      if (!isRightSize) {
        this.$message.error('文件大小超过 20MB');
      }
      let testmsg=file.name.substring(file.name.lastIndexOf('.')+1)
      if(isRightSize){
        if(testmsg=='jpg' || testmsg=='JPG'|| testmsg=='PNG' || testmsg=='png' || testmsg=='gif' || testmsg=='GIF'){

        }else{
          isRightSize = false;
          this.$message.error('只允许上传图片');
        }
      }

      return isRightSize;
    },
    /** 上传附件成功操作 **/
    fianlApprovalFileSuccess(res, file, fileList) {
      for (let index = 0; index < fileList.length; index++) {
        const element = fileList[index];
        if (element.uid == file.uid) {
          element.uid = res.msg;
        }
      }
      if (this.form.companyImg) {
        this.form.companyImg += res.msg + ",";
      } else {
        this.form.companyImg = res.msg + ",";
      }
      this.uploadVal1 = true;
      this.$refs.form.validateField("companyImg");

    },
    /** 删除附件操作 **/
    removeContractApprovalFile(file, fileList) {
      delFileById(file.uid).then(response => {
        if (response.code == 200) {
          this.form.companyImg = null;
          for (let index = 0; index < fileList.length; index++) {
            const element = fileList[index];
            if (this.form.companyImg) {
              this.form.companyImg += element.uid + ",";
            } else {
              this.form.companyImg = element.uid + ",";
            }
          }
        }
      });
    },

    /** 上传附件成功操作 **/
    invoiceImgFileSuccess(res, file, fileList) {
      for (let index = 0; index < fileList.length; index++) {
        const element = fileList[index];
        if (element.uid == file.uid) {
          element.uid = res.msg;
        }
      }
      if (this.form.invoiceImg) {
        this.form.invoiceImg += res.msg + ",";
      } else {
        this.form.invoiceImg = res.msg + ",";
      }
      this.uploadVal = true;
      this.$refs.form.validateField("invoiceImg");
    },
    /** 删除附件操作 **/
    removeInvoiceImglFile(file, fileList) {
      delFileById(file.uid).then(response => {
        if (response.code == 200) {
          this.form.invoiceImg = null;
          for (let index = 0; index < fileList.length; index++) {
            const element = fileList[index];
            if (this.form.invoiceImg) {
              this.form.invoiceImg += element.uid + ",";
            } else {
              this.form.invoiceImg = element.uid + ",";
            }
          }
        }
      });
    },
  }
};
</script>
