package com.md.basePlatform.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Apache Shiro 安全框架配置
 *
 * <p>Shiro 是一个 Java 安全框架，负责：认证（登录验证）、授权（角色/权限判断）、会话管理。
 *
 * <p>核心概念：
 * <ul>
 *   <li><b>Realm（域）</b> — 数据源，告诉 Shiro 用户信息和权限从哪里获取（数据库 / 内存 / LDAP）</li>
 *   <li><b>SecurityManager（安全管理器）</b> — Shiro 的核心，协调所有组件</li>
 *   <li><b>Filter Chain（过滤器链）</b> — URL 级别的访问控制：哪些 URL 需要登录，哪些可以匿名访问</li>
 * </ul>
 *
 * <p>过滤器名称说明：
 * <ul>
 *   <li>anon（Anonymous） — 匿名可访问，即不需要登录</li>
 *   <li>authc（Authentication） — 需要已认证的用户才能访问</li>
 *   <li>roles[admin] — 需要具有 admin 角色</li>
 *   <li>perms[drone:delete] — 需要具有 drone:delete 权限</li>
 * </ul>
 *
 * <p>当前配置了两种过滤器链定义方式（任一生效即可）：
 * <ul>
 *   <li>shiroFilterChainDefinition() — Shiro 自动配置会读取此 Bean 构建过滤器</li>
 *   <li>shiroFilterFactoryBean() — 显式创建 ShiroFilterFactoryBean，提供更多自定义选项</li>
 * </ul>
 * 两者定义相同规则，实际生效的是 ShiroFilterFactoryBean（优先级更高）。
 */
@Configuration
public class ShiroConfig {

    /**
     * 注册 Realm Bean
     * <p>@Bean 注解将此方法返回值纳入 Spring 容器管理。
     * Realm 是 Shiro 的"数据桥"，负责从自定义数据源加载认证/授权信息。
     */
    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    /**
     * 配置 Shiro 的核心：SecurityManager
     *
     * <p>SecurityManager 是 Shiro 的"大管家"，协调所有模块：
     * <ul>
     *   <li>认证（调用 Realm 的认证方法）</li>
     *   <li>授权（调用 Realm 的授权方法）</li>
     *   <li>会话管理</li>
     * </ul>
     *
     * @param shiroRealm 由 Spring 自动注入前面定义的 shiroRealm Bean
     * @return 配置好的 SecurityManager 实例
     */
    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm); // 告诉 SecurityManager 使用我们自定义的 Realm
        return securityManager;
    }

    /**
     * 方式一：通过 ShiroFilterChainDefinition 定义 URL 访问规则
     *
     * <p>这是 Shiro 1.5+ 推荐的声明式配置方式，Shiro 自动配置会读取此 Bean。
     *
     * <p>路径匹配规则（按注册顺序匹配，命中即停止）：
     * <pre>
     *   /, /index.html, /assets/**  → anon（公共资源，所有人可访问）
     *   /api/**                      → anon（API 接口，当前开发阶段放行）
     * </pre>
     *
     * <p>注意：当前所有路径都是 anon，意味着 Shiro 认证功能暂未启用。
     * 生产环境应改为：chain.addPathDefinition("/api/**", "authc");
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        // 静态资源和首页放行
        chain.addPathDefinition("/", "anon");
        chain.addPathDefinition("/index.html", "anon");
        chain.addPathDefinition("/assets/**", "anon"); // ** 是 Ant 路径通配符，匹配多级目录
        // API 接口临时放行（开发阶段），正式上线改为 authc
        chain.addPathDefinition("/api/**", "anon");
        return chain;
    }

    /**
     * 方式二：通过 ShiroFilterFactoryBean 显式创建过滤器（优先级高于方式一）
     *
     * <p>使用 LinkedHashMap 是因为其保持插入顺序，Shiro 按顺序匹配 URL 规则。
     * 如果用普通 HashMap，顺序不确定，可能导致预期外的访问控制结果。
     *
     * <p>两种方式的区别：
     * <ul>
     *   <li>shiroFilterChainDefinition：声明式，简洁，适合简单场景</li>
     *   <li>shiroFilterFactoryBean：命令式，可以设置 loginUrl、successUrl、自定义 Filter 等</li>
     * </ul>
     * 此处两种方式同时存在，ShiroFilterFactoryBean 优先级更高，为实际生效的配置。
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager); // 注入安全管理器

        // 使用 LinkedHashMap 保证 URL 规则按添加顺序匹配（先匹配到的先生效）
        Map<String, String> filterChain = new LinkedHashMap<>();
        filterChain.put("/", "anon");                 // 根路径 → 首页
        filterChain.put("/index.html", "anon");       // 首页 HTML
        filterChain.put("/assets/**", "anon");        // 前端静态资源（JS、CSS 等）
        filterChain.put("/api/**", "anon");           // REST API 接口

        factoryBean.setFilterChainDefinitionMap(filterChain);
        return factoryBean;
    }
}
