/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.19 : Database - permission_manage
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`permission_manage` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `permission_manage`;

/*Table structure for table `system_config` */

DROP TABLE IF EXISTS `system_config`;

CREATE TABLE `system_config` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(42) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '键',
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名称',
  `options` json NOT NULL COMMENT '配置选项',
  `is_public` tinyint NOT NULL COMMENT '公开:0否,1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `key` (`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_config` */

/*Table structure for table `system_dict` */

DROP TABLE IF EXISTS `system_dict`;

CREATE TABLE `system_dict` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `code` varchar(42) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编号',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_dict` */

insert  into `system_dict`(`id`,`name`,`code`,`remark`) values (1,'菜单管理_权限类型','system_menu_type','权限类型'),(5,'用户管理_用户状态','system_user_status','用户状态');

/*Table structure for table `system_dict_list` */

DROP TABLE IF EXISTS `system_dict_list`;

CREATE TABLE `system_dict_list` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据名称',
  `dict_id` int unsigned NOT NULL COMMENT '字典ID',
  `val` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据值',
  `status` tinyint NOT NULL COMMENT '状态:0=停用,1=启用',
  `rank` smallint unsigned NOT NULL COMMENT '排序',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dict_id` (`dict_id`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `rank` (`rank`) USING BTREE,
  CONSTRAINT `system_dict_list_ibfk_1` FOREIGN KEY (`dict_id`) REFERENCES `system_dict` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_dict_list` */

insert  into `system_dict_list`(`id`,`name`,`dict_id`,`val`,`status`,`rank`,`create_date`) values (1,'菜单',1,'0',1,0,'2020-01-03 20:59:47'),(2,'按钮/权限',1,'1',1,1,'2020-01-12 20:34:05'),(5,'正常',5,'1',1,0,'2020-01-06 16:33:49'),(6,'封禁',5,'0',1,0,'2020-01-12 20:34:16');

/*Table structure for table `system_role` */

DROP TABLE IF EXISTS `system_role`;

CREATE TABLE `system_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `status` tinyint unsigned NOT NULL COMMENT '角色状态:0=停用,1=启用',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_role` */

insert  into `system_role`(`id`,`name`,`status`) values (1,'超级管理员',1),(6,'超级DEMO员',1);

/*Table structure for table `system_role_router` */

DROP TABLE IF EXISTS `system_role_router`;

