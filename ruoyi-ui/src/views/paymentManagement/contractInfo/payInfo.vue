<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th><el-checkbox width="5%" v-model="checkedAll" v-on:change="checkedAllFun"></el-checkbox></th>
            <th width="16%">预计付款时间</th>
            <th width="16%">付款条件说明</th>
            <th width="10%">付款比例(%)</th>
            <th width="10%">应付金额</th>
            <th width="10%">发票号</th>
            <th width="10%">发票含税金额</th>
            <th width="10%">发票代码</th>
            <th width="16%">发票时间</th>
          </tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <!-- <td>{{ index }}</td> -->

            <td><el-checkbox v-model="value.isCheck" v-on:change="checkedItem(index)"/></td>

            <td>
              <el-date-picker
                v-model="value.estimateTime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :style="{ width: '100%' }"
                placeholder="请选择日期"
                clearable
              ></el-date-picker>
            </td>
            <td><el-input v-model="value.conditionInfo" :name="'appMateList[' + index + '].conditionInfo'" /></td>
            <td>
              <el-input v-model="value.paymentRatio" :name="'appMateList[' + index + '].paymentRatio'" v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')" />
            </td>
            <td><el-input v-model="value.estimateMoney" :name="'appMateList[' + index + '].estimateMoney'" oninput="value=value.replace(/[^0-9.]/g,'')" /></td>
            <td><el-input v-model="value.invoiceId" :name="'appMateList[' + index + '].invoiceId'" /></td>
            <td><el-input v-model="value.invoiceMoney" :name="'appMateList[' + index + '].invoiceMoney'" /></td>
            <td><el-input v-model="value.invoiceCode" :name="'appMateList[' + index + '].invoiceCode'" /></td>
            <td>
              <!-- <el-input
                   v-model="value.invoiceTime"
                   :name="'appMateList[' + index + '].invoiceTime'"
                 /> -->
              <el-date-picker
                v-model="value.invoiceTime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :style="{ width: '100%' }"
                placeholder="请选择日期"
                clearable
              ></el-date-picker>
            </td>
            <td><el-input type="hidden" v-model="value.id" :name="'appMateList[' + index + '].id'" /></td>
          </tr>

        </tbody>

      </table>
    </el-form>
    <el-button v-if="isAdd" type="primary" @click="dialogVisible = true">添加信息</el-button>
    <addDialog v-if="dialogVisible" ref="addDialog" @ok="objDialogOk" v-bind:dialogVisible="dialogVisible"/>
  </div>
</template>
<script>
import addDialog from './addDialog.vue'
export default {
  components: {
    addDialog
  },
  props: [],
  data() {
    return {
      dialogVisible:false,
      isAdd:false,
      checkedAll:false,
      formData: null,
      dataList: [],
      number: ''
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    /** 添加行 **/
    loadMore: function() {
      this.dataList.push({
        id: null,
        estimateTime: null,
        conditionInfo: null,
        paymentRatio: null,
        estimateMoney: null,
        actualMoney: null,
        actualTime: null,
        invoiceId: null,
        invoiceMoney: null,
        invoiceCode: null,
        invoiceTime: null,
        isCheck: false
      });
    },
    /** 删除行 **/
    removeTodo: function(index) {
      this.dataList.splice(index, 1);
    },

    //算账
    changeNum(index) {
      this.dataList[index].estimateMoney = ((this.dataList[index].paymentRatio / 100) * this.number).toFixed(2);
    },

    //全选监听
    checkedAllFun(){
      console.log("全选"+this.checkedAll);
      for (var i = 0; i < this.dataList.length; i++) {
        this.dataList[i].isCheck = this.checkedAll
      }
      this.isAdd = this.checkedAll
    },

    //监听item中的复选框按钮
    checkedItem(index){

      let is = true
      let number = 0
      for (var i = 0; i < this.dataList.length; i++) {
        if(this.dataList[i].isCheck){
          this.isAdd = true
        }else{
          is = false
          number++
        }
      }
      this.checkedAll = is
      if(number == this.dataList.length){
        this.isAdd = false
      }
    },

    //获取返回值
    objDialogOk(json,dialogVisible){
      if(json != 1){
        for (var i = 0; i < this.dataList.length; i++) {
          if(this.dataList[i].isCheck){

            this.dataList[i].invoiceId = json.invoiceId
            this.dataList[i].invoiceMoney = json.invoiceMoney
            this.dataList[i].invoiceCode = json.invoiceCode
            this.dataList[i].invoiceTime = json.invoiceTime

          }
        }
      }
      this.dialogVisible = dialogVisible
    }

  }
};
</script>
<style scoped  lang="scss">
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
