/*
Navicat MySQL Data Transfer

Source Server         : NSG
Source Server Version : 50629
Source Host           : 112.74.132.175:3306
Source Database       : gxsfxy

Target Server Type    : MYSQL
Target Server Version : 50629
File Encoding         : 65001

Date: 2017-04-20 14:35:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_name` text,
  `activity_image` text,
  `activity_praise` text,
  `activity_comment` text,
  `activity_content` text,
  `activity_start_time` text,
  `activity_end_time` text,
  `activity_state` text,
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `area_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_name` text,
  `area_parent_id` text,
  PRIMARY KEY (`area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5025 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_uid` text,
  `comment_table` text,
  `comment_tid` text,
  `comment_content` text,
  `comment_rid` text,
  `comment_time` text,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT,
  `config_type` text,
  `config_name` text,
  `config_value` text,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic` (
  `dynamic_id` int(11) NOT NULL AUTO_INCREMENT,
  `dynamic_uid` text,
  `dynamic_type` text,
  `dynamic_praise` text,
  `dynamic_comment` text,
  `dynamic_title` text,
  `dynamic_content` text,
  `dynamic_location` text,
  `dynamic_device` text,
  `dynamic_time` text,
  PRIMARY KEY (`dynamic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `follow_id` int(11) NOT NULL AUTO_INCREMENT,
  `follow_uid` text,
  `follow_tid` text,
  `follow_time` text,
  PRIMARY KEY (`follow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `grade_id` int(11) NOT NULL AUTO_INCREMENT,
  `grade_uid` text,
  `grade_xh` text,
  `grade_kkxq` text,
  `grade_kcbh` text,
  `grade_kcmc` text,
  `grade_cj` text,
  `grade_xf` text,
  `grade_zxs` text,
  `grade_khfs` text,
  `grade_kcsx` text,
  `grade_kcxz` text,
  PRIMARY KEY (`grade_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1314 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_model` text,
  `log_control` text,
  `log_action` text,
  `log_state` text,
  `log_content` text,
  `log_client` text,
  `log_version` text,
  `log_device` text,
  `log_time` text,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25674 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `message_uid` text,
  `message_tid` text,
  `message_type` text,
  `message_content` text,
  `message_del` text,
  `message_time` text,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `news_type` text,
  `news_from` text,
  `news_title` text,
  `news_image` text,
  `news_link` text,
  `news_comment` text,
  `news_praise` text,
  `news_click` text,
  `news_time` text,
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3857 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for phone
-- ----------------------------
DROP TABLE IF EXISTS `phone`;
CREATE TABLE `phone` (
  `phone_id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_type` text,
  `phone_class` text,
  `phone_name` text,
  `phone_number` text,
  `phone_address` text,
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=362 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for points_goods
-- ----------------------------
DROP TABLE IF EXISTS `points_goods`;
CREATE TABLE `points_goods` (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_name` text,
  `goods_summary` text,
  `goods_image` text,
  `goods_points` text,
  `goods_price` text,
  `goods_content` text,
  `goods_sale_num` text,
  `goods_evaluate` text,
  `goods_state` text,
  `goods_time` text,
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for points_log
-- ----------------------------
DROP TABLE IF EXISTS `points_log`;
CREATE TABLE `points_log` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `pl_uid` text,
  `pl_type` text,
  `pl_points` text,
  `pl_content` text,
  `pl_time` text,
  PRIMARY KEY (`pl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1758 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for points_order
-- ----------------------------
DROP TABLE IF EXISTS `points_order`;
CREATE TABLE `points_order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for praise
-- ----------------------------
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `praise_id` int(11) NOT NULL AUTO_INCREMENT,
  `praise_uid` text,
  `praise_table` text,
  `praise_tid` text,
  `praise_time` text,
  PRIMARY KEY (`praise_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for upload
-- ----------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload` (
  `upload_id` int(11) NOT NULL AUTO_INCREMENT,
  `upload_uid` text,
  `upload_class` text,
  `upload_type` text,
  `upload_tid` text,
  `upload_url` text,
  `upload_time` text,
  PRIMARY KEY (`upload_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `push_id` text,
  `user_power` text,
  `user_mobile` text,
  `user_pass` text,
  `stu_id` text,
  `stu_pass` text,
  `user_college` text,
  `user_professional` text,
  `user_class` text,
  `nick_name` text,
  `true_name` text,
  `user_province` text,
  `user_city` text,
  `user_area` text,
  `user_avatar` text,
  `user_qq` text,
  `user_sign` text,
  `user_card` text,
  `user_birthday` text,
  `user_gender` text,
  `user_points` text,
  `user_email` text,
  `email_bind` text,
  `user_follow` text,
  `follow_mine` text,
  `user_visitor` text,
  `reg_time` text,
  `login_state` text,
  `login_num` text,
  `login_last_time` text,
  `login_time` text,
  `login_last_ip` text,
  `login_ip` text,
  `device_name` text,
  `device_system` text,
  `app_version` text,
  `is_close` text,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_log
-- ----------------------------
DROP TABLE IF EXISTS `user_log`;
CREATE TABLE `user_log` (
  `ul_id` int(11) NOT NULL AUTO_INCREMENT,
  `ul_uid` text,
  `ul_content` text,
  `ul_time` text,
  PRIMARY KEY (`ul_id`)
) ENGINE=InnoDB AUTO_INCREMENT=377 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `ut_id` int(11) NOT NULL AUTO_INCREMENT,
  `ut_uid` text,
  `ut_client` text,
  `ut_token` text,
  `ut_time` text,
  PRIMARY KEY (`ut_id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for verify
-- ----------------------------
DROP TABLE IF EXISTS `verify`;
CREATE TABLE `verify` (
  `verify_id` int(11) NOT NULL AUTO_INCREMENT,
  `verify_uid` text,
  `verify_type` text,
  `verify_code` text,
  PRIMARY KEY (`verify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor` (
  `visitor_id` int(11) NOT NULL AUTO_INCREMENT,
  `visitor_uid` text,
  `visitor_tid` text,
  `visitor_time` text,
  PRIMARY KEY (`visitor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;
