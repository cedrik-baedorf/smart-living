/*
    This sql file may be used to insert test data into the tables of database 'smart-living'.
    Before inserting the test data, this script deletes all data from the tables.
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

-- export data of table smart-living.users: 5 rows
DELETE FROM `users`;
INSERT INTO `users` (`username`, `password`, `last_name`, `first_name`, `role`, `email`) VALUES
	('cbaedorf', '0000000000000000000000000000000000000000000000000000001216985755', 'Baedorf', 'Cedrik', 'ADMIN', 'cedrik.baedorf@studmail.hwg-lu.de'),
	('nrg', '0000000000000000000000000000000000000000000000000000001216985755', 'Gossner', 'Anna Maria', 'SUPREME', 'anna.gossner@studmail.hwg-lu.de'),
	('ivano', '0000000000000000000000000000000000000000000000000000001216985755', 'Sachau', 'Ivan', 'USER', 'ivan.sachau@studmail.hwg-lu.de'),
	('lienchen', '0000000000000000000000000000000000000000000000000000001216985755', 'Pradel', 'Alina', 'USER', 'alina.pradel@studmail.hwg-lu.de'),
	('heyden', '0000000000000000000000000000000000000000000000000000001216985755', 'Heyden', 'Tom', 'USER', 'tom.heyden@studmail.hwg-lu.de');

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
INSERT INTO shopping_list_items (item, amount, unit) VALUES
         ('Flour', 3, 'kg'),
         ('Rice', 2, 'kg'),
         ('Pasta', 4, 'kg'),
         ('Beans', 1, 'kg'),
         ('Lentils', 2, 'kg'),
         ('Tomato Sauce', 3, 'l'),
         ('Vegetable Oil', 5, 'l'),
         ('Apple Juice', 2, 'l'),
         ('Orange Juice', 2, 'l'),
         ('Soy Sauce', 1, 'l'),
         ('Vinegar', 1, 'l'),
         ('Water', 10, 'l'),
         ('Honey', 1, 'kg'),
         ('Salt', 1, 'kg'),
         ('Oatmeal', 2, 'kg');


DELETE FROM `expenses`;
INSERT INTO `expenses` (`expense_id`, `creditor_name`, `product`, `cost`) VALUES
        (0, 'cbaedorf','Chocolate', '1.70'),
        (1, 'nrg','Butter', '4.50'),
        (2, 'ivano', 'Milk', '1.20');

DELETE FROM `debtor_mapping`;
INSERT INTO `debtor_mapping` (`id`, `expense_id`, `debtor_name`) VALUES
        (0, 0, 'cbaedorf'),
        (1, 1, 'nrg'),
        (2, 2, 'ivano'),
        (3, 2, 'lienchen');


/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
