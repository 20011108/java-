package com.hfut.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hfut.stock.mapper.SysLogMapper;
import com.hfut.stock.pojo.entity.SysLog;
import com.hfut.stock.service.LogService;
import com.hfut.stock.vo.req.LogPageReqVo;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:18:03
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void addLog(SysLog sysLog) {
        threadPoolTaskExecutor.execute(()->{
            int i=sysLogMapper.addLog(sysLog);
            if(i<=0){
                log.error("日志插入错误；日志信息如下："+sysLog.toString());
            }else
                log.info("日志插入成功；日志信息如下："+sysLog.toString());
        });

    }
    @Override
    public R<PageResult> logPageQuery(LogPageReqVo vo) {
        if (vo==null) {
            return R.error(ResponseCode.DATA_ERROR.getMessage()) ;
        }
        //组装数据
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //分页查询
        List<SysLog> logList=this.sysLogMapper.findByCondition(vo.getUsername(),vo.getOperation(),vo.getStartTime(),vo.getEndTime());
        //封装PageResult
        PageResult<SysLog> pageResult = new PageResult<>(new PageInfo<>(logList));
        return R.ok(pageResult);
    }

    /**
     * 根据日志id集合批量删除日志信息
     * @param logIds
     * @return
     */
    @Override
    public R<String> deleteBatch(List<Long> logIds) {
        if (CollectionUtils.isEmpty(logIds)) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        this.sysLogMapper.deleteBatchByLogIds(logIds);
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }
}
