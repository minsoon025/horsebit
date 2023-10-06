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
-- Table structure for table `VOTE_STATUS`
--

DROP TABLE IF EXISTS `VOTE_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VOTE_STATUS` (
  `vote_st_no` bigint NOT NULL AUTO_INCREMENT,
  `vote_no` bigint NOT NULL,
  `token_no` bigint NOT NULL,
  `user_no` bigint NOT NULL,
  `token_cnt` int NOT NULL,
  PRIMARY KEY (`vote_st_no`),
  KEY `vote_no` (`vote_no`),
  KEY `token_no` (`token_no`),
  KEY `user_no` (`user_no`),
  CONSTRAINT `VOTE_STATUS_ibfk_1` FOREIGN KEY (`vote_no`) REFERENCES `VOTE_INFO` (`vote_no`),
  CONSTRAINT `VOTE_STATUS_ibfk_2` FOREIGN KEY (`token_no`) REFERENCES `VOTE_INFO` (`token_no`),
  CONSTRAINT `VOTE_STATUS_ibfk_3` FOREIGN KEY (`user_no`) REFERENCES `USER_INFO` (`user_no`)
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

-- Dump completed on 2023-10-06 11:25:37
