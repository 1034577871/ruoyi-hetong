<!-- 审批详情弹出页 -->
<template>
  <div>
    <el-form ref="form" :model="form"  label-width="100px">
      <el-dialog title="发票申请" :visible.sync="modalShow" width="80%" :before-close="modalClose">
            <el-row :align="15">
              <el-col :span="8">
                <el-form-item label="收款类型" prop="payType">
                  <el-select :disabled="true" v-model="form.payType" placeholder="请选择收款类型" style="width: 100%;">
                    <el-option
                      v-for="dict in payTypeList"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :align="15">
              <el-col :span="8">
                <el-form-item label="项目类型" prop="approvalId">
                  <el-select :disabled="true" v-model="form.approvalId" placeholder="请选择项目类型" style="width: 100%;">
                    <el-option
                      v-for="dict in projectCategoryTypeList"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="8">

                <el-form-item label="项目编号" prop="proNo">
                  <el-input v-model="form.ids" :readonly="true"   v-if="show"/>
                  <el-input v-model="form.proNo" :readonly="true"  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="项目名称" prop="proName">
                  <el-input v-model="form.proName"  :readonly="true"    placeholder="请输入项目名称"  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :align="15">
              <el-col :span="8">
                <el-form-item label="事前审批表ID 关联事前审批表ID" prop="contractId" v-if="show">
                  <el-input v-model="form.contractId" placeholder="" />
                </el-form-item>
                <el-form-item label="合同编号"     prop="contractNo">
                  <el-input v-model="form.contractNo" :readonly="true"  placeholder="请输入合同编号" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item
                  label="合同名称"    prop="contractName">
                  <el-input v-model="form.contractName" :readonly="true"  placeholder="请输入合同名称" >

                  </el-input>

                </el-form-item>
              </el-col>
              <el-form-item label="发票ID" prop="invoiceId"  v-if="show">
                <el-input v-model="form.invoiceId" placeholder="请输入发票ID" />
              </el-form-item>
              <el-col :span="8">
                <el-form-item label="发票代码" prop="invoiceCode">
                  <el-input v-model="form.invoiceCode" :readonly="true"  placeholder="请输入发票代码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :align="15">
              <el-col :span="8">
                <el-form-item label="发票号码" prop="invoiceNo">
                  <el-input v-model="form.invoiceNo" :readonly="true"  placeholder="请输入发票号码" />
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
                  <el-input v-model="form.collectionAmount" readOnly="true" placeholder="请输入回款金额" oninput="value=value.replace(/[^0-9.]/g,'')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :align="15">
              <el-col :span="8">
                <el-form-item label="单据编号" prop="documentNumber">
                  <el-input v-model="form.documentNumber" placeholder="请输入单据编号" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="回款方式" prop="paymentMethod">

                  <el-select :style="{ width: '100%' }"
                             v-model="form.paymentMethod"
                             placeholder="合同类别"
                  >
                    <el-option
                      v-for="dict in collectMethodList"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    />
                  </el-select>

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
                <el-form-item label="银行单据" prop="voucher">
                  <el-upload
                    :disabled="true"
                    ref="field101"
                    :file-list="voucher"
                    :action="field101Action"
                    :headers="token"
                    :on-success="contractApprovalFileSuccess"
                    :on-remove="removeContractApprovalFile"
                    :auto-upload="autoUploadFalg"
                    multiple
                    :limit="20"
                    :before-upload="field101BeforeUpload"
                    :on-preview="handlePictureCardPreview"
                  >
                    <el-button slot="trigger" size="small"  type="primary" :disabled="true">选取文件</el-button>

                  </el-upload>
                </el-form-item>
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
     </el-form>
  </div>
</template>

