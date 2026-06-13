package com.md.basePlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 无人机信息管理系统 — Spring Boot 启动入口
 *
 * <p>本类是整个后端应用的启动类，{@link SpringBootApplication} 注解会自动：
 * <ul>
 *   <li>启用自动配置（根据 classpath 中的依赖自动装配 Bean）</li>
 *   <li>启用组件扫描（扫描本包及子包下的 @Component、@Service、@Controller 等）</li>
 *   <li>启用 @EnableAutoConfiguration（根据依赖自动配置数据源、MVC 等）</li>
 * </ul>
 *
 * <p>启动方式：
 * <pre>java -jar baseplatform.jar</pre>
 * 或通过 IDE 直接运行 main 方法。
 *
 * @author md
 * @since 1.0
 */
@SpringBootApplication
public class BasePlatformApplication {

    /**
     * 应用主入口。
     * <p>调用 SpringApplication.run 启动内嵌 Tomcat 容器，加载所有 Spring Bean，
     * 并监听 application.yml 中配置的端口（默认 8080）。
     *
     * @param args 命令行参数（可传入 --spring.profiles.active=mysql|sqlite 指定配置环境）
     */
    public static void main(String[] args) {
        SpringApplication.run(BasePlatformApplication.class, args);
    }

}
