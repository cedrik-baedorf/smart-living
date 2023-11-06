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
INSERT INTO `users` (`username`, `password`, `last_name`, `first_name`, `role`) VALUES
	('cbaedorf', '0000000000000000000000000000000000000000000000000000001216985755', 'Baedorf', 'Cedrik', 'ADMIN'),
	('nrg', '0000000000000000000000000000000000000000000000000000001216985755', 'Gossner', 'Anna Maria', 'USER'),
	('ivano', '0000000000000000000000000000000000000000000000000000001216985755', 'Sachau', 'Ivan', 'USER'),
	('lienchen', '0000000000000000000000000000000000000000000000000000001216985755', 'Pradel', 'Alina', 'USER'),
	('heyden', '0000000000000000000000000000000000000000000000000000001216985755', 'Heyden', 'Tom', 'USER');

DELETE FROM `tasks`;
INSERT INTO `tasks` (`task_id`, `task_name`, `description`, `due_date`, `completed`) VALUES
    (0, 'Clean floors', 'Vaccuming the floors and mopping all floors', '2023-10-01',true),
    (1, 'Bathroom', 'Clean shower, toilet and sink', '2023-09-01', false),
    (2, 'Kitchen', 'Do the dishes and clean the working tables', '2023-11-07', false),
    (3, 'Living room', null, '2023-11-12', false);

DELETE FROM `assignments`;
INSERT INTO `assignments` (`assignment_id`, `task_id`, `username`) VALUES
        (0, 0, 'cbaedorf'),
        (1, 1, 'nrg'),
        (2, 2, 'ivano'),
        (3, 2, 'lienchen'),
        (4, 3, 'heyden');

DELETE FROM shopping_list_items;
INSERT INTO shopping_list_items (item, amount, unit) VALUES ('Chocolate', 2, 'kg (kilogram)');
INSERT INTO shopping_list_items (item, amount, unit) VALUES ('Milk', 1, 'l (litres)');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
