<!-- 审批详情弹出页 -->
<template>
  <div>
     <el-form ref="form" :model="form"  label-width="100px">
          <el-dialog title="收票记录审核" :visible.sync="modalShow" width="80%" :before-close="modalClose">
            <el-row :gutter="15">
              <el-col :span="24" style="margin-top: 0;">
                <el-divider content-position="center" ><el-link type="primary"><b>合同信息</b></el-link></el-divider>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <!-- <el-col :span="8" >
                <el-form-item label="项目编号" prop="proNo" >
                  <el-input v-model="form.proNo" :readonly="true" class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="项目名称" prop="proName">
                  <el-input v-model="form.proName" :readonly="true" class="input-noborder" />
                </el-form-item>
               </el-col> -->
               <el-col :span="8">
                 <el-form-item label="合同编号"    prop="contractNo">
                   <el-input v-model="form.contractNo" :readonly="true"  class="input-noborder" />
                 </el-form-item>
               </el-col>
               <el-col :span="8">
                 <el-form-item label="合同名称" prop="contractName" >
                   <el-input v-model="form.contractName"  :readonly="true"  class="input-noborder" />
                 </el-form-item>
               </el-col>
               <el-col :span="8">
                 <el-form-item label="对方单位名称" prop="oppositeName">
                   <el-input v-model="form.oppositeName" :readonly="true"  class="input-noborder" />
                 </el-form-item>
               </el-col>

            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="对方银行账户" prop="oppositeBank">
                  <el-input v-model="form.oppositeBank" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="对方银行账号" prop="oppositeBankId">
                  <el-input v-model="form.oppositeBankId"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="对方联系人" prop="oppositeContacts">
                  <el-input v-model="form.oppositeContacts" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="对方联系电话" prop="oppositePhone">
                  <el-input v-model="form.oppositeBank" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="对方联系地址" prop="oppositeAddress">
                  <el-input v-model="form.oppositeAddress"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="合同金额" prop="totalMoney">
                  <el-input v-model="form.totalMoney"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="已支付金额" prop="paidMoney">
                  <el-input v-model="form.paidMoney"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="待支付金额" prop="unpaidMoney">
                  <el-input v-model="form.unpaidMoney"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="8">
                <el-form-item label="付款周期" prop="payment_period">
                  <el-input v-model="form.payment_period" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row :gutter="15">
              <el-col :span="24" style="margin-top: 0;">
                <el-divider content-position="center" ><el-link type="primary"><b>发票信息</b></el-link></el-divider>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="发票代码" prop="invoiceCode">
                  <el-input v-model="form.invoiceCode"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="发票号码" prop="invoiceNo">
                  <el-input v-model="form.invoiceNo"    :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="税率" prop="taxrate">
                  <el-input v-model="form.taxrate" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="发票金额（含税）" prop="invoiceNomoney" >
                  <el-input v-model="form.invoiceMoney" :readonly="true" class="input-noborder"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="发票金额（不含税）" prop="invoiceNomoney">
                  <el-input v-model="form.invoiceNomoney" :readonly="true"  class="input-noborder" />
                </el-form-item>
              </el-col>
            </el-row>

           <el-col :span="8">
              <el-form-item label="票据图片" prop="invoiceVoucher">
                <el-upload ref="field101" :file-list="invoiceVoucher" :action="field101Action" :headers="token"
                  :on-preview="handlePictureCardPreview">
                </el-upload>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="其他附件" prop="unitQualification">
                <el-upload ref="field101" :file-list="unitQualification" :action="field101Action" :headers="token"
                  :on-preview="handlePictureCardPreview">
                </el-upload>
              </el-form-item>
            </el-col>


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
	import {
		listTicketRecord,
		getTicketRecord,
		delTicketRecord,
		addTicketRecord,
		updateTicketRecord,
		exportTicketRecord
	} from "@/api/paymentManagement/ticketRecord";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById
  } from "@/api/utils";
  import { getByBusinessKey } from '@/api/activiti/historyFormdata.js';
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

            },
            token: {},
            field101Action: this.$fieldPathAction,
            invoiceVoucher: [],
            unitQualification: [],
            autoUploadFalg: true,
          }
        },
        created() {},
        methods:{
          init(row){
            console.log(row)
            this.tableData=[];
            getTicketRecord(row.businessKey).then(response => {
              this.modalShow = true;
              this.form = response.data
              let proName_ = response.data;
              /** 获取文件列表 **/
              findFileListByPidPtype(row.businessKey, 1).then(response => {
                this.invoiceVoucher = response.data;
              });
              findFileListByPidPtype(row.businessKey, 2).then(response => {
                this.unitQualification = response.data;
              });

              getByBusinessKey(row.businessKey).then(response1 =>{
                this.tableData = response1;
                console.log( this.tableData)
              })

            })
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
