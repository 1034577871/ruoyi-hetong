package com.ruoyi.contract.contractInfo.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import cn.hutool.json.JSONUtil;
import com.ruoyi.base.fileManage.domain.CtBuFile;
import com.ruoyi.base.fileManage.mapper.CtBuFileMapper;
import com.ruoyi.base.fileManage.service.ICtBuFileService;
import com.ruoyi.collection.utils.ActUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.contract.contractIncomeType.domain.CtBuContractIncomeType;
import com.ruoyi.contract.contractIncomeType.mapper.CtBuContractIncomeTypeMapper;
import com.ruoyi.contract.payinfoInvoice.mapper.CtBuPayinfoInvoiceMapper;
import com.ruoyi.mq.bean.SysConstant;
import com.ruoyi.mq.service.MsgService;
import com.ruoyi.projectApproval.AppNgc.mapper.AppNgcMapper;
import com.ruoyi.projectApproval.ApprovalNg.mapper.CtBuApprovalNgMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.contract.contractContent.domain.CtBuContractContent;
import com.ruoyi.contract.contractContent.mapper.CtBuContractContentMapper;
import com.ruoyi.contract.contractInfo.domain.CtBuContract;
import com.ruoyi.contract.contractInfo.mapper.CtBuContractMapper;
import com.ruoyi.contract.contractInfo.service.ICtBuContractService;
import com.ruoyi.contract.contractPayinfo.domain.CtBuContractPayinfo;
import com.ruoyi.contract.contractPayinfo.mapper.CtBuContractPayinfoMapper;
import com.ruoyi.contract.contractWarning.domain.ContractWarning;
import com.ruoyi.contract.contractWarning.mapper.ContractWarningMapper;
import com.ruoyi.paymentManagement.ticketRecord.domain.TicketRecord;
import com.ruoyi.paymentManagement.ticketRecord.service.ITicketRecordService;
import com.ruoyi.system.mapper.SysDictDataMapper;

/**
 * 合同信息录入主Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-08
 */
@Service
public class CtBuContractServiceImpl implements ICtBuContractService {
    @Autowired
    private CtBuContractMapper ctBuContractMapper;

    @Autowired
    private CtBuContractContentMapper ctBuContractContentMapper;

    @Autowired
    private CtBuContractPayinfoMapper ctBuContractPayinfoMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private ContractWarningMapper contractWarningMapper;

    @Autowired
    private ITicketRecordService ticketRecordService;

    @Autowired
    private ICtBuFileService fileService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private CtBuFileMapper ctBuFileMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private AppNgcMapper appNgcMapper;

    @Autowired
    private CtBuApprovalNgMapper ctBuApprovalNgMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private CtBuPayinfoInvoiceMapper ctBuPayinfoInvoiceMapper;

    @Autowired
    private CtBuContractIncomeTypeMapper ctBuContractIncomeTypeMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;


    /**
     * 查询合同信息录入主
     *
     * @param id 合同信息录入主ID
     * @return 合同信息录入主
     */
    @Override
    public CtBuContract selectCtBuContractById(String id) {
        CtBuContract ctBuContract = ctBuContractMapper.selectCtBuContractById(id);
        if (ctBuContract != null && ctBuContract.getContractNo() != null) {

            //合同支付
            List<CtBuContractPayinfo> ctBuContractPayinfos = ctBuContractPayinfoMapper.selectCtBuContractPayinfoByContractId(ctBuContract.getId());
            if (ctBuContractPayinfos != null) {
                for (CtBuContractPayinfo cbcp : ctBuContractPayinfos) {
                    Map<String, String> stringStringMap = ctBuPayinfoInvoiceMapper.selectPayinfoInvoiceContractIdConcat(cbcp.getId());
                    if (stringStringMap != null) {
                        if (stringStringMap.get("groupConcatInvoiceNo") != null) {
                            cbcp.setInvoiceId(stringStringMap.get("groupConcatInvoiceNo"));
                        }
                        if (stringStringMap.get("groupConcatInvoiceCode") != null) {
                            cbcp.setInvoiceCode(stringStringMap.get("groupConcatInvoiceCode"));
                        }
                        if (stringStringMap.get("sumInvoiceMoney") != null) {
                            cbcp.setInvoiceMoney(String.valueOf(stringStringMap.get("sumInvoiceMoney")));
                        }
                        if (stringStringMap.get("groupConcatInvoiceTime") != null) {
                            cbcp.setInvoiceTime(stringStringMap.get("groupConcatInvoiceTime"));
                        }

                    }

                }
                ctBuContract.setCtBuContractPayinfoList(ctBuContractPayinfos);
            } else {
                ArrayList<CtBuContractPayinfo> ctBuContractPayinfo = new ArrayList<>();
                ctBuContract.setCtBuContractPayinfoList(ctBuContractPayinfo);
            }

            //合同内容
            List<CtBuContractContent> ctBuContractContents = ctBuContractContentMapper.selectCtBuContractContentByContractId(ctBuContract.getId());
            if (ctBuContractContents != null) {
                ctBuContract.setCtBuContractContentList(ctBuContractContents);
            } else {
                ArrayList<CtBuContractContent> ctBuContractContent = new ArrayList<>();
                ctBuContract.setCtBuContractContentList(ctBuContractContent);
            }

            //合同收入类型
            List<CtBuContractIncomeType> ctBuContractIncomeTypes = ctBuContractIncomeTypeMapper.selectCtBuContractIncomeTypeByContractIdList(ctBuContract.getId());
            if (ctBuContractIncomeTypes != null) {
                ctBuContract.setCtBuContractIncomeTypeList(ctBuContractIncomeTypes);
            } else {
                ArrayList<CtBuContractIncomeType> setCtBuContractIncomeTypeList = new ArrayList<>();
                ctBuContract.setCtBuContractIncomeTypeList(setCtBuContractIncomeTypeList);
            }

            return ctBuContract;
        }
        return new CtBuContract();

    }

