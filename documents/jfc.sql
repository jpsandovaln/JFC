CREATE DATABASE IF NOT EXISTS jfc;
USE jfc;

CREATE TABLE fileupload (
  id INT(11) NOT NULL AUTO_INCREMENT,
  file_path VARCHAR(255) DEFAULT NULL,
  MD5 VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;


CREATE TABLE USER (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) DEFAULT NULL,
  PASSWORD VARCHAR(255) DEFAULT NULL,
  rol VARCHAR(255) DEFAULT NULL,
  token VARCHAR(255) DEFAULT NULL,
  user_name VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;


INSERT  INTO USER(id,email,PASSWORD,rol,token,user_name) VALUES 
(1,'admin@msn.com','1234','admin','','admin');