package com.ruoyi.projectApproval.AppxNgReq.mapper;

import java.util.List;

import com.ruoyi.projectApproval.AppxNgReq.domain.AppxNgReq;

/**
 * 事前审批Mapper接口
 *
 * @author Mu
 * @date 2020-12-09
 */
public interface AppxNgReqMapper {

    /**
     * 查询事前审批
     *
     * @param id 事前审批ID
     * @return 事前审批
     */
    public AppxNgReq selectAppxNgReqById(String id);

    /**
     * @Title: IAppxNgReqService.java
     * @Description: 根据事前审批主表ID获取事前审批信息
     * @Author M.Mu
     * @Date 2020-12-09
     */
    public AppxNgReq selectAppxNgReqByBid(String id);

    /**
     * 查询事前审批列表
     *
     * @param appxNgReq 事前审批
     * @return 事前审批集合
     */
    public List<AppxNgReq> selectAppxNgReqList(AppxNgReq appxNgReq);

    /**
     * 新增事前审批
     *
     * @param appxNgReq 事前审批
     * @return 结果
     */
    public int insertAppxNgReq(AppxNgReq appxNgReq);

    /**
     * 修改事前审批
     *
     * @param appxNgReq 事前审批
     * @return 结果
     */
    public int updateAppxNgReq(AppxNgReq appxNgReq);

    /**
     * 删除事前审批
     *
     * @param id 事前审批ID
     * @return 结果
     */
    public int deleteAppxNgReqById(String id);

    /**
     * 批量删除事前审批
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppxNgReqByIds(String[] ids);

    /**
     * @Title: AppxNgReqMapper.java
     * @Description: 根据事前审批主表ID逻辑删除事前审批信息
     * @Author M.Mu
     * @Date 2020-12-09
     */
    public int deleteAppxNgReqByBid(String id);

    /**
     * @Title: AppxNgReqMapper.java
     * @Description: 传入时间获取对应时间段内的集客审批数量
     * @Author M.Mu
     * @Date 2020-12-16
     */
    public Integer findNumsByData(AppxNgReq appxNgReq);
}