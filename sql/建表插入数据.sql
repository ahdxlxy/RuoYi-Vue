/* 建表语句*/
create table s_paper
(
	paper_id int not null,
	user_id int not null,
	title varchar(32) not null,
	total_time int not null,
	user_time int not null,
	user_score int not null,
	qualify_score int not null,
	paper_content varchar(2000),
	paper_delete bit not null
);

create table s_subject
(
	subject_id int not null,
	bank_id int not null,
	subject_content varchar(5000) not null,
	subject_type int not null,
	subject_level int not null,
	subject_label varchar(100),
	analysis varchar(2000) not null,
	subject_delete bit not null
);

create table t_user
(
	user_id int not null,
	user_name varchar(20) not null,
	user_number varchar(15) not null,
	user_phone char(11) not null,
	user_sex char(4),
	role_id int not null,
	organ_id int not null,
	user_delete bit not null
);
create table exam
(
	exam_id int not null,
	exam_name varchar(50) not null,
	exam_pattern nchar(10) not null,
	exam_start datetime not null,
	exam_end datetime not null,
	exam_ctime int not null,
	exam_latetime int not null,
	paper_id int not null,
	exam_delete bit not null
);

create table organ
(
	organ_id int not null,
	organ_name varchar(20) not null,
	organ_exp varchar(50) not null,
	organ_delete bit not null
);

create table per_level_per
(
	per_level_id int not null,
	per_id int not null
);

create table permission
(
	per_id int not null,
	per_name varchar(20) not null,
	per_exp varchar(50)
);

create table permission_level
(
	per_level_id int not null,
    per_name varchar(20) not null
);

create table t_role
(
	role_id int not null,
	role_name varchar(20) not null,
	role_exp varchar(50),
	per_level_id int not null,
	role_delete bit not null
);

create table user_password
(
	user_id int,
	user_number varchar(15),
	pass_word varchar(20)
);

create table user_exam
(
	user_id int not null,
	user_name varchar(20) not null,
	exam_id int not null,
	exam_name varchar(50) not null,
	user_grade int,
	user_time int,
	user_statue bit not null,
	user_delete bit not null
);
/*数据插入语句*/
insert into organ
values
('1', '一年级数学组','一年级数学教学',0),
('2	','一年级语文组',	'一年级语文教学',0),
('3',	'二年级英语组',	'二年级英语教学',0),
('4',	'三年级数学组',	'三年级数学教学',0),
('5',	'四年级语文组',	'四年级语文教学',0),
('6',	'教务处',	'管理考试系统',0),
('7',	'三年级二班',	'班级',0);

insert into per_level_per
values
(2,2),
(1,1);

insert into permission
values
(1, '查看', '查看功能'),
(2, '出题', '拥有组卷权限');

insert into s_paper
values
(1, 2, 'title1', 90, 87, 100, 60, '1_2_3_4_10', 0),
(2, 2, 'title2', 90, 67, 100, 60, '1_2_3_4_5', 0),
(3, 12, 'title3', 90, 85, 100, 60, '1_3_2_4_10', 0),
(4, 3, 'title4', 90, 45, 100, 60, '1_2_3_6_210', 0),
(5, 24, 'title5', 90, 66, 100, 60, '1_7_3_4_10', 0);

insert into t_role
values
('1','管理员',NULL,1,0),
('2','教师',NULL,2,0),
('3','班主任',NULL,2,0),
('4','学生',NULL,3,0);

insert into t_user
values
(1,'张三','E01814002','15856987854','男',4,3,	0),
(2,'李四','R01512023','16689744561','女',4,2,	0),
(3,'王六','T45847896','18858744569','女',1,6,	0),
(4,'小赵','G01719827','14456789876','男',1,1,	0),
(5,	'小陈',	'E51814022',	'15855655421',	'男',	1,	1,	0),
(6,	'张五', 'E51778456',	'15567542341',	'男',	1,	1,	0),
(7,	'张七',	'E00000087',	'14567892345',	'女',	1,	1,	0);

insert into exam
values
(1, '数学第一次月考','固定时间','2021-08-15 18:00:00.000', '2021-08-15 19:00:00.000',60,10,1,0),
(2, '数学第二次月考','固定时长','2021-05-05 00:00:00.000', '2021-06-05 23:59:00.000',120,30,2,0),
(3, '数学第三次月考','固定时间','2020-08-12 00:00:00.000', '2020-08-15 00:00:00.000',90,15,3,0),
(4, '数学第四次月考','固定时长','2020-08-12 00:00:00.000', '2020-08-12 23:00:55.000',100,20,4,0),
(5, '数学第五次月考','固定时间','2020-08-15 23:59:59.000', '2020-08-15 23:50:32.000',120,30,5,0);

