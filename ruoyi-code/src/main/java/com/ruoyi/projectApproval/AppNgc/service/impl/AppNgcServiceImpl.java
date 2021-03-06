package com.ruoyi.projectApproval.AppNgc.service.impl;

import com.ruoyi.base.cache.service.CacheService;
import com.ruoyi.base.fileManage.domain.CtBuFile;
import com.ruoyi.base.fileManage.service.ICtBuFileService;
import com.ruoyi.collection.domain.ActWorkflowFormDatas;
import com.ruoyi.collection.mapper.CtBuInvoiceReqMapper;
import com.ruoyi.collection.utils.ActUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.contract.contractInfo.domain.CtBuContract;
import com.ruoyi.dictionary.proType.domain.CtDicProtype;
import com.ruoyi.projectApproval.AppNgBase.domain.AppNgBase;
import com.ruoyi.projectApproval.AppNgBase.service.IAppNgBaseService;
import com.ruoyi.projectApproval.AppNgConst.domain.AppNgConst;
import com.ruoyi.projectApproval.AppNgConst.service.IAppNgConstService;
import com.ruoyi.projectApproval.AppNgMaterial.domain.AppNgMaterial;
import com.ruoyi.projectApproval.AppNgMaterial.service.IAppNgMaterialService;
import com.ruoyi.projectApproval.AppNgReqList.domain.AppNgReqList;
import com.ruoyi.projectApproval.AppNgReqList.service.IAppNgReqListService;
import com.ruoyi.projectApproval.AppNgc.domain.AppNgc;
import com.ruoyi.projectApproval.AppNgc.mapper.AppNgcMapper;
import com.ruoyi.projectApproval.AppNgc.service.IAppNgcService;
import com.ruoyi.projectApproval.AppxNgReq.domain.AppxNgReq;
import com.ruoyi.projectApproval.AppxNgReq.service.IAppxNgReqService;
import com.ruoyi.system.domain.CtPdeptDept;
import com.ruoyi.system.service.ICtPdeptDeptService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 非集客项目Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-11
 */
@Service
public class AppNgcServiceImpl implements IAppNgcService {

    @Autowired
    private AppNgcMapper appNgcMapper;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ICtBuFileService fileService;
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private ISysUserService iSysUserService;


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ICtPdeptDeptService iCtPdeptDeptService;

    @Autowired
    private CtBuInvoiceReqMapper ctBuInvoiceReqMapper;

    @Autowired
    private IAppNgBaseService appNgBaseService;

    @Autowired
    private IAppNgConstService appNgConstService;

    @Autowired
    private IAppNgMaterialService appNgMaterialService;

    @Autowired
    private IAppxNgReqService appxNgReqService;

    @Autowired
    private IAppNgReqListService appNgReqListService;


    /**
     * 查询非集客项目
     *
     * @param id 非集客项目ID
     * @return 非集客项目
     */
    @Override
    public AppNgc selectAppNgcById(String id) {

        AppNgc appNgc = appNgcMapper.selectAppNgcById(id);
        if (appNgc != null && appNgc.getId() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            /** 项目基本情况 */
            AppNgBase appNgBase = appNgBaseService.selectAppNgBaseByBid(id);
            if (appNgBase != null) {
                appNgc.setAppNgBase(appNgBase);
            } else {
                AppNgBase appNgBase1 = new AppNgBase();
                appNgc.setAppNgBase(appNgBase1);
            }

            /** 事前审批 **/
            AppxNgReq appxNgReq = appxNgReqService.selectAppxNgReqByBid(id);
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getId())) {
                /** 获取成本子类信息 **/
                AppNgReqList appNgReqList = new AppNgReqList();
                appNgReqList.setPid(appxNgReq.getId());
                List<AppNgReqList> reqList = appNgReqListService.selectAppNgReqListList(appNgReqList);
                if (reqList != null && !reqList.isEmpty()) {
                    appxNgReq.setDataList(reqList);
                }
                appNgc.setAppxNgReq(appxNgReq);
            } else {
                AppxNgReq req = this.getAppxNgReq();
                appNgc.setAppxNgReq(req);//devicecosttaxrate
            }

