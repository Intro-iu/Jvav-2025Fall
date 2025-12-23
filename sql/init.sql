-- Safely initialize tables without dropping the database itself
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS news_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE news_db;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS t_news;
DROP TABLE IF EXISTS t_category;
DROP TABLE IF EXISTS t_user;
SET FOREIGN_KEY_CHECKS = 1;

-- Users table
CREATE TABLE t_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    nickname VARCHAR(50)
) DEFAULT CHARSET=utf8mb4;

-- Categories table
CREATE TABLE t_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
) DEFAULT CHARSET=utf8mb4;

-- News table
CREATE TABLE t_news (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    category_id INT,
    author_id INT,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES t_category(id),
    FOREIGN KEY (author_id) REFERENCES t_user(id)
) DEFAULT CHARSET=utf8mb4;

-- 1. Insert Users
INSERT INTO t_user (username, password, nickname) VALUES 
('admin', 'admin123', 'Admin'),
('editor', 'editor123', 'Editor John');

-- 2. Insert Categories
INSERT INTO t_category (name) VALUES 
('科技'), ('体育'), ('政治'), ('娱乐'), ('健康');

-- 3. Insert Mock News (35 records)
INSERT INTO t_news (title, content, category_id, author_id, publish_time) VALUES
('AI技术突破新高度', '随着深度学习的发展...', 1, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('奥运会筹备工作进展顺利', '各场馆建设已完成...', 2, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('新医改政策解读', '为了提高全民健康水平...', 3, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
('某知名歌手发布新专辑', '首日销量突破...', 4, 2, DATE_SUB(NOW(), INTERVAL 4 DAY)),
('春季流感预防指南', '医生建议...', 5, 2, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('Java 21 发布特性预览', '虚拟线程正式稳定...', 1, 1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
('世界杯预选赛战况', '国家队惜败...', 2, 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),
('全球气候峰会召开', '各国达成新共识...', 3, 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
('春节档电影票房预测', '多部大片云集...', 4, 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
('每日运动30分钟的好处', '研究表明...', 5, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
('量子计算机新进展', '错误率大幅降低...', 1, 1, DATE_SUB(NOW(), INTERVAL 11 DAY)),
('NBA总决赛前瞻', '两队实力分析...', 2, 1, DATE_SUB(NOW(), INTERVAL 12 DAY)),
('地方选举结果出炉', '执政党获胜...', 3, 1, DATE_SUB(NOW(), INTERVAL 13 DAY)),
('某明星塌房事件始末', '吃瓜群众...', 4, 1, DATE_SUB(NOW(), INTERVAL 14 DAY)),
('健康饮食金字塔', '如何均衡膳食...', 5, 1, DATE_SUB(NOW(), INTERVAL 15 DAY)),
('SpaceX发射成功', '回收火箭...', 1, 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
('马拉松比赛报名开启', '数万人参与...', 2, 1, DATE_SUB(NOW(), INTERVAL 17 DAY)),
('联合国发布人权报告', '关注...', 3, 1, DATE_SUB(NOW(), INTERVAL 18 DAY)),
('知名导演新片开机', '期待...', 4, 1, DATE_SUB(NOW(), INTERVAL 19 DAY)),
('心理健康的重要性', '缓解压力...', 5, 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),
('Web3.0的未来', '去中心化...', 1, 1, DATE_SUB(NOW(), INTERVAL 21 DAY)),
('欧冠淘汰赛抽签', '豪门对决...', 2, 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
('城市规划新方案', '建设智慧城市...', 3, 1, DATE_SUB(NOW(), INTERVAL 23 DAY)),
('音乐节阵容公布', '摇滚...', 4, 1, DATE_SUB(NOW(), INTERVAL 24 DAY)),
('如何改善睡眠质量', '专家建议...', 5, 1, DATE_SUB(NOW(), INTERVAL 25 DAY)),
('新能源汽车销量大涨', '电池技术...', 1, 1, DATE_SUB(NOW(), INTERVAL 26 DAY)),
('网球大满贯决赛', '三盘大战...', 2, 1, DATE_SUB(NOW(), INTERVAL 27 DAY)),
('税收政策调整', '惠及民生...', 3, 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
('游戏展会现场直击', '3A大作...', 4, 1, DATE_SUB(NOW(), INTERVAL 29 DAY)),
('定期体检的必要性', '早发现早治疗...', 5, 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
('5G网络全面覆盖', '速度提升...', 1, 1, DATE_SUB(NOW(), INTERVAL 31 DAY)),
('F1赛车阿布扎比站', '绝杀...', 2, 1, DATE_SUB(NOW(), INTERVAL 32 DAY)),
('外交部例行记者会', '回应...', 3, 1, DATE_SUB(NOW(), INTERVAL 33 DAY)),
('畅销书排行榜更新', '悬疑...', 4, 1, DATE_SUB(NOW(), INTERVAL 34 DAY)),
('瑜伽入门教程', '身心合一...', 5, 1, DATE_SUB(NOW(), INTERVAL 35 DAY));
