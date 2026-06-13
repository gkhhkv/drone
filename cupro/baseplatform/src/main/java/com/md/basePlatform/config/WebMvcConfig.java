package com.md.basePlatform.config;

import com.md.basePlatform.interceptor.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 *
 * <p>负责注册自定义拦截器等 MVC 层配置。当前主要功能：
 * <ul>
 *   <li>注册 {@link RequestLogInterceptor}：拦截所有请求（/**），记录请求日志</li>
 * </ul>
 *
 * <p>拦截器执行顺序：按注册顺序执行 preHandle，逆序执行 postHandle/afterCompletion。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestLogInterceptor requestLogInterceptor;

    /**
     * 构造注入请求日志拦截器（Spring 推荐的 DI 方式，无需 @Autowired）
     *
     * @param requestLogInterceptor 由 Spring 容器管理的请求日志拦截器 Bean
     */
    public WebMvcConfig(RequestLogInterceptor requestLogInterceptor) {
        this.requestLogInterceptor = requestLogInterceptor;
    }

    /**
     * 注册拦截器
     * <p>addPathPatterns("/**") 拦截所有请求路径。
     * 如需排除某些路径（如静态资源），可使用 excludePathPatterns()。
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**");
    }
}
