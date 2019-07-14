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
DROP DATABASE IF EXISTS `pdf_tools`;
CREATE DATABASE IF NOT EXISTS `pdf_tools` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pdf_tools`;

-- Dumping structure for table pdf_tools.pdf_data_coordinate
DROP TABLE IF EXISTS `pdf_data_coordinate`;
CREATE TABLE IF NOT EXISTS `pdf_data_coordinate` (
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

-- Dumping data for table pdf_tools.pdf_data_coordinate: ~16 rows (大约)
DELETE FROM `pdf_data_coordinate`;
/*!40000 ALTER TABLE `pdf_data_coordinate` DISABLE KEYS */;
INSERT INTO `pdf_data_coordinate` (`id`, `page_no`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`) VALUES
	('01', 1, 'awb', 91.12, 616.00, 158.25, 627.05, 91.12, 214.95, 67.13, 11.05, 'left', 'String', NULL, NULL, NULL, 2),
	('02', 1, 'num', 502.54, 617.30, 544.64, 626.43, 502.54, 215.57, 42.10, 9.13, 'right', 'Integer', NULL, NULL, NULL, 2),
	('03', 1, 'weight', 502.54, 607.30, 544.64, 616.43, 502.54, 225.57, 42.10, 9.13, 'right', 'Decimal', 1, NULL, NULL, 2),
	('04', 1, 'declareTotalAmountUsd', 444.83, 532.06, 499.98, 541.16, 444.83, 300.84, 55.15, 9.10, 'right', 'Decimal', 2, NULL, NULL, 2),
	('05', 1, 'declareFreightAmountUsd', 444.83, 523.06, 499.98, 532.16, 444.83, 309.84, 55.15, 9.10, 'right', 'Decimal', 2, NULL, NULL, 2),
	('06', 1, 'clearanceAmount', 458.83, 505.06, 513.98, 514.16, 458.83, 327.84, 55.15, 9.10, 'right', 'Decimal', 2, NULL, NULL, 2),
	('07', 1, 'bprAmount', 445.28, 442.48, 500.66, 452.05, 445.28, 389.95, 55.38, 9.57, 'right', 'Decimal', 2, NULL, NULL, 2),
	('08', 1, 'tariff', 167.77, 413.11, 229.41, 421.02, 167.77, 420.98, 61.64, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('09', 1, 'exciseTax', 167.77, 403.11, 229.41, 411.02, 167.77, 430.98, 61.64, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('10', 1, 'localExciseTax', 167.77, 394.11, 229.41, 402.02, 167.77, 439.98, 61.64, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('11', 1, 'taxTotalAmount', 332.77, 422.11, 394.41, 430.02, 332.77, 411.98, 61.64, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('12', 1, 'usdJpyExchangeRate', 514.88, 422.11, 553.41, 430.02, 514.88, 411.98, 38.53, 7.91, 'right', 'Decimal', 2, NULL, NULL, 2),
	('13', 1, 'prod1DeclareAmountUsd', 463.08, 328.11, 520.41, 336.02, 463.08, 505.98, 57.33, 7.91, 'right', 'Decimal', 2, NULL, NULL, 2),
	('14', 1, 'prod1TariffRate', 110.12, 336.92, 130.01, 344.83, 110.12, 497.17, 19.89, 7.91, 'left', 'Decimal', 0, NULL, '%', 2),
	('15', 1, 'prod1DeclareAmountJpy', 163.82, 354.92, 224.28, 362.83, 163.82, 479.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('16', 1, 'prod1Tariff', 163.82, 327.92, 224.28, 335.83, 163.82, 506.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('17', 1, 'prod1CountryExciseTax', 163.82, 270.92, 224.28, 278.83, 163.82, 563.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('18', 1, 'prod1CountryExciseTaxAmount', 163.82, 243.92, 224.28, 251.83, 163.82, 590.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('19', 1, 'prod1LocalExciseTaxBase', 163.82, 204.92, 224.28, 212.83, 163.82, 629.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('20', 1, 'prod1LocalExciseTaxAmount', 163.82, 177.92, 224.28, 185.83, 163.82, 656.17, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('21', 2, 'prod2DeclareAmountUsd', 460.27, 692.02, 520.73, 699.93, 460.27, 142.07, 60.46, 7.91, 'right', 'Decimal', 2, NULL, NULL, 2),
	('22', 2, 'prod2TariffRate', 109.27, 702.02, 129.16, 709.93, 109.27, 132.07, 19.89, 7.91, 'left', 'Decimal', 0, NULL, '%', 2),
	('23', 2, 'prod2DeclareAmountJpy', 163.29, 719.58, 223.75, 727.49, 163.29, 114.51, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('24', 2, 'prod2Tariff', 163.65, 692.30, 224.11, 700.21, 163.65, 141.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('25', 2, 'prod2CountryExciseTax', 163.65, 626.30, 224.11, 634.21, 163.65, 207.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('26', 2, 'prod2CountryExciseTaxAmount', 163.65, 599.30, 224.11, 607.21, 163.65, 234.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('27', 2, 'prod2LocalExciseTaxBase', 163.65, 560.30, 224.11, 568.21, 163.65, 273.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('28', 2, 'prod2LocalExciseTaxAmount', 163.65, 533.30, 224.11, 541.21, 163.65, 300.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('29', 2, 'prod3DeclareAmountUsd', 460.27, 449.02, 520.73, 456.93, 460.27, 385.07, 60.46, 7.91, 'right', 'Decimal', 2, NULL, NULL, 2),
	('30', 2, 'prod3TariffRate', 109.27, 459.02, 129.16, 466.93, 109.27, 375.07, 19.89, 7.91, 'left', 'Decimal', 0, NULL, '%', 2),
	('31', 2, 'prod3DeclareAmountJpy', 163.29, 476.58, 223.75, 484.49, 163.29, 357.51, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('32', 2, 'prod3Tariff', 163.65, 449.30, 224.11, 457.21, 163.65, 384.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('33', 2, 'prod3CountryExciseTax', 163.65, 383.30, 224.11, 391.21, 163.65, 450.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('34', 2, 'prod3CountryExciseTaxAmount', 163.65, 356.30, 224.11, 364.21, 163.65, 477.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('35', 2, 'prod3LocalExciseTaxBase', 163.65, 317.30, 224.11, 325.21, 163.65, 516.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('36', 2, 'prod3LocalExciseTaxAmount', 163.65, 290.30, 224.11, 298.21, 163.65, 543.79, 60.46, 7.91, 'right', 'Decimal', 0, '\\', NULL, 2),
	('38', 1, NULL, 97.04, 629.70, 377.05, 644.01, 97.04, 197.99, 280.01, 14.31, NULL, NULL, NULL, NULL, NULL, 1),
	('39', 1, NULL, 435.04, 629.70, 483.72, 644.01, 435.04, 197.99, 48.68, 14.31, NULL, NULL, NULL, NULL, NULL, 1),
	('40', 1, NULL, 88.96, 608.42, 205.04, 616.33, 88.96, 225.67, 116.08, 7.91, NULL, NULL, NULL, NULL, NULL, 1),
	('41', 1, NULL, 88.96, 573.88, 205.04, 579.84, 88.96, 262.16, 116.08, 5.96, NULL, NULL, NULL, NULL, NULL, 1),
	('42', 1, NULL, 304.96, 608.52, 422.35, 626.33, 304.96, 215.67, 117.39, 17.81, NULL, NULL, NULL, NULL, NULL, 1),
	('43', 1, 'weight', 479.59, 608.51, 545.59, 616.42, 479.59, 225.58, 66.00, 7.91, NULL, NULL, NULL, NULL, NULL, 1),
	('44', 1, NULL, 396.45, 363.57, 549.97, 382.01, 396.45, 459.99, 153.52, 18.44, NULL, NULL, NULL, NULL, NULL, 1),
	('45', 2, NULL, 356.92, 728.77, 562.23, 745.74, 356.92, 96.26, 205.31, 16.97, NULL, NULL, NULL, NULL, NULL, 1),
	('46', 2, NULL, 356.92, 485.77, 562.23, 502.74, 356.92, 339.26, 205.31, 16.97, NULL, NULL, NULL, NULL, NULL, 1),
	('47', 1, NULL, 97.63, 743.77, 565.12, 754.14, 97.63, 87.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('48', 1, NULL, 97.63, 733.77, 565.12, 744.14, 97.63, 97.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('49', 1, NULL, 97.63, 724.77, 565.12, 735.14, 97.63, 106.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('50', 1, NULL, 97.63, 708.77, 565.12, 719.14, 97.63, 122.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('51', 1, NULL, 97.63, 697.77, 565.12, 708.14, 97.63, 133.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('52', 1, NULL, 97.63, 677.77, 565.12, 688.14, 97.63, 153.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('53', 1, NULL, 97.63, 667.77, 565.12, 678.14, 97.63, 163.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('54', 1, NULL, 97.63, 657.77, 565.12, 668.14, 97.63, 173.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1),
	('55', 1, NULL, 97.63, 648.77, 565.12, 659.14, 97.63, 182.86, 467.49, 10.37, NULL, NULL, NULL, NULL, NULL, 1);
/*!40000 ALTER TABLE `pdf_data_coordinate` ENABLE KEYS */;

-- Dumping structure for table pdf_tools.pdf_file
DROP TABLE IF EXISTS `pdf_file`;
CREATE TABLE IF NOT EXISTS `pdf_file` (
  `pdf_file_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pdf_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'PDF数据的ID',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件存储路径',
  `file_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件访问路径',
  PRIMARY KEY (`pdf_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pdf_tools.pdf_file: ~0 rows (大约)
DELETE FROM `pdf_file`;
/*!40000 ALTER TABLE `pdf_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `pdf_file` ENABLE KEYS */;

-- Dumping structure for table pdf_tools.pdf_list
DROP TABLE IF EXISTS `pdf_list`;
CREATE TABLE IF NOT EXISTS `pdf_list` (
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
  `permission_time` timestamp NULL DEFAULT NULL COMMENT '许可时间',
  PRIMARY KEY (`pdf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pdf_tools.pdf_list: ~0 rows (大约)
DELETE FROM `pdf_list`;
/*!40000 ALTER TABLE `pdf_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `pdf_list` ENABLE KEYS */;

-- Dumping structure for trigger pdf_tools.clear_coordinate_trigger
DROP TRIGGER IF EXISTS `clear_coordinate_trigger`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `clear_coordinate_trigger` BEFORE UPDATE ON `pdf_data_coordinate` FOR EACH ROW begin
    /* 如果left，top，width，height有更新。则清除llx，lly，urx，ury */
    if (old.margin_left != new.margin_left or old.margin_top != new.margin_top or old.width != new.width or old.height != new.height) then
    	set new.llx = null;
    	set new.lly = null;
    	set new.urx = null;
    	set new.ury = null;
    end if;
end//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
