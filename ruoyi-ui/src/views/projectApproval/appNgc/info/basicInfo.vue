<template>
  <div ref="basicInfo">
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <!--      <el-row :gutter="15">-->
      <!--        <el-col :span="24">-->
      <!--          <el-form-item label="涉及分公司">-->
      <!--            <el-select-->
      <!--              v-model="formData.branIdStr"-->
      <!--              multiple-->
      <!--              placeholder="请选择"-->
      <!--              :style="{ width: '100%' }"-->
      <!--            >-->
      <!--              <el-option-->
      <!--                v-for="dict in belongCompanyNameList"-->
      <!--                :key="dict.deptId"-->
      <!--                :label="dict.deptName"-->
      <!--                :value="dict.deptId"-->
      <!--              />-->
      <!--            </el-select>-->
      <!--          </el-form-item>-->
      <!--        </el-col>-->
      <!--      </el-row>-->
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="项目类别">
            <el-select v-model="formData.projectTypeId" placeholder="请选择" style="width: 100%;" @change="$forceUpdate()" :disabled="true">
              <el-option v-for="dict in projectTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="公司负责人">
            <el-input v-model="formData.companyCharge"  readonly="readonly"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="部门负责人">
            <el-input v-model="formData.departCharge"  readonly="readonly"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目负责人">
            <el-input v-model="formData.proCharge"  readonly="readonly"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="建筑物数">
            <el-input-number :style="{ width: '100%' }" :disabled="true" v-model="formData.buildingsNumber" :min="0" :step="1" ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="涉及建筑">
            <el-input-number :style="{ width: '100%' }" :disabled="true" v-model="formData.buildingsInvolvedNumber" :min="0" :step="1"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="数字TV数">
            <el-input-number :style="{ width: '100%' }" :disabled="true" v-model="formData.digitaltvNumber" :min="0" :step="1"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="宽带数">
            <el-input-number :style="{ width: '100%' }" :disabled="true" v-model="formData.broadbandNumber" :min="0" :step="1"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="材料预算" prop="budgetMaterial">
            <el-input v-model="formData.budgetMaterial"  readonly="readonly" v-on:input="changeNum()"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="人工预算" prop="budgetLabor">
            <el-input v-model="formData.budgetLabor"  readonly="readonly" v-on:input="changeNum()"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="预算总额">
            <el-input v-model="formData.budgetTotal"  readonly="readonly"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-form-item label="设计时间">
            <el-date-picker v-model="formData.designTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
              placeholder="请选择日期" :disabled="true" clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入场时间">
            <el-date-picker v-model="formData.dmissionTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
              placeholder="请选择日期"  :disabled="true" clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="完工时间">
            <el-date-picker v-model="formData.completionTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
              placeholder="请选择日期" :disabled="true" clearable></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input type="textarea"  readonly="readonly" v-model="formData.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="no-print">
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="print_sp">打 印</el-button>
      </div>
    </div>
  </div>
</template>
<script>
  import {
    findProjectType,
    findBranchOffice,
    delFileById,
    findFileListByPid
  } from "@/api/utils";
  import {
    treeselect
  } from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    components: {
      Treeselect
    },
    props: {
      baseInfoDate: {}
    },
    data() {
      return {
        formData: this.baseInfoDate,
        proTypeNameList: [],
        belongCompanyNameList: [],
        deptOptions: [],
        branchInvolvedOptions: [],
        projectTypeOptions: [], // 表单校验
        rules: {
          projectTypeId: [{
            required: true,
            message: '请选择项目类别',
            trigger: 'blur'
          }],
          budgetMaterial: [{
              required: true,
              message: "请输入材料预算",
              trigger: "blur"
            },
            {
              pattern: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^[1-9]$)|(^\d\.[1-9]{1,2}$)|(^\d\.[0]{1}[1-9]{1}$|(^\d\.[1-9]{1}[0]{1}$)$)/,
              message: "请正确输入材料预算",
              trigger: "blur"
            }
          ],
          budgetLabor: [{
              required: true,
              message: "请输入人工预算",
              trigger: "blur"
            },
            {
              pattern: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^[1-9]$)|(^\d\.[1-9]{1,2}$)|(^\d\.[0]{1}[1-9]{1}$|(^\d\.[1-9]{1}[0]{1}$)$)/,
              message: "请正确输入人工预算",
              trigger: "blur"
            }
          ]
        }
      };
    },
    watch: {
      baseInfoDate: {
        handler(val) {
          this.formData = val;
        },
        immediate: true,
        deep: true
      }
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
      },
      //打印
      print_sp() {
        this.$print(this.$refs.basicInfo)
      },
    }
  };
</script>
<style scoped lang="scss">
  .el-textarea .el-textarea__inner {
    resize: none;
  }
</style>
