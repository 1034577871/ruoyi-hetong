<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="所属公司" prop="belongCompanyid">
            <el-select style="width:100%;" v-model="queryParams.belongCompanyid" placeholder="请输入所属分公司" clearable size="small">
              <el-option v-for="dict in belongCompanyNameList" :key="dict.deptId" :label="dict.deptName" :value="dict.deptId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="所属部门" prop="belongDeptid">
            <treeselect style="width:100%;" v-model="queryParams.belongDeptid" :options="deptOptions" :show-count="false"
              placeholder="请选择所属部门" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="申请编号" prop="reqNo">
            <el-input v-model="queryParams.reqNo" placeholder="请输入申请编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目名称" prop="proName">
            <el-input v-model="queryParams.proName" placeholder="请输入项目名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="项目类型" prop="proType">
            <el-select style="width:100%;" v-model="queryParams.proType" placeholder="请选择项目类型" clearable size="small">
              <el-option v-for="dict in proTypeNameList" :key="dict.id" :label="dict.typeName" :value="dict.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目分类" prop="classification">
            <el-select style="width:100%;" v-model="queryParams.classification" placeholder="请输入项目分类" clearable size="small">
              <el-option v-for="dict in projectSortOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="状态" prop="status">
            <el-select style="width:100%;" v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
              <el-option v-for="dict in collectTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
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
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['projectApproval:appNgc:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['projectApproval:appNgc:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['projectApproval:appNgc:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-upload2" size="mini" @click="handleExport" v-hasPermi="['projectApproval:appNgc:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" ref="itsmDataTable" :data="appNgcList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一ID" align="center" prop="id" v-if="show" />
      <el-table-column label="状态" align="center" prop="status" v-if="show" />
      <el-table-column label="项目编号" align="center" prop="reqNo" />
      <el-table-column label="项目名称" align="center" prop="proName" />
      <el-table-column label="项目类型" align="center" prop="proTypeName" />
      <el-table-column label="项目分类" align="center" prop="className" />
      <el-table-column label="合同金额" align="center" prop="contractAmountTotal" />
      <el-table-column label="不含税金额" align="center" prop="contractAmountTax" />
      <el-table-column label="所属分公司名称" align="center" prop="belongCompanyname" />
      <el-table-column label="所属部门名称" align="center" prop="belongDeptname" />
      <el-table-column label="状态说明" align="center" prop="statusPs" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div v-if="scope.row.status ==0">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['projectApproval:appNgc:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['projectApproval:appNgc:remove']">删除</el-button>
          </div>
          <el-button size="mini" type="text" icon="el-icon-view" v-if="scope.row.status==1 || scope.row.status==3"
            @click="exitFlow(scope.row)">终止
          </el-button>
          <div v-if="scope.row.status >0">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="addOrUpdateHandle(scope.row)">审批详情
            </el-button>

            <collctionDetail v-if='showcom'></collctionDetail>
            <el-button size="mini" v-if="scope.row.status>0 && scope.row.instanceId != null" type="text" icon="el-icon-view" @click="examineAndApproves (scope.row)">查看流程图
            </el-button>
            <div v-if="scope.row.status ==4">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleConfirm(scope.row)">确认</el-button>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改非集客项目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body :rules="rules">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否老项目" prop="isOldPro">
              <el-select
                v-model="form.isOldPro"
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
          <el-col :span="8">
            <el-form-item label="项目名称" prop="proName">
              <el-input v-model="form.proName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属公司" prop="belongCompanyid">
              <el-select v-model="form.belongCompanyid" placeholder="请输入所属分公司" style="width:100%;" size="small" @change="getDept($event)"
                :disabled="choise">
                <el-option v-for="dict in belongCompanyNameList" :key="dict.deptId" :label="dict.deptName" :value="dict.deptId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属部门" prop="belongDeptid">
              <treeselect v-model="form.belongDeptid" :options="deptOptions" :show-count="false" placeholder="请选择所属部门" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目类型" prop="proType">
              <el-select v-model="form.proType" style="width:100%;" placeholder="请选择项目类型" @change='changeIsSyncStatus'>
                <el-option v-for="dict in proTypeNameList" :key="dict.id" :label="dict.typeName" :value="dict.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目分类" prop="classification">
              <el-select style="width:100%;" v-model="form.classification" placeholder="请选择项目分类" clearable size="small">
                <el-option v-for="dict in projectSortOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同金额" prop="contractAmountTotal">
              <el-input v-model="form.contractAmountTotal" placeholder="请输入合同金额" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="税率(%)" prop="taxrate">
              <el-input v-model="form.taxrate" placeholder="请输入税率" v-on:input="changeNum()" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不含税金额" prop="contractAmountTax">
              <el-input v-model="form.contractAmountTax" placeholder="不含税金额" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税额(元)" prop="taxamount">
              <el-input v-model="form.taxamount" placeholder="税额" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否招标" prop="isBidInviting">
              <el-radio-group v-model="form.isBidInviting" size="mini" @change='changeApproveResultStatus'>
                <el-radio-button :label="1">是</el-radio-button>
                <el-radio-button :label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="同步资管" prop="isSync" v-show="showIsSync">
              <el-radio-group v-model="form.isSync" size="mini"  :validate-event="false">
                <el-radio-button :label="1">是</el-radio-button>
                <el-radio-button :label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请原因" prop="reqReason">
              <el-input v-model="form.reqReason" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="说明" prop="explainInfo">
              <el-input v-model="form.explainInfo" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="附件" prop="files">
              <el-upload ref="field101" :file-list="field101fileList" :action="field101Action" :headers="token"
                :on-success="handleAvatarSuccess" :on-remove="removeFile" :auto-upload="autoUploadFalg" multiple :limit="20"
                :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <!-- <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div> -->
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批结果" prop="approveResult" v-show="showApproveResult">
              <el-upload ref="field101" :file-list="approveResultList" :action="field101Action" :headers="token"
                :on-success="approveResultSuccess" :on-remove="removeApproveResultFile" :auto-upload="autoUploadFalg"
                multiple :limit="20" :before-upload="field101BeforeUpload" :on-preview="handlePictureCardPreview">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 如果是新建楼盘就显示tab页 -->
        <el-row v-if="showTabPane">
          <el-tabs v-model="activeName">
            <el-tab-pane label="基本信息" name="base" prop="appNgBase">
              <basicInfo ref="basicInfo" :baseInfoDate="form.appNgBase" />
            </el-tab-pane>
            <el-tab-pane label="材料信息" name="second" prop="appNgMaterialList">
              <materialsInfo ref="materialsInfo" :materialsInfoDate="form.appNgMaterialList" :belongCompanyid="form.belongCompanyid"></materialsInfo>
            </el-tab-pane>
            <el-tab-pane label="施工费信息" name="third" prop="appNgConstList">
              <construcostInfo ref="construcostInfo" :construcostInfoDate="form.appNgConstList" :belongCompanyid="form.belongCompanyid"></construcostInfo>
            </el-tab-pane>
            <el-tab-pane label="事前审批表" name="forth" prop="appNgApprovalList">
              <approvalInfo ref="approvalInfo" :approvalInfoDate="form.appxNgReq" :proName="form.proName" :proType="form.proType"
                :belongCompanyid="form.belongCompanyid" />
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="butVis">
        <el-button type="success" :disabled="isForbidden" @click="submitForm(0)">保 存</el-button>
        <el-button type="primary" :disabled="isForbidden" @click="submitForm(1)">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <AddorUpdate v-show="addOrUpdateVisible" ref="addOrUpdate"></AddorUpdate>
  </div>
</template>

<script>
  import {
    getUserInfo,
    exitFlow
  } from "@/api/projectApproval/collectProject";
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
  import {
    treeselect
  } from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {
    getToken
  } from "@/utils/auth";
  import AddorUpdate from './detail.vue';
  import {
    listAppNgc,
    getAppNgc,
    delAppNgc,
    addAppNgc,
    updateAppNgc,
    exportAppNgc,
    checkProNameUnique
  } from "@/api/projectApproval/appNgc";
  import basicInfo from "./basicInfo.vue";
  import approvalInfo from "./approvalInfo.vue";
  import construcostInfo from "./construcostInfo.vue";
  import materialsInfo from "./materialsInfo.vue";

  export default {
    name: "AppNgc",
    components: {
      Treeselect,
      AddorUpdate,
      basicInfo,
      approvalInfo,
      construcostInfo,
      materialsInfo
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

        //提交按钮是否禁用
        isForbidden: true,
        company: null,
        // proName:this.form.proName,
        choise: false,
        //按钮显示
        butVis: true,
        formData: {
          field101: null
        },
        addOrUpdateVisible: false,
        showIsSync: false,
        showTabPane: false, //显示四个标签页
        showApproveResult: false,
        // 遮罩层
        showcom: false,
        loading: true,
        activeName: "base",
        //选中的状态值
        statuses: [],
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        autoUploadFalg: true,
        show: false,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: false,
        // 总条数
        total: 0,
        // 非集客项目表格数据
        appNgcList: [],
        proTypeNameList: [],
        belongCompanyNameList: [],
        projectSortOptions: [],
        deptOptions: [],
        collectTypeOptions: [],
        field101Action: this.$fieldPathAction,
        field101fileList: [],
        approveResultList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          reqNo: null,
          proName: null,
          proType: null,
          proTypeName: null,
          classification: null,
          contractAmountTotal: null,
          contractAmountTax: null,
          taxrate: null,
          taxamount: null,
          belongCompanyid: null,
          belongCompanyname: null,
          belongDeptid: null,
          belongDeptname: null,
          reqReason: null,
          explainInfo: null,
          files: null,
          approveResult: null,
          status: null,
          statusPs: null,
          remarks: null
        },
        // 表单参数
        form: {
          appNgBase: null,
          appNgMaterialList: [],
          appNgConstList: [],
          appxNgReq: null,
          isOldPro:null,
        },
        // 表单校验
        rules: {
          isOldPro: [{
            required: true,
            message: "请选择是否是老项目",
            trigger: "blur"
          }],
          proName: [{
            required: true,
            message: "请输入项目名称",
            trigger: "blur"
          }],
          belongCompanyid: [{
            required: true,
            message: "请选择所属公司",
            trigger: "blur"
          }],
          belongDeptid: [{
            required: true,
            message: "请选择所属部门",
            trigger: "blur"
          }],
          proType: [{
            required: true,
            message: "请选择项目类型",
            trigger: "blur"
          }],
          classification: [{
            required: true,
            message: "请选择项目分类",
            trigger: "blur"
          }],
          isBidInviting: [{
            required: true,
            message: "请选择是否招标",
            trigger: "blur"
          }],
          contractAmountTotal: [{
              required: true,
              message: "请输入金额",
              trigger: "blur"
            },
            {
              pattern: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^[1-9]$)|(^\d\.[1-9]{1,2}$)|(^\d\.[0]{1}[1-9]{1}$|(^\d\.[1-9]{1}[0]{1}$)$)/,
              message: "请正确输入金额",
              trigger: "blur"
            }
          ],
          taxrate: [{
              required: true,
              message: "请输入税率",
              trigger: "blur"
            },
            {
              pattern: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^[1-9]$)|(^\d\.[1-9]{1,2}$)|(^\d\.[0]{1}[1-9]{1}$|(^\d\.[1-9]{1}[0]{1}$)$)/,
              message: "请正确输入税率",
              trigger: "blur"
            }
          ]
        },
        token: {}
      };
    },
    created() {
      this.getList();
      //  this.getTreeselect();
      findProjectType().then(response => {
        let datas = [];
        if (response.data != null && response.data.length > 0) {
          for (let item of response.data) {
            if (item.typeName != "集客项目") {
              datas.push(item);
            }
          }
        }
        this.proTypeNameList = datas;
      });
      findBranchOffice().then(response => {
        this.belongCompanyNameList = response.data;
      });
      this.getDicts("project_sort").then(response => {
        this.projectSortOptions = response.data;
      });
      this.getDicts("collect_type").then(response => {
        this.collectTypeOptions = response.data;
      });
    },
    methods: {
      changeApproveResultStatus() {
        if (this.form.isBidInviting == 1) {
          this.showApproveResult = true;
        } else {
          this.showApproveResult = false;
        }
      },
      changeIsSyncStatus(index) {
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
      },
      getDept(prov) {
        this.deptOptions = [];
        findDeptByBranchOffice(prov).then(
          response => {
            this.deptOptions = response.data;
          }
        );
      },
      //审批详情
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id);
        })
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
      /** 查询非集客项目列表 */
      getList() {
        this.loading = true;
        listAppNgc(this.queryParams).then(response => {
          this.appNgcList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 文件上传检测 **/
      // field101BeforeUpload(file) {
      //   let isRightSize = file.size / 1024 / 1024 < 2;
      //   if (!isRightSize) {
      //     this.$message.error("文件大小超过 2MB");
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
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.isForbidden = false;
        // this.form = Object.assign({},{});
        this.form = {
          id: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          reqNo: null,
          proName: null,
          proType: null,
          proTypeName: null,
          classification: null,
          contractAmountTotal: null,
          contractAmountTax: null,
          taxrate: null,
          taxamount: null,
          belongCompanyid: null,
          belongCompanyname: null,
          belongDeptid: null,
          belongDeptname: null,
          reqReason: null,
          explainInfo: null,
          files: null,
          status: 0,
          statusPs: null,
          remarks: null,
          delFlag: null,
          appNgBase: {},
          appNgMaterialList: [],
          appNgConstList: [],
          appxNgReq: null,
          // appxNgNo:null,
          projectTypeId:null,
        };
        this.resetForm("form");
      },
      changeNum() {
        let contractAmountTotal = this.form.contractAmountTotal;
        let taxrate = this.form.taxrate;
        if (contractAmountTotal && taxrate) {
          /** 税率转换 **/
          let taxrateStr = taxrate / 100;
          this.form.contractAmountTax = this.formatPrice(
            contractAmountTotal / (1 + taxrateStr)
          );
          this.form.taxamount = this.formatPrice(
            contractAmountTotal - this.form.contractAmountTax
          );
        }
      },
      /** 格式化金额 **/
      formatPrice(value) {
        let tempVal = parseFloat(value).toFixed(3);
        let realVal = tempVal.substring(0, tempVal.length - 1);
        return realVal;
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
        this.ids = selection.map(item => item.id);
        this.single = selection.length !== 1;
        this.multiple = !selection.length;
        this.statuses = selection.map(item => item.status);
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加非集客项目";
        /** 加入请求头 **/
        this.token = {
          Authorization: getToken()
        };
        this.butVis = true;

        this.$refs.basicInfo = null;
        this.$refs.materialsInfoDate = [];
        this.$refs.construcostInfo = [];
        this.$refs.approvalInfo = null;
        this.field101fileList = [];
        this.approveResultList = [];
        this.showTabPane = false;
        getUserInfo().then(response1 => {

          if (response1.data.user.userName == 'admin') {
            this.choise = false;
            //this.form.belongCompanyid=(response1.data.user.dept.deptId);
            // this.$refs.approvalInfo.formData.applyCompany = response1.data.belongDeptName;
          } else {
            this.choise = true;
            this.form.belongCompanyid = (response1.data.user.companyId);
            this.getDept(this.form.belongCompanyid);
            this.$refs.construcostInfo.deptId = this.form.belongCompanyid;
            findLoginbyCompany(response1.data.user.userId).then(response2 => {
              this.company = response2.msg;
            });
          }

        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids;
        let deleSta1 = 0;
        for (let item of id) {
          if (item == null) {
            deleSta1 = 1;
            break;
          }
        }
        if (deleSta1 == 1) {
          this.msgSuccess("请修改有合同信息的记录");
          return false;
        }
        const _selectData = this.$refs.itsmDataTable.selection
        let deleSta = 0;
        if (_selectData != '') {
          const flag = false;
          for (let item of _selectData) {
            if (item.status == 1 || item.status == 2) {
              deleSta = 1;
              break;
            }
          }
          if (deleSta != 0) {
            this.msgSuccess("请修改未提交的数据");
            return false;
          }
        }
        getAppNgc(id).then(response => {
          this.form = response.data;
          this.$refs.materialsInfo = response.data.appNgMaterialList;
          this.$refs.construcostInfo = response.data.appNgConstList;
          if (this.form.belongCompanyid) {
            this.form.belongCompanyid = parseInt(this.form.belongCompanyid);
          }
          if (this.form.classification) {
            this.form.classification = this.form.classification + "";
          }
          this.open = true;
          this.title = "修改非集客项目";
          if (response.data.status > 0) {
            this.butVis = false;
          } else {
            this.butVis = true;
          }

          //显示标签面板
          if (this.form.proType == "36e4550f15954fbbadf4dff4f6fc63ea") {
            this.showTabPane = true;
          } else {
            this.showTabPane = false;
          }

          getUserInfo().then(response1 => {

            if (response1.data.user.userName == 'admin') {
              this.choise = false;
              this.getDept(this.form.belongCompanyid)
            } else {
              this.choise = true;
              this.form.belongCompanyid = (response1.data.user.dept.deptId);
              this.getDept(this.form.belongCompanyid)
            }

          });

        });
        /** 获取文件列表 **/
        // findFileListByPid(id).then(response => {
        //   this.field101fileList = response.data;
        // });
        findFileListByPidPtype(id, 1).then(response => {
          this.field101fileList = response.data;
        });
        findFileListByPidPtype(id, 2).then(response => {
          this.approveResultList = response.data;
        });
      },
      /** 提交按钮 */
      submitForm(number) {

        this.$refs["form"].validate(valid => {
          if (valid) {

            checkProNameUnique(this.form.proName).then(response => {
              if((this.form.id != null && response > 1) || (this.form.id == null && response > 0)){
                this.msgSuccess("项目名称已存在");
                return false;
              } else {
                if (this.form.proType == "36e4550f15954fbbadf4dff4f6fc63ea") {
                  if(this.$refs.basicInfo.formData.budgetMaterial == null || this.$refs.basicInfo.formData.budgetMaterial == "" || this.$refs.basicInfo.formData.budgetLabor == null || this.$refs.basicInfo.formData.budgetLabor == ""){
                    this.msgSuccess("请完善必填项");
                    return false;
                  }
                  this.form.appNgBase = this.$refs.basicInfo.formData;
                  this.form.appNgMaterialList = this.$refs.materialsInfo.dataList;
                  this.form.appNgConstList = this.$refs.construcostInfo.dataList;
                  this.form.appxNgReq = this.$refs.approvalInfo.formData;
                }
                if(this.form.proType == "f03e24f34279452baed1cba24896653e"){
                  if(this.form.isSync == null){
                    this.msgSuccess("请选择是否同步资管");
                    return false;
                  }
                }


                this.form.status = number;
                this.isForbidden = true;
                if (this.form.id != null) {
                  updateAppNgc(this.form).then(response => {
                    this.msgSuccess("修改成功");
                    this.open = false;
                    this.isForbidden = false;
                    this.getList();
                  });
                } else {
                  if (this.form.proType == "36e4550f15954fbbadf4dff4f6fc63ea") {
                    this.form.appxNgReq.applyDate = null;
                  }
                  addAppNgc(this.form).then(response => {
                    this.msgSuccess("新增成功");
                    this.open = false;
                    this.isForbidden = false;
                    this.getList();
                  });
                }
              }
            });


          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        const statuses = row.status || this.statuses;
        if (statuses != null && statuses.length > 0) {
          for (let sta of statuses) {
            if (sta > 0) {
              this.msgSuccess("请选中未提交的数据进行删除");
              return false;
            }
          }
        }
        if (statuses > 0) {
          this.msgSuccess("请选中未提交的数据进行删除");
          return false;
        }
        this.$confirm(
            '是否确认删除该数据?',
            "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }
          )
          .then(function() {
            return delAppNgc(ids);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          });
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
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm("是否确认导出所有非集客项目数据项?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(function() {
            return exportAppNgc(queryParams);
          })
          .then(response => {
            this.download(response.msg);
          });
      },
      //流程终止
      exitFlow(row) {
        let instanceId = row.instanceId;
        this.$confirm('是否确认终止本条数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then((obj) => {
          //exitFlow(instanceId);
          exitFlow(instanceId, 'appNgcTermination').then(response => {
            this.getList();
            this.msgSuccess("终止成功");
          });
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
          window.open(url_);
        }
      },
    }
  };
</script>
<style scoped lang="scss">
  .el-textarea .el-textarea__inner {
    resize: none;
  }
</style>
