<template>
  <div class="app-container">
    <el-form :model="queryParams"
    ref="queryForm"
    :inline="true"
    v-show="showSearch"
    label-width="90px">
 <el-row :gutter="21">
        <el-col :span="7">
      <el-form-item label="项目编号" prop="proNo">
        <el-input
          v-model="queryParams.proNo"
          placeholder="请输入项目"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      </el-col>
       <el-col :span="7">
      <el-form-item label="项目名称" prop="proName">
        <el-input
          v-model="queryParams.proName"
          placeholder="请输入项目名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
</el-col>
<el-col :span="7">
      <el-form-item label="合同编号" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          placeholder="请输入合同编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      </el-col>
 </el-row>
  <el-row :gutter="21">
      <el-col :span="7">
      <el-form-item label="合同名称" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      </el-col>





      <el-col :span="7">
      <el-form-item label="发票种类" prop="invoiceType">
        <el-select

          v-model="queryParams.invoiceType"
          placeholder="发票类型"
        >
          <el-option
            v-for="dict in billTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
 </el-col>
 <el-col :span="7">
      <el-form-item label="姓名" prop="reqUsername">
        <el-input
          v-model="queryParams.reqUsername"
          placeholder="请输入申请人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
       </el-col>
       </el-row>
       <el-row :gutter="21">
        <el-col :span="7">
      <el-form-item label="申请日期" prop="reqDate">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>

      </el-form-item>
