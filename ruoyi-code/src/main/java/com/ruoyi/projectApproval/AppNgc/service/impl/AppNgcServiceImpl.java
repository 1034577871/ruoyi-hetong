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
 * ???????????????Service???????????????
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
     * ?????????????????????
     *
     * @param id ???????????????ID
     * @return ???????????????
     */
    @Override
    public AppNgc selectAppNgcById(String id) {

        AppNgc appNgc = appNgcMapper.selectAppNgcById(id);
        if (appNgc != null && appNgc.getId() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            /** ?????????????????? */
            AppNgBase appNgBase = appNgBaseService.selectAppNgBaseByBid(id);
            if (appNgBase != null) {
                appNgc.setAppNgBase(appNgBase);
            } else {
                AppNgBase appNgBase1 = new AppNgBase();
                appNgc.setAppNgBase(appNgBase1);
            }

            /** ???????????? **/
            AppxNgReq appxNgReq = appxNgReqService.selectAppxNgReqByBid(id);
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getId())) {
                /** ???????????????????????? **/
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

            /** ??????????????? **/
            AppNgConst appNgConst = new AppNgConst();
            appNgConst.setBid(appNgc.getId());
            List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
            if (appNgConstList != null && !appNgConstList.isEmpty()) {
                appNgc.setAppNgConstList(appNgConstList);
            } else {
                ArrayList<AppNgConst> objects = new ArrayList<>();
                appNgc.setAppNgConstList(objects);
            }
            /** ?????????????????? **/
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
     * ???????????????????????????
     *
     * @param appNgc ???????????????
     * @return ???????????????
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<AppNgc> selectAppNgcList(AppNgc appNgc) {
        List<AppNgc> list = appNgcMapper.selectAppNgcList1(appNgc);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                /** ?????????????????? **/
                CtDicProtype protype = cacheService.findProtypeById(list.get(i).getProType());
                if (protype != null && StringUtils.isNotBlank(protype.getId())) {
                    list.get(i).setProTypeName(protype.getTypeName());
                }
                /** ???????????? **/
                if (list.get(i).getClassification() != null) {
                    list.get(i)
                            .setClassName(DictUtils.getDictLabel("project_sort", list.get(i).getClassification() + ""));
                }
                /** ???????????????+?????? **/
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
     * ?????????????????????
     *
     * @param appNgc ???????????????
     * @return ??????
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
            /** ????????????ID?????????????????? **/
            CtDicProtype protype = cacheService.findProtypeById(appNgc.getProType());
            if (protype != null && StringUtils.isNotBlank(protype.getId())) {
                appNgc.setProTypeName(protype.getTypeName());
            }
        }

        /** ??????????????? **/
        if (StringUtils.isNotBlank(appNgc.getBelongCompanyid())) {
            SysDept sysDept = cacheService.findSysDeptById(appNgc.getBelongCompanyid());
            if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptName())) {
                appNgc.setBelongCompanyname(sysDept.getDeptName());
            }
        }
        /** ???????????? **/
        if (StringUtils.isNotBlank(appNgc.getBelongDeptid())) {
            SysDept dept = cacheService.findSysDeptById(appNgc.getBelongDeptid());
            if (dept != null && StringUtils.isNotBlank(dept.getDeptName())) {
                appNgc.setBelongDeptname(dept.getDeptName());
            }
        }

        if (appNgc.getProType() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            //??????????????????
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
            //?????????????????????
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
            //????????????????????????
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

            /** ???????????? **/
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
                /** ???????????? **/
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
        /** ???????????? **/
        if (StringUtils.isNotBlank(appNgc.getFiles())) {
            String[] str = appNgc.getFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
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
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(2);
                    fileService.updateCtBuFile(file);
                }
            }
        }
        /** ??????files?????? **/
        appNgc.setFiles(null);
        appNgc.setApproveResult(null);

        if (appNgc.getStatus() == 0) {
            appNgc.setStatusPs("??????");
        } else if (appNgc.getStatus() == 1) {
            if (appNgc.getIsOldPro() == 1){
                appNgc.setStatusPs("????????????");
                appNgc.setStatus(2);
            } else if (appNgc.getIsOldPro() == 2){
                appNgc.setStatusPs("?????????");
                //????????????????????????????????????
                /** ????????????????????? **/
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUser() != null) {
                    long dept_id = loginUser.getUser().getDept().getDeptId();
                    long parent_id = loginUser.getUser().getDept().getParentId();

                    String widthName = "???????????????(???????????????????????????)-" + loginUser.getUser().getUserName();
                    String key = "fjkxmDown";
                    BigDecimal interestRate = new BigDecimal("100000"); //??????
                    boolean flag = false;
                    if ((appNgc.getContractAmountTax()).compareTo(interestRate) == -1) {
                        widthName = "???????????????(10w???????????????????????????)-" + loginUser.getUser().getNickName();
                        key = "fjkxmDown";
                    } else {
                        widthName = "???????????????(10w???????????????????????????)-" + loginUser.getUser().getNickName();
                        key = "fjkxmUp";
                        flag = true;
                    }
                    Map<String, Object> variables = this.getVarirs(dept_id, appNgc, flag);
                    variables.put("apply", loginUser.getUsername());
                    //??????????????????
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

    //???????????????
    public Map<String, Object> getVarirs(long dept_id, AppNgc appNgc, boolean flag) {
        //?????????????????????????????????

        Map<String, Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();
        //?????????????????????
        variables.put("fgsjlsh", ActUtils.getVarirs(Long.parseLong(appNgc.getBelongCompanyid()), "110", iSysUserService));
        //?????????????????????  ????????????
        String deptName = appNgc.getBelongDeptname();
        if ("????????????".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 228, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("228");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("????????????".equals(appNgc.getProTypeName()) || "????????????".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 214, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("214");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("????????????".equals(appNgc.getProTypeName()) || "????????????/????????????".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 227, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("227");
            variables.put("fgfzsh", dept.getFgld());
        }
        if ("??????".equals(appNgc.getProTypeName())) {
            variables.put("bbmzrsh", ActUtils.getVarirs((long) 216, "109", iSysUserService));
            CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("216");
            variables.put("fgfzsh", dept.getFgld());
        }
        //?????????????????????
        if (flag) {
            variables.put("zjlsh", ActUtils.getVarirs(null, "115", iSysUserService));
        }

        return variables;
    }

    /**
     * ?????????????????????
     *
     * @param appNgc ???????????????
     * @return ??????
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
        /** ???????????? **/
//        if (StringUtils.isNotBlank(appNgc.getFiles())) {
//            String[] str = appNgc.getFiles().split(",");
//            for (int i = 0; i < str.length; i++) {
//                /** ??????????????????ID???????????? **/
//                if (StringUtils.isNotBlank(str[i])) {
//                    CtBuFile file = new CtBuFile();
//                    file.setId(str[i]);
//                    file.setPid(appNgc.getId());
//                    fileService.updateCtBuFile(file);
//                }
//            }
//        }
        /** ???????????? **/
        if (StringUtils.isNotBlank(appNgc.getFiles())) {
            String[] str = appNgc.getFiles().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
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
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPid(appNgc.getId());
                    file.setPtype(2);
                    fileService.updateCtBuFile(file);
                }
            }
        }
        /** ??????files?????? **/
        appNgc.setFiles(null);
        appNgc.setApproveResult(null);


        if (appNgc.getProType() != null && appNgc.getProType().equals("36e4550f15954fbbadf4dff4f6fc63ea")) {

            //??????????????????
            if (appNgc.getAppNgBase() != null) {
                appNgc.getAppNgBase().setUpdateBy(userId);
                appNgc.getAppNgBase().setDelFlag(1);
                appNgBaseService.updateAppNgBase(appNgc.getAppNgBase());
            }
            //?????????????????????
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
            //????????????????????????
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

            /** ???????????? **/
            AppxNgReq appxNgReq = appNgc.getAppxNgReq();
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getAppxNgNo())) {
                appxNgReq.setUpdateBy(userId);
                appxNgReq.setDelFlag(1);
                appxNgReqService.updateAppxNgReq(appxNgReq);
                /** ???????????? **/
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
            appNgc.setStatusPs("??????");
        } else if (appNgc.getStatus() == 1) {
            if (appNgc.getIsOldPro() == 1){
                appNgc.setStatusPs("????????????");
                appNgc.setStatus(2);
            } else if (appNgc.getIsOldPro() == 2){
                appNgc.setStatusPs("?????????");
                //????????????????????????????????????
                /** ????????????????????? **/
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUser() != null) {
                    long dept_id = loginUser.getUser().getDept().getDeptId();
                    long parent_id = loginUser.getUser().getDept().getParentId();

                    String widthName = "???????????????(???????????????????????????)-" + loginUser.getUser().getUserName();
                    String key = "fjkxmDown";
                    BigDecimal interestRate = new BigDecimal("100000"); //??????
                    boolean flag = false;
                    if ((appNgc.getContractAmountTax()).compareTo(interestRate) == -1) {
                        widthName = "???????????????(10w???????????????????????????)-" + loginUser.getUser().getNickName();
                        key = "fjkxmDown";
                    } else {
                        widthName = "???????????????(10w???????????????????????????)-" + loginUser.getUser().getNickName();
                        key = "fjkxmUp";
                        flag = true;
                    }
                    Map<String, Object> variables = this.getVarirs(dept_id, appNgc, flag);
                    variables.put("apply", loginUser.getUsername());
                    //??????????????????
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
     * ???????????????????????????
     *
     * @param ids ??????????????????????????????ID
     * @return ??????
     */
    @Override
    public int deleteAppNgcByIds(String[] ids) {
        return appNgcMapper.deleteAppNgcByIds(ids);
    }

    /**
     * ???????????????????????????
     *
     * @param id ???????????????ID
     * @return ??????
     */
    @Override
    public int deleteAppNgcById(String id) {
        return appNgcMapper.deleteAppNgcById(id);
    }

    /**
     * @Title: AppNgcServiceImpl.java
     * @Description: ???????????????????????????
     * @Description: deptId ---????????????ID
     * @Description: falg ---??????/??????(1??????2??????)
     * @Author M.Mu
     * @Date 2020-12-11
     */
    public String createReqNo(Long deptId, Integer falg) {
        /** ?????????????????????ID????????????????????? **/
        if (deptId != null && falg != null) {
            /** 1??????????????? **/
            String company = "JN";
            /** ????????????????????????????????????ID?????????????????? **/
            SysDept sysDept = deptService.selectDeptById(deptId);
            if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptName())) {
                if (sysDept.getDeptName().contains("??????")) {
                    company = "KD";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "SZ";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "JN";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "LX";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "SZ";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "HY";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "TQ";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "JY";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "SH";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "PY";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "CQ";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "ZQ";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "LW";
                } else if (sysDept.getDeptName().contains("??????")) {
                    company = "XX";
                }
            }
            /** 2????????? **/
            String falgStr = "1";
            if (falg == 1) {
                falgStr = "1";
            } else {
                falgStr = "2";
            }
            /** 3????????? **/
            String date = DateUtils.dateTime();
            /** 4???????????????????????????(5???) **/
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
     * ?????????????????????
     *
     * @param appNgc ???????????????
     * @return ??????
     */
    @Override
    public int updateAppNgcByInstanceId(AppNgc appNgc) {
        return appNgcMapper.updateAppNgcByInstanceId(appNgc);
    }

    /**
     * ??????????????????????????????
     *
     * @param proName ????????????
     * @return ??????
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
        req.setDevicecostremarks("???????????????");
        req.setStuffcostremarks("???????????????");
        req.setTurncosttaxrate(new BigDecimal(9));
        req.setTurncostremarks("???????????????");
        req.setServecosttaxrate(new BigDecimal(6));
        req.setServecostremarks("???????????????");
        req.setBuildcosttaxrate(new BigDecimal(3));
        req.setBuildcostremarks("??????????????????????????????????????????3%????????????????????????9%???????????????????????????????????????");
        req.setHavedevicetaxrate(new BigDecimal(13));
        req.setHavedeviceremarks("??????????????????????????????????????????????????????????????????????????????????????????????????????*????????????????????????=??????=?????????????????????*0.95/????????????*?????????????????????????????????8??????????????????????????????15???");
        req.setHavelinetaxrate(new BigDecimal(9));
        req.setUpholdcosttaxrate(new BigDecimal(6));
        req.setInterestcostremarks("\"????????????????????????????????????????????????????????????????????????????????????????????????4.35%??????????????????????????????????????????????????????????????????   ???????????????   ??????????????????=????????????*4.35%/12*??????\"");
        req.setAttachremarks("????????????12.5%????????????????????????7%??????????????????3%????????????????????????2%???????????????????????????0.5%???");
        req.setCulturebuildremarks("??????3%????????????1.5%??????????????????????????????=????????????*1.5%");
        return req;
    }


    /**
     * ???????????????????????????
     *
     * @param ids ???????????????ID
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
                    /** ???????????? **/
                    appxNgReqService.deleteAppxNgReqByBid(ids[i]);
                }
            }
        }
        return 1;
    }
}