<template>
  <div>
    <el-row :gutter="15">
      <el-col :span="24" style="margin-top: 0;">
        <el-divider content-position="center" ><el-link type="primary"><b>合同信息</b></el-link></el-divider>
      </el-col>
    <el-form ref="form" :model="form"  label-width="100px">
	        <el-row :gutter="15">
	          <el-col :span="8" >
	            <el-form-item label="项目类型" prop="projectCategoryName" >
	              <el-input v-model="form.projectCategoryName" :readonly="true" class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="项目名称" prop="projectName" v-if="show">
	              <el-input v-model="form.projectName" :readonly="true" class="input-noborder" />
	            </el-form-item>
	           </el-col>
	           <el-col :span="8">
	            <el-form-item label="合同名称" prop="contractName" >
	              <el-input v-model="form.contractName"  :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="合同类别"    prop="contractCategoryName">
	              <el-input v-model="form.contractCategoryName" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	        </el-row>
	        <el-row :gutter="15">
	          <el-col :span="8">
	            <el-form-item label="付款方式" prop="payMethodName">
	              <el-input v-model="form.payMethodName" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="对方单位名称" prop="oppositeName">
	              <el-input v-model="form.oppositeName" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="对方银行账户" prop="oppositeBank">
	              <el-input v-model="form.oppositeBank" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	        </el-row>
	        <el-row :gutter="15">
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
	          <el-col :span="8">
	            <el-form-item label="对方联系电话" prop="oppositePhone">
	              <el-input v-model="form.oppositeBank" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	        </el-row>
	        <el-row :gutter="15">
	          <el-col :span="8">
	            <el-form-item label="对方联系地址" prop="oppositeAddress">
	              <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="form.oppositeAddress" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="我方单位名称" prop="ourName">
	              <el-input v-model="form.ourName" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="我方联系人" prop="oppositeContacts">
	              <el-input v-model="form.oppositeContacts" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	        </el-row>

	        <el-row :gutter="15">
	          <el-col :span="8">
	            <el-form-item label="我方联系电话" prop="ourPhone">
	              <el-input v-model="form.ourPhone"    :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="我方银行账户" prop="ourBank">
	              <el-input v-model="form.ourBank" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	          <el-col :span="8">
	            <el-form-item label="我方银行账号" prop="ourBankId">
	              <el-input v-model="form.ourBankId" :readonly="true"  class="input-noborder" />
	            </el-form-item>
	          </el-col>
	        </el-row>

	          <el-row :gutter="15">
	            <el-col :span="8">
	              <el-form-item label="我方联系地址" prop="ourAddress">
	                <el-input v-model="form.ourAddress"    :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="合同签订日期" prop="starttime" >
	                <el-input v-model="form.starttime" :readonly="true" class="input-noborder"/>
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="合同截止日期" prop="endtime">
	                <el-input v-model="form.endtime" :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	          </el-row>

	          <el-row :gutter="15">
	            <el-col :span="8">
	              <el-form-item label="服务开始日期" prop="contractServeStart">
	                <el-input v-model="form.contractServeStart"    :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="服务截止日期" prop="contractServeEnd" >
	                <el-input v-model="form.contractServeEnd" :readonly="true" class="input-noborder"/>
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="合同标的额" prop="totalMoney">
	                <el-input v-model="form.totalMoney" :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	          </el-row>

	          <el-row :gutter="15">
	            <el-col :span="8">
	              <el-form-item label="税率(%)" prop="taxrate">
	                <el-input v-model="form.taxrate"    :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="不含税金额" prop="notTaxMoney" >
	                <el-input v-model="form.notTaxMoney" :readonly="true" class="input-noborder"/>
	              </el-form-item>
	            </el-col>
	            <el-col :span="8">
	              <el-form-item label="经办部门" prop="manageDeptmentName">
	                <el-input v-model="form.manageDeptmentName" :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>
	          </el-row>

	          <el-row :gutter="15">
	            <el-col :span="8">
	              <el-form-item label="经办人" prop="manageUserName">
	                <el-input v-model="form.manageUserName"    :readonly="true"  class="input-noborder" />
	              </el-form-item>
	            </el-col>

	          </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="合同归档资料" prop="archiveFiles">
                  <el-upload ref="field101" :file-list="archiveFiles" :action="field101Action" :headers="token"
                    :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                    :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="合同扫描件" prop="contractScanningFile">
                  <el-upload ref="field101" :file-list="contractScanningFile" :action="field101Action" :headers="token"
                    :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                    :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="合同签订审批流程附件" prop="contractApprovalFile">
                  <el-upload ref="field101" :file-list="contractApprovalFile" :action="field101Action" :headers="token"
                    :on-success="contractApprovalFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                    :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="其他附件" prop="otherFiles">
                  <el-upload ref="field101" :file-list="otherFiles" :action="field101Action" :headers="token" :on-success="otherFilesSuccess"
                    :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview"
                    :disabled="true">
                  </el-upload>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row>
              <el-col :span="24" style="margin-top: 0;">
               <el-divider content-position="center" ><el-link type="primary"><b>合同内容</b></el-link></el-divider>
              </el-col>
            </el-row>

            <el-row :gutter="15">
              <el-table :data="form.ctBuContractContentList" border>
                <!-- <el-table-column label="项目类型" prop="contentType" header-align="center" align="center">
                  <template slot-scope="s">
                  	<p v-if="s.row.contentType == 1">服务类</p>
                  	<p v-if="s.row.contentType == 2">采购类</p>
                    <p v-if="s.row.contentType == 3">工程类</p>
                  </template>
                </el-table-column> -->
                <el-table-column label="项目类型" prop="contentName" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="类型税率(%)" prop="contentTaxrate" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="含税金额(元)" prop="intaxAmount"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="不含税额(元)" prop="extaxAmount"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="备注" prop="remark"   header-align="center" align="center">
                </el-table-column>
              </el-table>
            </el-row>
            <el-row>
              <el-col :span="24" style="margin-top: 0;">
               <el-divider content-position="center" ><el-link type="primary"><b>支付信息</b></el-link></el-divider>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-table :data="form.ctBuContractPayinfoList" border>
                <el-table-column label="预计付款时间" prop="estimateTime" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="付款条件说明" prop="conditionInfo" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="付款比例" prop="paymentRatio" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="应付金额(元)" prop="estimateMoney"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="发票号" prop="invoiceNo"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="发票含税金额(元)" prop="invoiceMoney"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="发票代码" prop="invoiceCode"   header-align="center" align="center">
                </el-table-column>
                <el-table-column label="发票时间" prop="invoiceTime"   header-align="center" align="center">
                </el-table-column>
              </el-table>
            </el-row>


	   </el-form>
   </el-row>
  <el-row :gutter="15">
    <el-col :span="24" >
      <el-divider content-position="center" ><el-link type="primary"><b>审核信息</b></el-link></el-divider>
    </el-col>
  </el-row>

  <el-card class="box-card" v-for="(historyData, index) in fromData"  :key="index" >
    <div slot="header" class="clearfix">
      <span>{{historyData.taskNodeName}}</span>
      <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
    </div>
    <el-alert  type="success"  :closable="false" style="margin-bottom: 10px;">
      <span class="in-text">审批人：{{historyData.createName}}</span>
      <span class="in-text">审批时间：{{historyData.createdDate}}</span>
    </el-alert>
    <el-form v-for="(fistoryFormData, indexH) in historyData.formHistoryDataDTO" :key="indexH" label-width="80px">
      <el-form-item :label=fistoryFormData.title >
        <el-input v-model="fistoryFormData.value"/>
      </el-form-item>
    </el-form>
  </el-card>

