<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['collection:financial:edit']"
        >凭证单录入</el-button>
      </el-col>

	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

      <el-table border v-loading="loading" ref="financialList"  :data="financialList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="show"/>
        <el-table-column label="主键" align="center" v-if="show" prop="ctBuInvoiceReq.instanceId" />
       <el-table-column label="项目名称" align="center" prop="ctBuInvoiceReq.proName" />
       <el-table-column label="合同名称" align="center" prop="ctBuInvoiceReq.contractName" />
       <el-table-column label="发票类型" align="center" prop="ctBuInvoiceReq.invoiceType" >
        <template slot-scope="s">
           <p v-if="s.row.ctBuInvoiceReq.invoiceType == 1">增值税专票</p >
           <p v-if="s.row.ctBuInvoiceReq.invoiceType == 2">增值税普票</p >
          </template>
        </el-table-column>
       <el-table-column label="申请部门" align="center" prop="ctBuInvoiceReq.reqDeptname" />
       <el-table-column label="申请人" align="center" prop="ctBuInvoiceReq.reqUsername" />
       <el-table-column label="申请日期" align="center" prop="ctBuInvoiceReq.reqDate" />
       <el-table-column label="付款单位名称" align="center" width="150px"  prop="ctBuInvoiceReq.payerName" />
       <el-table-column label="开票金额" align="center" prop="ctBuInvoiceReq.invoiceAmountTax" />

        <el-table-column label="发票代码" align="center" prop="invoiceCode" />
        <el-table-column label="发票号码" align="center" prop="invoiceNo" />
        <el-table-column label="发票开具日期" align="center" prop="invoiceDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.invoiceDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开票人" align="center" prop="invoiceDrawer" />
        <el-table-column label="复核人" align="center" prop="invoiceReviewer" />
        <el-table-column label="收款人" align="center" prop="invoicePayee" />
        <el-table-column label="发票金额" align="center" prop="invoiceAmountTax" />
        <el-table-column label="金额" align="center" prop="invoiceAmountNtax" />
        <el-table-column label="移交人" align="center" prop="handoverUsername" />
        <el-table-column label="移交时间" align="center" prop="handoverTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.handoverTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="确认人" align="center" prop="confirmerUsername" />
        <el-table-column label="确认时间" align="center" prop="confirmerTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.confirmerTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="凭证号" align="center" prop="voucherNo" />
        <el-table-column label="凭证号录入人" width="150px" align="center" prop="voucherUsername" />
        <el-table-column label="凭证号录入时间" align="center" prop="voucherTime" width="180">
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
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-input v-model="form.id" placeholder="" v-if="show"/>

        <el-form-item label="所属分公司" label-width="121px" prop="voucherDept">
          <el-input v-model="form.voucherDept" placeholder="请输入凭证号"  :readOnly="true" />
        </el-form-item>
        <el-form-item label="日期" prop="voucherTime">

          <el-date-picker clearable size="small" :style="{ width: '100%' }"
                          v-model="form.voucherTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>

        </el-form-item>

        <el-form-item label="凭证号" prop="voucherNo">
        <el-input type="textarea" v-model="form.voucherNo" placeholder="请输入凭证号" />
      </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="showBtn" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFinancial, getFinancial, delFinancial, moveFinancial,addFinancial, updateFinancial, exportFinancial, voucherWarning } from "@/api/collection/financial";

export default {
  name: "Financial",
  data() {
    return {
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询财务开票列表 */
    getList() {
      this.loading = true;
      voucherWarning().then(response => {
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
      console.log(_selectData);
      if(_selectData[0].status>=4){
       this.showBtn = false;

      }else{
        this.showBtn = true;
      }
      //测试
     let rid = "xxxxxxxxxxx";
      getFinancial(_selectData[0].id,rid).then(response => {
        console.log(response.data);//Remark
        this.form = response.data;
        if(this.form.id==null || this.form.id==''){
          this.form.id = _selectData[0].id;
        }
        this.open = true;
        this.title = "凭证单录入";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.voucherNo==null || this.form.voucherNo ==''){
            this.msgSuccess("不能为空");
            return false;
          }
          this.form.status = 4;
          let  id_ = this.form.id;

          let no_=this.form.voucherNo;
          let dates=this.form.voucherTime;
          let voucherDept=this.form.voucherDept;
          this.$confirm('是否确认录入内容,录入后将不能修改?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              return moveFinancial(id_,no_,dates,voucherDept);
            }).then(() => {
              this.open = false;
              this.getList();
              this.msgSuccess("录入成功");
            })
          // moveFinancial(this.form.id,this.form.voucherNo).then(response => {
          //   this.msgSuccess("修改成功");
          //   this.open = false;
          //   this.getList();
          // });
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
    }
  }
};
</script>
