package com.md.basePlatform.repository.impl;

import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.repository.DroneRepository;
import com.md.basePlatform.repository.mapper.DroneMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 无人机仓储实现类
 *
 * <p>实现 {@link DroneRepository} 接口，作为 MyBatis Mapper 的薄封装层。
 * 所有持久化操作委托给 {@link DroneMapper} 执行。
 *
 * <p>设计意图：Repository 层隔离了 Service 层对具体持久化框架（MyBatis）的直接依赖，
 * 如果将来需要切换 ORM 框架（如 JPA / JDBC Template），只需修改本类即可。
 *
 * <p>findById 使用 Optional 包装返回值，避免 Service 层直接面对 null。
 */
@Repository
public class DroneRepositoryImpl implements DroneRepository {

    /** MyBatis Mapper 接口（Spring 自动注入代理实现） */
    private final DroneMapper droneMapper;

    /**
     * 构造注入 MyBatis Mapper
     *
     * @param droneMapper MyBatis 生成的 Mapper 代理 Bean
     */
    public DroneRepositoryImpl(DroneMapper droneMapper) {
        this.droneMapper = droneMapper;
    }

    @Override
    public List<Drone> findAll() {
        return droneMapper.findAll();
    }

    @Override
    public Optional<Drone> findById(Long id) {
        // MyBatis Mapper 返回 null 时转为 Optional.empty()
        return Optional.ofNullable(droneMapper.findById(id));
    }

    @Override
    public int insert(Drone drone) {
        return droneMapper.insert(drone);
    }

    @Override
    public int update(Drone drone) {
        return droneMapper.update(drone);
    }

    @Override
    public int deleteById(Long id) {
        return droneMapper.deleteById(id);
    }

    @Override
    public Long findMaxId() {
        return droneMapper.findMaxId();
    }

    @Override
    public int decrementIdsAfter(Long id) {
        return droneMapper.decrementIdsAfter(id);
    }
}
