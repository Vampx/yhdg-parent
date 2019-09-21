set names utf8;

/*电池上报日期*/
create table hdg_battery_report_date (
 battery_id char(12) not null,/*电池id*/
 report_date char(10), /*上报日期 2017-01-01*/
 record_count int unsigned, /*记录数*/
 primary key (battery_id, report_date)
) engine=innodb default charset=utf8;

/*充电器上报日期*/
create table hdg_cabinet_charger_report_date (
 cabinet_id char(12) not null,/*柜子id*/
 box_num varchar(10) not null,/*格口号*/
 report_date char(10), /*上报日期 2017-01-01*/
 record_count int unsigned, /*记录数*/
 primary key (cabinet_id, box_num, report_date)
) engine=innodb default charset=utf8;

/*柜子上报日期*/
create table hdg_cabinet_report_date (
 cabinet_id char(12) not null,/*柜子id*/
 report_date char(10), /*上报日期 2017-01-01*/
 record_count int unsigned, /*记录数*/
 primary key (cabinet_id, report_date)
) engine=innodb default charset=utf8;

/*柜子上报电池日期*/
create table hdg_cabinet_report_battery_date (
 battery_id char(12) not null,/*电池id*/
 report_date char(10), /*上报日期 2017-01-01*/
 record_count int unsigned, /*记录数*/
 primary key (battery_id, report_date)
) engine=innodb default charset=utf8;

/*车辆上报日期*/
create table hdg_vehicle_report_date (
 vehicle_id char(12) not null,/*车辆id*/
 report_date char(10), /*上报日期 2017-01-01*/
 record_count int unsigned, /*记录数*/
 primary key (vehicle_id, report_date)
) engine=innodb default charset=utf8;