<script>
  import { findWorkFlowFormData ,listApplyCollection,detailNgcApproval,findContract,detailhtCon,getUserInfo  } from "@/api/collection/common";
    import { historyFromData,getCollection } from '@/api/activiti/historyFormdata'
  import { getToken } from "@/utils/auth";
  import { findProjectType, findBranchOffice,findFileListByPidPtype,delFileById } from "@/api/utils";
  import { getReceiptInfo } from "@/api/collection/receiptInfo";

  export default {
        name: "index",
        data() {
            return {
              //回款方式
              collectMethodList:[],
              field101Action: this.$fieldPathAction,
              voucher:[],
              uploadVis:true,
              //收款类型
              payTypeList:[],
              //收款类型
              payTypeList:[],
              authenStatus:true,
              record:[],
              zmcl:[],
              token: {},
              autoUploadFalg: true,
              field111Action: this.$fieldPathAction,
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
                    businessKey:null,
                   zmcl:null,
                   record:null
                  },
            }
        },

        methods:{
          handlePictureCardPreview(file) {//点击放大图片
            if((file.url).indexOf("profile")>=0){
              let files = (file.url).split("profile");
              let url = window.location.hostname;
              if(url.indexOf("http")<0){
                url = "http://"+url;
              }
              // var url_ = url+":9001/profile"+files[1];
              window.open(url+":9001/dictionary/fileManage/dwonLoadFile/"+file.uid+'/'+file.ptype);
            }
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
            this.$refs.form.validateField("voucher");
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

          init(row){
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

              this.form.businessKey = row.id;
               this.tableData=[];
              this.token = { Authorization: getToken() };
              let id = row.id;
              getReceiptInfo(row.id).then(response => {
                this.modalShow = true;
                this.tableData = response.data.actWorkflowFormData;
                this.form = response.data;
                this.open = true;
                this.title = "收款单录入";

                let status = response.data.status;
                if(status>0){
                  this.butVis = false;
                  this.uploadVis = false;
                }

                /** 获取文件列表 **/
                if(id!='' && id!=null){
                  findFileListByPidPtype(id,1).then(response => {
                    this.voucher= response.data;
                  });
                }
              });

              //this.findWork();
            },
            findWork(){
              findWorkFlowFormData(this.form).then(responses => {
                this.tableData = responses;
              });
            },
            modalClose(){
                this.modalShow = false;
            },
          /** 文件上传检测 **/
          field111BeforeUpload(file) {
            let isRightSize = file.size / 1024 / 1024 < 20;
            if (!isRightSize) {
              this.$message.error('文件大小超过 20MB');
            }
            return isRightSize;
          },
          /** 上传附件成功操作 **/
          zmclApprovalFileSuccess(res, file, fileList) {
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (element.uid == file.uid) {
                element.uid = res.msg;
              }
            }
            if (this.form.zmcl) {
              this.form.zmcl += res.msg + ",";
            } else {
              this.form.zmcl = res.msg + ",";
            }
            this.uploadVal1 = true;
          },
          /** 上传附件成功操作 **/
          contractApprovalFileSuccess(res, file, fileList) {
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (element.uid == file.uid) {
                element.uid = res.msg;
              }
            }
            if (this.form.record) {
              this.form.record += res.msg + ",";
            } else {
              this.form.record = res.msg + ",";
            }
            this.uploadVal1 = true;
          },
          /** 删除附件操作 **/
          removeContractApprovalFile(file, fileList) {
            delFileById(file.uid).then(response => {
              if (response.code == 200) {
                this.form.record = null;
                for (let index = 0; index < fileList.length; index++) {
                  const element = fileList[index];
                  if (this.form.record) {
                    this.form.record += element.uid + ",";
                  } else {
                    this.form.record = element.uid + ",";
                  }
                }
              }
            });
          },
          removeZmclApprovalFile(file, fileList) {
            delFileById(file.uid).then(response => {
              if (response.code == 200) {
                this.form.zmcl = null;
                for (let index = 0; index < fileList.length; index++) {
                  const element = fileList[index];
                  if (this.form.zmcl) {
                    this.form.zmcl += element.uid + ",";
                  } else {
                    this.form.zmcl = element.uid + ",";
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
          handlePictureCardPreview(file) {//点击放大图片
            if((file.url).indexOf("profile")>=0){
              let files = (file.url).split("profile");
              let url = window.location.hostname;
              if(url.indexOf("http")<0){
                url = "http://"+url;
              }
              // var url_ = url+":9001/profile"+files[1];
              window.open(url+":9001/dictionary/fileManage/dwonLoadFile/"+file.uid+'/'+file.ptype);
            }
          },


          /*文件上传,重写文件上传方法,action的路径不会起作用*/
          myUpload(file){
            console.log(file);
            this.fd = new FormData();
            this.fd.form=this.form;//传文件
            this.fd.file=file;//传其他参数
            // fd.append('filename',file.name);//传其他参数
            // uploadFiles(fd).then(function(res) {
            //   console.log(res);
            // });

          },
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
