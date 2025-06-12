-- MySQL dump 10.13  Distrib 9.2.0, for macos15.2 (arm64)
--
-- Host: localhost    Database: paymybuddy
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `db_transaction`
--

DROP TABLE IF EXISTS `db_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `db_transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `fee` decimal(10,2) NOT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sender_id` (`sender_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `db_transaction_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `db_user` (`id`),
  CONSTRAINT `db_transaction_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `db_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `db_transaction`
--

LOCK TABLES `db_transaction` WRITE;
/*!40000 ALTER TABLE `db_transaction` DISABLE KEYS */;
INSERT INTO `db_transaction` VALUES (1,1,2,'Cadeau de noel',20.00,0.10,'2025-06-03 10:39:52');
/*!40000 ALTER TABLE `db_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `db_user`
--

DROP TABLE IF EXISTS `db_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `db_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `balance` decimal(10,2) DEFAULT '0.00',
  `role` varchar(20) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `db_user`
--

LOCK TABLES `db_user` WRITE;
/*!40000 ALTER TABLE `db_user` DISABLE KEYS */;
INSERT INTO `db_user` VALUES (1,'Jean964','jeanjean@email.com','$2a$10$Zd6iahffw6N11rTySBO4bOu35lLvdRpbg968nex8L5CvNSuUg44Pm',79.90,'USER'),(2,'Pierre42','pierre-berger@email.com','$2a$10$9mptt2ZUeBUtrE38mmCieOZX6zyQsUdOE.Gy8IuC.rHR3UP/xAcOm',40.00,'USER'),(3,'admin','admin@email.com','$2a$10$udSlyPlByzgQPkxaZn.C8Og5TNKnykKSrmZfPQSY.p7wzupA0Y01q',0.00,'ADMIN');
/*!40000 ALTER TABLE `db_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_friend`
--

DROP TABLE IF EXISTS `user_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_friend` (
  `user_id` int NOT NULL,
  `friend_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`friend_id`),
  KEY `friend_id` (`friend_id`),
  CONSTRAINT `user_friend_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `db_user` (`id`),
  CONSTRAINT `user_friend_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `db_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_friend`
--

LOCK TABLES `user_friend` WRITE;
/*!40000 ALTER TABLE `user_friend` DISABLE KEYS */;
INSERT INTO `user_friend` VALUES (2,1),(1,2);
/*!40000 ALTER TABLE `user_friend` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-03 10:43:33
