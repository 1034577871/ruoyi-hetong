<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="项目类别" prop="projectCategory">
            <el-select v-model="queryParams.projectCategory" placeholder="请选择项目类别" clearable size="small">
              <el-option v-for="dict in projectCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目编号" prop="projectNo">
            <el-input v-model="queryParams.projectNo" placeholder="请输入项目编号" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="queryParams.contractName" placeholder="请输入合同名称" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="合同类别" prop="contractCategory">
            <el-select v-model="queryParams.contractCategory" placeholder="请选择合同类别" clearable size="small">
              <el-option v-for="dict in contractCategoryTypeList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="收入类型" prop="incomeType">
            <el-select v-model="queryParams.incomeType" placeholder="请选择收入类型" clearable size="small">
              <el-option v-for="dict in incomeTypeList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="对方单位" prop="oppositeName">
            <el-input v-model="queryParams.oppositeName" placeholder="请输入对方单位名称" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="我方单位" prop="ourName">
            <el-input v-model="queryParams.ourName" placeholder="请输入我方单位名称" clearable size="small"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="付款方式" prop="payMethod">
            <el-select v-model="queryParams.payMethod" placeholder="请选择付款方式" clearable size="small">
              <el-option v-for="dict in payMethodList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="是否归档" prop="isArchive">
            <el-select v-model="queryParams.isArchive" placeholder="请选择" clearable size="small">
              <el-option label="已归档" value="1"></el-option>
              <el-option label="未归档" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="6">
          <el-form-item label="合同签订日期" label-width="100px">
            <el-date-picker
              v-model="starttime"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col> -->
      </el-row>
      <!-- <el-row :gutter="15">
        <el-col :span="8">
          <el-form-item label="合同截至日期" label-width="100px">
            <el-date-picker
              v-model="endtime0"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="服务开始日期" label-width="100px">
            <el-date-picker
              v-model="contractServeStart"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-row :gutter="15">
        <!-- <el-col :span="6">
          <el-form-item label="服务截至日期" label-width="100px">
            <el-date-picker
              v-model="contractServeEnd"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col> -->
        <el-col :span="6">
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>

      </el-row>

    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="multiple" @click="updateIsArchiveByIds" >归档
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="multiple" @click="updateCancelIsArchiveByIds" >取消归档
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-dialog :title="title" :visible.sync="dialogVisible" width="80%" append-to-body>
      <el-form :model="form" ref="form" label-width="90px">
        <el-row :gutter="15">
          <el-col :span="6">
            <el-form-item label="是否归档" prop="isArchive"  >
              <el-radio-group v-model="form.isArchive" size="mini"  @change='changeStatus' >
              	<el-radio-button :label="1">是</el-radio-button>
              	<el-radio-button :label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="未归档原因" prop="unfinishedArchiveCause" v-show="isUnfinishedArchiveCause" >
              <el-input
                v-model="form.unfinishedArchiveCause"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="最终归档资料" prop="finalArchiveFile" v-show="isFinalArchiveFile">
              <el-upload
                ref="field101"
                :file-list="finalArchiveFile"
                :action="field101Action"
                :headers="token"
                :on-success="finalArchiveFileSuccess"
                :on-remove="removeFinalArchiveFile"
                :auto-upload="autoUploadFalg"
                multiple
                :limit="20"
                :before-upload="field101BeforeUpload"
                :on-preview="handlePictureCardPreview"
              >
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <div slot="footer" class="dialog-footer">
        	<el-button type="success" @click="submitForm">确 定</el-button>
        	<el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>

    <el-table v-loading="loading" ref="itsmDataTable" :data="contractList" @selection-change="handleSelectionChange"
              border>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="合同编号" align="center" prop="contractNo" width="100" />
      <el-table-column label="合同名称" align="center" prop="contractName" width="100"/>
      <el-table-column label="合同类别" align="center" prop="contractCategoryName"/>
      <el-table-column label="是否归档" align="center" prop="isArchive">
        <template v-slot="scope">
          <p v-if="scope.row.isArchive == 1">已归档</p>
          <p v-if="scope.row.isArchive == 2">未归档</p>
        </template>
      </el-table-column>
      <el-table-column label="对方单位名称" align="center" prop="oppositeName" width="100" />
      <el-table-column label="我方单位名称" align="center" prop="ourName" width="100" />
      <el-table-column label="合同签订日期" align="center" prop="starttime" width="100" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同金额(元)" align="center" prop="totalMoney" width="100"  />
      <el-table-column label="税率(%)" align="center" prop="taxrate"/>
      <el-table-column label="不含税金额(元)" align="center" prop="notTaxMoney" width="100"  />
      <el-table-column label="付款方式" align="center" prop="payMethodName"/>
      <el-table-column label="经办部门" align="center" prop="manageDeptmentName"  />
      <el-table-column label="经办人" align="center" prop="manageUserName"/>
      <el-table-column label="状态说明" align="center" prop="statusPs"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status==2 && scope.row.isArchive == 2" size="mini" type="text" icon="el-icon-edit"
                     @click="updateIsArchiveByIds(scope.row)">归档
          </el-button>
          <el-button v-if="scope.row.status==2 && scope.row.isArchive == 1" size="mini" type="text" icon="el-icon-edit"
                     @click="updateCancelIsArchiveByIds(scope.row)">取消归档
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0"
                     @click="addOrUpdateHandle(scope.row)">审批详情
          </el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status>0"
                     @click="examineAndApproves(scope.row)">查看流程图
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <contract v-if="addOrUpdateVisible" ref="contract"></contract>
  </div>

