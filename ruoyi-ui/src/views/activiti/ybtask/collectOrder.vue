<template>
  <div>
    <el-dialog title="发票申请" :visible.sync="modalShow" width="80%" :before-close="modalClose">
    <el-row :gutter="15">
      <el-col :span="24" >
        <el-divider content-position="center" ><el-link type="primary"><b>收款单信息</b></el-link></el-divider>
      </el-col>
	<el-form ref="form" :model="form"  label-width="80px">
	        <el-col :span="8">
	          <el-form-item label="项目类型" prop="approvalId">
	            <el-input v-model="form.approvalId" :readonly="true"  />
	          </el-form-item>
	        </el-col>

	        <el-col :span="8">

	          <el-form-item label="项目编号" prop="proNo">
	            <el-input v-model="form.proNo" :readonly="true"  />
	          </el-form-item>
	         </el-col>
	         <el-col :span="8">
	          <el-form-item label="项目名称" prop="proName">
	            <el-input v-model="form.proName"  :readonly="true"    placeholder="请输入项目名称"  />
	          </el-form-item>
	        </el-col>

	         <el-col :span="8">
	           <el-form-item label="事前审批表ID 关联事前审批表ID" prop="approvalId" v-if="show">
	              <el-input v-model="form.contractId" placeholder="" />
	           </el-form-item>
	          <el-form-item label="合同编号"     prop="contractNo">
	              <el-input v-model="form.contractNo" :readonly="true"  placeholder="请输入合同编号" />
	          </el-form-item>
	        </el-col>
	        <el-col :span="8">
	          <el-form-item
	        label="合同名称"    prop="contractName">
	            <el-input v-model="form.contractName" :readonly="true"  placeholder="请输入合同名称" />
	          </el-form-item>
	        </el-col>
	        <el-form-item label="发票ID" prop="invoiceId"  v-if="show">
	          <el-input v-model="form.invoiceId" placeholder="请输入发票ID" />
	        </el-form-item>
	         <el-col :span="8">
	          <el-form-item label="发票代码" prop="invoiceCode">
	            <el-input v-model="form.invoiceCode" placeholder="请输入发票代码" />
	          </el-form-item>
	         </el-col>
	         <el-col :span="8">
	          <el-form-item label="发票号码" prop="invoiceNo">
	            <el-input v-model="form.invoiceNo" placeholder="请输入发票号码" />
	          </el-form-item>
	         </el-col>
	         <el-col :span="8">
	          <el-form-item label="回款时间" prop="collectionTime">
	            <el-date-picker clearable size="small" :style="{ width: '100%' }"
	              v-model="form.collectionTime"
	              type="date"
	              value-format="yyyy-MM-dd"
	              placeholder="选择回款时间">
	            </el-date-picker>
	          </el-form-item>
	         </el-col>
	         <el-col :span="8">
	          <el-form-item label="回款金额" prop="collectionAmount">
	            <el-input v-model="form.collectionAmount" placeholder="请输入回款金额" />
	          </el-form-item>
	         </el-col>
	         <el-col :span="8">
	          <el-form-item label="单据编号" prop="documentNumber">
	            <el-input v-model="form.documentNumber" placeholder="请输入单据编号" />
	          </el-form-item>
	         </el-col>
	          <el-col :span="8">
	          <el-form-item label="回款方式" prop="paymentMethod">
              <el-input v-model="form.paymentMethod" placeholder="请输入单据编号" />

	          </el-form-item>
	         </el-col>
	        <el-form-item label="状态" v-if="show">
	          <el-radio-group v-model="form.status">
	            <el-radio label="1">请选择字典生成</el-radio>
	          </el-radio-group>
	        </el-form-item>
	        <el-form-item label="状态说明" prop="statusps"  v-if="show">
	          <el-input v-model="form.statusps" placeholder="请输入状态说明" />
	        </el-form-item>
	        <el-form-item label="删除标志" prop="delFlag"  v-if="show">
	          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
	        </el-form-item>
	        <el-form-item label="备注" prop="remark"  v-if="show">
	          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
	        </el-form-item>
	         <el-col :span="8">
	          <el-form-item label="银行单据" prop="remark">
	            <!-- <el-upload
	              name = "form.voucher"
	              class="upload-demo"
	              action="https://jsonplaceholder.typicode.com/posts/"
	              :on-preview="handlePreview"
	              :on-remove="handleRemove"
	              :before-remove="beforeRemove"
	              multiple
	              :limit="3"
	              :on-exceed="handleExceed"
	              :file-list="fileList">
	              <el-button size="small" type="primary">点击上传</el-button>
	              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
	            </el-upload> -->
	          </el-form-item>
	</el-col>

	      </el-form>
         </el-row>
  <el-row :gutter="15">
    <el-col :span="24" >
      <el-divider content-position="center" ><el-link type="primary"><b>审核信息</b></el-link></el-divider>
    </el-col>
  </el-row>

  <el-row :gutter="15">
    <el-table :data="tableData">
      <el-table-column label="任务节点" prop="taskNodeName" header-align="center" align="center">
      </el-table-column>
      <el-table-column label="审批人" prop="createName" header-align="center" align="center">
      </el-table-column>
      <el-table-column label="审批意见" prop="controlValue" header-align="center" align="center">
      </el-table-column>
      <el-table-column label="审批时间" prop="createTime"   header-align="center" align="center">
      </el-table-column>
    </el-table>
  </el-row>
