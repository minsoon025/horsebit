-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: j9a406.p.ssafy.io    Database: sys
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `MATURING_SCHEDULE`
--

DROP TABLE IF EXISTS `MATURING_SCHEDULE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MATURING_SCHEDULE` (
  `maturing_plan_no` bigint NOT NULL AUTO_INCREMENT,
  `maturing_date` date DEFAULT NULL,
  `day_flag` varchar(1) DEFAULT NULL,
  `ranch_name` varchar(30) DEFAULT NULL,
  `father_hr_no` bigint DEFAULT NULL,
  `mother_hr_no` bigint DEFAULT NULL,
  PRIMARY KEY (`maturing_plan_no`),
  KEY `father_hr_no` (`father_hr_no`),
  KEY `mother_hr_no` (`mother_hr_no`),
  CONSTRAINT `MATURING_SCHEDULE_ibfk_1` FOREIGN KEY (`father_hr_no`) REFERENCES `HORSE_INFO` (`hr_no`),
  CONSTRAINT `MATURING_SCHEDULE_ibfk_2` FOREIGN KEY (`mother_hr_no`) REFERENCES `HORSE_INFO` (`hr_no`),
  CONSTRAINT `MATURING_SCHEDULE_chk_1` CHECK ((`day_flag` in (_utf8mb4'D',_utf8mb4'N')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-06 11:25:36
