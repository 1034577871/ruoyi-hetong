<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th width="10%">序号</th>
            <th width="10%">材料编码</th>
            <th width="25%">材料名称</th>
            <th width="10%">单位</th>
            <th width="10%">单价</th>
            <th width="10%">数量</th>
            <th width="15%">金额(元)</th>
           
          </tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td>
              {{ index+1 }}
            </td>
            <td>
              <el-input
                v-model="value.materialCode"
                :name="'appMateList[' + index + '].materialCode'"
              />
            </td>
            <td>
              <el-input
                v-model="value.materialName"
                :name="'appMateList[' + index + '].materialName'"
              />
            </td>
            <td>
              <el-input
                v-model="value.materialUnit"
                :name="'appMateList[' + index + '].materialUnit'"
              />
            </td>
            <td>
              <el-input
                v-model="value.materialPrice"
                :name="'appMateList[' + index + '].materialPrice'"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
                v-model="value.materialMount"
                :name="'appMateList[' + index + '].materialMount'"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
                v-model="value.materialAmount"
                :name="'appMateList[' + index + '].materialAmount'"
              />
            </td>
            
          </tr>
        </tbody>
      </table>
    </el-form>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    materialsInfoDate: {}
  },
  data() {
    return {
      formData: null,
      dataList: []
    };
  },
  created() {
    if (!this.materialsInfoDate) {
      this.dataList.push({
        id: null,
        materialCode: null,
        materialName: null,
        materialUnit: null,
        materialPrice: null,
        materialMount: null,
        materialAmount: null
      });
    }
  },
  methods: {
    /** 添加行 **/
    loadMore: function() {
      this.dataList.push({
        id: null,
        materialCode: null,
        materialName: null,
        materialUnit: null,
        materialPrice: null,
        materialMount: null,
        materialAmount: null
      });
    },
    /** 删除行 **/
    removeTodo: function(index) {
      this.dataList.splice(index, 1);
    },
    /** 计算金额,格式化信息 **/
    changeNum(indexNum) {
      let price = this.dataList[indexNum].materialPrice;
      let mount = this.dataList[indexNum].materialMount;
      if (price && mount) {
        this.dataList[indexNum].materialAmount = this.formatPrice(
          price * mount
        );
      }
    },
    /** 格式化金额 **/
    formatPrice(value) {
      let tempVal = parseFloat(value).toFixed(3);
      let realVal = tempVal.substring(0, tempVal.length - 1);
      return realVal;
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
