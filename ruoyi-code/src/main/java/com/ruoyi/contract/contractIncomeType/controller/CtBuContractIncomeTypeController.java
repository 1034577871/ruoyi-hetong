package com.ruoyi.contract.contractIncomeType.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.contract.contractIncomeType.domain.CtBuContractIncomeType;
import com.ruoyi.contract.contractIncomeType.service.ICtBuContractIncomeTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同收入类型Controller
 * 
 * @author ruoyi
 * @date 2021-02-22
 */
@RestController
@RequestMapping("/contractIncomeType/contractIncomeType")
public class CtBuContractIncomeTypeController extends BaseController
{
    @Autowired
    private ICtBuContractIncomeTypeService ctBuContractIncomeTypeService;

    /**
     * 查询合同收入类型列表
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:list')")
    @GetMapping("/list")
    public TableDataInfo list(CtBuContractIncomeType ctBuContractIncomeType)
    {
        startPage();
        List<CtBuContractIncomeType> list = ctBuContractIncomeTypeService.selectCtBuContractIncomeTypeList(ctBuContractIncomeType);
        return getDataTable(list);
    }

    /**
     * 导出合同收入类型列表
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:export')")
    @Log(title = "合同收入类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CtBuContractIncomeType ctBuContractIncomeType)
    {
        List<CtBuContractIncomeType> list = ctBuContractIncomeTypeService.selectCtBuContractIncomeTypeList(ctBuContractIncomeType);
        ExcelUtil<CtBuContractIncomeType> util = new ExcelUtil<CtBuContractIncomeType>(CtBuContractIncomeType.class);
        return util.exportExcel(list, "contractIncomeType");
    }

    /**
     * 获取合同收入类型详细信息
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(ctBuContractIncomeTypeService.selectCtBuContractIncomeTypeById(id));
    }

    /**
     * 新增合同收入类型
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:add')")
    @Log(title = "合同收入类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CtBuContractIncomeType ctBuContractIncomeType)
    {
        return toAjax(ctBuContractIncomeTypeService.insertCtBuContractIncomeType(ctBuContractIncomeType));
    }

    /**
     * 修改合同收入类型
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:edit')")
    @Log(title = "合同收入类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CtBuContractIncomeType ctBuContractIncomeType)
    {
        return toAjax(ctBuContractIncomeTypeService.updateCtBuContractIncomeType(ctBuContractIncomeType));
    }

    /**
     * 删除合同收入类型
     */
//    @PreAuthorize("@ss.hasPermi('contractIncomeType:contractIncomeType:remove')")
    @Log(title = "合同收入类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(ctBuContractIncomeTypeService.updateCtBuContractIncomeTypeByIds(ids));
    }
}
