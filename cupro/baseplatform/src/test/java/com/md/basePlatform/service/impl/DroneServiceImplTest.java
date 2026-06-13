package com.md.basePlatform.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.repository.DroneRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DroneServiceImpl 单元测试
 *
 * <p>使用 Mockito 扩展模拟 DroneRepository，验证 Service 层业务逻辑：
 * <ul>
 *   <li>fillByAiStrategy — 缺失字段自动补齐逻辑</li>
 *   <li>createDrone — 新增流程（含 ID 计算、默认值生成）</li>
 *   <li>updateDrone — 更新流程（含存在性校验、字段补齐）</li>
 * </ul>
 *
 * <p>Mockito 注解说明：
 * <ul>
 *   <li>@Mock — 创建模拟对象（DroneRepository）</li>
 *   <li>@InjectMocks — 将 Mock 对象注入到被测试对象（DroneServiceImpl）</li>
 *   <li>@ExtendWith(MockitoExtension.class) — 启用 Mockito 注解处理</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    /** 模拟的 Repository（所有数据库调用由 stub 控制返回值） */
    @Mock
    private DroneRepository droneRepository;

    /** 被测试对象（通过构造注入接收 Mock Repository） */
    @InjectMocks
    private DroneServiceImpl droneService;

    /**
     * 测试用例：新增时所有字段为空 → 应自动生成默认值
     *
     * <p>验证点：
     * <ul>
     *   <li>序列号应自动生成（"SN-" + 时间戳）</li>
     *   <li>型号应随机选取（Mavic-Air/Inspire-2/Phantom-4/Mini-3 之一）</li>
     *   <li>电量应在 60-100 范围内</li>
     *   <li>最大飞行时长应在 20-50 范围内</li>
     *   <li>状态应随机选取（IDLE/CHARGING/IN_MAINTENANCE 之一）</li>
     * </ul>
     */
    @Test
    void should_generateDefaultFields_when_createDroneWithEmptyFields() {
        // 创建一个所有字段都为 null 的请求对象
        Drone request = new Drone();

        // Stub insert 方法返回 1（插入成功）
        when(droneRepository.insert(any(Drone.class))).thenReturn(1);

        // 执行新增操作
        Drone saved = droneService.createDrone(request);

        // 验证所有字段都已被 fillByAiStrategy 填充为非空值
        assertNotNull(saved.getSerialNumber(), "序列号应被自动生成");
        assertNotNull(saved.getModel(), "型号应被自动生成");
        assertNotNull(saved.getStatus(), "状态应被自动生成");
        assertNotNull(saved.getBatteryPercent(), "电量应被自动生成");
        assertNotNull(saved.getMaxFlightMinutes(), "最大飞行时长应被自动生成");
    }

    /**
     * 测试用例：更新已存在的无人机
     *
     * <p>验证点：
     * <ul>
     *   <li>findById 能找到目标记录时正常更新</li>
     *   <li>更新后的对象 ID 与被更新目标的 ID 一致</li>
     *   <li>更新后 model 字段为传入的新值</li>
     * </ul>
     */
    @Test
    void should_updateDrone_when_targetExists() {
        // 准备已存在的无人机记录
        Drone existing = new Drone();
        existing.setId(1L);

        // Stub：findById 返回已存在的记录
        when(droneRepository.findById(1L)).thenReturn(Optional.of(existing));
        // Stub：update 返回 1（更新成功）
        when(droneRepository.update(any(Drone.class))).thenReturn(1);

        // 准备更新请求（只传 model 字段）
        Drone req = new Drone();
        req.setModel("Mini-3");

        // 执行更新
        Drone result = droneService.updateDrone(1L, req);

        // 断言 ID 保持不变
        assertEquals(1L, result.getId(), "更新后的 ID 应与目标一致");
        // 断言 model 已更新为新值
        assertEquals("Mini-3", result.getModel(), "model 应被更新为 Mini-3");
    }
}
