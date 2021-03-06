package com.ruoyi.projectApproval.ApprovalNg.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import cn.hutool.json.JSONUtil;
import com.ruoyi.base.fileManage.domain.CtBuFile;
import com.ruoyi.base.fileManage.service.ICtBuFileService;
import com.ruoyi.collection.domain.ActWorkflowFormDatas;
import com.ruoyi.collection.mapper.CtBuInvoiceReqMapper;
import com.ruoyi.collection.utils.ActUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.dictionary.proType.mapper.CtDicProtypeMapper;
import com.ruoyi.mq.bean.SysConstant;
import com.ruoyi.mq.config.CustomMessageSender;
import com.ruoyi.mq.service.MsgService;
import com.ruoyi.projectApproval.AppNgBase.mapper.AppNgBaseMapper;
import com.ruoyi.projectApproval.AppNgCompany.domain.CtBuApprovaldtNgCompany;
import com.ruoyi.projectApproval.AppNgCompany.domain.CtBuApprovaldtNgCompanys;
import com.ruoyi.projectApproval.AppNgCompany.mapper.CtBuApprovaldtNgCompanyMapper;
import com.ruoyi.projectApproval.AppNgConst.mapper.AppNgConstMapper;
import com.ruoyi.projectApproval.AppNgMaterial.mapper.AppNgMaterialMapper;
import com.ruoyi.projectApproval.AppNgc.domain.AppNgc;
import com.ruoyi.projectApproval.AppNgc.mapper.AppNgcMapper;
import com.ruoyi.system.domain.CtPdeptDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ICtPdeptDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.base.cache.service.CacheService;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.dictionary.proType.domain.CtDicProtype;
import com.ruoyi.projectApproval.AppNgBase.domain.AppNgBase;
import com.ruoyi.projectApproval.AppNgBase.service.IAppNgBaseService;
import com.ruoyi.projectApproval.AppNgConst.domain.AppNgConst;
import com.ruoyi.projectApproval.AppNgConst.service.IAppNgConstService;
import com.ruoyi.projectApproval.AppNgMaterial.domain.AppNgMaterial;
import com.ruoyi.projectApproval.AppNgMaterial.service.IAppNgMaterialService;
import com.ruoyi.projectApproval.AppNgReqList.domain.AppNgReqList;
import com.ruoyi.projectApproval.AppNgReqList.service.IAppNgReqListService;
import com.ruoyi.projectApproval.ApprovalNg.domain.CtBuApprovalNg;
import com.ruoyi.projectApproval.ApprovalNg.mapper.CtBuApprovalNgMapper;
import com.ruoyi.projectApproval.ApprovalNg.service.ICtBuApprovalNgService;
import com.ruoyi.projectApproval.AppxNgReq.domain.AppxNgReq;
import com.ruoyi.projectApproval.AppxNgReq.service.IAppxNgReqService;
import com.ruoyi.system.service.ISysDeptService;

/**
 * @Title: CtBuApprovalNgServiceImpl.java
 * @Description: ????????????Service
 * @Author M.Mu
 * @Date 2020-12-08
 */
@Service
public class CtBuApprovalNgServiceImpl implements ICtBuApprovalNgService {

    @Autowired
    private CtBuApprovalNgMapper ctBuApprovalNgMapper;

    @Autowired
    private IAppNgBaseService appNgBaseService;

    @Autowired
    private IAppNgConstService appNgConstService;

    @Autowired
    private IAppNgMaterialService appNgMaterialService;

    @Autowired
    private IAppxNgReqService appxNgReqService;

    @Autowired
    private RedisTemplate<String, Serializable> redis1;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private IAppNgReqListService appNgReqListService;

    @Autowired
    private ICtPdeptDeptService iCtPdeptDeptService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private CtBuInvoiceReqMapper ctBuInvoiceReqMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private CtDicProtypeMapper ctDicProtypeMapper;
    @Autowired
    private
    CtBuApprovaldtNgCompanyMapper ctBuApprovaldtNgCompanyMapper;

    @Autowired
    private ICtBuFileService fileService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private CustomMessageSender customMessageSender;

    @Autowired
    private ActUtils actUtils;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private  AppNgBaseMapper appNgBaseMapper;
    @Autowired
    private AppNgConstMapper appNgConstMapper;
    @Autowired
    private AppNgMaterialMapper appNgMaterialMapper;
    @Autowired
    private AppNgcMapper appNgcMapper;

