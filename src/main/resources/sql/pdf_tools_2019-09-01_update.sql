alter table pdf_list add column importer varchar(200) null comment '进口商';

INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('173', 'SEA/IMP', 1, 'list', NULL, 'importer', NULL, NULL, NULL, NULL, 194.82, 87.8, 132.1, 9.1, 'left', 'String', NULL, NULL, NULL, 2, 'itext');
INSERT INTO `pdf_data_coordinate` (`id`, `template_name`, `page_no`, `field_type`, `field_category`, `field_name`, `llx`, `lly`, `urx`, `ury`, `margin_left`, `margin_top`, `width`, `height`, `align`, `data_type`, `decimal_digits`, `prefix`, `suffix`, `action_type`, `read_type`)
 VALUES ('174', 'AIR/IMP', 1, 'list', NULL, 'importer', NULL, NULL, NULL, NULL, 194.82, 87.8, 132.1, 9.1, 'left', 'String', NULL, NULL, NULL, 2, 'itext');