-- ============================================================
-- SQLite 数据库 Schema（自动执行）
-- ============================================================
-- 此文件由 Spring Boot 在启动时自动执行
-- 配置项：spring.datasource.schema + initialization-mode: always
-- 对应 SQLite profile，创建 drone 表结构
-- SQLite 使用 AUTOINCREMENT 关键字（非 MySQL 的 AUTO_INCREMENT）
-- ============================================================

-- 创建无人机表（SQLite 语法）
-- TEXT 类型在 SQLite 中用于存储字符串（等价于 MySQL VARCHAR）
-- INTEGER 类型用于存储整数值
CREATE TABLE IF NOT EXISTS drone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    serial_number TEXT,
    model TEXT,
    battery_percent INTEGER,
    max_flight_minutes INTEGER,
    status TEXT
);
