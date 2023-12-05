/*
    This sql file may be used to create a demo user in the database 'smart-living'.
    The demo user is identified by its username = 'DEMO_USER' and its password = 'PASSWORD'.
    The demo user has SELECT, INSERT, DELETE, and UPDATE rights on all tables of database 'smart-living'.
*/

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP USER IF EXISTS 'DEMO_USER';
CREATE USER IF NOT EXISTS 'DEMO_USER' IDENTIFIED BY 'PASSWORD';
REVOKE ALL PRIVILEGES ON *.* FROM 'DEMO_USER'@'localhost';
GRANT SELECT ON `smart-living`.* TO 'DEMO_USER'@'localhost';
GRANT INSERT ON `smart-living`.* TO 'DEMO_USER'@'localhost';
GRANT DELETE ON `smart-living`.* TO 'DEMO_USER'@'localhost';
GRANT UPDATE ON `smart-living`.* TO 'DEMO_USER'@'localhost';
FLUSH PRIVILEGES;