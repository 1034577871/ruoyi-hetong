<template>
  <div ref="materialsInfo">
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
          <tr style="height: 10px;">
            <td colspan="9"></td>
          </tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td>
              {{ index+1 }}
            </td>
            <td>
              <el-input v-model="value.materialCode" readonly="readonly" :name="'appMateList[' + index + '].materialCode'" />
            </td>
            <td>
              <el-input v-model="value.materialName" readonly="readonly" :name="'appMateList[' + index + '].materialName'" />
            </td>
            <td>
              <el-input v-model="value.materialUnit" readonly="readonly" :name="'appMateList[' + index + '].materialUnit'" />
            </td>
            <td>
              <el-input v-model="value.materialPrice" readonly="readonly" :name="'appMateList[' + index + '].materialPrice'" v-on:input="changeNum(index)" />
            </td>
            <td>
              <el-input v-model="value.materialMount" readonly="readonly" :name="'appMateList[' + index + '].materialMount'" v-on:input="changeNum(index)" />
            </td>
            <td>
              <el-input readonly="readonly" v-model="value.materialAmount" :name="'appMateList[' + index + '].materialAmount'" />
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
              <el-input type="hidden" v-model="value.id" :name="'appMateList[' + index + '].id'" />
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
      materialsInfoDate: {
        type: Array,
        default: () => {
          return []
        }
      },

    },
    data() {
      return {
        formData: null,
        desr: '材料信息',
        dataList: this.materialsInfoDate
      };
    },
    watch: {
      materialsInfoDate: {
        handler(val) {
          this.dataList = val;
        },
        immediate: true,
      }
    },
    created() {

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
        let num = 0;
        this.dataList.map(res => {
          res.materialAmount ? num += parseInt(res.materialAmount) : ''
        })
        this.$bus.$emit('aaa', num)
      },
      /** 格式化金额 **/
      formatPrice(value) {
        let tempVal = parseFloat(value).toFixed(3);
        let realVal = tempVal.substring(0, tempVal.length - 1);
        return realVal;
      },
      //打印
      print_sp() {
        this.$print(this.$refs.materialsInfo)
      },
    }
  };
</script>
<style scoped lang="scss">
  .curr-table {
    text-align: center;
    width: 100%;
    vertical-align: middle;
    border-spacing: 0px;
    border-style: none;
    border-collapse: collapse;
  }

  .curr-table td {
    padding: 10px;
  }

  .curr-table th {
    font-weight: bold;
    background-color: #eff3fa;
    height: 40px;
    padding: 5px 10px;
  }
</style>
