# 数据库初始化说明

## 文件说明

1. `init_database.sql` - MySQL数据库初始化脚本
2. `init_database_sqlite.sql` - SQLite数据库初始化脚本
3. `base-platform.db` - 项目使用的SQLite数据库文件（已存在）

## 表结构

### drone表（无人机表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT/INTEGER | 主键，自增 |
| serial_number | VARCHAR(64) | 序列号 |
| model | VARCHAR(64) | 型号 |
| battery_percent | INT/INTEGER | 电池百分比 |
| max_flight_minutes | INT/INTEGER | 最大飞行分钟数 |
| status | VARCHAR(32) | 状态 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

## 状态枚举值

- `ACTIVE` - 活跃/可用
- `CHARGING` - 充电中
- `MAINTENANCE` - 维护中
- `LOW_BATTERY` - 低电量
- `INACTIVE` - 未激活
- `REPAIR` - 维修中

## 示例数据说明

脚本中插入了10条示例无人机数据，包括：
- 不同型号的DJI无人机
- 不同的电池状态（85%-95%）
- 不同的飞行时间（18-60分钟）
- 各种状态（ACTIVE, CHARGING, MAINTENANCE等）

## 使用方法

### MySQL
1. 确保MySQL服务运行
2. 使用root用户登录MySQL
3. 执行：`mysql -u root -p < init_database.sql`

### SQLite
1. 确保SQLite已安装
2. 执行：`sqlite3 base-platform.db < init_database_sqlite.sql`

## 项目配置

当前项目配置使用SQLite数据库，数据库文件为`base-platform.db`。如需切换为MySQL，请修改`src/main/resources/application.yml`中的数据库配置。