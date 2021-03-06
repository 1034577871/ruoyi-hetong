package com.ruoyi.projectApproval.AppNgc.domain;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.collection.domain.ActWorkflowFormDatas;
import com.ruoyi.projectApproval.AppNgBase.domain.AppNgBase;
import com.ruoyi.projectApproval.AppNgConst.domain.AppNgConst;
import com.ruoyi.projectApproval.AppNgMaterial.domain.AppNgMaterial;
import com.ruoyi.projectApproval.AppxNgReq.domain.AppxNgReq;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 非集客项目对象 ct_bu_approval_ngc
 *
 * @author ruoyi
 * @date 2020-12-11
 */
public class AppNgc extends BaseEntity {

    private static final long serialVersionUID = 1L;


    public List<ActWorkflowFormDatas> getActWorkflowFormDatas() {
        return actWorkflowFormDatas;
    }

    public void setActWorkflowFormDatas(List<ActWorkflowFormDatas> actWorkflowFormDatas) {
        this.actWorkflowFormDatas = actWorkflowFormDatas;
    }

    private List<ActWorkflowFormDatas> actWorkflowFormDatas;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    //实例id
    private String instanceId;

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 申请编号
     */
    @Excel(name = "申请编号")
    private String reqNo;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String proName;

    /**
     * 项目类型编号
     */
//    @Excel(name = "项目类型编号")
    private String proType;

    /**
     * 项目类型名称
     */
    @Excel(name = "项目类型名称")
    private String proTypeName;

    /**
     * 项目分类（收入、支出）
     */
//    @Excel(name = "项目分类", readConverterExp = "收入、支出")
    private String classification;

    /**
     * 项目分类（收入、支出）
     */
    private String className;

    /**
     * 合同金额
     */
    @Excel(name = "合同金额")
    private BigDecimal contractAmountTotal;

    /**
     * 不含税金额
     */
    @Excel(name = "不含税金额")
    private BigDecimal contractAmountTax;

    /**
     * 税率
     */
    @Excel(name = "税率")
    private BigDecimal taxrate;

    /**
     * 税额
     */
    @Excel(name = "税额")
    private BigDecimal taxamount;

    /**
     * 所属分公司编号
     */
//    @Excel(name = "所属分公司编号")
    private String belongCompanyid;

    /**
     * 所属分公司名称
     */
    @Excel(name = "所属分公司名称")
    private String belongCompanyname;

    /**
     * 所属部门编号
     */
//    @Excel(name = "所属部门编号")
    private String belongDeptid;

    /**
     * 所属部门名称
     */
    @Excel(name = "所属部门名称")
    private String belongDeptname;

    /**
     * 申请原因
     */
    @Excel(name = "申请原因")
    private String reqReason;

    /**
     * 说明
     */
    @Excel(name = "说明")
    private String explainInfo;

    /**
     * 附件
     */
//    @Excel(name = "附件")
    private String files;

    /**
     * 状态
     */
//    @Excel(name = "状态")
    private Integer status;

    /**
     * 状态说明
     */
    @Excel(name = "状态说明")
    private String statusPs;

    /**
     * 备注信息
     */
    @Excel(name = "备注信息")
    private String remarks;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 开始时间
     **/
    private String stime;

    /**
     * 结束时间
     **/
    private String etime;

    /**
     * 是否同步资管(1=是，2=否)
     */
    private Integer isSync;

    /**
     * 是否招标(1=是，2=否)
     */
    private Integer isBidInviting;

    /**
     * 审批结果
     */
    private String approveResult;

    /**
     * 是否终止(1=是，2=否)
     */
    private Integer isTermination;

    /**
     * 项目基本情况
     **/
    private AppNgBase appNgBase;

    /**
     * 是否老项目 1是 2否
     */
    private int isOldPro;

    /**
     * 材料使用情况
     **/
    private List<AppNgMaterial> appNgMaterialList;

    /**
     * 施工费情况
     **/
    private List<AppNgConst> appNgConstList;

    /**
     * 事前审批对象
     **/
    private AppxNgReq appxNgReq;

    public int getIsOldPro() {
        return isOldPro;
    }

