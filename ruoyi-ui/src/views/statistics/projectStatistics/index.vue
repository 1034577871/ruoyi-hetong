<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="录入日期" label-width="100px">
            <el-date-picker
              v-model="createTime"
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
        <el-col :span="6" >
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table border v-loading="loading" :data="projectCount" @selection-change="handleSelectionChange" @cell-click="cellClick">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="公司ID" align="center" prop="companyId" v-if="show" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="集客数量" align="center" prop="countApprovalNg" />
      <el-table-column label="非集客数量" align="center" prop="countAppNgc" />
      <el-table-column label="合同签订数量" align="center" prop="countContract" />
      <el-table-column label="进行中合同数量" align="center" prop="countContractUnderway" />
      <el-table-column label="未启动合同数量" align="center" prop="countContractUnStart" />
      <el-table-column label="完成合同数量" align="center" prop="countContractAccomplish" />
    </el-table>


    <el-dialog  title="集客统计" :visible.sync="jkDialog"  width="60%" :before-close="handleClose">
        <el-table :data="jkTableData"  ref="itsmDataTable" border >
          <el-table-column label="项目ID" align="center" prop="id" v-if="show" />
          <el-table-column label="项目编号" align="center" prop="reqNo" />
          <el-table-column label="项目名称" align="center" prop="proName" />
          <el-table-column label="预计投资金额" align="center" prop="expectAmount" />
        </el-table>
        <pagination v-show="total > 0"  :total="total"  :page.sync="projectQueryParams.pageNum"  :limit.sync="projectQueryParams.pageSize"  @pagination="getjkList"  />
        <span slot="footer" class="dialog-footer">
          <el-button @click="jkDialog = false">关 闭</el-button>
          <!-- <el-button type="primary" @click="fjkDialog = false">确 定</el-button> -->
        </span>
    </el-dialog>
    <el-dialog  title="非集客统计" :visible.sync="fjkDialog"  width="60%" :before-close="handleClose">
        <el-table  :data="fjkTableData"  ref="itsmDataTable" border  >
          <el-table-column label="项目ID" align="center" prop="id" v-if="show" />
          <el-table-column label="项目编号" align="center" prop="reqNo" />
          <el-table-column label="项目名称" align="center" prop="proName" />
          <el-table-column label="项目类型" align="center" prop="proTypeName" />
          <el-table-column label="合同金额" align="center" prop="contractAmountTotal" />
        </el-table>
        <pagination  v-show="total > 0" :total="total"  :page.sync="projectQueryParams.pageNum" :limit.sync="projectQueryParams.pageSize" @pagination="getfjkList"  />
        <span slot="footer" class="dialog-footer">
          <el-button @click="fjkDialog = false">关 闭</el-button>
          <!-- <el-button type="primary" @click="fjkDialog = false">确 定</el-button> -->
        </span>
    </el-dialog>
    <el-dialog  title="合同统计信息" :visible.sync="htDialog"  width="60%" :before-close="handleClose">
        <el-table  :data="htTableData"  ref="itsmDataTable" border  >
          <el-table-column label="合同ID" align="center" prop="id" v-if="show" />
          <el-table-column label="项目编号" align="center" prop="projectNo" />
          <el-table-column label="项目名称" align="center" prop="projectName" />
          <el-table-column label="合同编号" align="center" prop="contractNo" />
          <el-table-column label="合同名称" align="center" prop="contractName" />
          <el-table-column label="合同类别" align="center" prop="contractCategoryName" />
          <el-table-column label="对方单位" align="center" prop="oppositeName" />
          <el-table-column label="合同金额" align="center" prop="totalMoney" />
        </el-table>
        <pagination  v-show="total > 0" :total="total"  :page.sync="contractQueryParams.pageNum" :limit.sync="contractQueryParams.pageSize" @pagination="gethtList"  />
        <span slot="footer" class="dialog-footer">
          <el-button @click="htDialog = false">关 闭</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import { listContract, getContract, delContract, addContract, updateContract, exportContract, projectCount, contractStatisticsList } from '@/api/contract/contractInfo';