    /**
     * 查询合同信息录入主列表与金额统计
     *
     * @param ctBuContract 合同信息录入主
     * @return 合同信息录入主
     */
    @Override
    public List<Object> selectCtBuContractListSum(CtBuContract ctBuContract) {

        //合同信息列表
        List<CtBuContract> ctBuContracts = ctBuContractMapper.selectCtBuContractList(ctBuContract);
        //金额统计
        HashMap<String, BigDecimal> hashMap = ctBuContractMapper.selectCtBuContractSum(ctBuContract);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(ctBuContracts);
        objects.add(hashMap);
        return objects;
    }


    /**
     * 查询合同信息金额统计
     *
     * @param ctBuContract 合同信息录入主
     * @return 合同信息录入主
     */
    @Override
    public Map<String, BigDecimal> selectCtBuContractSum(CtBuContract ctBuContract) {
        //金额统计
        return ctBuContractMapper.selectCtBuContractSum(ctBuContract);
    }


    /**
     * 查询合同信息录入主列表
     *
     * @param ctBuContract 合同信息录入主
     * @return 合同信息录入主
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<CtBuContract> selectCtBuContractList(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList(ctBuContract);
    }

    /**
     * 查询合同信息录入主列表
     *
     * @param ctBuContract 合同信息录入主
     * @return 合同信息录入主
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<CtBuContract> selectCtBuContractList1(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList1(ctBuContract);
    }

    /**
     * 查询合同信息录入主列表
     *
     * @param ctBuContract 合同信息录入主
     * @return 合同信息录入主
     */
    @Override
    public List<CtBuContract> selectCtBuContractList2(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList2(ctBuContract);
    }

