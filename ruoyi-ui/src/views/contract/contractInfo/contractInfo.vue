<!-- 审批详情弹出页 -->
<template>
    <el-dialog title="合同信息详情" :visible.sync="modalShow" width="80%" :before-close="modalClose">
     <el-form ref="form" :model="form" label-width="100px" >
      <div ref="contractInfo">
        <!-- <div id="div1">
          <div style="page-break-after:always;"></div>
          <div id="div1_1"> -->
        <el-row :gutter="15">
          <el-col :span="24" style="margin-top: 0; margin-bottom: 30px;">
            <el-divider content-position="center">
              <el-link type="primary"><b>合同信息</b></el-link>
            </el-divider>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="24">
            <table   class="border-table" cellspacing="0"  >
              <tr height="35px" >
                <td width="30%" >项目类型:{{form.projectCategoryName}}</td>
                <td width="30%" >合同名称:{{form.contractName}}</td>
                <td width="30%" >合同类别:{{form.contractCategoryName}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">付款方式:{{form.payMethodName}}</td>
                <td width="30%">对方单位名称:{{form.contractName}}</td>
                <td width="30%">对方银行账户:{{form.oppositeBank}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">对方银行账号:{{form.oppositeBankId}}</td>
                <td width="30%">对方联系人:{{form.oppositeContacts}}</td>
                <td width="30%">对方联系电话:{{form.oppositePhone}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">对方联系地址:{{form.oppositeAddress}}</td>
                <td width="30%">我方单位名称:{{form.ourName}}</td>
                <td width="30%">我方联系人:{{form.oppositeContacts}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">我方联系电话:{{form.ourPhone}}</td>
                <td width="30%">我方银行账户:{{form.ourBank}}</td>
                <td width="30%">我方银行账号:{{form.ourBankId}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">我方联系地址:{{form.ourAddress}}</td>
                <td width="30%">合同签订日期:{{form.starttime}}</td>
                <td width="30%">合同截止日期:{{form.endtime}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">服务开始日期:{{form.contractServeStart}}</td>
                <td width="30%">服务截止日期:{{form.contractServeEnd}}</td>
                <td width="30%">合同金额:{{form.totalMoney}}</td>
              </tr>
              <tr height="35px">
                <td width="30%">税率(%):{{form.taxrate}}</td>
                <td width="30%">不含税金额:{{form.notTaxMoney}}</td>
                <td width="30%">经办部门:{{form.manageDeptment}}</td>
              </tr>
              <tr height="35px">
                <td width="30%" colspan="4">经办人:{{form.taxrate}}</td>
              </tr>

              <tr height="35px" class="no-print">
                <td width="30%" >
                  <el-form-item label="合同归档资料" prop="archiveFiles">
                    <el-upload ref="field101" :file-list="archiveFiles" :action="field101Action" :headers="token"
                               :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                               :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                    </el-upload>
                  </el-form-item>
                </td>
                <td width="30%">
                  <el-form-item label="合同扫描件" prop="contractScanningFile">
                    <el-upload ref="field101" :file-list="contractScanningFile" :action="field101Action" :headers="token"
                               :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                               :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                    </el-upload>
                  </el-form-item>
                </td>
                <td width="30%">
                  <el-form-item label="合同签订审批流程附件" prop="contractApprovalFile">
                    <el-upload ref="field101" :file-list="contractApprovalFile" :action="field101Action" :headers="token"
                               :on-success="contractApprovalFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"
                               :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">
                    </el-upload>
                  </el-form-item>
                </td>
              </tr>
              <tr height="35px" class="no-print">
                <td width="30%" colspan="4">
                  <el-form-item label="其他附件" prop="otherFiles">
                    <el-upload ref="field101" :file-list="otherFiles" :action="field101Action" :headers="token" :on-success="otherFilesSuccess"
                               :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview"
                               :disabled="true">
                    </el-upload>
                  </el-form-item>
                </td>


              </tr>


            </table>
          </el-col>

        </el-row>

<!--        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="项目类型" prop="projectCategoryName">
              <el-input v-model="form.projectCategoryName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="projectName" v-if="show">
              <el-input v-model="form.projectName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同类别" prop="contractCategoryName">
              <el-input v-model="form.contractCategoryName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="付款方式" prop="payMethodName">
              <el-input v-model="form.payMethodName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对方单位名称" prop="oppositeName">
              <el-input v-model="form.oppositeName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对方银行账户" prop="oppositeBank">
              <el-input v-model="form.oppositeBank" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="对方银行账号" prop="oppositeBankId">
              <el-input v-model="form.oppositeBankId" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对方联系人" prop="oppositeContacts">
              <el-input v-model="form.oppositeContacts" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对方联系电话" prop="oppositePhone">
              <el-input v-model="form.oppositeBank" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="对方联系地址" prop="oppositeAddress">
              <el-input v-model="form.oppositeAddress" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="我方单位名称" prop="ourName">
              <el-input v-model="form.ourName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="我方联系人" prop="oppositeContacts">
              <el-input v-model="form.oppositeContacts" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="我方联系电话" prop="ourPhone">
              <el-input v-model="form.ourPhone" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="我方银行账户" prop="ourBank">
              <el-input v-model="form.ourBank" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="我方银行账号" prop="ourBankId">
              <el-input v-model="form.ourBankId" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="我方联系地址" prop="ourAddress">
              <el-input v-model="form.ourAddress" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同签订日期" prop="starttime">
              <el-input v-model="form.starttime" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同截止日期" prop="endtime">
              <el-input v-model="form.endtime" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="服务开始日期" prop="contractServeStart">
              <el-input v-model="form.contractServeStart" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="服务截止日期" prop="contractServeEnd">
              <el-input v-model="form.contractServeEnd" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同金额" prop="totalMoney">
              <el-input v-model="form.totalMoney" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="税率(%)" prop="taxrate">
              <el-input v-model="form.taxrate" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不含税金额" prop="notTaxMoney">
              <el-input v-model="form.notTaxMoney" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经办部门" prop="manageDeptment">
              <el-input v-model="form.manageDeptmentName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="经办人" prop="manageUser">
              <el-input v-model="form.manageUserName" :readonly="true" class="input-noborder" />
            </el-form-item>
          </el-col>
        </el-row> -->
<!--        <div class="no-print">-->
<!--          <el-row :gutter="15">-->
<!--            <el-col :span="8">-->
<!--              <el-form-item label="合同归档资料" prop="archiveFiles">-->
<!--                <el-upload ref="field101" :file-list="archiveFiles" :action="field101Action" :headers="token"-->
<!--                  :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"-->
<!--                  :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">-->
<!--                </el-upload>-->
<!--              </el-form-item>-->
<!--            </el-col>-->
<!--            <el-col :span="8">-->
<!--              <el-form-item label="合同扫描件" prop="contractScanningFile">-->
<!--                <el-upload ref="field101" :file-list="contractScanningFile" :action="field101Action" :headers="token"-->
<!--                  :on-success="contractScanningFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"-->
<!--                  :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">-->
<!--                </el-upload>-->
<!--              </el-form-item>-->
<!--            </el-col>-->
<!--          </el-row>-->
<!--          <el-row :gutter="15">-->
<!--            <el-col :span="8">-->
<!--              <el-form-item label="合同签订审批流程附件" prop="contractApprovalFile">-->
<!--                <el-upload ref="field101" :file-list="contractApprovalFile" :action="field101Action" :headers="token"-->
<!--                  :on-success="contractApprovalFileSuccess" :auto-upload="autoUploadFalg" multiple :limit="20"-->
<!--                  :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview" :disabled="true">-->
<!--                </el-upload>-->
<!--              </el-form-item>-->
<!--            </el-col>-->
<!--            <el-col :span="8">-->
<!--              <el-form-item label="其他附件" prop="otherFiles">-->
<!--                <el-upload ref="field101" :file-list="otherFiles" :action="field101Action" :headers="token" :on-success="otherFilesSuccess"-->
<!--                  :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview"-->
<!--                  :disabled="true">-->
<!--                </el-upload>-->
<!--              </el-form-item>-->
<!--            </el-col>-->

<!--          </el-row>-->
<!--        </div>-->
        <el-row :gutter="15">
          <el-col :span="24" style="margin-top: 0; margin-bottom: 30px;">
          <el-table :data="form.ctBuContractIncomeTypeList" border style="width:100%">
            <el-table-column label="收入类型" prop="incomeTypeId" header-align="center" align="center">
              <template slot-scope="s">
                <p v-if="s.row.incomeTypeId == 1">一次性</p>
                <p v-if="s.row.incomeTypeId == 2">分期</p>
              </template>
            </el-table-column>
            <el-table-column label="类型金额" prop="incomeTypeMoney" header-align="center" align="center">
            </el-table-column>
          </el-table>
          </el-col>
        </el-row>
        <!-- </div>
          <div style="page-break-after:always;"></div>
          <div id="div1_2"> -->
        <el-row>
          <el-col :span="24" style="margin-top: 15px; margin-bottom: 30px;">
            <el-divider content-position="center">
              <el-link type="primary"><b>合同内容</b></el-link>
            </el-divider>
          </el-col>
        </el-row>

        <el-row :gutter="15">
          <el-col :span="24" >
          <el-table :data="form.ctBuContractContentList" border>
            <el-table-column label="类型名称" prop="contentName" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="类型税率(%)" prop="contentTaxrate" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="含税金额(元)" prop="intaxAmount" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="不含税额(元)" prop="extaxAmount" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="备注" prop="remark" header-align="center" align="center">
            </el-table-column>
          </el-table>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" style="margin-top: 0;  margin-bottom: 30px;">
            <el-divider content-position="center">
              <el-link type="primary"><b>支付信息</b></el-link>
            </el-divider>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="24" >
          <el-table :data="form.ctBuContractPayinfoList" border>
            <el-table-column label="预计付款时间" prop="estimateTime" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="付款条件说明" prop="conditionInfo" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="付款比例" prop="paymentRatio" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="应付金额(元)" prop="estimateMoney" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="发票号" prop="invoiceId" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="发票含税金额(元)" prop="invoiceMoney" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="发票代码" prop="invoiceCode" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="发票时间" prop="invoiceTime" header-align="center" align="center">
            </el-table-column>
          </el-table>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" style="margin-top: 0;  margin-bottom: 30px;">
            <el-divider content-position="center">
              <el-link type="primary"><b>审核信息</b></el-link>
            </el-divider>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="24" >
          <el-table :data="tableData" border>
            <el-table-column label="任务节点" prop="taskNodeName" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="审批人" prop="createName" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="审批意见" prop="controlValue" header-align="center" align="center">
            </el-table-column>
            <el-table-column label="审批时间" prop="createTime" header-align="center" align="center">
            </el-table-column>
          </el-table>
          </el-col>
        </el-row>
        <!-- </div> -->
      </div>

      </el-form>
      <div slot="footer" class="no-print">
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="print_sp">打 印</el-button>
        </div>
      </div>
    </el-dialog>

</template>

<script>
  import {
    findWorkFlowFormData,
    listApplyCollection,
    detailNgcApproval,
    findContract,
    detailhtCon,
    getUserInfo
  } from "@/api/collection/common";
  import {
    listContract,
    getContract,
    delContract,
    addContract,
    updateContract,
    exportContract
  } from '@/api/contract/contractInfo';
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById,
    findLoginbyCompany
  } from "@/api/utils";
  import {
    getByBusinessKey
  } from '@/api/activiti/historyFormdata.js';

  export default {
    name: "index",
    data() {
      return {
        tableData: [], //审批信息
        invoiceDataList: [], //开票信息
        show: false,
        modalShow: false,
        projectCategoryTypeList: [],
        autoUploadFalg: true,
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
        archiveFiles: [],
        contractScanningFile: [],
        contractApprovalFile: [],
        otherFiles: [],
        field101Action: this.$fieldPathAction,
        token: {},
        businessKey: "",
      }
    },
    created() {
      //项目类型
      this.getDicts("content_type").then((response) => {
        this.contentTypeList = response.data;
      });
    },
    methods: {
      init(row) {
        this.form.id = row.id;

        this.tableData = [];
        /** 加入请求头 **/
        // this.$refs.basicInfo.token = {
        //   Authorization: getToken()
        // };
        getContract(this.form.id).then(response => {
          this.form = response.data
          let proName_ = response.data;
          this.modalShow = true;
          getByBusinessKey(this.form.id).then(response1 => {
            this.tableData = response1;
          })


          /** 获取文件列表 **/
          findFileListByPidPtype(this.form.id, 1).then(response => {
            this.archiveFiles = response.data;
          });
          findFileListByPidPtype(this.form.id, 2).then(response => {
            this.contractScanningFile = response.data;
          });
          findFileListByPidPtype(this.form.id, 3).then(response => {
            this.contractApprovalFile = response.data;
          });
          findFileListByPidPtype(this.form.id, 4).then(response => {
            this.otherFiles = response.data;
          });

          //项目类型
          // this.getDicts("content_type").then((response1) => {
          //   for (let item of response1.data) {
          //     if (item.dictValue == response.data.ctBuContractContentList[0].contentType) {
          //       this.form.ctBuContractContentList.contrntType = item.dictLabel;
          //     }
          //   }
          // });
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
      //打印
      print_sp() {

        // var print= document.getElementById('info');
        // var newContent = print.innerHTML;
        // var oldContent = document.body.innerHTML;
        // document.body.innerHTML = newContent;
        //
        // window.print();
        this.$print(this.$refs.contractInfo)
      },


      modalClose() {
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



  .border-table {
    border-collapse: collapse;
    border: none;
    width: 100%;
    vertical-align:middle;
  }
  .border-table td {
    border:1px solid #dfe6ec;
    padding-left: 6px
  }
</style>
