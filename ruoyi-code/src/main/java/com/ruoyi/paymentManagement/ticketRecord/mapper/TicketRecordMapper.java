package com.ruoyi.paymentManagement.ticketRecord.mapper;

import java.util.List;

import com.ruoyi.paymentManagement.ticketRecord.domain.TicketRecord;

/**
 * 收票记录Mapper接口
 * 
 * @author lps
 * @date 2020-12-17
 */
public interface TicketRecordMapper 
{
    /**
     * 查询收票记录
     * 
     * @param id 收票记录ID
     * @return 收票记录
     */
    public TicketRecord selectTicketRecordById(String id);

    /**
     * 查询收票记录列表
     * 
     * @param ticketRecord 收票记录
     * @return 收票记录集合
     */
    public List<TicketRecord> selectTicketRecordList(TicketRecord ticketRecord);

    /**
     * 新增收票记录
     * 
     * @param ticketRecord 收票记录
     * @return 结果
     */
    public int insertTicketRecord(TicketRecord ticketRecord);

    /**
     * 修改收票记录
     * 
     * @param ticketRecord 收票记录
     * @return 结果
     */
    public int updateTicketRecord(TicketRecord ticketRecord);

    /**
     * 修改收票记录
     *
     * @param ticketRecord 收票记录
     * @return 结果
     */
    public int updateTicketRecordByInstanceId(TicketRecord ticketRecord);

    /**
     * 删除收票记录
     * 
     * @param id 收票记录ID
     * @return 结果
     */
    public int deleteTicketRecordById(String id);

    /**
     * 逻辑删除收票记录
     *
     * @param id 收票记录ID
     * @return 结果
     */
    public int updateTicketRecordById(String id);

    /**
     * 批量删除收票记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTicketRecordByIds(String[] ids);


    /**
     * 查询收票记录
     * @param instanceId 收票记录实例ID
     * @return 收票记录
     */
    public TicketRecord selectTicketRecordByInstanceId(String instanceId);

    /**
     * 查询收票记录列表
     *
     * @param ticketRecord 收票记录
     * @return 收票记录集合
     */
    public List<TicketRecord> selectTicketRecordList1(TicketRecord ticketRecord);

    public List<TicketRecord> selectTicketRecordList2(TicketRecord ticketRecord);


}
