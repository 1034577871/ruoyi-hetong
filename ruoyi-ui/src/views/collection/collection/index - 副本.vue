<template>
  <div class="app-container">
    <el-form :model="queryParams"
    ref="queryForm"
    v-show="showSearch"
    label-width="90px">
   <el-row :gutter="15">
      <el-col :span="6">
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
        <el-col :span="6">
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
        <el-col :span="6">
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
        <el-col :span="6">
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
      </el-row>
      <el-row :gutter="15">
        <el-col :span="6">
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
          <el-col :span="6">
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
         <el-col :span="6">
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

        <el-col :span="6">
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

    <!-- 添加或修改事前审批主(集客类)对话框 :rules="rules"
    <el-dialog  :modal="true" :title="title" :visible.sync="open" width="70%" >-->
    <el-dialog title="发票申请" :visible.sync="open" width="80%">
    <el-form ref="form" :model="form"  :rules="rules"  label-width="100px">
      <el-row :gutter="15">
        <el-col :span="24" style="margin-top: 0;">
          <el-divider content-position="center" ><el-link type="primary"><b>申请信息</b></el-link></el-divider>
        </el-col>
        <el-col :span="6">
          <el-form-item label="项目类型" prop="ApprovalType">
            <el-select v-model="form.approvalType" placeholder="请选择项目类型" style="width: 100%;">
              <el-option
                v-for="dict in projectCategoryTypeList"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2" style="height: 36px;">
           <el-button type="primary"   @click="getApplyList" size="mini" >选择项目</el-button>
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
                  <el-col :span="10">
                    <el-form-item label="项目编号" prop="reqNo" >
                      <el-input

                        v-model="queryParams.reqNo"
                        placeholder="请输入项目编号"
                        clearable
                        size="small"

                        @keyup.enter.native="handlesQuery"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="10">
                    <el-form-item label="项目名称" prop="proName" >
                      <el-input
                        v-model="queryParams.proName"
                        placeholder="请输入项目名称"
                        clearable
                        size="small"

                        @keyup.enter.native="handlesQuery"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="4" style="padding-left: 10px; line-height: 36px;">
                      <el-button
                        type="cyan"
                        icon="el-icon-search"
                        size="mini"
                        @click="handlesQuery"
                        >搜索</el-button
                      >
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
          </el-dialog>
        </el-col>
        <el-col :span="8">

          <el-form-item label="项目编号" prop="proNo">
            <el-input v-model="form.proNo" :readonly="true"  />
          </el-form-item>
         </el-col>
         <el-col :span="8">
          <el-form-item label="项目名称" prop="proName">
            <el-input v-model="form.proName"  :readonly="true"    placeholder="请输入项目名称"  />
          </el-form-item>
        </el-col>


        <el-col :span="6">
          <el-form-item label="" prop="projectCategory">
            <!-- <el-select v-model="form.projectCategory" placeholder="请选择项目类型" style="width: 100%;">
              <el-option
                v-for="dict in projectCategoryTypeList"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select> -->
          </el-form-item>
        </el-col>
        <el-col :span="2" style="height: 36px;">
          <el-button type="success"   @click="getContractList"   size="mini">选择合同</el-button>
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

                                <el-col :span="10">
                                  <el-form-item label="合同编号" prop="contractNo">
                                    <el-input
                                      v-model="queryParams.contractNo"
                                      placeholder="请输入合同编号"
                                      clearable
                                      size="small"
                                      @keyup.enter.native="handlehtQuery"
                                    />
                                  </el-form-item>
                                </el-col>
                                <el-col :span="10">
                                  <el-form-item label="合同名称" prop="contractName" >
                                    <el-input
                                      v-model="queryParams.contractName"
                                      placeholder="请输入项目名称"
                                      clearable
                                      size="small"
                                      @keyup.enter.native="handlesQuery"
                                    />
                                  </el-form-item>
                                </el-col>

                                <el-col :span="4" style="padding-left: 10px; line-height: 36px;">
                                    <el-button
                                      type="cyan"
                                      icon="el-icon-search"
                                      size="mini"
                                      @click="handlehtQuery"
                                      >搜索</el-button
                                    >
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
         <el-col :span="8">
           <el-form-item label="事前审批表ID 关联事前审批表ID" prop="approvalId" v-if="show">
              <el-input v-model="form.contractId" placeholder="" />
           </el-form-item>
          <el-form-item label="合同编号"     prop="contractNo">
              <el-input v-model="form.contractNo" :readonly="true"  placeholder="请输入合同编号" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="合同名称"    prop="contractName">
            <el-input v-model="form.contractName" :readonly="true"  placeholder="请输入合同名称" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="发票种类" prop="invoiceType" >
           <el-select :style="{ width: '100%' }"
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
         <el-col :span="8">
          <el-form-item label="部门编号" prop="reqDeptid">
            <el-input v-model="form.reqDeptid"  :readonly="true"    placeholder="请输入申请部门编号" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="部门名称" prop="reqDeptname">
            <el-input v-model="form.reqDeptname"  :readonly="true"    placeholder="请输入申请部门名称" />
          </el-form-item>
        </el-col>
            <el-col :span="8">
              <el-form-item  label="申请人编号" prop="reqUserid">
                <el-input v-model="form.reqUserid"  :readonly="true"    placeholder="请输入申请人编号" />
              </el-form-item>
            </el-col>
             <el-col :span="8">
              <el-form-item label="申请人姓名" prop="reqUsername">
                <el-input v-model="form.reqUsername"  :readonly="true"    placeholder="请输入申请人姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="申请日期" prop="reqDate">
                <el-date-picker clearable size="small" :style="{ width: '100%' }"
                  v-model="form.reqDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择申请日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单位名称" prop="payerName">
                <!-- <el-input v-model="form.payerName" placeholder="请输入付款单位名称" /> -->
                <selectCompany ref='company'   @ok='companyOk'/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单位地址" prop="payerAddress">
                <el-input v-model="form.payerAddress"  :readonly="true"    placeholder="请输入付款单位地址" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="纳税人识别号" prop="creditCode" >
                <el-input v-model="form.creditCode" placeholder="请输入纳税人识别号" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单位电话" :readonly="true"   prop="payerPhone">
                <el-input v-model="form.payerPhone"   placeholder="请输入付款单位电话" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单位开户行" :readonly="true"   prop="payerOpeningBank">
                <el-input v-model="form.payerOpeningBank"   placeholder="请输入付款单位开户行" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单位账号" :readonly="true"   prop="payerAccount">
                <el-input v-model="form.payerAccount"   placeholder="请输入付款单位账号" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="开票金额(含税)" prop="invoiceAmountTax">
                <el-input v-model="form.invoiceAmountTax" placeholder="请输入开票金额"  @blur='takeAcc(this)' oninput="value=value.replace(/[^0-9.]/g,'')"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="开票金额(不含税)" :readonly="true"   prop="invoiceAmountNtax">
                <el-input v-model="form.invoiceAmountNtax"  placeholder="请输入开票金额" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预计回款时间" prop="expectedCollectionTime" >
                <el-date-picker clearable size="small"
                  v-model="form.expectedCollectionTime"
                  type="date"  :style="{ width: '100%' }"
                  value-format="yyyy-MM-dd"
                  placeholder="选择预计回款时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>

      <el-col :span="24" style="margin-top: 0;">
       <el-divider content-position="center" ><el-link type="primary"><b>开票信息</b></el-link></el-divider>
      </el-col>
    <el-form ref="elForm" size="medium" label-width="100px">
      <table class="curr-table ">
        <tbody>
          <tr>
            <th width="5%">序号</th>
            <th width="25%">项目名称</th>
            <th width="10%">规格</th>
            <th width="10%">型号</th>
            <!-- <th width="8%">单位</th> -->
            <th width="10%">单价</th>
            <th width="8%">数量</th>
            <th width="12%">金额(含税)</th>
            <th width="12%">税率</th>
            <th width="12%">金额(不含税)</th>
            <th width="5%">
              操作
            </th>
            <th>
              <el-button
                size="mini"
                type="success"
                @click="loadMore()"
                style="margin: 10px;"
                >新增</el-button
              >
            </th>
          </tr>
          <tr v-for="(value, index) in dataList" :key="index">
            <td>
              {{ index }}
            </td>
            <td>
              <el-input
                v-model="value.invoiceProName"
                :name="'appMateList[' + index + '].invoiceProName'"
              />
            </td>
            <td>
              <el-input
                v-model="value.invoiceSpec"
                :name="'appMateList[' + index + '].invoiceSpec'"
              />
            </td>
            <td>
              <el-input
                v-model="value.invoiceType"
                :name="'appMateList[' + index + '].invoiceType'"
              />
            </td>
            <!-- <td>
              <el-input
                v-model="value.unit"
                :name="'appMateList[' + index + '].unit'"
                v-on:input="changeNum(index)"
              />
            </td> -->
            <td>
              <el-input
                v-model="value.invoicePrice"
                :name="'appMateList[' + index + '].invoicePrice'"
                v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')"
              />
            </td>
            <td>
              <el-input
                v-model="value.invoiceMount"
                :name="'appMateList[' + index + '].invoiceMount'"
                v-on:input="changeNum(index)" oninput="value=value.replace(/[^0-9.]/g,'')"
              />
            </td>
            <td>
              <el-input

                v-model="value.invoiceAmountTax"
                :name="'appMateList[' + index + '].invoiceAmountTax'"
              />
            </td>

            <td>
              <el-input

                v-model="value.invoiceTaxrate"
                :name="'appMateList[' + index + '].invoiceTaxrate'"

              />
            </td>
            <td>
              <el-input

                v-model="value.invoiceAmountNtax"
                :name="'appMateList[' + index + '].invoiceAmountNtax'"
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
      dataList: [],  //动态添加行的数据集合
      taxrate:0,
      //项目类型
      projectCategoryTypeList:[],
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
      projectCategory: null,
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
  // this.getContractList();
   this.getDicts("bill_type").then((response) => {
     this.billTypeOptions = response.data;
   });
   //项目类型列表
   this.getDicts("project_category_type").then((response) => {
     console.log("************************************");
     this.approvalTypeOptions = response.data;
     this.projectCategoryTypeList = response.data;
   });
 },
  methods: {




    /** 添加行 **/
    loadMore: function() {
      this.dataList.push({
        id: null,
        invoiceProName: null,
        invoiceSpec: null,
        invoiceType: null,
        unit: null,
        invoicePrice: null,
        invoiceMount: null,
        invoiceAmountTaxs: null,

        invoiceTaxrate: this.taxrate,
        invoiceAmountNtax: null,
      });
    },
    /** 删除行 **/
    removeTodo: function(index) {
      this.dataList.splice(index, 1);
    },
    //监听输入框输入值
    changeNum(index){
      //单价
      let dj = this.dataList[index].invoicePrice;
      //数量
      let sl = this.dataList[index].invoiceMount

      if(dj != null && sl != null){
        //获得不含税金额
        this.dataList[index].invoiceAmountTax = (dj*sl).toFixed(2);
        this.dataList[index].invoiceAmountNtax = ((dj*sl) / (this.taxrate * 0.01 + 1)).toFixed(2);
      }

    },
    //获取合同信息
    getContractList() {
      this.htloading = true;
      this.innerhtVisible = true;
      this.queryParams.projectId = (this.form.approvalId);
      findContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.contractList = response.rows;
        this.total = response.total;
        this.htloading = false;
         this.innerhtVisible = true;
      });
    },
    /** 搜索按钮操作 */
    handlesQuery() {
      this.queryParams.pageNum = 1;
      this.getApplyList();
    },
  takeTotal(obj){

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
    //含税金额技术
    takeAcc(obj){
      if(this.form.invoiceAmountTax!=''){
        this.form.invoiceAmountNtax = ((this.form.invoiceAmountTax)/(1+this.taxrate*0.01)).toFixed(2);
      }

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
      this.dataList=new Array();;
      this.reset();
      const id = row.id || this.ids
      getCollection(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改发票申请";
        if(response.data.ctBuInvoiceReqDetail!=null && response.data.ctBuInvoiceReqDetail.length>0){
         
         for(let item of response.data.ctBuInvoiceReqDetail) {
           this.taxrate =   item.invoiceAmountTaxs;
          this.dataList.push({
            id: item.id,
            invoiceProName: item.invoiceProName,
            invoiceSpec: item.invoiceSpec,
            invoiceType: item.invoiceType,
            invoicePrice: item.invoicePrice,
            invoiceMount: item.invoiceMount,
            invoiceAmountTaxs: item.invoiceAmountTaxs,
             
            invoiceTaxrate: item.invoiceTaxrate,
            invoiceAmountNtax: item.invoiceAmountNtax,
          });
        }
        }
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
        this.taxrate = response.data.taxrate;
       this.form.contractId=id;
       this.form.contractNo=response.data.contractNo;
       this.form.contractName=response.data.contractName;
       this.innerhtVisible = false;
      });


    },
    // 提交按钮
    submitForm(val) {

      this.$refs["form"].validate(valid => {
        this.form.ctBuInvoiceReqDetail = this.dataList;
        if (valid) {
          this.form.state = val;
          if (this.form.id != null) {
            updateCollection(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
               this.reset();
            });
          } else {
            addCollection(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
               this.reset();
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
