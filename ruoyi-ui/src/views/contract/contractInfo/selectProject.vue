<template>
  <div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      append-to-body
      width="40%"
      v-on:close="close">
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        v-show="showSearch"
        label-width="90px"
      >
        <el-form-item label="项目名称" prop="proName">
          <el-input
            v-model="queryParams.proName"
            placeholder="请输入项目名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="cyan"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
          >搜索
          </el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置
          </el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="projectList"
        tooltip-effect="dark"
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column label="项目编号">
          <template slot-scope="scope">{{ scope.row.reqNo }}</template>
        </el-table-column>
        <el-table-column label="项目名称">
          <template slot-scope="scope">{{ scope.row.proName }}</template>
        </el-table-column>
        <el-table-column label="分公司名称">
          <template slot-scope="scope">{{ scope.row.belongCompanyName }}{{ scope.row.belongCompanyname }}</template>
        </el-table-column>
        <el-table-column label="部门名称">
          <template slot-scope="scope">{{ scope.row.belongDeptName }}{{ scope.row.belongDeptname }}</template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="submithtForm(scope.row)"
            >选择
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-dialog>
  </div>


</template>


<script>
  import request from '@/utils/request';
  // import {listCompany,findAreaList} from '../../../api/dictionary/company.js';
  import {listAppNgc} from '../../../api/projectApproval/appNgc';
  import {listCollectProject} from '../../../api/projectApproval/collectProject';

  export default {
    name: 'seleccompanyList',
    props: {
      objId: {
        type: String,
        default: '',
      },
      companyType: {
        type: String,
        default: ''
      },
      stat: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        show: false,
        desc: '选择项目对话框',
        title: '',
        dialogVisible: true,
        projectList: [],
        selectItem: '',
        // companyType:{companyType:2},
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          proName: null,
          status: 2,
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 选择项目 */
      submithtForm(row) {
        this.$emit('ok', row.id, row.reqNo, row.proName, row.belongCompanyName)
        this.title = row.proName;
        this.dialogVisible = true;
        this.open = false;
      },

      close() {
        this.$emit('ok', "", "", "", "")
        this.dialogVisible = true;
      },
      /** 查询项目列表 */
      getList() {
        this.loading = true;
        //集客
        if (this.stat == "1") {
          listCollectProject(this.queryParams).then((response) => {
            this.projectList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        }
        //非集客
        if (this.stat == "2") {
          listAppNgc(this.queryParams).then((response) => {
            this.projectList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        }


        // findAreaList().then((response) => {
        //   this.field101Options = JSON.parse(response.data);
        // });
      },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        // this.form = {
        //   id: null,
        //   createBy: null,
        //   createTime: null,
        //   updateBy: null,
        //   updateTime: null,
        //   creditCode: null,
        //   companyName: null,
        //   provinceId: null,
        //   provinceName: null,
        //   cityId: null,
        //   cityName: null,
        //   address: null,
        //   linkMan: null,
        //   linkPhone: null,
        //   openingBank: null,
        //   openingAccount: null,
        //   creditCode:null,
        //   // companyType: null,
        //   delFlag: null,
        //   remark: null,
        // };
        this.resetForm("form");
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

    },

  }
</script>

<style>
</style>
