java-spring-atmosphere-chat
===========================

You should install MySQL 5 and Tomcat 7 or another server to run application.
Throught jdbc.properties you can configure database connection properties.
There is another jdbc.properties for JUnit tests in Test catalogue.

SQL script for creating database:

CREATE DATABASE `chat`;
USE `chat`;

CREATE TABLE `message` (
  
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  
  `author` varchar(50) DEFAULT NULL,
  
  `text` varchar(1000) DEFAULT NULL,
  
  `date` datetime DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  
  KEY `date_index` (`date`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `name` varchar(50) DEFAULT NULL,

  `uniqueid` varchar(45) DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uniqueid_UNIQUE` (`uniqueid`),

  UNIQUE KEY `name_UNIQUE` (`name`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


