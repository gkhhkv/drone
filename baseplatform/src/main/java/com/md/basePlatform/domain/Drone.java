package com.md.basePlatform.domain;

/**
 * 无人机领域实体
 *
 * <p>对应数据库 drone 表的 Java 映射对象，字段使用驼峰命名。
 * MyBatis 通过配置 {@code map-underscore-to-camel-case: true} 自动完成下划线到驼峰的转换：
 * <ul>
 *   <li>数据库 serial_number → Java serialNumber</li>
 *   <li>数据库 battery_percent → Java batteryPercent</li>
 *   <li>数据库 max_flight_minutes → Java maxFlightMinutes</li>
 * </ul>
 *
 * <p>字段说明：
 * <ul>
 *   <li>id — 自增主键（删除后会重排以保持连续）</li>
 *   <li>serialNumber — 无人机序列号，如 "SN-1704067200000"</li>
 *   <li>model — 型号，如 "Mavic-Air"、"Inspire-2"</li>
 *   <li>batteryPercent — 电量百分比 0-100</li>
 *   <li>maxFlightMinutes — 最大飞行时长（分钟）</li>
 *   <li>status — 状态：IDLE（空闲）/ CHARGING（充电中）/ IN_MAINTENANCE（维护中）</li>
 * </ul>
 */
public class Drone {

    /** 主键 ID（新增时由后端计算：MAX(id)+1） */
    private Long id;

    /** 序列号 */
    private String serialNumber;

    /** 型号 */
    private String model;

    /** 电池百分比（0-100） */
    private Integer batteryPercent;

    /** 最大飞行时长（分钟） */
    private Integer maxFlightMinutes;

    /** 状态（IDLE/CHARGING/IN_MAINTENANCE） */
    private String status;

    // ---- Getters & Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(Integer batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public Integer getMaxFlightMinutes() {
        return maxFlightMinutes;
    }

    public void setMaxFlightMinutes(Integer maxFlightMinutes) {
        this.maxFlightMinutes = maxFlightMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
