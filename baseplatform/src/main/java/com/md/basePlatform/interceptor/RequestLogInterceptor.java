package com.md.basePlatform.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求日志拦截器
 *
 * <p>拦截所有 HTTP 请求，在请求进入 Controller 之前记录请求信息到日志中。
 * 主要用于开发调试和运维排查问题。
 *
 * <p>日志输出格式示例：
 * <pre>INFO: request method=GET, uri=/api/drones, query=null</pre>
 * <pre>INFO: request method=POST, uri=/api/drones, query=null</pre>
 *
 * <p>拦截器在 WebMvcConfig 中注册，拦截路径为 "/**"（所有路径）。
 * preHandle 返回 true 表示放行请求，继续执行后续拦截器和 Controller。
 */
@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    /** SLF4J 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogInterceptor.class);

    /**
     * 请求前置处理
     * <p>在 Controller 方法执行前调用，记录请求方法、URI 和查询参数。
     *
     * @param request   HTTP 请求对象
     * @param response  HTTP 响应对象
     * @param handler   处理器对象（通常是 HandlerMethod）
     * @return true 始终放行请求
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 输出格式：method=GET, uri=/api/drones, query=null
        LOGGER.info("request method={}, uri={}, query={}",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return true; // 返回 true 继续执行后续拦截器和 Controller
    }
}
