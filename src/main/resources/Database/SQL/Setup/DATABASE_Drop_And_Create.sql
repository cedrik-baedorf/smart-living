/*
    This sql file may be used to create database named 'smart-living'.
    If there is already a database existing with such name, the existing database will be deleted
*/

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dropping the database 'smart-living' if previously existing.
DROP DATABASE IF EXISTS `smart-living`;

-- Creating a new database named 'smart-living'.
CREATE DATABASE IF NOT EXISTS `smart-living` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;