    //??????????????????
    @Override
    public List<CtBuApprovalNg> selectCtBuApprovalNgLists(CtBuApprovalNg ctBuApprovalNg) {
        return  ctBuApprovalNgMapper.selectCtBuApprovalNgListByJq(ctBuApprovalNg);
    }
    /**
     * ????????????
     *
     * @param id ???????????????(?????????)ID
     * @return ???????????????(?????????)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyCtBuApprovalNgById(String id) {
        /** ??????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        CtBuApprovalNg ctBuApprovalNg = ctBuApprovalNgMapper.selectCtBuApprovalNgById(id);
        if (ctBuApprovalNg != null && StringUtils.isNotBlank(ctBuApprovalNg.getId())) {
            String code = this.createReqNo(loginUser.getUser().getDept().getDeptId());
            ctBuApprovalNg.setId(UUID.fastUUID().toString(true));
            ctBuApprovalNg.setStatus(0);
            ctBuApprovalNg.setStatusPs("??????");
            ctBuApprovalNg.setCreateBy(loginUser.getUser().getUserId().toString());
            ctBuApprovalNg.setCreateTime(new Date());
            ctBuApprovalNg.setInstanceId(null);
            ctBuApprovalNg.setProjectCode(code);
            ctBuApprovalNg.setGhbState(null);
            ctBuApprovalNg.setDelFlag(1);
            ctBuApprovalNg.setReqNo(ctBuApprovalNg.getReqNo()+"_copy");
            ctBuApprovalNg.setProName(ctBuApprovalNg.getProName()+"_copy");
            ctBuApprovalNgMapper.insertCtBuApprovalNg(ctBuApprovalNg);
            //??????company
            CtBuApprovaldtNgCompany company = new CtBuApprovaldtNgCompany();
            company.setDelFlag("1");
            company.setBid(id);
            List<CtBuApprovaldtNgCompany> clist = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(company);
            if(clist!=null && clist.size()>0){
                String cid = "";
                for (CtBuApprovaldtNgCompany com: clist) {
                    CtBuApprovaldtNgCompany comOld = com;
                    cid = com.getBranchInvolvedId();
                    com.setCreateBy(loginUser.getUser().getUserId().toString());
                    com.setCreateTime(new Date());
                    com.setDelFlag("1");
                    com.setId(UUID.fastUUID().toString(true));
                    com.setBid(ctBuApprovalNg.getId());
                    ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(com);
                    //??????base
                    AppNgBase base = new AppNgBase();
                    base.setBid(id);
                    base.setDelFlag(1);
                    base.setCid(cid);
                    List<AppNgBase> appNgBases = appNgBaseService.selectAppNgBaseList(base);
                    if(appNgBases!=null && appNgBases.size()>0){
                        for (AppNgBase appNgBase:appNgBases) {
                            appNgBase.setCreateBy(loginUser.getUser().getUserId().toString());
                            appNgBase.setCreateTime(new Date());
                            appNgBase.setBid(ctBuApprovalNg.getId());
                            appNgBase.setCid(com.getId());
                            appNgBase.setId(UUID.fastUUID().toString(true));
                            appNgBaseService.insertAppNgBase(appNgBase);
                        }
                    }
                    /** ??????????????? **/
                    AppNgConst appNgConst = new AppNgConst();
                    appNgConst.setBid(id);
                    appNgConst.setCid(cid);
                    List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
                    if (appNgConstList != null && !appNgConstList.isEmpty()) {
                        for (AppNgConst appCon:appNgConstList) {
                            appCon.setCreateBy(loginUser.getUser().getUserId().toString());
                            appCon.setCreateTime(new Date());
                            appCon.setBid(ctBuApprovalNg.getId());
                            appCon.setCid(com.getId());
                            appCon.setId(UUID.fastUUID().toString(true));
                            appNgConstService.insertAppNgConst(appCon);
                        }
                    }
                    /** ?????????????????? **/
                    AppNgMaterial appNgMaterial = new AppNgMaterial();
                    appNgMaterial.setBid(id);
                    appNgMaterial.setCid(cid);
                    List<AppNgMaterial> appNgMaterialList = appNgMaterialService.selectAppNgMaterialList(appNgMaterial);
                    BigDecimal cltotal = new BigDecimal(0);
                    if (appNgMaterialList != null && !appNgMaterialList.isEmpty()) {
                        for (AppNgMaterial mate:appNgMaterialList) {
                            mate.setId(UUID.fastUUID().toString(true));
                            mate.setCid(com.getId());
                            mate.setDelFlag(1);
                            mate.setBid(ctBuApprovalNg.getId());
                            mate.setCreateBy(loginUser.getUser().getUserId().toString());
                            mate.setCreateTime(new Date());
                            appNgMaterialService.insertAppNgMaterial(mate);
                        }
                    }


                }
            }
            /** ???????????? **/
            AppxNgReq appxNgReq = new AppxNgReq();
            appxNgReq.setBid(ctBuApprovalNg.getId());
            List<AppxNgReq> appxNgReqs = appxNgReqService.selectAppxNgReqList(appxNgReq);
            if (appxNgReqs != null && appxNgReqs.size()>0) {
                appxNgReq = appxNgReqs.get(0);
                appxNgReq.setId(UUID.fastUUID().toString(true));
                appxNgReq.setBid(ctBuApprovalNg.getId());
                appxNgReq.setCreateBy(loginUser.getUser().getUserId().toString());
                appxNgReq.setAppxNgNo(ctBuApprovalNg.getReqNo());
                appxNgReq.setDelFlag(1);
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
    }

    /**
     * ?????????????????????(?????????)
     *
     * @param id ???????????????(?????????)ID
     * @return ???????????????(?????????)
     */
    @Override
    public CtBuApprovalNg selectCtBuApprovalNgReqById(String id) {
        String key = "CtBuApprovalNg";
        String hashKey = id;
        /** ????????????ID???????????? **/
        CtBuApprovalNg ctBuApprovalNg = ctBuApprovalNgMapper.selectCtBuApprovalNgById(id);
        if (ctBuApprovalNg != null && StringUtils.isNotBlank(ctBuApprovalNg.getId())) {

            AppNgBase abase = new AppNgBase();
            abase.setBid(id);
            List<AppNgBase> appNgBases = appNgBaseService.selectAppNgBaseList(abase);
            ctBuApprovalNg.setAppNgBaseList(appNgBases);


            //???????????????
            CtBuApprovaldtNgCompany ctBuApprovaldtNgCompany = new CtBuApprovaldtNgCompany();
            ctBuApprovaldtNgCompany.setDelFlag("1");
            ctBuApprovaldtNgCompany.setBid(id);
            List<CtBuApprovaldtNgCompany> ctBuApprovaldtNgCompanies = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(ctBuApprovaldtNgCompany);
            ctBuApprovalNg.setCompanyList(ctBuApprovaldtNgCompanies);
            /** ???????????? **/
            AppxNgReq appxNgReq = appxNgReqService.selectAppxNgReqByBid(id);
            AppxNgReq req = this.getAppxNgReq();
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getId())) {
                /** ???????????????????????? **/
                AppNgReqList appNgReqList = new AppNgReqList();
                appNgReqList.setPid(appxNgReq.getId());
                List<AppNgReqList> reqList = appNgReqListService.selectAppNgReqListList(appNgReqList);
                if (reqList != null && !reqList.isEmpty()) {
                    appxNgReq.setDataList(reqList);
                }
                ctBuApprovalNg.setAppxNgReq(appxNgReq);
            } else {
                req.setApplyCompany(ctBuApprovalNg.getBelongDeptName());

            }

            ActWorkflowFormDatas data = new ActWorkflowFormDatas();
            data.setBusinessKey(ctBuApprovalNg.getId());
            List<ActWorkflowFormDatas> alist = ctBuInvoiceReqMapper.selectGroupActWorkflowFormDataLists(data);
            ctBuApprovalNg.setActWorkflowFormDatas(alist);
            //findNextAppNo
            ctBuApprovalNg.setProjectCode(findNextAppNo());
            //??????????????????
            /** ??????????????? **/
            AppNgConst appNgConst = new AppNgConst();
            appNgConst.setBid(ctBuApprovalNg.getId());
            BigDecimal sgtotal = new BigDecimal(0);
            List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
            if (appNgConstList != null && !appNgConstList.isEmpty()) {
                for (AppNgConst base:appNgConstList) {
                    sgtotal = sgtotal.add(base.getDaysAmount());
                }
            }
            ctBuApprovalNg.setSgtotal(sgtotal);
            /** ?????????????????? **/
            AppNgMaterial appNgMaterial = new AppNgMaterial();
            appNgMaterial.setBid(ctBuApprovalNg.getId());
            List<AppNgMaterial> appNgMaterialList = appNgMaterialService.selectAppNgMaterialList(appNgMaterial);
            BigDecimal cltotal = new BigDecimal(0);
            if (appNgMaterialList != null && !appNgMaterialList.isEmpty()) {
                for (AppNgMaterial base:appNgMaterialList) {
                    cltotal = cltotal.add(base.getMaterialAmount());
                }
            }
            if (appxNgReq == null || StringUtils.isEmpty(appxNgReq.getId())){
                req.setStuffcostmoney(cltotal);
                req.setBuildcostmoney(sgtotal);
                req.setApplyDate(new Date());
                req.setProType("9f30ebd46d3c428cbc5e54f7175eb4f2");
                req.setApplyCompany(SecurityUtils.getLoginUser().getUser().getDept().getDeptName());
                req.setAppxNgNo(findNextAppNo());
                ctBuApprovalNg.setAppxNgReq(req);
            }

            return ctBuApprovalNg;
        } else {
            ctBuApprovalNg = new CtBuApprovalNg();
            ctBuApprovalNg.setProjectCode(findNextAppNo());
            ctBuApprovalNg.setBelongDeptName(SecurityUtils.getLoginUser().getUser().getDept().getDeptName());
            AppxNgReq req = this.getAppxNgReq();
            req.setApplyDate(new Date());
            req.setProType("9f30ebd46d3c428cbc5e54f7175eb4f2");
            req.setApplyCompany(SecurityUtils.getLoginUser().getUser().getDept().getDeptName());
            req.setAppxNgNo(findNextAppNo());
            ctBuApprovalNg.setAppxNgReq(req);//devicecosttaxrate
            return ctBuApprovalNg;
        }
    }

   public AppxNgReq getAppxNgReq(){
       AppxNgReq req = new AppxNgReq();
       req.setAppxNgNo(findNextAppNo());
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
     * ?????????????????????(?????????)
     *
     * @param id ???????????????(?????????)ID
     * @return ???????????????(?????????)
     */
    @Override
    public CtBuApprovalNg selectCtBuApprovalNgById(String id) {
        String key = "CtBuApprovalNg";
        String hashKey = id;
        CtBuApprovalNg ctBuApprovalNg = ctBuApprovalNgMapper.selectCtBuApprovalNgById(id);
        if (ctBuApprovalNg != null && StringUtils.isNotBlank(ctBuApprovalNg.getId())) {
            //??????company
            CtBuApprovaldtNgCompany company = new CtBuApprovaldtNgCompany();
            company.setDelFlag("1");
            company.setBid(ctBuApprovalNg.getId());
            List<CtBuApprovaldtNgCompany> clist = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(company);
            ctBuApprovalNg.setCompanyList(clist);
            ctBuApprovalNg.setProNames(ctBuApprovalNg.getProName());


            AppxNgReq appxNgReq = appxNgReqService.selectAppxNgReqByBid(ctBuApprovalNg.getId());
            ctBuApprovalNg.setAppxNgReq(appxNgReq);
            ActWorkflowFormDatas data = new ActWorkflowFormDatas();
            data.setBusinessKey(ctBuApprovalNg.getId());
            List<ActWorkflowFormDatas> alist = ctBuInvoiceReqMapper.selectGroupActWorkflowFormDataLists(data);
            ctBuApprovalNg.setActWorkflowFormDatas(alist);
            return ctBuApprovalNg;
        } else {
            ctBuApprovalNg = new CtBuApprovalNg();
            ctBuApprovalNg.setProjectCode(findNextAppNo());
            return ctBuApprovalNg;
        }
    }


    /**
     * ???????????????
     *
     * @param map ???????????????(?????????)ID
     * @return ???????????????(?????????)
     */
    @Override
    public CtBuApprovalNg selectCtBuApprovalFgsNgById(Map map) {
        /** ????????????ID???????????? **/
        CtBuApprovalNg ctBuApprovalNg = new CtBuApprovalNg();
        AppNgBase base = new AppNgBase();
        base.setBid(map.get("bid").toString());
        base.setCid(map.get("cid").toString());
        List<AppNgBase> appNgBases = appNgBaseService.selectAppNgBaseList(base);
        if (appNgBases != null && appNgBases.size() > 0) {
            AppNgBase appNgBase = appNgBases.get(0);
            if (appNgBase != null && StringUtils.isNotBlank(appNgBase.getId())) {
                if (StringUtils.isNotBlank(appNgBase.getBranchInvolvedId())) {
                    Integer[] branIdStr = com.ruoyi.common.utils.StringUtils
                            .stringToIntArr(appNgBase.getBranchInvolvedId());
                    if (branIdStr != null && branIdStr.length > 0) {
                        appNgBase.setBranIdStr(branIdStr);
                    }
                }
                ctBuApprovalNg.setAppNgBase(appNgBase);
            } else {
                ctBuApprovalNg.setAppNgBase(new AppNgBase());
            }
        }
        /** ??????????????? **/
        AppNgConst appNgConst = new AppNgConst();
        appNgConst.setBid(map.get("bid").toString());
        appNgConst.setCid(map.get("cid").toString());
        List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
        if (appNgConstList != null && !appNgConstList.isEmpty()) {
            ctBuApprovalNg.setAppNgConstList(appNgConstList);
        } else {
            ctBuApprovalNg.setAppNgConstList(new ArrayList<>());
        }

        /** ?????????????????? **/
        AppNgMaterial appNgMaterial = new AppNgMaterial();
        appNgMaterial.setBid(map.get("bid").toString());
        appNgMaterial.setCid(map.get("cid").toString());
        List<AppNgMaterial> appNgMaterialList = appNgMaterialService.selectAppNgMaterialList(appNgMaterial);
        if (appNgMaterialList != null && !appNgMaterialList.isEmpty()) {
            ctBuApprovalNg.setAppNgMaterialList(appNgMaterialList);
        } else {
            ctBuApprovalNg.setAppNgMaterialList(new ArrayList<>());
        }
        return ctBuApprovalNg;

    }

        /**
         * ?????????????????????(?????????)
         *
         * @param id ???????????????(?????????)ID
         * @return ???????????????(?????????)
         */
        public CtBuApprovalNg selectCtBuApprovalNgByIdOld(String id) {
            String key = "CtBuApprovalNg";
            String hashKey = id;
            /** ????????????ID???????????? **/
            if (redis1.opsForHash().hasKey(key, hashKey)) {
                CtBuApprovalNg ctBuApprovalNg = (CtBuApprovalNg) redis1.opsForHash().get(key, hashKey);
                if (ctBuApprovalNg != null && StringUtils.isNotBlank(ctBuApprovalNg.getId())) {
                    return ctBuApprovalNg;
                } else {
                    return new CtBuApprovalNg();
                }
            } else {
                CtBuApprovalNg ctBuApprovalNg = ctBuApprovalNgMapper.selectCtBuApprovalNgById(id);
                if (ctBuApprovalNg != null && StringUtils.isNotBlank(ctBuApprovalNg.getId())) {
                    /** ?????????????????? **/
                    AppNgBase appNgBase = appNgBaseService.selectAppNgBaseByBid(id);
                    if (appNgBase != null && StringUtils.isNotBlank(appNgBase.getId())) {
                        if (StringUtils.isNotBlank(appNgBase.getBranchInvolvedId())) {
                            Integer[] branIdStr = com.ruoyi.common.utils.StringUtils
                                    .stringToIntArr(appNgBase.getBranchInvolvedId());
                            if (branIdStr != null && branIdStr.length > 0) {
                                appNgBase.setBranIdStr(branIdStr);
                            }
                        }
                        ctBuApprovalNg.setAppNgBase(appNgBase);
                    } else {
                        ctBuApprovalNg.setAppNgBase(new AppNgBase());
                    }
                    /** ??????????????? **/
                    AppNgConst appNgConst = new AppNgConst();
                    appNgConst.setBid(id);
                    List<AppNgConst> appNgConstList = appNgConstService.selectAppNgConstList(appNgConst);
                    if (appNgConstList != null && !appNgConstList.isEmpty()){
                        ctBuApprovalNg.setAppNgConstList(appNgConstList);
                    } else {
                        ctBuApprovalNg.setAppNgConstList(new ArrayList<>());
                    }

                    /** ?????????????????? **/
                    AppNgMaterial appNgMaterial = new AppNgMaterial();
                    appNgMaterial.setBid(id);
                    List<AppNgMaterial> appNgMaterialList = appNgMaterialService.selectAppNgMaterialList(appNgMaterial);
                    if (appNgMaterialList != null && !appNgMaterialList.isEmpty()) {
                        ctBuApprovalNg.setAppNgMaterialList(appNgMaterialList);
                    } else {
                        ctBuApprovalNg.setAppNgMaterialList(new ArrayList<>());
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
                        ctBuApprovalNg.setAppxNgReq(appxNgReq);
                    } else {
                        ctBuApprovalNg.setAppxNgReq(new AppxNgReq());
                    }
                    ActWorkflowFormDatas data = new ActWorkflowFormDatas();
                    data.setBusinessKey(ctBuApprovalNg.getId());
                    List<ActWorkflowFormDatas> alist = ctBuInvoiceReqMapper.selectGroupActWorkflowFormDataLists(data);
                    ctBuApprovalNg.setActWorkflowFormDatas(alist);
                    //findNextAppNo
                    ctBuApprovalNg.setProjectCode(findNextAppNo());
                    redis1.opsForHash().put(key, hashKey, ctBuApprovalNg);

                    return ctBuApprovalNg;
                } else {
                    ctBuApprovalNg = new CtBuApprovalNg();
                    ctBuApprovalNg.setProjectCode(findNextAppNo());
                    return ctBuApprovalNg;
                }
            }
        }

    /**
     * ?????????????????????(?????????)??????
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ???????????????(?????????)
     */
    @Override
    @DataScope(deptAlias = "dept")
    public List<CtBuApprovalNg> selectCtBuApprovalNgList(CtBuApprovalNg ctBuApprovalNg) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysRole> roles = loginUser.getUser().getRoles();
        ctBuApprovalNg.setStatusPs(null);
        if(roles!=null && roles.size()>0){
            for (SysRole role: roles) {
                if(role.getRoleId()==121){
                    ctBuApprovalNg.setIsAdvanceNeed("Y");
                    ctBuApprovalNg.setIsPlanConfirm("Y");
                    ctBuApprovalNg.setStatusPs("121");
                }
            }
        }
        List<CtBuApprovalNg> list = ctBuApprovalNgMapper.selectCtBuApprovalNgList(ctBuApprovalNg);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                CtBuApprovalNg approvalNg = list.get(i);
                /** ???????????????????????? **/
                if (approvalNg != null && StringUtils.isNotBlank(approvalNg.getProType())) {
                    CtDicProtype protype = cacheService.findProtypeById(approvalNg.getProType());
                    if (protype != null && StringUtils.isNotBlank(protype.getTypeName())) {
                        approvalNg.setProTypeName(protype.getTypeName());
                    }
                }


            }
        }

        return list;
    }



    /**
     * ?????????????????????(?????????)
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCtBuApprovalNg(CtBuApprovalNg ctBuApprovalNg) {
        /** ??????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ctBuApprovalNg.setId(UUID.fastUUID().toString(true));
        ctBuApprovalNg.setDelFlag(1);
        String userId = "";
        Long deptId = 1L;
        ctBuApprovalNg.setProType("9f30ebd46d3c428cbc5e54f7175eb4f2");
        ctBuApprovalNg.setProTypeName("????????????");
        String companyId = "";
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();

        } catch (Exception e) {

        }
        ctBuApprovalNg.setCreateBy(userId);
        ctBuApprovalNg.setCreateTime(DateUtils.getNowDate());
        ctBuApprovalNg.setCreateName(loginUser.getUsername());
        ctBuApprovalNg.setBelongDeptid((SecurityUtils.getLoginUser().getUser().getDeptId())+"");
        if (ctBuApprovalNg.getBelongDeptid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongDeptid()));
            ctBuApprovalNg.setBelongDeptName(sysDept.getDeptName());
        }
        ctBuApprovalNg.setBelongCompanyid((SecurityUtils.getLoginUser().getUser().getCompanyId())+"");
        if (ctBuApprovalNg.getBelongCompanyid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongCompanyid()));
            ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
        }

        if (ctBuApprovalNg.getProType() != null){
            CtDicProtype ctDicProtype = ctDicProtypeMapper.selectCtDicProtypeById(ctBuApprovalNg.getProType());
            ctBuApprovalNg.setProTypeName(ctDicProtype.getTypeName());
        }
       //??????????????????
        List<CtBuApprovaldtNgCompany> companyList = ctBuApprovalNg.getCompanyList();
        int company_num = 0;
        if(companyList!=null && companyList.size()>0){
            for (CtBuApprovaldtNgCompany c: companyList) {
                c.setId(UUID.fastUUID().toString(true));
                c.setBid(ctBuApprovalNg.getId());
                c.setBranchInvolvedId(c.getBranchInvolvedId());
                c.setDelFlag("1");
                c.setCreateTime(new Date());
                c.setCreateBy(loginUser.getUser().getUserName());
                ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(c);
                company_num++;
                /** ?????????????????? **/
                List<CtBuApprovaldtNgCompanys> companys = ctBuApprovalNg.getCompanys();
                if(companys!=null && companys.size()>0){
                    for (CtBuApprovaldtNgCompanys company:companys) {
                        String detpId = company.getDeptId();
                        if(Long.parseLong(detpId)==(Long.parseLong(c.getBranchInvolvedId()))){
                            List<AppNgBase> appNgBaseList = company.getAppNgBaseList();
                            if(appNgBaseList!=null && appNgBaseList.size()>0){
                                for ( AppNgBase appNgBase:appNgBaseList) {
                                    appNgBase.setId(UUID.fastUUID().toString(true));
                                    appNgBase.setBid(ctBuApprovalNg.getId());
                                    appNgBase.setCreateBy(userId);
                                    appNgBase.setCid(c.getId());
                                    appNgBase.setDelFlag(1);
                                    appNgBase.setBranchInvolvedId(detpId);
                                    appNgBaseService.insertAppNgBase(appNgBase);
                                }
                            }
                            List<List<AppNgConst>> appNgConstLists = company.getAppNgConstLists();
                            if(appNgConstLists!=null && appNgConstLists.size()>0){
                                for ( List<AppNgConst> clist:appNgConstLists) {
                                    if(clist!=null && clist.size()>0){
                                        for (int i = 0; i < clist.size(); i++){
                                            AppNgConst appNgConst = clist.get(i);
                                            appNgConst.setId(UUID.fastUUID().toString(true));
                                            appNgConst.setBid(ctBuApprovalNg.getId());
                                            appNgConst.setCreateBy(userId);
                                            appNgConst.setDelFlag(1);
                                            appNgConst.setCid(c.getId());
                                            appNgConstService.insertAppNgConst(appNgConst);
                                        }
                                    }
                                }
                            }
                            List<List<AppNgMaterial>> appNgMaterialLists = company.getAppNgMaterialLists();
                            if(appNgMaterialLists!=null && appNgMaterialLists.size()>0){
                                for ( List<AppNgMaterial> mlist:appNgMaterialLists) {
                                    if(mlist!=null && mlist.size()>0){
                                        for (int i = 0; i < mlist.size(); i++) {
                                            AppNgMaterial appNgMaterial = mlist.get(i);
                                            appNgMaterial.setId(UUID.fastUUID().toString(true));
                                            appNgMaterial.setBid(ctBuApprovalNg.getId());
                                            appNgMaterial.setCreateBy(userId);
                                            appNgMaterial.setDelFlag(1);
                                            appNgMaterial.setCid(c.getId());
                                            appNgMaterialService.insertAppNgMaterial(appNgMaterial);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            List<String> list = ctBuApprovalNg.getSelectComp();
            if(list!=null && list.size()>0){
                for (String str:list) {
                    CtBuApprovaldtNgCompany any = new CtBuApprovaldtNgCompany();
                    any.setBid(ctBuApprovalNg.getId());
                    int index = str.indexOf("(")+1;
                    String val = str.substring(index,str.length()-1);
                    any.setBranchInvolvedId(val);
                    List<CtBuApprovaldtNgCompany> companies = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(any);
                    if (companies != null && companies.size() > 0) {
                        any = companies.get(0);
                        any.setId(any.getId());
                        any.setUpdateTime(new Date());
                        any.setUpdateBy(loginUser.getUser().getUserName());
                        ctBuApprovaldtNgCompanyMapper.updateCtBuApprovaldtNgCompany(any);
                    } else {
                        any.setId(UUID.fastUUID().toString(true));
                        any.setBid(ctBuApprovalNg.getId());
                        any.setBranchInvolvedId(any.getBranchInvolvedId());
                        any.setDelFlag("1");
                        any.setCreateTime(new Date());
                        any.setCreateBy(loginUser.getUser().getUserName());
                        ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(any);
                    }

                }
            }
        }

        /** ???????????? **/
        AppxNgReq appxNgReq = ctBuApprovalNg.getAppxNgReq();
        if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getAppxNgNo())) {
            appxNgReq.setId(UUID.fastUUID().toString(true));
            appxNgReq.setBid(ctBuApprovalNg.getId());
            appxNgReq.setCreateBy(userId);
            appxNgReq.setAppxNgNo(createReqNo(deptId));
            appxNgReq.setDelFlag(1);
            appxNgReq.setAppxNgName(ctBuApprovalNg.getProName());
            appxNgReq.setProType(ctBuApprovalNg.getProType());
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
        /** ???????????????????????? **/
        String widthName = "";
        ctBuApprovalNg.setReqNo(appxNgReq==null || appxNgReq.getAppxNgNo()==null?"":appxNgReq.getAppxNgNo());
        if (ctBuApprovalNg.getStatus() == 0){
            ctBuApprovalNg.setStatusPs("??????");
        } else if (ctBuApprovalNg.getStatus() == 1){
            ctBuApprovalNg.setStatusPs("?????????");
            //????????????????????????????????????

            if (loginUser != null && loginUser.getUser() != null) {
                long dept_id = loginUser.getUser().getDept().getDeptId();
                long parent_id = loginUser.getUser().getDept().getParentId();
                boolean flag = true;
                String key = "";
                Map<String, Object> variables = this.getVarirs(dept_id,ctBuApprovalNg,flag);
                if (ctBuApprovalNg.getInstanceType() == 1){
                    variables = this.getSzVarirs(dept_id,ctBuApprovalNg,flag);
                    if(company_num==1){
                        List<String> signList = new ArrayList<>();
                        //???????????????
                        Map params = new HashMap();
                        params.put("roleId",109);
                        params.put("deptId",227);
                        List<SysUser>list = ActUtils.getVarirs(params,iSysUserService);
                        if(list!=null && list.size()>0){
                            for (SysUser user:list ) {
                                signList.add(user.getUserName());
                            }
                        }
                        variables.put("ifgh","1");
                        variables.put("ghzzbsh",org.apache.commons.lang.StringUtils.join(signList.toArray(),","));
                    }else{
                        variables.put("ifgh","0");
                    }
                    widthName = "??????????????????(?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "important_project";
                    //flag = false;
                    variables.put("business",loginUser.getUsername());
                } else if (ctBuApprovalNg.getInstanceType() == 2){
                    widthName = "????????????(?????????10?????? ??? 5?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "xmlx2";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());

                    if(company_num==1){
                        List<String> signList = new ArrayList<>();
                        //???????????????
                        Map params = new HashMap();
                        params.put("roleId",109);
                        params.put("deptId",227);
                        List<SysUser>list = ActUtils.getVarirs(params,iSysUserService);
                        if(list!=null && list.size()>0){
                            for (SysUser user:list ) {
                                signList.add(user.getUserName());
                            }
                        }
                        variables.put("ifgh","1");
                        variables.put("ghbsh",org.apache.commons.lang.StringUtils.join(signList.toArray(),","));
                    }else{
                        variables.put("ifgh","0");
                    }
                } else if (ctBuApprovalNg.getInstanceType() == 3){
                    widthName = "???????????????(???????????????10?????????5?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "xmlx3";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());
                } else if (ctBuApprovalNg.getInstanceType() == 4){
                    widthName = "?????????????????????????????????????????????????????????(???????????????10?????????????????????)-" + loginUser.getUser().getNickName();
                    key = "xmlx4";
                    variables = this.getVarirs4( loginUser,ctBuApprovalNg.getInstanceType());

                } else if (ctBuApprovalNg.getInstanceType() == 5){
                    widthName = "???????????????(?????????????????????10?????? ??? ????????????5???????????????)-" + loginUser.getUser().getNickName();
                    key = "cliquish_project";
                    flag = false;

                    variables.put("business",loginUser.getUsername());
                }
//                Map map = new HashMap();
//                map.put("loginUser",loginUser);
//                map.put("key",key);
//                map.put("widthName",widthName);
//                map.put("id",ctBuApprovalNg.getId());
//                map.put("variables",variables);

                ctBuApprovalNg.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuApprovalNg.getId(), variables
                        , taskService, taskRuntime, key, widthName));
            }
        }
        ctBuApprovalNg.setReqNo(ctBuApprovalNg.getReqNo()==null || "".equals(ctBuApprovalNg.getReqNo())?this.createReqNo(deptId):ctBuApprovalNg.getReqNo());
        /** ???????????? **/
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ctBuApprovalNg.getConstructionPlan())) {
            String[] str = ctBuApprovalNg.getConstructionPlan().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //?????????????????? ptype:1
                    file.setPid(ctBuApprovalNg.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        int num = ctBuApprovalNgMapper.insertCtBuApprovalNg(ctBuApprovalNg);
        if(num>0){
            if (ctBuApprovalNg.getStatus() == 1){
                // ??????????????????Id????????????
                Map map = new HashMap();
                map.put("instanceId",ctBuApprovalNg.getInstanceId());
                map.put("name",widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE,SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map),0);//30???????????????
            }
//            if (ctBuApprovalNg.getStatus() == 4) {
//                // ??????????????????Id????????????
//                Map map = new HashMap();
//                //????????????
//                List<String> ulist = new ArrayList<>();
//                String varirs = ActUtils.getVarirs(null, "121", iSysUserService);
//                if (varirs != null && !"".equals(varirs)) {
//                    String[] strs = varirs.split(",");
//                    for (String str : strs) {
//                        ulist.add(str);
//                    }
//                }
//                if (ulist != null && ulist.size() > 0) {
//                    String popName = "";
//                    if (ctBuApprovalNg.getInstanceType() == 1) {
//                        popName = "??????????????????(?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 2) {
//                        popName = "????????????(?????????10?????? ??? 5?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 3) {
//                        popName = "???????????????(???????????????10?????????5?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 4) {
//                        popName = "?????????????????????????????????????????????????????????(???????????????10?????????????????????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 5) {
//                        popName = "???????????????(?????????????????????10?????? ??? ????????????5???????????????)-" + loginUser.getUser().getNickName();
//                    }
//
//                    map.put("users", ulist);
//                    map.put("name", popName + "-?????????");
//                    map.put("status","4");
//                    msgService.sendDelayMsgToMQ(SysConstant.SYS_ORDER_DELAY_EXCHANGE, SysConstant.SYS_ORDER_DELAY_KEY, JSONUtil.toJsonStr(map), 0);//30???????????????
//                }
//            }
        }

        return num;
    }

    /**
     * ?????????????????????(?????????)
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Transactional
    public int insertCtBuApprovalNgOld(CtBuApprovalNg ctBuApprovalNg) {
        ctBuApprovalNg.setId(UUID.fastUUID().toString(true));
        ctBuApprovalNg.setDelFlag(1);
        String userId = "";
        Long deptId = 1L;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
        } catch (Exception e) {

        }
        ctBuApprovalNg.setCreateBy(userId);
        ctBuApprovalNg.setCreateTime(DateUtils.getNowDate());
//        ctBuApprovalNg.setStatus(0);
//        ctBuApprovalNg.setStatusPs("??????");

        /** ??????????????? **/
//        if (StringUtils.isNotBlank(ctBuApprovalNg.getBelongCompanyid())) {
//            SysDept sysDept = cacheService.findSysDeptById(ctBuApprovalNg.getBelongCompanyid());
//            if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptName())) {
//                ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
//            }
//        }
//        /** ???????????? **/
//        if (StringUtils.isNotBlank(ctBuApprovalNg.getBelongDeptid())) {
//            SysDept dept = cacheService.findSysDeptById(ctBuApprovalNg.getBelongDeptid());
//            if (dept != null && StringUtils.isNotBlank(dept.getDeptName())) {
//                ctBuApprovalNg.setBelongDeptName(dept.getDeptName());
//            }
//        }
        if (ctBuApprovalNg.getBelongCompanyid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongCompanyid()));
            ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
        }
        if (ctBuApprovalNg.getBelongDeptid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongDeptid()));
            ctBuApprovalNg.setBelongDeptName(sysDept.getDeptName());
        }
        if (ctBuApprovalNg.getProType() != null){
            CtDicProtype ctDicProtype = ctDicProtypeMapper.selectCtDicProtypeById(ctBuApprovalNg.getProType());
            ctBuApprovalNg.setProTypeName(ctDicProtype.getTypeName());
        }
