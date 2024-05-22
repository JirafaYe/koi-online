package com.xc.log.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.xc.api.client.log.LogClient;
import com.xc.api.dto.log.req.LogInfo;
import com.xc.common.utils.StringUtils;
import com.xc.log.service.LogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private LogInfoService logInfoService;

    @Resource
    private LogClient logClient;

    /**
     * 环绕通知
     * @param joinPoint 切点
     * @param log 日志注解
     * @return 方法返回值
     * @throws Throwable 异常信息
     */
    @Around(value = "execution(* *(..)) && @annotation(log)")
    public Object doAround(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;
        try {
            result = joinPoint.proceed();   // 执行切点
        } catch (Exception e) {
            exception = e;
            throw e; // 让异常继续向上抛，以便于Spring的异常处理器处理
        } finally {
            handleLog(joinPoint, log, exception, result);
        }
        return result;
    }
    protected void handleLog(final JoinPoint joinPoint, Log log, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            Long user = UserContext.getUser();
            // 日志记录
            LogInfo logInfo = new LogInfo();
            logInfo.setStatus(0);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();

            // 请求的IP地址
            String iP = ServletUtil.getClientIP(request);
            if ("0:0:0:0:0:0:0:1".equals(iP)) {
                iP = "127.0.0.1";
            }
            logInfo.setOperIp(iP);
            logInfo.setOperUrl(request.getRequestURI());
            if (user != null) {
                logInfo.setUserId(user);
            }
            if (e != null) {
                logInfo.setStatus(1);
                logInfo.setErrorMsg(e.getMessage());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            logInfo.setMethod(className + "." + methodName + "()");
            logInfo.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, log, logInfo, jsonResult);
            // 保存数据库
            logClient.saveLog(logInfo);
        } catch (Exception exp) {
            LogAspect.log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param logInfo 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, LogInfo logInfo, Object jsonResult) throws Exception {
        // 设置操作业务类型
        logInfo.setBusinessType(log.businessType().ordinal());
        // 设置标题
        logInfo.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 设置参数的信息
            setRequestValue(joinPoint, logInfo);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && jsonResult != null) {
            logInfo.setJsonResult(StringUtils.sub(JSONUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param logInfo 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, LogInfo logInfo) throws Exception {
        String requsetMethod = logInfo.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requsetMethod) || HttpMethod.POST.name().equals(requsetMethod)) {
            String parsams = argsArrayToString(joinPoint.getArgs());
            logInfo.setOperParam(parsams);
        } else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            logInfo.setOperParam(paramsMap.toString());
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object object : paramsArray) {
                // 不为空 并且是不需要过滤的 对象
                if (object != null && !isFilterObject(object)) {
                    Object jsonObj = JSONUtil.parse(object);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param object 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object object) {
        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) object;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) object;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return object instanceof MultipartFile || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse || object instanceof BindingResult;
    }
}
