<template>
  <div class="dashboard-editor-container">
    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <el-row style="margin-bottom:32px;" :gutter="20">
      <el-col :xs="12" :sm="12" :lg="12" >
          <el-card class="box-card">
          	<div slot="header" class="clearfix">
              <img src="../assets/image/notice.png" />
          		<span>&nbsp;通知公告</span>
          		<el-button style="float: right; padding: 3px 0" type="text">
                <router-link :to="'/notice/noticeList'" class="link-type">
                  <span><i class="el-icon-more"> </i></span>
                </router-link>
              </el-button>
          	</div>
          	<div v-for="(notice , index) in noticeList" :key="index" v-if="index < 5"  class="row-text">
          		<el-col :xs="13" :sm="13" :lg="13" style="white-space: nowrap;" :title="notice.noticeTitle">{{(notice.noticeTitle).length>20?(notice.noticeTitle).substring(0,20):notice.noticeTitle }}</el-col>
              <el-col :xs="4" :sm="4" :lg="4" >{{notice.createBy}}</el-col>
              <el-col :xs="7" :sm="7" :lg="7" >{{notice.createTime}}</el-col>
          	</div>
          </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="12" >
        <el-card class="box-card">
        	<div slot="header" class="clearfix">
            <img src="../assets/image/schedule.png" />
        		<span>&nbsp;我的待办</span>
        		<el-button style="float: right; padding: 3px 0" type="text" >

              <router-link :to="'/task/dbTask'" class="link-type">
                <span><i class="el-icon-more"> </i></span>
              </router-link>

           </el-button>
        	</div>
        	<div v-for="(task , index) in taskList" :key="index" v-if="index < 5" class="row-text">
        		<el-col :xs="10" :sm="10" :lg="10"   v-bind:title="(task.instanceName)">{{(task.instanceName).length>15?(task.instanceName).substring(0,15): (task.instanceName)}}</el-col>
        	  <el-col :xs="4" :sm="4" :lg="4" >{{task.applyName }}</el-col>
        	  <el-col  :xs="7" :sm="7" :lg="7"  >{{task.createdDate }}</el-col>
            <el-col :xs="3" :sm="3" :lg="3"  align="right">
              <el-button type="primary" plain icon="el-icon-edit" size="mini" @click="examineAndApprove(task)"></el-button>
            </el-col>
        	</div>
        </el-card>
      </el-col>
    </el-row>
    <!-- <p style="border-left:5px solid #409EFF;padding-left: 10px;">2020年06月履约执行合同统计</p> -->
    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <line-chart :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row> -->
    <!--审核列表-->

    <!-- 审批对话框 -->
    <el-dialog :title="title" :visible.sync="open" v-if="open" width="80%" append-to-body>
      <leaveHistoryForm :businessKey="businessKey"  v-if="audit"/>
      <collectOrder :businessKey="businessKey"  v-if="collectAudit"/>
      <contractInfo :businessKey="businessKey" v-if="contractAudit"/>
      <ngcInfo :businessKey="businessKey"  v-if="ngcAudit"/>
      <payInfo :businessKey="businessKey"  v-if="payInfo"/>
      <ticketRecordInfo :businessKey="businessKey"  v-if="ticketRecordInfo"/>
      <!--      集客项目-->
      <el-form :model="forms" ref="forms" label-width="100px" class="demo-dynamic">
        <div v-if="ngAudit">

          <el-row :gutter="15">
            <el-col :span="12">
              <el-form-item label="项目名称" prop="proName" label-width="130px">
                <el-input v-model="forms.id" placeholder="请输入项目名称" v-if="show"/>
                <el-input v-model="forms.status" placeholder="请输入项目名称" v-if="show"/>
                <el-input v-model="forms.proName" placeholder="请输入项目名称"  :disabled="disabled"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预计投资金额(元)" prop="expectAmount" label-width="130px">
                <el-input
                  v-model="forms.expectAmount"
                  clearable
                  placeholder="请输入预计投资金额"
                  oninput="value=value.replace(/[^0-9.]/g,'')"
                  :disabled="disabled"
                  @blur="onBsp($event)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="企业是否垫资" prop="isAdvanceNeed" label-width="130px">
                <el-select
                  v-model="forms.isAdvanceNeed"
                  placeholder="请选择"
                  style="width: 100%;"
                  :disabled="disabled"
                >
                  <el-option
                    v-for="dict in isAdvanceNeedList"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规划部是否确认" prop="isPlanConfirm" label-width="130px">
                <el-select
                  v-model="forms.isPlanConfirm"
                  placeholder="请选择"
                  style="width: 100%;"
                  :disabled="disabled"
                >
                  <el-option
                    v-for="dict in isPlanConfirmList"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>


            <el-col :span="24">
              <el-form-item label="涉及分公司" prop="selectComp" label-width="130px">
                <el-select
                  v-model="forms.selectComp"
                  multiple
                  placeholder="请选择"
                  @change="$forceUpdate()"
                  :style="{ width: '100%' }"
                  :disabled="disabled"
                >
                  <el-option
                    v-for="dict in belongCompanyNameList"
                    :key="dict.deptId"
                    :label="dict.deptName"
                    :value="dict.deptName+'('+dict.deptId+')'"
                  />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="项目描述" prop="projectDescription" label-width="130px">
                <el-input
                  type="textarea"
                  :rows="3"
                  placeholder="请输入内容"
                  :disabled="disabled"
                  v-model="forms.projectDescription">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目效益说明" prop="benefitDescription" label-width="130px">
                <el-input
                  type="textarea"
                  :rows="3"
                  placeholder="请输入内容"
                  :disabled="disabled"
                  v-model="forms.benefitDescription">
                </el-input>
              </el-form-item>
            </el-col>

          </el-row>
          <el-row v-if="ghb">
            <el-col :span="12">
              <el-form-item label="实际投资金额(元)" prop="actualAmount" label-width="130px">
                <el-input
                  v-model="forms.actualAmount"
                  clearable
                  placeholder="请输入实际投资金额"
                  oninput="value=value.replace(/[^0-9.]/g,'')"
                  :disabled="disabled"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">

              <el-form-item
                label="建设方案" prop="constructionPlan" label-width="130px">
                <el-upload
                  :disabled="authenStatus"
                  ref="field111"
                  :file-list="constructionPlan"
                  :action="field111Action"
                  :headers="token"
                  :on-success="contractApprovalFileSuccess"
                  :on-remove="removeContractApprovalFile"
                  :auto-upload="autoUploadFalg"
                  multiple
                  :limit="20"
                  :before-upload="field111BeforeUpload"
                  :on-preview="handlePictureCardPreview"
                >
                  <el-button slot="trigger"  size="small" type="primary" :disabled="authenStatus">选取文件</el-button>
                  <!-- <div slot="tip" class="el-upload__tip">  v-if="uploadVis"
                      只能上传jpg/png文件，且不超过500kb
                    </div> -->
                </el-upload>


              </el-form-item>



            </el-col>
          </el-row>
          <el-row  v-if="showTable">
            <el-col :span="24">
              <table class="curr-table " border="1" style="border-color: #ddd;">
                <tbody>
                <tr>
                  <th width="10%">序号</th>
                  <th width="75%">已选分公司</th>
                  <th width="15%">操作</th>
                </tr>
                <tr v-for="(value, index) in forms.selectComp" :key="value">
                  <td align="center">
                    {{ index+1 }}
                  </td>
                  <td align="center">
                    {{value }}

                  </td>
                  <td align="center">
                    <el-button
                      size="mini"
                      type="success"
                      @click="openInfoWin(value)"
                      style="margin: 10px;"
                    >填写信息</el-button>
                  </td>
                </tr>
                </tbody>
              </table>
            </el-col>
          </el-row>
          <el-row style="margin-top: 15px;" v-if="showTables">
            <el-col :span="12">
              <el-form-item label="事前审批表" prop="isPlanConfirm">
                <el-button type="primary" size="small" plain @click="showApprovalInfo()">在线查看</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="showUploads">
              <el-form-item
                label="上传资料" prop="uploudInfo">
                <el-upload
                  :disabled="dis"
                  ref="field112"
                  :file-list="uploudInfo"
                  :action="field111Action"
                  :headers="token"
                  :on-success="approvalUploadFileSuccess"
                  :on-remove="removeApprovalUploadFile"
                  :auto-upload="autoUploadFalg"
                  multiple
                  :limit="20"
                  :before-upload="field111BeforeUpload"
                  :on-preview="handlePictureCardPreview"
                >
                  <el-button slot="trigger"  size="small" type="primary" :disabled="dis">选取文件</el-button>
                  <div slot="tip" class="el-upload__tip">
                    只能上传jpg/png/gif/pdf文件，且不超过20M
                  </div>
                </el-upload>
              </el-form-item>
            </el-col>




          </el-row>
          <!-- 事前审批窗口 -->
          <el-dialog title="事前审批"
                     :visible.sync="approvalInfoVisible"
                     width="80%"
                     append-to-body
                     @close=""
          >
            <approvalInfo ref="approvalInfo" :approvalInfoDate="forms.appxNgReq" :appxNgName="forms.proName" :proType="forms.proType"></approvalInfo>
            <div slot="footer" class="dialog-footer">
              <el-button  @click="approvalInfoVisible = false">取 消</el-button>
            </div>
          </el-dialog>
          <!-- 编辑分公司信息的内部嵌套窗口 -->
          <el-dialog title="编辑信息"
                     :visible.sync="editInfoVisible"
                     width="80%"
                     append-to-body
                     @close="">
            <el-tabs v-model="activeName">
              <el-tab-pane label="基本信息" name="base" prop="appNgBase">
                <basicInfo
                  ref="basicInfo"
                  :baseInfoDate="forms.appNgBase"
                ></basicInfo>
              </el-tab-pane>
              <el-tab-pane label="材料信息" name="second" prop="appNgMaterialList"
              ><materialsInfo
                ref="materialsInfo"
                :materialsInfoDate="forms.appNgMaterialList"
              ></materialsInfo
              ></el-tab-pane>
              <el-tab-pane label="施工费信息" name="third" prop="appNgConstList"
              ><construcostInfo
                ref="construcostInfo"
                :construcostInfoDate="forms.appNgConstList"
              ></construcostInfo
              ></el-tab-pane>


            </el-tabs>
            <div slot="footer" class="dialog-footer">
              <el-button @click="editInfoVisible = false">取 消</el-button>
            </div>
          </el-dialog>

          <el-row :gutter="15">
            <el-table
              :data="fromData"
              border
              style="width: 100%">
              <el-table-column
                prop="taskNodeName"
                label="审批部门"
                width="250">
              </el-table-column>
              <el-table-column
                prop="createName"
                label="审批人"
                width="180">
              </el-table-column>
              <el-table-column
                prop="createdDate"
                width="180"
                label="审批时间">
              </el-table-column>
              <el-table-column
                width="100"
                prop="formHistoryDataDTO[0].value"
                label="是否同意">
              </el-table-column>
              <el-table-column
                prop="formHistoryDataDTO[1].value"
                label="审批意见(批注)">
              </el-table-column>
            </el-table>
          </el-row>

        </div>
      </el-form>
      <el-form :model="form" ref="form" label-width="100px" class="demo-dynamic">
        <el-form-item
          v-for="(domain, index) in form.formData"
          :label="domain.controlLable"
          :key="index"
        >
          <el-radio-group v-model="domain.controlValue" v-if="'radio'==domain.controlType">
            <el-radio v-for="(defaults,indexd) in domain.controlDefault.split('--__--')"
                      :label=indexd
                      :key="indexd"
                       @change="changeInput(($event))"
            >{{defaults}}

            </el-radio>

          </el-radio-group>
          <el-input type="textarea" @input="changeInput($event)" v-model="domain.controlValue" v-if="'textarea'==domain.controlType"
          ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm_"  v-if="showSave">保 存</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    findProjectType,
    findBranchOffice,
    findFileListByPidPtype,
    findNextAppNo,
    findDeptByBranchOffice
  } from "@/api/utils";
  import {confirmTransfer,getUserByRole,listTask, formDataShow, formDataSave} from "@/api/activiti/task"; //导入接口js文件
  import leaveHistoryForm from './activiti/task/leaveHistoryForm.vue';  //导入自定义组件页面
  import collectOrder  from './activiti/task/collectOrder.vue';  //导入自定义组件页面
  import contractInfo  from './activiti/task/contractInfo.vue';  //导入自定义组件页面
  import ngcInfo from './activiti/task/ngcInfo.vue';  //导入自定义组件页面
  import { historyFromData,getCollection } from '@/api/activiti/historyFormdata'
  import basicInfo from "./activiti/task/nginfo/basicInfo.vue";
  import materialsInfo from "./activiti/task/nginfo/materialsInfo.vue";
  import construcostInfo from "./activiti/task/nginfo/construcostInfo.vue";
  import approvalInfo from "./activiti/task/nginfo/approvalInfo";
  import payInfo from "./activiti/task/payInfo.vue";
  import ticketRecordInfo from "./activiti/task/ticketRecordInfo.vue";
  import { listNotice } from "@/api/system/notice";
  import { getToken } from "@/utils/auth";

  import {listCollectProject,
    getCollectProject,
    delCollectProject,
    addCollectProject,
    updateCollectProject,
    addCollectProject_,
    updateCollectProject_,
    exportCollectProject,
    getFxCollectProject,
    selectCtBuApprovalNgReqById,
    getUserInfo} from "@/api/projectApproval/collectProject";



  import PanelGroup from './dashboard/PanelGroup'
  import LineChart from './dashboard/LineChart'
  import request from '@/utils/request'
  import { countContractIndex } from '@/api/contract/contractInfo';
  const lineChartData = {
    newVisitis: {
      expectedData: [100, 120, 161, 134, 105, 160, 165],
      actualData: [120, 82, 91, 154, 162, 140, 145],
      actualData1: [10, 8, 9, 54, 162, 140, 145]
    },
    messages: {
      expectedData: [200, 192, 120, 144, 160, 130, 140],
      actualData: [180, 160, 151, 106, 145, 150, 130]
    },
    purchases: {
      expectedData: [80, 100, 121, 104, 105, 90, 100],
      actualData: [120, 90, 100, 138, 142, 130, 130]
    },
    shoppings: {
      expectedData: [130, 140, 141, 142, 145, 150, 160],
      actualData: [120, 82, 91, 154, 162, 140, 130]
    }
  }
  export default {
    name: 'Index',
    components: {
      PanelGroup,
      LineChart,
      leaveHistoryForm,collectOrder,contractInfo,ngcInfo,
      basicInfo,
      materialsInfo,
      construcostInfo,
      approvalInfo,
      payInfo,
      ticketRecordInfo
    },
    data() {
      return {
        showSave:false,
        fromData:[],
        uploudInfo:[],
        showUploads:false,
        autoUploadFalg: true,
        token: {},
        field111Action: this.$fieldPathAction,
        constructionPlan:[],
        authenStatus:false,
        noticeList: [],
        showMsg:null,
        lineChartData: lineChartData.newVisitis,
        taskList:[],
        //本月合同统计
        countContract:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        // path:"ws://127.0.0.1:9001/qrCodePage/ID=1/refreshTime=5",
        //new WebSocket("ws://localhost:8888/WebSocketServer/2");
        path:"ws://127.0.0.1:9001/webSocketServer/2",
        socket:"",
        queueReceiveSetting: { // 消息队列配置
          websock: null,
          client: null,
          wsuri: "ws://127.0.0.1:9001/websocket/",
        },

        editInfoVisible:false,  //信息编辑窗口
        approvalInfoVisible:false ,  //审批窗口
        newProjectVisible:false,  //立项新增窗口
        deptId_:null,
        showTables:false,
        showTable:true,
        butVis:false,
        ghb:false,
        bc1:true,
        collectProjectList: [],  // 表格数据
        collectTypeOptions: [],  //状态
        proTypeNameList: [],  //项目类型
        belongCompanyNameList: [], //所属分公司
        isPlanConfirmList:[],  // 是否经过规划确认
        isAdvanceNeedList:[],  //公司是否垫资
        disabled:null,
        fromData:[],
        tableDatas_:[],
        activeName: "base",
        //极客
        ngAudit:false,
        //非极客
        ngcAudit:false,
        payInfo:false,
        show:false,
        // 人员信息列表
        userList: [],
        htloading:true,
        //部门人员选中
        innerhtVisible:false,
        audit:false,
        //收款单
        collectAudit:false,
        //合同审核
        contractAudit:false,
        id:'',
        definitionKey: '',
        businessKey: '',
        // 遮罩层
        loading: true,
// 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 请假表格数据
        tastList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        // 查询参数
        queryParams_: {
          pageNum: 1,
          pageSize: 10,
        },
        // 表单参数
        forms: {

        },
      }
    },
    created(){

      // this.showMessage();
      this.getTaskList();
      this.getNoticeList();
      this.getDicts("collect_type").then(response => {
        this.collectTypeOptions = response.data;
      });
      findProjectType().then(response => {
        this.proTypeNameList = response.data;
      });
      findBranchOffice().then(response => {
        this.belongCompanyNameList = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.isPlanConfirmList = response.data;
        this.isAdvanceNeedList  = response.data;
      });
    },
    methods: {
      changeInput(val){
        this.$forceUpdate()
      },
      approvalUploadFileSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.uploudInfo) {
          this.form.uploudInfo += res.msg + ",";
        } else {
          this.form.uploudInfo = res.msg + ",";
        }
        this.uploadVal1 = true;
        this.$refs.form.validateField("uploudInfo");
      },
      /** 删除附件操作 **/
      removeApprovalUploadFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.uploudInfo == 200) {
            this.form.uploudInfo = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.uploudInfo) {
                this.form.uploudInfo += element.uid + ",";
              } else {
                this.form.uploudInfo = element.uid + ",";
              }
            }
            this.form.uploudInfo = null;
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
          var url_ = url+":9001/profile"+files[1];
          window.open(url_);
          // window.open(url+":9001/dictionary/fileManage/dwonLoadFile/"+file.uid+'/'+file.ptype);
        }
      },
      /** 文件上传检测 **/
      field111BeforeUpload(file) {
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
      },
      /** 上传附件成功操作 **/
      contractApprovalFileSuccess(res, file, fileList) {
        for (let index = 0; index < fileList.length; index++) {
          const element = fileList[index];
          if (element.uid == file.uid) {
            element.uid = res.msg;
          }
        }
        if (this.form.constructionPlan) {
          this.form.constructionPlan += res.msg + ",";
        } else {
          this.form.constructionPlan = res.msg + ",";
        }
        this.uploadVal1 = true;
        this.$refs.form.validateField("constructionPlan");
      },
      /** 删除附件操作 **/
      removeContractApprovalFile(file, fileList) {
        delFileById(file.uid).then(response => {
          if (response.constructionPlan == 200) {
            this.form.constructionPlan = null;
            for (let index = 0; index < fileList.length; index++) {
              const element = fileList[index];
              if (this.form.constructionPlan) {
                this.form.constructionPlan += element.uid + ",";
              } else {
                this.form.constructionPlan = element.uid + ",";
              }
            }
          }
        });
      },
      getNoticeList() {
        listNotice(this.queryParams).then(response => {
          this.noticeList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      openInfoWin(val){
        let value = val.indexOf("(")+1;
        this.deptId_ = parseInt(
          val.substring(value,val.length-1)
        );
        this.editInfoVisible = true;
        let id = this.forms.id;
        getFxCollectProject(id,this.deptId_).then(response => {
          let storage = (sessionStorage.getItem(this.deptId_));
          let appBase = {};
          let mateList = {};
          if(storage!=null){
            appBase = JSON.parse(storage);
          }
          /** 基本信息赋值 **/
          if(response.data.appNgBase==null || response.data.appNgBase.id==null){
            if(storage==null){
              this.$refs.basicInfo.formData = {};
            }else{
              this.$refs.basicInfo.formData = appBase.appNgBase;
              this.$refs.basicInfo.formData.projectTypeId = appBase.appNgBase.projectTypeId+"";
            }
          }else{
            this.$refs.basicInfo.formData = response.data.appNgBase;
            this.$refs.basicInfo.formData.projectTypeId = response.data.appNgBase.projectTypeId+"";
          }
          /** 材料信息 **/
          if(response.data.appNgMaterialList==null || response.data.appNgMaterialList.length<=0){
            if(storage==null){
              this.$refs.materialsInfo.dataList = [];
            }else{
              this.$refs.materialsInfo.dataList = appBase.appNgMaterialList;
            }
            console.log(this.$refs.materialsInfo.dataList);
          }else{
            this.$refs.materialsInfo.dataList = response.data.appNgMaterialList;
          }
          /** 施工费信息 **/
          if(response.data.appNgConstList==null || response.data.appNgConstList.length<=0){
            if(storage==null){
              this.$refs.construcostInfo.dataList = [];
            }else{
              this.$refs.construcostInfo.dataList = appBase.appNgConstList;
            }
          }else{
            this.$refs.construcostInfo.dataList = response.data.appNgConstList;
          }
        });
      },
      showApprovalInfo(){
        let id = this.forms.id;

        this.$refs["forms"].validate(valid => {
          if (valid) {
            this.approvalInfoVisible = true;
            selectCtBuApprovalNgReqById(id).then(response => {
              /** 事前审批 **/
              if(response.data.approvalInfo==null){
                this.$refs.approvalInfo.formData  = this.form.appxNgReq==null?{}:this.form.appxNgReq;
              }else{
                this.$refs.approvalInfo.formData = response.data.appxNgReq;
              }

              // this.$refs.approvalInfo.approvalInfoDate = response.data.appxNgReq;

              /** 事前审批 **/
              this.$refs.approvalInfo.formData = response.data.appxNgReq;

              this.$refs.approvalInfo.formData.proType = "9f30ebd46d3c428cbc5e54f7175eb4f2";
              this.$refs.approvalInfo.formData.appxNgNo = response.data.projectCode;
              this.$refs.approvalInfo.formData.applyCompany = response.data.belongDeptName;

              this.$refs.approvalInfo.formData.stuffcostmoney = response.data.cltotal;
              this.$refs.approvalInfo.formData.buildcostmoney = response.data.sgtotal;
            });
          }
        })


      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      /** 提交按钮 */
      submitForm() {
        formDataSave(this.id,this.form.formData).then(response => {
          this.msgSuccess("审批成功");
          this.open = false;
          this.getTaskList();
        });
      },
      historyFromData() {
        historyFromData(this.businessKey).then(response => {
          this.fromData = response.data
        })
      },
      // 表单重置
      reset() {

        this.definitionKey = '',
          this.businessKey = '',
          this.form = {
            formData:[],
          };
        this.showSave = false;
        this.disabled=false;
        this.showUploads = false;
        this.resetForm("form");
      },
      /** 审批按钮操作 */
      examineAndApprove(row) {
        this.token = {Authorization: getToken()};
        this.reset();
        this.definitionKey = row.definitionKey;
        this.businessKey = row.businessKey;
        this.id=row.id;

        formDataShow(row.id).then(response => {
          let datas = response.data;
          let formData = []
          if(datas!=null && datas.length>0){
            for (let i = 0; i < datas.length; i++) {
              let strings = datas[i].split('--__!!')
              let controlValue = null
              let controlDefault = null
              switch (strings[1]) {
                case 'radio':
                  controlValue = 0;
                  controlDefault = strings[4]
                  break;
                // default:
              }
              formData.push({
                controlId: strings[0],
                controlType: strings[1],
                controlLable: strings[2],
                controlIsParam: strings[3],
                controlValue: controlValue,
                controlDefault: controlDefault
              })
            }
          }

          this.getDioag(row.instanceName);
          this.form.formData = formData;
          this.open = true;
          this.title = "审批详情";

          if(row.name =='经办人上传资料'){
            this.showUploads = true;
            this.showSave = true;
          }else{
            this.showUploads = false;
          }

          if(row.name =='市公司集客部确认审核'){
            this.showUploads = true;
            this.dis = true;
          }

          //添加表单
          if(this.ngAudit){
            getCollectProject(this.businessKey).then(response1 => {

              this.forms = response1.data;
              let brans = response1.data.companyList;
              let dpData = [];
              if(brans!='' && brans.length>0){
                let num=0;
                for(let item of brans)
                {
                  let name = "";
                  for(let it1 of this.belongCompanyNameList){
                    if(item.branchInvolvedId==it1.deptId){
                      name=it1.deptName+'('+it1.deptId+')';
                      break;
                    }
                  }
                  dpData.push(name);
                  this.oldData = dpData;
                  num++;
                }
                this.forms.selectComp = dpData;
              }
              this.forms.belongCompanyid = parseInt(this.forms.belongCompanyid);
              this.title = "集客项目";
              if(this.forms.isAdvanceNeed=='N' || (this.forms.isAdvanceNeed=='Y' && this.forms.isPlanConfirm=='N')){
                if(response1.data.status==0 || response1.data.status==3 ){
                  this.butVis = true;
                }else{
                  this.butVis = false;
                }
              }else{
                this.bc3 = false;
                this.tj3 = false;
                this.ghb=true;
                this.authenStatus=true;
              }
              this.showTables = true;
              this.historyFromData();



              if(this.businessKey!=null && this.businessKey!=''){
                findFileListByPidPtype(this.businessKey,1).then(response5 => {
                  this.constructionPlan= response5.data;
                });
                findFileListByPidPtype(this.businessKey,5).then(response6 => {
                  this.uploudInfo= response6.data;
                  if(this.uploudInfo!=null && this.uploudInfo.length>0){
                    this.form.uploudInfo = this.forms.uploudInfo;
                  }
                });
              }else{
                this.$nextTick(() => {
                  this.$refs.field111.clearFiles();
                  this.$refs.field112.clearFiles();
                })
              }
            });
          }

        });
      },
      getDioag(obj){
        if(obj.indexOf("发票申请")>=0 || obj.indexOf("开票申请")>=0){
          this.audit = true;
          this.collectAudit = false;
          this.contractAudit = false;
          this.ngcAudit = false;
          this.ngAudit = false;
          this.payInfo = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("收款单审核")>=0){
          this.collectAudit = true;
          this.audit = false;
          this.contractAudit = false;
          this.ngcAudit = false;
          this.ngAudit = false;
          this.payInfo = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("合同审核")>=0){
          this.contractAudit = true;
          this.addOrUpdateVisible = false;
          this.collectAudit = false;
          this.ngcAudit = false;
          this.ngAudit = false;
          this.payInfo = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("非集客项目(10w限额以下的审批流程)")>=0 || obj.indexOf("非集客项目(10w限额以上的审批流程)")>=0){
          this.contractAudit = false;
          this.addOrUpdateVisible = false;
          this.collectAudit = false;
          this.ngcAudit = true;
          this.ngAudit = false;
          this.payInfo = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("三重一大项目(预算投资金额≥50万元)")>=0
          || obj.indexOf("小集客业务(预计项目金额＜10万元 或 投资额＜5万元的项目)")>=0
          || obj.indexOf("跨区项目(金额≥10万元 或 5万元≤投资额＜50万元)")>=0 ||
          obj.indexOf("非跨区项目(项目金额≥10万元或5万元≤投资额＜50万元)")>=0
          ||obj.indexOf("不涉及网络自主建设或技术平台建设及应用(项目金额≥10万元（非垫资）)")>=0
        ){
          this.contractAudit = false;
          this.addOrUpdateVisible = false;
          this.collectAudit = false;
          this.ngcAudit = false;
          this.ngAudit = true;
          this.payInfo = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("付款申请单审核")>=0){
          this.payInfo = true;
          this.contractAudit = false;
          this.addOrUpdateVisible = false;
          this.collectAudit = false;
          this.ngcAudit = false;
          this.ngAudit = false;
          this.ticketRecordInfo = false;
        }
        if(obj.indexOf("收票记录审核")>=0){
          this.ticketRecordInfo = true;
          this.payInfo = false;
          this.contractAudit = false;
          this.addOrUpdateVisible = false;
          this.collectAudit = false;
          this.ngcAudit = false;
          this.ngAudit = false;
        }

      },
      //获取代办列表
      getTaskList(){
        let query = this.queryParams;
        return request({
          url: '/task/list',
          method: 'get',
          params: query
        }).then(response => {
          this.taskList = response.rows;
        });
      },

      handleSetLineChartData(type) {
        this.lineChartData = lineChartData[type]
      },

      /** 查询当月顶部合同统计 */
      countContractIndex() {
        this.loading = true;
        countContractIndex().then(response => {
          this.countContract = response.rows;
          this.loading = false;
        });
      },
    },



  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 10px 32px;
    background-color: #fafafa;
    position: relative;

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
  .news-col{
      padding: 20px;
      background: #fff;
      border: 1px solid #ddd;
  }
  .box-card img{
    width: 20px;
    vertical-align: bottom;
  }
  .box-card .row-text{
    font-size: 14px;
    line-height: 36px;
    height: 36px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }


  ::v-deep .el-input-group__append{
    padding: 0 12px;
  }
  ::v-deep .el-dialog {
    .el-dialog__body {
      padding: 5px 20px;
    }
  }
  .curr-table {
    text-align: center;
    width: 100%;
    vertical-align: middle;
    border-spacing: 0px;
    border-style: none;
    border-collapse: collapse;
  }
  .curr-table td,th {
    padding: 5px 10px;
  }
  .curr-table th {
    font-weight: bold;
    background-color: #eff3fa;
    height: 40px;
  }


</style>
