<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm"  v-show="showSearch" label-width="90px">
     <el-row :gutter="15">
      <el-col :span="8">
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
       <el-col :span="8">
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
        <el-col :span="8">
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

       </el-row>
       <el-row :gutter="15">
         <el-col :span="8">
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
         <el-col :span="8">

           <el-form-item label="开具日期" prop="invoiceDate">
             <el-date-picker
               v-model="dateRange"
               size="small"
               style="width: 384px"
               value-format="yyyy-MM-dd"
               type="daterange"
               range-separator="-"
               start-placeholder="开始日期"
               end-placeholder="结束日期"
             ></el-date-picker>
           </el-form-item>


         </el-col>
          <el-col :span="8">
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

      </el-row>

      <el-row :gutter="15">
        <el-col :span="8">
          <el-form-item label="所属分公司" prop="belongCompanyid">
            <el-select
              style="width:100%;"
              v-model="queryParams.reqDeptid"
              placeholder="请输入所属分公司"
              clearable
              @clear="remove()"
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

      <el-col :span="8">
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

      <el-col :span="8">
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuerys">重置</el-button>
        </el-form-item>
      </el-col>

      </el-row>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleAdd"
          v-hasPermi="['collection:financial:edit']"
        >开票</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['collection:financial:edit']"
        >编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['collection:financial:remove']"
        >删除</el-button>
      </el-col>
      <!--
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['collection:financial:export']"
        >导出</el-button>
      </el-col>
      -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleMove"

        >财务移交</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" border ref="itsmDataTable"  :data="financialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="show"/>
<!--      <el-table-column label="主键" align="center" v-if="show" prop="ctBuInvoiceReq.instanceId" />-->
      <el-table-column label="合同类别" align="center" v-if="show" prop="contractCategory" />
     <el-table-column label="合同类别名称" align="center" v-if="show" prop="contractCategoryName" />
     <el-table-column label="主键" align="center" v-if="show" prop="rid" />

      <el-table-column label="项目名称" align="center" prop="proName" width="150px"  :show-overflow-tooltip="true" />
     <el-table-column label="合同名称" align="center" prop="contractName" width="150px"  :show-overflow-tooltip="true" />
     <el-table-column label="税率" align="center" prop="invoiceTaxrate"  v-if="show" />

     <el-table-column label="发票类型" align="center" prop="invoiceType" width="110px" >
       <template slot-scope="s">
        <p v-if="s.row.invoiceType == 1">增值税专票</p >
        <p v-if="s.row.invoiceType == 2">增值税普票</p >
       </template>
     </el-table-column>
     <el-table-column label="申请部门" align="center" prop="reqDeptname" :show-overflow-tooltip="true"/>
     <el-table-column label="申请人" align="center" prop="reqUsername" />
     <el-table-column label="申请日期" align="center" prop="reqDate" width="100px" />
     <el-table-column label="付款单位名称"  width="150px" align="center" prop="payerName" :show-overflow-tooltip="true"/>
     <el-table-column label="申请金额(元)" align="center" prop="invoiceAmountTax1" />
     <el-table-column label="申请金额(不含税)" align="center" prop="invoiceAmountNtax1" />
      <el-table-column label="发票代码" align="center" prop="invoiceCode" />
      <el-table-column label="发票号码" align="center" prop="invoiceNo" />
      <el-table-column label="开票金额(元)" align="center" prop="invoiceAmountTax" />
      <el-table-column label="开票金额(不含税)" align="center" prop="invoiceAmountNtax" />
      <el-table-column label="发票开具日期" align="center" prop="invoiceDate" width="100" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.invoiceDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开票人" align="center" prop="invoiceDrawer" />
<!--      <el-table-column label="复核人" align="center" prop="invoiceReviewer" />-->
      <el-table-column label="收款人" align="center" prop="invoicePayee" />
      <!-- <el-table-column label="金额" align="center" prop="invoiceAmountNtax" /> -->
