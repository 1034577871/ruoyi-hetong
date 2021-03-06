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
 * ?????????????????????Service???????????????
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
     * ???????????????????????????
     *
     * @param id ?????????????????????ID
     * @return ?????????????????????
     */
    @Override
    public CtBuContract selectCtBuContractById(String id) {
        CtBuContract ctBuContract = ctBuContractMapper.selectCtBuContractById(id);
        if (ctBuContract != null && ctBuContract.getContractNo() != null) {

            //????????????
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

            //????????????
            List<CtBuContractContent> ctBuContractContents = ctBuContractContentMapper.selectCtBuContractContentByContractId(ctBuContract.getId());
            if (ctBuContractContents != null) {
                ctBuContract.setCtBuContractContentList(ctBuContractContents);
            } else {
                ArrayList<CtBuContractContent> ctBuContractContent = new ArrayList<>();
                ctBuContract.setCtBuContractContentList(ctBuContractContent);
            }

            //??????????????????
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
     * ????????????????????????????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ?????????????????????
     */
    @Override
    public List<Object> selectCtBuContractListSum(CtBuContract ctBuContract) {

        //??????????????????
        List<CtBuContract> ctBuContracts = ctBuContractMapper.selectCtBuContractList(ctBuContract);
        //????????????
        HashMap<String, BigDecimal> hashMap = ctBuContractMapper.selectCtBuContractSum(ctBuContract);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(ctBuContracts);
        objects.add(hashMap);
        return objects;
    }


    /**
     * ??????????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ?????????????????????
     */
    @Override
    public Map<String, BigDecimal> selectCtBuContractSum(CtBuContract ctBuContract) {
        //????????????
        return ctBuContractMapper.selectCtBuContractSum(ctBuContract);
    }


    /**
     * ?????????????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ?????????????????????
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<CtBuContract> selectCtBuContractList(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList(ctBuContract);
    }

    /**
     * ?????????????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ?????????????????????
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<CtBuContract> selectCtBuContractList1(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList1(ctBuContract);
    }

    /**
     * ?????????????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ?????????????????????
     */
    @Override
    public List<CtBuContract> selectCtBuContractList2(CtBuContract ctBuContract) {
        return ctBuContractMapper.selectCtBuContractList2(ctBuContract);
    }

    /**
     * ???????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ??????
     */
    @Override
    @Transactional
    public int insertCtBuContract(CtBuContract ctBuContract) {

        /** ????????????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            ctBuContract.setCreateBy(loginUser.getUser().getUserId().toString());
        }

        String s = UUID.fastUUID().toString(true);
        ctBuContract.setId(s);                              //Id
        ctBuContract.setCreateTime(DateUtils.getNowDate());//????????????
        ctBuContract.setDelFlag(1);                   //???????????????1????????? 2????????????
        ctBuContract.setIsArchive(2);                 //???????????????1????????????2????????????
        if (ctBuContract.getOriginalId() == null || ctBuContract.getOriginalId() == "") {
            ctBuContract.setOutstandingAmount(ctBuContract.getTotalMoney());            //????????????
            ctBuContract.setNwriteOffAmount(ctBuContract.getTotalMoney());              //???????????????
        }


        //??????????????????
        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyyMM");
        String date = matter.format(dt);

        //??????
        SimpleDateFormat matter1 = new SimpleDateFormat("MM");
        String date1 = matter1.format(dt);
        ctBuContract.setMonth(date1);

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("GDJ").append(date);

        //????????????????????????
//        String s1 = ctBuContractMapper.newestContractNo(stringBuilder + "");

        //??????????????????
//        if (s1 == null) {
//            ctBuContract.setContractNo(stringBuilder.append("001") + "");
//        } else {
//            int i = Integer.parseInt(s1.substring(s1.length() - 3, s1.length())) + 1;
//            DecimalFormat decimalFormat = new DecimalFormat("000");
//            String format = decimalFormat.format(i);
//            String strm = s1.substring(0, s1.length() - 3);
//            ctBuContract.setContractNo(strm + format);
//        }

        //??????????????????????????????
        if (ctBuContract.getContractServeStart() != null && ctBuContract.getServiceTerm() != null) {
            LocalDate localDate = ctBuContract.getContractServeStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate1 = localDate.plusMonths(ctBuContract.getServiceTerm());
            ctBuContract.setContractServeEnd(Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        if (ctBuContract.getContractCategory() != null){
            String contract_category_type = sysDictDataMapper.selectDictLabel("contract_category_type", ctBuContract.getContractCategory());
            ctBuContract.setContractCategoryName(contract_category_type);
        }

        //??????????????????
        List<CtBuContractIncomeType> ctBuContractIncomeTypeList = ctBuContract.getCtBuContractIncomeTypeList();
        if (ctBuContractIncomeTypeList != null) {
            for (CtBuContractIncomeType cbcit : ctBuContractIncomeTypeList) {
                cbcit.setId(UUID.fastUUID().toString(true));          //??????ID
                cbcit.setContractId(ctBuContract.getId());            //??????Id
                cbcit.setCreateBy(loginUser.getUser().getUserId().toString());              //?????????
                cbcit.setCreateTime(ctBuContract.getCreateTime());            //????????????
                cbcit.setDelFlag(1);                                         //???????????? 1?????????2?????????

                //?????????????????????
                ctBuContractIncomeTypeMapper.insertCtBuContractIncomeType(cbcit);
            }
        }

        //???????????????
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();

        if (ctBuContractPayinfoList != null) {
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setId(UUID.fastUUID().toString(true));          //??????ID
                cbp.setContractId(ctBuContract.getId());            //??????Id
                cbp.setCreateBy(loginUser.getUser().getUserId().toString());              //?????????
                cbp.setCreateTime(ctBuContract.getCreateTime());            //????????????
                cbp.setDelFlag(1);                                         //???????????? 1?????????2?????????

                //?????????????????????
                ctBuContractPayinfoMapper.insertCtBuContractPayinfo(cbp);
            }
        }


        //???????????????
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
                cc.setCreateBy(loginUser.getUser().getUserId().toString());              //?????????
                cc.setCreateTime(ctBuContract.getCreateTime());            //????????????
                cc.setContractId(ctBuContract.getId());            //??????ID
                cc.setDelFlag(1);             //????????????

                //??????????????????
                ctBuContractContentMapper.insertCtBuContractContent(cc);
            }
        }


        //????????????  ???????????????????????????
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


        /** ???????????? **/
        //??????????????????
        if (StringUtils.isNotBlank(ctBuContract.getArchiveFiles())) {
            String[] str = ctBuContract.getArchiveFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //?????????????????? ptype:1
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //???????????????
        if (StringUtils.isNotBlank(ctBuContract.getContractScanningFile())) {
            String[] str = ctBuContract.getContractScanningFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(2);   //??????????????? ptype:2
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //??????????????????????????????
        if (StringUtils.isNotBlank(ctBuContract.getContractApprovalFile())) {
            String[] str = ctBuContract.getContractApprovalFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(3);   //?????????????????????????????? ptype:3
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //????????????
        if (StringUtils.isNotBlank(ctBuContract.getOtherFiles())) {
            String[] str = ctBuContract.getOtherFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(4);   //???????????? ptype:4
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //??????archiveFiles??????
        ctBuContract.setArchiveFiles(null);
        //??????contractScanningFile??????
        ctBuContract.setContractScanningFile(null);
        //??????contractApprovalFile??????
        ctBuContract.setContractApprovalFile(null);
        //??????otherFiles??????
        ctBuContract.setOtherFiles(null);

        String widthName = "";
        if (ctBuContract.getStatus() == 0) {
            ctBuContract.setStatusPs("??????");
        } else if (ctBuContract.getStatus() == 1) {
            if (ctBuContract.getIsOldCon() == 2){
                ctBuContract.setStatusPs("?????????");
                //????????????????????????????????????
                long dept_id = loginUser.getUser().getDept().getDeptId();
                Map<String, Object> variables = this.getVarirs(dept_id);
                variables.put("apply", loginUser.getUsername());
                widthName = "????????????-" + loginUser.getUser().getNickName();
                String key = "contractAudit";
                ctBuContract.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuContract.getId(), variables
                        , taskService, taskRuntime, key, widthName));
            } else if (ctBuContract.getIsOldCon() == 1){
                ctBuContract.setStatus(2);
                ctBuContract.setStatusPs("????????????");
            }

        }
        int i = ctBuContractMapper.insertCtBuContract(ctBuContract);
        if (i > 0) {
            if (ctBuContract.getStatus() == 1) {
                // ??????????????????Id????????????
                Map map = new HashMap();
                map.put("instanceId", ctBuContract.getInstanceId());
                map.put("name", widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE, SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map), 0);//30???????????????
            }
        }

        return i;
    }

    //???????????????
    public Map<String, Object> getVarirs(long dept_id) {
        Map<String, Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();
        //
        variables.put("ministryOfLaw", ActUtils.getVarirs(null, "107", iSysUserService));
        return variables;
    }

    /**
     * ???????????????????????????
     *
     * @param ctBuContract ?????????????????????
     * @return ??????
     */
    @Override
    @Transactional
    public int updateCtBuContract(CtBuContract ctBuContract) {
        /** ????????????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            ctBuContract.setUpdateBy(loginUser.getUser().getUserId().toString());
        }

        ctBuContract.setUpdateTime(DateUtils.getNowDate());
        ctBuContract.setDelFlag(1);                   //???????????????1????????? 2????????????

        //??????????????????????????????
        if (ctBuContract.getContractServeStart() != null && ctBuContract.getServiceTerm() != null) {
            LocalDate localDate = ctBuContract.getContractServeStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate1 = localDate.plusMonths(ctBuContract.getServiceTerm());
            ctBuContract.setContractServeEnd(Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (ctBuContract.getContractCategory() != null){
            String contract_category_type = sysDictDataMapper.selectDictLabel("contract_category_type", ctBuContract.getContractCategory());
            ctBuContract.setContractCategoryName(contract_category_type);
        }

        //??????????????????
        List<CtBuContractIncomeType> ctBuContractIncomeTypeList = ctBuContract.getCtBuContractIncomeTypeList();
        if (ctBuContractIncomeTypeList != null) {
            for (CtBuContractIncomeType cbcit : ctBuContractIncomeTypeList) {
                cbcit.setContractId(ctBuContract.getId());            //??????Id
                cbcit.setDelFlag(1);                                         //???????????? 1?????????2?????????
                if (cbcit.getId() != null) {
                    cbcit.setUpdateBy(loginUser.getUser().getUserId().toString());              //?????????
                    cbcit.setUpdateTime(ctBuContract.getUpdateTime());            //????????????
                    ctBuContractIncomeTypeMapper.updateCtBuContractIncomeType(cbcit);
                } else {
                    cbcit.setId(UUID.fastUUID().toString(true));
                    cbcit.setCreateBy(loginUser.getUser().getUserId().toString());
                    cbcit.setCreateTime(DateUtils.getNowDate());
                    ctBuContractIncomeTypeMapper.insertCtBuContractIncomeType(cbcit);
                }
            }
        }


        //???????????????
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();
        if (ctBuContractPayinfoList != null) {
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setContractId(ctBuContract.getId());            //??????id
                cbp.setDelFlag(1);                                         //???????????? 1?????????2?????????

                if (cbp.getId() != null) {
                    cbp.setUpdateBy(loginUser.getUser().getUserId().toString());              //?????????
                    cbp.setUpdateTime(ctBuContract.getUpdateTime());            //????????????
                    //???????????????
                    ctBuContractPayinfoMapper.updateCtBuContractPayinfo(cbp);
                } else {
                    cbp.setId(UUID.fastUUID().toString(true));
                    cbp.setCreateBy(loginUser.getUser().getUserId().toString());
                    cbp.setCreateTime(DateUtils.getNowDate());
                    ctBuContractPayinfoMapper.insertCtBuContractPayinfo(cbp);
                }
            }
        }


        //???????????????
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
                cc.setContractId(ctBuContract.getId());            //??????ID
                cc.setDelFlag(1);             //????????????

                if (cc.getId() != null) {
                    cc.setUpdateBy(loginUser.getUser().getUserId().toString());              //?????????
                    cc.setUpdateTime(ctBuContract.getUpdateTime());            //????????????
                    //??????????????????
                    ctBuContractContentMapper.updateCtBuContractContent(cc);
                } else {
                    cc.setId(UUID.fastUUID().toString(true));
                    cc.setCreateBy(loginUser.getUser().getUserId().toString());
                    cc.setCreateTime(DateUtils.getNowDate());
                    //??????????????????
                    ctBuContractContentMapper.insertCtBuContractContent(cc);
                }
            }
        }


        /** ???????????? **/
        //??????????????????
        if (StringUtils.isNotBlank(ctBuContract.getArchiveFiles())) {
            String[] str = ctBuContract.getArchiveFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //?????????????????? ptype:1
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //???????????????
        if (StringUtils.isNotBlank(ctBuContract.getContractScanningFile())) {
            String[] str = ctBuContract.getContractScanningFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(2);   //??????????????? ptype:2
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //??????????????????????????????
        if (StringUtils.isNotBlank(ctBuContract.getContractApprovalFile())) {
            String[] str = ctBuContract.getContractApprovalFile().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(3);   //?????????????????????????????? ptype:3
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //????????????
        if (StringUtils.isNotBlank(ctBuContract.getOtherFiles())) {
            String[] str = ctBuContract.getOtherFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(4);   //???????????? ptype:4
                    file.setPid(ctBuContract.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        //??????archiveFiles??????
        ctBuContract.setArchiveFiles(null);
        //??????contractScanningFile??????
        ctBuContract.setContractScanningFile(null);
        //??????contractApprovalFile??????
        ctBuContract.setContractApprovalFile(null);
        //??????otherFiles??????
        ctBuContract.setOtherFiles(null);

        String widthName = "";
        if (ctBuContract.getStatus() == 0) {
            ctBuContract.setStatusPs("??????");
        } else if (ctBuContract.getStatus() == 1) {
            if (ctBuContract.getIsOldCon() == 2){
                ctBuContract.setStatusPs("?????????");
                //????????????????????????????????????
                long dept_id = loginUser.getUser().getDept().getDeptId();
                Map<String, Object> variables = this.getVarirs(dept_id);
                variables.put("apply", loginUser.getUsername());
                widthName = "????????????-" + loginUser.getUser().getNickName();
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
                ctBuContract.setStatusPs("????????????");
            }

        }

        int i = ctBuContractMapper.updateCtBuContract(ctBuContract);
        if (i > 0) {
            if (ctBuContract.getStatus() == 1) {
                // ??????????????????Id????????????
                Map map = new HashMap();
                map.put("instanceId", ctBuContract.getInstanceId());
                map.put("name", widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE, SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map), 0);//30???????????????
            }
        }


        return i;
    }

    /**
     * ?????????????????????????????????
     *
     * @param ids ????????????????????????????????????ID
     * @return ??????
     */
    @Override
    public int deleteCtBuContractByIds(String[] ids) {
        return ctBuContractMapper.deleteCtBuContractByIds(ids);
    }

    /**
     * ?????????????????????????????????
     *
     * @param id ?????????????????????ID
     * @return ??????
     */
    @Override
    public int deleteCtBuContractById(String id) {
        return ctBuContractMapper.deleteCtBuContractById(id);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param ids ????????????????????????????????????ID
     * @return ??????
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
     * ???????????????????????????
     */
    @Override
    public void selectWarning() {

        //????????????????????????
        SysDictData sysDictData = sysDictDataMapper.selectDictDataById(146L);
        Integer dayNumber = Integer.valueOf(sysDictData.getDictValue().trim());

        //??????????????????
        LocalDate localDate = LocalDate.now().plusDays(dayNumber);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        //?????????????????????
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
     * ??????????????????
     *
     * @param ctBuContract ????????????
     * @return ????????????
     */
    @Override
    public int saveTicketInfo(CtBuContract ctBuContract) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //???????????????
        List<CtBuContractPayinfo> ctBuContractPayinfoList = ctBuContract.getCtBuContractPayinfoList();
        if (ctBuContractPayinfoList != null) {
            TicketRecord ticketRecordObj = null;
            TicketRecord obj = null;
            for (CtBuContractPayinfo cbp : ctBuContractPayinfoList) {
                cbp.setContractId(ctBuContract.getId());            //??????ID
                cbp.setUpdateBy(loginUser.getUser().getUserId().toString());              //?????????
                cbp.setUpdateTime(ctBuContract.getCreateTime());            //????????????
                cbp.setDelFlag(1);                                         //???????????? 1?????????2?????????
                //???????????????
                ctBuContractPayinfoMapper.updateCtBuContractPayinfo(cbp);
                //??????????????????
                ticketRecordObj = new TicketRecord();
                ticketRecordObj.setProNo(ctBuContract.getProjectNo());//????????????
                ticketRecordObj.setProName(ctBuContract.getProjectName());//????????????
                ticketRecordObj.setContractId(ctBuContract.getId());//??????id
                ticketRecordObj.setContractNo(ctBuContract.getContractNo());//????????????
                ticketRecordObj.setContractName(ctBuContract.getContractName());//????????????
                ticketRecordObj.setPayinfoid(cbp.getId());
                ticketRecordObj.setPaymentPeriod(cbp.getEstimateTime());//????????????             
//                ticketRecordObj.setInvoiceDate(cbp.getInvoiceTime());//????????????
//                ticketRecordObj.setInvoiceCode(cbp.getInvoiceCode());//????????????
//                ticketRecordObj.setInvoiceNo(cbp.getInvoiceId());// ????????????
//                ticketRecordObj.setInvoiceMoney(cbp.getInvoiceMoney());//????????????????????????
                if (ctBuContract.getTaxrate() != null) {
                    ticketRecordObj.setTaxrate(ctBuContract.getTaxrate().longValue());//??????
                } else {
                    ticketRecordObj.setTaxrate(null);//??????
                }
//                ticketRecordObj.setInvoiceNomoney(cbp.getInvoiceNomoney());//???????????????????????????
                ticketRecordObj.setOppositeName(ctBuContract.getOppositeName());//??????????????????
                ticketRecordObj.setOppositeBank(ctBuContract.getOppositeBank());//????????????????????????
                ticketRecordObj.setOppositeBankId(ctBuContract.getOppositeBankId());//????????????????????????
                ticketRecordObj.setOppositeContacts(ctBuContract.getOppositeContacts());
                ;//?????????????????????
                ticketRecordObj.setOppositePhone(ctBuContract.getOppositePhone());// ????????????????????????
                ticketRecordObj.setOppositeAddress(ctBuContract.getOppositeAddress());//????????????????????????
                ticketRecordObj.setInvoiceVoucher(null);//????????????
                ticketRecordObj.setUnitQualification(null);//??????????????????
                ticketRecordObj.setStatus(ctBuContract.getStatus().longValue());
                if (ctBuContract.getStatus() == 0) {
                    ticketRecordObj.setStatusps("??????");
                } else {
                    ticketRecordObj.setStatusps("?????????");
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
     * ????????????
     *
     * @return
     */
    @Override
    public List projectCount() {
        /** ????????????????????? **/
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
                    //?????????????????????
//                    int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId());
                    //??????????????????
//                    int countApprovalNg = ctBuApprovalNgMapper.countApprovalNgByCompanyId(sd.getDeptId());
                    //??????????????????
                    int countContract = ctBuContractMapper.countContractByCompany(sd.getDeptId());
                    //?????????????????????
                    int countContractUnderway = ctBuContractMapper.countContractUnderwayByCompany(sd.getDeptId());
                    //?????????????????????
                    int countContractUnStart = ctBuContractMapper.countContractUnStartByCompany(sd.getDeptId());
                    //??????????????????
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
        /** ????????????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            // ???????????????????????????
            if (loginUser.getUser().isAdmin()) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                List<SysDept> sysDepts = sysDeptMapper.selectDeptByParentId(200L);
                if (sysDepts != null && sysDepts.size() != 0) {
                    for (SysDept sd : sysDepts) {
                        if (sd.getDeptId() != null) {
                            HashMap<String, Object> stringIntegerHashMap = ctBuContractMapper.contractCount(sd.getDeptId(), beginTime, endTime);
                            //?????????????????????
                            int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId(), beginTime, endTime);
                            //??????????????????
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
                                //?????????????????????
                                int countAppNgc = appNgcMapper.countAppNgcByCompanyId(sd.getDeptId(), beginTime, endTime);
                                //??????????????????
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
                        //?????????????????????
                        int countAppNgc = appNgcMapper.countAppNgcByCompanyId(loginUser.getUser().getDeptId(), beginTime, endTime);
                        //??????????????????
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
     * ????????????
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
     * ????????????
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
