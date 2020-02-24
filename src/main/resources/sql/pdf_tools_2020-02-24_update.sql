alter table pdf_list add column exchange_rate_currency varchar(20) null comment '汇率币种';

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('204', 'AIR/IMP', 1, 'list', NULL, 'exchangeRateCurrency', NULL, NULL, NULL, NULL, 484.86, 411.98, 16.53, 7.25, 'right', 'String', NULL, NULL, NULL, 2, 'aspose');

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('205', 'SEA/IMP', 1, 'list', NULL, 'exchangeRateCurrency', NULL, NULL, NULL, NULL, 483.93, 432.31, 16.53, 7.25, 'right', 'String', NULL, NULL, NULL, 2, 'aspose');