//        if (StringUtils.isNotBlank(ctBuApprovalNg.getProType())) {
//            CtDicProtype protype = cacheService.findProtypeById(ctBuApprovalNg.getProType());
//            if (protype != null && StringUtils.isNotBlank(protype.getId())) {
//                ctBuApprovalNg.setProTypeName(protype.getTypeName());
//            }
//        }


        /** ?????????????????? **/
        AppNgBase appNgBase = ctBuApprovalNg.getAppNgBase();
        appNgBase.setId(UUID.fastUUID().toString(true));
        appNgBase.setBid(ctBuApprovalNg.getId());
        appNgBase.setCreateBy(userId);
        appNgBase.setDelFlag(1);
        if (appNgBase.getBranIdStr() != null && appNgBase.getBranIdStr().length > 0) {
            String branchInvolvedId = "";
            Integer[] branIdStr = appNgBase.getBranIdStr();
            for (int i = 0; i < branIdStr.length; i++) {
                branchInvolvedId += branIdStr[i] + ",";
            }
            appNgBase.setBranchInvolvedId(branchInvolvedId);
        }
        appNgBaseService.insertAppNgBase(appNgBase);

        /** ?????????????????? **/
        List<AppNgMaterial> mateList = ctBuApprovalNg.getAppNgMaterialList();
        if (mateList != null && !mateList.isEmpty()) {
            for (int i = 0; i < mateList.size(); i++) {
                AppNgMaterial appNgMaterial = mateList.get(i);
                appNgMaterial.setId(UUID.fastUUID().toString(true));
                appNgMaterial.setBid(ctBuApprovalNg.getId());
                appNgMaterial.setCreateBy(userId);
                appNgMaterial.setDelFlag(1);
                appNgMaterialService.insertAppNgMaterial(appNgMaterial);
            }
        }

        /** ??????????????? **/
        List<AppNgConst> constList = ctBuApprovalNg.getAppNgConstList();
        if (constList != null && !constList.isEmpty()){
            for (int i = 0; i < constList.size(); i++){
                AppNgConst appNgConst = constList.get(i);
                appNgConst.setId(UUID.fastUUID().toString(true));
                appNgConst.setBid(ctBuApprovalNg.getId());
                appNgConst.setCreateBy(userId);
                appNgConst.setDelFlag(1);
                appNgConstService.insertAppNgConst(appNgConst);
            }
        }

        /** ???????????? **/
        AppxNgReq appxNgReq = ctBuApprovalNg.getAppxNgReq();
        if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getAppxNgNo())) {
            appxNgReq.setId(UUID.fastUUID().toString(true));
            appxNgReq.setBid(ctBuApprovalNg.getId());
            appxNgReq.setCreateBy(userId);
            appxNgReq.setAppxNgNo(ctBuApprovalNg.getReqNo()==null?createReqNo(deptId):ctBuApprovalNg.getReqNo());
            appxNgReq.setDelFlag(1);
            appxNgReq.setAppxNgName(ctBuApprovalNg.getProName());
            appxNgReq.setProType(ctBuApprovalNg.getProType());
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
        /** ???????????????????????? **/
        ctBuApprovalNg.setReqNo(appxNgReq.getAppxNgNo()==null?createReqNo(deptId):appxNgReq.getAppxNgNo());
        if (ctBuApprovalNg.getStatus() == 0){
            ctBuApprovalNg.setStatusPs("??????");
        } else if (ctBuApprovalNg.getStatus() == 1){
            ctBuApprovalNg.setStatusPs("?????????");
            //????????????????????????????????????
            /** ??????????????? **/
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null && loginUser.getUser() != null) {
                long dept_id = loginUser.getUser().getDept().getDeptId();
                long parent_id = loginUser.getUser().getDept().getParentId();
                boolean flag = true;
                String key = "";
                String widthName = "";
                Map<String, Object> variables = this.getVarirs(dept_id,ctBuApprovalNg,flag);
                if (ctBuApprovalNg.getInstanceType() == 1){
                    widthName = "??????????????????(?????????????????????50??????)-" + loginUser.getUser().getUserName();
                    key = "important_project";
                    //flag = false;
                    variables.put("business",loginUser.getUsername());
                } else if (ctBuApprovalNg.getInstanceType() == 2){
                    widthName = "????????????(?????????10?????? ??? 5?????????????????????50??????)-" + loginUser.getUser().getUserName();
                    key = "xmlx2";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());
                } else if (ctBuApprovalNg.getInstanceType() == 3){
                    widthName = "???????????????(???????????????10?????????5?????????????????????50??????)-" + loginUser.getUser().getUserName();
                    key = "xmlx3";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());
                } else if (ctBuApprovalNg.getInstanceType() == 4){
                    widthName = "?????????????????????????????????????????????????????????(???????????????10?????????????????????)" + loginUser.getUser().getUserName();
                    key = "xmlx4";
                    variables = this.getVarirs4( loginUser,ctBuApprovalNg.getInstanceType());

                } else if (ctBuApprovalNg.getInstanceType() == 5){
                    widthName = "???????????????(?????????????????????10?????? ??? ????????????5???????????????)-" + loginUser.getUser().getUserName();
                    key = "cliquish_project";
                    flag = false;

                    variables.put("business",loginUser.getUsername());
                }
                ctBuApprovalNg.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuApprovalNg.getId(), variables
                        , taskService, taskRuntime, key, widthName));
            }

        }
        return ctBuApprovalNgMapper.insertCtBuApprovalNg(ctBuApprovalNg);
    }




    /**
     * ?????????????????????(?????????)
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Override
    @Transactional
    public int insertCtBuApprovalNg_(CtBuApprovalNg ctBuApprovalNg) {
        ctBuApprovalNg.setId(UUID.fastUUID().toString(true));
        ctBuApprovalNg.setDelFlag(1);
        ctBuApprovalNg.setProType("9f30ebd46d3c428cbc5e54f7175eb4f2");
        ctBuApprovalNg.setProTypeName("????????????");
        String userId = "";
        Long deptId = 1L;
        LoginUser loginUser = SecurityUtils.getLoginUser();

        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
            ctBuApprovalNg.setBelongCompanyid(loginUser.getUser().getCompanyId().toString());
            ctBuApprovalNg.setBelongDeptid(loginUser.getUser().getDept().getDeptId().toString());
        } catch (Exception e) {

        }
        ctBuApprovalNg.setCreateBy(userId);
        ctBuApprovalNg.setCreateTime(DateUtils.getNowDate());

        if (ctBuApprovalNg.getBelongCompanyid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongCompanyid()));
            ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
        }
        if (ctBuApprovalNg.getBelongDeptid() != null){
            SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongDeptid()));
            ctBuApprovalNg.setBelongDeptName(sysDept.getDeptName());
        }
        if (ctBuApprovalNg.getProType() != null){
            CtDicProtype ctDicProtype = ctDicProtypeMapper.selectCtDicProtypeById(ctBuApprovalNg.getProType());
            ctBuApprovalNg.setProTypeName(ctDicProtype.getTypeName());
        }

        ctBuApprovalNg.setReqNo(createReqNo(deptId));

        /** ???????????????????????? **/
        if (ctBuApprovalNg.getStatus() == 0){
            ctBuApprovalNg.setStatusPs("??????");
        } else if (ctBuApprovalNg.getStatus() == 1){
            ctBuApprovalNg.setStatusPs("?????????");
        }
        return ctBuApprovalNgMapper.insertCtBuApprovalNg(ctBuApprovalNg);
    }

    /**
     * ?????????????????????(?????????)
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Override
    @Transactional
    public int updateCtBuApprovalNg_(CtBuApprovalNg ctBuApprovalNg) {
        /** ??????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ctBuApprovalNg.setDelFlag(1);
        String userId = "";
        Long deptId = 1L;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
        } catch (Exception e) {

        }
        ctBuApprovalNg.setUpdateBy(userId);
//        ctBuApprovalNg.setUpdateTime(DateUtils.getNowDate());
        if (ctBuApprovalNg.getStatus() <= 1 || ctBuApprovalNg.getBelongDeptid() == null) {
            ctBuApprovalNg.setBelongDeptid((SecurityUtils.getLoginUser().getUser().getDeptId()) + "");
            if (ctBuApprovalNg.getBelongDeptid() != null) {
                SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongDeptid()));
                ctBuApprovalNg.setBelongDeptName(sysDept.getDeptName());
            }
            ctBuApprovalNg.setBelongCompanyid((SecurityUtils.getLoginUser().getUser().getCompanyId()) + "");
            if (ctBuApprovalNg.getBelongCompanyid() != null) {
                SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongCompanyid()));
                ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
            }
        }
        if (ctBuApprovalNg.getProType() != null) {
            CtDicProtype ctDicProtype = ctDicProtypeMapper.selectCtDicProtypeById(ctBuApprovalNg.getProType());
            ctBuApprovalNg.setProTypeName(ctDicProtype.getTypeName());
        }
        //??????????????????
        List<CtBuApprovaldtNgCompany> companyLists = ctBuApprovalNg.getCompanyList();
        ctBuApprovalNg.setCreateTime(null);
        ctBuApprovalNg.setCreateBy(null);
        int num = ctBuApprovalNgMapper.updateCtBuApprovalNg(ctBuApprovalNg);

        if(num>0){
            Map map = new HashMap();
            map.put("bid",ctBuApprovalNg.getId());
            ctBuApprovaldtNgCompanyMapper.deleteCtBuApprovaldtNgCompanyByIBid(map);
            for (CtBuApprovaldtNgCompany c : companyLists) {
                c.setId(UUID.fastUUID().toString(true));
                c.setBid(ctBuApprovalNg.getId());
                c.setBranchInvolvedId(c.getBranchInvolvedId());
                c.setDelFlag("1");
                c.setCreateTime(new Date());
                c.setCreateBy(loginUser.getUser().getUserName());
                ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(c);
            }
        }

        return num;
    }

    //???????????????????????????
    public Map<String,Object> getSzVarirs(long dept_id, CtBuApprovalNg ctBuApprovalNg, boolean flag){
        //?????????????????????????????????

        Map<String,Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();

        //????????????????????????
        variables.put("cityDepartment", ActUtils.getVarirs(229L,"109",iSysUserService));

        //????????????????????????
        CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("229");
        variables.put("deputyGeneralManager", dept.getFgld());

        //??????????????????????????? a?????? ?????????
        List<String> ulists = new ArrayList();
        ulists.add(ctBuApprovalNg.getCreateName());
        variables.put("investmentCommittee", org.apache.commons.lang.StringUtils.join(ulists.toArray(),","));

        //???????????????
        variables.put("partyCommittee", ActUtils.getVarirs(229L,"109",iSysUserService));

        return variables;
    }
    //???????????????
    public Map<String,Object> getVarirs(long dept_id, CtBuApprovalNg ctBuApprovalNg, boolean flag){
        //?????????????????????????????????

        Map<String,Object> variables = new HashMap();
        List<String> ulist = new ArrayList();
        Map params = new HashMap();

        //????????????????????????
        variables.put("cityDepartment", ActUtils.getVarirs(229L,"109",iSysUserService));

        //????????????????????????
        CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("229");
        variables.put("deputyGeneralManager", dept.getFgld());

        //???????????????????????????
        variables.put("investmentCommittee", ActUtils.getVarirs(213L,"119",iSysUserService));

        //???????????????
        variables.put("partyCommittee", ActUtils.getVarirs(213L,"118",iSysUserService));

        return variables;
    }

    //??????????????????????????????2
    public Map<String,Object> getVarirs2( LoginUser loginUser,long num){
        //?????????????????????????????????


        HashMap<String, Object> map = new HashMap<>();
        // ????????????????????????????????????
        map.put("apply", loginUser.getUser().getUserName());
        //??????????????????
        Map params = new HashMap();
        params.put("roleId",109);
        params.put("deptId",229);
        List<SysUser> list = ActUtils.getVarirs(params,iSysUserService);
        List<String> signList = new ArrayList<String>();
        if(list!=null && list.size()>0){
            for (SysUser user:list ) {
                signList.add(user.getUserName());
            }
        }
        params = new HashMap();
        params.put("roleId",109);
        params.put("deptId",226);
        list = ActUtils.getVarirs(params,iSysUserService);
        if(list!=null && list.size()>0){
            for (SysUser user:list ) {
                signList.add(user.getUserName());
            }
        }
        if(num==3){
            //???????????????
            params = new HashMap();
            params.put("roleId",109);
            params.put("deptId",227);
            list = ActUtils.getVarirs(params,iSysUserService);
            if(list!=null && list.size()>0){
                for (SysUser user:list ) {
                    signList.add(user.getUserName());
                }
            }
        }
        map.put("jkbshList", signList);
        //????????????
        CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("229");
        CtPdeptDept dept1 = iCtPdeptDeptService.selectCtPdeptDeptById("226");
        signList = new ArrayList<>();
        signList.add(dept.getFgld());
        signList.add(dept1.getFgld());
        if(num==3){
            CtPdeptDept dept2 = iCtPdeptDeptService.selectCtPdeptDeptById("227");
            signList.add(dept2.getFgld());
        }

        map.put("fgfzshList", signList);

        //???????????????
        map.put("zjlsh", ActUtils.getVarirs(null,"115",iSysUserService));


        return map;
    }
    //??????????????????????????????4
    public Map<String,Object> getVarirs4( LoginUser loginUser,long num){
        //?????????????????????????????????


        HashMap<String, Object> map = new HashMap<>();
        // ????????????????????????????????????
        map.put("apply", loginUser.getUser().getUserName());
        //??????????????????
        Map params = new HashMap();
        params.put("roleId",109);
        params.put("deptId",226);
        List<SysUser> list = ActUtils.getVarirs(params,iSysUserService);
        List<String> signList = new ArrayList<String>();
        if(list!=null && list.size()>0){
            for (SysUser user:list ) {
                signList.add(user.getUserName());
            }
        }
        //?????????????????????
        map.put("cwzcbsh",signList);

        params = new HashMap();
        params.put("roleId",109);
        params.put("deptId",229);
        signList = new ArrayList<String>();
        list = ActUtils.getVarirs(params,iSysUserService);
        if(list!=null && list.size()>0){
            for (SysUser user:list ) {
                signList.add(user.getUserName());
            }
        }
        //????????????????????????
        map.put("sgsjkbsh",signList);

        //????????????
        CtPdeptDept dept = iCtPdeptDeptService.selectCtPdeptDeptById("229");
        CtPdeptDept dept1 = iCtPdeptDeptService.selectCtPdeptDeptById("226");
        signList = new ArrayList<>();
        signList.add(dept.getFgld());
        signList.add(dept1.getFgld());
        map.put("fgfzshList", signList);

        //???????????????
        map.put("zjlsh", ActUtils.getVarirs(null,"115",iSysUserService));


        return map;
    }






    /**
     * ?????????????????????(?????????)
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Override
    @Transactional
    public int updateCtBuApprovalNg(CtBuApprovalNg ctBuApprovalNg) {
        /** ??????????????? **/
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ctBuApprovalNg.setDelFlag(1);
        String userId = "";
        Long deptId = 1L;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
        } catch (Exception e) {

        }
        if(ctBuApprovalNg.getCreateBy()==null){
            ctBuApprovalNg.setCreateBy(userId);
            ctBuApprovalNg.setCreateTime(DateUtils.getNowDate());
        }else{
            ctBuApprovalNg.setUpdateBy(userId);
            ctBuApprovalNg.setUpdateTime(DateUtils.getNowDate());
        }

        if(ctBuApprovalNg.getStatus()<=1 || ctBuApprovalNg.getBelongDeptid()==null){
            ctBuApprovalNg.setBelongDeptid((SecurityUtils.getLoginUser().getUser().getDeptId())+"");
            if (ctBuApprovalNg.getBelongDeptid() != null){
                SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongDeptid()));
                ctBuApprovalNg.setBelongDeptName(sysDept.getDeptName());
            }
            ctBuApprovalNg.setBelongCompanyid((SecurityUtils.getLoginUser().getUser().getCompanyId())+"");
            if (ctBuApprovalNg.getBelongCompanyid() != null){
                SysDept sysDept = sysDeptMapper.selectDeptById(Long.valueOf(ctBuApprovalNg.getBelongCompanyid()));
                ctBuApprovalNg.setBelongCompanyName(sysDept.getDeptName());
            }
        }
        if (ctBuApprovalNg.getProType() != null){
            CtDicProtype ctDicProtype = ctDicProtypeMapper.selectCtDicProtypeById(ctBuApprovalNg.getProType());
            ctBuApprovalNg.setProTypeName(ctDicProtype.getTypeName());
        }
        //??????????????????
        List<CtBuApprovaldtNgCompany> companyLists = ctBuApprovalNg.getCompanyList();
        List<CtBuApprovaldtNgCompany> companyList  = new ArrayList<>();
        final List<String> selectComp = ctBuApprovalNg.getSelectComp();
        String mps = "";
        List<String> result = new ArrayList<>();
        if(selectComp!=null && selectComp.size()>0){
            for (String c:selectComp) {
                String str = c.substring(c.indexOf("(")+1,c.indexOf(")"));
                mps=mps+(str)+",";

                boolean falg = false;
                if(companyLists!=null && companyLists.size()>0){
                    for(CtBuApprovaldtNgCompany x : companyLists){
                        if(str.equals(x.getBranchInvolvedId())){
                            companyList.add(x);
                            falg = true;
                            break;
                        }
                    }
                    if(!falg){
                        CtBuApprovaldtNgCompany any = new CtBuApprovaldtNgCompany();
                        any.setBid(ctBuApprovalNg.getId());
                        any.setBranchInvolvedId(str);
                        companyList.add(any);
                    }
                }else{
                    CtBuApprovaldtNgCompany any = new CtBuApprovaldtNgCompany();
                    any.setBid(ctBuApprovalNg.getId());
                    any.setBranchInvolvedId(str);
                    companyList.add(any);
                }
            }
        }
        if(companyLists!=null && companyLists.size()>0){
            for(CtBuApprovaldtNgCompany c : companyLists){
                if(mps.contains(c.getBranchInvolvedId())){
                    System.out.println("-------------------");
                }else{
                    result.add(c.getBranchInvolvedId());
                }
            }
        }
        if(result!=null && result.size()>0){
            int number = 0;
            for (String c:result) {
                CtBuApprovaldtNgCompany company = new CtBuApprovaldtNgCompany();
                company.setBid(ctBuApprovalNg.getId());
                company.setBranchInvolvedId(c);
                List<CtBuApprovaldtNgCompany> ctBuApprovaldtNgCompanies = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(company);
                if(ctBuApprovaldtNgCompanies!=null){
                    CtBuApprovaldtNgCompany company1 = ctBuApprovaldtNgCompanies.get(0);

                    Map anyc = new HashMap();
                    anyc.put("bid",ctBuApprovalNg.getId());
                    anyc.put("branchs",c);
                    ctBuApprovaldtNgCompanyMapper.deleteCtBuApprovaldtNgCompanyByIBid(anyc);

                    //????????????


                    appNgBaseMapper.deleteAppNgBaseByCid(company1.getId());
                    appNgMaterialMapper.deleteAppNgMaterialByCid(company1.getId());
                    appNgConstMapper.deleteAppNgConstByCid(company1.getId());
                }


            }
        }


        int company_num = 0;
        if(companyList!=null && companyList.size()>0) {
            //????????????????????????
            for (CtBuApprovaldtNgCompany c : companyList) {
                company_num++;
                CtBuApprovaldtNgCompany any = new CtBuApprovaldtNgCompany();
                any.setBid(ctBuApprovalNg.getId());
                any.setBranchInvolvedId(c.getBranchInvolvedId());
                List<CtBuApprovaldtNgCompany> companies = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(any);
                if (companies != null && companies.size() > 0) {
                    any = companies.get(0);
                    c.setId(any.getId());
                    c.setUpdateTime(new Date());
                    c.setUpdateBy(loginUser.getUser().getUserName());
                    ctBuApprovaldtNgCompanyMapper.updateCtBuApprovaldtNgCompany(c);
                } else {
                    c.setId(UUID.fastUUID().toString(true));
                    c.setBid(ctBuApprovalNg.getId());
                    c.setBranchInvolvedId(c.getBranchInvolvedId());
                    c.setDelFlag("1");
                    c.setCreateTime(new Date());
                    c.setCreateBy(loginUser.getUser().getUserName());
                    ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(c);
                }
                /** ?????????????????? **/
                List<CtBuApprovaldtNgCompanys> companys = ctBuApprovalNg.getCompanys();
                if (companys != null && companys.size() > 0) {
                    for (CtBuApprovaldtNgCompanys company : companys) {
//                        if (companies != null && companies.size() > 0) {
                            String detpId = company.getDeptId();
                            if (Long.parseLong(detpId) == (Long.parseLong(c.getBranchInvolvedId()))) {
                                List<AppNgBase> appNgBaseList = company.getAppNgBaseList();
                                if (appNgBaseList != null && appNgBaseList.size() > 0) {
                                    for (AppNgBase appNgBase : appNgBaseList) {
                                        if (appNgBase.getId() != null && !"".equals(appNgBase.getId())) {
                                            appNgBase.setBid(ctBuApprovalNg.getId());
                                            appNgBase.setCreateBy(userId);
                                            appNgBase.setCid(c.getId());
                                            appNgBase.setDelFlag(1);
                                            appNgBase.setBranchInvolvedId(detpId);
                                            appNgBaseService.updateAppNgBase(appNgBase);
                                        } else {
                                            appNgBase.setId(UUID.fastUUID().toString(true));
                                            appNgBase.setBid(ctBuApprovalNg.getId());
                                            appNgBase.setCreateBy(userId);
                                            appNgBase.setCid(c.getId());
                                            appNgBase.setDelFlag(1);
                                            appNgBase.setBranchInvolvedId(detpId);
                                            appNgBaseService.insertAppNgBase(appNgBase);
                                        }
                                    }
                                }
                                List<List<AppNgConst>> appNgConstLists = company.getAppNgConstLists();
                                if (appNgConstLists != null && appNgConstLists.size() > 0) {
                                    for (List<AppNgConst> clist : appNgConstLists) {
                                        if (clist != null && clist.size() > 0) {
                                            for (int i = 0; i < clist.size(); i++) {
                                                AppNgConst appNgConst = clist.get(i);
                                                if (appNgConst.getId() != null && !"".equals(appNgConst.getId())) {
                                                    appNgConst.setBid(ctBuApprovalNg.getId());
                                                    appNgConst.setUpdateBy(userId);
                                                    appNgConst.setUpdateTime(new Date());
                                                    appNgConst.setDelFlag(1);
                                                    appNgConst.setCid(c.getId());
                                                    appNgConstService.updateAppNgConst(appNgConst);
                                                } else {
                                                    appNgConst.setId(UUID.fastUUID().toString(true));
                                                    appNgConst.setBid(ctBuApprovalNg.getId());
                                                    appNgConst.setCreateBy(userId);
                                                    appNgConst.setDelFlag(1);
                                                    appNgConst.setCid(c.getId());
                                                    appNgConstService.insertAppNgConst(appNgConst);
                                                }

                                            }
                                        }
                                    }
                                }
                                List<List<AppNgMaterial>> appNgMaterialLists = company.getAppNgMaterialLists();
                                if (appNgMaterialLists != null && appNgMaterialLists.size() > 0) {
                                    for (List<AppNgMaterial> mlist : appNgMaterialLists) {
                                        if (mlist != null && mlist.size() > 0) {
                                            for (int i = 0; i < mlist.size(); i++) {
                                                AppNgMaterial appNgMaterial = mlist.get(i);
                                                if (appNgMaterial.getId() != null && !"".equals(appNgMaterial.getId())) {
                                                    appNgMaterial.setBid(ctBuApprovalNg.getId());
                                                    appNgMaterial.setUpdateBy(userId);
                                                    appNgMaterial.setDelFlag(1);
                                                    appNgMaterial.setCid(c.getId());
                                                    appNgMaterial.setUpdateTime(new Date());
                                                    appNgMaterialService.updateAppNgMaterial(appNgMaterial);
                                                } else {
                                                    appNgMaterial.setId(UUID.fastUUID().toString(true));
                                                    appNgMaterial.setBid(ctBuApprovalNg.getId());
                                                    appNgMaterial.setCreateBy(userId);
                                                    appNgMaterial.setDelFlag(1);
                                                    appNgMaterial.setCid(c.getId());
                                                    appNgMaterialService.insertAppNgMaterial(appNgMaterial);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                    }
                }
            }
            /** ???????????? **/
            AppxNgReq appxNgReq = ctBuApprovalNg.getAppxNgReq();
            /** ???????????????????????? **/
            ctBuApprovalNg.setReqNo(appxNgReq==null || appxNgReq.getAppxNgNo()==null?this.createReqNo(deptId):appxNgReq.getAppxNgNo());
            if (appxNgReq != null && StringUtils.isNotBlank(appxNgReq.getAppxNgNo())) {
                if(appxNgReq.getId()!=null && !"".equals(appxNgReq.getId())){
                    appxNgReq.setBid(ctBuApprovalNg.getId());
                    appxNgReq.setCreateBy(userId);
                    appxNgReq.setAppxNgNo(ctBuApprovalNg.getReqNo());
                    appxNgReq.setDelFlag(1);
                    appxNgReq.setAppxNgName(ctBuApprovalNg.getProName());
                    appxNgReq.setProType(ctBuApprovalNg.getProType());
                    appxNgReq.setUpdateTime(new Date());
                    appxNgReq.setUpdateBy(loginUser.getUser().getUserName());
                    appxNgReqService.updateAppxNgReq(appxNgReq);
                }else{
                    appxNgReq.setId(UUID.fastUUID().toString(true));
                    appxNgReq.setBid(ctBuApprovalNg.getId());
                    appxNgReq.setCreateBy(userId);
                    appxNgReq.setAppxNgNo(ctBuApprovalNg.getReqNo());
                    appxNgReq.setDelFlag(1);
                    appxNgReq.setAppxNgName(ctBuApprovalNg.getProName());
                    appxNgReq.setProType(ctBuApprovalNg.getProType());
                    appxNgReq.setCreateTime(new Date());
                    appxNgReq.setCreateBy(loginUser.getUser().getUserName());
                    appxNgReqService.insertAppxNgReq(appxNgReq);
                }

                /** ???????????? **/
                List<AppNgReqList> aaList = appxNgReq.getDataList();
                if (aaList != null && !aaList.isEmpty()) {
                    for (int i = 0; i < aaList.size(); i++) {
                        AppNgReqList appNgReqList = aaList.get(i);
                        if(appNgReqList!=null && appNgReqList.getId()!=null && !"".equals(appNgReqList.getId())){
                            appNgReqList.setPid(appxNgReq.getId());
                            appNgReqList.setCreateTime(new Date());
                            appNgReqListService.updateAppNgReqList(appNgReqList);
                        }else{
                            appNgReqList.setId(UUID.fastUUID().toString(true));
                            appNgReqList.setPid(appxNgReq.getId());
                            appNgReqList.setCreateTime(new Date());
                            appNgReqListService.insertAppNgReqList(appNgReqList);
                        }

                    }
                }
            }


        }else{
            List<String> list = ctBuApprovalNg.getSelectComp();
            if(list!=null && list.size()>0){
                for (String str:list) {
                    CtBuApprovaldtNgCompany any = new CtBuApprovaldtNgCompany();
                    any.setBid(ctBuApprovalNg.getId());
                    int index = str.indexOf("(")+1;
                    String val = str.substring(index,str.length()-1);
                    any.setBranchInvolvedId(val);
                    List<CtBuApprovaldtNgCompany> companies = ctBuApprovaldtNgCompanyMapper.selectCtBuApprovaldtNgCompanyList(any);
                    if (companies != null && companies.size() > 0) {
                        any = companies.get(0);
                        any.setId(any.getId());
                        any.setUpdateTime(new Date());
                        any.setUpdateBy(loginUser.getUser().getUserName());
                        ctBuApprovaldtNgCompanyMapper.updateCtBuApprovaldtNgCompany(any);
                    } else {
                        any.setId(UUID.fastUUID().toString(true));
                        any.setBid(ctBuApprovalNg.getId());
                        any.setBranchInvolvedId(any.getBranchInvolvedId());
                        any.setDelFlag("1");
                        any.setCreateTime(new Date());
                        any.setCreateBy(loginUser.getUser().getUserName());
                        ctBuApprovaldtNgCompanyMapper.insertCtBuApprovaldtNgCompany(any);
                    }

                }
            }
        }
        String widthName = "";
        if (ctBuApprovalNg.getStatus() == 0){
            ctBuApprovalNg.setStatusPs("??????");
        } else if (ctBuApprovalNg.getStatus() == 1){
            ctBuApprovalNg.setStatusPs("?????????");
            //????????????????????????????????????

            if (loginUser != null && loginUser.getUser() != null) {
                long dept_id = loginUser.getUser().getDept().getDeptId();
                long parent_id = loginUser.getUser().getDept().getParentId();
                boolean flag = true;
                String key = "";
                Map<String, Object> variables = this.getVarirs(dept_id,ctBuApprovalNg,flag);
                if (ctBuApprovalNg.getInstanceType() == 1){
                    variables = this.getSzVarirs(dept_id,ctBuApprovalNg,flag);
                    widthName = "??????????????????(?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "important_project";
                    //flag = false;
                    variables.put("business",loginUser.getUsername());
                    if(company_num==1){
                        List<String> signList = new ArrayList<>();
                        //???????????????
                        Map params = new HashMap();
                        params.put("roleId",109);
                        params.put("deptId",227);
                        List<SysUser>list = ActUtils.getVarirs(params,iSysUserService);
                        if(list!=null && list.size()>0){
                            for (SysUser user:list ) {
                                signList.add(user.getUserName());
                            }
                        }
                        variables.put("ifgh","1");
                        variables.put("ghzzbsh",org.apache.commons.lang.StringUtils.join(signList.toArray(),","));
                    }else{
                        variables.put("ifgh","0");
                    }

                } else if (ctBuApprovalNg.getInstanceType() == 2){
                    widthName = "????????????(?????????10?????? ??? 5?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "xmlx2";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());

                    if(company_num==1){
                        List<String> signList = new ArrayList<>();
                        //???????????????
                        Map params = new HashMap();
                        params.put("roleId",109);
                        params.put("deptId",227);
                        List<SysUser>list = ActUtils.getVarirs(params,iSysUserService);
                        if(list!=null && list.size()>0){
                            for (SysUser user:list ) {
                                signList.add(user.getUserName());
                            }
                        }
                        variables.put("ifgh","1");
                        variables.put("ghbsh",org.apache.commons.lang.StringUtils.join(signList.toArray(),","));
                    }else{
                        variables.put("ifgh","0");
                    }
                } else if (ctBuApprovalNg.getInstanceType() == 3){
                    widthName = "???????????????(???????????????10?????????5?????????????????????50??????)-" + loginUser.getUser().getNickName();
                    key = "xmlx3";
                    variables = this.getVarirs2( loginUser,ctBuApprovalNg.getInstanceType());
                } else if (ctBuApprovalNg.getInstanceType() == 4){
                    widthName = "?????????????????????????????????????????????????????????(???????????????10?????????????????????)-" + loginUser.getUser().getNickName();
                    key = "xmlx4";
                    variables = this.getVarirs4( loginUser,ctBuApprovalNg.getInstanceType());

                } else if (ctBuApprovalNg.getInstanceType() == 5){
                    widthName = "???????????????(?????????????????????10?????? ??? ????????????5???????????????)-" + loginUser.getUser().getNickName();
                    key = "cliquish_project";
                    flag = false;
                    variables.put("business",loginUser.getUsername());
                }
                if(ctBuApprovalNg.getInstanceId()!=null && !"".equals(ctBuApprovalNg.getInstanceId())){
                    ctBuApprovalNg.setInstanceId(ActUtils.submitTask(runtimeService,processRuntime, loginUser, ctBuApprovalNg.getInstanceId(), variables
                            , taskService, taskRuntime, key, widthName,ctBuApprovalNg.getId()));
                }else{
                    ctBuApprovalNg.setInstanceId(ActUtils.submitTask(processRuntime, loginUser, ctBuApprovalNg.getId(), variables
                            , taskService, taskRuntime, key, widthName));
                }
            }
        }
        /** ???????????? **/
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ctBuApprovalNg.getConstructionPlan())) {
            String[] str = ctBuApprovalNg.getConstructionPlan().split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(1);   //?????????????????? ptype:1
                    file.setPid(ctBuApprovalNg.getId());
                    fileService.updateCtBuFile(file);
                }
            }
        }
        ctBuApprovalNg.setCreateTime(null);
        ctBuApprovalNg.setCreateBy(null);
        int num = ctBuApprovalNgMapper.updateCtBuApprovalNg(ctBuApprovalNg);
        if(num>0){
            if (ctBuApprovalNg.getStatus() == 1){
                // ??????????????????Id????????????
                Map map = new HashMap();
                map.put("instanceId",ctBuApprovalNg.getInstanceId());
                map.put("name",widthName);
                msgService.sendDelayMsgToMQ(SysConstant.SYS_NG_EXCHANGE,SysConstant.SYS_NG_KEY, JSONUtil.toJsonStr(map),0);//30???????????????
            }
//            if (ctBuApprovalNg.getStatus() == 4){
//                // ??????????????????Id????????????
//                Map map = new HashMap();
//                //????????????
//                List<String> ulist = new ArrayList<>();
//                String varirs = ActUtils.getVarirs(null, "121", iSysUserService);
//                if(varirs!=null && !"".equals(varirs)){
//                    String[] strs = varirs.split(",");
//                    for (String str:strs) {
//                        ulist.add(str);
//                    }
//                }
//                if(ulist!=null && ulist.size()>0){
//                    String popName = "";
//                    if (ctBuApprovalNg.getInstanceType() == 1){
//                        popName = "??????????????????(?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 2){
//                        popName = "????????????(?????????10?????? ??? 5?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 3){
//                        popName = "???????????????(???????????????10?????????5?????????????????????50??????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 4){
//                        popName = "?????????????????????????????????????????????????????????(???????????????10?????????????????????)-" + loginUser.getUser().getNickName();
//                    } else if (ctBuApprovalNg.getInstanceType() == 5){
//                        popName = "???????????????(?????????????????????10?????? ??? ????????????5???????????????)-" + loginUser.getUser().getNickName();
//                    }
//                    map.put("status","4");
//                    map.put("users",ulist);
//                    map.put("name",popName+"-?????????");
//                    msgService.sendDelayMsgToMQ(SysConstant.SYS_ORDER_DELAY_EXCHANGE,SysConstant.SYS_ORDER_DELAY_KEY, JSONUtil.toJsonStr(map),0);//30???????????????
//                }
//            }
        }
        return num;
    }


    /**
     * ?????????????????????(?????????)
     *
     * @return ??????
     */
    @Override
    public int updateCtBuApprovalNgOld(String uploads,String nid) {
        CtBuApprovalNg ng = new CtBuApprovalNg();
        ng.setUploudInfo(uploads);
        ng.setId(nid);
        int num = ctBuApprovalNgMapper.updateCtBuApprovalNg(ng);
        if(num>0){
            String[] str = uploads.split(",");
            for (int i = 0; i < str.length; i++) {
                /** ??????????????????ID???????????? **/
                if (org.apache.commons.lang3.StringUtils.isNotBlank(str[i])) {
                    CtBuFile file = new CtBuFile();
                    file.setId(str[i]);
                    file.setPtype(5);   //?????????????????? ptype:1
                    file.setPid(nid);
                    fileService.updateCtBuFile(file);
                }
            }
        }
        return num;
    }


    /**
     * ?????????????????????(?????????) ????????????id
     *
     * @param ctBuApprovalNg ???????????????(?????????)
     * @return ??????
     */
    @Override
    @Transactional
    public int updateCtBuApprovalNgByInstanceId(CtBuApprovalNg ctBuApprovalNg) {

        return ctBuApprovalNgMapper.updateCtBuApprovalNgByInstanceId(ctBuApprovalNg);
    }
    /**
     * ???????????????????????????(?????????)
     *
     * @param ids ??????????????????????????????(?????????)ID
     * @return ??????
     */
    @Override
    @Transactional
    public int deleteCtBuApprovalNgByIds(String[] ids) {
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                /** ???????????????????????????????????? **/
                Integer numFlag = ctBuApprovalNgMapper.deleteCtBuApprovalNgById(ids[i]);
                if (numFlag != null && numFlag > 0) {
                    /** ?????????????????? **/
                    appNgBaseService.deleteAppNgBaseByBid(ids[i]);
                    /** ??????????????? **/
                    appNgConstService.deleteAppNgConstByBid(ids[i]);
                    /** ?????????????????? **/
                    appNgMaterialService.deleteAppNgMaterialByBid(ids[i]);
                    /** ???????????? **/
                    appxNgReqService.deleteAppxNgReqByBid(ids[i]);
                }
            }
        }
        return 1;
    }
    /**
     * ?????????????????????(?????????)??????
     *
     * @param id ???????????????(?????????)ID
     * @return ??????
     */
    @Override
    @Transactional
    public int deleteCtBuApprovalNgById(String id) {
        /** ???????????????????????????????????? **/
        Integer numFlag = ctBuApprovalNgMapper.deleteCtBuApprovalNgById(id);
        if (numFlag != null && numFlag > 0) {
            /** ?????????????????? **/
            appNgBaseService.deleteAppNgBaseByBid(id);
            /** ??????????????? **/
            appNgConstService.deleteAppNgConstByBid(id);
            /** ?????????????????? **/
            appNgMaterialService.deleteAppNgMaterialByBid(id);
            /** ???????????? **/
            appxNgReqService.deleteAppxNgReqByBid(id);
        }
        return numFlag;
    }

    /**
     * @Title: AppNgcServiceImpl.java
     * @Description: ????????????????????????????????????
     * @Description: deptId ---????????????ID
     * @Author M.Mu
     * @Date 2020-12-11
     */
    public String createReqNo(Long deptId) {
        /** ?????????????????????ID????????????????????? **/
        if (deptId != null) {
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
            /** 2???????????? **/
            String date = DateUtils.dateTime();
            /** 3???????????????????????????(5???) **/
            String numStr = "";
            AppxNgReq appxNgReq = new AppxNgReq();
            appxNgReq.setStime(DateUtils.getMonthBegin(DateUtils.getDate()));
            appxNgReq.setEtime(DateUtils.getMonthEnd(DateUtils.getDate()));
            appxNgReq.setProType(company);
            Integer nums = appxNgReqService.findNumsByData(appxNgReq);
            if (nums != null && nums > 0) {
                numStr = com.ruoyi.common.utils.StringUtils.getStringByInt(nums);
            } else {
                numStr = com.ruoyi.common.utils.StringUtils.getStringByInt(0);
            }
            return company + date + numStr;
        }
        return "";
    }

    @Override
    public String findNextAppNo() {
        Long deptId = 1L;
        try {
            deptId = SecurityUtils.getLoginUser().getUser().getCompanyId();
        } catch (Exception e) {

        }
        return this.createReqNo(deptId);
    }
}