<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane label="发票收款"  name="base" prop="appNgBase">
        <fpsq
          ref="fpsq"

        ></fpsq>
      </el-tab-pane>
      <el-tab-pane label="预收款"  name="second" prop="appNgMaterialList"
      >
        <ysk
          ref="ysk"

        ></ysk>
      </el-tab-pane>

    </el-tabs>


</div>
</template>

<script>

  import fpsq from "./fpsq.vue";
  import ysk from "./index_.vue";
import { listFinancialList ,listReceiptInfo, getReceiptInfo, delReceiptInfo, addReceiptInfo, updateReceiptInfo, exportReceiptInfo } from "@/api/collection/receiptInfo";
import { listApplyCollection,detailNgcApproval,findContract,detailhtCon,getUserInfo  } from "@/api/collection/common";
import { listFinancial } from "@/api/collection/voucher";
import { getToken } from "@/utils/auth";
import { findProjectType, findBranchOffice, delFileById, findFileListByPid, findFileListByPidPtype, downloadfileById} from "@/api/utils";

export default {
  name: "ReceiptInfo",
  components: {
    fpsq,ysk
  },
  data() {
    return {
      activeName: "base",
      voucher:[],
      uploadVis:true,
      dialogVisible:false,
      dialogImageUrl:null,
      butVis:true,
      autoUploadFalg: true,
      field101Action: this.$fieldPathAction,
      token: {},
      //收款类型
      payTypeList:[],
      // 财务开票表格数据
      financialList: [],
       projectCategoryTypeList:[],
       //回款方式
       collectMethodList:[],
      show: false,
      innerVisible: false,
      innerhtVisible: false,
      loadings:false,
      // 遮罩层
      loading: true,
      // 项目信息列表
      projectList: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
       innerhtVisible: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 收款单录入表格数据
      receiptInfoList: [],
      contractList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
       htloading:true,
      open: false,
      statusList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        approvalId: null,
        proNo: null,
        proName: null,
        contractId: null,
        contractNo: null,
        contractName: null,
        invoiceId: null,
        invoiceCode: null,
        invoiceNo: null,
        collectionTime: null,
        collectionAmount: null,
        documentNumber: null,
        paymentMethod: null,
        voucher: null,
        status: null,
        statusps: null,

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    //项目类型列表
    this.getDicts("project_category_type").then((response) => {
      this.approvalTypeOptions = response.data;
      this.projectCategoryTypeList = response.data;
    });
    //项目类型列表
    this.getDicts("collection_method_type").then((response) => {
      this.collectMethodList = response.data;
    });
    //收款类型列表
    this.getDicts("sys_pay_type").then((response) => {
      this.approvalTypeOptions = response.data;
      this.payTypeList = response.data;
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
    /** 文件上传检测 **/
    field101BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 20;
      if (!isRightSize) {
        this.$message.error('文件大小超过 20MB');
      }
      return isRightSize;
    },
    /** 上传附件成功操作 **/
    contractApprovalFileSuccess(res, file, fileList) {
      for (let index = 0; index < fileList.length; index++) {
        const element = fileList[index];
        if (element.uid == file.uid) {
          element.uid = res.msg;
        }
      }
      if (this.form.voucher) {
        this.form.voucher += res.msg + ",";
      } else {
        this.form.voucher = res.msg + ",";
      }
    },
    /** 删除附件操作 **/
    removeContractApprovalFile(file, fileList) {
      delFileById(file.uid).then(response => {
        if (response.code == 200) {
          this.form.voucher = null;
          for (let index = 0; index < fileList.length; index++) {
            const element = fileList[index];
            if (this.form.voucher) {
              this.form.voucher += element.uid + ",";
            } else {
              this.form.voucher = element.uid + ",";
            }
          }
        }
      });
    },
    handleRemove(file, fileList) {
            console.log(file, fileList);
          },
          handlePreview(file) {
            console.log(file);
          },
          handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
          },
          beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${ file.name }？`);
          },

// 多选框选中数据
    handleXmSelectionChange(val) {
       this.ids = val.map(item => item.id)
       this.statusList =  val.map(item => item.status)
       if (val.length > 1) {
       		this.$refs.Table.clearSelection()
       		this.$refs.Table.toggleRowSelection(val.pop())
       	} else {
       	}

    },
  /** 搜索按钮操作 */
    handlesQuery() {
      this.queryParams.pageNum = 1;
      this.getApplyList();
    },
    /** 搜索按钮操作 */
    handlehtQuery() {
      this.queryParams.pageNum = 1;
      this.getContractList();
    },
//获取项目信息
    getApplyList() {
      this.projectList = [];
      this.loadings = true;
      this.innerVisible = true;
      this.queryParams.pageNum = 1;
      this.apptype=this.form.approvalId;
    //  this.queryParams.approvalType = this.form.approvalType;
      listApplyCollection(this.form.approvalId,this.addDateRange(this.queryParams, this.dateRange)).then(response => {
         this.total = response.total;
         if(this.total==0){
           this.projectList = [];
         }else{
           this.projectList = response.rows;
         }


        this.loadings = false;

        this.innerVisible = true;
          this.title = "修改发票申请";
          this.apptype=this.form.approvalId;
      });
    },
    //获取合同信息
    getContractList() {
      this.htloading = true;
      this.innerhtVisible = true;
      this.queryParams.projectId = (this.form.approvalId);
      console.log(this.queryParams);
      this.queryParams={};
      this.queryParams.status = 2;
      findContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.contractList = response.rows;
        this.total = response.total;
        this.htloading = false;
         this.innerhtVisible = true;
      });
    },
    /** 查询收款单录入列表 */
    getList() {
      this.loading = true;
      this.queryParams.status = 1;
      this.queryParams.createBy = '';
      listFinancialList(this.queryParams).then(response => {
        this.financialList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      // this.loading = true;
      // listReceiptInfo(this.queryParams).then(response => {
      //   this.receiptInfoList = response.rows;
      //   this.total = response.total;
      //   this.loading = false;
      // });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelAppri() {
      this.innerVisible = false;
      this.form.reqNo = null;
      this.form.proName = null;
    },
    /** 修改按钮操作 */
    submitsForm(row) {
      const id = row.id || this.ids;
      detailNgcApproval(this.apptype,id).then(response => {
       this.form.approvalId=id;
       this.form.proNo=response.data.reqNo;
       this.form.proName=response.data.proName;
       this.innerVisible = false;
      });
    },
    /** 选择合同 */
    submithtForm(row) {
      const id = row.id || this.ids;
      this.taxrate = row.taxrate;
      this.form.contractId=row.id;
      this.form.contractNo=row.contractNo;
      this.form.contractName=row.contractName;
      //项目信息
      this.form.approvalId=row.projectCategory+"";
      this.form.proNo=row.projectNo;
      this.form.proName=row.projectName;
      // if( this.form.approvalType==''){
      //   this.form.approvalType=row.projectCategory;
      // }
      this.form.payerName = row.oppositeName;
      this.form.payerAddress = row.oppositeAddress;
      this.form.creditCode = row.oppositeCreditCode;
      this.form.payerPhone = row.oppositePhone;
      this.form.payerOpeningBank = row.oppositeBank;
      this.form.payerAccount = row.oppositeBankId;

      // this.form.approvalType = row.projectCategory;
      this.form.contractCategoryName = row.contractCategoryName;
      this.form.contractCategory = row.contractCategory;
      this.innerhtVisible = false;


    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        approvalId: null,
        proNo: null,
        proName: null,
        contractId: null,
        contractNo: null,
        contractName: null,
        invoiceId: null,
        invoiceCode: null,
        invoiceNo: null,
        collectionTime: null,
        collectionAmount: null,
        documentNumber: null,
        paymentMethod: null,
        voucher: null,
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
    resetForm() {
    //  this.form.resetFields();
      /** 加入请求头 **/
      this.token = { Authorization: getToken() };
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
       this.statusList = selection.map(item => item.status)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收款单录入";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      let id = row.id || this.ids
      let approvalId_ = '';
      let proName_ ='';
      let proNo_ = '';
      let contractId_ = '';
      let contractNo_='';
      let contractName_='';
      let invoiceId_='';
      let invoiceCode_='';
      let invoiceNo_='';
      this.form.payType = 2+"";
      this.form.payTypeName="发票付款";
      if(id==''){
        const _selectData = this.$refs.financialList.selection;
        if(_selectData==''){
           //添加预付款
          this.reset();
          this.open = true;
          this.title = "添加收款单录入";
          this.form.payType = 1+"";
          this.form.payTypeName="预付款";
        }else{
          if(_selectData.length>1){
            this.msgError("只能选择一条记录");
            return false;
          }
          let status = _selectData[0].status;
          if(status>0){
            this.butVis = false;
            this.uploadVis = false;
          }
          this.queryParams.status = 1;
          this.queryParams.id_ = _selectData[0].ctBuInvoiceReqVo.id_;

          listFinancialList(this.queryParams).then(response => {
            this.token = { Authorization: getToken() };
            this.form.approvalId = (response.rows[0].ctBuInvoiceReqVo.approvalType_)+'';
            this.form.proNo = response.rows[0].ctBuInvoiceReqVo.proNo_;
            this.form.proName = response.rows[0].ctBuInvoiceReqVo.proName_;
            this.form.contractId = response.rows[0].ctBuInvoiceReqVo.contractId_;
            this.form.contractNo = response.rows[0].ctBuInvoiceReqVo.contractNo_;
            this.form.contractName = response.rows[0].ctBuInvoiceReqVo.contractName_;
            // this.form.invoiceId = response.rows[0].ctBuInvoiceReqVo.id_;
            this.form.invoiceId = response.rows[0].ctBuInvoiceInfoVo.ids;
            this.form.invoiceCode = response.rows[0].ctBuInvoiceInfoVo.invoiceCodes;
            this.form.invoiceNo =response.rows[0].ctBuInvoiceInfoVo.invoiceNos;
            this.form.ids =response.rows[0].ctBuInvoiceInfoVo.ids;
            this.open = true;
            this.title = "收款单录入";

          });
        }
      }else{
        getReceiptInfo(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "收款单录入";

          let status = response.data.status;
          if(status>0){
              this.butVis = false;
              this.uploadVis = false;
          }
        });
      }

 /** 获取文件列表 **/
      if(id!='' && id!=null){
        findFileListByPidPtype(id,1).then(response => {
          this.voucher= response.data;
        });
      }
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.status=val;
          if (this.form.id != null) {
            updateReceiptInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReceiptInfo(this.form).then(response => {
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
      const status = row.status || this.statusList;
      if(status!=null && status.length>0){
        for(let item  of status){
          if(item>0){
             this.msgSuccess("请删除为提交数据");
             return false;
          }
        }
      }
      this.$confirm('是否确认删除选中的数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delReceiptInfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有收款单录入数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportReceiptInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
<style>
  .el-tabs__item {
    padding: 0 20px;
    height: 40px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    line-height: 40px;
    display: inline-block;
    list-style: none;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    position: relative;
  }
</style>