    public void setIsOldPro(int isOldPro) {
        this.isOldPro = isOldPro;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProName() {
        return proName;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProType() {
        return proType;
    }

    public void setProTypeName(String proTypeName) {
        this.proTypeName = proTypeName;
    }

    public String getProTypeName() {
        return proTypeName;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    public BigDecimal getContractAmountTotal() {
        return contractAmountTotal;
    }

    public void setContractAmountTotal(BigDecimal contractAmountTotal) {
        this.contractAmountTotal = contractAmountTotal;
    }

    public BigDecimal getContractAmountTax() {
        return contractAmountTax;
    }

    public void setContractAmountTax(BigDecimal contractAmountTax) {
        this.contractAmountTax = contractAmountTax;
    }

    public BigDecimal getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(BigDecimal taxrate) {
        this.taxrate = taxrate;
    }

    public BigDecimal getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(BigDecimal taxamount) {
        this.taxamount = taxamount;
    }

    public void setBelongCompanyid(String belongCompanyid) {
        this.belongCompanyid = belongCompanyid;
    }

    public String getBelongCompanyid() {
        return belongCompanyid;
    }

    public void setBelongCompanyname(String belongCompanyname) {
        this.belongCompanyname = belongCompanyname;
    }

    public String getBelongCompanyname() {
        return belongCompanyname;
    }

    public void setBelongDeptid(String belongDeptid) {
        this.belongDeptid = belongDeptid;
    }

    public String getBelongDeptid() {
        return belongDeptid;
    }

    public void setBelongDeptname(String belongDeptname) {
        this.belongDeptname = belongDeptname;
    }

    public String getBelongDeptname() {
        return belongDeptname;
    }

    public void setReqReason(String reqReason) {
        this.reqReason = reqReason;
    }

    public String getReqReason() {
        return reqReason;
    }

    public void setExplainInfo(String explainInfo) {
        this.explainInfo = explainInfo;
    }

    public String getExplainInfo() {
        return explainInfo;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getFiles() {
        return files;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatusPs(String statusPs) {
        this.statusPs = statusPs;
    }

    public String getStatusPs() {
        return statusPs;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getIsSync() {
        return isSync;
    }

    public void setIsSync(Integer isSync) {
        this.isSync = isSync;
    }

    public Integer getIsBidInviting() {
        return isBidInviting;
    }

    public void setIsBidInviting(Integer isBidInviting) {
        this.isBidInviting = isBidInviting;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public Integer getIsTermination() {
        return isTermination;
    }

    public void setIsTermination(Integer isTermination) {
        this.isTermination = isTermination;
    }

    public AppNgBase getAppNgBase() {
        return appNgBase;
    }

    public void setAppNgBase(AppNgBase appNgBase) {
        this.appNgBase = appNgBase;
    }

    public List<AppNgMaterial> getAppNgMaterialList() {
        return appNgMaterialList;
    }

    public void setAppNgMaterialList(List<AppNgMaterial> appNgMaterialList) {
        this.appNgMaterialList = appNgMaterialList;
    }

    public List<AppNgConst> getAppNgConstList() {
        return appNgConstList;
    }

    public void setAppNgConstList(List<AppNgConst> appNgConstList) {
        this.appNgConstList = appNgConstList;
    }

    public AppxNgReq getAppxNgReq() {
        return appxNgReq;
    }

    public void setAppxNgReq(AppxNgReq appxNgReq) {
        this.appxNgReq = appxNgReq;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
                .append("createBy", getCreateBy()).append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("reqNo", getReqNo())
                .append("proName", getProName()).append("proType", getProType()).append("proTypeName", getProTypeName())
                .append("classification", getClassification()).append("contractAmountTotal", getContractAmountTotal())
                .append("contractAmountTax", getContractAmountTax()).append("taxrate", getTaxrate())
                .append("taxamount", getTaxamount()).append("belongCompanyid", getBelongCompanyid())
                .append("belongCompanyname", getBelongCompanyname()).append("belongDeptid", getBelongDeptid())
                .append("belongDeptname", getBelongDeptname()).append("reqReason", getReqReason())
                .append("explainInfo", getExplainInfo()).append("files", getFiles()).append("status", getStatus())
                .append("statusPs", getStatusPs()).append("remarks", getRemarks()).append("delFlag", getDelFlag())
                .toString();
    }
}