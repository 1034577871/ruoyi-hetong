<template>
  <div>
    <el-form ref="elForm" :model="formData" size="medium" label-width="100px" :rules="rules">
      <table class="curr-table ">
        <tbody>
        <tr>
          <!-- <th width="5%">序号</th> -->
          <th width="18%">预计收/付款时间</th>
          <th width="15%">收/付款条件说明</th>
          <th width="8%">收/付款比例(%)</th>
          <th width="15%">应收/付金额</th>
          <th width="10%">发票号</th>
          <th width="10%">发票含税金额</th>
          <th width="10%">发票代码</th>
          <th width="16%">发票时间</th>
          <th>
            <el-button size="mini" type="success" @click="loadMore()" style="margin: 10px;">新增</el-button>
          </th>
        </tr>
        <tr v-for="(value, index) in dataList" :key="index">
          <!-- <td>{{ index+1 }}</td> -->
          <td>
            <el-date-picker
              v-model="value.estimateTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              placeholder="请选择日期"
              clearable
              prop="estimateTime"
            ></el-date-picker>
            <!-- <el-input
                 v-model="value.estimateTime"
                 :name="'appMateList[' + index + '].estimateTime'"
               /> -->
          </td>
          <td>
            <el-input v-model="value.conditionInfo" :name="'appMateList[' + index + '].conditionInfo'"/>
          </td>
          <td>
            <el-input v-model="value.paymentRatio" :name="'appMateList[' + index + '].paymentRatio'"
                      v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')"/>
          </td>
          <td>
            <el-input v-model="value.estimateMoney" :name="'appMateList[' + index + '].estimateMoney'"
                      oninput="value=value.replace(/[^0-9.]/g,'')"/>
          </td>
          <td>
            <el-input v-model="value.invoiceId" :disabled="true" :name="'appMateList[' + index + '].invoiceNo'"/>
          </td>
          <td>
            <el-input v-model="value.invoiceMoney" :disabled="true" :name="'appMateList[' + index + '].invoiceMoney'"/>
          </td>
          <td>
            <el-input v-model="value.invoiceCode" :disabled="true" :name="'appMateList[' + index + '].invoiceCode'"/>
          </td>
          <td>
            <el-input
                 v-model="value.invoiceTime"
                 :disabled="true"
                 :name="'appMateList[' + index + '].invoiceTime'"
               />
            <!-- <el-date-picker
              v-model="value.invoiceTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              placeholder="请选择日期"
              clearable
            ></el-date-picker> -->
          </td>
          <td>
            <el-button size="mini" type="warning" @click.prevent="removeTodo(index)">删除</el-button>
          </td>
          <td>
            <el-input type="hidden" v-model="value.id" :name="'appMateList[' + index + '].id'"/>
          </td>
        </tr>
        </tbody>
      </table>
    </el-form>
  </div>
</template>
<script>
  import { delContractPayinfo } from "@/api/contract/contractPayinfo"
  export default {
    components: {},
    props: [],
    data() {
      return {
        formData: null,
        dataList: [],
        number: '',
        rules: {
          estimateTime: [
            {
              required: true,
              message: '请选择预计付款时间',
              trigger: 'blur'
            }
          ],
          taxrate: [
            {
              required: true,
              message: '请输入税率',
              trigger: 'blur'
            }
          ],
          serviceTerm: [
            {
              required: true,
              message: '请输入服务期限',
              trigger: 'blur'
            }
          ],
          contractServeStart: [
            {
              required: true,
              message: '请选择合同服务开始日期',
              trigger: 'blur'
            }
          ]
        },
      };
    },
    computed: {},
    watch: {},
    created() {
    },
    mounted() {
    },
    methods: {
      /** 添加行 **/
      loadMore: function () {
        this.dataList.push({
          id: null,
          estimateTime: null,
          conditionInfo: null,
          paymentRatio: null,
          estimateMoney: null,
          actualMoney: null,
          actualTime: null,
          invoiceNo: null,
          invoiceMoney: null,
          invoiceCode: null,
          invoiceTime: null
        });
      },
      /** 删除行 **/
      removeTodo: function (index) {
        if(this.dataList[index].id != null){
          delContractPayinfo(this.dataList[index].id).then(response=>{
          })
        }
        this.dataList.splice(index, 1);
      },
      //根据第一页传入的合同标的额,计算应付金额
      changeNum(index) {
        this.dataList[index].estimateMoney = (this.dataList[index].paymentRatio / 100 * this.number).toFixed(2);
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

  .curr-table td, th {
    padding: 5px 10px;
  }

  .curr-table th {
    font-weight: bold;
    background-color: #eff3fa;
    height: 40px;
  }

</style>
