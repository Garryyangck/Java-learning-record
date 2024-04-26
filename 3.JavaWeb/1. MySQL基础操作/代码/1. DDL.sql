



create table tb_user(

	id int ,
	username varchar(20),
	password varchar(32)

);





-- 学生表

create table tb_stu(
	id int ,
	name varchar(10),
	gender char(1),
	birthday date,
	score double(5,2) ,
	email varchar(64),
	tel varchar(20),
	status tinyint
);