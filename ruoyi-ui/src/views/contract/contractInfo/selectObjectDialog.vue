<template>
  <div>
    <!-- <el-input placeholder="请选择项目名称" :value="title" :readonly='true' @focus='dialogVisible = true'/> -->

    <el-dialog title="提示" :visible.sync="dialogVisible" append-to-body width="40%" v-on:close="quXiao">
      <el-table ref="multipleTable" :data="dataSource" tooltip-effect="dark" highlight-current-row style="width: 100%"
                @row-click="rowClick">
        <el-table-column label="项目编号">
          <template slot-scope="scope">
            {{ scope.row.reqNo }}
          </template>
        </el-table-column>
        <el-table-column label="项目名称">
          <template slot-scope="scope">
            {{ scope.row.proName }}
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="quXiao">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import request from '@/utils/request';
  import {listAppNgc} from '../../../api/projectApproval/appNgc';

  export default {
    name: 'selectObjectDialog',
    props: {
      objId: {
        type: String,
        default: ''
      },

      stat: {
        type: String,
        default: ''
      },

      dialogVisible: {
        type: Boolean,
        default: false
      }

    },
    data() {
      return {
        desc: '选择项目对话框',
        title: '',
        dialogVisible: false,
        dataSource: [],
        companyList: [],
        selectItem: '',
      };
    },
    created() {
      this.dataSource = []
      if (this.stat == "1") {
        this.getObjectList().then(res => {
          this.dataSource = res.rows;
        });
      } else {
        listAppNgc("").then(res => {
          this.dataSource = res.rows
        })
      }


    },
    methods: {
      getObjectList() {
        //查询项目列表
        return request({
          url: '/projectApproval/collectProject/list',
          methods: 'get'
        });
      },

      rowClick(e) {
        this.selectItem = e; //点击后 选中当前行的数据
      },
      confirm() {
        this.dialogVisible = false;
        this.$emit('ok', this.selectItem.id, this.selectItem.reqNo, this.selectItem.proName, this.dialogVisible);
        this.title = this.selectItem.proName;
      },

      quXiao() {
        this.dialogVisible = false
        this.$emit('ok', "", "", "", this.dialogVisible);
      }
    }
  };
</script>

<style></style>