    /**
     * 新增合同信息录入主
     *
     * @param ctBuContract 合同信息录入主
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCtBuContract(CtBuContract ctBuContract) {

        /** 判断当前登录人 **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            ctBuContract.setCreateBy(loginUser.getUser().getUserId().toString());
        }

        String s = UUID.fastUUID().toString(true);
        ctBuContract.setId(s);                              //Id
        ctBuContract.setCreateTime(DateUtils.getNowDate());//创建时间
        ctBuContract.setDelFlag(1);                   //删除标志（1未删除 2已删除）
        ctBuContract.setIsArchive(2);                 //是否归档（1已归档，2未归档）
        if (ctBuContract.getOriginalId() == null || ctBuContract.getOriginalId() == "") {
            ctBuContract.setOutstandingAmount(ctBuContract.getTotalMoney());            //未收金额
            ctBuContract.setNwriteOffAmount(ctBuContract.getTotalMoney());              //未核销金额
        }


        //获取当前年月
        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyyMM");
        String date = matter.format(dt);

        //月份
        SimpleDateFormat matter1 = new SimpleDateFormat("MM");
        String date1 = matter1.format(dt);
        ctBuContract.setMonth(date1);

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("GDJ").append(date);

        //获取最新合同编码
//        String s1 = ctBuContractMapper.newestContractNo(stringBuilder + "");

        //生成合同编号
//        if (s1 == null) {
//            ctBuContract.setContractNo(stringBuilder.append("001") + "");
//        } else {
//            int i = Integer.parseInt(s1.substring(s1.length() - 3, s1.length())) + 1;
//            DecimalFormat decimalFormat = new DecimalFormat("000");
//            String format = decimalFormat.format(i);
//            String strm = s1.substring(0, s1.length() - 3);
//            ctBuContract.setContractNo(strm + format);
//        }

        //计算合同服务截止日期
        if (ctBuContract.getContractServeStart() != null && ctBuContract.getServiceTerm() != null) {
            LocalDate localDate = ctBuContract.getContractServeStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate1 = localDate.plusMonths(ctBuContract.getServiceTerm());
            ctBuContract.setContractServeEnd(Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        if (ctBuContract.getContractCategory() != null){
            String contract_category_type = sysDictDataMapper.selectDictLabel("contract_category_type", ctBuContract.getContractCategory());
            ctBuContract.setContractCategoryName(contract_category_type);
        }

        //合同收入类型
        List<CtBuContractIncomeType> ctBuContractIncomeTypeList = ctBuContract.getCtBuContractIncomeTypeList();
        if (ctBuContractIncomeTypeList != null) {
            for (CtBuContractIncomeType cbcit : ctBuContractIncomeTypeList) {
                cbcit.setId(UUID.fastUUID().toString(true));          //主键ID
                cbcit.setContractId(ctBuContract.getId());            //合同Id
                cbcit.setCreateBy(loginUser.getUser().getUserId().toString());              //创建人
                cbcit.setCreateTime(ctBuContract.getCreateTime());            //创建日期
                cbcit.setDelFlag(1);                                         //删除标志 1未删除2已删除

                //新增合同付款表
                ctBuContractIncomeTypeMapper.insertCtBuContractIncomeType(cbcit);
            }
        }

        //合同付款表
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();

        if (ctBuContractPayinfoList != null) {
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setId(UUID.fastUUID().toString(true));          //主键ID
                cbp.setContractId(ctBuContract.getId());            //合同Id
                cbp.setCreateBy(loginUser.getUser().getUserId().toString());              //创建人
                cbp.setCreateTime(ctBuContract.getCreateTime());            //创建日期
                cbp.setDelFlag(1);                                         //删除标志 1未删除2已删除

                //新增合同付款表
                ctBuContractPayinfoMapper.insertCtBuContractPayinfo(cbp);
            }
        }


        //合同内容表
        List<CtBuContractContent> ctBuContractContentList = ctBuContract.getCtBuContractContentList();
        if (ctBuContractContentList != null) {
            for (CtBuContractContent cc : ctBuContractContentList) {
                String contentTypeIncome = sysDictDataMapper.selectDictLabel("content_type_income", cc.getContentType());
                String contentTypeExpend = sysDictDataMapper.selectDictLabel("content_type_expend", cc.getContentType());
                if (contentTypeIncome != null){
                    cc.setContentName(contentTypeIncome);
                } else if (contentTypeExpend != null){
                    cc.setContentName(contentTypeExpend);
                }
                cc.setId(UUID.fastUUID().toString(true));
                cc.setCreateBy(loginUser.getUser().getUserId().toString());              //创建人
                cc.setCreateTime(ctBuContract.getCreateTime());            //创建日期
                cc.setContractId(ctBuContract.getId());            //合同ID
                cc.setDelFlag(1);             //删除标志

                //新增合同内容
                ctBuContractContentMapper.insertCtBuContractContent(cc);
            }
        }


        //变更操作  删除变更前合同信息
        if (ctBuContract.getOriginalId() != null) {
//            CtBuContract ctBuContract1 = ctBuContractMapper.selectCtBuContractById(ctBuContract.getOriginalId());
            int i1 = ctBuContractMapper.updateCtBuContractById(ctBuContract.getOriginalId());
            if (i1 > 0) {
                CtBuFile file = new CtBuFile();
                file.setPid(ctBuContract.getOriginalId());
                List<CtBuFile> ctBuFiles = ctBuFileMapper.findFileListByPid(file);
                if (ctBuFiles != null && ctBuFiles.size() != 0) {
                    for (CtBuFile cbf : ctBuFiles) {
                        CtBuFile ctBuFile = new CtBuFile();
                        ctBuFile.setId(UUID.fastUUID().toString(true));
                        ctBuFile.setPid(ctBuContract.getId());
                        ctBuFile.setPtype(cbf.getPtype());
                        ctBuFile.setDelFlag(1);
                        ctBuFile.setPath(cbf.getPath());
                        ctBuFile.setCreateBy(cbf.getCreateBy());
                        ctBuFile.setCreateTime(new Date());
                        ctBuFileMapper.insertCtBuFile(ctBuFile);
                        ctBuFileMapper.deleteCtBuFileById(cbf.getId());
                    }
                }
                ctBuContractContentMapper.updateCtBuContractContentByContractId(ctBuContract.getOriginalId());
                ctBuContractPayinfoMapper.updateCtBuContractPayinfoByContractId(ctBuContract.getOriginalId());
            }
        }


        /** 处理文件 **/
        //合同归档资料
        if (StringUtils.isNotBlank(ctBuContract.getArchiveFiles())) {
            String[] str = ctBuContract.getArchiveFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //合同归档资料 ptype:1
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //合同扫描件
        if (StringUtils.isNotBlank(ctBuContract.getContractScanningFile())) {
            String[] str = ctBuContract.getContractScanningFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(2);   //合同扫描件 ptype:2
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //合同签订审批流程附件
        if (StringUtils.isNotBlank(ctBuContract.getContractApprovalFile())) {
            String[] str = ctBuContract.getContractApprovalFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(3);   //合同签订审批流程附件 ptype:3
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //其他附件
        if (StringUtils.isNotBlank(ctBuContract.getOtherFiles())) {
            String[] str = ctBuContract.getOtherFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(4);   //其他附件 ptype:4
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //清除archiveFiles字段
        ctBuContract.setArchiveFiles(null);
        //清除contractScanningFile字段
        ctBuContract.setContractScanningFile(null);
        //清除contractApprovalFile字段
        ctBuContract.setContractApprovalFile(null);
        //清除otherFiles字段
        ctBuContract.setOtherFiles(null);

        String widthName = "";
        if (ctBuContract.getStatus() == 0) {
            ctBuContract.setStatusPs("保存");
        } else if (ctBuContract.getStatus() == 1) {
            if (ctBuContract.getIsOldCon() == 2){
                ctBuContract.setStatusPs("审核中");
                //获取当前登陆人的所属部门
                long dept_id = loginUser.getUser().getDept().getDeptId();
                Map<String, Object> variables = this.getVarirs(dept_id);
                variables.put("apply", loginUser.getUsername());
                widthName = "合同审核-" + loginUser.getUser().getNickName();
                String key = "contractAudit";
                ctBuContract.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuContract.getId(), variables
                        , taskService, taskRuntime, key, widthName));
            } else if (ctBuContract.getIsOldCon() == 1){
                ctBuContract.setStatus(2);
                ctBuContract.setStatusPs("审核通过");
            }

        }
        int i = ctBuContractMapper.insertCtBuContract(ctBuContract);
        if (i > 0) {
            if (ctBuContract.getStatus() == 1) {
                // 根据流程实例Id查询任务
                Map map = new HashMap();
                map.put("instanceId", ctBuContract.getInstanceId());
                map.put("name", widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE, SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map), 0);//30秒钟后执行
            }
        }

        return i;
    }

