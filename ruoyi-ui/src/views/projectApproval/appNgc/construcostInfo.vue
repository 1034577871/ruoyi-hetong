<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th width="5%">序号</th>
            <th width="20%">施工项目</th>
            <th width="25%">内容说明</th>
            <th width="8%">单位</th>
            <th width="10%">所需工日</th>
            <th width="10%">工日单价</th>
            <th width="10%">金额</th>
            <th width="5%">操作</th>
            <th>
              <el-button
                size="mini"
                type="success"
                @click="loadMore()"
                >新增</el-button>
            </th>
          </tr>
          <tr style="height: 10px;"><td colspan="9"></td></tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td> {{ index+1 }} </td>
            <td>
              <el-autocomplete
                class="inline-input"
                v-model="value.projectName"
                :fetch-suggestions="querySearch"
                v-on:input="selectIndex(index)"
                :trigger-on-focus="false"
                @select="handleSelect"
                :name="'appMateList[' + index + '].projectName'"
              ></el-autocomplete>
              <!-- <el-input
                v-model="value.projectName"
                :name="'appMateList[' + index + '].projectName'"
              /> -->
            </td>
            <td>
              <el-input
                v-model="value.description"
                :name="'appMateList[' + index + '].description'"
              />
            </td>
            <td>
              <el-input
                v-model="value.projectUnit"
                :name="'appMateList[' + index + '].projectUnit'"
              />
            </td>
            <td>
              <el-input
                v-model="value.days"
                :name="'appMateList[' + index + '].days'"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
                v-model="value.daysPrice"
                :name="'appMateList[' + index + '].daysPrice'"
                v-on:input="changeNum(index)"
              />
            </td>
            <td>
              <el-input
              :disabled="true"
                v-model="value.daysAmount"
                :name="'appMateList[' + index + '].daysAmount'"
              />
            </td>
            <td>
              <el-button
                size="mini"
                type="warning"
                @click.prevent="removeTodo(index)"
                >删除</el-button
              >
            </td>
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
  </div>
</template>
<script>
  import request from '@/utils/request'
export default {
  components: {},
  props: {
    construcostInfoDate: {
      type: Array,
      default: () => {
        return []
      }
    },
    belongCompanyid: {},
  },
  data() {
    return {
      formData: null,
      dataList: this.construcostInfoDate,

      // 查询参数
      queryParams: {
        sgxName: null,
        companyId:null
      },
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
    selectIndex(index){
      this.index = index;
    },
    querySearch(queryString, cb) {
      this.queryParams.companyId = this.belongCompanyid;
      this.queryParams.sgxName = queryString;
      return request({
        url: '/code/info/lists',
        method: 'get',
        params: this.queryParams
      }).then(response => {
        var restaurants = response;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      });
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.sgxName.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    handleSelect(item) {
      this.dataList[this.index].materialName = item.name;
      this.dataList[this.index].description = item.sgContent;
      this.dataList[this.index].projectUnit = item.units;
      this.dataList[this.index].daysPrice = item.price;
    },
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
      let amount = this.dataList[index].daysAmount;
      this.cons = this.cons -parseFloat(amount==null?0:amount);


      let ids = this.dataList[index].id;
      if(ids!='' && ids!=null){
        return request({
          url: '/projectApprovalSubclass/AppNgConst/removeCon/'+ids,
          methods: 'get'
        }).then(response => {
          this.dataList.splice(index, 1);
        });
      }else{
        this.dataList.splice(index, 1);
      }
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
