-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.11.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for smart-living
CREATE DATABASE IF NOT EXISTS `smart-living` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `smart-living`;

DROP TABLE IF EXISTS `assignments`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `shopping_list_items`;
DROP TABLE IF EXISTS `tasks`;

CREATE TABLE IF NOT EXISTS `users` (
  `username` VARCHAR(8) NOT NULL,
  `password` CHAR(64) NOT NULL,
  `last_name` VARCHAR(24) DEFAULT NULL,
  `first_name` VARCHAR(24) DEFAULT NULL,
PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `tasks` (
   `task_id` INT NOT NULL AUTO_INCREMENT,
   `task_name` CHAR(32) NOT NULL,
   `description` CHAR(128) DEFAULT NULL,
   `start_date` DATE NOT NULL,
   `end_date` DATE DEFAULT NULL,
   `reoccurrence` INT NOT NULL,
   `completed` BOOLEAN DEFAULT FALSE,
   PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `assignments` (
   `assignment_id` INT NOT NULL AUTO_INCREMENT,
   `task_id` INT NOT NULL,
   `username` CHAR(8) DEFAULT NULL,
   PRIMARY KEY (`assignment_id`),
   CONSTRAINT `fk_task_id` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `shopping_list_items` (
    `item` CHAR(32) NOT NULL,
    `amount` INT NOT NULL,
    `unit` CHAR(2) NOT NULL,
    PRIMARY KEY (`item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
