<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="180px" :rules="rules">

      <el-row :gutter="15">
        <el-col :span="24">
          <el-form-item label="是否老合同" prop="isOldCon">
            <el-select
              v-model="formData.isOldCon"
              placeholder="请选择"
              style="width: 100%;"

            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>



          </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectCategory">
            <el-select :disabled="true" v-model="formData.projectCategory" :typeNumber="formData.projectCategory"
              placeholder="请选择项目类型" style="width: 100%;" @change="setProjectCategoryName">
              <el-option v-for="dict in projectCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="formData.projectId" :disabled="true"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编号" prop="projectNo">
            <el-input v-model="formData.projectNo" :disabled="true"/>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectNo">
            <el-input v-model="formData.projectName" :disabled="true" />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="formData.projectName" placeholder="请输入项目名称" v-on:click.native="showDialog"
              :validate-event="false" />
            <selectProject v-if="isShowDialog" ref="selectProject" @ok="objDialogOk" v-bind:stat="formData.projectCategory"
              v-bind:dialogVisible="isShowDialog" />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractNo4">
              <el-input v-model="formData.contractNo1" placeholder="请输入合同编号" :validate-event="false" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-date-picker
                  v-model="formData.contractNo2"
                  type="year"
                  placeholder="选择年"
                  format="yyyy"
                  value-format="yyyy">
                </el-date-picker>
          </el-col>
          <el-col :span="4">
            <el-date-picker
                  v-model="formData.contractNo3"
                  type="month"
                  placeholder="选择月"
                  format="MM"
                  value-format="MM">
                </el-date-picker>
          </el-col>
          <el-col :span="4">
            <el-input v-model="formData.contractNo4"  onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" maxlength="3" placeholder="请输入合同编号" />
          </el-col>
          <!-- <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="formData.contractNo" placeholder="请输入合同编号" />
          </el-form-item> -->
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="formData.contractName" placeholder="请输入合同名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类别" prop="contractCategory">
            <el-select v-model="formData.contractCategory" placeholder="请选择合同类别" style="width: 100%;">
              <el-option v-for="dict in contractCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="虚拟合同" prop="isVirtual">
            <el-radio-group v-model="formData.isVirtual" size="mini">
              <el-radio-button :label="1">是</el-radio-button>
              <el-radio-button :label="2">否</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <!-- <el-form-item label="收入类型" prop="incomeType" v-show="isIncomeType">
            <el-select v-model="formData.incomeType" placeholder="请选择收入类型" style="width: 100%;">
              <el-option v-for="dict in incomeTypeList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue"/>
            </el-select>
          </el-form-item> -->
          <div>
            <el-form ref="elForm" :model="formData" size="medium" label-width="100px" >
              <table class="curr-table ">
                <tbody>
                  <tr>
                    <th width="50%">收支类型</th>
                    <th width="50%">类型金额</th>
                    <th>
                      <el-button size="mini" type="success" @click="loadMore()" style="margin: 0.625rem;">新增</el-button>
                    </th>
                  </tr>
                  <tr v-for="(value, index) in formData.dataList1" :key="index">
                    <td>
                      <el-select v-model="value.incomeTypeId" placeholder="请选择收入类型">
                        <el-option v-for="dict in incomeTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"
                          prop="incomeType" />
                      </el-select>
                    </td>
                    <td>
                      <el-input v-model="value.incomeTypeMoney" :name="'appMateList[' + index + '].incomeTypeMoney'"
                        v-on:input="changeNum()" oninput="value=value.replace(/[^0-9.]/g,'')" />
                    </td>
                    <td>
                      <el-button size="mini" type="warning" @click.prevent="removeTodo(index)">删除</el-button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </el-form>
          </div>
        </el-col>
        <el-col :span="24" style="margin-top: 0;">
          <el-divider content-position="center">
            <el-link type="primary"><b>对方单位信息</b></el-link>
          </el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="对方单位ID" prop="oppositeId" v-if="show">
            <el-input v-model="formData.oppositeId" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位名称" prop="oppositeName">
            <el-input v-model="formData.oppositeName" placeholder="请选择对方名称" v-on:click.native="showOppositeName = true" />
            <selectCompany v-if="showOppositeName" ref="company" @ok="companyOk" :companyType='"2"' />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位银行账户" prop="oppositeBank">
            <el-input v-model="formData.oppositeBank" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位银行账号" prop="oppositeBankId">
            <el-input v-model="formData.oppositeBankId" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位联系人" prop="oppositeContacts">
            <el-input v-model="formData.oppositeContacts" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位联系电话" prop="oppositePhone">
            <el-input v-model="formData.oppositePhone" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方单位联系地址" prop="oppositeAddress">
            <el-input v-model="formData.oppositeAddress" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="24" style="margin-top: 0;">
          <el-divider content-position="center">
            <el-link type="primary"><b>我方单位信息</b></el-link>
          </el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位名称" prop="ourName">
            <el-input v-model="formData.ourName" placeholder="请选择我方名称" v-on:click.native="showOurselves = true" />
            <selectCompany v-if="showOurselves" ref="company" @ok="Ourselves" :companyType='"1"' />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位联系人" prop="ourContacts">
            <el-input v-model="formData.ourContacts" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位联系电话" prop="ourPhone">
            <el-input v-model="formData.ourPhone"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位银行账户" prop="ourBank">
            <el-input v-model="formData.ourBank" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位银行账号" prop="ourBankId">
            <el-input v-model="formData.ourBankId" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方单位联系地址" prop="ourAddress">
            <el-input v-model="formData.ourAddress" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="24" style="margin-top: 0;">
          <el-divider content-position="center">
            <el-link type="primary"><b>合同签订信息</b></el-link>
          </el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同签订日期" prop="starttime">
            <el-date-picker v-model="formData.starttime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
              placeholder="请选择日期" clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同截止日期" prop="endtime">
            <el-date-picker v-model="formData.endtime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
              placeholder="请选择日期" clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同服务开始日期" prop="contractServeStart">
            <el-date-picker clearable :style="{ width: '100%' }" v-model="formData.contractServeStart" type="date"
              value-format="yyyy-MM-dd" placeholder="选择合同服务开始日期"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务期限(月)" prop="serviceTerm">
            <el-input v-model="formData.serviceTerm"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额(元)" prop="totalMoney">
            <el-input v-model="formData.totalMoney" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税率(%)" prop="taxrate">
            <el-input v-model="formData.taxrate"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="不含税金额(元)">
            <el-input v-model="formData.notTaxMoney" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款方式" prop="payMethod">
            <el-select v-model="formData.payMethod" placeholder="请选择付款方式" style="width: 100%;" @change="setPayMethodName">
              <el-option v-for="dict in payMethodList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="经办部门" prop="manageDeptment">
            <el-select v-model="formData.manageDeptment" placeholder="请选择经办部门" style="width: 100%;">
              <el-option v-for="dict in projectCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="12">
          <el-form-item label="经办部门" prop="manageDeptment">
            <treeselect v-model="formData.manageDeptment" :options="deptOptions" :show-count="false" placeholder="请选择所属部门" />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="经办部门" prop="manageDeptmentName">
            <el-input v-model="formData.manageDeptmentName" :disabled="true" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="经办人" prop="manageUserName">
            <el-input v-model="formData.manageUserName" :disabled="true" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-col :span="12">
          <el-form-item label="变更之前ID" prop="originalId">
            <el-input v-model="formData.originalId" :disabled="true"/>
          </el-form-item>
        </el-col> -->
      <!-- <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择" clearable style="width: 100%;">
              <el-option v-for="dict in contractStatusList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态说明"><el-input type="textarea" v-model="formData.statusPS"></el-input></el-form-item>
        </el-col> -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="合同归档资料" prop="archiveFiles">
            <el-upload ref="field101" :file-list="archiveFiles" :action="field101Action" :headers="token" :on-success="archiveFilesSuccess"
              :on-remove="removeArchiveFile" :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload"
              :on-preview="handlePictureCardPreview">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <!-- <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div> -->
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同扫描件" prop="contractScanningFile">
            <el-upload ref="field101" :file-list="contractScanningFile" :action="field101Action" :headers="token"
              :on-success="contractScanningFileSuccess" :on-remove="removeContractScanningFile" :auto-upload="autoUploadFalg"
              multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <!-- <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div> -->
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="合同签订审批流程附件" prop="contractApprovalFile">
            <el-upload ref="field101" :file-list="contractApprovalFile" :action="field101Action" :headers="token"
              :on-success="contractApprovalFileSuccess" :on-remove="removeContractApprovalFile" :auto-upload="autoUploadFalg"
              multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <!-- <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div> -->
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他附件" prop="otherFiles">
            <el-upload ref="field101" :file-list="otherFiles" :action="field101Action" :headers="token" :on-success="otherFilesSuccess"
              :on-remove="removeOtherFiles" :auto-upload="autoUploadFalg" multiple :limit="20" :before-upload="field101BeforeUpload"
              :on-preview="handlePictureCardPreview">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <!-- <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div> -->
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <el-col :span="24">
        <el-form-item label="备注">
          <el-input type="textarea" v-model="formData.remarks"></el-input>
        </el-form-item>
      </el-col>
    </el-form>
  </div>
