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

USE `smart-living`;

-- export data of table smart-living.users: 5 rows
DELETE FROM `users`;
INSERT INTO `users` (`username`, `password`, `last_name`, `first_name`) VALUES
	('cbaedorf', '0000000000000000000000000000000000000000000000000000001216985755', 'Baedorf', 'Cedrik');

-- Data exporting was unselected.

DELETE FROM tasks;
INSERT INTO tasks (task_id, task_name, description, start_date, end_date, reoccurrence, completed)
VALUES (0, 'Clean floors', 'Vaccuming the floors and mopping all floors', '2023-10-01', '2023-10-10', 7, false);

INSERT INTO tasks (task_id, task_name, description, start_date, reoccurrence, completed)
VALUES (1, 'Bathroom', 'Clean shower, toilet and sink', '2023-09-01', 3, true);

INSERT INTO tasks (task_id, task_name, description, start_date, end_date, reoccurrence)
VALUES (2, 'Kitchen', 'Do the dishes and clean the working tables', '2023-01-01', '2024-12-31', 9);

DELETE FROM assignments;
INSERT INTO assignments (assignment_id, task_id, username) VALUES (0, 0, 'cbaedorf');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
