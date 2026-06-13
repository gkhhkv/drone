package com.md.basePlatform.service;

import com.md.basePlatform.domain.Drone;
import java.util.List;

/**
 * 无人机业务服务接口
 *
 * <p>定义无人机业务操作的方法契约，实现类为 {@link com.md.basePlatform.service.impl.DroneServiceImpl}。
 *
 * <p>主要业务规则：
 * <ul>
 *   <li>新增和更新时，未填写的字段由后端自动补齐默认值（fillByAiStrategy）</li>
 *   <li>ID 由后端计算（MAX(id)+1），不使用数据库自增</li>
 *   <li>删除无人机后自动调整后续 ID 以保持连续</li>
 * </ul>
 */
public interface DroneService {

    /**
     * 查询全部无人机列表
     *
     * @return 所有无人机（按 ID 升序）
     */
    List<Drone> listDrones();

    /**
     * 根据 ID 查询单个无人机
     *
     * @param id 无人机主键
     * @return 无人机实体
     * @throws com.md.basePlatform.exception.BusinessException 当 ID 不存在时
     */
    Drone getDrone(Long id);

    /**
     * 新增无人机
     * <p>自动为 null/空字符串的字段生成默认值（调用 fillByAiStrategy）。
     *
     * @param drone 前端传入的无人机数据（字段可部分为空）
     * @return 新增后的无人机（含自动生成的 ID 和默认字段值）
     */
    Drone createDrone(Drone drone);

    /**
     * 更新无人机
     * <p>先校验目标 ID 是否存在，然后为缺失字段补齐默认值，最后执行更新。
     *
     * @param id    要更新的无人机 ID
     * @param drone 前端传入的更新数据（字段可部分为空）
     * @return 更新后的无人机对象
     * @throws com.md.basePlatform.exception.BusinessException 当 ID 不存在或更新失败时
     */
    Drone updateDrone(Long id, Drone drone);

    /**
     * 删除无人机
     * <p>删除后调用 decrementIdsAfter 将后续记录的 ID 前移一位，保持 ID 连续。
     *
     * @param id 要删除的无人机 ID
     * @throws com.md.basePlatform.exception.BusinessException 当 ID 不存在时
     */
    void deleteDrone(Long id);
}
