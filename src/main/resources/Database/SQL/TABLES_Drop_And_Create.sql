/*
    This sql file may be used to create all necessary table in database 'smart-living'.
    If such tables already exist, this script will delete these tables.
*/

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE `smart-living`;

DROP TABLE IF EXISTS `assignments`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `shopping_list_items`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `expenses`;
DROP table if exists `debitors_table`;


CREATE TABLE IF NOT EXISTS `users` (
    `username` VARCHAR(8) NOT NULL,
    `password` CHAR(64) NOT NULL,
    `last_name` VARCHAR(24) DEFAULT NULL,
    `first_name` VARCHAR(24) DEFAULT NULL,
    `role` VARCHAR(8) DEFAULT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `tasks` (
   `task_id` INT NOT NULL,
   `task_name` CHAR(32) NOT NULL,
   `description` CHAR(128) DEFAULT NULL,
   `due_date` DATE NOT NULL,
   `completed` BOOLEAN DEFAULT FALSE,
   PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `assignments` (
   `assignment_id` INT NOT NULL,
   `task_id` INT NOT NULL,
   `username` CHAR(8) DEFAULT NULL,
   PRIMARY KEY (`assignment_id`),
   CONSTRAINT `fk_task_id` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `shopping_list_items` (
    `item` VARCHAR(32) NOT NULL,
    `amount` DECIMAL(6,2) NOT NULL,
    `unit` VARCHAR(2) NOT NULL,
    PRIMARY KEY (`item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `expenses` (
    `expense_id` INT NOT NULL,
    `creditor` VARCHAR(32) NOT NULL,
    `product` VARCHAR(32) NOT NULL,
    `cost` DECIMAL(6,2) NOT NULL,
    PRIMARY KEY (`expense_id`),
    CONSTRAINT `fk_creditor` FOREIGN KEY (`creditor`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `debitors_table` (
    `debitors_table_id` INT NOT NULL,
    `expense_id` INT NOT NULL,
    `debitor_username` CHAR(8) DEFAULT NULL,
    PRIMARY KEY (`debitors_table_id`),
    CONSTRAINT `fk_expense_id` FOREIGN KEY (`expense_id`) REFERENCES `expenses` (`expense_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_debitor_username` FOREIGN KEY (`debitor_username`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
