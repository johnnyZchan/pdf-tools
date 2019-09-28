INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('175', 'AIR/IMP', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 514.88, 421.98, 38.53, 7.61, NULL, NULL, NULL, NULL, NULL, 1, 'aspose');
INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('176', 'AIR/IMP', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 481.86, 421.98, 19.55, 7.61, NULL, NULL, NULL, NULL, NULL, 1, 'aspose');

alter table pdf_list add column declare_freight_amount_unit varchar(50) null comment '申报运费单位' after declare_freight_amount_usd;

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('177', 'AIR/IMP', 1, 'list', NULL, 'declareFreightAmountUnit', NULL, NULL, NULL, NULL, 345.86, 310.98, 16.53, 7.25, 'left', 'String', NULL, NULL, NULL, 2, 'aspose');