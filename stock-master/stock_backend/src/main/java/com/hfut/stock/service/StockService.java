package com.hfut.stock.service;

import com.hfut.stock.pojo.domin.*;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-25
 * Time:14:46
 */
public interface StockService {

    R<List<InnerMarketDomain>> innerIndexAll();

    R<List<blockMarketDomain>> blockIndexAll();

    R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize);

    R<List<StockUpdownDomain>> getStockPageInfo4();

    R<Map<String, List>> getStockUpdownCount();

    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    R<Map<String, List>> stockTradeVol4InnerMarket();

    R<Map<String, Object>> stockUpDownScopeCount();

    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);

    R<List<Stock4EvrDayDomain>> stockCreenDkLine(String stockCode);

    R<List<outerMarketDomain>> externalIndex4();

    R<List<Map>> stockSearchObscure(String searchStr);

    R<stockBusinessDomain> stockDescribe(String code);

    R<List<Stock4EvrWeekDomain>> stockCreenWkLine(String stockCode);

    R<StockSecondDetailDomain> stockScreenSecondDetail(String code);

    R<List<StockScreenSecondDomain>> stockScreenSecond(String code);
}
