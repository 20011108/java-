package com.hfut.stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.hfut.stock.mapper.StockRtInfoMapper;
import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.pojo.domin.Stock4EvrDayDomain;
import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.pojo.vo.StockInfoConfig;
import com.hfut.stock.utils.DateTimeUtil;
import com.hfut.stock.vo.resp.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-24
 * Time:21:14
 */
@SpringBootTest
public class TestAll {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Test
    public void EncodeTest(){
        String s="$2a$10$JqoiFCw4LUj184ghgynYp.4kW5BVeAZYjKqu7xEKceTaq7X3o4I4W";
        boolean flag = passwordEncoder.matches("123456",s);
        System.out.println(flag);
    }
    @Test
    public void testPwd(){
        String pwd="123456";
        //加密  $2a$10$WAWV.QEykot8sHQi6FqqDOAnevkluOZJqZJ5YPxSnVVWqvuhx88Ha
        String encode = passwordEncoder.encode(pwd);
        System.out.println(encode);
        /*
            matches()匹配明文密码和加密后密码是否匹配，如果匹配，返回true，否则false
            just test
         */
        boolean flag = passwordEncoder.matches(pwd,encode);
        System.out.println(flag);
    }

    @Test
    public void test01(){
        //存入值
        redisTemplate.opsForValue().set("myname","zhangsan");
        //获取值
        String myname = redisTemplate.opsForValue().get("myname");
        System.out.println(myname);
    }
    @Test
    public void test02(){
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //2.查询股票信息
        List<Map> maps=stockRtInfoMapper.getStockUpDownSectionByTime(curDate);
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        /*List<Map> sortedMaps=new ArrayList<>();
        for (String s : upDownRange) {
            Map temp=null;
            for (Map map : maps) {
                if(map.containsValue(s))
                {
                    temp=map;break;
                }
            }
            if(temp==null){
                temp=new HashMap();
                temp.put("count",0);
                temp.put("title",s);
            }
            sortedMaps.add(temp);
        }*/
        List<Map> sortedMaps = upDownRange.stream().map(new Function<String, Map>() {
            @Override
            public Map apply(String s) {
                Optional<Map> first = maps.stream().filter(new Predicate<Map>() {
                    @Override
                    public boolean test(Map map) {
                        if (map.containsValue(s)) return true;
                        else return false;
                    }
                }).findFirst();
                if (first.isPresent()) return first.get();
                else {
                    HashMap temp = new HashMap();
                    temp.put("count", 0);
                    temp.put("title", s);
                    return temp;
                }
            }
        }).collect(Collectors.toList());
        //3.组装数据
        HashMap<String, Object> mapInfo = new HashMap<>();
        //获取指定日期格式的字符串
        String curDateStr = dateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        mapInfo.put("time",curDateStr);
        mapInfo.put("infos",sortedMaps);
        //4.返回数据
        System.out.println(mapInfo.toString());
    }

    @Test
    public void test03(){
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = dateTime.toDate();
        Date startTime =dateTime.minusDays(10).toDate();
        String code="000021";
        //2.调用mapper接口获取查询的集合信息-方案1
        List<Stock4EvrDayDomain> data1= stockRtInfoMapper.getStockInfo4EvrDayBlock(code,
                stockRtInfoMapper.getLatestTime(code,startTime,endTime));
        List<Stock4EvrDayDomain> data= stockRtInfoMapper.getStockInfo4EvrDay(code,startTime,endTime);
        //3.组装数据，响应
        System.out.println(data.toString());
        System.out.println(data1.toString());
        System.out.println(data==data1);
    }
    @Test
    public void test04(){
        List<SysPermission> permsByUserId = sysPermissionMapper.getPermsByUserId(1246071816909361152L);
        List<menusPermDomain> menus= digui(permsByUserId,0L);
        System.out.println(menus);
    }
    @Test
    public void test05(){
        List<SysPermission> permsByUserId = sysPermissionMapper.getPermsByUserId(1246071816909361152L);
        List<String> permissions=new ArrayList<>();
        for (SysPermission sysPermission : permsByUserId) {
            if(sysPermission.getType().equals(0003))
                permissions.add(sysPermission.getCode());
        }
        System.out.println(permissions.size());
        System.out.println(permissions);
    }
    public List<menusPermDomain> digui(List<SysPermission> permsByUserId,Long pid){
        ArrayList<menusPermDomain> list = new ArrayList<>();
        for (SysPermission p : permsByUserId) {
            if(p.getPid().equals(pid)){
                list.add(new menusPermDomain(p.getId(), p.getTitle(), p.getIcon(), p.getUrl(), p.getName(), digui(permsByUserId,p.getId())));
            }
        }
        return list;
    }
    /**
     * 测试base64编码
     */
    @Test
    public void testBase64Encode(){
        String info="11223344:zhangsan";
        String encodeInfo = BaseEncoding.base64().encode(info.getBytes());
        System.out.println(encodeInfo);
        //MTEyMjMzNDQ6emhhbmdzYW4=
    }

    /**
     * @Description 测试base64解码
     */
    @Test
    public void testDecode(){
        String encodeInfo="MTEyMjMzNDQ6emhhbmdzYW4=";
        byte[] decode = BaseEncoding.base64().decode(encodeInfo);
        String info = new String(decode);
        System.out.println(info);
    }
    @Test
    public void test06(){
       Object[] str={"234",11};
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> strings = new ArrayList<>();
        try {
            System.out.println(mapper.writeValueAsString(str));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
