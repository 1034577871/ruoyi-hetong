<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th width="10%">序号</th>
            <th width="20%">材料编码</th>
            <th width="25%">材料名称</th>
            <th width="8%">单位</th>
            <th width="10%">单价</th>
            <th width="10%">数量</th>
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
          <tr style="height: 10px;">
            <td colspan="9"></td>
          </tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td>
              {{ index+1 }}
            </td>
            <td>
              <el-autocomplete
                class="inline-input"
                v-model="value.materialCode"
                :fetch-suggestions="querySearch"
                v-on:input="selectIndex(index)"
                :trigger-on-focus="false"
                @select="handleSelect"
                :name="'appMateList[' + index + '].materialCode'"
              ></el-autocomplete>
              <!-- <el-input v-model="value.materialCode" :name="'appMateList[' + index + '].materialCode'" /> -->
            </td>
            <td>
              <el-input v-model="value.materialName" :name="'appMateList[' + index + '].materialName'" />
            </td>
            <td>
              <el-input v-model="value.materialUnit" :name="'appMateList[' + index + '].materialUnit'" />
            </td>
            <td>
              <el-input v-model="value.materialPrice" :name="'appMateList[' + index + '].materialPrice'" v-on:input="changeNum(index)" />
            </td>
            <td>
              <el-input v-model="value.materialMount" :name="'appMateList[' + index + '].materialMount'" v-on:input="changeNum(index)" />
            </td>
            <td>
              <el-input :disabled="true" v-model="value.materialAmount" :name="'appMateList[' + index + '].materialAmount'" />
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
              <el-input type="hidden" v-model="value.id" :name="'appMateList[' + index + '].id'" />
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
      materialsInfoDate: {
        type: Array,
        default: () => {
          return []
        }
      },
      belongCompanyid:{},

    },
    data() {
      return {
        formData: null,
        desr: '材料信息',
        dataList: this.materialsInfoDate,
        // 查询参数
        queryParams: {
          code: null,
        },
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
      selectIndex(index){
        this.index = index;
      },
      querySearch(queryString, cb) {
        this.queryParams.code = queryString;
        return request({
          url: '/system/ctBuMaterielInfo/lists',
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
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        this.dataList[this.index].materialName = item.name;
        this.dataList[this.index].materialUnit = item.units;
        this.dataList[this.index].materialPrice = item.price;
      },
      getMate() {

        return request({
          url: '/system/ctBuMaterielInfo/lists',
          method: 'get',
          params: query
        }).then(response => {
          this.restaurants = (response)
        });
      },
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
        let amount = this.dataList[index].materialAmount;
        this.mates = this.mates -parseFloat(amount==null?0:amount);


        let ids = this.dataList[index].id;
        if(ids!='' && ids!=null){
          return request({
            url: '/projectApprovalSubclass/AppNgMaterial/removeMea/'+ids,
            methods: 'delete'
          }).then(response => {
            this.dataList.splice(index, 1);
          });
        } else {
          this.dataList.splice(index, 1);
        };
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
      }
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
