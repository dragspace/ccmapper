# Host: 127.0.0.1  (Version: 5.6.21-log)
# Date: 2016-05-31 14:42:41
# Generator: MySQL-Front 5.3  (Build 4.13)

/*!40101 SET NAMES utf8 */;

#
# Source for table "demo"
#

DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "demo"
#

INSERT INTO `demo` VALUES (1,'demo',0,67);

#
# Source for table "demo2"
#

DROP TABLE IF EXISTS `demo2`;
CREATE TABLE `demo2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "demo2"
#

INSERT INTO `demo2` VALUES (1,'demo2',0,67);

#
# Source for table "demo3"
#

DROP TABLE IF EXISTS `demo3`;
CREATE TABLE `demo3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "demo3"
#

INSERT INTO `demo3` VALUES (1,'demo3',0,67);

#
# Source for table "demo4"
#

DROP TABLE IF EXISTS `demo4`;
CREATE TABLE `demo4` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

#
# Data for table "demo4"
#

INSERT INTO `demo4` VALUES (2,'呵呵',NULL,99),(3,'fasd',3,343),(4,'abc',1,120),(5,'abc',1,120),(6,'我是谁',NULL,NULL),(7,'我是谁',NULL,NULL),(8,'我是谁',NULL,NULL),(9,'我是谁',NULL,NULL),(10,'我是谁',NULL,NULL),(11,'我是谁',NULL,NULL),(12,'我是谁',NULL,NULL),(13,'我是谁',NULL,NULL),(14,'我是谁',NULL,NULL),(15,'我是谁',NULL,NULL),(16,'我是谁',NULL,NULL);

#
# Source for table "demo5"
#

DROP TABLE IF EXISTS `demo5`;
CREATE TABLE `demo5` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

#
# Data for table "demo5"
#

INSERT INTO `demo5` VALUES (6,'fads',NULL,123),(7,'fs',1,33);
