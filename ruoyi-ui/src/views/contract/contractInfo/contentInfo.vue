<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
        <tr>
          <th width="16%">项目类型</th>
          <!-- <th width="15%">类型名称</th> -->
          <th width="8%">类型税率(%)</th>
          <th width="15%">含税金额(元)</th>
          <th width="15%">不含税额(元)</th>
          <th width="20%">备注</th>
          <th>
            <el-button size="mini" type="success" @click="loadMore()" style="margin: 0.625rem;">新增</el-button>
          </th>
        </tr>
        <tr v-for="(value, index) in dataList" :key="index">
          <td>
            <el-select v-model="value.contentType" placeholder="请选择项目类型" >
              <el-option v-for="dict in contentTypeList" :key="dict.dictValue" :label="dict.dictLabel"
                         :value="dict.dictValue" prop="contentType"/>
            </el-select>
          </td>
          <!-- <td><el-input v-model="value.contentType" :name="'appMateList[' + index + '].condition'" /></td> -->

          <!-- <td>
            <el-input v-model="value.contentName" prop="value.contentType"
                      :name="'appMateList[' + index + '].contentName'"/>
          </td> -->
          <td>
            <el-input v-model="value.contentTaxrate" :name="'appMateList[' + index + '].contentTaxrate'"
                      v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')"/>
          </td>
          <td>
            <el-input v-model="value.intaxAmount" :name="'appMateList[' + index + '].intaxAmount'"
                      v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')"/>
          </td>
          <td>
            <el-input v-model="value.extaxAmount" :disabled="true" :name="'appMateList[' + index + '].extaxAmount'"
                      oninput="value=value.replace(/[^0-9.]/g,'')"/>
          </td>
          <td>
            <el-input v-model="value.remark" :name="'appMateList[' + index + '].remark'"/>
          </td>

          <td>
            <el-button size="mini" type="warning" @click.prevent="removeTodo(index)">删除</el-button>
          </td>
          <td><el-input type="hidden" v-model="value.id" :name="'appMateList[' + index + '].id'" /></td>
        </tr>
        </tbody>
      </table>

    </el-form>

  </div>
</template>

<script>
import { delContractContent } from "@/api/contract/contractContent"
  export default {
    components: {},
    props: [],
    data() {
      return {
        formData: null,
        dataList: [],
        contentTypeList: [],
        contractCategory:'',
      };
    },
    computed: {},
    watch: {},
    created() {
      //项目类型
      // this.getDicts("content_type").then((response) => {
      //   this.contentTypeList = response.data;
      // });
      this.$bus.$on("aaa",value=>{
        if(value == 1){
          this.getDicts("content_type_income").then((response) => {
            this.contentTypeList = response.data;
          });
        }
        if(value == 2){
          this.getDicts("content_type_expend").then((response) => {
            this.contentTypeList = response.data;
          });
        }
        if(value == 3){
          this.getDicts("content_type_income").then((response) => {
            // this.contentTypeList = response.data;
            this.getDicts("content_type_expend").then((response1) => {
              this.contentTypeList = (response.data.concat(response1.data));
            });
          });


        }
        // this.contractCategory = value;

      })
    },
    mounted() {
    },
    methods: {
      /** 添加行 **/
      loadMore: function () {

        console.log(this.dataList)
        this.dataList.push({
          id: null,
          contentType: null,
          contentTaxrate: null,
          contentName: null,
          intaxAmount: null,
          extaxAmount: null,
          remark: null
        });

      },
      /** 删除行 **/
      removeTodo: function (index) {
        if(this.dataList[index].id != null){
          delContractContent(this.dataList[index].id).then(response=>{
          })
        }
        this.dataList.splice(index, 1);

      },

      //监听输入框输入值
      changeNum(index) {
        //税率
        let contentTaxrate = this.dataList[index].contentTaxrate;
        //含税金额
        let intaxAmount = this.dataList[index].intaxAmount

        if (contentTaxrate != null && intaxAmount != null) {
          //获得不含税金额
          this.dataList[index].extaxAmount = (intaxAmount / (contentTaxrate * 0.01 + 1)).toFixed(2);
        }

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

  .curr-table td, th {
    padding: 5px 10px;
  }

  .curr-table th {
    font-weight: bold;
    background-color: #eff3fa;
    height: 40px;
  }

</style>