<!--      <el-table-column label="移交人" align="center" prop="handoverUsername" />-->
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
      <el-table-column label="凭证号录入人"    align="center" prop="voucherUsername" />
      <el-table-column label="凭证号录入时间" align="center" prop="voucherTime" width="100" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voucherTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
        <el-table-column label="状态">
              <template slot-scope="s">
               <p v-if="s.row.status == 0">保存</p >
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
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-view"-->
<!--            @click="handleUpdate(scope.row,0)"-->
<!--            v-hasPermi="['collection:financial:edit']"-->
<!--          >详情</el-button>-->


<!--        </template>-->
<!--      </el-table-column>-->
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
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同类别" prop="contractCategory">
              <el-input v-model="form.contractCategoryName" placeholder="请输入合同类别"  />
              <el-input v-model="form.contractCategory" :readonly="true" placeholder="请输入申请单号"  v-if="show"/>
            </el-form-item>
          </el-col>
          <el-col :span="12"   v-if="show">
            <el-form-item label="申请单号" prop="reqId">
              <el-input v-model="form.reqId" placeholder="请输入申请单号"  v-if="show"/>
            </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="发票代码" prop="invoiceCode">
               <el-input v-model="form.invoiceCode" placeholder="请输入发票代码" />
             </el-form-item>
           </el-col>
        </el-row>
        <el-row>
           <el-col :span="12">
            <el-form-item label="发票号码" prop="invoiceNo">
              <el-input v-model="form.invoiceNo" placeholder="请输入发票号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票开具日期" prop="invoiceDate">
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
              <el-input v-model="form.invoiceDrawer" :readonly="true" placeholder="请输入开票人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="复核人" prop="invoiceReviewer">
              <el-input v-model="form.invoiceReviewer" placeholder="请输入复核人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款人" prop="invoicePayee">
              <el-input v-model="form.invoicePayee" placeholder="请输入收款人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票金额(元)" prop="invoiceAmountTax">
              <el-input v-model="form.invoiceAmountTax"  v-on:input="changeNum()" oninput="value=value.replace(/[^0-9.]/g,'')"  placeholder="请输入发票金额" />

              <!-- <el-input v-model="form.invoiceAmountTax" :readonly="true"  v-on:input="changeNum()" oninput="value=value.replace(/[^0-9.]/g,'')" placeholder="请输入发票金额" /> -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="税率(%)" prop="invoiceTaxrate">
             <el-input v-model="form.invoiceTaxrate"  placeholder="请输入税率" v-on:input="changeNum()" oninput="value=value.replace(/[^0-9.]/g,'')" />

              <!-- <el-input v-model="form.invoiceTaxrate" :readonly="true" @focus="getSlLists" oninput="value=value.replace(/[^0-9.]/g,'')" placeholder="请输入税率" /> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额（不含税）" prop="invoiceAmountNtax">
              <el-input v-model="form.invoiceAmountNtax"   placeholder="请输入金额" :readOnly="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
       <el-col :span="12" >
         <el-form-item  label="发票图片" prop="invoiceImg">
          <el-upload
            :disabled="authenStatus"
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
            <el-button slot="trigger"  size="small"  type="primary" :disabled="authenStatus">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png/gif文件
            </div>
          </el-upload>
         </el-form-item>
       </el-col>
       <el-col :span="12">
         <el-form-item
           label="企业资质图片" prop="companyImg">
           <el-upload
             :disabled="authenStatus"
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
             <el-button slot="trigger"  size="small" type="primary" :disabled="authenStatus">选取文件</el-button>
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


      </el-form>

      <div slot="footer" class="dialog-footer">