CREATE TABLE `system_role_router` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `router_id` int unsigned NOT NULL COMMENT '权限id',
  `role_id` int unsigned NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `router_id` (`router_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `system_role_router_ibfk_1` FOREIGN KEY (`router_id`) REFERENCES `system_router` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `system_role_router_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `system_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=526 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_role_router` */

insert  into `system_role_router`(`id`,`router_id`,`role_id`) values (491,5,6),(492,35,6),(493,36,6),(494,32,6),(495,34,6),(496,4,6),(497,3,6),(498,17,6),(499,18,6),(500,9,6),(501,24,6),(502,21,6),(503,10,6),(504,7,6),(505,8,6),(506,6,6),(507,27,6),(508,1,6),(509,2,6);

/*Table structure for table `system_router` */

DROP TABLE IF EXISTS `system_router`;

CREATE TABLE `system_router` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限路径',
  `pid` int unsigned NOT NULL COMMENT '上级权限',
  `name` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `status` tinyint NOT NULL COMMENT '状态:0=停用,1=启用',
  `face` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `type` tinyint NOT NULL COMMENT '类型:0=菜单,1=路由权限/按钮',
  `rank` smallint NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_router` */

insert  into `system_router`(`id`,`path`,`pid`,`name`,`status`,`face`,`type`,`rank`) values (1,'dashboard',0,'仪表盘',1,'layui-icon-home',0,0),(2,'console',1,'控制台',1,'layui-icon-water',0,0),(3,'/system/user/getMeInfo',32,'获取自己的信息',1,'layui-icon-water',1,0),(4,'/system/user/getMenu',32,'获取菜单列表',1,'layui-icon-water',1,0),(5,'system',0,'系统管理',1,'layui-icon-set',0,5),(6,'user',5,'用户管理',1,NULL,0,0),(7,'menu',5,'菜单管理',1,NULL,0,1),(8,'/system/menu/getMenus',7,'获取全部菜单',1,NULL,1,0),(9,'dict',5,'数据字典',1,NULL,0,2),(10,'/system/dict/getDict',9,'获取字典数据',1,NULL,1,0),(11,'/system/menu/saveMenu',7,'保存菜单',1,NULL,1,0),(16,'/system/menu/delMenu',7,'删除菜单',1,'',1,0),(17,'role',5,'角色管理',1,'',0,3),(18,'/system/role/getRoles',17,'获取权限列表',1,'',1,0),(19,'/system/role/saveRole',17,'保存角色',1,'',1,0),(20,'/system/role/delRole',17,'删除角色',1,'',1,0),(21,'/system/dict/getDicts',9,'获取全部字典数据',1,'',1,0),(22,'/system/dict/saveDict',9,'保存数据字典',1,'',1,0),(23,'/system/dict/delDict',9,'删除字典',1,'',1,0),(24,'/system/dict/list/getDictLists',9,'获取数据列表',1,'',1,0),(25,'/system/dict/list/saveDictValue',9,'保存字典数据值',1,'',1,0),(26,'/system/dict/list/delDictValue',9,'删除字典数据',1,'',1,0),(27,'/system/user/getUsers',6,'获取用户列表',1,'',1,0),(28,'/system/user/saveUser',6,'保存用户',1,'',1,0),(29,'/system/user/delUser',6,'删除用户',1,'',1,0),(30,'other',5,'其他功能',0,NULL,0,0),(31,'/system/other/upload',30,'文件上传',1,NULL,1,0),(32,'personal',5,'个人中心',0,NULL,0,0),(33,'/system/user/saveMeInfo',32,'修改资料',1,NULL,1,0),(34,'/system/user/logout',32,'安全注销',1,NULL,1,0),(35,'config',5,'配置管理',1,NULL,0,5),(36,'/system/config/getConfigs',35,'获取配置列表',1,NULL,1,0),(37,'/system/config/saveConfig',35,'保存配置',1,NULL,1,0),(38,'/system/config/delConfig',35,'删除配置',1,NULL,1,0),(39,'/system/config/getPrivatelyConfig',35,'获取键配置',1,NULL,1,0);

/*Table structure for table `system_user` */

DROP TABLE IF EXISTS `system_user`;

CREATE TABLE `system_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pass` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `face` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `phone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '呢称',
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `login_date` datetime DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `login_ip` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` tinyint NOT NULL COMMENT '状态:0=停用,1=启用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user` (`user`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_user` */

insert  into `system_user`(`id`,`user`,`pass`,`face`,`phone`,`nickname`,`salt`,`login_date`,`create_date`,`login_ip`,`status`) values (1,'admin','8b3cbe175607107385e153031b009e98','/resource/20200107/b427a257f11bdb8.jpg','13800138000','没有梦想的小鱼','5ab76519e8dc23e934625e900075217','2020-01-12 20:26:06','2020-01-05 21:11:26','127.0.0.1',1),(7,'demo','4f610048fdfd87c40d9e342d3302d2af','/images/avatar.png','13712312311','哈哈1','aad2264d7f0324a0214090f51758494','2020-02-02 21:26:15','2020-01-12 20:29:07','127.0.0.1',1);

/*Table structure for table `system_user_role` */

DROP TABLE IF EXISTS `system_user_role`;

CREATE TABLE `system_user_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned DEFAULT NULL,
  `role_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `system_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `system_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `system_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `system_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_user_role` */

insert  into `system_user_role`(`id`,`user_id`,`role_id`) values (27,1,1),(30,7,6);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
