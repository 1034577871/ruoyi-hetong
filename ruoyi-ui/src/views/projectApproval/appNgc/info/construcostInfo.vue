<template>
  <div ref="construcostInfo">
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th width="10%">序号</th>
            <th width="10%">施工项目</th>
            <th width="25%">内容说明</th>
            <th width="10%">单位</th>
            <th width="10%">所需工日</th>
            <th width="10%">工日单价</th>
            <th width="15%">金额</th>
            <!-- <th width="5%">操作</th>
            <th>
              <el-button
                size="mini"
                type="success"
                @click="loadMore()"
                >新增</el-button>
            </th> -->
          </tr>
          <tr style="height: 10px;"><td colspan="9"></td></tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td> {{ index+1 }} </td>
            <td>
              <el-input
                v-model="value.projectName"
                :name="'appMateList[' + index + '].projectName'"
                readonly="readonly"
              />
            </td>
            <td>
              <el-input
                v-model="value.description"
                :name="'appMateList[' + index + '].description'"
                readonly="readonly"
              />
            </td>
            <td>
              <el-input
                v-model="value.projectUnit"
                :name="'appMateList[' + index + '].projectUnit'"
                readonly="readonly"
              />
            </td>
            <td>
              <el-input
                v-model="value.days"
                :name="'appMateList[' + index + '].days'"
                readonly="readonly"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
                v-model="value.daysPrice"
                :name="'appMateList[' + index + '].daysPrice'"
                readonly="readonly"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
                v-model="value.daysAmount"
                readonly="readonly"
                :name="'appMateList[' + index + '].daysAmount'"
              />
            </td>
            <!-- <td>
              <el-button
                size="mini"
                type="warning"
                @click.prevent="removeTodo(index)"
                >删除</el-button
              >
            </td> -->
            <td>
              <el-input
                type="hidden"
                v-model="value.id"
                :name="'appMateList[' + index + '].id'"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </el-form>
    <div slot="footer" class="no-print">
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="print_sp">打 印</el-button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    construcostInfoDate: {
      type: Array,
    }
  },
  data() {
    return {
      formData: null,
      dataList: this.construcostInfoDate
    };
  },
  watch:{
    construcostInfoDate:{
      handler(val){
        this.dataList = val;
      },
      immediate:true,
    }
  },
  created() {

  },
  methods: {
    /** 添加行 **/
    loadMore: function() {
      this.dataList.push({
        id: null,
        projectName: null,
        description: null,
        projectUnit: null,
        days: null,
        daysPrice: null,
        daysAmount: null
      });
    },
    /** 删除行 **/
    removeTodo: function(index) {
      this.dataList.splice(index, 1);
    },
    /** 计算金额,格式化信息 **/
    changeNum(indexNum) {
      let days = this.dataList[indexNum].days;
      let daysPrice = this.dataList[indexNum].daysPrice;
      if (days && daysPrice) {
        this.dataList[indexNum].daysAmount = this.formatPrice(
          days * daysPrice
        );
      }
      let num = 0;
      this.dataList.map(res=>{
       res.daysAmount? num += parseInt(res.daysAmount):''
      })
      this.$bus.$emit('bbb',num)
    },
    /** 格式化金额 **/
    formatPrice(value) {
      let tempVal = parseFloat(value).toFixed(3);
      let realVal = tempVal.substring(0, tempVal.length - 1);
      return realVal;
    },
    //打印
    print_sp() {
      this.$print(this.$refs.construcostInfo)
    },
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
