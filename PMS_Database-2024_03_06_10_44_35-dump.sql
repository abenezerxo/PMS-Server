-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: pms
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_name` varchar(50) DEFAULT NULL,
  `tin_number` varchar(50) DEFAULT NULL,
  `Address_1` varchar(200) DEFAULT NULL,
  `Address_2` varchar(200) DEFAULT NULL,
  `fee_per_hr` double DEFAULT NULL,
  `phone_no` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` (`id`, `company_name`, `tin_number`, `Address_1`, `Address_2`, `fee_per_hr`, `phone_no`) VALUES (1,'SINDU GEBERU PARKING FACILITY PLC','0005657455','A.A. S/C KIRKOS W.08 H.NO 485/C','KASSANCHIS MENEHARIA AREAD',10.5,'09123456789/09987456321'),(2,'ADDIS ABABA UNIVERSITY MAIN CAMPUS','0002223356','ADDIS ABABA 4 KILO','INFRONT OF MBR FACILITY',10,'0912456789'),(3,'BAHIR DAR AIR PORT','00111256555','BAHIR DAR MAIN AIR PORT','NEAR PEDA CAMPUS',10,'09784545485/00966356565'),(4,'HILTON HOTEL','00144545454','ADDIS ABABA ETHIOPIAN PARK','KIKOS SUB CITY',40,'01111111445/09878744442'),(5,'SHERATON HOTEL','0011188789','ADDIS ABABA INFRONT OF UNITY PARK','NIFAS SILK LAFTO',50,'09784513333/07656598998'),(7,'ADMINISTRATION','-','-','-',1,'-');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `job_title` varchar(500) DEFAULT NULL,
  `tin` varchar(50) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` (`id`, `first_name`, `middle_name`, `last_name`, `gender`, `address`, `mobile`, `date_of_birth`, `job_title`, `tin`, `email`) VALUES (1,'Abenezer','Desta','Zeleke','Male','Addis Ababa','0939966486','1995-01-10','System Admin','0000112254','abenezerth@gmail.com'),(2,'Fantahun','Bishaw','Hailu','Male','Addis Ababa Kality',NULL,'1999-02-10',NULL,NULL,'Fantahun@gmail.com'),(17,'Surafel','Tadesse','Guda','Male','Ferensay Legasion','0977423120','1997-01-09','System Administrator','00012345665','surafel.tadesse@gmail.com'),(23,'','','','','','',NULL,'','',''),(24,'','','','','','',NULL,'','',''),(25,'','','','','','',NULL,'','',''),(26,'','','','','','',NULL,'','',''),(27,'','','','','','',NULL,'','',''),(28,'','','','','','',NULL,'','',''),(29,'','','','','','',NULL,'','',''),(30,'','','','','','',NULL,'','',''),(31,'','','','','','',NULL,'','',''),(32,'','','','','','',NULL,'','',''),(33,'Mitiku','Tegegn','Asmelash','Male','','095412548','2024-10-10','System Administrator','-','mite@yahoo.com'),(34,'Estifanos','Eshetu','Teshome','Male','Addis Ababa','091234568','2020-01-08','','',''),(35,'ac','','','','','',NULL,'','',''),(36,'','','','','','',NULL,'','',''),(37,'','','','','','',NULL,'','',''),(38,'','','','','','',NULL,'','',''),(39,'aa','','','','','',NULL,'','',''),(40,'Eleni','','','','','',NULL,'','',''),(42,'Eyob','Abate','Abate','Male','Addisu Gebeya','091454545454','2000-02-27','System Admin','1234545','eyob.abante@gmail.com');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parked_car`
--

DROP TABLE IF EXISTS `parked_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parked_car` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vehicle_id` int DEFAULT NULL,
  `parking_lot_id` int DEFAULT NULL,
  `date_entry` datetime DEFAULT NULL,
  `date_exit` datetime DEFAULT NULL,
  `company_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parked_car`
--

LOCK TABLES `parked_car` WRITE;
/*!40000 ALTER TABLE `parked_car` DISABLE KEYS */;
INSERT INTO `parked_car` (`id`, `vehicle_id`, `parking_lot_id`, `date_entry`, `date_exit`, `company_id`) VALUES (1,1,1,'2024-02-16 02:38:44','2024-02-16 04:37:58',1),(2,2,2,'2024-02-16 00:38:51','2024-03-01 01:27:46',1),(6,32,27,'2024-02-28 06:30:00',NULL,1),(7,30,49,'2024-02-28 01:48:00','2024-03-01 01:55:21',1),(8,24,31,'2024-02-29 10:45:00',NULL,1),(9,1,34,'2024-03-01 10:30:00',NULL,1),(10,18,35,'2024-03-01 03:25:00','2024-03-01 04:28:42',1),(11,26,35,'2024-03-01 04:25:00','2024-03-01 04:30:56',1);
/*!40000 ALTER TABLE `parked_car` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_lot`
--

DROP TABLE IF EXISTS `parking_lot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_lot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL,
  `PARKING_LOT_NO` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_lot`
--

