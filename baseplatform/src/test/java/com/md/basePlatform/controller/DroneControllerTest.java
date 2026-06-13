package com.md.basePlatform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.exception.GlobalExceptionHandler;
import com.md.basePlatform.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

/**
 * DroneController 单元测试
 *
 * <p>使用 MockMvc Standalone 模式测试 Controller 层，不加载完整 Spring 上下文。
 * 通过 Mockito 模拟 DroneService，验证 Controller 的：
 * <ul>
 *   <li>URL 路由映射是否正确</li>
 *   <li>请求和响应的序列化/反序列化</li>
 *   <li>参数校验和异常处理</li>
 * </ul>
 *
 * <p>测试覆盖：
 * <ul>
 *   <li>should_returnCreatedDrone_when_createDrone — 正常新增无人机</li>
 * </ul>
 */
class DroneControllerTest {

    /** MockMvc 实例，用于模拟 HTTP 请求 */
    private MockMvc mockMvc;

    /** Mock 的 DroneService（所有方法调用需 stub） */
    private DroneService droneService;

    /**
     * 每个测试方法执行前的初始化
     * <p>使用 Standalone 模式创建 MockMvc：
     * <ul>
     *   <li>手动创建 Controller 实例（注入 Mock Service）</li>
     *   <li>手动注册 GlobalExceptionHandler（使异常处理逻辑也参与测试）</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        // 创建模拟的 Service
        droneService = Mockito.mock(DroneService.class);

        // 使用 Standalone 模式：只加载 Controller 层，不加载 Spring 上下文
        mockMvc = MockMvcBuilders.standaloneSetup(new DroneController(droneService))
                .setControllerAdvice(new GlobalExceptionHandler()) // 注册全局异常处理器
                .build();
    }

    /**
     * 测试用例：新增无人机 — 验证正常创建流程
     *
     * <p>模拟场景：
     * <ol>
     *   <li>前端发送 POST /api/drones 带 JSON body {"model":"Mavic-Air"}</li>
     *   <li>Mock Service 返回一个 id=1, model=Mavic-Air 的 Drone 对象</li>
     *   <li>断言响应 HTTP 200, success=true, data.id=1, data.model=Mavic-Air</li>
     * </ol>
     */
    @Test
    void should_returnCreatedDrone_when_createDrone() throws Exception {
        // 准备 Mock 数据：Service 返回一个已设置 ID 的无人机
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setModel("Mavic-Air");

        // Stub：当 createDrone 被调用时返回预设的 drone 对象
        when(droneService.createDrone(any(Drone.class))).thenReturn(drone);

        // 发送 POST 请求并验证响应
        MediaType jsonContentType = MediaType.APPLICATION_JSON;
        mockMvc.perform(post("/api/drones")
                        .contentType(jsonContentType)
                        .content("{\"model\":\"Mavic-Air\"}")) // 模拟前端 JSON 请求体
                // 断言 HTTP 状态码 200
                .andExpect(status().isOk())
                // 断言响应 JSON 中 success 字段为 true
                .andExpect(jsonPath("$.success").value(true))
                // 断言 data.id = 1
                .andExpect(jsonPath("$.data.id").value(1L))
                // 断言 data.model = "Mavic-Air"
                .andExpect(jsonPath("$.data.model").value("Mavic-Air"));
    }
}
