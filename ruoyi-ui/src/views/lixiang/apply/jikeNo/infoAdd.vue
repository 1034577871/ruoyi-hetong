<template>
  <div style="padding: 0 15px;">
    <h2 style="text-align: center;color:#3388ee;">非集客项目事前审批表</h2>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <table class="table table-bordered" style="text-align: center;width: 100%;vertical-align:middle;">
        	<tbody>
        		<tr>
        			<td width="20%"><span class="labelX">申请公司</span></td>
        			<td width="30%">
        				{{formData.applycompany}}
        			</td>
        			<td width="20%"><span class="labelX">审批编号</span></td>
        			<td  width="30%">
        				{{formData.approvalno}}
        			</td>
        		</tr>
        		<tr>
        			<td ><span class="labelX">项目名称</span></td>
        			<td colspan="3" style="text-align: left;">
        				<el-input v-model="formData.projectName" placeholder="请输入项目名称" style="border: none;"></el-input>
        			</td>
        		</tr>
        		<tr>
        			<td ><span class="labelX">项目类别</span></td>
        			<td >
               <el-select v-model="formData.proType" placeholder="请选择项目类型">
                 <el-option
                   v-for="dict in proTypeNameList"
                   :key="dict.id"
                   :label="dict.typeName"
                   :value="dict.id"
                 />
               </el-select>
        			</td>
        			<td ><span class="labelX">申请日期</span></td>
        			<td > {{formData.applydate}}</td>
        		</tr>
            <tr>
            	<td ><span class="labelX">项目分类</span></td>
            	<td colspan="3" style="text-align: left;">
                <el-radio-group v-model="formData.classification" size="medium">
                  <el-radio v-for="(item, index) in classificationOptions" :key="index" :label="item.value"
                    :disabled="item.disabled">{{item.label}}</el-radio>
                </el-radio-group>
            	</td>
            </tr>
				<tr>
					<td ><span class="labelX">合同金额</span></td>
					<td >
						<el-input v-model="formData.contractAmountTotal"></el-input>
					</td>
					<td ><span class="labelX">税率</span></td>
					<td > {{formData.taxrate}}</td>
				</tr>
        <tr>
        	<td ><span class="labelX">不含税合同金额</span></td>
        	<td >
        		<el-input v-model="formData.contractAmountTax"></el-input>
        	</td>
        	<td ><span class="labelX">税额</span></td>
        	<td > {{formData.taxrateTotal}}</td>
        </tr>
        <tr>
        	<td ><span class="labelX">申请原因</span></td>
        	<td colspan="3" style="text-align: left;">
            <el-input type="textarea" v-model="formData.reqReason"></el-input>
        	</td>
        </tr>
        <tr>
        	<td ><span class="labelX">说明</span></td>
        	<td colspan="3" style="text-align: left;">
            <el-input type="textarea" v-model="formData.explain"></el-input>
        	</td>
        </tr>
        	</tbody>
        </table>

      </el-form>
    </el-row>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        applycompany: "山东广电网络有限公司济南分公司",
        approvalno:"JN202008002",
        applydate:"2020-12-09",
        projectName:"",
        classification:undefined,
      },
      classificationOptions: [{
      	"label": "收入",
      	"value": 1
      }, {
      	"label": "支出",
      	"value": 2
      }],
      rules: {
        field102: [{
          required: true,
          message: '请选择日期选择',
          trigger: 'change'
        }],
        field101: [{
          required: true,
          message: '请选择日期选择',
          trigger: 'change'
        }],
      },
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
      })
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style scoped  lang="scss">
  .el-dialog__body{
    padding: 0 20px;
  }
	.table-bordered {
		border: 1px solid #ddd;
		border-collapse: collapse;
		border-spacing: 0;
	}

	.table-bordered td {
		border: 1px solid #ddd;
	}

	.labelX {
		display: inline-block;
		max-width: 100%;
		margin: 5px;
		font-weight: bold;
		font-size: 14px;
	}

</style>