LOCK TABLES `parking_lot` WRITE;
/*!40000 ALTER TABLE `parking_lot` DISABLE KEYS */;
INSERT INTO `parking_lot` (`id`, `company_id`, `PARKING_LOT_NO`, `description`) VALUES (1,1,'1A','Infront of the Exit door edited'),(2,1,'1C','Inside the 3rd floor building'),(25,1,'C20','TEST'),(26,1,'Aa','Tet edited'),(27,1,'A43','1nd Floor'),(30,1,'D73','At the roof'),(31,1,'E29','At the roof'),(32,1,'F9','At the roof'),(33,1,'G12','At the roof'),(34,1,'H90','At the roof'),(35,1,'I64','At the roof'),(36,1,'J7','At the roof'),(37,1,'K36','1nd Floor'),(38,1,'L1','1nd Floor'),(39,1,'M23','1nd Floor'),(40,1,'N4','1nd Floor'),(41,1,'O6','1nd Floor'),(42,1,'P15','1nd Floor'),(43,1,'Q79','1nd Floor'),(44,1,'R80','1nd Floor'),(45,1,'S17','2nd Floor'),(46,1,'T51','2nd Floor'),(47,1,'U8','2nd Floor'),(48,1,'V27','2nd Floor'),(49,1,'A5','2nd Floor'),(50,1,'B91','2nd Floor'),(51,1,'C74','Basement'),(52,1,'D16','Basement'),(53,1,'E11','Basement'),(55,1,'G91','Basement'),(56,1,'H65','Basement'),(57,1,'I61','Basement'),(58,1,'J6','Basement'),(59,1,'K80','Basement'),(60,1,'L97','VIP'),(61,1,'M86','VIP'),(62,1,'N10','VIP'),(63,1,'O84','VIP'),(64,1,'P63','VIP'),(65,1,'Q77','VIP'),(66,1,'R16','VIP'),(67,1,'S80','Presendential Parking Lot'),(68,1,'T32','Presendential Parking Lot'),(69,1,'U67','Presendential Parking Lot'),(70,1,'V5','Presendential Parking Lot'),(71,1,'C88','C88 TEST Parking Spot');
/*!40000 ALTER TABLE `parking_lot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL,
  `full_name` varchar(500) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `company_id`, `full_name`, `user_id`, `password`, `role`, `status`, `mobile`) VALUES (2,1,'Estifanos Eskinder Assefa','Estifanos','827ccb0eea8a706c4c34a16891f84e7b','Admin',_binary '','0920523470'),(3,1,'Natnael Teklu Gebeyehu','Natnael','827ccb0eea8a706c4c34a16891f84e7b','Admin',_binary '',NULL),(4,1,'Fantahun Bishaw Hailu','Fantahun','e10adc3949ba59abbe56e057f20f883e','Agent',_binary '',''),(5,1,'Semahegn Kassie Tamer','Semahegn','827ccb0eea8a706c4c34a16891f84e7b','Agent',_binary '',''),(6,1,'Natinael Seifu Mengesha','Natinael','827ccb0eea8a706c4c34a16891f84e7b','Agent',_binary '','0911111111'),(7,3,'Abenezer Desta Zeleke','Abenezer','e10adc3949ba59abbe56e057f20f883e','Agent',_binary '\0','0939966486'),(10,2,'eyerusalm tadesseg','eyerus','e10adc3949ba59abbe56e057f20f883e','Agent',_binary '','0365656565');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_detail`
--

DROP TABLE IF EXISTS `user_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `temp_password` varchar(100) DEFAULT NULL,
  `temp_pass_flag` bit(1) DEFAULT NULL,
  `wrong_pass_count` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_detail`
--

LOCK TABLES `user_detail` WRITE;
/*!40000 ALTER TABLE `user_detail` DISABLE KEYS */;
INSERT INTO `user_detail` (`id`, `user_id`, `temp_password`, `temp_pass_flag`, `wrong_pass_count`) VALUES (8,2,'f1887d3f9e6ee7a32fe5e76f4ab80d63',_binary '\0',0),(9,3,'827ccb0eea8a706c4c34a16891f84e7b',_binary '\0',0),(10,4,'827ccb0eea8a706c4c34a16891f84e7b',_binary '\0',0),(11,5,'827ccb0eea8a706c4c34a16891f84e7b',_binary '\0',0),(12,6,'827ccb0eea8a706c4c34a16891f84e7b',_binary '\0',0),(13,7,'2dba05bea3e8010380c4539dcf39c9eb',_binary '\0',0),(15,10,'e10adc3949ba59abbe56e057f20f883e',_binary '\0',0);
/*!40000 ALTER TABLE `user_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `license_plate` varchar(50) DEFAULT NULL,
  `manufacturer` varchar(200) DEFAULT NULL,
  `model` varchar(200) DEFAULT NULL,
  `year` varchar(4) DEFAULT NULL,
  `driver_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` (`id`, `code`, `license_plate`, `manufacturer`, `model`, `year`, `driver_id`) VALUES (1,'3','54678','Toyota','Toyota Camry','2020',1),(2,'1','12345','Ford','Ford Mustang','2020',2),(7,'4','456789','Totyota','Corola','2010',17),(13,'1','1','','','',23),(14,'1','2222','','','',24),(15,'11','111','','','',25),(16,'12','121212','','','',26),(17,'2222','22','','','',27),(18,'3','232323','','','',28),(19,'23','232323','','','',29),(20,'test','test','','','',30),(21,'wewe','tes','','','',31),(22,'df','dfdf','','','',32),(23,'2','234912','Ford','Lexic','2019',33),(24,'1','129122','Fiat','v8','2019',34),(25,'12','2222','','','',35),(26,'332','2121212','','','',36),(27,'12','ddd','','','',37),(28,'cd','13223','','','',38),(29,'23','3333','','','',39),(30,'3','444444','','','',40),(32,'4','123458','Toyota','Corola','1995',42);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-06 10:44:35
