<template>
  <div>
    <el-row :gutter="15">
      <el-col :span="24" >
        <el-divider content-position="center" ><el-link type="primary"><b>收款单信息</b></el-link></el-divider>
      </el-col>
	<el-form ref="form" :model="form"  label-width="80px">

          <el-tabs v-model="activeName">
            <el-tab-pane label="基本信息" name="base" prop="appNgBase">
              <basicInfo
                ref="basicInfo"
                :baseInfoDate="form.appNgBase"
              ></basicInfo>
            </el-tab-pane>
            <el-tab-pane label="材料信息" name="second" prop="appNgMaterialList"
              ><materialsInfo
                ref="materialsInfo"
                :materialsInfoDate="form.appNgMaterialList"
              ></materialsInfo
            ></el-tab-pane>
            <el-tab-pane label="施工费信息" name="third" prop="appNgConstList"
              ><construcostInfo
                ref="construcostInfo"
                :construcostInfoDate="form.appNgConstList"
              ></construcostInfo
            ></el-tab-pane>
            <el-tab-pane label="事前审批表" name="fourth" prop="appxNgReq"
              ><approvalInfo
                ref="approvalInfo"
                :approvalInfoDate="form.appxNgReq"
              ></approvalInfo
            ></el-tab-pane>
          </el-tabs>


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
  import { getLeave } from '@/api/workflow/leave'
  import { getNgcAudit ,historyFromData,getCollectionAudit } from '@/api/activiti/historyFormdata'
  import basicInfo from "./nginfo/basicInfo.vue";
  import materialsInfo from "./nginfo/materialsInfo.vue";
  import construcostInfo from "./nginfo/construcostInfo.vue";
  import approvalInfo from "./nginfo/approvalInfo";
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
        // 表单参数
        form: {},
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
        this.form.instanceId = this.businessKey;
        getNgcAudit(this.businessKey).then(response => {
          this.form = response.data
          let proName_ = response.data;
      //项目类型列表
         this.getDicts("project_sort").then((response) => {
           for(let item of response.data) {
             if(item.dictValue==proName_.classification){
               this.form.classification = item.dictLabel;
             }
           }
         });

        })
      },
      historyFromData() {
        historyFromData(this.businessKey).then(response => {
          this.fromData = response.data
        })
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
