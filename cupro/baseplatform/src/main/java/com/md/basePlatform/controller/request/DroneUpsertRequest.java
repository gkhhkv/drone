package com.md.basePlatform.controller.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 无人机新增/修改请求参数 DTO
 *
 * <p>专门用于接收前端 POST/PUT 请求的请求体，与 {@code Drone} 实体类分离。
 * 所有字段均为可选（前端可以只传部分字段，后端自动补齐默认值）。
 *
 * <p>校验注解：
 * <ul>
 *   <li>@Size — 字符串长度校验</li>
 *   <li>@Min / @Max — 数值范围校验</li>
 * </ul>
 * 校验失败时由 GlobalExceptionHandler 统一捕获并返回友好错误信息。
 */
public class DroneUpsertRequest {

    /** 序列号，最长 64 字符，留空由后端自动生成 "SN-{timestamp}" */
    @Size(max = 64, message = "长度不能超过64")
    private String serialNumber;

    /** 型号，最长 64 字符，留空由后端随机选取默认型号 */
    @Size(max = 64, message = "长度不能超过64")
    private String model;

    /** 电池百分比（0-100），留空由后端随机生成 60-100 之间的值 */
    @Min(value = 0, message = "不能小于0")
    @Max(value = 100, message = "不能大于100")
    private Integer batteryPercent;

    /** 最大飞行时长（分钟），1-300，留空由后端随机生成 20-50 之间的值 */
    @Min(value = 1, message = "不能小于1")
    @Max(value = 300, message = "不能大于300")
    private Integer maxFlightMinutes;

    /** 状态，最长 32 字符，可选值：IDLE/CHARGING/IN_MAINTENANCE，留空随机 */
    @Size(max = 32, message = "长度不能超过32")
    private String status;

    // ---- 以下为 Getters & Setters ----

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
