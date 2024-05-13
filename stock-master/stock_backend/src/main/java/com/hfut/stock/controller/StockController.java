package com.hfut.stock.controller;

import com.hfut.stock.pojo.domin.*;
import com.hfut.stock.service.StockService;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * Author:yuyang
 * Date:2024-04-25
 * Time:14:42
 */
@Api(value = "/api/quot", tags = {"Description: Author:yuyang Date:2024-04-25 Time:14:42"})
@RestController
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;

    /**
     * - 获取最新国内A股大盘信息（仅包含上证和深证大盘数据）;
     * - 查询时间点不在正常股票交易时间内，则显示最近时间点的交易信息;
     *
     * @return
     */
    @ApiOperation(value = "- 获取最新国内A股大盘信息（仅包含上证和深证大盘数据）; - 查询时间点不在正常股票交易时间内，则显示最近时间点的交易信息;", notes = "- 获取最新国内A股大盘信息（仅包含上证和深证大盘数据）; - 查询时间点不在正常股票交易时间内，则显示最近时间点的交易信息;", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> innerIndexAll() {
        return stockService.innerIndexAll();
    }

    /**
     * 外盘指数行情数据查询，根据时间和大盘点数降序排序取前4
     * @return
     */
    @ApiOperation(value = "外盘指数行情数据查询，根据时间和大盘点数降序排序取前4", notes = "外盘指数行情数据查询，根据时间和大盘点数降序排序取前4", httpMethod = "GET")
    @GetMapping("/external/index")
    public R<List<outerMarketDomain>> externalIndex4() {
        return stockService.externalIndex4();
    }

    /**
     * 查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录
     *
     * @return
     */
    @ApiOperation(value = "查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录", notes = "查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<blockMarketDomain>> blockIndexAll() {
        return stockService.blockIndexAll();
    }

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "")
    })
    @ApiOperation(value = "分页查询股票最新数据，并按照涨幅排序查询", notes = "分页查询股票最新数据，并按照涨幅排序查询", httpMethod = "GET")
    @GetMapping("stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getStockPageInfo(page, pageSize);
    }

    /**
     * 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     *
     * @return
     */
    @ApiOperation(value = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", notes = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", httpMethod = "GET")
    @GetMapping("stock/increase")
    public R<List<StockUpdownDomain>> getStockPageInfo4() {
        return stockService.getStockPageInfo4();
    }

    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     *
     * @return
     */
    @ApiOperation(value = "统计最新交易日下股票每分钟涨跌停的数量", notes = "统计最新交易日下股票每分钟涨跌停的数量", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String, List>> getStockUpdownCount() {
        return stockService.getStockUpdownCount();
    }

    /**
     * 将指定页的股票数据导出到excel表下
     *
     * @param response
     * @param page     当前页
     * @param pageSize 每页大小
     */

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "将指定页的股票数据导出到excel表下", notes = "将指定页的股票数据导出到excel表下", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void stockExport(HttpServletResponse response,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        stockService.stockExport(response, page, pageSize);
    }
    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    @ApiOperation(value = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", notes = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> stockTradeVol4InnerMarket(){
        return stockService.stockTradeVol4InnerMarket();
    }
    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * @return
     */
    @ApiOperation(value = "查询当前时间下股票的涨跌幅度区间统计功能", notes = "查询当前时间下股票的涨跌幅度区间统计功能", httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map<String,Object>> getStockUpDown(){
        return stockService.stockUpDownScopeCount();
    }
    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点", notes = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点", httpMethod = "GET")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(@RequestParam(value = "code",required = true) String code){
        return stockService.stockScreenTimeSharing(code);
    }

    /**
     * 获取个股最新分时行情数据，主要包含：
     * 开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;
     * @param code
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "", required = true)
    })
    @ApiOperation(value = "获取个股最新分时行情数据，主要包含： 开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;", notes = "获取个股最新分时行情数据，主要包含： 开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;", httpMethod = "GET")
    @GetMapping("/stock/screen/second/detail")
    public R<StockSecondDetailDomain> stockScreenSecondDetail(@RequestParam(value = "code",required = true) String code){
        return stockService.stockScreenSecondDetail(code);
    }
    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getDayKLinData(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.stockCreenDkLine(stockCode);
    }

    /**
     * 单个个股周K 数据查询 ，统计每周内的股票数据信息，信息包含：
     * 	股票ID、 一周内最高价、 一周内最低价 、周1开盘价、周5的收盘价、
     * 	整周均价、以及一周内最大交易日期（一般是周五所对应日期）;
     * @param stockCode
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "", required = true)
    })
    @ApiOperation(value = "单个个股周K 数据查询 ，统计每周内的股票数据信息，信息包含： 	股票ID、 一周内最高价、 一周内最低价 、周1开盘价、周5的收盘价、 	整周均价、以及一周内最大交易日期（一般是周五所对应日期）;", notes = "单个个股周K 数据查询 ，统计每周内的股票数据信息，信息包含： 	股票ID、 一周内最高价、 一周内最低价 、周1开盘价、周5的收盘价、 	整周均价、以及一周内最大交易日期（一般是周五所对应日期）;", httpMethod = "GET")
    @GetMapping("/stock/screen/weekkline")
    public R<List<Stock4EvrWeekDomain>> getWeekKLinData(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.stockCreenWkLine(stockCode);
    }

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param searchStr （只接受代码模糊查询，不支持文字查询）
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "searchStr", value = "（只接受代码模糊查询，不支持文字查询）", required = true)
    })
    @ApiOperation(value = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称", notes = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称", httpMethod = "GET")
    @GetMapping("/stock/search")
    public R<List<Map>> stockSearchObscure(@RequestParam("searchStr") String searchStr){
        return stockService.stockSearchObscure(searchStr);
    }

    /**
     * 个股主营业务查询接口
     * @param code 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "个股主营业务查询接口", notes = "个股主营业务查询接口", httpMethod = "GET")
    @GetMapping("/stock/describe")
    public R<stockBusinessDomain> stockDescribe(@RequestParam("code") String code){
        return stockService.stockDescribe(code);
    }

    /**
     * 个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10
     * @param code 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10", notes = "个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10", httpMethod = "GET")
    @GetMapping("/stock/screen/second")
    public R<List<StockScreenSecondDomain>> stockScreenSecond(@RequestParam("code") String code){
        return stockService.stockScreenSecond(code);
    }
}
