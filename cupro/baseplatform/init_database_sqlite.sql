-- ============================================================
-- 无人机管理平台 — 数据库初始化脚本（SQLite）
-- ============================================================
-- 用途：初始化 SQLite 数据库，创建 drone 表并插入示例数据
-- 使用方式：sqlite3 base-platform.db < init_database_sqlite.sql
-- SQLite 特点：
--   - 文件型数据库，无需安装数据库服务
--   - INTEGER PRIMARY KEY 即自增主键
--   - VARCHAR/TEXT 类型在 SQLite 中几乎等价
--   - 不支持 COMMENT 语法（列注释被省略）
-- ============================================================

-- 创建无人机表（SQLite 语法）
-- id: INTEGER PRIMARY KEY AUTOINCREMENT，自增主键
-- serial_number: 序列号
-- model: 型号
-- battery_percent: 电量百分比
-- max_flight_minutes: 最大飞行时长
-- status: 状态
-- created_at / updated_at: 时间戳
CREATE TABLE IF NOT EXISTS drone (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  serial_number VARCHAR(64) NOT NULL,
  model VARCHAR(64) NOT NULL,
  battery_percent INTEGER NOT NULL,
  max_flight_minutes INTEGER NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入 10 条示例 DJI 无人机数据
-- 状态说明：ACTIVE(活跃) / CHARGING(充电中) / MAINTENANCE(维护) / LOW_BATTERY(低电量) / INACTIVE(停用) / REPAIR(维修)
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
