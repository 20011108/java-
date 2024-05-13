package com.hfut.stock.aop;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:18:02
 */

import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.hfut.stock.annotation.MyLog;
import com.hfut.stock.controller.RoleController;
import com.hfut.stock.mapper.SysLogMapper;
import com.hfut.stock.pojo.entity.SysLog;
import com.hfut.stock.service.LogService;
import com.hfut.stock.utils.IdWorker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.yaml.snakeyaml.events.Event;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 切面处理类，记录操作日志到数据库
 */
@Aspect
@Component
public class OperLogAspect {
    @Autowired
    private LogService logService;
    @Autowired
    private IdWorker idWorker;
    //为了记录方法的执行时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();


    /**
     * 设置操作日志切入点，这里介绍两种方式：
     * 1、基于注解切入（也就是打了自定义注解的方法才会切入）
     *
     * @Pointcut("@annotation(org.wujiangbo.annotation.MyLog)") 2、基于包扫描切入
     * @Pointcut("execution(public * org.wujiangbo.controller..*.*(..))")
     */
    @Pointcut("@annotation(com.hfut.stock.annotation.MyLog)")//在注解的位置切入代码
    //@Pointcut("execution(public * org.wujiangbo.controller..*.*(..))")//从controller切入
    public void operLogPoinCut() {
    }

    @Before("operLogPoinCut()")
    public void beforMethod(JoinPoint point) {
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */

    @Pointcut("execution(* com.hfut.stock.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "result")
    public void saveOperLog(JoinPoint joinPoint, Object result) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);

        SysLog sysLog = new SysLog();
        sysLog.setId(idWorker.nextId());
        String authorization = request.getHeader("Authorization");
        byte[] decode = BaseEncoding.base64().decode(authorization);
        String info = new String(decode);
        sysLog.setUserId(info.split(":")[0]);
        sysLog.setUsername(info.split(":")[1]);
        if (myLog != null) {
            sysLog.setOperation(myLog.value());
        }
        Long takeTime = System.currentTimeMillis() - startTime.get();//记录方法执行耗时时间（单位：毫秒）
        sysLog.setTime(Integer.parseInt(String.valueOf(takeTime)));
        //获取接口/类全限定名
        String className = signature.getDeclaringTypeName();
        //获取方法名
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName);
        ObjectMapper mapper = new ObjectMapper();
        try {
            sysLog.setParams(mapper.writeValueAsString(joinPoint.getArgs()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        sysLog.setIp(getIp(request));
        logService.addLog(sysLog);
    }
    //根据HttpServletRequest获取访问者的IP地址
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}