</el-dialog>
</div>
</template>

<script>
  import { getLeave } from '@/api/workflow/leave'
  import { historyFromData,getCollectionAudit } from '@/api/activiti/historyFormdata'
  export default {
    name: "leaveHistoryForm",
    props: {
      businessKey: {
        type: String
      }
    },
    data(){
      return{
        tableData:[],
          modalShow:false,
        show:false,
        // 表单参数
        form: {},
        invoiceDataList:[], //开票信息
        fromData:[],
      }
    },
    created() {
      // this.getLeave()
      // this.historyFromData()
    },
    methods:{
       init(row) {
          this.tableData=[];
        getCollectionAudit(row.businessKey).then(response => {
           this.modalShow = true;
          this.form = response.data
          let proName_ = response.data;
           this.tableData = response.data.actWorkflowFormData;
      //项目类型列表
         this.getDicts("project_category_type").then((response) => {
           for(let item of response.data) {
             if(item.dictValue==proName_.approvalId){
               this.form.approvalId = item.dictLabel;
             }
           }
         });

         this.getDicts("collection_method_type").then((response) => {
           for(let item of response.data) {
             if(item.dictValue==proName_.paymentMethod){
               this.form.paymentMethod = item.dictLabel;
             }
           }
         });
          for(let item of response.data.ctBuInvoiceReqDetail) {
             this.taxrate =   item.invoiceAmountTaxs;
            this.invoiceDataList.push({
              id: item.id,
              invoiceProName: item.invoiceProName,
              invoiceSpec: item.invoiceSpec,
              invoiceType: item.invoiceType,
              invoicePrice: item.invoicePrice,
              invoiceMount: item.invoiceMount,
              invoiceAmountTax: item.invoiceAmountTax,
              invoiceTaxrate: item.invoiceTaxrate,
              invoiceAmountNtax: item.invoiceAmountNtax,
            });
          }



        })
      },
      historyFromData() {
        historyFromData(this.businessKey).then(response => {
          this.fromData = response.data
        })
      },
    }

  }
</script>

<style scoped lang="scss">
  .input-noborder ::v-deep {
    .el-input__inner {
      border: 0;
      background-color: #f8f8f9;
    }
  }
  .el-alert  ::v-deep {
    .el-alert__content {
      width: 100%;
    }
  }
  .box-card {
    width: 50%;
  }
  .in-text {
    display:inline-block;width: 50%;
    font-size: 14px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

</style>
