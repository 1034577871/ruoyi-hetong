<!-- 审批详情弹出页 -->
<template>
  <div>
     <el-form ref="form" :model="form"  label-width="100px">
          <el-dialog title="发票申请" :visible.sync="modalShow" width="80%" :before-close="modalClose">
            <el-row :gutter="15">
              <el-col :span="24" style="margin-top: 0;">
                <el-divider content-position="center" ><el-link type="primary"><b>申请信息</b></el-link></el-divider>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8" >
                <el-form-item label="项目类型" prop="ApprovalType" >
                  <el-input v-model="form.approvalType" :readonly="true" class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="项目编号" prop="proNo" v-if="show">
                  <el-input v-model="form.proNo" :readonly="true" class="input-noborder" />
                </el-form-item>
               </el-col>
               <el-col :span="8">
                <el-form-item label="项目名称" prop="proName" >
                  <el-input v-model="form.proName"  :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="合同名称"    prop="contractName">
                  <el-input v-model="form.contractName" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">
               <el-col :span="8">
                 <el-form-item label="事前审批表ID 关联事前审批表ID" prop="approvalId" v-if="show">
                    <el-input v-model="form.contractId" class="input-noborder"/>
                 </el-form-item>
                <el-form-item label="合同编号"  v-if="show"   prop="contractNo">
                    <el-input v-model="form.contractNo"  :readonly="true" class="input-noborder" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="发票种类" prop="invoiceType" >
                  <el-input v-model="form.invoiceType"  :readonly="true"  class="input-noborder" />
                </el-form-item>
               </el-col>
               <el-col :span="8">
                <el-form-item label="部门名称" prop="reqDeptname">
                  <el-input v-model="form.reqDeptname"   :readonly="true"  class="input-noborder"/>
                </el-form-item>
               </el-col>
               <el-col :span="8">
                 <el-form-item label="申请人姓名" prop="reqUsername">
                   <el-input v-model="form.reqUsername"  :readonly="true"  class="input-noborder" />
                 </el-form-item>
               </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="部门编号" v-if="show" prop="reqDeptid">
                  <el-input v-model="form.reqDeptid"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>

              <el-col :span="8">

              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item  label="申请人编号" v-if="show" prop="reqUserid">
                  <el-input v-model="form.reqUserid"   :readonly="true"  class="input-noborder"/>
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="申请日期" prop="reqDate">
                  <el-input v-model="form.reqDate"  :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位名称" prop="payerName">
                  <el-input v-model="form.payerName"  :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位地址" prop="payerAddress">
                  <el-input v-model="form.payerAddress"  :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">

              <el-col :span="8">
                <el-form-item label="纳税人识别号" prop="creditCode" >
                  <el-input v-model="form.creditCode" :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位电话"    prop="payerPhone">
                  <el-input v-model="form.payerPhone"   :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位开户行"    prop="payerOpeningBank">
                  <el-input v-model="form.payerOpeningBank" :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">

              <el-col :span="8">
                <el-form-item label="单位账号"    prop="payerAccount">
                  <el-input v-model="form.payerAccount" :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="开票金额(含税)" prop="invoiceAmountTax" label-width="120px">
                  <el-input v-model="form.invoiceAmountTax" :readonly="true" class="input-noborder" @blur='takeAcc(this)' oninput="value=value.replace(/[^0-9.]/g,'')"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="开票金额(不含税)"  prop="invoiceAmountNtax" label-width="130px">
                  <el-input v-model="form.invoiceAmountNtax"  :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">

                  <el-col :span="8">
                    <el-form-item label="预计回款时间" prop="expectedCollectionTime" >
                      <el-input v-model="form.expectedCollectionTime" :readonly="true" class="input-noborder"/>
                    </el-form-item>
                  </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" style="margin-top: 0;">
               <el-divider content-position="center" ><el-link type="primary"><b>开票信息</b></el-link></el-divider>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-table :data="invoiceDataList">
                <el-table-column label="项目名称" prop="invoiceProName" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="规格" prop="invoiceSpec" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="型号" prop="invoiceType" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="单价" prop="invoicePrice"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="数量" prop="invoiceMount"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="金额(含税)" prop="invoiceAmountTax"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="税率" prop="invoiceTaxrate"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="金额(不含税)" prop="invoiceAmountNtax"   header-align="center" align="center">
                </el-table-column>

              </el-table>
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
     </el-form>
  </div>
</template>

<script>
  import { findWorkFlowFormData ,listApplyCollection,detailNgcApproval,findContract,detailhtCon,getUserInfo  } from "@/api/collection/common";
    import { historyFromData,getCollection } from '@/api/activiti/historyFormdata'
    export default {
        name: "index",
        data() {
            return {
               tableData:[], //审批信息
               invoiceDataList:[], //开票信息
              show:false,
                modalShow:false,
                 projectCategoryTypeList:[],
                 form: {
                   proNo: null,
                    proName:null,
                    contractNo:null,
                    contractName:null,
                    reqUsername:null,
                    reqDeptname:null,
                    reqDate:null,
                    invoiceAmountTax:null,
                    invoiceAmountNtax:null,
                    businessKey:null
                  },
            }
        },
        methods:{
            init(row){
             this.tableData=[];
             this.invoiceDataList=[];
              this.form.businessKey = row.businessKey;
             getCollection(this.form.businessKey).then(response => {
                 this.form = response.data
                 let proName_ = response.data;
                this.modalShow = true;
                 this.tableData = response.data.actWorkflowFormData;
             //项目类型列表
                this.getDicts("project_category_type").then((response1) => {
                  for(let item of response1.data) {
                    if(item.dictValue==proName_.approvalType){
                      this.form.approvalType = item.dictLabel;
                    }
                  }
                });
             
                this.getDicts("bill_type").then((response2) => {
                  for(let item of response2.data) {
                    if(item.dictValue==proName_.invoiceType){
                      this.form.invoiceType = item.dictLabel;
                    }
                  }
                });
                this.invoiceDataList = [];
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
            modalClose(){
                this.modalShow = false;
            }
        },
    }

</script>

<style scoped lang="scss">
  ::v-deep .input-noborder {
    .el-input__inner {
      border: 0;
      background-color: #f8f8f9;
    }
  }
  ::v-deep .el-dialog {
    .el-dialog__body {
      padding: 0px 20px !important;
    }
  }
</style>