    //获取组人员
    public Map<String, Object> getVarirs(long dept_id) {
        Map<String, Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();
        //
        variables.put("ministryOfLaw", ActUtils.getVarirs(null, "107", iSysUserService));
        return variables;
    }

    /**
     * 修改合同信息录入主
     *
     * @param ctBuContract 合同信息录入主
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCtBuContract(CtBuContract ctBuContract) {
        /** 判断当前登录人 **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            ctBuContract.setUpdateBy(loginUser.getUser().getUserId().toString());
        }

        ctBuContract.setUpdateTime(DateUtils.getNowDate());
        ctBuContract.setDelFlag(1);                   //删除标志（1未删除 2已删除）

        //计算合同服务截止日期
        if (ctBuContract.getContractServeStart() != null && ctBuContract.getServiceTerm() != null) {
            LocalDate localDate = ctBuContract.getContractServeStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate1 = localDate.plusMonths(ctBuContract.getServiceTerm());
            ctBuContract.setContractServeEnd(Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (ctBuContract.getContractCategory() != null){
            String contract_category_type = sysDictDataMapper.selectDictLabel("contract_category_type", ctBuContract.getContractCategory());
            ctBuContract.setContractCategoryName(contract_category_type);
        }

        //合同收入类型
        List<CtBuContractIncomeType> ctBuContractIncomeTypeList = ctBuContract.getCtBuContractIncomeTypeList();
        if (ctBuContractIncomeTypeList != null) {
            for (CtBuContractIncomeType cbcit : ctBuContractIncomeTypeList) {
                cbcit.setContractId(ctBuContract.getId());            //合同Id
                cbcit.setDelFlag(1);                                         //删除标志 1未删除2已删除
                if (cbcit.getId() != null) {
                    cbcit.setUpdateBy(loginUser.getUser().getUserId().toString());              //更新人
                    cbcit.setUpdateTime(ctBuContract.getUpdateTime());            //更新日期
                    ctBuContractIncomeTypeMapper.updateCtBuContractIncomeType(cbcit);
                } else {
                    cbcit.setId(UUID.fastUUID().toString(true));
                    cbcit.setCreateBy(loginUser.getUser().getUserId().toString());
                    cbcit.setCreateTime(DateUtils.getNowDate());
                    ctBuContractIncomeTypeMapper.insertCtBuContractIncomeType(cbcit);
                }
            }
        }


        //合同付款表
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();
        if (ctBuContractPayinfoList != null) {
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setContractId(ctBuContract.getId());            //合同id
                cbp.setDelFlag(1);                                         //删除标志 1未删除2已删除

                if (cbp.getId() != null) {
                    cbp.setUpdateBy(loginUser.getUser().getUserId().toString());              //更新人
                    cbp.setUpdateTime(ctBuContract.getUpdateTime());            //更新日期
                    //合同付款表
                    ctBuContractPayinfoMapper.updateCtBuContractPayinfo(cbp);
                } else {
                    cbp.setId(UUID.fastUUID().toString(true));
                    cbp.setCreateBy(loginUser.getUser().getUserId().toString());
                    cbp.setCreateTime(DateUtils.getNowDate());
                    ctBuContractPayinfoMapper.insertCtBuContractPayinfo(cbp);
                }
            }
        }


        //合同内容表
        List<CtBuContractContent> ctBuContractContentList = ctBuContract.getCtBuContractContentList();
        if (ctBuContractContentList != null) {
            for (CtBuContractContent cc : ctBuContractContentList) {
                String contentTypeIncome = sysDictDataMapper.selectDictLabel("content_type_income", cc.getContentType());
                String contentTypeExpend = sysDictDataMapper.selectDictLabel("content_type_expend", cc.getContentType());
                if (contentTypeIncome != null){
                    cc.setContentName(contentTypeIncome);
                } else if (contentTypeExpend != null){
                    cc.setContentName(contentTypeExpend);
                }
                cc.setContractId(ctBuContract.getId());            //合同ID
                cc.setDelFlag(1);             //删除标志

                if (cc.getId() != null) {
                    cc.setUpdateBy(loginUser.getUser().getUserId().toString());              //更新人
                    cc.setUpdateTime(ctBuContract.getUpdateTime());            //更新日期
                    //修改合同内容
                    ctBuContractContentMapper.updateCtBuContractContent(cc);
                } else {
                    cc.setId(UUID.fastUUID().toString(true));
                    cc.setCreateBy(loginUser.getUser().getUserId().toString());
                    cc.setCreateTime(DateUtils.getNowDate());
                    //新增合同内容
                    ctBuContractContentMapper.insertCtBuContractContent(cc);
                }
            }
        }


        /** 处理文件 **/
        //合同归档资料
        if (StringUtils.isNotBlank(ctBuContract.getArchiveFiles())) {
            String[] str = ctBuContract.getArchiveFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //合同归档资料 ptype:1
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //合同扫描件
        if (StringUtils.isNotBlank(ctBuContract.getContractScanningFile())) {
            String[] str = ctBuContract.getContractScanningFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(2);   //合同扫描件 ptype:2
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //合同签订审批流程附件
        if (StringUtils.isNotBlank(ctBuContract.getContractApprovalFile())) {
            String[] str = ctBuContract.getContractApprovalFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(3);   //合同签订审批流程附件 ptype:3
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //其他附件
        if (StringUtils.isNotBlank(ctBuContract.getOtherFiles())) {
            String[] str = ctBuContract.getOtherFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(4);   //其他附件 ptype:4
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //清除archiveFiles字段
        ctBuContract.setArchiveFiles(null);
        //清除contractScanningFile字段
        ctBuContract.setContractScanningFile(null);
        //清除contractApprovalFile字段
        ctBuContract.setContractApprovalFile(null);
        //清除otherFiles字段
        ctBuContract.setOtherFiles(null);

        String widthName = "";
        if (ctBuContract.getStatus() == 0) {
            ctBuContract.setStatusPs("保存");
        } else if (ctBuContract.getStatus() == 1) {
            if (ctBuContract.getIsOldCon() == 2){
                ctBuContract.setStatusPs("审核中");
                //获取当前登陆人的所属部门
                long dept_id = loginUser.getUser().getDept().getDeptId();
                Map<String, Object> variables = this.getVarirs(dept_id);
                variables.put("apply", loginUser.getUsername());
                widthName = "合同审核-" + loginUser.getUser().getNickName();
                String key = "contractAudit";
//            ctBuContract.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuContract.getId(), variables
//                    , taskService, taskRuntime, key, widthName));

                if (ctBuContract.getInstanceId() != null && !"".equals(ctBuContract.getInstanceId())) {
                    ctBuContract.setInstanceId(ActUtils.submitTask(runtimeService, processRuntime, loginUser, ctBuContract.getInstanceId(), variables
                            , taskService, taskRuntime, key, widthName, ctBuContract.getId()));
                } else {
                    ctBuContract.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuContract.getId(), variables
                            , taskService, taskRuntime, key, widthName));
                }
            } else if (ctBuContract.getIsOldCon() == 1){
                ctBuContract.setStatus(2);
                ctBuContract.setStatusPs("审核通过");
            }

        }

        int i = ctBuContractMapper.updateCtBuContract(ctBuContract);
        if (i > 0) {
            if (ctBuContract.getStatus() == 1) {
                // 根据流程实例Id查询任务
                Map map = new HashMap();
                map.put("instanceId", ctBuContract.getInstanceId());
                map.put("name", widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE, SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map), 0);//30秒钟后执行
            }
        }


        return i;
    }

