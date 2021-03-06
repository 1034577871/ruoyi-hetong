package com.ruoyi.contract.contractInfo.controller;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.contract.contractInfo.domain.CtBuContractVo;
import com.ruoyi.contract.contractInfo.util.ContractInfoUtil;
import com.ruoyi.contract.contractPayinfo.domain.CtBuContractPayinfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.contract.contractContent.domain.CtBuContractContent;
import com.ruoyi.contract.contractInfo.domain.CtBuContract;
import com.ruoyi.contract.contractInfo.service.ICtBuContractService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同信息录入主Controller
 *
 * @author ruoyi
 * @date 2020-12-08
 */
@RestController
@RequestMapping("/contract/contractInfo")
public class CtBuContractController extends BaseController {
    @Autowired
    private ICtBuContractService ctBuContractService;

    /**
     * 查询合同信息录入主列表
     */
    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CtBuContract ctBuContract) {
        startPage();
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList(ctBuContract);
        return getDataTable(list);
    }

    /**
     * 查询合同信息录入主列表
     */
    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/list1")
    public TableDataInfo list1(CtBuContract ctBuContract) {
        startPage();
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList1(ctBuContract);
        return getDataTable(list);
    }

    /**
     * 查询合同信息录入主列表
     */
    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/list2")
    public TableDataInfo list2(CtBuContract ctBuContract) {
        startPage();
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList2(ctBuContract);
        return getDataTable(list);
    }


    /**
     * 查询合同信息录入主列表
     */
    @PreAuthorize("@ss.hasPermi('contract:contractInfo:listSum')")
    @GetMapping("/listSum")
    public TableDataInfo listSum(CtBuContract ctBuContract) {
        startPage();
        List<Object> list = ctBuContractService.selectCtBuContractListSum(ctBuContract);
        return getDataTable(list);
    }


    /**
     * 查询合同信息统计数据
     */
    @PreAuthorize("@ss.hasPermi('contract:contractInfo:sum')")
    @GetMapping("/sum")
    public Map<String, BigDecimal> sum(CtBuContract ctBuContract) {
        return ctBuContractService.selectCtBuContractSum(ctBuContract);
    }


    /**
     * 导出合同信息录入主列表
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:export')")
    @Log(title = "合同信息录入主", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CtBuContract ctBuContract) {
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList1(ctBuContract);
        for (CtBuContract cbc : list){
            if ("9f30ebd46d3c428cbc5e54f7175eb4f2".equals(cbc.getCbanProType())){
                cbc.setProjectCategoryName("集客类");
            } else {
                cbc.setProjectCategoryName("非集客类");
            }
        }
        ExcelUtil<CtBuContract> util = new ExcelUtil<CtBuContract>(CtBuContract.class);
        return util.exportExcel(list, "contract");
    }

    /**
     * 获取合同信息录入主详细信息
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(ctBuContractService.selectCtBuContractById(id));
    }

    /**
     * 新增合同信息录入主
     */
    //@PreAuthorize("@ss.hasPermi('contract:contractInfo:add')")
    @Log(title = "合同信息录入主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CtBuContract ctBuContract) {
        //非空校验
//        if (ctBuContract.getOppositeBankId() == null) {
//            ContractInfoUtil.checkContractInfo(ctBuContract);
//            for (CtBuContractContent cc : ctBuContract.getCtBuContractContentList()) {
//                ContractInfoUtil.checkContractContent(cc);
//            }
//            for (CtBuContractPayinfo cp : ctBuContract.getCtBuContractPayinfoList()) {
//                ContractInfoUtil.checkContractPayInfo(cp);
//            }
//        }
        return toAjax(ctBuContractService.insertCtBuContract(ctBuContract));
    }

    /**
     * 修改合同信息录入主
     */
    //@PreAuthorize("@ss.hasPermi('contract:contractInfo:edit')")
    @Log(title = "合同信息录入主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CtBuContract ctBuContract) {
        return toAjax(ctBuContractService.updateCtBuContract(ctBuContract));
    }

    /**
     * 删除合同信息录入主
     */
