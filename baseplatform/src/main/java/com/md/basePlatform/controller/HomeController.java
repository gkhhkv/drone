package com.md.basePlatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 *
 * <p>负责处理根路径("/")的请求，将其转发到静态首页 index.html。
 * 使用 @Controller（非 @RestController）是为了让 "forward:" 前缀正常工作（转发而非返回 JSON）。
 *
 * <p>当前端构建产物放入 static 目录后，访问 http://localhost:8080/ 即可直接看到 Vue SPA 页面。
 */
@Controller
public class HomeController {

    /**
     * 根路径转发到静态首页
     *
     * @return forward 转发指令（转发到 static/index.html）
     */
    @GetMapping("/")
    public String index() {
        // forward: 前缀表示服务器端转发，浏览器 URL 不变
        return "forward:/index.html";
    }

}
