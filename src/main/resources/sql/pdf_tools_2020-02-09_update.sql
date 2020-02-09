alter table pdf_list add column declare_freight_trade_terms varchar(20) null comment '申报贸易条款';
alter table pdf_list add column declare_freight_currency varchar(20) null comment '申报币种';

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('200', 'AIR/IMP', 1, 'list', NULL, 'declareFreightTradeTerms', NULL, NULL, NULL, NULL, 346.37, 300.43, 15.74, 7.38, 'left', 'String', NULL, NULL, NULL, 2, 'aspose');
INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('201', 'AIR/IMP', 1, 'list', NULL, 'declareFreightCurrency', NULL, NULL, NULL, NULL, 368.37, 300.43, 15.74, 7.38, 'left', 'String', NULL, NULL, NULL, 2, 'aspose');

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('202', 'SEA/IMP', 1, 'list', NULL, 'declareFreightTradeTerms', NULL, NULL, NULL, NULL, 359.37, 325.96, 15.74, 7.38, 'left', 'String', NULL, NULL, NULL, 2, 'aspose');
INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('203', 'SEA/IMP', 1, 'list', NULL, 'declareFreightCurrency', NULL, NULL, NULL, NULL, 383.37, 325.96, 15.74, 7.38, 'left', 'String', NULL, NULL, NULL, 2, 'aspose');