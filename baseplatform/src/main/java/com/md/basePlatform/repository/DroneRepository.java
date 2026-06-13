package com.md.basePlatform.repository;

import com.md.basePlatform.domain.Drone;
import java.util.List;
import java.util.Optional;

/**
 * 无人机数据访问接口（Repository 层）
 *
 * <p>定义无人机持久化操作的方法契约，不依赖具体持久化技术（MyBatis / JDBC / JPA）。
 * 当前实现类为 {@link com.md.basePlatform.repository.impl.DroneRepositoryImpl}，
 * 内部委托给 MyBatis Mapper 执行 SQL。
 *
 * <p>ID 设计说明：
 * <ul>
 *   <li>无人机 ID 不是数据库自增主键，而是由后端在插入前计算：MAX(id) + 1</li>
 *   <li>删除无人机后调用 {@link #decrementIdsAfter(Long)} 将后续 ID 前移一位</li>
 *   <li>这样做的目的是保持 ID 的连续性和紧凑性（从 1 开始，无空缺）</li>
 * </ul>
 */
public interface DroneRepository {

    /**
     * 查询全部无人机
     *
     * @return 无人机列表（按 ID 升序）
     */
    List<Drone> findAll();

    /**
     * 根据主键查询单个无人机
     *
     * @param id 主键 ID
     * @return 包装在 Optional 中的无人机（不存在时 Optional.empty()）
     */
    Optional<Drone> findById(Long id);

    /**
     * 新增无人机
     * <p>注意：调用前需先设置 id（通过 findMaxId + 1 计算），数据库不会自动生成。
     *
     * @param drone 无人机实体（必须包含 id）
     * @return 影响行数（1 = 成功）
     */
    int insert(Drone drone);

    /**
     * 更新无人机
     * <p>根据 drone.id 更新对应记录的所有字段。
     *
     * @param drone 无人机实体（必须包含 id）
     * @return 影响行数（0 = 目标不存在，1 = 成功）
     */
    int update(Drone drone);

    /**
     * 删除无人机
     * <p>仅删除该行，不自动将后续 ID 前移（调用方需手动调用 decrementIdsAfter）。
     *
     * @param id 要删除的无人机 ID
     * @return 影响行数（0 = 目标不存在，1 = 成功）
     */
    int deleteById(Long id);

    /**
     * 查询当前表中最大的 ID
     * <p>用于插入前计算新 ID（新 ID = MAX(id) + 1）。
     *
     * @return 最大 ID，表为空时返回 0
     */
    Long findMaxId();

    /**
     * 将大于指定 ID 的记录的 ID 减 1
     * <p>在删除某条记录后调用，用于将后续记录的 ID 前移填补空缺。
     * 例如删除 id=3 → id=4 改为 3、id=5 改为 4…
     *
     * @param id 被删除记录的 ID
     * @return 受影响行数（被调整的记录数）
     */
    int decrementIdsAfter(Long id);
}