            /** 施工费情况 **/
            AppNgConst appNgConst = new AppNgConst();
            appNgConst.setBid(appNgc.getId());
            List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
            if (appNgConstList != null && !appNgConstList.isEmpty()) {
                appNgc.setAppNgConstList(appNgConstList);
            } else {
                ArrayList<AppNgConst> objects = new ArrayList<>();
                appNgc.setAppNgConstList(objects);
            }
            /** 材料使用情况 **/
            AppNgMaterial appNgMaterial = new AppNgMaterial();
            appNgMaterial.setBid(appNgc.getId());
            List<AppNgMaterial> appNgMaterialList = appNgMaterialService.selectAppNgMaterialList(appNgMaterial);
            if (appNgMaterialList != null && !appNgMaterialList.isEmpty()) {
                appNgc.setAppNgMaterialList(appNgMaterialList);
            } else {
                ArrayList<AppNgMaterial> appNgMaterials = new ArrayList<>();
                appNgc.setAppNgMaterialList(appNgMaterials);
            }

        }
        if (appNgc != null && appNgc.getId() != null) {
            ActWorkflowFormDatas data = new ActWorkflowFormDatas();
            data.setBusinessKey(appNgc.getId());
            List<ActWorkflowFormDatas> alist = ctBuInvoiceReqMapper.selectGroupActWorkflowFormDataLists(data);
            appNgc.setActWorkflowFormDatas(alist);
        }
        return appNgc;
    }

    /**
     * 查询非集客项目列表
     *
     * @param appNgc 非集客项目
     * @return 非集客项目
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<AppNgc> selectAppNgcList(AppNgc appNgc) {
        List<AppNgc> list = appNgcMapper.selectAppNgcList1(appNgc);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                /** 项目类型名称 **/
                CtDicProtype protype = cacheService.findProtypeById(list.get(i).getProType());
                if (protype != null && StringUtils.isNotBlank(protype.getId())) {
                    list.get(i).setProTypeName(protype.getTypeName());
                }
                /** 项目分类 **/
                if (list.get(i).getClassification() != null) {
                    list.get(i)
                            .setClassName(DictUtils.getDictLabel("project_sort", list.get(i).getClassification() + ""));
                }
                /** 所属分公司+部门 **/
                if (StringUtils.isNotBlank(list.get(i).getBelongCompanyid())) {
                    SysDept company = cacheService.findSysDeptById(list.get(i).getBelongCompanyid());
                    if (company != null && StringUtils.isNotBlank(company.getDeptName())) {
                        list.get(i).setBelongCompanyname(company.getDeptName());
                    }
                }
                if (StringUtils.isNotBlank(list.get(i).getBelongDeptid())) {
                    SysDept dept = cacheService.findSysDeptById(list.get(i).getBelongDeptid());
                    if (dept != null && StringUtils.isNotBlank(dept.getDeptName())) {
                        list.get(i).setBelongDeptname(dept.getDeptName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增非集客项目
     *
     * @param appNgc 非集客项目
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAppNgc(AppNgc appNgc) {
        appNgc.setId(UUID.fastUUID().toString(true));
        String userId = "";
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
        } catch (Exception e) {

        }
        appNgc.setCreateBy(userId);
        appNgc.setDelFlag(1);
        appNgc.setReqNo(createReqNo(SecurityUtils.getLoginUser().getUser().getDeptId(), Integer.valueOf(appNgc.getClassification())));
        appNgc.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNotBlank(appNgc.getProType())) {
            /** 根据项目ID获取项目信息 **/
            CtDicProtype protype = cacheService.findProtypeById(appNgc.getProType());
            if (protype != null && StringUtils.isNotBlank(protype.getId())) {
                appNgc.setProTypeName(protype.getTypeName());
            }
        }

        /** 所属分公司 **/
        if (StringUtils.isNotBlank(appNgc.getBelongCompanyid())) {
            SysDept sysDept = cacheService.findSysDeptById(appNgc.getBelongCompanyid());
            if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptName())) {
                appNgc.setBelongCompanyname(sysDept.getDeptName());
            }
        }
        /** 所属部门 **/
        if (StringUtils.isNotBlank(appNgc.getBelongDeptid())) {
            SysDept dept = cacheService.findSysDeptById(appNgc.getBelongDeptid());
            if (dept != null && StringUtils.isNotBlank(dept.getDeptName())) {
                appNgc.setBelongDeptname(dept.getDeptName());
            }
        }

        if (appNgc.getProType() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            //项目基本情况
            if (appNgc.getAppNgBase() != null) {
                appNgc.getAppNgBase().setId(UUID.fastUUID().toString(true));
                appNgc.getAppNgBase().setBid(appNgc.getId());
                appNgc.getAppNgBase().setCreateBy(userId);
                //appNgc.getAppNgBase().setCid(c.getId());
                appNgc.getAppNgBase().setDelFlag(1);
                appNgc.getAppNgBase().setBranchInvolvedId(appNgc.getBelongCompanyid());
                appNgc.getAppNgBase().setBranchInvolvedName(appNgc.getBelongCompanyname());

                appNgBaseService.insertAppNgBase(appNgc.getAppNgBase());
            }
            //项目施工费情况
            List<AppNgConst> appNgConstList = appNgc.getAppNgConstList();
            if (appNgConstList != null && appNgConstList.size() != 0) {
                for (AppNgConst anc : appNgConstList) {
                    anc.setId(UUID.fastUUID().toString(true));
                    anc.setCreateBy(userId);
                    anc.setBid(appNgc.getId());
                    anc.setDelFlag(1);
                    appNgConstService.insertAppNgConst(anc);
                }
            }
            //项目材料使用情况
            List<AppNgMaterial> appNgMaterialList = appNgc.getAppNgMaterialList();
            if (appNgMaterialList != null && appNgMaterialList.size() != 0) {
                for (AppNgMaterial anm : appNgMaterialList) {
                    anm.setId(UUID.fastUUID().toString(true));
                    anm.setBid(appNgc.getId());
                    anm.setCreateBy(userId);
                    anm.setDelFlag(1);
                    appNgMaterialService.insertAppNgMaterial(anm);
                }
            }

            /** 事前审批 **/
            AppxNgReq appxNgReq = appNgc.getAppxNgReq();
            if (appxNgReq != null) {
                appxNgReq.setId(UUID.fastUUID().toString(true));
                appxNgReq.setProType(appNgc.getProType());
                appxNgReq.setAppxNgNo(appNgc.getReqNo());
                appxNgReq.setAppxNgName(appNgc.getProName());
                appxNgReq.setBid(appNgc.getId());
                appxNgReq.setCreateBy(userId);
                appxNgReq.setAppxNgNo(appNgc.getReqNo());
                appxNgReq.setDelFlag(1);
                appxNgReq.setAppxNgName(appNgc.getProName());
                appxNgReq.setProType(appNgc.getProType());
                appxNgReq.setApplyDate(new Date());
                appxNgReq.setApplyCompany(appNgc.getBelongCompanyname());
                appxNgReqService.insertAppxNgReq(appxNgReq);
                /** 成本子类 **/
                List<AppNgReqList> aaList = appxNgReq.getDataList();
                if (aaList != null && !aaList.isEmpty()) {
                    for (int i = 0; i < aaList.size(); i++) {
                        AppNgReqList appNgReqList = aaList.get(i);
                        appNgReqList.setId(UUID.fastUUID().toString(true));
                        appNgReqList.setPid(appxNgReq.getId());
                        appNgReqList.setCreateTime(new Date());
                        appNgReqListService.insertAppNgReqList(appNgReqList);
                    }
                }
            }


        }
        /** 处理文件 **/
        if (StringUtils.isNotBlank(appNgc.getFiles())) {
            String[] str = appNgc.getFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(1);
                    fileService.updateCtBuFile(file);
                }
            }
        }

        if (StringUtils.isNotBlank(appNgc.getApproveResult())) {
            String[] str = appNgc.getApproveResult().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(2);
                    fileService.updateCtBuFile(file);
                }
            }
        }
        /** 清除files字段 **/
        appNgc.setFiles(null);
        appNgc.setApproveResult(null);

        if (appNgc.getStatus() == 0) {
            appNgc.setStatusPs("保存");
        } else if (appNgc.getStatus() == 1) {
            if (appNgc.getIsOldPro() == 1){
                appNgc.setStatusPs("审核通过");
                appNgc.setStatus(2);
            } else if (appNgc.getIsOldPro() == 2){
                appNgc.setStatusPs("审核中");
                //获取当前登陆人的所属部门
                /** 判断当前登录人 **/
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUser() != null) {
                    long dept_id = loginUser.getUser().getDept().getDeptId();
                    long parent_id = loginUser.getUser().getDept().getParentId();

                    String widthName = "非集客项目(限额以下的审批流程)-" + loginUser.getUser().getUserName();
                    String key = "fjkxmDown";
                    BigDecimal interestRate = new BigDecimal("100000"); //利率
                    boolean flag = false;
                    if ((appNgc.getContractAmountTax()).compareTo(interestRate) == -1) {
                        widthName = "非集客项目(10w限额以下的审批流程)-" + loginUser.getUser().getNickName();
                        key = "fjkxmDown";
                    } else {
                        widthName = "非集客项目(10w限额以上的审批流程)-" + loginUser.getUser().getNickName();
                        key = "fjkxmUp";
                        flag = true;
                    }
                    Map<String, Object> variables = this.getVarirs(dept_id, appNgc, flag);
                    variables.put("apply", loginUser.getUsername());
                    //本部人员提交
                    if ("1".equals(loginUser.getUser().getUserType())) {
                        variables.put("tjstate", 2);
                    }
                    if ("2".equals(loginUser.getUser().getUserType())) {
                        variables.put("tjstate", 1);
                    }

                    appNgc.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, appNgc.getId(), variables
                            , taskService, taskRuntime, key, widthName));
                }
            }

        }
        return appNgcMapper.insertAppNgc(appNgc);

    }

    //获取组人员
    public Map<String, Object> getVarirs(long dept_id, AppNgc appNgc, boolean flag) {
        //查询当前登陆人所属部门

        Map<String, Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();
        //分公司经理审核
        variables.put("fgsjlsh", ActUtils.getVarirs(Long.parseLong(appNgc.getBelongCompanyid()), "110", iSysUserService));
        //本部门主任审核  分管领导
        String deptName = appNgc.getBelongDeptname();
        if ("投资项目".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 228, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("228");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("新建楼盘".equals(appNgc.getProTypeName()) || "服务项目".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 214, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("214");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("集中采购".equals(appNgc.getProTypeName()) || "其它采购/销售项目".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 227, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("227");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("其他".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 216, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("216");
            variables.put("fgfzsh", dept.getFgld());
        }
        //增加总经理审核
        if (flag) {
            variables.put("zjlsh", ActUtils.getVarirs(null, "115", iSysUserService));
        }

        return variables;
    }

    /**
     * 修改非集客项目
     *
     * @param appNgc 非集客项目
     * @return 结果
     */
    @Override
    public int updateAppNgc(AppNgc appNgc) {
        String userId = "";
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
        } catch (Exception e) {

        }
        appNgc.setUpdateTime(DateUtils.getNowDate());
        appNgc.setUpdateBy(userId);
        /** 处理文件 **/
//        if (StringUtils.isNotBlank(appNgc.getFiles())) {
//            String[] str = appNgc.getFiles().split(",");
//            for (int i = 0; i < str.length; i++) {
//                /** 判断传入文件ID是否为空 **/
//                if (StringUtils.isNotBlank(str[i])) {
//                    CtBuFile file = new CtBuFile();
//                    file.setId(str[i]);
//                    file.setPid(appNgc.getId());
//                    fileService.updateCtBuFile(file);
//                }
//            }
//        }
        /** 处理文件 **/
        if (StringUtils.isNotBlank(appNgc.getFiles())) {
            String[] str = appNgc.getFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(1);
                    fileService.updateCtBuFile(file);
                }
            }
        }

        if (StringUtils.isNotBlank(appNgc.getApproveResult())) {
            String[] str = appNgc.getApproveResult().split(",");
            for (int i = 0; i < str.length; i++) {
                /** 判断传入文件ID是否为空 **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(2);
                    fileService.updateCtBuFile(file);
                }
            }
        }
        /** 清除files字段 **/
        appNgc.setFiles(null);
        appNgc.setApproveResult(null);


        if (appNgc.getProType() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            //项目基本情况
            if (appNgc.getAppNgBase() != null) {
                appNgc.getAppNgBase().setUpdateBy(userId);
                appNgc.getAppNgBase().setDelFlag(1);
                appNgBaseService.updateAppNgBase(appNgc.getAppNgBase());
            }
            //项目施工费情况
            List<AppNgConst> appNgConstList = appNgc.getAppNgConstList();
            if (appNgConstList != null && appNgConstList.size() != 0) {
                for (AppNgConst anc : appNgConstList) {
                    if (anc.getId() != null) {
                        anc.setUpdateBy(userId);
                        anc.setDelFlag(1);
                        appNgConstService.updateAppNgConst(anc);
                    } else {
                        anc.setId(UUID.fastUUID().toString(true));
                        anc.setCreateBy(userId);
                        anc.setBid(appNgc.getId());
                        anc.setDelFlag(1);
                        appNgConstService.insertAppNgConst(anc);
                    }

                }
            }
            //项目材料使用情况
            List<AppNgMaterial> appNgMaterialList = appNgc.getAppNgMaterialList();
            if (appNgMaterialList != null && appNgMaterialList.size() != 0) {
                for (AppNgMaterial anm : appNgMaterialList) {
                    if (anm.getId() != null) {
                        anm.setUpdateBy(userId);
                        anm.setDelFlag(1);
                        appNgMaterialService.updateAppNgMaterial(anm);
                    } else {
                        anm.setId(UUID.fastUUID().toString(true));
                        anm.setBid(appNgc.getId());
                        anm.setCreateBy(userId);
                        anm.setDelFlag(1);
                        appNgMaterialService.insertAppNgMaterial(anm);
                    }

                }
            }

            /** 事前审批 **/
            AppxNgReq appxNgReq = appNgc.getAppxNgReq();
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getAppxNgNo())) {
                appxNgReq.setUpdateBy(userId);
                appxNgReq.setDelFlag(1);
                appxNgReqService.updateAppxNgReq(appxNgReq);
                /** 成本子类 **/
                List<AppNgReqList> aaList = appxNgReq.getDataList();
                if (aaList != null && !aaList.isEmpty()) {
                    for (int i = 0; i < aaList.size(); i++) {
                        if (aaList.get(i).getId() != null) {
                            AppNgReqList appNgReqList = aaList.get(i);
                            appNgReqList.setUpdateTime(new Date());
                            appNgReqListService.updateAppNgReqList(appNgReqList);
                        } else {
                            AppNgReqList appNgReqList = aaList.get(i);
                            appNgReqList.setId(UUID.fastUUID().toString(true));
                            appNgReqList.setPid(appxNgReq.getId());
                            appNgReqList.setCreateTime(new Date());
                            appNgReqListService.insertAppNgReqList(appNgReqList);
                        }

                    }
                }
            }


        }


        if (appNgc.getStatus() == 0) {
            appNgc.setStatusPs("保存");
        } else if (appNgc.getStatus() == 1) {
            if (appNgc.getIsOldPro() == 1){
                appNgc.setStatusPs("审核通过");
                appNgc.setStatus(2);
            } else if (appNgc.getIsOldPro() == 2){
                appNgc.setStatusPs("审核中");
                //获取当前登陆人的所属部门
                /** 判断当前登录人 **/
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUser() != null) {
                    long dept_id = loginUser.getUser().getDept().getDeptId();
                    long parent_id = loginUser.getUser().getDept().getParentId();

                    String widthName = "非集客项目(限额以下的审批流程)-" + loginUser.getUser().getUserName();
                    String key = "fjkxmDown";
                    BigDecimal interestRate = new BigDecimal("100000"); //利率
                    boolean flag = false;
                    if ((appNgc.getContractAmountTax()).compareTo(interestRate) == -1) {
                        widthName = "非集客项目(10w限额以下的审批流程)-" + loginUser.getUser().getNickName();
                        key = "fjkxmDown";
                    } else {
                        widthName = "非集客项目(10w限额以下的审批流程)-" + loginUser.getUser().getNickName();
                        key = "fjkxmUp";
                        flag = true;
                    }
                    Map<String, Object> variables = this.getVarirs(dept_id, appNgc, flag);
                    variables.put("apply", loginUser.getUsername());
                    //本部人员提交
                    if ("1".equals(loginUser.getUser().getUserType())) {
                        variables.put("tjstate", 2);
                    }
                    if ("2".equals(loginUser.getUser().getUserType())) {
                        variables.put("tjstate", 1);
                    }

                    if (appNgc.getInstanceId() != null && !"".equals(appNgc.getInstanceId())) {
                        appNgc.setInstanceId(ActUtils.submitTask(runtimeService, processRuntime, loginUser, appNgc.getInstanceId(), variables
                                , taskService, taskRuntime, key, widthName, appNgc.getId()));
                    } else {
                        appNgc.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, appNgc.getId(), variables
                                , taskService, taskRuntime, key, widthName));
                    }
                }
            }

        }
        System.err.println(appNgc);
        return appNgcMapper.updateAppNgc(appNgc);
    }

    /**
     * 批量删除非集客项目
     *
     * @param ids 需要删除的非集客项目ID
     * @return 结果
     */
    @Override
    public int deleteAppNgcByIds(String[] ids) {
        return appNgcMapper.deleteAppNgcByIds(ids);
    }

    /**
     * 删除非集客项目信息
     *
     * @param id 非集客项目ID
     * @return 结果
     */
    @Override
    public int deleteAppNgcById(String id) {
        return appNgcMapper.deleteAppNgcById(id);
    }

    /**
     * @Title: AppNgcServiceImpl.java
     * @Description: 生成非集客项目编号
     * @Description: deptId ---所属部门ID
     * @Description: falg ---收入/支出(1收入2支出)
     * @Author M.Mu
     * @Date 2020-12-11
     */
    public String createReqNo(Long deptId, Integer falg) {
        /** 根据所属分公司ID获取分公司全名 **/
        if (deptId != null && falg != null) {
            /** 1、公司别名 **/
            String company = "JN";
            /** 根据当前登录人的所属公司ID获取公司名称 **/
            SysDept sysDept = deptService.selectDeptById(deptId);
            if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptName())) {
                if (sysDept.getDeptName().contains("宽带")) {
                    company = "KD";
                } else if (sysDept.getDeptName().contains("数字")) {
                    company = "SZ";
                } else if (sysDept.getDeptName().contains("本部")) {
                    company = "JN";
                } else if (sysDept.getDeptName().contains("历下")) {
                    company = "LX";
                } else if (sysDept.getDeptName().contains("市中")) {
                    company = "SZ";
                } else if (sysDept.getDeptName().contains("槐荫")) {
                    company = "HY";
                } else if (sysDept.getDeptName().contains("天桥")) {
                    company = "TQ";
                } else if (sysDept.getDeptName().contains("济阳")) {
                    company = "JY";
                } else if (sysDept.getDeptName().contains("商河")) {
                    company = "SH";
                } else if (sysDept.getDeptName().contains("平阴")) {
                    company = "PY";
                } else if (sysDept.getDeptName().contains("长清")) {
                    company = "CQ";
                } else if (sysDept.getDeptName().contains("章丘")) {
                    company = "ZQ";
                } else if (sysDept.getDeptName().contains("莱芜")) {
                    company = "LW";
                } else if (sysDept.getDeptName().contains("先行")) {
                    company = "XX";
                }
            }
            /** 2、类型 **/
            String falgStr = "1";
            if (falg == 1) {
                falgStr = "1";
            } else {
                falgStr = "2";
            }
            /** 3、年月 **/
            String date = DateUtils.dateTime();
            /** 4、当月集客项目数量(5位) **/
            String numStr = "";
            AppNgc appNgc = new AppNgc();
            appNgc.setStime(DateUtils.getMonthBegin(DateUtils.getDate()));
            appNgc.setEtime(DateUtils.getMonthEnd(DateUtils.getDate()));
            Integer nums = appNgcMapper.findNumsByData(appNgc);
            if (nums != null && nums > 0) {
                numStr = com.ruoyi.common.utils.StringUtils.getStringByInt(nums);
            } else {
                numStr = com.ruoyi.common.utils.StringUtils.getStringByInt(0);
            }
            return company + falgStr + date + numStr;
        }
        return "";
    }

    @Override
    public String findNextAppNo(Integer falg) {
        Long deptId = 1L;
        try {
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
        } catch (Exception e) {

        }
        return this.createReqNo(deptId, falg);
    }

    /**
     * 修改非集客项目
     *
     * @param appNgc 非集客项目
     * @return 结果
     */
    @Override
    public int updateAppNgcByInstanceId(AppNgc appNgc) {
        return appNgcMapper.updateAppNgcByInstanceId(appNgc);
    }

    /**
     * 校验项目名称是否唯一
     *
     * @param proName 项目名称
     * @return 结果
     */
    @Override
    public int checkProNameUnique(String proName) {
        return appNgcMapper.checkProNameUnique(proName);
    }

    public AppxNgReq getAppxNgReq() {
        AppxNgReq req = new AppxNgReq();
        req.setDevicecosttaxrate(new BigDecimal(13));
        req.setTrantaxrate(new BigDecimal(6));
        req.setDevicetaxrate(new BigDecimal(13));
        req.setServetaxrate(new BigDecimal(6));
        req.setProjecttaxrate(new BigDecimal(9));
        req.setLeasetaxrate(new BigDecimal(6));
        req.setSoftwaretaxrate(new BigDecimal(13));
        req.setAdverttaxrate(new BigDecimal(6));
        req.setDevicecosttaxrate(new BigDecimal(13));
        req.setDevicecostremarks("无产权工程");
        req.setStuffcostremarks("无产权工程");
        req.setTurncosttaxrate(new BigDecimal(9));
        req.setTurncostremarks("无产权工程");
        req.setServecosttaxrate(new BigDecimal(6));
        req.setServecostremarks("无产权工程");
        req.setBuildcosttaxrate(new BigDecimal(3));
        req.setBuildcostremarks("无产权工程，小规模纳税人税率3%，一般纳税人税率9%，根据施工方情况可修改税率");
        req.setHavedevicetaxrate(new BigDecimal(13));
        req.setHavedeviceremarks("自有产权工程，成本（不含税金额）按折旧计算，按投资形成资产月折旧金额*项目期限，即成本=折旧=设备不含税金额*0.95/折旧年限*服务年份，设备折旧年限8年，线路资产折旧年限15年");
        req.setHavelinetaxrate(new BigDecimal(9));
        req.setUpholdcosttaxrate(new BigDecimal(6));
        req.setInterestcostremarks("\"政府采购项目经批准后可以垫资，需计算垫资利息作为项目成本，利率按4.35%计算。其他集客项目原则上不允许垫资。垫资金额   万元，垫资   月，垫资利息=垫资金额*4.35%/12*月份\"");
        req.setAttachremarks("综合税率12.5%（城市维护建设税7%，教育费附加3%，地方教育费附加2%，地方水利建设基金0.5%）");
        req.setCulturebuildremarks("费率3%，暂减按1.5%征收，文化事业建设费=广告收入*1.5%");
        return req;
    }


    /**
     * 逻辑删除非集客项目
     *
     * @param ids 非集客项目ID
     * @return
     */
    @Override
    public int updateAppNgcById(String[] ids) {

        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                int i1 = appNgcMapper.updateAppNgcById(ids[i]);
                if (i1 > 0) {
                    appNgBaseService.deleteAppNgBaseByBid(ids[i]);
                    appNgMaterialService.deleteAppNgMaterialByBid(ids[i]);
                    appNgConstService.deleteAppNgConstByBid(ids[i]);
                    /** 事前审批 **/
                    appxNgReqService.deleteAppxNgReqByBid(ids[i]);
                }
            }
        }
        return 1;
    }
}