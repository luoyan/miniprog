SET NAMES 'utf8';
CREATE DATABASE `miui_ad_statistic` DEFAULT CHARSET=utf8;

USE `miui_ad_statistic`;
CREATE TABLE IF NOT EXISTS `demo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) DEFAULT NULL,
  `keyword` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;