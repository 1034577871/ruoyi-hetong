<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
       <el-col :span="24">
         <el-form-item label="施工项目" >
         	<el-input v-model="formData.projectName" ></el-input>
         </el-form-item>
       </el-col>
	   <el-col :span="24">
	     <el-form-item label="内容说明" >
	     	<el-input v-model="formData.description" ></el-input>
	     </el-form-item>
	   </el-col>
	   <el-col :span="12">
	     <el-form-item label="单位" >
	     	<el-input v-model="formData.projectUnit" ></el-input>
	     </el-form-item>
	   </el-col>
	   <el-col :span="12">
	     <el-form-item label="所需工日" >
	     	<el-input v-model="formData.days" ></el-input>
	     </el-form-item>
	   </el-col>
	   <el-col :span="12">
	     <el-form-item label="工日单价" >
	     	<el-input v-model="formData.daysPrice" ></el-input>
	     </el-form-item>
	   </el-col>
     <el-col :span="12">
       <el-form-item label="金额" >
       	<el-input v-model="formData.daysAmount" :disabled="true"></el-input>
       </el-form-item>
     </el-col>
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
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
        projectName:"",
        description:"",
        projectUnit:"",
        days:0,
        daysPrice:0,
        daysAmount:0
      },
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
<style>
</style>
