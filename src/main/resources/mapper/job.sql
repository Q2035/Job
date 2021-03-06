use job;

create table j_user(
    id int primary key auto_increment,
    username varchar(50), # 学号
    clazz_id int,# 所属班级
    real_name varchar(50),
    password varchar(50),
    nickname varchar(50), # 昵称
    avatar varchar(100) # 头像地址
);

create table j_role(
    id int primary key auto_increment,
    user_id int , # 指向j_user表的id字段
    role_name varchar(20) # 角色
);

create table j_blacklist(
    id int primary key auto_increment,
    user_id int, # 指向j_user表的id字段
    message varchar(200) # 账号封禁原因
);

create table j_log_user(
    id int primary key auto_increment,
    user_id int,
    username varchar(50),
    message varchar(200) # 操作详情，可以包含IP Device等信息
);

# 暂时先将所有的提交记录放在一张表中
create table j_submit_info(
    id int primary key auto_increment,
    submit_time datetime,
    user_id int , # 作业提交者
    job_id int  # 所提交的作业
);

create table j_job_info(
    id int primary key auto_increment,
    job_name varchar(50), # 作业名字
    separators varchar(10),
    deadline datetime,
    create_time datetime,
    job_filename_info varchar(200), # 作业提交后最后重命名的方式
    user_id int # 作业创建者
);

create table j_clazz(
    id int primary key auto_increment,
    clazz_name varchar(50), # 班级名字
    student_count int # 班级人数
);

# 班级管理员
create table j_clazz_admin(
    id int primary key auto_increment,
    clazz_id int,
    user_id int
);

insert into j_user values (default, "Q", 1, "Q", "123456", "Q", null);
insert into j_clazz
    value (default, "1班", 1);

alter table j_user add account_status int;
drop table j_role;
alter table j_user add role_name varchar(20);
update j_user set role_name = "admin" where username = "Q";

# 路径拦截
create table j_uri_security(
  id int primary key auto_increment,
  uri varchar(255),
  role_name varchar(100)
);
# 2 学生 3 班级管理员 4 管理员
insert into j_uri_security values(default,"/user/main",2);
insert into j_uri_security values(default,"/manager/main",3);
insert into j_uri_security values(default,"/admin/main",4);

insert into j_uri_security values(default,"/manager/clazz",3);
insert into j_uri_security values(default,"/manager/clazz/index",3);
insert into j_uri_security values(default,"/manager/clazz/create",3);

alter table j_user drop column role_name;
alter table j_user add column roles int;

alter table j_uri_security change role_name roles varchar(20);

alter table j_user drop column clazz_id;

# 为了满足一个学生可以同时属于多个班级
create table j_user_clazz(
  id int primary key auto_increment,
  user_id int,
  clazz_id int
);

# 文件上传数据库表
CREATE TABLE `j_file`  (
     `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `user_id` int comment '上传用户',
     `path` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '相对路径',
     `name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '文件名',
     `suffix` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '后缀',
     `size` bigint(0) NULL DEFAULT NULL COMMENT '大小|字节B',
     `created_at` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
     `updated_at` bigint(0) NULL DEFAULT NULL COMMENT '修改时间',
     `shard_index` bigint(0) NULL DEFAULT NULL COMMENT '已上传分片',
     `shard_size` bigint(0) NULL DEFAULT NULL COMMENT '分片大小|B',
     `shard_total` bigint(0) NULL DEFAULT NULL COMMENT '分片总数',
     `file_key` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '文件标识',
     PRIMARY KEY (`id`) USING BTREE
);

# 班级和作业关联信息
create table `j_clazz_job` (
    id int auto_increment primary key ,
    clazz_id int,
    job_id int
);