import { listAppNgc,  getAppNgc, delAppNgc, addAppNgc, updateAppNgc, exportAppNgc } from "@/api/projectApproval/appNgc";
import { listCollectProject, getCollectProject, delCollectProject,  addCollectProject, updateCollectProject,  addCollectProject_, updateCollectProject_,  exportCollectProject, getFxCollectProject, selectCtBuApprovalNgReqById, getUserInfo,  exitFlow,  copyReqInfo,  listCollectProjects } from "@/api/projectApproval/collectProject";
export default {
  name: 'Warning',
  data() {
    return {
      // 遮罩层
      loading: true,
      show: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 统计数据
      projectCount: [],
      form: {},

      // 查询参数
      queryParams: {
      },
      // 日期范围
      createTime: [],

      // 总条数
      total: 0,
      // 项目查询参数
      projectQueryParams: {
        pageNum: 1,
        pageSize: 10,
        belongCompanyid: null,
        status: 2,
      },
      // 合同查询参数
      contractQueryParams: {
        pageNum: 1,
        pageSize: 10,
        manageCompany: null,
        type:null,
      },
      // 表单校验
      rules: {},
      //各子窗口显示变量
      jkDialog:false,
      fjkDialog:false,
      htDialog:false,
      //各子窗口表格数据
      jkTableData: [],
      fjkTableData:[],
      htTableData:[],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      projectCount(this.addDateRange(this.queryParams,this.createTime)).then(response => {
        this.projectCount = response;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery(query) {
      console.log(this.invoiceDate);
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm');
      this.createTime = [];
      this.handleQuery();
    },

    getjkList() {
      this.jkDialog = true;
      listCollectProject(this.projectQueryParams).then(response => {
        this.jkTableData = response.rows;
        this.total= response.total;
        this.loading = false;
      });
    },
    getfjkList() {
      this.fjkDialog = true;
      listAppNgc(this.projectQueryParams).then(response => {
        this.fjkTableData = response.rows;
        this.total= response.total;
        this.loading = false;
      });
    },
    gethtList() {
      this.htDialog = true;
      contractStatisticsList(this.contractQueryParams).then(response => {
        this.htTableData = response.rows;
        this.total= response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    //单元格点击事件
    cellClick(row, column, event) {
      if(column.label==='集客数量'){
        this.jkDialog = true;
        this.projectQueryParams.belongCompanyid = row.companyId;
        // listCollectProject(this.projectQueryParams).then(response => {
        //   console.log(response)
        //   this.jkTableData = response.rows;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.getjkList();
      }
      else if(column.label==='非集客数量'){
        this.fjkDialog = true;
        this.projectQueryParams.belongCompanyid = row.companyId;
        // listAppNgc(this.projectQueryParams).then(response => {
        //   this.fjkTableData = response.data;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.getfjkList();
      }
      else if(column.label==='合同签订数量'){
        this.htDialog = true;
        this.contractQueryParams.manageCompany = row.companyId;
        this.contractQueryParams.type = null;
        this.contractQueryParams.pageNum = 1;
        // contractStatisticsList(this.contractQueryParams).then(response => {
        //   this.htTableData = response.rows;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.gethtList();
      }
      else if(column.label==='进行中合同数量'){
        this.htDialog = true;
        console.log(row)
        this.contractQueryParams.manageCompany = row.companyId;
        this.contractQueryParams.type = 1;
        this.contractQueryParams.pageNum = 1;
        // contractStatisticsList(this.contractQueryParams).then(response => {
        //   this.htTableData = response.rows;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.gethtList();
      }
      else if(column.label==='未启动合同数量'){
        this.htDialog = true;
        this.contractQueryParams.manageCompany = row.companyId;
        this.contractQueryParams.type = 2;
        this.contractQueryParams.pageNum = 1;
        // contractStatisticsList(this.contractQueryParams).then(response => {
        //   this.htTableData = response.rows;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.gethtList();
      }
      else if(column.label==='完成合同数量'){
        this.htDialog = true;
        this.contractQueryParams.manageCompany = row.companyId;
        this.contractQueryParams.type = 3;
        this.contractQueryParams.pageNum = 1;
        // contractStatisticsList(this.contractQueryParams).then(response => {
        //   this.htTableData = response.rows;
        //   this.total= response.total;
        //   this.loading = false;
        // });
        this.gethtList();
      }
    },
    handleClose(){
      this.jkDialog = false;
      this.fjkDialog = false;
      this.htDialog = false;
    }
  }
};
</script>
