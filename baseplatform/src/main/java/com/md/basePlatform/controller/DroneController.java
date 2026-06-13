package com.md.basePlatform.controller;

import com.md.basePlatform.common.ApiResponse;
import com.md.basePlatform.controller.request.DroneUpsertRequest;
import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.service.DroneService;
import javax.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无人机管理 REST 控制器
 *
 * <p>提供无人机 CRUD 的全部 REST API 端点，所有接口路径前缀为 /api/drones：
 * <ul>
 *   <li>GET    /api/drones       — 查询无人机列表</li>
 *   <li>GET    /api/drones/{id}  — 查询单个无人机</li>
 *   <li>POST   /api/drones       — 新增无人机</li>
 *   <li>PUT    /api/drones/{id}  — 更新无人机</li>
 *   <li>DELETE /api/drones/{id}  — 删除无人机</li>
 * </ul>
 *
 * <p>所有接口统一返回 {@link ApiResponse} 包装的 JSON 格式。
 * 参数校验使用 Jakarta Bean Validation（@Valid），校验失败由 GlobalExceptionHandler 统一处理。
 *
 * <p>设计要点：
 * <ul>
 *   <li>Controller 层不包含业务逻辑，仅做参数转换和路由</li>
 *   <li>请求体使用 DroneUpsertRequest（独立的请求 DTO）而非 Drone 实体</li>
 *   <li>通过构造注入 DroneService（Spring 推荐的 DI 方式）</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService droneService;

    /**
     * 构造注入无人机服务
     *
     * @param droneService 无人机业务服务
     */
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    /**
     * 查询全部无人机列表
     *
     * @return ApiResponse 包装的无人机列表
     */
    @GetMapping
    public ApiResponse<List<Drone>> listDrones() {
        return ApiResponse.ok(droneService.listDrones());
    }

    /**
     * 根据 ID 查询单个无人机
     *
     * @param id 无人机主键 ID（路径参数）
     * @return ApiResponse 包装的无人机对象（不存在时抛出 BusinessException）
     */
    @GetMapping("/{id}")
    public ApiResponse<Drone> getDrone(@PathVariable Long id) {
        return ApiResponse.ok(droneService.getDrone(id));
    }

    /**
     * 新增无人机
     * <p>请求体中的字段均为可选，未传的字段由后端 DroneServiceImpl.fillByAiStrategy() 自动生成默认值。
     *
     * @param request 新增无人机请求体（使用 @Valid 自动触发 Jakarta Bean Validation 校验）
     * @return ApiResponse 包装的创建后的无人机对象（含自动生成的 ID）
     */
    @PostMapping  //接收前端的 HTTP POST 请求
    public ApiResponse<Drone> createDrone(@Valid @RequestBody DroneUpsertRequest request) {
        // 将请求 DTO 转换为领域实体后交给 Service 层处理
        return ApiResponse.ok(droneService.createDrone(toDomain(request)));
    }

    /**
     * 更新无人机
     * <p>只更新请求体中传入的字段，未传字段由后端补齐默认值。
     *
     * @param id      要更新的无人机 ID（路径参数）
     * @param request 更新内容请求体
     * @return ApiResponse 包装的更新后的无人机对象
     */
    @PutMapping("/{id}")
    public ApiResponse<Drone> updateDrone(@PathVariable Long id, @Valid @RequestBody DroneUpsertRequest request) {
        return ApiResponse.ok(droneService.updateDrone(id, toDomain(request)));
    }

    /**
     * 删除无人机
     * <p>删除成功后，后端会自动调用 decrementIdsAfter 将后续记录的 ID 前移一位以保持 ID 连续性。
     * 这是一个非标准设计，目的是让 ID 始终从 1 开始连续排列。
     *
     * @param id 要删除的无人机 ID（路径参数）
     * @return ApiResponse（data 为 null，仅返回 success 和 message）
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDrone(@PathVariable Long id) {
        droneService.deleteDrone(id);
        return ApiResponse.ok(null); // 删除成功返回空数据体
    }

    /**
     * 请求 DTO → 领域实体 转换器
     * <p>将前端传来的 DroneUpsertRequest 对象转换为 Drone 领域实体。
     * 此方法为 Controller 层内部使用，不对外暴露。
     *
     * @param request 前端请求体
     * @return 转换后的 Drone 实体对象
     */
    private Drone toDomain(DroneUpsertRequest request) {
        Drone drone = new Drone();
        drone.setSerialNumber(request.getSerialNumber());
        drone.setModel(request.getModel());
        drone.setBatteryPercent(request.getBatteryPercent());
        drone.setMaxFlightMinutes(request.getMaxFlightMinutes());
        drone.setStatus(request.getStatus());
        return drone;
    }
}
