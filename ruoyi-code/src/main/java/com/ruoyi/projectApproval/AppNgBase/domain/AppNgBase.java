package com.ruoyi.projectApproval.AppNgBase.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Title: AppNgBase.java
 * @Description: 事前审批明细(集客类 - 项目基本情况)
 * @Author M.Mu
 * @Date 2020-12-09
 */
public class AppNgBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    private String cid;
    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 申请编号
     */
    @Excel(name = "申请编号")
    private String bid;

    /**
     * 涉及分公司编号
     */
    @Excel(name = "涉及分公司编号")
    private String branchInvolvedId;

    private Integer[] branIdStr;

    /**
     * 涉及分公司名称
     */
    @Excel(name = "涉及分公司名称")
    private String branchInvolvedName;

    /**
     * 公司负责人
     */
    @Excel(name = "公司负责人")
    private String companyCharge;

    /**
     * 部门负责人
     */
    @Excel(name = "部门负责人")
    private String departCharge;

    /**
     * 项目负责人
     */
    @Excel(name = "项目负责人")
    private String proCharge;

    /**
     * 工程项目类别编号
     */
    @Excel(name = "工程项目类别编号")
    private String projectTypeId;

    /**
     * 工程项目类别名称
     */
    @Excel(name = "工程项目类别名称")
    private String projectTypeName;

    /**
     * 建筑物数量
     */
    @Excel(name = "建筑物数量")
    private Integer buildingsNumber;

    /**
     * 涉及建筑户数
     */
    @Excel(name = "涉及建筑户数")
    private Integer buildingsInvolvedNumber;

    /**
     * 数字电视用户数量
     */
    @Excel(name = "数字电视用户数量")
    private Integer digitaltvNumber;

    /**
     * 宽带用户数量
     */
    @Excel(name = "宽带用户数量")
    private Integer broadbandNumber;

    /**
     * 材料预算
     */
    @Excel(name = "材料预算")
    private BigDecimal budgetMaterial;

    /**
     * 人工预算
     */
    @Excel(name = "人工预算")
    private BigDecimal budgetLabor;

    /**
     * 预算总额
     */
    @Excel(name = "预算总额")
    private BigDecimal budgetTotal;

    /**
     * 设计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "设计时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date designTime;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dmissionTime;

    /**
     * 完工时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完工时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;

    /**
     * 备注信息
     */
    @Excel(name = "备注信息")
    private String remarks;

    /**
     * 删除标记
     */
    private Integer delFlag;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBid() {
        return bid;
    }

    public void setBranchInvolvedId(String branchInvolvedId) {
        this.branchInvolvedId = branchInvolvedId;
    }

    public String getBranchInvolvedId() {
        return branchInvolvedId;
    }

    public Integer[] getBranIdStr() {
        return branIdStr;
    }

    public void setBranIdStr(Integer[] branIdStr) {
        this.branIdStr = branIdStr;
    }

    public void setBranchInvolvedName(String branchInvolvedName) {
        this.branchInvolvedName = branchInvolvedName;
    }

    public String getBranchInvolvedName() {
        return branchInvolvedName;
    }

    public void setCompanyCharge(String companyCharge) {
        this.companyCharge = companyCharge;
    }

    public String getCompanyCharge() {
        return companyCharge;
    }

    public void setDepartCharge(String departCharge) {
        this.departCharge = departCharge;
    }

    public String getDepartCharge() {
        return departCharge;
    }

    public void setProCharge(String proCharge) {
        this.proCharge = proCharge;
    }

    public String getProCharge() {
        return proCharge;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setBuildingsNumber(Integer buildingsNumber) {
        this.buildingsNumber = buildingsNumber;
    }

    public Integer getBuildingsNumber() {
        return buildingsNumber;
    }

    public void setBuildingsInvolvedNumber(Integer buildingsInvolvedNumber) {
        this.buildingsInvolvedNumber = buildingsInvolvedNumber;
    }

    public Integer getBuildingsInvolvedNumber() {
        return buildingsInvolvedNumber;
    }

    public void setDigitaltvNumber(Integer digitaltvNumber) {
        this.digitaltvNumber = digitaltvNumber;
    }

    public Integer getDigitaltvNumber() {
        return digitaltvNumber;
    }

    public void setBroadbandNumber(Integer broadbandNumber) {
        this.broadbandNumber = broadbandNumber;
    }

    public Integer getBroadbandNumber() {
        return broadbandNumber;
    }

    public BigDecimal getBudgetMaterial() {
        return budgetMaterial;
    }

    public void setBudgetMaterial(BigDecimal budgetMaterial) {
        this.budgetMaterial = budgetMaterial;
    }

    public BigDecimal getBudgetLabor() {
        return budgetLabor;
    }

    public void setBudgetLabor(BigDecimal budgetLabor) {
        this.budgetLabor = budgetLabor;
    }

    public BigDecimal getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(BigDecimal budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public void setDesignTime(Date designTime) {
        this.designTime = designTime;
    }

    public Date getDesignTime() {
        return designTime;
    }

    public void setDmissionTime(Date dmissionTime) {
        this.dmissionTime = dmissionTime;
    }

    public Date getDmissionTime() {
        return dmissionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public Date getCompletionTime() {
        return completionTime;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
                .append("createBy", getCreateBy()).append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("bid", getBid())
                .append("branchInvolvedId", getBranchInvolvedId()).append("branchInvolvedName", getBranchInvolvedName())
                .append("companyCharge", getCompanyCharge()).append("departCharge", getDepartCharge())
                .append("proCharge", getProCharge()).append("projectTypeId", getProjectTypeId())
                .append("projectTypeName", getProjectTypeName()).append("buildingsNumber", getBuildingsNumber())
                .append("buildingsInvolvedNumber", getBuildingsInvolvedNumber())
                .append("digitaltvNumber", getDigitaltvNumber()).append("broadbandNumber", getBroadbandNumber())
                .append("budgetMaterial", getBudgetMaterial()).append("budgetLabor", getBudgetLabor())
                .append("budgetTotal", getBudgetTotal()).append("designTime", getDesignTime())
                .append("dmissionTime", getDmissionTime()).append("completionTime", getCompletionTime())
                .append("remarks", getRemarks()).append("delFlag", getDelFlag()).toString();
    }
}