</el-col>
        <el-col :span="7">
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      </el-col>
</el-row>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['collection:collection:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['collection:collection:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['collection:collection:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['collection:collection:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="collectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一ID" align="center" prop="id" v-if="show" />
      <el-table-column label="项目名称" align="center" prop="proName" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="合同名称" align="center" prop="contractName" />
      <el-table-column label="发票种类" align="center" prop="invoiceType" />
      <el-table-column label="申请部门名称" align="center" prop="reqDeptname" />
      <el-table-column label="申请人姓名" align="center" prop="reqUsername" />
      <el-table-column label="申请日期" align="center" prop="reqDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reqDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['collection:collection:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['collection:collection:remove']"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="historyFory(scope.row)"
            v-hasPermi="['workflow:leave:edit']"
          >审批详情
          </el-button>

          <el-button v-if="0==scope.row.state"
                     size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="checkTheSchedule(scope.row)"
                     v-hasPermi="['workflow:leave:edit']"
          >查看进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!-- 添加或修改事前审批主(集客类)对话框
    <el-dialog  :modal="true" :title="title" :visible.sync="open" width="70%" >-->
    <el-dialog title="发票申请" :visible.sync="open" width="80%">
    <el-form ref="form" :model="form" :rules="rules"   label-width="180px">
      <el-col :span="24" style="margin-top: 0;">
        <el-divider content-position="center" ><el-link type="primary"><b>申请信息</b></el-link></el-divider>
      </el-col>
          <el-col :span="24">
            <el-form-item label="事前审批类型" prop="approvalType">
              <el-select
                v-model="form.approvalType"
                placeholder="事前审批类型"
              >
                <el-option
                  v-for="dict in approvalTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>


            </el-form-item>
          </el-col>
          <el-col :span="24" style="text-align:center">
             <el-button type="primary"    @click="getApplyList"  >选择项目</el-button>
             <!--弹框-->
            <el-dialog
                width="50%"
                height="70%"
                title="项目信息"
                :visible.sync="innerVisible"
                append-to-body>


                <el-form
                  :model="queryParams"
                  ref="queryForm"
                  v-show="showSearch"
                  label-width="90px"
                >
                  <el-row >

                    <el-col :span="8">
                      <el-form-item label="项目编号" prop="reqNo" width="60%">
                        <el-input

                          v-model="queryParams.reqNo"
                          placeholder="请输入项目编号"
                          clearable
                          size="small"

                          @keyup.enter.native="handlesQuery"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="项目名称" prop="proName"  width="60%">
                        <el-input
                          v-model="queryParams.proName"
                          placeholder="请输入项目名称"
                          clearable
                          size="small"

                          @keyup.enter.native="handlesQuery"
                        />
                      </el-form-item>
                    </el-col>

                    <el-col :span="3">
                      <el-form-item>
                        <el-button
                          type="cyan"
                          icon="el-icon-search"
                          size="mini"
                          @click="handlesQuery"
                          >搜索</el-button
                        >

                      </el-form-item>
                    </el-col>

                    <el-col :span="2">
                      <el-form-item>
                        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                          >重置</el-button
                        >
                      </el-form-item>
                    </el-col>

                  </el-row>

                </el-form>

                <el-table  ref="Table"  v-loading="loadings" :data="projectList" >
                  <el-table-column type="selection" width="55" align="center" v-if="show"/>
                  <el-table-column label="唯一ID" align="center" prop="id" v-if="show" />
                  <el-table-column label="项目名称" align="center" prop="proName" />
                  <el-table-column label="项目编号" align="center" prop="reqNo" />
                  <el-table-column label="项目类型名称" align="center" prop="proTypeName" />
                  <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                          <template slot-scope="scope">
                            <el-button
                              size="mini"
                              type="text"
                              icon="el-icon-edit"
                              @click="submitsForm(scope.row)"

                            >选择</el-button>

                          </template>
                        </el-table-column>
                </el-table>

                <pagination
                      v-show="total>0"
                      :total="total"
                      :page.sync="queryParams.pageNum"
                      :limit.sync="queryParams.pageSize"
                      @pagination="getApplyList"
                    />


            <div slot="footer" class="dialog-footer">
              <!-- <el-button type="primary" @click="submitsForm">确 定</el-button> -->
              <el-button @click="cancelAppri">取 消</el-button>
            </div>
    s

            </el-dialog>



          </el-col>

            <el-col :span="12">
              <el-form-item label="事前审批表ID 关联事前审批表ID" prop="approvalId" v-if="show">
                 <el-input v-model="form.approvalId" placeholder="请输入事前审批表ID 关联事前审批表ID" />
              </el-form-item>
              <el-form-item label="项目编号" prop="proNo">
                <el-input v-model="form.proNo"    placeholder="请输入项目编号"  />
              </el-form-item>
             </el-col>
             <el-col :span="12">
              <el-form-item label="项目名称" prop="proName">
                <el-input v-model="form.proName"   placeholder="请输入项目名称"  />
              </el-form-item>
            </el-col>
           <!-- <el-form-item label="合同ID " prop="contractId">
              <el-input v-model="form.contractId" placeholder="请输入合同ID " />
            </el-form-item> -->
            <el-col :span="24"  style="text-align:center">
               <el-button type="primary" @click="innerhtVisible = true" >选择合同</el-button>
                <!--弹框-->
                      <el-dialog
                            width="50%"
                            height="70%"
                            title="合同信息"
                            :visible.sync="innerhtVisible"
                            append-to-body>


                            <el-form
                              :model="queryParams"
                              ref="queryhtForm"
                              v-show="showSearch"
                              label-width="90px"
                            >
                              <el-row >

                                <el-col :span="8">
                                  <el-form-item label="合同编号" prop="contractNo" width="60%">
                                    <el-input
                                      v-model="queryParams.contractNo"
                                      placeholder="请输入合同编号"
                                      clearable
                                      size="small"
                                      @keyup.enter.native="handlehtQuery"
                                    />
                                  </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                  <el-form-item label="合同名称" prop="contractName"  width="60%">
                                    <el-input
                                      v-model="queryParams.contractName"
                                      placeholder="请输入项目名称"
                                      clearable
                                      size="small"
                                      @keyup.enter.native="handlesQuery"
                                    />
                                  </el-form-item>
                                </el-col>

                                <el-col :span="3">
                                  <el-form-item>
                                    <el-button
                                      type="cyan"
                                      icon="el-icon-search"
                                      size="mini"
                                      @click="handlesQuery"
                                      >搜索</el-button
                                    >

                                  </el-form-item>
                                </el-col>

                                <el-col :span="2">
                                  <el-form-item>
                                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                                      >重置</el-button
                                    >
                                  </el-form-item>
                                </el-col>

                              </el-row>

                            </el-form>

                            <el-table  ref="htTable"  v-loading="htloading" :data="contractList" @selection-change="handleXmSelectionChange">
                               <el-table-column type="selection" width="55" align="center" v-if="show"/>
                              <el-table-column label="唯一ID" align="center" prop="id" v-if="show" />
                              <el-table-column label="项目编号" align="center" prop="projectNo" />
                              <el-table-column label="项目名称" align="center" prop="projectName" />
                              <el-table-column label="合同编号" align="center" prop="contractNo" />
                              <el-table-column label="合同名称" align="center" prop="contractName" />
                              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                                      <template slot-scope="scope">
                                        <el-button
                                          size="mini"
                                          type="text"
                                          icon="el-icon-edit"
                                          @click="submithtForm(scope.row)"

                                        >选择</el-button>

                                      </template>
                                    </el-table-column>
                            </el-table>

                            <pagination
                                  v-show="total>0"
                                  :total="total"
                                  :page.sync="queryParams.pageNum"
                                  :limit.sync="queryParams.pageSize"
                                  @pagination="contractList"
                                />


                        <div slot="footer" class="dialog-footer">
                          <!-- <el-button type="primary" @click="submithtForm">确 定</el-button> -->
                          <el-button @click="innerhtVisible=false">取 消</el-button>
                        </div>

                        </el-dialog>
            </el-col>
             <el-col :span="12">
               <el-form-item label="事前审批表ID 关联事前审批表ID" prop="approvalId" v-if="show">
                  <el-input v-model="form.contractId" placeholder="" />
               </el-form-item>
              <el-form-item label="合同编号" prop="contractNo">
                  <el-input v-model="form.contractNo" readOnly="true" placeholder="请输入合同编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同名称" prop="contractName">
                <el-input v-model="form.contractName" readOnly="true" placeholder="请输入合同名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票种类" prop="invoiceType">
               <el-select
                 v-model="form.invoiceType"
                 placeholder="发票类型"
               >
                 <el-option
                   v-for="dict in billTypeOptions"
                   :key="dict.dictValue"
                   :label="dict.dictLabel"
                   :value="dict.dictValue"
                 />
               </el-select>
              </el-form-item>
             </el-col>
             <el-col :span="12">
              <el-form-item label="部门编号" prop="reqDeptid">
                <el-input v-model="form.reqDeptid" readOnly="true" placeholder="请输入申请部门编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门名称" prop="reqDeptname">
                <el-input v-model="form.reqDeptname" readOnly="true" placeholder="请输入申请部门名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item  label="申请人编号" prop="reqUserid">
                <el-input v-model="form.reqUserid" readOnly="true" placeholder="请输入申请人编号" />
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="申请人姓名" prop="reqUsername">
                <el-input v-model="form.reqUsername" readOnly="true" placeholder="请输入申请人姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请日期" prop="reqDate">
                <el-date-picker clearable size="small" style="width: 200px"
                  v-model="form.reqDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择申请日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="payerName">
                <!-- <el-input v-model="form.payerName" placeholder="请输入付款单位名称" /> -->
                <selectCompany ref='company' readOnly="true" @ok='companyOk'/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="纳税人识别号" prop="creditCode" >
                <el-input v-model="form.creditCode" placeholder="请输入纳税人识别号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位地址" prop="payerAddress">
                <el-input v-model="form.payerAddress" readOnly="true" placeholder="请输入付款单位地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位电话" prop="payerPhone">
                <el-input v-model="form.payerPhone" readOnly="true" placeholder="请输入付款单位电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位开户行" prop="payerOpeningBank">
                <el-input v-model="form.payerOpeningBank" readOnly="true" placeholder="请输入付款单位开户行" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位账号" prop="payerAccount">
                <el-input v-model="form.payerAccount" readOnly="true" placeholder="请输入付款单位账号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票金额(含税)" prop="invoiceAmountTax">
                <el-input v-model="form.invoiceAmountTax" placeholder="请输入开票金额" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票金额(不含税)" prop="invoiceAmountNtax">
                <el-input v-model="form.invoiceAmountNtax" placeholder="请输入开票金额" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预计回款时间" prop="expectedCollectionTime">
                <el-date-picker clearable size="small" style="width: 200px"
                  v-model="form.expectedCollectionTime"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择预计回款时间">
                </el-date-picker>
              </el-form-item>
            </el-col>


     <el-col :span="24" style="margin-top: 0;">
       <el-divider content-position="center" ><el-link type="primary"><b>开票信息</b></el-link></el-divider>
     </el-col>

        <el-col :span="12">
         <el-form-item label="开票项目名称" prop="invoiceProName">
             <el-input v-model="form.invoiceProName" placeholder="开票项目名称" />
         </el-form-item>
        </el-col>
        <el-col :span="12">
         <el-form-item label="规格" prop="invoiceSpec">
              <el-input v-model="form.invoiceSpec"  placeholder="规格" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
         <el-form-item label="型号" prop="invoiceTypes">
              <el-input v-model="form.invoiceTypes"  placeholder="型号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
         <el-form-item label="单位" prop="unit">
             <el-input v-model="form.unit"  placeholder="单位" />
         </el-form-item>
        </el-col>
        <el-col :span="12">
         <el-form-item label="单价" prop="invoicePrice">
             <el-input v-model="form.invoicePrice"  placeholder="单价" />
         </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="数量" prop="invoiceMount">
            <el-input v-model="form.invoiceMount"  placeholder="数量" />
        </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="金额（含税）" prop="invoiceAmountTaxs">
            <el-input v-model="form.invoiceAmountTaxs"  placeholder="金额（含税）" />
        </el-form-item>
        </el-col>


          </el-form>
     <span slot="footer" class="dialog-footer">
       <el-button type="primary" @click="submitForm('0')">保存</el-button>
       <el-button type="primary" @click="submitForm('1')">提交</el-button>
       <el-button @click="open = false">取消</el-button>
     </span>
  </el-dialog>

  </div>





</template>

<script>
import { listCollection, getCollection, delCollection, addCollection, updateCollection, exportCollection } from "@/api/collection/collection";

import { listApplyCollection,detailNgcApproval,findContract,detailhtCon,getUserInfo  } from "@/api/collection/common";


import { findProjectType, findBranchOffice } from "@/api/utils";

import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import billInfo from './billInfo.vue'
import applyBill from './applyBill.vue'
import selectCompany from './selectCompany';


export default {
  name: "Collection",
  components: {
    Treeselect,
    billInfo,
    applyBill,
      selectCompany
  },




  data() {
    return {
       dialogVisibles: false,
      centerDialogVisible: false,
       dialogVisible: false,
       innerVisible: false,
       innerhtVisible: false,
        outerVisible: false,
       show: false,
      // 遮罩层
      loading: true,
      loadings: true,
      htloading:true,
      apptype:'',
      // 选中数组

      ids: [],
      reqNo:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 项目信息列表
      contractList: [],
      // 项目信息列表
      projectList: [],
      // 发票申请表格数据
      collectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        approvalType: null,
        approvalId: null,
        proNo: null,
        proName: null,
        contractId: null,
        contractNo: null,
        contractName: null,
        invoiceType: null,
        reqDeptid: null,
        reqDeptname: null,
        reqUserid: null,
        reqUsername: null,
        reqDate: null,
        payerName: null,
        creditCode: null,
        payerAddress: null,
        payerPhone: null,
        payerOpeningBank: null,
        payerAccount: null,
        invoiceAmountTax: null,
        invoiceAmountNtax: null,
        expectedCollectionTime: null,
        status: null,
        statusps: null,
      },
      // 表单参数
    // form: {},
    form: {
        // "ctBuInvoiceReqDetail": {
        //     "invoiceProName": "",

        //  }
     },

      // 表单校验
     rules:{
         approvalType: [{ required: true, message: '请选择事前审批类型', trigger: 'blur' }],
         approvalId: [{ required: true, message: '请选择项目', trigger: 'blur' }],
         proNo: [{ required: true, message: '项目编号不能为空', trigger: 'blur' }],
         proName: [{ required: true, message: '项目名称不能为空', trigger: 'blur' }],
         reqDeptid: [{ required: true, message: '申请部门编号不能为空', trigger: 'blur' }],
         reqDeptname: [{ required: true, message: '申请部门名称不能为空', trigger: 'blur' }],
         creditCode: [{ required: true, message: '纳税人识别号不能为空', trigger: 'blur' }],
         payerAddress: [{ required: true, message: '付款单位地址不能为空', trigger: 'blur' }],
         payerPhone: [{ required: true, message: '付款单位电话不能为空', trigger: 'blur' }],
         payerOpeningBank: [{ required: true, message: '付款单位开户行不能为空', trigger: 'blur' }],
         payerAccount: [{ required: true, message: '付款单位账号不能为空', trigger: 'blur' }],
         invoiceAmountTax: [{ required: true, message: '开票金额（含税）不能为空', trigger: 'blur' }],
         invoiceAmountNtax: [{ required: true, message: '开票金额（不含税）不能为空', trigger: 'blur' }],
         expectedCollectionTime: [{ required: true, message: '预计回款时间不能为空', trigger: 'blur' }],
         reqUserid: [{ required: true, message: '申请人编号不能为空', trigger: 'blur' }],
         reqUsername: [{ required: true, message: '申请人姓名不能为空', trigger: 'blur' }],
         reqDate: [{ required: true, message: '申请日期不能为空', trigger: 'blur' }],
         invoiceId: [{ required: true, message: '发票单号不能为空', trigger: 'blur' }],
         invoiceProName: [{ required: true, message: '开票项目名称不能为空', trigger: 'blur' }],
        invoiceSpec: [{ required: true, message: '规格不能为空', trigger: 'blur' }],
        invoiceTypes: [{ required: true, message: '型号不能为空', trigger: 'blur' }],
     },
     billTypeOptions: [],
     approvalTypeOptions:[],
    };
  },



 created() {
   this.getList();
  // this.getApplyList();
   this.getContractList();
   this.getDicts("bill_type").then((response) => {
     this.billTypeOptions = response.data;
   });
   this.getDicts("project_category_type").then((response) => {
     console.log("************************************");
     this.approvalTypeOptions = response.data;
   });

 },
  methods: {
    //获取合同信息
    getContractList() {
      this.htloading = true;
      findContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.contractList = response.rows;
        this.total = response.total;
        this.htloading = false;
      });
    },
    /** 搜索按钮操作 */
    handlesQuery() {
      this.queryParams.pageNum = 1;
      this.getApplyList();
    },

    /*选择对方单位弹出框 调用方法*/
    companyOk(id,companyName,openingBank,openingAccount,linkMan,linkPhone,address){
      this.form.payerName = companyName;
      this.form.payerAddress = address;
      this.form.payerPhone = linkPhone;
      this.form.payerOpeningBank = openingBank;
      this.form.payerAccount = openingAccount;
      this.$set(this.form,'oppositeName',companyName)
    },


    /** 搜索按钮操作 */
    handlehtQuery() {
      this.queryParams.pageNum = 1;
      this.getContractList();
    },
    //获取项目信息
    getApplyList() {
      this.projectList = [];
      this.loadings = true;
      this.innerVisible = true;
      this.queryParams.pageNum = 1;
      this.apptype=this.form.approvalType;
    //  this.queryParams.approvalType = this.form.approvalType;
      listApplyCollection(this.form.approvalType,this.addDateRange(this.queryParams, this.dateRange)).then(response => {
         this.total = response.total;
         if(this.total==0){
           this.projectList = [];
         }else{
           this.projectList = response.rows;
         }


        this.loadings = false;

        this.innerVisible = true;
          this.title = "修改发票申请";
          this.apptype=this.form.approvalType;
      });
    },
    /** 查询发票申请列表 */
    getList() {
      this.loading = true;
      listCollection(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.collectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });

    },
    handleClose(done) {
            this.$confirm('确认关闭？')
              .then(_ => {
                done();
              })
              .catch(_ => {});
          },
    czyFormat(row, column) {
      return this.selectDictLabel(this.billTypeOptions, row.invoiceType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelAppri() {
      this.innerVisible = false;
      this.form.reqNo = null;
      this.form.proName = null;
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        approvalType: null,
        approvalId: null,
        proNo: null,
        proName: null,
        contractId: null,
        contractNo: null,
        contractName: null,
        invoiceType: null,
        reqDeptid: null,
        reqDeptname: null,
        reqUserid: null,
        reqUsername: null,
        reqDate: null,
        payerName: null,
        creditCode: null,
        payerAddress: null,
        payerPhone: null,
        payerOpeningBank: null,
        payerAccount: null,
        invoiceAmountTax: null,
        invoiceAmountNtax: null,
        expectedCollectionTime: null,
        status: 0,
        statusps: null,
        delFlag: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
      this.reqNo =selection
    },
    // 多选框选中数据
    handleXmSelectionChange(val) {
       this.ids = val.map(item => item.id)
       if (val.length > 1) {
       		this.$refs.Table.clearSelection()
       		this.$refs.Table.toggleRowSelection(val.pop())
       	} else {
       	}

    },
    currentChange(currentRow, oldCurrentRow) {
      this.$refs.Table.toggleRowSelection(currentRow)
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      //获取当前用户信息
      getUserInfo().then(response => {
        this.form.reqDeptid=response.data.user.dept.deptId;
        this.form.reqDeptname=response.data.user.dept.deptName;
        this.form.reqUserid=response.data.user.userId
       this.form.reqUsername=response.data.user.userName
        this.open = true;
        this.title = "添加发票申请";
      });
      //this.title = "添加发票申请";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCollection(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改发票申请";
      });
    },
    /** 修改按钮操作 */
    submitsForm(row) {
      const id = row.id || this.ids;
      detailNgcApproval(this.apptype,id).then(response => {
       this.form.approvalId=id;
       this.form.proNo=response.data.reqNo;
       this.form.proName=response.data.proName;
       this.innerVisible = false;
      });
    },
    /** 选择合同 */
    submithtForm(row) {
      const id = row.id || this.ids;
      detailhtCon(id).then(response => {
       this.form.contractId=id;
       this.form.contractNo=response.data.contractNo;
       this.form.contractName=response.data.contractName;
       this.innerhtVisible = false;
      });


    },
    // 提交按钮
    submitForm(val) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.state = val;
          if (this.form.id != null) {
            updateCollection(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCollection(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    //选择项目

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除发票申请编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCollection(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有发票申请数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCollection(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
