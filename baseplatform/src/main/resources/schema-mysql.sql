-- ============================================================
-- MySQL 数据库 Schema（自动执行）
-- ============================================================
-- 此文件由 Spring Boot 在启动时自动执行
-- 配置项：spring.datasource.schema + initialization-mode: always
-- 对应 MySQL profile，创建 drone 表结构
-- ============================================================

-- 创建无人机表（MySQL 语法）
-- 注意：MySQL profile 使用数据库自增 AUTO_INCREMENT 管理 ID
-- 但后端 Service 层会通过 findMaxId 手动计算 ID，两者配合使用
CREATE TABLE IF NOT EXISTS drone (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    serial_number VARCHAR(64),
    model VARCHAR(64),
    battery_percent INT,
    max_flight_minutes INT,
    status VARCHAR(32)
);