</template>

<script>
  import {
    listContract2,
    getContract,
    delContract,
    addContract,
    updateContract,
    exportContract,
    updateIsArchiveByIds,
    updateCancelIsArchiveByIds,
    exportArchive
  } from '@/api/contract/contractInfo';

  import {getToken} from "@/utils/auth";
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid,
    findFileListByPidPtype,
    downloadfileById,
    findLoginbyCompany
  } from "@/api/utils";
  import {getUserInfo} from "@/api/collection/common";
  import { exitFlow  } from "@/api/projectApproval/collectProject";
  import contract from "../contractInfo/contractInfo.vue";


  export default {
    name: 'Contract',
    components: {
      contract
    },

    data() {
      return {
        addOrUpdateVisible: false,
        activeName: 'first',
        formData: [],
        dialogVisible: false,
        dialogVisible1: false,
        isFinalArchiveFile:false,
        isUnfinishedArchiveCause:false,
        finalArchiveFile:[],
        // 合同信息录入主表格数据
        contractList: [],
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: false,
        // 总条数
        total: 0,
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

        contentTypeList: [],
        // 日期范围
        endtime0: [],
        starttime: [],
        endtime: [],
        contractServeStart: [],
        contractServeEnd: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        field101Action: this.$fieldPathAction,
        token: {},
        autoUploadFalg:true,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          projectCategory: null,
          contentType: null,
          projectId: null,
          projectNo: null,
          projectName: null,
          contractNo: null,
          contractName: null,
          month: null,
          contractCategory: null,
          incomeType: null,
          oppositeName: null,
          oppositeBank: null,
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
          // endtime: null,
          contractServeStart: null,
          serviceTerm: null,
          contractServeEnd: null,
          totalMoney: null,
          taxrate: null,
          notTaxMoney: null,
          payMethod: null,
          archiveFiles: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: 2,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          remarks: null,
          sum: '', //统计
          // 日期范围
          // starttime: [],
          endtime1: '',
          endtime2: '',
          contractServeStart1: '',
          contractServeStart2: '',
          contractServeEnd1: '',
          contractServeEnd2: '',
          isArchive:null,

        },

        form: {
          id: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          projectCategory: null,
          projectId: null,
          projectNo: null,
          projectName: null,
          contractNo: null,
          contractName: null,
          month: null,
          contractCategory: null,
          incomeType: null,
          oppositeName: null,
          oppositeBank: null,
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
          archiveFiles: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: 0,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          delFlag: null,
          remarks: null,
          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
          field101fileList: [],
          pid: null,
          ptype: null,
          finalArchiveFile:null,
          unfinishedArchiveCause:null,
        }
      };
    },
    created() {
      this.getList();
      //项目类型列表
      this.getDicts('project_category_type').then(response => {
        this.projectCategoryTypeList = response.data;
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
    },
    methods: {
      //合同标的额传值给第三页面的总额
      onTabClick() {
        this.$refs.payInfo.number = this.$refs.basicInfo.formData.totalMoney;
      },

      changeStatus(){
        if(this.form.isArchive == 1){
            this.isFinalArchiveFile = true;
            this.isUnfinishedArchiveCause = false;
            // this.form.unfinishedArchiveCause = null;
        } else if(this.form.isArchive == 2) {
            this.isFinalArchiveFile = false;
            this.isUnfinishedArchiveCause = true;
            // this.form.finalArchiveFile = null;
        }
      },

      /** 查询合同信息录入主列表 */
      getList() {
        this.loading = true;
        listContract2(this.addDateRange(this.queryParams, this.starttime)).then(response => {
          this.contractList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.dialogVisible = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          projectCategory: null,
          projectId: null,
          projectNo: null,
          projectName: null,
          contractNo: null,
          contractName: null,
          month: null,
          contractCategory: null,
          incomeType: null,
          oppositeName: null,
          oppositeBank: null,
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
          archiveFiles: null,
          manageCompany: null,
          manageCompanyName: null,
          manageDeptment: null,
          manageDeptmentName: null,
          manageUser: null,
          manageUserName: null,
          originalId: null,
          status: 0,
          statusPs: null,
          contractScanningFile: null,
          contractApprovalFile: null,
          otherFiles: null,
          delFlag: null,
          remarks: null,
          ctBuContractPayinfoList: [],
          ctBuContractContentList: [],
          contentInfo: null,
          payInfo: null,

        };
        this.resetForm('form');
      },
      /** 搜索按钮操作 */
      handleQuery() {
        let startDate = this.endtime0.toString();
        this.queryParams.endtime1 = startDate.substring(0, 10);
        this.queryParams.endtime2 = startDate.substring(11);
        let startDate1 = this.contractServeStart.toString();
        this.queryParams.contractServeStart1 = startDate1.substring(0, 10);
        this.queryParams.contractServeStart2 = startDate1.substring(11);
        let startDate2 = this.contractServeEnd.toString();
        this.queryParams.contractServeEnd1 = startDate2.substring(0, 10);
        this.queryParams.contractServeEnd2 = startDate2.substring(11);
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.starttime = [];
        this.endtime0 = [];
        this.contractServeStart = [];
        this.contractServeEnd = [];
        this.resetForm('queryForm');
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id);
        this.single = selection.length !== 1;
        this.multiple = !selection.length;
      },

      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids[0]
        this.form.id = id;
        this.finalArchiveFile = [];
        this.dialogVisible = true;
        this.title = "归档修改";
        /** 获取文件列表 **/
        // findFileListByPidPtype(id, 5).then(response => {
        // 	this.finalArchiveFile = response.data;
        // });
      },

      /** 归档按钮操作 */
      updateIsArchiveByIds(row) {
        const ids = row.id || this.ids;
        // let deleSta1 = 0;
        // for (let item of ids) {
        //   if (item == null) {
        //     deleSta1 = 1;
        //     break;
        //   }
        // }
        // if (deleSta1 == 1) {
        //   this.msgSuccess("请删除有合同信息的记录");
        //   return false;
        // }
        const _selectData = this.$refs.itsmDataTable.selection
        let deleSta = 0;
        if (_selectData != '') {
          const flag = false;
          for (let item of _selectData) {
            if (item.isArchive == 1) {
              deleSta = 1;
              break;
            }
          }
          if (deleSta == 1) {
            this.msgSuccess("请选择未归档的合同操作");
            return false;
          }
        }
        this.$confirm('是否确认归档?', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(function() {
            return updateIsArchiveByIds(ids);
          })
          .then(() => {
            this.getList();
            this.msgSuccess('归档成功');
          });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出?', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(function() {
            return exportArchive(queryParams);
          })
          .then(response => {
            this.download(response.msg);
          });
      },

      /** 取消归档按钮操作 */
      updateCancelIsArchiveByIds(row) {
        const ids = row.id || this.ids;
        // let deleSta1 = 0;
        // for (let item of ids) {
        //   if (item == null) {
        //     deleSta1 = 1;
        //     break;
        //   }
        // }
        // if (deleSta1 == 1) {
        //   this.msgSuccess("请删除有合同信息的记录");
        //   return false;
        // }
        const _selectData = this.$refs.itsmDataTable.selection
        let deleSta = 0;
        if (_selectData != '') {
          const flag = false;
          for (let item of _selectData) {
            if (item.isArchive == 2) {
              deleSta = 1;
              break;
            }
          }
          if (deleSta == 1) {
            this.msgSuccess("请选择已归档的合同操作");
            return false;
          }
        }
        this.$confirm('是否确认取消归档?', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then(function() {
            return updateCancelIsArchiveByIds(ids);
          })
          .then(() => {
            this.getList();
            this.msgSuccess('归档成功');
          });
      },


      /** 提交按钮 */
      submitForm(number) {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != null && this.form.id.length != 0) {
              updateContract(this.form).then(response => {
                this.msgSuccess('修改成功');
                this.dialogVisible = false
                this.open = false;
                this.getList();
              });
            } else {
              addContract(this.form).then(response => {
                this.msgSuccess('新增成功');
                this.dialogVisible = false
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },


      /** 审批按钮操作 */
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.contract.init(id);
        })
      },

      /** 文件上传检测 **/
      field101BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 20;
        if (!isRightSize) {
          this.$message.error('文件大小超过 20MB');
        }
        let testmsg=file.name.substring(file.name.lastIndexOf('.')+1)
        if(isRightSize){
          if(testmsg=='jpg' || testmsg=='JPG'|| testmsg=='PNG' || testmsg=='png' || testmsg=='gif' || testmsg=='GIF'
            || testmsg=='jpeg' || testmsg=='JPEG' || testmsg=='pdf' || testmsg=='PDF'){

          }else{
            isRightSize = false;
            this.$message.error('只允许上传图片和pdf');
          }
        }
        return isRightSize;
        // 只能上传jpg/png/jpeg/gif/pdf文件，且不超过500kb
      },
      /** 上传附件资料成功操作 **/
      finalArchiveFileSuccess(res, file, fileList) {
      	for (let index = 0; index < fileList.length; index++) {
      		const element = fileList[index];
      		if (element.uid == file.uid) {
      			element.uid = res.msg;
      		}
      	}
      	if (this.form.finalArchiveFile) {
      		this.form.finalArchiveFile += res.msg + ",";
      	} else {
      		this.form.finalArchiveFile = res.msg + ",";
      	}
      },
      /** 删除附件 **/
      removeFinalArchiveFile(file, fileList) {
      	delFileById(file.uid).then(response => {
      		if (response.code == 200) {
      			this.form.finalArchiveFile = null;
      			for (let index = 0; index < fileList.length; index++) {
      				const element = fileList[index];
      				if (this.form.finalArchiveFile) {
      					this.form.finalArchiveFile += element.uid + ",";
      				} else {
      					this.form.finalArchiveFile = element.uid + ",";
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
      		window.open(url + ":9001/dictionary/fileManage/dwonLoadFile/" + file.uid + '/' + file.ptype);
      	}
      },
      /** 查看流程图 */
      examineAndApproves(row) {
        this.reset();
        let url = window.location.hostname;
        if (url.indexOf("http") < 0) {
          url = "http://" + url;
        }
        let hosturl = url + ":9001/task/exportImage?processInstanceId=" + row.instanceId;
        window.open(hosturl, "_blank");
      },

    }
  };
</script>
<style scoped lang="scss">
  .el-dialog__body {
    padding: 0px 20px;
  }
</style>