insert into user_exam
values
(1,'张三',1, '数学第一次月考',null,null,0,0),
(1,'张三',2, '数学第二次月考',null,null,0,0),
(1,'张三',3, '数学第三次月考',null,null,0,0);

insert into s_subject
values
(1,2,'数a分解质因数是a=2*2*3，数b分解质因数是b=2*2*5，数a和数b的最大公因数是（）。%%%A.2_B.4_C.6_D.60',1,2,null,'B',0),
(2,2,'已知3个数的和是470，第一个数比第二个数多160，第三个数比第一个数少180，这三个数的最大公因数是（）。%%%A.20_B.12_C.10_D.2',1,3,null,'C',0),
(3,2,'下列选项中的数是序数的是（）。%%%A.6只鸡_B.5支铅笔_C.2栋楼房_D.第2节课',1,1,null,'D',0),
(4,2,'a和b都是整数，且a*b=36，则a和b的和最大可能是（）。%%%A.12_B.13_C.20_D.37',1,2,null,'D',0),
(5,2,'一个两位数，各位数字的和的5倍比原数大6，则这个两位数是（）。%%%A.24_B.69_C.24和69_D.74',1,3,null,'C',0),
(6,2,'甲每秒跑3米，乙每秒跑2米，丙每秒跑4米，三人沿600米的环形跑道从同一点同时同向跑步，经过（）秒三人又同时从出发点出发。%%%A.12_B.600_C.300_D.无法确定',1,5,null,'B',0),
(7,2,'30以内的质数加上2，还是质数的有（）个。%%%A.4_B.5_C.6_D.7',1,3,null,'B',0),
(8,2,'一个两位数，个位上的数字和十位上的数字都是合数，并且是互质数，这个数最大是（）。%%%A.94_B.96_C.98_D.99',1,2,null,'C',0),
(9,2,'小圆的直径为a厘米，大圆的半径为a厘米，则小圆面积是大圆面积的（）。%%%A.50%_B.25%_C.20%_D.80%',1,2,null,'B',0),
(10,2,'某专业户去年每公顷产粮食9400千克，比前年增产二成，前年每公顷产粮食（）千克。%%%A.9400X*(1+20%)_B.9400+(1+20%)_C.9400*(1-20%)_D.9400+(1-20%)',1,4,null,'C',0),
(11,2,'“红、黄、黄、蓝、蓝、红、黄、黄、蓝......”，求第36个字的正确的算式是__________。',2,5,null,'36/(1+2+2)',0),
(12,2,'如果20天后是星期五，那么今天是星期__________。',2,1,null,'六',0),
(13,2,'一个等腰直角三角形，把它的两条直线边都扩大2倍后，成了一个较大的三角形，这个较大的三角形是__________。',2,4,null,'等腰直角三角形',0),
(14,2,'在一个锐角三角形中，有__________个锐角。',2,1,null,'3',0),
(15,2,'A、B两只青蛙进行跳跃比赛，A每次跳10厘米，B每次跳15厘米，他们每秒都只跳1次，且一起从起点开始向同一方向跳跃。在比赛途中，每隔12厘米有一陷阱，当它们中第一只掉进陷阱时，另一只距离它最近的陷阱有__________厘米。',2,5,null,'4',0),
(16,2,'一件工作，甲做1/3小时完成，乙做0.4小时完成，两人合作__________小时完成。',2,4,null,'2/11',0),
(17,2,'把5克食盐放入100克水中，盐水和水的重量比是__________。',2,3,null,'21:20',0),
(18,2,'投掷3次硬币，有2次正面朝上，1次反面朝上，那么，投掷第4次硬币正面朝上的可能性是__________。',2,2,null,'0.5',0),
(19,2,'将1/7化成小数后，小数点后第20位上的数字是__________。',2,2,null,'4',0),
(210,2,'100以内所有4的倍数的和是__________。',2,3,null,'1300',0);

insert into user_password
values
(1,'E01814002',123456),
(2,'R01512023',123456),
(3,'T45847896',123456),
(4,'G01719827',123456),
(5,'E51814022',123456),
(6,'E51778456',123456),
(7,'E00000087',123456)