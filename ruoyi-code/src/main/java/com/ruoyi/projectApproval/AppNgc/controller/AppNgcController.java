package com.ruoyi.projectApproval.AppNgc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.projectApproval.AppNgc.domain.AppNgc;
import com.ruoyi.projectApproval.AppNgc.service.IAppNgcService;

/**
 * 非集客项目Controller
 *
 * @author ruoyi
 * @date 2020-12-11
 */
@RestController
@RequestMapping("/projectApproval/appNgc")
public class AppNgcController extends BaseController {

    @Autowired
    private IAppNgcService appNgcService;

    /**
     * 查询非集客项目列表
     */
//    @PreAuthorize("@ss.hasPermi('projectApproval:appNgc:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppNgc appNgc) {
        startPage();
        List<AppNgc> list = appNgcService.selectAppNgcList(appNgc);
        return getDataTable(list);
    }

    /**
     * 导出非集客项目列表
     */
//    @PreAuthorize("@ss.hasPermi('projectApproval:appNgc:export')")
    @Log(title = "非集客项目", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AppNgc appNgc) {
        List<AppNgc> list = appNgcService.selectAppNgcList(appNgc);
        ExcelUtil<AppNgc> util = new ExcelUtil<AppNgc>(AppNgc.class);
        return util.exportExcel(list, "appNgc");
    }

    /**
     * 获取非集客项目详细信息
     */
    //@PreAuthorize("@ss.hasPermi('projectApproval:appNgc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(appNgcService.selectAppNgcById(id));
    }

    /**
     * 新增非集客项目
     */
    //@PreAuthorize("@ss.hasPermi('projectApproval:appNgc:add')")
    @Log(title = "非集客项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppNgc appNgc) {
//        int i = appNgcService.checkProNameUnique(appNgc.getProName());
//        if (i > 0) {
//            return AjaxResult.error("新增非集客项目'" + appNgc.getProName() + "'失败，项目名称已存在");
//        }
        return toAjax(appNgcService.insertAppNgc(appNgc));
    }

    /**
     * 修改非集客项目
     */
    //@PreAuthorize("@ss.hasPermi('projectApproval:appNgc:edit')")
    @Log(title = "非集客项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppNgc appNgc) {
        return toAjax(appNgcService.updateAppNgc(appNgc));
    }

    /**
     * 删除非集客项目
     */
//    @PreAuthorize("@ss.hasPermi('projectApproval:appNgc:remove')")
    @Log(title = "非集客项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(appNgcService.updateAppNgcById(ids));
    }

    @GetMapping(value = "/findNextAppNgcNo")
    public AjaxResult findNextAppNo(Integer falg) {
        return AjaxResult.success(appNgcService.findNextAppNo(falg));
    }


    @GetMapping("/checkProNameUnique/{proName}")
    public int checkProNameUnique(@PathVariable String proName){
        return appNgcService.checkProNameUnique(proName);
    }
}