    /**
     * 批量删除合同信息录入主
     *
     * @param ids 需要删除的合同信息录入主ID
     * @return 结果
     */
    @Override
    public int deleteCtBuContractByIds(String[] ids) {
        return ctBuContractMapper.deleteCtBuContractByIds(ids);
    }

    /**
     * 删除合同信息录入主信息
     *
     * @param id 合同信息录入主ID
     * @return 结果
     */
    @Override
    public int deleteCtBuContractById(String id) {
        return ctBuContractMapper.deleteCtBuContractById(id);
    }

    /**
     * 批量逻辑删除合同信息录入主
     *
     * @param ids 需要删除的合同信息录入主ID
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCtBuContractByIds(String[] ids) {
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                CtBuContract ctBuContract = ctBuContractMapper.selectCtBuContractById(ids[i]);
                int i1 = ctBuContractMapper.updateCtBuContractById(ids[i]);
                if (i1 > 0) {
                    ctBuContractContentMapper.updateCtBuContractContentByContractId(ctBuContract.getId());
                    ctBuContractPayinfoMapper.updateCtBuContractPayinfoByContractId(ctBuContract.getId());
                }
            }
        }
        return 1;
    }

    /**
     * 查询需预警付款信息
     */
    @Override
    public void selectWarning() {

        //获取提前预警天数
        SysDictData sysDictData = sysDictDataMapper.selectDictDataById(146L);
        Integer dayNumber = Integer.valueOf(sysDictData.getDictValue().trim());

        //计算预警日期
        LocalDate localDate = LocalDate.now().plusDays(dayNumber);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        //查询需预警信息
        List<CtBuContract> ctBuContracts = ctBuContractMapper.selectWarning(date);
        if (ctBuContracts.size() > 0) {
            for (CtBuContract cbc : ctBuContracts) {
                ContractWarning contractWarning = new ContractWarning();
                contractWarning.setId(UUID.fastUUID().toString(true));
                contractWarning.setCreateTime(DateUtils.getNowDate());
                contractWarning.setContractId(cbc.getId());
                contractWarning.setContractNo(cbc.getContractNo());
                contractWarning.setContractName(cbc.getContractName());
                contractWarning.setManageDeptment(cbc.getManageDeptment());
                contractWarning.setManageUser(cbc.getManageUser());
                contractWarning.setEstimateMoney(cbc.getEstimateMoney());
                contractWarning.setEstimateTime(cbc.getEstimateTime());
                contractWarning.setDelFlag(1);
                if (cbc.getActualMoney() != null) {
                    contractWarning.setActualMoney(cbc.getActualMoney());
                }
                contractWarningMapper.insertContractWarning(contractWarning);

            }
        }
    }

