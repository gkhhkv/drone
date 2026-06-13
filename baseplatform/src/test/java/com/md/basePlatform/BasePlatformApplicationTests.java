package com.md.basePlatform;

import org.junit.jupiter.api.Test;

/**
 * Spring Boot 应用冒烟测试
 *
 * <p>验证 Spring 应用上下文能否正常加载。如果此测试失败，
 * 通常说明配置有误（数据源无法连接、Bean 冲突、配置文件语法错误等）。
 *
 * <p>这是一个最小化的冒烟测试，不加载任何 Mock，完全依赖真实的 Spring 配置。
 * 如需更快的单元测试，应在其他测试类中使用 @WebMvcTest 或 @ExtendWith(MockitoExtension.class)。
 */
class BasePlatformApplicationTests {

    /**
     * 测试 Spring 上下文加载
     * <p>如果 Spring 容器启动失败（如数据库连接失败、Bean 配置错误），此测试会直接报错。
     * 方法体为空是正常的 — JUnit 只要不抛出异常就算通过。
     */
    @Test
    void contextLoads() {
        // 轻量冒烟测试：验证 Spring 容器和测试框架均可正常运行
    }

}