//    @PreAuthorize("@ss.hasPermi('dictionary:contract:remove')")
//    @Log(title = "合同信息录入主", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable String[] ids)
//    {
//        return toAjax(ctBuContractService.deleteCtBuContractByIds(ids));
//    }


    /**
     * 逻辑删除合同信息录入主
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:remove')")
    @Log(title = "合同信息录入主", businessType = BusinessType.UPDATE)
    @PutMapping("/{ids}")
    public AjaxResult editRemove(@PathVariable String[] ids) {
        return toAjax(ctBuContractService.updateCtBuContractByIds(ids));
    }

    /**
     * 保存、修改付款记录
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:edit')")
    @Log(title = "付款记录", businessType = BusinessType.UPDATE)
    @PutMapping("/saveTicketInfo")
    public AjaxResult saveTicketInfo(@RequestBody CtBuContract ctBuContract) {
        return toAjax(ctBuContractService.saveTicketInfo(ctBuContract));
    }

    /**
     * 查询合同内容列表(付款记录查询 查询所有支出的合同)
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/contractlist")
    public TableDataInfo contractlist(CtBuContract ctBuContract) {
        startPage();
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList(ctBuContract);
        return getDataTable(list);
    }

    /**
     * 项目统计
     *
     * @return
     */
    @GetMapping("/projectCount")
    public List<HashMap<String, Object>> projectCount(Date beginTime, Date endTime) {
        List<HashMap<String, Object>> hashMaps = ctBuContractService.contractCount(beginTime,endTime);
        return hashMaps;
    }


    /**
     * 查询合同信息录入主列表
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/collectionAndPaymentStatistics")
    public TableDataInfo collectionAndPaymentStatistics(CtBuContract ctBuContract) {
        startPage();
        List<CtBuContract> list = ctBuContractService.collectionAndPaymentStatistics(ctBuContract);
        return getDataTable(list);
    }


    /**
     * 合同统计列表查询
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:list')")
    @GetMapping("/contractStatisticsList")
    public TableDataInfo contractStatisticsList(String manageCompany, Integer type) {
        startPage();
        List<CtBuContract> list = ctBuContractService.contractStatisticsList(manageCompany, type);
        return getDataTable(list);
    }


    @GetMapping("/contractWarningList")
    public TableDataInfo contractWarningList() {
        startPage();
        List<HashMap<String, Object>> hashMaps = ctBuContractService.contractWarningList();
        return getDataTable(hashMaps);
    }

    /**
     * 首页本月合同统计
     * @return
     */
    @GetMapping("/countContractIndex")
    public HashMap countContractIndex(CtBuContract ctBuContract) {
        HashMap<String, Object> hashMaps = ctBuContractService.countContractIndex(ctBuContract);
        return hashMaps;
    }


    /**
     * 根据收付款周期预警
     * @return
     */
    @GetMapping("/notReceivedWarning")
    public TableDataInfo notReceivedWarning(CtBuContract ctBuContract) {
        List<HashMap<String, Object>> hashMaps = ctBuContractService.notReceivedWarning(ctBuContract);
        return getDataTable(hashMaps);
    }

    /**
     * 法务归档
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:updateIsArchiveByIds')")
    @Log(title = "法务归档", businessType = BusinessType.UPDATE)
    @PutMapping("/updateIsArchiveByIds/{ids}")
    public AjaxResult updateIsArchiveByIds(@PathVariable String[] ids) {
        return toAjax(ctBuContractService.updateIsArchiveByIds(ids));
    }

    /**
     * 法务归档
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:updateIsArchiveByIds')")
    @Log(title = "法务取消归档", businessType = BusinessType.UPDATE)
    @PutMapping("/updateCancelIsArchiveByIds/{ids}")
    public AjaxResult updateCancelIsArchiveByIds(@PathVariable String[] ids) {
        return toAjax(ctBuContractService.updateCancelIsArchiveByIds(ids));
    }

    /**
     * 导出法务归档合同信息录入主列表
     */
//    @PreAuthorize("@ss.hasPermi('contract:contractInfo:export')")
    @Log(title = "法务归档导出", businessType = BusinessType.EXPORT)
    @GetMapping("/exportArchive")
    public AjaxResult exportArchive(CtBuContract ctBuContract) {
        List<CtBuContract> list = ctBuContractService.selectCtBuContractList2(ctBuContract);
        ArrayList<CtBuContractVo> ctBuContractVos = new ArrayList<>();
        for (CtBuContract cbc : list){
            CtBuContractVo ctBuContractVo = new CtBuContractVo();
//            if ("9f30ebd46d3c428cbc5e54f7175eb4f2".equals(cbc.getCbanProType())){
//                ctBuContractVo.setProjectCategoryName("集客类");
//            } else {
//                ctBuContractVo.setProjectCategoryName("非集客类");
//            }
            if (cbc.getIsArchive() != null){
                if (cbc.getIsArchive() == 1){
                    ctBuContractVo.setIsArchiveName("已归档");
                } else if (cbc.getIsArchive() == 2){
                    ctBuContractVo.setIsArchiveName("未归档");
                }
            }

            ctBuContractVo.setContractNo(cbc.getContractNo());
            ctBuContractVo.setContractName(cbc.getContractName());
            ctBuContractVo.setContractCategoryName(cbc.getContractCategoryName());
            ctBuContractVo.setOppositeName(cbc.getOppositeName());
            ctBuContractVo.setOurName(cbc.getOurName());
            ctBuContractVo.setStarttime(cbc.getStarttime());
            ctBuContractVo.setTotalMoney(cbc.getTotalMoney());
            ctBuContractVo.setTaxrate(cbc.getTaxrate());
            ctBuContractVo.setNotTaxMoney(cbc.getNotTaxMoney());
            ctBuContractVo.setPayMethodName(cbc.getPayMethodName());
            ctBuContractVo.setManageDeptmentName(cbc.getManageDeptmentName());
            ctBuContractVo.setManageUserName(cbc.getManageUserName());
            ctBuContractVo.setStatusPs(cbc.getStatusPs());
            ctBuContractVos.add(ctBuContractVo);
        }
        ExcelUtil<CtBuContractVo> util = new ExcelUtil<CtBuContractVo>(CtBuContractVo.class);
        return util.exportExcel(ctBuContractVos, "contract");
    }

}
