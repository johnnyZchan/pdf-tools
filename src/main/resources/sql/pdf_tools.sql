-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.16 - MySQL Community Server - GPL
-- 服务器OS:                        Win64
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for pdf_tools
CREATE DATABASE IF NOT EXISTS `pdf_tools` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pdf_tools`;

-- Dumping structure for table pdf_tools.pdf_file
DROP TABLE IF EXISTS `pdf_file`;
CREATE TABLE IF NOT EXISTS `pdf_file` (
  `pdf_file_id` varchar(32) NOT NULL COMMENT '主键',
  `pdf_id` varchar(32) NOT NULL COMMENT 'PDF数据的ID',
  `file_name` varchar(50) NOT NULL COMMENT '文件名',
  `file_path` varchar(200) NOT NULL COMMENT '文件存储路径',
  `file_url` varchar(200) NOT NULL COMMENT '文件访问路径',
  PRIMARY KEY (`pdf_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table pdf_tools.pdf_list
DROP TABLE IF EXISTS `pdf_list`;
CREATE TABLE IF NOT EXISTS `pdf_list` (
  `pdf_id` varchar(32) NOT NULL COMMENT '主键ID',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类型（1：原始文件、2：更新文件）',
  `awb` varchar(50) DEFAULT NULL COMMENT '单号',
  `num` int(11) DEFAULT NULL COMMENT '件数',
  `weight` decimal(12,2) DEFAULT NULL COMMENT '重量',
  `declare_freight_price` decimal(12,2) DEFAULT NULL COMMENT '申报运费单价',
  `declare_total_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '总申报价值USD',
  `declare_freight_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '申报运费USD',
  `clearance_amount` decimal(12,2) DEFAULT NULL COMMENT '通关金额',
  `bpr_amount` decimal(12,2) DEFAULT NULL COMMENT 'BPR合计',
  `tariff` decimal(12,2) DEFAULT NULL COMMENT '关税',
  `excise_tax` decimal(12,2) DEFAULT NULL COMMENT '消费税',
  `local_excise_tax` decimal(12,2) DEFAULT NULL COMMENT '地方消费税',
  `tax_total_amount` decimal(12,2) DEFAULT NULL COMMENT '税金总计',
  `usd_jpy_exchange_rate` decimal(12,4) DEFAULT NULL COMMENT '美元日元汇率',
  `prod1_declare_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '品名1美金申报价值',
  `prod2_declare_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '品名2美金申报价值',
  `prod3_declare_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '品名3美金申报价值',
  `prod1_tariff_rate` decimal(12,2) DEFAULT NULL COMMENT '品名1关税率',
  `prod2_tariff_rate` decimal(12,2) DEFAULT NULL COMMENT '品名2关税率',
  `prod3_tariff_rate` decimal(12,2) DEFAULT NULL COMMENT '品名3关税率',
  `prod1_freight_pct` decimal(12,4) DEFAULT NULL COMMENT '品名1运费比重',
  `prod2_freight_pct` decimal(12,4) DEFAULT NULL COMMENT '品名2运费比重',
  `prod3_freight_pct` decimal(12,4) DEFAULT NULL COMMENT '品名3运费比重',
  `prod1_declare_amount_jpy` decimal(12,2) DEFAULT NULL COMMENT '品名1日元申报价值',
  `prod2_declare_amount_jpy` decimal(12,2) DEFAULT NULL COMMENT '品名2日元申报价值',
  `prod3_declare_amount_jpy` decimal(12,2) DEFAULT NULL COMMENT '品名3日元申报价值',
  `prod1_tariff_base` decimal(12,2) DEFAULT NULL COMMENT '品名1关税计算基数',
  `prod2_tariff_base` decimal(12,2) DEFAULT NULL COMMENT '品名2关税计算基数',
  `prod3_tariff_base` decimal(12,2) DEFAULT NULL COMMENT '品名3关税计算基数',
  `prod1_tariff` decimal(12,2) DEFAULT NULL COMMENT '品名1关税额',
  `prod2_tariff` decimal(12,2) DEFAULT NULL COMMENT '品名2关税额',
  `prod3_tariff` decimal(12,2) DEFAULT NULL COMMENT '品名3关税额',
  `prod1_tariff_rounding` decimal(12,2) DEFAULT NULL COMMENT '品名1关税取整',
  `prod2_tariff_rounding` decimal(12,2) DEFAULT NULL COMMENT '品名2关税取整',
  `prod3_tariff_rounding` decimal(12,2) DEFAULT NULL COMMENT '品名3关税取整',
  `prod1_country_excise_tax` decimal(12,2) DEFAULT NULL COMMENT '品名1国内消费税',
  `prod2_country_excise_tax` decimal(12,2) DEFAULT NULL COMMENT '品名2国内消费税',
  `prod3_country_excise_tax` decimal(12,2) DEFAULT NULL COMMENT '品名3国内消费税',
  `prod1_country_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名1国内消费税额基数',
  `prod2_country_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名2国内消费税额基数',
  `prod3_country_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名3国内消费税额基数',
  `prod1_country_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名1国内消费税金额',
  `prod2_country_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名2国内消费税金额',
  `prod3_country_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名3国内消费税金额',
  `prod1_local_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名1地方消费税基数',
  `prod2_local_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名2地方消费税基数',
  `prod3_local_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '品名3地方消费税基数',
  `prod1_local_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名1地方消费税金额',
  `prod2_local_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名2地方消费税金额',
  `prod3_local_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '品名3地方消费税金额',
  `tariff_total_amount` decimal(12,2) DEFAULT NULL COMMENT '关税合计',
  `country_excise_tax_total_amount` decimal(12,2) DEFAULT NULL COMMENT '国内消费税合计',
  `local_excise_tax_total_amount` decimal(12,2) DEFAULT NULL COMMENT '地方消费税合计',
  `make_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '制作状态（0：未制作、1：已制作）',
  `make_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '制作时间',
  `del_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0：未删除、1：已删除）',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`pdf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