</div>
</template>

<script>
  import { findWorkFlowFormData ,listApplyCollection,detailNgcApproval,findContract,detailhtCon,getUserInfo  } from "@/api/collection/common";
  import { listContract, getContract, delContract, addContract, updateContract, exportContract } from '@/api/contract/contractInfo';
  import { getLeave } from '@/api/workflow/leave'
  import { historyFromData,getCollectionAudit } from '@/api/activiti/historyFormdata'
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById,
    findLoginbyCompany
  } from "@/api/utils";
  export default {
    name: "leaveHistoryForm",
    props: {
      businessKey: {
        type: String
      }
    },
    data(){
      return{
        show:false,
        autoUploadFalg: true,
        archiveFiles: [],
        contractScanningFile: [],
        contractApprovalFile: [],
        otherFiles: [],
        field101Action: this.$fieldPathAction,
        token: {},
        // 表单参数
        form: {
          oppositeBank: '',
          projectCategory: null,
          projectCategoryName: null,
          contentType: null,
          projectId: null,
          projectNo: null,
          projectName: null,
          contractNo: null,
          contractName: null,
          month: null,
          contractCategory: null,
          contractCategoryName: null,
          incomeType: null,
          oppositeName: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          ourName: null,
          ourContacts: null,
          ourPhone: null,
          ourBank: null,
          ourBankId: null,
          ourAddress: null,
          starttime: null,
          endtime: null,
          contractServeStart: null,
          serviceTerm: null,
          contractServeEnd: null,
          totalMoney: null,
          taxrate: null,
          notTaxMoney: null,
          payMethod: null,
          payMethodName: null,
          archiveFiles: null,
          manageDeptment: null,
          manageUser: null,
          originalId: null,
          status: null,
          // statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          remarks: null,

          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
         },
        invoiceDataList:[], //开票信息
        fromData:[],
      }
    },
    created() {
      this.getLeave()
      this.historyFromData()
    },
    methods:{
      getLeave() {
        getContract(this.businessKey).then(response => {
          this.form = response.data
          let proName_ = response.data;
          this.modalShow = true;
          console.log(response.data);
          /** 获取文件列表 **/
          findFileListByPidPtype(this.businessKey, 1).then(response => {
            this.archiveFiles = response.data;
          });
          findFileListByPidPtype(this.businessKey, 2).then(response => {
            this.contractScanningFile = response.data;
          });
          findFileListByPidPtype(this.businessKey, 3).then(response => {
            this.contractApprovalFile = response.data;
          });
          findFileListByPidPtype(this.businessKey, 4).then(response => {
            this.otherFiles = response.data;
          });

        })
      },
      historyFromData() {
        historyFromData(this.businessKey).then(response => {
          this.fromData = response.data
        })
      },
      /** 文件上传检测 **/
      field101BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 20;
        if (!isRightSize) {
          this.$message.error('文件大小超过 20MB');
        }
        return isRightSize;
      },
      /** 上传合同归档资料成功操作 **/
      archiveFilesSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.formData.archiveFiles) {
          this.formData.archiveFiles += res.msg + ",";
        } else {
          this.formData.archiveFiles = res.msg + ",";
        }
      },
      /** 删除合同归档资料操作 **/
      removeArchiveFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.formData.archiveFiles = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.formData.archiveFiles) {
                this.formData.archiveFiles += element.uid + ",";
              } else {
                this.formData.archiveFiles = element.uid + ",";
              }
            }
          }
        });
      },
      /** 上传合同扫描件成功操作 **/
      contractScanningFileSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.formData.contractScanningFile) {
          this.formData.contractScanningFile += res.msg + ",";
        } else {
          this.formData.contractScanningFile = res.msg + ",";
        }
      },
      /** 删除合同扫描件操作 **/
      removeContractScanningFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.formData.contractScanningFile = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.formData.contractScanningFile) {
                this.formData.contractScanningFile += element.uid + ",";
              } else {
                this.formData.contractScanningFile = element.uid + ",";
              }
            }
          }
        });
      },
      /** 上传合同签订审批流程附件成功操作 **/
      contractApprovalFileSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.formData.contractApprovalFile) {
          this.formData.contractApprovalFile += res.msg + ",";
        } else {
          this.formData.contractApprovalFile = res.msg + ",";
        }
      },
      /** 删除合同签订审批流程附件操作 **/
      removeContractApprovalFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.formData.contractApprovalFile = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.formData.contractApprovalFile) {
                this.formData.contractApprovalFile += element.uid + ",";
              } else {
                this.formData.contractApprovalFile = element.uid + ",";
              }
            }
          }
        });
      },
      /** 上传其他附件成功操作 **/
      otherFilesSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.formData.otherFiles) {
          this.formData.otherFiles += res.msg + ",";
        } else {
          this.formData.otherFiles = res.msg + ",";
        }
      },
      /** 删除其他附件操作 **/
      removeOtherFiles(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.formData.otherFiles = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.formData.otherFiles) {
                this.formData.otherFiles += element.uid + ",";
              } else {
                this.formData.otherFiles = element.uid + ",";
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
          window.open(url_);
          // var url_ = url + ":9001/profile" + files[1];
          // window.open(url + ":9001/dictionary/fileManage/dwonLoadFile/" + file.uid + '/' + file.ptype);
        }
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