<!--        <el-button type="primary"   v-if="butVis" @click="submitForm(0)" :disabled="subBtn">保 存</el-button>-->
        <el-button type="primary" v-if="butVis"   @click="submitForm(0)" :disabled="subBtn">保 存</el-button>

        <el-button type="primary"  v-if="butVis" @click="submitForm(1)" :disabled="subBtn">提 交</el-button>
        <el-button @click="cancel"  >取 消</el-button>
      </div>
    </el-dialog>





    <el-dialog
           width="50%"
           height="70%"
           title="税率信息"
           :visible.sync="slVisible"
           append-to-body>
           <el-table  ref="htTable"  v-loading="htloading" :data="contractList" >
              <el-table-column type="selection" width="55" align="center" v-if="show"/>
             <el-table-column label="唯一ID" align="center" prop="taxrate" v-if="show" />
             <el-table-column label="税率" align="center" prop="taxrate" />
             <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                     <template slot-scope="scope">
                       <el-button
                         size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="submithtForm(scope.row)"

                       >选择</el-button>

                     </template>
                   </el-table-column>
           </el-table>
          <pagination
                 v-show="total>0"
                 :total="total"
                 :page.sync="queryParams.pageNum"
                 :limit.sync="queryParams.pageSize"
                 @pagination="contractList"
               />


       <div slot="footer" class="dialog-footer">
         <!-- <el-button type="primary" @click="submithtForm">确 定</el-button> -->
         <el-button @click="slVisible=false">取 消</el-button>
       </div>

       </el-dialog>

        <AddorUpdate v-if="addOrUpdateVisible" ref="addOrUpdate"></AddorUpdate>
  </div>



</template>

<script>
import { getInfoById ,selectTotalInfo,getSlList,listFinancial, getFinancial, delFinancial, addFinancial, updateFinancial, exportFinancial } from "@/api/collection/financial";
import selectCompany from './selectCompany';
import { moveFinancial} from "@/api/collection/transfer";
import AddorUpdate from './detail.vue';
import { getToken } from "@/utils/auth";
import { findProjectType, findBranchOffice, delFileById, findFileListByPid, findFileListByPidPtype, downloadfileById} from "@/api/utils";


