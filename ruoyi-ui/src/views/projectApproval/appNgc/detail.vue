<!-- 审批详情弹出页 -->
<template>
  <div>
    <el-dialog title="非集客项目立项详情" :visible.sync="modalShow" width="80%" :before-close="modalClose">
      <div ref="detail">
      <el-row :gutter="15">
        <el-form ref="form" :model="form" label-width="100px">
          <el-col :span="8">
            <el-form-item label="项目名称" prop="proName">
              <el-input v-model="form.proName"  readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属公司" prop="belongCompanyname">
              <el-input v-model="form.belongCompanyname" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属部门" prop="belongDeptname">
              <el-input v-model="form.belongDeptname" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目类型" prop="proName">
              <el-input v-model="form.proTypeName" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目分类" prop="classification">
              <el-input v-model="form.classification"  readonly="readonly" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
              <el-form-item label="项目分类" prop="classification">
                <el-select
                  style="width:100%;"
                  v-model="form.classification"
                  placeholder="请输入项目分类"
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="dict in projectSortOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col> -->
          <el-col :span="8">
            <el-form-item label="合同金额" prop="contractAmountTotal">
              <el-input v-model="form.contractAmountTotal" readonly="readonly" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税率(%)" prop="taxrate">
              <el-input v-model="form.taxrate"  readonly="readonly" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不含税金额" prop="contractAmountTax">
              <el-input v-model="form.contractAmountTax" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税额(元)" prop="taxamount">
              <el-input v-model="form.taxamount" readonly="readonly"  />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="申请原因" prop="reqReason">
              <el-input v-model="form.reqReason" type="textarea" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="说明" prop="explainInfo">
              <el-input v-model="form.explainInfo" type="textarea" readonly="readonly" />
            </el-form-item>
          </el-col>
          <div class="no-print">
          <el-col :span="12">
            <el-form-item label="附件" prop="files">
              <el-upload ref="field101" :file-list="field101fileList" :action="field101Action" :headers="token"
                :on-success="handleAvatarSuccess" :auto-upload="autoUploadFalg" :on-preview="handlePictureCardPreview"
                multiple :limit="20" :before-upload="field101BeforeUpload" :disabled="true">
                <!-- <el-button slot="trigger" size="small" type="primary"
                >选取文件</el-button
              > -->
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批结果" prop="approveResult" v-show="showApproveResult">
              <el-upload ref="field101" :file-list="approveResultList" :action="field101Action" :headers="token"
                :on-success="approveResultSuccess" :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload"
                :on-preview="handlePictureCardPreview" :disabled="true">
                <!-- <el-button slot="trigger" size="small" type="primary">选取文件</el-button> -->
              </el-upload>
            </el-form-item>
          </el-col>
          </div>
        </el-form>
      </el-row>
      <div class="no-print">
      <!-- 如果是新建楼盘就显示tab页 -->
      <el-row v-show="showTabPane">
        <el-tabs v-model="activeName">
          <el-tab-pane label="基本信息" name="base" prop="appNgBase">
            <basicInfo ref="basicInfo" :baseInfoDate="form.appNgBase" />
          </el-tab-pane>
          <el-tab-pane label="材料信息" name="second" prop="appNgMaterialList">
            <materialsInfo ref="materialsInfo" :materialsInfoDate="form.appNgMaterialList"></materialsInfo>
          </el-tab-pane>
          <el-tab-pane label="施工费信息" name="third" prop="appNgConstList">
            <construcostInfo ref="construcostInfo" :construcostInfoDate="form.appNgConstList"></construcostInfo>
          </el-tab-pane>
          <el-tab-pane label="事前审批表" name="forth" prop="appNgApprovalList">
            <approvalInfo ref="approvalInfo" :approvalInfoDate="form.appxNgReq" :proName="form.proName" :proType="form.proType"
              :belongCompanyid="form.belongCompanyname" />
          </el-tab-pane>
        </el-tabs>
      </el-row>
      </div>
      <el-row :gutter="15">
        <el-col :span="24">
          <el-divider content-position="center">
            <el-link type="primary"><b>审核信息</b></el-link>
          </el-divider>
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
          <el-table-column label="审批时间" prop="createTime" header-align="center" align="center">
          </el-table-column>
        </el-table>
      </el-row>
      <div slot="footer" class="no-print">
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="print_sp">打 印</el-button>
        </div>
      </div>
      </div>

    </el-dialog>
  </div>
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
    historyFromData,
    getCollection
  } from '@/api/activiti/historyFormdata'
  import {
    getFjkDetail
  } from '@/api/projectApproval/collectProject'
  import basicInfo from "./info/basicInfo.vue";
  import approvalInfo from "./info/approvalInfo.vue";
  import construcostInfo from "./info/construcostInfo.vue";
  import materialsInfo from "./info/materialsInfo.vue";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    downloadfileById,
    findDeptByBranchOffice,
    findFileListByPidPtype,
    findLoginbyCompany
  } from "@/api/utils";
  export default {
    name: "index",
    components: {
      basicInfo,
      approvalInfo,
      construcostInfo,
      materialsInfo
    },
    data() {
      return {
        tableData: [], //审批信息
        invoiceDataList: [], //开票信息
        projectSortOptions: [],
        showTabPane: false,
        activeName: "base",
        show: false,
        modalShow: false,
        projectCategoryTypeList: [],
        autoUploadFalg: true,
        token: {},
        field101Action: this.$fieldPathAction,
        field101fileList: [],
        approveResultList: [],
        showApproveResult: false,
        form: {
          proNo: null,
          proName: null,
          contractNo: null,
          contractName: null,
          reqUsername: null,
          reqDeptname: null,
          reqDate: null,
          invoiceAmountTax: null,
          invoiceAmountNtax: null,
          businessKey: null
        },
      }
    },
    methods: {
      init(row) {
        this.form.businessKey = row.id;
        this.tableData = [];

        getFjkDetail(this.form.businessKey).then(response => {

          this.form = response.data
          // this.$refs.materialsInfo = response.data.appNgMaterialList;
          // this.$refs.construcostInfo = response.data.appNgConstList;
          let proName_ = response.data;
          this.modalShow = true;
          this.tableData = response.data.actWorkflowFormDatas;
          //显示标签面板
          if (this.form.proType == "f03e24f34279452baed1cba24896653e") {
            this.showIsSync = true;
          } else {
            this.showIsSync = false;
          }
          //显示标签面板
          if (this.form.proType == "36e4550f15954fbbadf4dff4f6fc63ea") {
            this.showTabPane = true;
          } else {
            this.showTabPane = false;
          }
          if (this.form.isBidInviting == 1) {
            this.showApproveResult = true;
          } else {
            this.showApproveResult = false;
          }
          findFileListByPidPtype(this.form.id, 1).then(response => {
            this.field101fileList = response.data;
          });
          findFileListByPidPtype(this.form.id, 2).then(response => {
            this.approveResultList = response.data;
          });

          //项目类型列表
          this.getDicts("project_category_type").then((response1) => {
            for (let item of response1.data) {
              if (item.dictValue == proName_.proType) {
                this.form.approvalType = item.dictLabel;
              }
            }
          });
          this.getDicts("project_sort").then(response1 => {
            for (let item of response1.data) {
              if (item.dictSort == this.form.classification) {
                this.form.classification = item.dictLabel;
              }
            }
          });



        })

        //this.findWork();
      },
      findWork() {
        findWorkFlowFormData(this.form).then(responses => {
          this.tableData = responses;
        });
      },
      /** 文件上传检测 **/
      field101BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 20;
        if (!isRightSize) {
          this.$message.error('文件大小超过 20MB');
        }
        let testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
        if (isRightSize) {
          if (testmsg == 'jpg' || testmsg == 'JPG' || testmsg == 'PNG' || testmsg == 'png' || testmsg == 'gif' ||
            testmsg == 'GIF' ||
            testmsg == 'jpeg' || testmsg == 'JPEG' || testmsg == 'pdf' || testmsg == 'PDF') {

          } else {
            isRightSize = false;
            this.$message.error('只允许上传图片和pdf');
          }
        }
        return isRightSize;
        // 只能上传jpg/png/jpeg/gif/pdf文件，且不超过500kb
      },
      /** 上传文件成功操作 **/
      handleAvatarSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.files) {
          this.form.files += res.msg + ",";
        } else {
          this.form.files = res.msg + ",";
        }
      },
      approveResultSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.approveResult) {
          this.form.approveResult += res.msg + ",";
        } else {
          this.form.approveResult = res.msg + ",";
        }
      },
      /** 删除文件操作 **/
      removeFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.files = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.files) {
                this.form.files += element.uid + ",";
              } else {
                this.form.files = element.uid + ",";
              }
            }
          }
        });
      },
      removeApproveResultFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.code == 200) {
            this.form.approveResult = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.approveResult) {
                this.form.approveResult += element.uid + ",";
              } else {
                this.form.approveResult = element.uid + ",";
              }
            }
          }
        });
      },
      handlePictureCardPreview(file) { //点击放大图片
        if ((file.url).indexOf("profile") >= 0) {
          console.log(file)
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
      modalClose() {
        this.modalShow = false;
      },
      //打印
      print_sp() {
        this.$print(this.$refs.detail)
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