    /**
     * 付款记录保存
     *
     * @param ctBuContract 付款记录
     * @return 付款记录
     */
    @Override
    public int saveTicketInfo(CtBuContract ctBuContract) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //合同付款表
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();
        if (ctBuContractPayinfoList != null) {
            TicketRecord ticketRecordObj = null;
            TicketRecord obj = null;
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setContractId(ctBuContract.getId());            //合同ID
                cbp.setUpdateBy(loginUser.getUser().getUserId().toString());              //更新人
                cbp.setUpdateTime(ctBuContract.getCreateTime());            //更新日期
                cbp.setDelFlag(1);                                         //删除标志 1未删除2已删除
                //合同付款表
                ctBuContractPayinfoMapper.updateCtBuContractPayinfo(cbp);
                //添加付款记录
                ticketRecordObj = new TicketRecord();
                ticketRecordObj.setProNo(ctBuContract.getProjectNo());//项目编号
                ticketRecordObj.setProName(ctBuContract.getProjectName());//项目名称
                ticketRecordObj.setContractId(ctBuContract.getId());//合同id
                ticketRecordObj.setContractNo(ctBuContract.getContractNo());//合同编号
                ticketRecordObj.setContractName(ctBuContract.getContractName());//合同名称
                ticketRecordObj.setPayinfoid(cbp.getId());
                ticketRecordObj.setPaymentPeriod(cbp.getEstimateTime());//付款周期             
//                ticketRecordObj.setInvoiceDate(cbp.getInvoiceTime());//开票日期
//                ticketRecordObj.setInvoiceCode(cbp.getInvoiceCode());//发票代码
//                ticketRecordObj.setInvoiceNo(cbp.getInvoiceId());// 发票号码
//                ticketRecordObj.setInvoiceMoney(cbp.getInvoiceMoney());//发票金额（含税）
                if (ctBuContract.getTaxrate() != null) {
                    ticketRecordObj.setTaxrate(ctBuContract.getTaxrate().longValue());//税率
                } else {
                    ticketRecordObj.setTaxrate(null);//税率
                }
//                ticketRecordObj.setInvoiceNomoney(cbp.getInvoiceNomoney());//发票金额（不含税）
                ticketRecordObj.setOppositeName(ctBuContract.getOppositeName());//对方单位名称
                ticketRecordObj.setOppositeBank(ctBuContract.getOppositeBank());//对方单位银行账户
                ticketRecordObj.setOppositeBankId(ctBuContract.getOppositeBankId());//对方单位银行账号
                ticketRecordObj.setOppositeContacts(ctBuContract.getOppositeContacts());
                ;//对方单位联系人
                ticketRecordObj.setOppositePhone(ctBuContract.getOppositePhone());// 对方单位联系电话
                ticketRecordObj.setOppositeAddress(ctBuContract.getOppositeAddress());//对方单位联系地址
                ticketRecordObj.setInvoiceVoucher(null);//发票凭证
                ticketRecordObj.setUnitQualification(null);//对方单位资质
                ticketRecordObj.setStatus(ctBuContract.getStatus().longValue());
                if (ctBuContract.getStatus() == 0) {
                    ticketRecordObj.setStatusps("保存");
                } else {
                    ticketRecordObj.setStatusps("审核中");
                }
                ticketRecordObj.setDelFlag("1");


                obj = new TicketRecord();
                obj.setPayinfoid(cbp.getId());
                obj.setDelFlag("1");
                List<TicketRecord> list = ticketRecordService.selectTicketRecordList(obj);
                if (list != null && list.size() > 0) {
                    obj = list.get(0);
                    ticketRecordObj.setId(obj.getId());
                    ticketRecordService.updateTicketRecord(ticketRecordObj);
                } else {
                    ticketRecordService.insertTicketRecord(ticketRecordObj);
                }
            }
        }
        return 1;
    }

    @Override
    public int updateCtBuContractByInstanceId(CtBuContract ctBuContract) {
        return ctBuContractMapper.updateCtBuContractByInstanceId(ctBuContract);
    }

    /**
     * 项目统计
     *
     * @return
     */
    @Override
    public List projectCount() {
        /** 判断当前登录人 **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
//            ctBuContract.setCreateBy(loginUser.getUser().getUserId().toString());
        }


        List<SysDept> sysDepts = sysDeptMapper.selectDeptByParentId(200L);
        ArrayList<HashMap> list = new ArrayList<>();
        if (sysDepts != null && sysDepts.size() != 0) {
            for (SysDept sd : sysDepts) {
                HashMap<String, Object> hashMap = new HashMap<>();
                if (sd.getDeptId() != null) {
                    //非集客项目统计
//                    int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId());
                    //集客立项统计
//                    int countApprovalNg = ctBuApprovalNgMapper.countApprovalNgByCompanyId(sd.getDeptId());
                    //合同签订数量
                    int countContract = ctBuContractMapper.countContractByCompany(sd.getDeptId());
                    //进行中合同数量
                    int countContractUnderway = ctBuContractMapper.countContractUnderwayByCompany(sd.getDeptId());
                    //未启动合同数量
                    int countContractUnStart = ctBuContractMapper.countContractUnStartByCompany(sd.getDeptId());
                    //完成合同数量
                    int countContractAccomplish = ctBuContractMapper.countContractAccomplishByCompany(sd.getDeptId());
                    hashMap.put("deptId", sd.getDeptId());
                    hashMap.put("deptName", sd.getDeptName());
//                    hashMap.put("countAppNgc", countAppNgc);
//                    hashMap.put("countApprovalNg", countApprovalNg);
                    hashMap.put("countContract", countContract);
                    hashMap.put("countContractUnderway", countContractUnderway);
                    hashMap.put("countContractUnStart", countContractUnStart);
                    hashMap.put("countContractAccomplish", countContractAccomplish);
                    list.add(hashMap);
                }
            }
        }
        return list;
    }


    @Override
    public List<CtBuContract> collectionAndPaymentStatistics(CtBuContract ctBuContract) {
        return ctBuContractMapper.collectionAndPaymentStatistics(ctBuContract);
    }

    @Override
    public List<CtBuContract> contractStatisticsList(String companyId, Integer type) {
        return ctBuContractMapper.contractStatisticsList(companyId, type);
    }

    @Override
    public List<HashMap<String, Object>> contractCount(Date beginTime, Date endTime) {
        /** 判断当前登录人 **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            // 管理员拥有所有权限
            if (loginUser.getUser().isAdmin()) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                List<SysDept> sysDepts = sysDeptMapper.selectDeptByParentId(200L);
                if (sysDepts != null && sysDepts.size() != 0) {
                    for (SysDept sd : sysDepts) {
                        if (sd.getDeptId() != null) {
                            HashMap<String, Object> stringIntegerHashMap = ctBuContractMapper.contractCount(sd.getDeptId(), beginTime, endTime);
                            //非集客项目统计
                            int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId(), beginTime, endTime);
                            //集客立项统计
                            int countApprovalNg = ctBuApprovalNgMapper.countApprovalNgByCompanyId(sd.getDeptId(), beginTime, endTime);
                            stringIntegerHashMap.put("companyId", sd.getDeptId());
                            stringIntegerHashMap.put("companyName", sd.getDeptName());
                            stringIntegerHashMap.put("countAppNgc", countAppNgc);
                            stringIntegerHashMap.put("countApprovalNg", countApprovalNg);
                            list.add(stringIntegerHashMap);
                        }
                    }
                }
                return list;
            } else {
                String s = sysRoleMapper.selectDataScopeByUserId(loginUser.getUser().getUserId());
                boolean status = s.contains("1");
                if (status){
                    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                    List<SysDept> sysDepts = sysDeptMapper.selectDeptByParentId(200L);
                    if (sysDepts != null && sysDepts.size() != 0) {
                        for (SysDept sd : sysDepts) {
                            if (sd.getDeptId() != null) {
                                HashMap<String, Object> stringIntegerHashMap = ctBuContractMapper.contractCount(sd.getDeptId(), beginTime, endTime);
                                //非集客项目统计
                                int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId(), beginTime, endTime);
                                //集客立项统计
                                int countApprovalNg = ctBuApprovalNgMapper.countApprovalNgByCompanyId(sd.getDeptId(), beginTime, endTime);
                                stringIntegerHashMap.put("companyId", sd.getDeptId());
                                stringIntegerHashMap.put("companyName", sd.getDeptName());
                                stringIntegerHashMap.put("countAppNgc", countAppNgc);
                                stringIntegerHashMap.put("countApprovalNg", countApprovalNg);
                                list.add(stringIntegerHashMap);
                            }
                        }
                    }
                    return list;
                }
                boolean status3 = s.contains("3");
                boolean status4 = s.contains("4");
                if (status3 || status4){
                    if (loginUser.getUser().getDept().getParentId() == 200){
                        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                        HashMap<String, Object> stringIntegerHashMap = ctBuContractMapper.contractCount(loginUser.getUser().getDeptId(), beginTime, endTime);
                        //非集客项目统计
                        int countAppNgc = appNgcMapper.countAppNgcByCompanyId(loginUser.getUser().getDeptId(), beginTime, endTime);
                        //集客立项统计
                        int countApprovalNg = ctBuApprovalNgMapper.countApprovalNgByCompanyId(loginUser.getUser().getDeptId(), beginTime, endTime);
                        stringIntegerHashMap.put("companyId", loginUser.getUser().getDeptId());
                        stringIntegerHashMap.put("companyName", loginUser.getUser().getDept().getDeptName());
                        stringIntegerHashMap.put("countAppNgc", countAppNgc);
                        stringIntegerHashMap.put("countApprovalNg", countApprovalNg);
                        list.add(stringIntegerHashMap);
                        return list;
                    }

                }
            }

        }
        return new ArrayList<>();
    }


    @Override
    public List<HashMap<String, Object>> contractWarningList() {
        return ctBuContractMapper.contractWarningList();
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public HashMap<String, Object> countContractIndex(CtBuContract ctBuContract) {
        return ctBuContractMapper.countContractIndex(ctBuContract);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<HashMap<String, Object>> notReceivedWarning(CtBuContract ctBuContract) {
        return ctBuContractMapper.notReceivedWarning(ctBuContract);
    }

    /**
     * 合同归档
     * @param ids
     * @return
     */
    @Override
    public int updateIsArchiveByIds(String[] ids) {
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                int i2 = ctBuContractMapper.updateIsArchiveById(1,ids[i]);
            }
        }
        return 1;
    }


    /**
     * 合同归档
     * @param ids
     * @return
     */
    @Override
    public int updateCancelIsArchiveByIds(String[] ids) {
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                int i2 = ctBuContractMapper.updateIsArchiveById(2,ids[i]);
            }
        }
        return 1;
    }
}