export default {
  name: "Financial",
  components: {
      selectCompany,
      AddorUpdate
  },
  data() {
    return {
      belongCompanyNameList:[],
      subBtn:false,
      dateRange: [],
      authenStatus:false,
      ticketRecordList:[],
      uploadVal1:false,
      uploadVal:false,
      //上传按钮显示
      uploadVis:true,
      dialogVisible:false,
      dialogImageUrl:null,
        autoUploadFalg: true,
      isdisabledFn:true,
      butVis:true,
       autoUploadFalg: true,
       invoiceImg:[],
      companyImg:[],
      fd :[],
      addOrUpdateVisible:false,
      htloading:false,
      slVisible:false,
      show:false,
      token: {},
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      status:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      contractList:[],
      //合同类别
      contractCategoryList: [],
      // 财务开票表格数据
      financialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
        field111Action: this.$fieldPathAction,
      // 查询参数
      queryParams: {
        reqDeptid:null,
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
        voucherUserid: null,
        voucherUsername: null,
        voucherTime: null,
        status: null,
        statusps: null,
      },
      // 表单参数
      form: {
        //invoiceDate:new Date()
        //invoiceReviewer:'石庆滨'
      },
      // 表单校验
     // 表单校验
     rules:{
       invoiceDate: [{ required: true, message: '发票开具日期不能为空', trigger: 'blur' }],
       contractCategory: [{ required: true, message: '请选择合同类别', trigger: 'blur' }],
         invoiceCode: [{ required: true, message: '请选择发票代码', trigger: 'blur' }],
         invoiceNo: [{ required: true, message: '发票号码不能为空', trigger: 'blur' }],
         invoiceDrawer: [{ required: true, message: '开票人不能为空', trigger: 'blur' }],
         invoiceReviewer: [{ required: true, message: '复核人不能为空', trigger: 'blur' }],
         invoicePayee: [{ required: true, message: '收款人不能为空', trigger: 'blur' }],
         invoiceAmountTax: [{ required: true, message: '发票金额不能为空', trigger: 'blur' }],
       invoiceTaxrate: [{ required: true, message: '税率不能为空', trigger: 'change' }],
       invoiceAmountNtax: [{ required: true, message: '金额不能为空', trigger: 'change' }],

       invoiceImg:[{
         required: true,
         message: '请上传发票图片',
         trigger: 'change'
       }],
       // companyImg:[{
       //   required: true,
       //   message: '请上传企业资质图片',
       //   trigger: 'blur'
       // }],

     },
    };
  },
  created() {
    this.getList();
    this.getDicts("contract_category_type").then((response) => {
      this.contractCategoryList = response.data;
    });
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

/*文件上传,重写文件上传方法,action的路径不会起作用*/
    myUpload(file){
      this.fd = new FormData();
      this.fd.form=this.form;//传文件
      this.fd.file=file;//传其他参数
      // fd.append('filename',file.name);//传其他参数
      // uploadFiles(fd).then(function(res) {
      //   console.log(res);
      // });

    },

 /** 移交按钮操作 */
 handleMove(row) {
    const ids = row.id || this.ids;
    const _selectData = this.$refs.itsmDataTable.selection;
    if(_selectData!='' && _selectData.length>0){
      const state = 0;
      for(let item of _selectData) {
        if(item.status==undefined || item.status=="undefined" || item.status==0){
           this.msgError("请选中开具完成的记录进行移交");
          return false;
        }
      		if(item.status>=2){
            this.msgError("发票移交已完成，不能进行此操作");
           return false;
         }
      }
    }
  this.$confirm('是否确认移交本条开票信息?', "警告", {
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
//监听输入框输入值
    changeNum(){
      //单价
      let fpje = this.form.invoiceAmountTax;
      //数量
      let sl = this.form.invoiceTaxrate


      if(fpje != null && sl != null){
        //获得不含税金额
        this.form.invoiceAmountNtax = (fpje/ (sl * 0.01 + 1)).toFixed(2);
      }

    },
    /*选择对方单位弹出框 调用方法*/
    companyOk(id,taxtrate){
      this.form.invoiceTaxrate = taxtrate;
      this.$set(this.form,'oppositeName',taxtrate)
    },
  addOrUpdateHandle(id){
      this.addOrUpdateVisible = true
      this.$nextTick(() =>{
       this.$refs.addOrUpdate.init(id);
      })
    },

//获取税率信息
    getSlLists() {
      this.slVisible = true;
      getSlList().then(response => {
        this.contractList = response.rows;
        this.total = response.total;
         this.slVisible = true;
      });
    },
    /** 查询财务开票列表 */
    getList() {
      this.loading = true;
      listFinancial(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
    /** 选择合同 */
    submithtForm(row) {
      const id = row.taxrate ;
       this.form.invoiceTaxrate = id;

       //单价
       let fpje = this.form.invoiceAmountTax;
       //数量
       let sl = id;


       if(fpje != null && sl != null){
         //获得不含税金额
         this.form.invoiceAmountNtax = (fpje/ (sl * 0.01 + 1)).toFixed(2);
       }

       this.slVisible = false;
    },
    // 表单重置
    reset() {
      this.butVis = true;
      this.subBtn = false;
      this.isdisabledFn = true;
      this.uploadVis = true;
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

    // 表单重置
    resetQuerys() {
      this.queryParams = {
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
      this.resetForm("queryParams");
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;

      this.getList();
    },
    resetForm() {
    //  this.form.resetFields();
      /** 加入请求头 **/
      this.token = { Authorization: getToken() };
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 重置按钮操作 */
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = (selection || []).map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
      this.status = (selection || []).map(item => item.status)
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.open = true;
      this.title = "添加财务开票";

      const _selectData = this.$refs.itsmDataTable.selection
      let rid = null;
      if(_selectData!=null && _selectData!=''){
        rid = _selectData[0].rid;
      }else{
        this.msgError("请选择记录进行添加");
        return false;
      }

      getFinancial(rid).then(response => {
        this.token = { Authorization: getToken() };
        this.form = response.data;
        this.open = true;
        this.title = "财务开票";
        let createName = "";
        if(this.form.contractCategory == null || this.form.contractCategory == ''){
          this.form.contractCategory = _selectData[0].contractCategory;
        }
        this.form.contractCategoryName = _selectData[0].contractCategoryName;
        //this.form.invoiceAmountNtax = _selectData[0].invoiceAmountNtax;
        //this.form.invoiceAmountTax = _selectData[0].invoiceAmountTax;
        this.form.invoiceTaxrate = _selectData[0].invoiceTaxrate;
        createName = _selectData[0].reqUsername+"";

        //this.form.invoicePayee = createName;
        this.$set(this.form,'invoicePayee',createName);

        this.$set(this.form,'reqId',rid);

        this.$nextTick(() => {
          this.$refs.field115.clearFiles();
          this.$refs.field112.clearFiles();
        })
        //添加默认日期
        let now = new Date()
        let year = now.getFullYear() // 得到年份
        let month = now.getMonth() // 得到月份
        let date = now.getDate() // 得到日期
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        let defaultDate = `${year}-${month}-${date}`;
        this.$set(this.form,'invoiceDate', defaultDate);
        this.$set(this.form,'invoiceReviewer', '石庆滨')
      });

    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
        const _selectData = this.$refs.itsmDataTable.selection

      let id = row.id || this.ids;
      if(id==null || id==''){
        id = _selectData[0].id;
      }

      getInfoById(id).then(response => {
        this.token = { Authorization: getToken() };
        this.form = response.data;
        this.open = true;
        this.title = "财务开票";
        if(this.form.status>0){
          this.butVis = false;
        }
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
        //this.form.invoicePayee = createName;
        //this.$set(this.form,'invoicePayee',createName)
      });


    },
    /** 提交按钮 */
    submitForm(value) {
      this.subBtn = true;
      this.form.voucherTime = '';
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.status = value;
          //提交验证是否超值
          if(value==1){
            selectTotalInfo(this.form.reqId).then(response => {
              // && response[0].invoiceAmountTax1!=null
              if(response!=null && response[0]!=null){
                let rmon = response[0].invoiceAmountTax1;
                let mon = response[0].invoiceAmountTax;
                let ze = parseFloat(mon==null?0:mon)+parseFloat(this.form.invoiceAmountTax);
                if(ze>parseFloat(rmon)){
                  this.msgError("开票金额不能大于申请金额");
                  this.subBtn = false;
                  return false;
                }
              }


              if (this.form.id != null) {
                updateFinancial(this.form).then(response => {
                  this.msgSuccess("提交成功");
                  this.open = false;
                  this.getList();
                  this.subBtn = false;
                });
              } else {
                addFinancial(this.form).then(response => {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                  this.subBtn = false;
                });
              }


            });
          }else{
            if (this.form.id != null) {
              updateFinancial(this.form).then(response => {
                this.msgSuccess("提交成功");
                this.open = false;
                this.getList();
                this.subBtn = false;
              });
            } else {
              addFinancial(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.subBtn = false;
              });
            }
          }

        }else{
          this.subBtn = false;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
       const ids = row.id || this.ids;
       const _selectData = this.$refs.itsmDataTable.selection
       let deleSta = 0;
       if(_selectData!=''){
           const flag = false;
           for(let item of _selectData) {
              if(item.status >0){
                 deleSta = 1;
                 break;
               }
           }
           if(deleSta==1){
              this.msgSuccess("请删除未提交的数据");
              return false;
           }
         }
      this.$confirm('是否确认删除本条数据?', "警告", {
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
<style>
.el-upload_ {
    text-align: center;
    cursor: pointer;
    outline: none;
}
</style>
