# ************************************************************
# Sequel Pro SQL dump
# Version 5446
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 8.0.16)
# Database: pdf_tools
# Generation Time: 2019-07-06 14:51:33 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table pdf_data_coordinate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pdf_data_coordinate`;

CREATE TABLE `pdf_data_coordinate` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键ID',
  `page_no` int(11) NOT NULL COMMENT '页',
  `field_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段名',
  `llx` decimal(10,2) DEFAULT NULL COMMENT '左下X坐标',
  `lly` decimal(10,2) DEFAULT NULL COMMENT '左下Y坐标',
  `urx` decimal(10,2) DEFAULT NULL COMMENT '右上X坐标',
  `ury` decimal(10,2) DEFAULT NULL COMMENT '右上Y坐标',
  `margin_left` decimal(10,2) DEFAULT NULL COMMENT '左距离',
  `margin_top` decimal(10,2) DEFAULT NULL COMMENT '上距离',
  `width` decimal(10,2) DEFAULT NULL COMMENT '矩形宽度',
  `height` decimal(10,2) DEFAULT NULL COMMENT '矩形高度',
  `align` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对齐方式',
  `data_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据类型',
  `decimal_digits` int(11) DEFAULT NULL COMMENT '小数位数',
  `prefix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前缀',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '后缀',
  `action_type` tinyint(1) DEFAULT NULL COMMENT '动作类型（1：删除、2：替换）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `pdf_data_coordinate` WRITE;
/*!40000 ALTER TABLE `pdf_data_coordinate` DISABLE KEYS */;

INSERT INTO `pdf_data_coordinate` (`id`, `page_no`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`)
VALUES
	('01',1,'awb',NULL,NULL,NULL,NULL,91.12,214.95,67.13,11.05,'left','String',NULL,NULL,NULL,2),
	('02',1,'num',NULL,NULL,NULL,NULL,502.54,215.57,42.10,9.13,'right','Integer',NULL,NULL,NULL,2),
	('03',1,'weight',NULL,NULL,NULL,NULL,502.54,225.57,42.10,9.13,'right','Decimal',1,NULL,NULL,2),
	('04',1,'declareTotalAmountUsd',NULL,NULL,NULL,NULL,444.83,300.84,55.15,9.10,'right','Decimal',2,NULL,NULL,2),
	('05',1,'declareFreightAmountUsd',NULL,NULL,NULL,NULL,444.83,309.84,55.15,9.10,'right','Decimal',2,NULL,NULL,2),
	('06',1,'clearanceAmount',NULL,NULL,NULL,NULL,458.83,327.84,55.15,9.10,'right','Decimal',2,NULL,NULL,2),
	('07',1,'bprAmount',NULL,NULL,NULL,NULL,445.28,389.95,55.38,9.57,'right','Decimal',2,NULL,NULL,2),
	('08',1,'tariff',NULL,NULL,NULL,NULL,167.77,420.98,61.64,7.91,'right','Decimal',0,'\\',NULL,2),
	('09',1,'exciseTax',NULL,NULL,NULL,NULL,167.77,430.98,61.64,7.91,'right','Decimal',0,'\\',NULL,2),
	('10',1,'localExciseTax',NULL,NULL,NULL,NULL,167.77,439.98,61.64,7.91,'right','Decimal',0,'\\',NULL,2),
	('11',1,'taxTotalAmount',NULL,NULL,NULL,NULL,332.77,411.98,61.64,7.91,'right','Decimal',0,'\\',NULL,2),
	('12',1,'usdJpyExchangeRate',NULL,NULL,NULL,NULL,514.88,411.98,38.53,7.91,'right','Decimal',2,NULL,NULL,2),
	('13',1,'prod1DeclareAmountUsd',NULL,NULL,NULL,NULL,463.08,505.98,57.33,7.91,'right','Decimal',2,NULL,NULL,2),
	('14',1,'prod1TariffRate',NULL,NULL,NULL,NULL,110.12,497.17,19.89,7.91,'left','Decimal',0,NULL,'%',2),
	('15',1,'prod1DeclareAmountJpy',NULL,NULL,NULL,NULL,163.82,479.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('16',1,'prod1Tariff',NULL,NULL,NULL,NULL,163.82,506.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('17',1,'prod1CountryExciseTax',NULL,NULL,NULL,NULL,163.82,563.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('18',1,'prod1CountryExciseTaxAmount',NULL,NULL,NULL,NULL,163.82,590.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('19',1,'prod1LocalExciseTaxBase',NULL,NULL,NULL,NULL,163.82,629.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('20',1,'prod1LocalExciseTaxAmount',NULL,NULL,NULL,NULL,163.82,656.17,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('21',2,'prod2DeclareAmountUsd',NULL,NULL,NULL,NULL,460.27,142.07,60.46,7.91,'right','Decimal',2,NULL,NULL,2),
	('22',2,'prod2TariffRate',NULL,NULL,NULL,NULL,109.27,132.07,19.89,7.91,'left','Decimal',0,NULL,'%',2),
	('23',2,'prod2DeclareAmountJpy',NULL,NULL,NULL,NULL,163.29,114.51,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('24',2,'prod2Tariff',NULL,NULL,NULL,NULL,163.65,141.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('25',2,'prod2CountryExciseTax',NULL,NULL,NULL,NULL,163.65,207.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('26',2,'prod2CountryExciseTaxAmount',NULL,NULL,NULL,NULL,163.65,234.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('27',2,'prod2LocalExciseTaxBase',NULL,NULL,NULL,NULL,163.65,273.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('28',2,'prod2LocalExciseTaxAmount',NULL,NULL,NULL,NULL,163.65,300.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('29',2,'prod3DeclareAmountUsd',NULL,NULL,NULL,NULL,460.27,385.07,60.46,7.91,'right','Decimal',2,NULL,NULL,2),
	('30',2,'prod3TariffRate',NULL,NULL,NULL,NULL,109.27,375.07,19.89,7.91,'left','Decimal',0,NULL,'%',2),
	('31',2,'prod3DeclareAmountJpy',NULL,NULL,NULL,NULL,163.29,357.51,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('32',2,'prod3Tariff',NULL,NULL,NULL,NULL,163.65,384.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('33',2,'prod3CountryExciseTax',NULL,NULL,NULL,NULL,163.65,450.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('34',2,'prod3CountryExciseTaxAmount',NULL,NULL,NULL,NULL,163.65,477.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('35',2,'prod3LocalExciseTaxBase',NULL,NULL,NULL,NULL,163.65,516.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('36',2,'prod3LocalExciseTaxAmount',NULL,NULL,NULL,NULL,163.65,543.79,60.46,7.91,'right','Decimal',0,'\\',NULL,2),
	('37',1,NULL,NULL,NULL,NULL,NULL,98.31,85.60,438.50,109.47,NULL,NULL,NULL,NULL,NULL,1),
	('38',1,NULL,NULL,NULL,NULL,NULL,97.04,197.99,280.01,14.31,NULL,NULL,NULL,NULL,NULL,1),
	('39',1,NULL,NULL,NULL,NULL,NULL,435.04,197.99,48.68,14.31,NULL,NULL,NULL,NULL,NULL,1),
	('40',1,NULL,NULL,NULL,NULL,NULL,88.96,225.67,116.08,7.91,NULL,NULL,NULL,NULL,NULL,1),
	('41',1,NULL,NULL,NULL,NULL,NULL,88.96,261.67,116.08,7.91,NULL,NULL,NULL,NULL,NULL,1),
	('42',1,NULL,NULL,NULL,NULL,NULL,304.96,215.67,117.39,17.81,NULL,NULL,NULL,NULL,NULL,1),
	('43',1,NULL,NULL,NULL,NULL,NULL,479.59,225.58,67.76,7.91,NULL,NULL,NULL,NULL,NULL,1),
	('44',1,NULL,NULL,NULL,NULL,NULL,356.92,460.97,205.31,16.97,NULL,NULL,NULL,NULL,NULL,1),
	('45',2,NULL,NULL,NULL,NULL,NULL,356.92,96.26,205.31,16.97,NULL,NULL,NULL,NULL,NULL,1),
	('46',2,NULL,NULL,NULL,NULL,NULL,356.92,339.26,205.31,16.97,NULL,NULL,NULL,NULL,NULL,1);

/*!40000 ALTER TABLE `pdf_data_coordinate` ENABLE KEYS */;
UNLOCK TABLES;

DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `clear_coordinate_trigger` BEFORE UPDATE ON `pdf_data_coordinate` FOR EACH ROW begin
    /* 如果left，top，width，height有更新。则清除llx，lly，urx，ury */
    if (old.margin_left != new.margin_left or old.margin_top != new.margin_top or old.width != new.width or old.height != new.height) then
    	set new.llx = null;
    	set new.lly = null;
    	set new.urx = null;
    	set new.ury = null;
    end if;
end */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;


# Dump of table pdf_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pdf_file`;

CREATE TABLE `pdf_file` (
  `pdf_file_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pdf_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'PDF数据的ID',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件存储路径',
  `file_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件访问路径',
  PRIMARY KEY (`pdf_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



# Dump of table pdf_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pdf_list`;

CREATE TABLE `pdf_list` (
  `pdf_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类型（1：原始文件、2：更新文件）',
  `awb` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单号',
  `awb_replace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换单号',
  `num` int(11) DEFAULT NULL COMMENT '件数',
  `weight` decimal(12,2) DEFAULT NULL COMMENT '重量',
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
  `prod1_tariff_rate` decimal(12,4) DEFAULT NULL COMMENT '品名1关税率',
  `prod2_tariff_rate` decimal(12,4) DEFAULT NULL COMMENT '品名2关税率',
  `prod3_tariff_rate` decimal(12,4) DEFAULT NULL COMMENT '品名3关税率',
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
  `make_time` timestamp NULL DEFAULT NULL COMMENT '制作时间',
  `del_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0：未删除、1：已删除）',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`pdf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
