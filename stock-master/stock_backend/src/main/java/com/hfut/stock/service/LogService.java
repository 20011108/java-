package com.hfut.stock.service;

import com.hfut.stock.pojo.entity.SysLog;
import com.hfut.stock.vo.req.LogPageReqVo;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;

import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:18:03
 */
public interface LogService {
    void addLog(SysLog sysLog);
    /**
     * 日志信息分页综合查询
     * @param vo
     * @return
     */
    R<PageResult> logPageQuery(LogPageReqVo vo);

    /**
     * 删除日志信息
     * @param logIds
     * @return
     */
    R<String> deleteBatch(List<Long> logIds);
}
