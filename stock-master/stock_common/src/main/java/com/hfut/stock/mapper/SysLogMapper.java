package com.hfut.stock.mapper;

import com.hfut.stock.pojo.entity.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    int addLog(@Param("sysLog") SysLog sysLog);

    /**
     * 多添加查询
     * @param username 操作者账户名称
     * @param operation 操作类型
     * @param startTime 起始时间
     * @param endTime 截止时间
     * @return
     */
    List<SysLog> findByCondition(@Param("username") String username, @Param("operation") String operation,
                                 @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据角色id批量删除用户信息
     * @param logIds
     */
    int deleteBatchByLogIds(@Param("logIds") List<Long> logIds);
}
