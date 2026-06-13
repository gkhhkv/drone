package com.md.basePlatform.service.impl;

import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.exception.BusinessException;
import com.md.basePlatform.repository.DroneRepository;
import com.md.basePlatform.service.DroneService;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 无人机业务服务实现类
 *
 * <p>实现所有无人机业务逻辑，包括：
 * <ul>
 *   <li>基本 CRUD 操作</li>
 *   <li>新增/更新时自动补齐缺失字段（fillByAiStrategy）</li>
 *   <li>删除后的 ID 重排（decrementIdsAfter）</li>
 * </ul>
 *
 * <p>"AI 策略" 名称由来：最初设计是调用 AI 接口根据无人机型号等信息智能生成默认参数，
 * 当前简化为随机选取预设值，保留了方法命名以便将来接入真实 AI 服务。
 */
@Service
public class DroneServiceImpl implements DroneService {

    /** 默认型号列表（新增时未指定型号则从中随机选取） */
    private static final String[] DEFAULT_MODELS = {"Mavic-Air", "Inspire-2", "Phantom-4", "Mini-3"};

    /** 默认状态列表 */
    private static final String[] DEFAULT_STATUS = {"IDLE", "CHARGING", "IN_MAINTENANCE"};

    /** 随机数生成器（用于生成默认值） */
    private static final Random RANDOM = new Random();

    private final DroneRepository droneRepository;

    /**
     * 构造注入 Repository
     *
     * @param droneRepository 无人机仓储
     */
    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public List<Drone> listDrones() {
        return droneRepository.findAll();
    }

    @Override
    public Drone getDrone(Long id) {
        // findById 返回 Optional，不存在时抛出业务异常
        return droneRepository.findById(id)
                .orElseThrow(() -> new BusinessException("无人机不存在，id=" + id));
    }

    @Override
    public Drone createDrone(Drone drone) {
        // 1. 补齐缺失字段（序列号、型号、电量、飞行时长、状态）
        fillByAiStrategy(drone);

        // 2. 计算新 ID：当前最大 ID + 1
        drone.setId(droneRepository.findMaxId() + 1);

        // 3. 插入数据库
        droneRepository.insert(drone);
        return drone;
    }

    @Override
    public Drone updateDrone(Long id, Drone drone) {
        // 1. 校验目标是否存在（不存在则抛异常）
        getDrone(id);

        // 2. 设置 ID 并补齐缺失字段
        drone.setId(id);
        fillByAiStrategy(drone);

        // 3. 执行更新
        int rows = droneRepository.update(drone);
        if (rows == 0) {
            throw new BusinessException("更新失败，id=" + id);
        }
        return drone;
    }

    @Override
    @Transactional // 删除 + ID 重排必须在同一事务中完成
    public void deleteDrone(Long id) {
        // 1. 执行删除
        int rows = droneRepository.deleteById(id);
        if (rows == 0) {
            throw new BusinessException("删除失败，id=" + id);
        }

        // 2. 将后续记录的 ID 减 1 以保持连续性
        //    例如删除 id=3 → id=4 变成 3, id=5 变成 4…
        droneRepository.decrementIdsAfter(id);
    }

    /**
     * 使用简化 "AI 策略" 自动生成缺失属性
     *
     * <p>对于 null 或空字符串的字段，自动生成合理的默认值：
     * <ul>
     *   <li>序列号 → "SN-{当前时间毫秒}"，保证唯一性</li>
     *   <li>型号 → 从预设列表中随机选取</li>
     *   <li>电量 → 随机 60-100 之间的值</li>
     *   <li>最大飞行时长 → 随机 20-50 分钟</li>
     *   <li>状态 → 从预设状态列表中随机选取</li>
     * </ul>
     *
     * <p>设计意图：如果前端没有传某些字段，系统不会报错，而是智能地补充默认值，
     * 提升用户体验（减少必填字段）同时保证数据完整性。
     *
     * @param drone 无人机对象（原地修改，返回值通过参数传出）
     */
    private void fillByAiStrategy(Drone drone) {
        // 序列号：使用时间戳保证唯一性
        if (drone.getSerialNumber() == null || drone.getSerialNumber().trim().isEmpty()) {
            drone.setSerialNumber("SN-" + System.currentTimeMillis());
        }

        // 型号：从 4 种默认型号中随机选取
        if (drone.getModel() == null || drone.getModel().trim().isEmpty()) {
            drone.setModel(DEFAULT_MODELS[RANDOM.nextInt(DEFAULT_MODELS.length)]);
        }

        // 电量：随机 60-100（RANDOM.nextInt(41) 返回 0-40）
        if (drone.getBatteryPercent() == null) {
            drone.setBatteryPercent(60 + RANDOM.nextInt(41));
        }

        // 最大飞行时长：随机 20-50 分钟
        if (drone.getMaxFlightMinutes() == null) {
            drone.setMaxFlightMinutes(20 + RANDOM.nextInt(31));
        }

        // 状态：从 3 种默认状态中随机选取
        if (drone.getStatus() == null || drone.getStatus().trim().isEmpty()) {
            drone.setStatus(DEFAULT_STATUS[RANDOM.nextInt(DEFAULT_STATUS.length)]);
        }
    }
}
