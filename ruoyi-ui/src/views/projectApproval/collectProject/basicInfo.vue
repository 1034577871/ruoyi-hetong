<template>
  <div>
    <el-form
      ref="elForm"
      :model="formData"
      :rules="rules"
      size="medium"
      label-width="100px"
    >
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="项目类别" prop="projectTypeId"
        >
            <el-select
              v-model="formData.projectTypeId"
              placeholder="请选择"
              style="width: 100%;"
              @change="$forceUpdate()"
            >
              <el-option
                v-for="dict in projectTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="公司负责人">
            <el-input v-model="formData.companyCharge"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="部门负责人">
            <el-input v-model="formData.departCharge"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目负责人">
            <el-input v-model="formData.proCharge"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="建筑物数" >
            <el-input-number  :style="{ width: '100%' }"
              v-model="formData.buildingsNumber"
              :min="0"
              :step="1"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="涉及建筑" >
            <el-input-number  :style="{ width: '100%' }"
              v-model="formData.buildingsInvolvedNumber"
              :min="0"
              :step="1"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="数字TV数" >
            <el-input-number  :style="{ width: '100%' }"
              v-model="formData.digitaltvNumber"
              :min="0"
              :step="1"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="宽带数" >
            <el-input-number  :style="{ width: '100%' }"
              v-model="formData.broadbandNumber"
              :min="0"
              :step="1"
            ></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="材料预算" prop="budgetMaterial">
            <el-input
              v-model="formData.budgetMaterial"
              v-on:input="changeNum()"
              :readOnly="true"
            >  </el-input>

          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="人工预算" prop="budgetLabor">
            <el-input
              v-model="formData.budgetLabor"
              v-on:input="changeNum()"
              :readOnly="true"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="预算总额">
            <el-input
              v-model="formData.budgetTotal"
              :disabled="true"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="设计时间">
            <el-date-picker
              v-model="formData.designTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              placeholder="请选择日期"
              clearable
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入场时间">
            <el-date-picker
              v-model="formData.dmissionTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              placeholder="请选择日期"
              clearable
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="完工时间">
            <el-date-picker
              v-model="formData.completionTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              placeholder="请选择日期"
              clearable
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="24">
          <el-form-item label="项目概况">
            <el-input type="textarea" v-model="formData.remark" maxlength="1000"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>
<script>
import {
  findProjectType,
  findBranchOffice,
  delFileById,
  findFileListByPid
} from "@/api/utils";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {
    Treeselect
  },
  props: {
    baseInfoDate: {},
    hiMsg:String
  },
  data() {
    //自定义下拉框校验函数
    const isSelect = (rule, value, callback) => {
      if (value == null || value=='') { //如果值是 0，提示用户选择正确的选项
        return callback(new Error("请正确选择一级标题"));
      } else {
        callback();
      }
    };

    return {
      formData: this.baseInfoDate,

      proTypeNameList: [],
      belongCompanyNameList: [],
      deptOptions: [],
      branchInvolvedOptions: [],
      projectTypeOptions: [], // 表单校验
      rules: {
        projectTypeId: [{ required: true, message: '请选择项目类别',  trigger: ['blur','change']   }],
        // budgetMaterial: [
        //   {
        //     required: true,
        //     message: "请输入材料预算",
        //     trigger: "change"
        //   }
        //
        // ],
        // budgetLabor: [
        //   {
        //     required: true,
        //     message: "请输入人工预算",
        //     trigger: "change"
        //   }
        //
        // ]
      }
    };
  },
  created() {
    this.getTreeselect();
    findProjectType().then(response => {
      this.proTypeNameList = response.data;
    });
    findBranchOffice().then(response => {
      this.belongCompanyNameList = response.data;
    });
    this.getDicts("base_project_type").then(response => {
      this.projectTypeOptions = response.data;
    });
    if (!this.formData) {
      this.formData = {
        branIdStr: [],
        companyCharge: "",
        departCharge: "",
        proCharge: "",
        projectTypeId: "",
        buildingsNumber: 0,
        buildingsInvolvedNumber: 0,
        digitaltvNumber: 0,
        broadbandNumber: 0,
        budgetMaterial: 0,
        budgetLabor: 0,
        budgetTotal: 0,
        designTime: ""
      };
    } else {
      this.formData.projectTypeId = this.formData.projectTypeId + "";
    }


  },
  methods: {
    validateForm() {
      let flag = false
      this.$refs['elForm'].validate((valid) => {
        flag = valid
      })
      return flag
    },
    submitForm() {
      this.$refs["elForm"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
    },
    /** 计算预算金额 **/
    changeNum() {
      if (this.formData.budgetMaterial) {
        if (this.formData.budgetLabor) {
          this.formData.budgetTotal =
            parseInt(this.formData.budgetMaterial) +
            parseInt(this.formData.budgetLabor);
        } else {
          this.formData.budgetTotal = parseInt(this.formData.budgetMaterial);
        }
      } else if (this.formData.budgetLabor) {
        this.formData.budgetTotal = parseInt(this.formData.budgetLabor);
      }
    }
  }
};
</script>
<style scoped  lang="scss">
.el-textarea .el-textarea__inner {
  resize: none;
}
</style>
