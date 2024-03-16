-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: quanlithuvien
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `phieu`
--

DROP TABLE IF EXISTS `phieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phieu` (
  `id` varchar(10) NOT NULL,
  `id_books` varchar(500) NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'Borrowing',
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `date_start` datetime NOT NULL,
  `date_return` datetime NOT NULL,
  PRIMARY KEY (`id`,`id_books`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu`
--

LOCK TABLES `phieu` WRITE;
/*!40000 ALTER TABLE `phieu` DISABLE KEYS */;
INSERT INTO `phieu` VALUES ('1eXyIa8IBd','002','Borrowing','','','Male','','','2024-03-16 11:04:06','2028-08-08 00:00:00'),('4AuBg8c3Vq','001,025,8936107813439','Borrowing','','','Male','','','2024-03-16 10:55:44','2024-09-14 00:00:00'),('7cUMM8M3Dv','008,009','Borrowing','','','Male','','','2024-03-16 10:45:28','2024-03-25 00:00:00'),('d8BCUh8ubE','003','Borrowing','','','Female','','','2024-03-16 12:12:32','2024-02-02 00:00:00'),('FZxzhTn9A5','002','Borrowing','','','Male','','','2024-03-16 11:02:27','2028-05-05 00:00:00'),('iY5OpQnB4x','003','Borrowing','','','Male','','','2024-03-16 11:00:42','2027-06-06 00:00:00'),('JwH5zKExSh','003,002','Borrowing','','','Male','','','2024-03-16 10:48:01','2024-03-25 00:00:00'),('K9ZS4GCXrU','002,003','Borrowing','','','Male','','','2024-03-16 11:37:39','2025-06-03 00:00:00'),('X6fLnV82YG','8936107813439,8936107813880','Borrowing','','','Non-Binary','','','2024-03-16 10:57:09','2024-03-14 00:00:00'),('yaxf6OXA4T','003','Borrowing','','','Female','','','2024-03-16 12:13:43','2024-02-02 00:00:00'),('yHNbkmoyhC','003','Borrowing','','','Male','','','2024-03-16 11:32:53','2024-06-05 00:00:00');
/*!40000 ALTER TABLE `phieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sach`
--

DROP TABLE IF EXISTS `sach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sach` (
  `id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `summary` varchar(70) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name_author` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `inventory_quantity` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sach`
--

LOCK TABLES `sach` WRITE;
/*!40000 ALTER TABLE `sach` DISABLE KEYS */;
INSERT INTO `sach` VALUES ('001','The Great Gatsby','Classic novel about the American Dream','F. Scott Fitzgerald',14),('002','To Kill a Mockingbird','Racial injustice in the American South','Harper Lee',3),('003','1984','Dystopian fiction exploring surveillance','George Orwell',15),('004','Pride and Prejudice','Romantic novel by Jane Austen','Jane Austen',17),('005','The Catcher in the Rye','Coming-of-age novel by J.D. Salinger','J.D. Salinger',11),('006','The Lord of the Rings','Epic fantasy adventure','J.R.R. Tolkien',29),('007','Harry Potter and the Sorcerer\'s Stone','Wizardry and magic at Hogwarts','J.K. Rowling',21),('008','The Hunger Games','Dystopian tale of survival','Suzanne Collins',27),('009','The Hobbit','Adventure in the Shire','J.R.R. Tolkien',24),('010','The Da Vinci Code','Thriller by Dan Brown','Dan Brown',20),('011','The Hitchhiker\'s Guide to the Galaxy','Science fiction comedy','Douglas Adams',15),('012','Frankenstein','Classic gothic horror','Mary Shelley',18),('013','The Shining','Horror novel by Stephen King','Stephen King',23),('014','The Girl with the Dragon Tattoo','Mystery thriller','Stieg Larsson',17),('015','Brave New World','Dystopian novel by Aldous Huxley','Aldous Huxley',21),('016','The Chronicles of Narnia','Fantasy series by C.S. Lewis','C.S. Lewis',26),('017','The Road','Post-apocalyptic fiction','Cormac McCarthy',19),('018','Moby-Dick','Adventure novel by Herman Melville','Herman Melville',14),('019','Wuthering Heights','Gothic novel by Emily Brontë','Emily Brontë',16),('020','The Color Purple','Drama exploring racism and sexism','Alice Walker',24),('021','The Grapes of Wrath','Depiction of the Great Depression','John Steinbeck',27),('022','One Hundred Years of Solitude','Magical realism by Gabriel García Márquez','Gabriel García Márquez',29),('023','Anna Karenina','Russian novel by Leo Tolstoy','Leo Tolstoy',18),('024','The Picture of Dorian Gray','Oscar Wilde\'s philosophical novel','Oscar Wilde',20),('025','The Odyssey','Epic poem by Homer','Homer',14),('8936107813439','Mastering Design Patterns','Giới thiệu đến bạn hơn 20 mẫu thiết kế','Ta Van Dung',193),('8936107813880','Programming Principles','Giúp viết mã gọn gàng và dễ nâng cấp, bảo trì','Ta Van Dung',298);
/*!40000 ALTER TABLE `sach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `id` varchar(10) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES ('plk2j3hf45','duy','123');
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-16 19:53:41
