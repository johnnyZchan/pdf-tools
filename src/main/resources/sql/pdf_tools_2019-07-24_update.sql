ALTER table pdf_data_coordinate add COLUMN read_type varchar(10) not null default 'aspose' comment '读取方式（aspose、itext）';

UPDATE pdf_data_coordinate SET read_type='itext' WHERE id='14';
