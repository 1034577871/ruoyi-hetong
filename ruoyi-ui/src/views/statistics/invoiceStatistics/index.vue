<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="90px">
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="开票日期" label-width="100px">
            <el-date-picker
              v-model="invoiceDate"
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


    <el-table border v-loading="loading" :data="invoiceStatistics" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="公司ID" align="center" prop="companyId" v-if="show" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="增值税专票数量" align="center" prop="specialInvoiceCount" />
      <el-table-column label="增值税专票总额" align="center" prop="specialInvoiceAmountSum" />
      <el-table-column label="增值税普票数量" align="center" prop="plainInvoiceCount" />
      <el-table-column label="增值税普票总额" align="center" prop="plainInvoiceAmountSum" />
    </el-table>

  </div>
</template>

<script>
import { getSlList,listFinancial, getFinancial, delFinancial, addFinancial, updateFinancial, exportFinancial, invoiceStatistics } from "@/api/collection/financial";
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
      // 显示搜索条件
      showSearch: true,
      // 统计数据
      invoiceStatistics: [],
      // 日期范围
      invoiceDate: [],
      startTime: "",
      // 查询参数
      queryParams: {
      },

      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();

  },
  methods: {
    getList() {
      this.loading = true;
      invoiceStatistics(this.addDateRange(this.queryParams,this.invoiceDate)).then(response => {
        this.invoiceStatistics = response;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery(query) {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm');
      this.invoiceDate = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
  }
};
</script>
