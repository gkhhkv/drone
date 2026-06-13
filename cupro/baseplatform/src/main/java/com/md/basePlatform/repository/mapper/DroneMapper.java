package com.md.basePlatform.repository.mapper;

import com.md.basePlatform.domain.Drone;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 无人机 MyBatis Mapper 接口
 *
 * <p>MyBatis 3.5.x 的接口 + XML 映射模式：
 * <ul>
 *   <li>接口使用 @Mapper 注解标注，Spring 自动生成代理实现</li>
 *   <li>SQL 语句在 classpath:mapper/DroneMapper.xml 中定义</li>
 *   <li>方法名与 XML 中的 &lt;select&gt;/&lt;insert&gt;/&lt;update&gt;/&lt;delete&gt; id 对应</li>
 * </ul>
 *
 * <p>字段名映射：数据库下划线命名（serial_number）↔ Java 驼峰命名（serialNumber），
 * 由 application.yml 中的 {@code map-underscore-to-camel-case: true} 自动转换。
 *
 * @see resources/mapper/DroneMapper.xml
 */
@Mapper
public interface DroneMapper {

    /**
     * 查询全部无人机（按 ID 升序）
     *
     * @return 所有无人机列表
     */
    List<Drone> findAll();

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 无人机实体，不存在时返回 null
     */
    Drone findById(Long id);

    /**
     * 插入新记录
     *
     * @param drone 无人机实体（所有字段写入对应列）
     * @return 影响行数
     */
    int insert(Drone drone);

    /**
     * 按主键更新（全字段覆盖）
     *
     * @param drone 无人机实体
     * @return 影响行数
     */
    int update(Drone drone);

    /**
     * 按主键删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 获取当前最大 ID
     * <p>SQL: SELECT COALESCE(MAX(id), 0) FROM drone
     *
     * @return 最大 ID，空表返回 0
     */
    Long findMaxId();

    /**
     * 将大于指定 ID 的记录的 ID 减 1
     * <p>SQL: UPDATE drone SET id = id - 1 WHERE id > #{id} ORDER BY id ASC
     * <p>ORDER BY id ASC 确保按 ID 升序更新，避免主键冲突。
     *
     * @param id 被删除记录的 ID
     * @return 影响行数
     */
    int decrementIdsAfter(Long id);
}
