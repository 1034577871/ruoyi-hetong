

package com.ruoyi.activiti.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.ruoyi.activiti.domain.ActWorkflowFormData;
import com.ruoyi.activiti.domain.dto.ActTaskDTO;
import com.ruoyi.activiti.domain.dto.ActTaskVO;
import com.ruoyi.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.ruoyi.activiti.domain.dto.SearchTodoTaskVo;
import com.ruoyi.activiti.mapper.ActWorkflowFormDataMapper;
import com.ruoyi.activiti.mapper.PublicMapper;
import com.ruoyi.activiti.service.IActTaskService;
import com.ruoyi.activiti.service.IActWorkflowFormDataService;
import com.ruoyi.activiti.utils.ActivitiUtil;
import com.ruoyi.base.fileManage.domain.CtBuFile;
import com.ruoyi.base.fileManage.service.ICtBuFileService;
import com.ruoyi.collection.domain.*;
import com.ruoyi.collection.mapper.CtBuInvoiceInfoMapper;
import com.ruoyi.collection.mapper.CtBuInvoiceReqMapper;
import com.ruoyi.collection.mapper.CtBuReceiptInfoMapper;
import com.ruoyi.collection.service.ICtBuInvoiceInfoService;
import com.ruoyi.collection.service.ICtBuInvoiceReqService;
import com.ruoyi.collection.service.ICtBuReceiptInfoService;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.contract.contractInfo.domain.CtBuContract;
import com.ruoyi.contract.contractInfo.mapper.CtBuContractMapper;
import com.ruoyi.mq.bean.SysConstant;
import com.ruoyi.mq.service.MsgService;
import com.ruoyi.paymentManagement.payinfo.domain.CtBuPayinfo;
import com.ruoyi.paymentManagement.payinfo.mapper.CtBuPayinfoMapper;
import com.ruoyi.paymentManagement.ticketRecord.domain.TicketRecord;
import com.ruoyi.paymentManagement.ticketRecord.mapper.TicketRecordMapper;
import com.ruoyi.projectApproval.AppNgc.domain.AppNgc;
import com.ruoyi.projectApproval.AppNgc.mapper.AppNgcMapper;
import com.ruoyi.projectApproval.ApprovalNg.domain.CtBuApprovalNg;
import com.ruoyi.projectApproval.ApprovalNg.mapper.CtBuApprovalNgMapper;
import com.ruoyi.projectApproval.ApprovalNg.service.ICtBuApprovalNgService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IActWorkflowFormDataService actWorkflowFormDataService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    @Autowired
    private ActWorkflowFormDataMapper actWorkflowFormDataMapper;
    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;
    @Autowired
    private ICtBuInvoiceReqService iCtBuInvoiceReqService;

    @Autowired
    private ICtBuReceiptInfoService iCtBuReceiptInfoService;
    @Autowired
    PublicMapper publicMapper;

    @Autowired
    private ICtBuInvoiceInfoService iCtBuInvoiceInfoService;
    @Autowired
    private CtBuInvoiceInfoMapper ctBuInvoiceInfoMapper;
    @Autowired
    private RedisTemplate<String, Serializable> redis1;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ICtBuApprovalNgService iCtBuApprovalNgService;

    @Autowired
    private CtBuApprovalNgMapper ctBuApprovalNgMapper;

    @Autowired
    private MsgService msgService;

    @Autowired
    private CtBuInvoiceReqMapper ctBuInvoiceReqMapper;
    @Autowired
    private CtBuContractMapper ctBuContractMapper;

    @Autowired
    private TicketRecordMapper ticketRecordMapper;

    @Autowired
    private CtBuPayinfoMapper ctBuPayinfoMapper;

    @Autowired
    private AppNgcMapper appNgcMapper;

    @Autowired
    private ActivitiUtil activitiUtil;

    @Autowired
    private ICtBuFileService fileService;
    @Override
    public void exportImage(HttpServletResponse response, String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = "";
        boolean flag = false;
        //???????????????????????????????????????????????????
        if(processInstance==null){
            List<HistoricActivityInstance>  list=historyService // ????????????Service
                    .createHistoricActivityInstanceQuery() // ??????????????????????????????
                    .processInstanceId(processInstanceId) // ??????????????????id
                    .finished()
                    .list();
            HistoricActivityInstance hai = list.get(0);
            processDefinitionId = hai.getProcessDefinitionId();
            flag = true;

        }else{
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        HistoricActivityInstanceQuery historyInstanceQuery = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId);

        response.setContentType("text/html;charset=utf-8");
        // ??????????????????
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery
                .orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> executedActivityIdList = historicActivityInstanceList.stream().map(item -> item.getActivityId())
                .collect(Collectors.toList());

        InputStream imageStream = null;
        if(flag){
            imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "??????", "??????", "??????");
        }else{
             imageStream = processDiagramGenerator.generateDiagram(bpmnModel,runtimeService.getActiveActivityIds(processInstanceId),Collections.<String>emptyList(), "??????", "??????", "??????");
        }
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.setCharacterEncoding("utf-8");
            response.getOutputStream().flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally { // ?????????
            try {
                imageStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public InputStream findProcessPic(String procDefId) {
       // ProcessDefinition definition = getProcessDefinition(procDefId);
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("invoiceApply").processDefinitionVersion(7)
                .singleResult();
        String source = definition.getDiagramResourceName();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(),source);
        return inputStream;
    }


    /**
     * ???????????????id???
     *
     * @param assignee ??????
     * @return ?????????????????????
     */

    @Override
    public Map selectBuessKeyByExe(String id){
        return actWorkflowFormDataMapper.selectBuessKeyByExe(id);
    }
    /**
     * ??????????????????????????????
     *
     * @param assignee ??????
     * @return ?????????????????????
     */

    @Override
    public Page<ActTaskDTO> queryDoneTasks(PageDomain pageDomain,String processDefinitionName,String taskName
    ,String startDueDate,String endDueDate) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SearchTodoTaskVo searchTodoTaskVo  = new SearchTodoTaskVo();
        String processDefinitionNameLike="";
        String taskNameLike="";
        //???????????????????????????????????????  %%
        if(StringUtils.isNotEmpty(processDefinitionName)) {
            StringBuffer buf=new StringBuffer();
            buf.append("%");
            buf.append(processDefinitionName);
            buf.append("%");
            processDefinitionNameLike=buf.toString();
        }
        if(StringUtils.isNotEmpty(taskName)) {
            StringBuffer buf1=new StringBuffer();
            buf1.append("%");
            buf1.append(taskName);
            buf1.append("%");
            taskNameLike=buf1.toString();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //???????????????????????????????????????
        if(!StringUtils.isEmpty(startDueDate)) {
            Date startDueDate1 = null;
            try {
                startDueDate1 = formatter.parse(startDueDate);
                searchTodoTaskVo.setStartDueDate(startDueDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(!StringUtils.isEmpty(endDueDate)) {
            Date endDueDate1 = null;
            try {
                endDueDate1 = formatter.parse(endDueDate);
                Calendar calendar = Calendar.getInstance(); //????????????
                calendar.setTime(endDueDate1);//???????????????????????????
                calendar.add(Calendar.DAY_OF_MONTH, +1);  //??????????????????
                String day2 = formatter.format(calendar.getTime());
                endDueDate1=formatter.parse(day2);
                searchTodoTaskVo.setEndDueDate(endDueDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //searchTodoTaskVo.setProcessDefinitionName(processDefinitionNameLike);
        searchTodoTaskVo.setProcessDefinitionName(processDefinitionNameLike);
        searchTodoTaskVo.setTaskName(taskNameLike);

        //???????????????????????????????????????????????????????????????????????????getTaskDoneQuery
        HistoricTaskInstanceQuery  taskQuery =getTaskDoneQuery(searchTodoTaskVo).taskAssignee(loginUser.getUser().getUserName()).orderByHistoricTaskInstanceEndTime().desc();;
        List<HistoricTaskInstance> historicTaskInstances_ = taskQuery.finished().list();
       //?????????????????????????????????.finished()
        List<HistoricTaskInstance> historicTaskInstances = taskQuery.finished().listPage((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize());
        int num = 0;
        List<ActTaskDTO> lists= new ArrayList<>();
        if(historicTaskInstances!=null && historicTaskInstances.size()>0){
            for(HistoricTaskInstance historicTaskInstance:historicTaskInstances) {
                ActTaskDTO dto = new ActTaskDTO();
                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().
                        processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
                //??????????????????
                ActWorkflowFormData data = new ActWorkflowFormData();
                data.setBusinessKey(processInstance.getBusinessKey());
                data.setFormKey(historicTaskInstance.getFormKey()==null?"xxx":historicTaskInstance.getFormKey());
                data.setExecutionId(historicTaskInstance.getExecutionId());

                List<ActWorkflowFormData> dlist = actWorkflowFormDataMapper.selectGroupActWorkflowFormDataList(data);
                if(dlist!=null && dlist.size()>0){
                    ActWorkflowFormData actWorkflowFormData = dlist.get(0);
                    if(actWorkflowFormData.getControlValue()!=null){
                        String agree = actWorkflowFormData.getControlValue();
                        if(!"".equals(agree)){
                            String[] agrees = agree.split(":");
                            if("??????".equals(agrees[0])){
                                dto.setStatus("??????");
                            }
                            if("?????????".equals(agrees[0])){
                                dto.setStatus("??????");
                            }
                            if("??????".equals(agrees[0])){
                                dto.setStatus("??????");
                            }
                        }
                    }
                }else{
                    dto.setStatus("??????");
                }

                dto.setRemark(historicTaskInstance.getAssignee());
                String[] strs = processInstance.getName().split("-");
                dto.setInstanceName(strs[0]);
                dto.setAssignee(strs[1]);
                dto.setId(processInstance.getId());
                dto.setName(historicTaskInstance.getName());
//                dto.setStatus(historicTaskInstance.get);
                dto.setBusinessKey(processInstance.getBusinessKey());
                dto.setCreatedDate(historicTaskInstance.getEndTime());
                lists.add(dto);
            }
        }
        //Page<HistoricTaskInstance> list = new Page<HistoricTaskInstance>();
        Page<ActTaskDTO> list = new Page<ActTaskDTO>();
        int totalItems_ = historicTaskInstances_.size()
;
        list.setTotal(totalItems_-num);

        list.addAll(lists);


        return list;
    }



    public SysUser getUser(String userName){
        SysUser sysUser = iSysUserService.selectUserByUserName(userName);
        return sysUser;
    }
    @Override
    public Page<ActTaskVO> selectProcessDefinitionList(PageDomain pageDomain,SearchTodoTaskVo searchTodoTaskVo) {
        Page<ActTaskVO> list = new Page<ActTaskVO>();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysRole> roles = loginUser.getUser().getRoles();
        org.activiti.api.runtime.shared.query.Page<Task> pageTasks = taskRuntime.tasks(Pageable.of((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize()));
        List<Task> tasks = pageTasks.getContent();
        int totalItems = pageTasks.getTotalItems();
        list.setTotal(totalItems);
        if (totalItems != 0) {
            Set<String> processInstanceIdIds = tasks.parallelStream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());
            QueryProperty pro = new QueryProperty() {
                @Override
                public String getName() {
                    return "start_time_";
                }
            };
            ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().orderBy(pro).desc();
            List<ProcessInstance> processInstanceList = query.processInstanceIds(processInstanceIdIds).list();
            List<ActTaskVO> actNewTask = new ArrayList<>();
            List<ActTaskVO> actTaskDTOS = tasks.stream()
                    .map(t -> new ActTaskVO(t, processInstanceList.parallelStream().filter(pi -> t.getProcessInstanceId().equals(pi.getId())).findAny().get()))
                    .collect(Collectors.toList());
            if(actTaskDTOS!=null && actTaskDTOS.size()>0){
                Collections.sort(actTaskDTOS);
                for (ActTaskVO dto:actTaskDTOS) {
                  //  dto.setAssignee(assgin);
                    dto.setFlag(false);
                    String instanceName = dto.getInstanceName();
                    String[] names = instanceName.split("-");
                    dto.setInstanceName(names[0]);
                    dto.setApplyName(names[1]);
                    if(searchTodoTaskVo.getProcessInstanceName()!=null && !"".equals(searchTodoTaskVo.getProcessInstanceName())){
                        if(dto.getInstanceName().contains(searchTodoTaskVo.getProcessInstanceName())){
                            for (SysRole role: roles) {
                                if(role.getRoleId()==109){
                                    dto.setFlag(true);
                                    break;
                                }
                            }
                            actNewTask.add(dto);
                        }
                    }else{
                        for (SysRole role: roles) {
                            if(role.getRoleId()==109){
                                dto.setFlag(true);
                                break;
                            }
                        }
                        if("ASSIGNED".equals(dto.getStatus()) || "CREATED".equals(dto.getStatus())){
                            dto.setStatusName("?????????");
                        }
                        actNewTask.add(dto);
                    }
                }
            }
            list.addAll(actNewTask);
//            list.addAll(actTaskDTOS);
        }
        totalItems = list==null?0:list.size();
        list.setTotal(totalItems);
        return list;
    }

    @Override
    public List<String> formDataShow(String taskID) {
        Task task = taskRuntime.task(taskID);
/*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2--__!!??????--__!!??????--__!!????????????--__!!?????????
            ?????????
            FormProperty_0lovri0--__!!string--__!!??????--__!!f--__!!??????!!__--??????
            FormProperty_1iu6onu--__!!int--__!!??????--__!!s

            ?????????????????????????????????FormProperty_????????????????????????ID
            ???????????????f??????????????????s????????????t?????????(?????????int???????????????int?????????string)
            ?????????????????????????????????????????????????????????????????????????????????
            */





        //??????!!!!!!!!:??????Key????????????????????????????????????????????????????????????key??????????????????????????????????????????key???task.getFormKey()???????????????key
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());

        if (userTask == null) {
            return null;
        }
        List<FormProperty> formProperties = userTask.getFormProperties();
        List<String> collect = formProperties.stream().map(fp -> fp.getId()).collect(Collectors.toList());

        return collect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int formDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs,String uploads,String nid) throws ParseException {
        redis1.delete("CtBuApprovalNg");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Task task = taskRuntime.task(taskID);
        String instanceId = task.getProcessInstanceId();
        org.activiti.engine.task.Task taskExe = taskService.createNativeTaskQuery().sql("select * from act_ru_task where id_='"+taskID+"'").singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        //???????????????
        Boolean hasVariables = false;//??????????????????
        HashMap<String, Object> variables = new HashMap<String, Object>();
        //????????????????????????????????????????????????
        List<ActWorkflowFormData> acwfds = new ArrayList<>();
        boolean agree = false;
        for (ActWorkflowFormDataDTO awf : awfs) {
            ActWorkflowFormData actWorkflowFormData = new ActWorkflowFormData(processInstance.getBusinessKey(), awf, task);
            if(taskExe!=null){
                actWorkflowFormData.setExecutionId(taskExe.getExecutionId());
            }
            acwfds.add(actWorkflowFormData);
            if("??????".equals(actWorkflowFormData.getControlValue())){
                agree = true;
            }
            //??????????????????
            if (!"f".equals(awf.getControlIsParam())) {
                variables.put(awf.getControlId(), awf.getControlValue());
                hasVariables = true;

                if("xmlx2".equals(processInstance.getProcessDefinitionKey()) || "xmlx3".equals(processInstance.getProcessDefinitionKey()) || "xmlx4".equals(processInstance.getProcessDefinitionKey())){
                    //????????????
                  if("??????".equals(actWorkflowFormData.getControlValue())){
                        variables.put("pass", true);
                    }
                    if("?????????".equals(actWorkflowFormData.getControlValue())){
                        variables.put("pass", false);
                    }
                }
            }
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(taskExe.getTaskDefinitionKey());
        boolean flag = this.getFlowElementStatus(flowElement, taskExe.getExecutionId());
        Map map = new HashMap();
        if(flag){
                // UserTask userTask = (UserTask)flow;
                map.put("instanceId",processInstance.getId());
                map.put("name",processInstance.getProcessDefinitionName());
                if(agree){
                    List<String> flow = ActivitiUtil.getNextUserFlowElement(task);
                    List<String> userList = new ArrayList<>();
                    if(flow!=null &&  flow.size()>0){
                        for (String str:flow ) {
                            userList.add(str);
                        }
                    }
                    map.put("users",userList);
                }
//                msgService.sendDelayMsgToMQ(SysConstant.SYS_SP_EXCHANGE,SysConstant.SYS_SP_KEY, JSONUtil.toJsonStr(map),0);//30???????????????
        }


        if("?????????????????????".equals(task.getName()) && !"".equals(uploads)){
            /** ???????????? **/
            if (org.apache.commons.lang3.StringUtils.isNotBlank(uploads)) {
                //????????????
                ActWorkflowFormData data = new ActWorkflowFormData();
                data.setExecutionId(taskExe.getExecutionId());
                data.setBusinessKey(processInstance.getBusinessKey());
                data.setFormKey(taskExe.getFormKey());
                data.setControlName("????????????");
                data.setControlValue("?????????");
                data.setTaskNodeName(taskExe.getName());
                data.setCreateName(loginUser.getUser().getNickName());
                data.setCreateBy(loginUser.getUser().getUserId().toString());
                data.setCreateTime(new Date());
                acwfds = new ArrayList<>();
                acwfds.add(data);
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

            }
        }
        try{
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            if (hasVariables) {
                //?????????????????????
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                        .withVariables(variables)
                        .build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                        .build());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        //?????????????????????????????????
        Map returnMap = new HashMap();
        returnMap.put("acwfds",acwfds);
        returnMap.put("processInstance",processInstance);
        Map pmap = new HashMap();
        returnMap.put("processDefinitionKey",processInstance.getProcessDefinitionKey());
        returnMap.put("processDefinitionId",processInstance.getId());
        returnMap.put("businessKey",processInstance.getBusinessKey());
        msgService.sendDelayMsgToMQ(SysConstant.SYS_PRO_AUDIT_EXCHANGE,SysConstant.SYS_PRO_AUDIT_KEY, JSONUtil.toJsonStr(returnMap),0);//30???????????????
        int num = actWorkflowFormDataService.insertActWorkflowFormDatas(acwfds);
        if(num>0){
            if(flag){
                try{
                    if(agree){
                        List<String> userList = (List)map.get("users");
                        if(userList!=null && userList.size()>0)
                        msgService.sendDelayMsgToMQ(SysConstant.SYS_ORDER_DELAY_EXCHANGE,SysConstant.SYS_ORDER_DELAY_KEY, JSONUtil.toJsonStr(map),0);//30???????????????
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return num;
    }



    /**
     * ???????????????????????????????????????/?????????/????????????
     *
     * @param flowElement
     * @return
     */
    public  boolean getFlowElementStatus(FlowElement flowElement, String executionId) {
        Activity activity = (Activity) flowElement;
        MultiInstanceLoopCharacteristics loopCharacteristics = activity.getLoopCharacteristics();
        if (loopCharacteristics == null) {
            // ???????????????????????????
            UserTask userTask = (UserTask) flowElement;
//            getTaskStatus();
            return true;
        } else {
            // ???????????????????????????
            // ??????????????????????????????
            Integer complete = (Integer) runtimeService.getVariable(executionId, "nrOfCompletedInstances");
            Integer all = (Integer) runtimeService.getVariable(executionId, "nrOfInstances");
            if((complete + 1) / all == 1){
                return true;
            }else{
                return false;
            }


//            if(complete.equals(all)|| complete==(all)){
//                return true;
//            } else{
//                return false;
//            }
        }
    }


    private HistoricTaskInstanceQuery getTaskDoneQuery(SearchTodoTaskVo searchTodoTaskVo) {

        HistoricTaskInstanceQuery hisTask =		historyService.createHistoricTaskInstanceQuery();
        if (searchTodoTaskVo != null) {
            String processDefinitionKey = searchTodoTaskVo.getProcessDefinitionKey();
            if (StringUtils.isNotEmpty(processDefinitionKey)) {
                hisTask = hisTask.processDefinitionKey(processDefinitionKey);
            }
            String processDefinitionName = searchTodoTaskVo.getProcessDefinitionName();
            if (StringUtils.isNotEmpty(processDefinitionName)) {
                hisTask = hisTask.processDefinitionNameLike(processDefinitionName);
            }
            String taskName = searchTodoTaskVo.getTaskName();
            if (StringUtils.isNotEmpty(taskName)) {
                hisTask = hisTask.taskNameLike(taskName);
            }
            //before,after ????????????api????????????
            Date startDueDate = searchTodoTaskVo.getStartDueDate();
            if ( startDueDate!= null) {
                hisTask.taskCompletedAfter(startDueDate);
            }

            Date endDueDate = searchTodoTaskVo.getEndDueDate();
            if ( endDueDate!= null) {
                hisTask.taskCompletedBefore(endDueDate);

            }

        }
        return hisTask;
    }

    /**
     * ????????????
     * @param processId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTaskByProcessId(String processId,String status){
        runtimeService.deleteProcessInstance(processId, "??????");
        if("ng".equals(status)){
            CtBuApprovalNg ng = new CtBuApprovalNg();
            ng.setStatus(6);
            ng.setStatusPs("??????");
            ng.setInstanceId(processId);
            iCtBuApprovalNgService.updateCtBuApprovalNgByInstanceId(ng);
        }
        if("fpsq".equals(status)){
            CtBuInvoiceReqDto ctBuInvoiceReq = new CtBuInvoiceReqDto();
            ctBuInvoiceReq.setStatus(6);
//            ctBuInvoiceReq.setStatusPs("??????");
            ctBuInvoiceReq.setInstanceId(processId);
            ctBuInvoiceReqMapper.updateCtBuInvoiceReqByInstanceId(ctBuInvoiceReq);
        }

        if("fpsh".equals(status)){
            CtBuReceiptInfo info = new CtBuReceiptInfo();
            info.setStatus(Long.parseLong("6"));
            info.setStatusps("??????");
            info.setInstanceId(processId);
            iCtBuReceiptInfoService.updateEndCtBuReceiptInfo(info);
        }

        //??????????????????
        if("contractTermination".equals(status)){
            CtBuContract ctBuContract = new CtBuContract();
            ctBuContract.setStatus(6);
            ctBuContract.setStatusPs("??????");
            ctBuContract.setInstanceId(processId);
            ctBuContract.setExpiryDate(new Date());
            ctBuContractMapper.updateCtBuContractByInstanceId(ctBuContract);
        }

        //????????????????????????
        if("ticketRecordTermination".equals(status)){
            TicketRecord ticketRecord = new TicketRecord();
            ticketRecord.setStatus(6L);
            ticketRecord.setStatusps("??????");
            ticketRecord.setInstanceId(processId);
            ticketRecordMapper.updateTicketRecordByInstanceId(ticketRecord);
        }

        //????????????????????????
        if("payinfoTermination".equals(status)){
            CtBuPayinfo ctBuPayinfo = new CtBuPayinfo();
            ctBuPayinfo.setStatus(6);
            ctBuPayinfo.setStatusps("??????");
            ctBuPayinfo.setInstanceId(processId);
            ctBuPayinfoMapper.updateCtBuPayinfoByInstanceId(ctBuPayinfo);
        }

        //???????????????????????????
        if("appNgcTermination".equals(status)){
            AppNgc appNgc = new AppNgc();
            appNgc.setStatus(6);
            appNgc.setStatusPs("??????");
            appNgc.setInstanceId(processId);
            appNgcMapper.updateAppNgcByInstanceId(appNgc);
        }
    }
    /**
     * ????????????
     * @param taskID
     * @param userName
     */
    @Override
    public int setAssign(String taskID,String userName) {
        int num =0;
        try{
            taskService.setAssignee(taskID, userName);
            num = 1;
        }catch (Exception e){
            num =0;
        }
       return num;
    }




}