</template>
<script>
  import {
    listContract,
    getContract,
    delContract,
    addContract,
    updateContract,
    exportContract
  } from '@/api/contract/contractInfo';
  import selectProject from './selectProject';
  import selectCompany from './selectCompany';
  import selectOurselves from './selectOurselves.vue'
  import {
    treeselect
  } from '../../../api/system/dept';
  import Treeselect from '@riophae/vue-treeselect';
  import '@riophae/vue-treeselect/dist/vue-treeselect.css';
  import {
    getUserInfo
  } from "@/api/collection/common";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    downloadfileById,
    findLoginbyCompany
  } from "@/api/utils";
  import {
    getToken
  } from "@/utils/auth";

  import {
    delContractIncomeType
  } from "@/api/contract/contractIncomeType"

  export default {
    components: {
      selectProject,
      selectCompany,
      Treeselect,
      selectOurselves
    },
    props: {
      typeNumber: 0,
      project: {
        type: Object,
        default: () => {
          return {}
        }
      },
    },
    data() {
      return {
        options: [{
          value: 1,
          label: '是'
        }, {
          value: 2,
          label: '否'
        }],



        showOurselves: false,
        showOppositeName: false,
        isShowDialog: false,
        isIncomeType: false,
        show: false,
        vars: 'xxxx',
        autoUploadFalg: true,
        projectCategory: "",
        formData: {
          isOldCon:null,
          branchInvolvedId: undefined,
          id: '',
          oppositeBank: '',
          projectCategory: null,
          projectCategoryName: null,
          contentType: null,
          projectId: null,
          projectNo: null,
          projectName: null,
          contractNo: null,
          contractNo1: "GDJ",
          contractNo2: null,
          contractNo3: null,
          contractNo4: null,
          contractName: null,
          month: null,
          contractCategory: null,
          contractCategoryName: null,
          // incomeType: null,
          oppositeName: null,
          oppositeBankId: null,
          oppositeContacts: null,
          oppositePhone: null,
          oppositeAddress: null,
          oppositeCreditCode: null,
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
          manageCompany: null,
          manageCompanyName: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: null,
          // statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          remarks: null,
          isVirtual:2,
          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
          ctBuContractIncomeTypeList:[],
          // archiveFiles:[],
          // contractScanningFile: [],
          // contractApprovalFile: [],
          // otherFiles: [],
          isArchive:2,
        },

        rules: {
          // projectCategory: [{
          //   required: true,
          //   message: '请选择项目类型',
          // }],
          // projectName: [{
          //   required: true,
          //   message: '请选择项目名称',
          // }],
          isOldCon: [{ required: true, message: '是否老合同不能为空', trigger: 'blur' }],
          contractNo4: [{
            required: true,
            message: '请输入合同编号',
          }],
          contractName: [{
            required: true,
            message: '请输入合同名称',
          }],
          contractCategory: [{
            required: true,
            message: '请选择合同类别',
          }],
          ourName: [{
            required: true,
            message: '请选择我方单位名称',
          }],
          ourContacts: [{
            required: true,
            message: '请输入我方单位联系人',
          }],
          ourPhone: [{
            required: true,
            message: '请输入我方单位联系电话',
          }],
          oppositeName: [{
            required: true,
            message: '请选择对方单位名称',
          }],
          totalMoney: [{
            required: true,
            message: '请输入合同金额',
            trigger: 'blur'
          }],
          taxrate: [{
            required: true,
            message: '请输入税率',
            trigger: 'blur'
          }],
          serviceTerm: [{
            required: true,
            message: '请输入服务期限',
            trigger: 'blur'
          }],
          contractServeStart: [{
            required: true,
            message: '请选择合同服务开始日期',
            trigger: 'blur'
          }],
          starttime: [{
            required: true,
            message: '请选择合同签订日期',
            trigger: 'blur'
          }],
          endtime: [{
            required: true,
            message: '请选择合同截至日期',
            trigger: 'blur'
          }],
          archiveFiles: [
            {
              required: true,
              message: '请上传合同归档资料',
              trigger: 'blur'
            }
          ],
          contractScanningFile: [
            {
              required: true,
              message: '请上传合同扫描件',
              trigger: 'blur'
            }
          ],
          contractApprovalFile: [
            {
              required: true,
              message: '请上传合同签订审批流程附件',
              trigger: 'blur'
            }
          ],
          payMethod: [
            {
              required: true,
              message: '请选择付款方式',
              trigger: 'blur'
            }
          ]
        },

        field101Action: this.$fieldPathAction,
        //field101fileList: [],
        archiveFiles: [],
        contractScanningFile: [],
        contractApprovalFile: [],
        otherFiles: [],
        //项目类型
        projectCategoryTypeList: [],
        //合同类别
        contractCategoryTypeList: [],
        //收入类型
        incomeTypeList: [],
        //付款方式
        payMethodList: [],
        //合同状态
        contractStatusList: [],
        //我方单位名称
        ourNameList: [],
        proTypeNameList: [],
        belongCompanyNameList: [],
        //经办单位
        deptOptions: [],
        token: {}

      };
    },
    computed: {},
    watch: {
      'formData.totalMoney': function(val) {
        //var self = this;
        if (this.formData.taxrate != null) {
          this.formData.notTaxMoney = (val / (this.formData.taxrate * 0.01 + 1)).toFixed(2);
        }
      },

      'formData.taxrate': function(val) {
        //var self = this;
        if (this.formData.totalMoney != null) {
          this.formData.notTaxMoney = (this.formData.totalMoney / (val * 0.01 + 1)).toFixed(2);
        }
      },
      'formData.contractCategory': {
        handler(value) {
          this.$bus.$emit("aaa", value)
          // this.changeIsIncomeType()
        },
      },
      //新增带入项目信息
      'project': {
        handler(val) {
          this.formData.projectName = val.cbanProName;
          this.formData.projectNo = val.cbanReqNo;
          this.formData.projectId = val.cbanId;
          if (val.cbanProType == "9f30ebd46d3c428cbc5e54f7175eb4f2") {
            this.formData.projectCategory = "1"
            this.formData.projectCategoryName = "集客类"
          } else {
            this.formData.projectCategory = "2"
            this.formData.projectCategoryName = "非集客类"
          }
        },
        immediate: true,
        deep: true,
      }
    },
    created() {

      //项目类型列表
      this.getDicts('project_category_type').then(response => {
        this.projectCategoryTypeList = response.data;
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
      });
      //合同类别
      this.getDicts('contract_category_type').then(response => {
        this.contractCategoryTypeList = response.data;
      });
      //收入类型
      this.getDicts('income_type').then(response => {
        this.incomeTypeList = response.data;
      });
      //付款方式
      this.getDicts('pay_method').then(response => {
        this.payMethodList = response.data;
      });
      //合同状态
      this.getDicts('contract_status').then(response => {
        this.contractStatusList = response.data;
      });
      //经办部门
      this.getTreeselect();
    },
    mounted() {
      this.$on('basic');
    },
    methods: {
      /** 添加行 **/
      loadMore() {
        if (!this.formData.dataList1) {
          this.$set(this.formData, 'dataList1', [])
        }
        this.formData.dataList1.push({
          id: null,
          incomeTypeId: null,
          incomeTypeMoney: null,
        });
        // this.$set(this.formData.dataList1,this.formData.dataList1.length,{
        //   id: null,
        //   incomeTypeId: null,
        //   incomeTypeMoney: null,
        // })
      },
      /** 删除行 **/
      removeTodo: function(index) {
        if (this.formData.dataList1[index].id != null) {
          delContractIncomeType(this.formData.dataList1[index].id).then(response => {})
        }
        this.formData.dataList1.splice(index, 1);
        this.changeNum();
      },

      //监听输入框输入值
      changeNum() {
        let incomeTypeMoneySum = 0;
        let rowList = this.formData.dataList1;
        if (rowList && rowList.length > 0) {
          for (let i = 0; i < rowList.length; i++) {
            if (rowList[i].incomeTypeMoney) {
              incomeTypeMoneySum += parseFloat(rowList[i].incomeTypeMoney);
            }
          }
        }
        this.formData.totalMoney = incomeTypeMoneySum;

      },
      showDialog() {
        //this.isShowDialog = false;
        this.isShowDialog = true;

      },

      changeIsIncomeType(index) {
        if (this.formData.contractCategory != "2") {
          this.isIncomeType = true;
        } else {
          this.isIncomeType = false;
        }
      },

      // submitForm() {
      //   this.$refs['elForm'].validate(valid => {
      //     if (valid) return true
      //     else return false
      //     // TODO 提交表单
      //   });
      // },

      submitForm() {
        this.$refs['elForm'].validate(valid => {
          if (!valid) return;
          // TODO 提交表单
        });
      },
      resetForm() {
        this.$refs['elForm'].resetFields();

      },
      /*选择项目弹出框 调用方法*/
      objDialogOk(id, no, name) {
        this.formData.projectId = id;
        this.formData.projectNo = no;
        this.formData.projectName = name;
        this.formData.contractName = name;
        this.$set(this.formData, 'projectName', name);
        this.isShowDialog = false;

      },
      /*选择对方单位弹出框 调用方法*/
      companyOk(id, companyName, openingBank, openingAccount, linkMan, linkPhone, address, creditCode) {
        if (companyName != "") {
          this.formData.oppositeId = id;
          this.formData.oppositeName = companyName;
          this.formData.oppositeBank = openingBank;
          this.formData.oppositeBankId = openingAccount;
          this.formData.oppositeContacts = linkMan;
          this.formData.oppositePhone = linkPhone;
          this.formData.oppositeAddress = address;
          this.formData.oppositeCreditCode = creditCode;
          this.$set(this.formData, 'oppositeName', companyName);
        }

        // this.$set(this.formObj,'vars',"xxxx")
        this.showOppositeName = false
      },

      //我方单位
      Ourselves(id, companyName, openingBank, openingAccount, linkMan, linkPhone, address) {

        if (companyName != "") {
          this.formData.oppositeId = id;
          this.formData.ourName = companyName;
          this.formData.ourBank = openingBank;
          this.formData.ourBankId = openingAccount;
          // this.formData.ourContacts = linkMan;
          // this.formData.ourPhone = linkPhone;
          this.formData.ourAddress = address;
          this.$set(this.formData, 'ourName', companyName);
        }

        this.showOurselves = false
      },
      /** 文件上传检测 **/
      // field101BeforeUpload(file) {
      //   let isRightSize = file.size / 1024 / 1024 < 2;
      //   if (!isRightSize) {
      //     this.$message.error('文件大小超过 2MB');
      //   }
      //   return isRightSize;
      // },

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
            testmsg == 'jpeg' || testmsg == 'JPEG' || testmsg == 'pdf' || testmsg == 'PDF' || testmsg == 'word' || testmsg == 'WORD' ||
            testmsg == 'dox' || testmsg == 'DOX' || testmsg == 'docx' || testmsg == 'DOCX' ) {

          } else {
            isRightSize = false;
            this.$message.error('只允许上传图片、PDF、WORD');
          }
        }
        return isRightSize;
        // 只能上传jpg/png/jpeg/gif/pdf文件，且不超过500kb
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
      // handlePictureCardPreview(file) { //点击放大图片
      //   if ((file.url).indexOf("profile") >= 0) {
      //     let files = (file.url).split("profile");
      //     let url = window.location.hostname;
      //     if (url.indexOf("http") < 0) {
      //       url = "http://" + url;
      //     }
      //     var url_ = url + ":9001/profile" + files[1];
      //     window.open(url + ":9001/dictionary/fileManage/dwonLoadFile/" + file.uid + '/' + file.ptype);
      //   }
      // },
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

      /** 查询部门下拉树结构 */
      getTreeselect() {
        // treeselect().then(response => {
        //   this.deptOptions = response.data;
        // });
        getUserInfo().then(response => {
          this.formData.manageDeptment = response.data.user.dept.deptId;
          this.formData.manageDeptmentName = response.data.user.dept.deptName;
          this.formData.manageUser = response.data.user.userId;
          this.formData.manageUserName = response.data.user.nickName;
          this.formData.manageCompany = response.data.user.companyId;
        });
        findLoginbyCompany().then(response => {
          this.formData.manageCompanyName = response.msg;
        });
      },


      setProjectCategoryName(obj) {
        // alert(obj)
        this.formData.projectCategoryName = this.projectCategoryTypeList[obj - 1].dictLabel
      },
      setContractCategoryName(obj) {
        this.formData.contractCategoryName = this.contractCategoryTypeList[obj - 1].dictLabel
        if (this.formData.contractCategory != "2") {
          this.isIncomeType = true;
        } else {
          this.isIncomeType = false;
          this.formData.incomeType = null;
        }
      },
      setPayMethodName(obj) {
        this.formData.payMethodName = this.payMethodList[obj - 1].dictLabel
      }
    }
  };
</script>
<style scoped lang="scss">
  .curr-table {
    text-align: center;
    width: 100%;
    vertical-align: middle;
    border-spacing: 0px;
    border-style: none;
    border-collapse: collapse;
  }

  .curr-table td,
  th {
    padding: 5px 10px;
  }

  .curr-table th {
    font-weight: bold;
    background-color: #eff3fa;
    height: 15px;
  }
</style>
