<template>
  <div>
      <el-form ref="elForm" :model="formData"  size="medium" label-width="100px">
       <table class="curr-table " >
        	<tbody>
        		<tr>
              <th width="15%">材料编码</th>
              <th width="25%">材料名称</th>
              <th width="10%">单位</th>
              <th width="10%">单价</th>
              <th width="10%">数量</th>
              <th width="15%">金额(元)</th>
              <th>操作</th>
             </tr>
             <tr>
              <td>
                <div v-for="item in formData.materialCode" class="mrg">
                 <el-input v-model="item.value" ></el-input>
               </div>
              </td>
              <td>
                <div v-for="item in formData.materialName" class="mrg">
                  <el-input v-model="item.value" ></el-input>
                </div>
              </td>
              <td>
                <div v-for="item in formData.materialUnit" class="mrg">
                  <el-input v-model="item.value" ></el-input>
                </div>
              </td>
              <td>
                <div v-for="item in formData.materialPrice" class="mrg">
                  <el-input v-model="item.value" ></el-input>
                </div>
              </td>
               <td>
                <div v-for="item in formData.materialMount" class="mrg">
                  <el-input v-model="item.value" ></el-input>
                </div>
              </td>
               <td>
                 <div v-for="item in formData.materialAmount" class="mrg">
                   <el-input v-model="item.value" ></el-input>
                 </div>
               </td>
               <td>
                  <el-button size="mini" type="success" @click="addRow()" style="margin: 10px;">新增</el-button>
                  <div  v-for="item in formData.btnlist" style="margin: 15px 10px;">
                    <el-button size="mini" type="warning" @click.prevent="delRow(item)">删除</el-button>
                  </div>
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
  props: [],
  data() {
    return {
      formData: {
        num:0,
        rate:0.2,
        total:0,
        materialCode: [{
          value: ''
        }],
        materialName: [{
          value: ''
        }],
        materialUnit: [{
          value: ''
        }],
        materialPrice: [{
          value: ''
        }],
        materialMount: [{
          value: ''
        }],
        materialAmount: [{
          value: ''
        }],
        btnlist: [],
      }
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    addRow() {
      this.formData.materialName.push({
        value: '',
        key: Date.now()
      });
      this.formData.materialCode.push({
        value: '',
        key: Date.now()
      });
      this.formData.materialUnit.push({
        value: '',
        key: Date.now()
      });
      this.formData.materialPrice.push({
        value: '',
        key: Date.now()
      });
      this.formData.materialMount.push({
        value: '',
        key: Date.now()
      });
      this.formData.materialAmount.push({
        value: '',
        key: Date.now()
      });
      this.formData.btnlist.push({
        value: '',
        key: Date.now()
      });
    },
    delRow(item){
      var index = this.formData.btnlist.indexOf(item)
      //alert(index);
      if (index !== -1) {
        this.formData.btnlist.splice(index, 1);
        index +=1;
        this.formData.materialCode.splice(index, 1);
        this.formData.materialName.splice(index, 1);
        this.formData.materialUnit.splice(index, 1);
        this.formData.materialPrice.splice(index, 1);
        this.formData.materialMount.splice(index, 1);
        this.formData.materialAmount.splice(index, 1);
      }
    }
  }
}

</script>
<style scoped  lang="scss">
  .curr-table{
    text-align: center;width: 100%;vertical-align:middle;
    border-spacing:0px; border-style:none; border-collapse:collapse;
  }
  .curr-table td,th{
      padding: 5px 10px;
  }
  .curr-table th{
      font-weight: bold;
      background-color: #eff3fa;
      height: 40px;
  }
  .mrg{
    margin: 10px;
  }
</style>
