-- ============================================================
-- 无人机管理平台 — 数据库初始化脚本（MySQL）
-- ============================================================
-- 用途：初始化 MySQL 数据库，创建 drone 表并插入示例数据
-- 使用方式：mysql -u root -p < init_database.sql
-- 注意：如果数据库已存在会被删除重建，请勿在生产环境直接运行
-- ============================================================

-- 创建数据库（使用 utf8mb4 字符集支持中文和 emoji）
CREATE DATABASE IF NOT EXISTS base_platform
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 切换到目标数据库
USE base_platform;

-- 创建无人机表
-- id: 主键，BIGINT 自增（兼容后端手动管理 ID 的模式）
-- serial_number: 无人机序列号，如 "DJI-001-2024"
-- model: 型号名称
-- battery_percent: 电池百分比 0-100
-- max_flight_minutes: 最大飞行时长（分钟）
-- status: 状态（ACTIVE/CHARGING/MAINTENANCE/LOW_BATTERY/INACTIVE/REPAIR）
-- created_at / updated_at: 自动维护的创建和更新时间
CREATE TABLE IF NOT EXISTS drone (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  serial_number VARCHAR(64) NOT NULL COMMENT '序列号',
  model VARCHAR(64) NOT NULL COMMENT '型号',
  battery_percent INT NOT NULL COMMENT '电池百分比',
  max_flight_minutes INT NOT NULL COMMENT '最大飞行分钟数',
  status VARCHAR(32) NOT NULL COMMENT '状态',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='无人机表';

-- 为常用查询字段添加索引以提升查询性能
CREATE INDEX idx_serial_number ON drone(serial_number);
CREATE INDEX idx_status ON drone(status);

-- 插入 10 条示例 DJI 无人机数据，用于开发测试
INSERT INTO drone (serial_number, model, battery_percent, max_flight_minutes, status) VALUES
('DJI-001-2024', 'DJI Mavic 3 Pro', 85, 45, 'ACTIVE'),
('DJI-002-2024', 'DJI Air 3', 92, 40, 'ACTIVE'),
('DJI-003-2024', 'DJI Mini 4 Pro', 78, 38, 'CHARGING'),
('DJI-004-2024', 'DJI Inspire 3', 65, 55, 'MAINTENANCE'),
('DJI-005-2024', 'DJI Avata', 95, 18, 'ACTIVE'),
('DJI-006-2024', 'DJI Phantom 4 Pro', 45, 30, 'LOW_BATTERY'),
('DJI-007-2024', 'DJI Matrice 300', 88, 60, 'ACTIVE'),
('DJI-008-2024', 'DJI Mavic 2 Pro', 72, 35, 'INACTIVE'),
('DJI-009-2024', 'DJI Mini 3 Pro', 90, 42, 'ACTIVE'),
('DJI-010-2024', 'DJI FPV', 60, 20, 'REPAIR');

-- 验证数据插入结果
SELECT '无人机表数据插入完成，共插入 ' || COUNT(*) || ' 条记录' AS message FROM drone;
