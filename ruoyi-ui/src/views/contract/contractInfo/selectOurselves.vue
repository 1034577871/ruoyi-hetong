<template>
  <div>
    <!-- <el-input placeholder="请选择对方单位" :value="title" :readonly='true' @focus='dialogVisible = true'/> -->
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      append-to-body
      width="40%"
      v-on:close="close">
      <el-table
        ref="multipleTable"
        :data="companyList"
        tooltip-effect="dark"
        highlight-current-row
        style="width: 100%"
        @row-click="rowClick">
        <el-table-column
          label="单位名称">
          <template slot-scope="scope">{{ scope.row.companyName }}</template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>


</template>


<script>
  import request from '@/utils/request'
  import {listCompany} from '../../../api/dictionary/company.js'

  export default {
    name: 'selectOurselves',
    props: {
      objId: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        show: false,
        desc: '选择项目对话框',
        title: '',
        dialogVisible: true,
        companyList: [],
        selectItem: '',
        companyType: {companyType: 1}
      }
    },
    created() {
      listCompany(this.companyType).then(response => {
        this.companyList = response.rows;
      });

    },
    methods: {
      rowClick(e) {
        this.selectItem = e;//点击后 选中当前行的数据
      },
      confirm() {
        this.$emit('ok', this.selectItem.id, this.selectItem.companyName, this.selectItem.openingBank, this.selectItem.openingAccount, this.selectItem.linkMan, this.selectItem.linkPhone, this.selectItem.address)
        this.title = this.selectItem.companyName;
        this.dialogVisible = true;
      },

      close() {
        this.$emit('ok', "", "", "", "", "", "", "")
        this.dialogVisible = true;
      }

    },

  }
</script>

<style>
</style>
