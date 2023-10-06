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
-- Table structure for table `TRADE_HISTORY`
--

DROP TABLE IF EXISTS `TRADE_HISTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRADE_HISTORY` (
  `execution_no` bigint NOT NULL AUTO_INCREMENT,
  `token_no` bigint DEFAULT NULL,
  `price` int NOT NULL,
  `quantity` double NOT NULL,
  `fee` double DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `seller_order_no` bigint NOT NULL,
  `seller_user_no` bigint NOT NULL,
  `seller_order_time` datetime(6) NOT NULL,
  `buyer_order_no` bigint NOT NULL,
  `buyer_user_no` bigint NOT NULL,
  `buyer_order_time` datetime(6) NOT NULL,
  `sell_buy_flag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`execution_no`),
  KEY `seller_order_no` (`seller_order_no`),
  KEY `seller_user_no` (`seller_user_no`),
  KEY `buyer_order_no` (`buyer_order_no`),
  KEY `buyer_user_no` (`buyer_user_no`),
  KEY `FK11gb42annv8sohvox0agve0ea` (`token_no`),
  CONSTRAINT `FK11gb42annv8sohvox0agve0ea` FOREIGN KEY (`token_no`) REFERENCES `TOKEN_INFO` (`token_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2919 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-06 11:25:33
