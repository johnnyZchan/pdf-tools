CREATE TABLE `pdf_list_detail` (
  `pdf_detail_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键ID',
  `pdf_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'PDF ID',
  `prod_no` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品名编号',
  `declare_amount_usd` decimal(12,2) DEFAULT NULL COMMENT '美金申报价值',
  `tariff_rate` decimal(12,4) DEFAULT NULL COMMENT '关税率',
  `freight_pct` decimal(12,4) DEFAULT NULL COMMENT '运费比重',
  `declare_amount_jpy` decimal(12,2) DEFAULT NULL COMMENT '日元申报价值',
  `tariff_base` decimal(12,2) DEFAULT NULL COMMENT '关税计算基数',
  `tariff` decimal(12,2) DEFAULT NULL COMMENT '关税额',
  `tariff_rounding` decimal(12,2) DEFAULT NULL COMMENT '关税取整',
  `country_excise_tax` decimal(12,2) DEFAULT NULL COMMENT '国内消费税',
  `country_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '国内消费税额基数',
  `country_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '国内消费税金额',
  `local_excise_tax_base` decimal(12,2) DEFAULT NULL COMMENT '地方消费税基数',
  `local_excise_tax_amount` decimal(12,2) DEFAULT NULL COMMENT '地方消费税金额',
  PRIMARY KEY (`pdf_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO pdf_list_detail(pdf_detail_id,pdf_id,prod_no,declare_amount_usd,tariff_rate,freight_pct,declare_amount_jpy,tariff_base,tariff,tariff_rounding,
 country_excise_tax,country_excise_tax_base,country_excise_tax_amount,local_excise_tax_base,local_excise_tax_amount)
 SELECT REPLACE(UUID(),"-",""), pl.pdf_id, 'prod1', pl.prod1_declare_amount_usd, pl.prod1_tariff_rate,
 pl.prod1_freight_pct, pl.prod1_declare_amount_jpy, pl.prod1_tariff_base, pl.prod1_tariff, pl.prod1_tariff_rounding,
 pl.prod1_country_excise_tax, pl.prod1_country_excise_tax_base, pl.prod1_country_excise_tax_amount, pl.prod1_local_excise_tax_base,
 pl.prod1_local_excise_tax_amount
 FROM pdf_list AS pl;

INSERT INTO pdf_list_detail(pdf_detail_id,pdf_id,prod_no,declare_amount_usd,tariff_rate,freight_pct,declare_amount_jpy,tariff_base,tariff,tariff_rounding,
 country_excise_tax,country_excise_tax_base,country_excise_tax_amount,local_excise_tax_base,local_excise_tax_amount)
 SELECT REPLACE(UUID(),"-",""), pl.pdf_id, 'prod2', pl.prod2_declare_amount_usd, pl.prod2_tariff_rate,
 pl.prod2_freight_pct, pl.prod2_declare_amount_jpy, pl.prod2_tariff_base, pl.prod2_tariff, pl.prod2_tariff_rounding,
 pl.prod2_country_excise_tax, pl.prod2_country_excise_tax_base, pl.prod2_country_excise_tax_amount, pl.prod2_local_excise_tax_base,
 pl.prod2_local_excise_tax_amount
 FROM pdf_list AS pl;

INSERT INTO pdf_list_detail(pdf_detail_id,pdf_id,prod_no,declare_amount_usd,tariff_rate,freight_pct,declare_amount_jpy,tariff_base,tariff,tariff_rounding,
 country_excise_tax,country_excise_tax_base,country_excise_tax_amount,local_excise_tax_base,local_excise_tax_amount)
 SELECT REPLACE(UUID(),"-",""), pl.pdf_id, 'prod3', pl.prod3_declare_amount_usd, pl.prod3_tariff_rate,
 pl.prod3_freight_pct, pl.prod3_declare_amount_jpy, pl.prod3_tariff_base, pl.prod3_tariff, pl.prod3_tariff_rounding,
 pl.prod3_country_excise_tax, pl.prod3_country_excise_tax_base, pl.prod3_country_excise_tax_amount, pl.prod3_local_excise_tax_base,
 pl.prod3_local_excise_tax_amount
 FROM pdf_list AS pl;

