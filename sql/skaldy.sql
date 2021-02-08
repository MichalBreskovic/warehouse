CREATE DATABASE  IF NOT EXISTS `sklady` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_slovak_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sklady`;
-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: sklady
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `historia`
--

DROP TABLE IF EXISTS `historia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historia` (
  `umiestnenie` int NOT NULL,
  `skladnik` int NOT NULL,
  `datum` datetime NOT NULL,
  `pocet` int NOT NULL,
  `operacia` enum('naskladnenie','vyskladnenie') COLLATE utf8_slovak_ci NOT NULL,
  KEY `fk_umiestnenie_has_skladnik_skladnik1_idx` (`skladnik`),
  KEY `fk_umiestnenie_has_skladnik_umiestnenie1_idx` (`umiestnenie`),
  CONSTRAINT `fk_skladnik` FOREIGN KEY (`skladnik`) REFERENCES `skladnik` (`id`),
  CONSTRAINT `fk_umiestnenie` FOREIGN KEY (`umiestnenie`) REFERENCES `umiestnenie` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jednotka`
--

DROP TABLE IF EXISTS `jednotka`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jednotka` (
  `id` int NOT NULL AUTO_INCREMENT,
  `oznacenie` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sklad`
--

DROP TABLE IF EXISTS `sklad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sklad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nazov` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  `pocet_regalov` int NOT NULL,
  `pocet_policiek_v_regali` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `skladnik`
--

DROP TABLE IF EXISTS `skladnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skladnik` (
  `id` int NOT NULL AUTO_INCREMENT,
  `meno` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  `sklad` int NOT NULL,
  `veduci` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sklad` (`sklad`),
  CONSTRAINT `skladnik_ibfk_1` FOREIGN KEY (`sklad`) REFERENCES `sklad` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tovar`
--

DROP TABLE IF EXISTS `tovar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tovar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nazov` varchar(45) COLLATE utf8_slovak_ci NOT NULL,
  `merna_jednotka` int NOT NULL,
  `jednotkova_cena` double NOT NULL,
  `obmedzenie` int NOT NULL,
  `popis` varchar(100) COLLATE utf8_slovak_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tovar_jednotka1_idx` (`merna_jednotka`),
  CONSTRAINT `fk_tovar_jednotka1` FOREIGN KEY (`merna_jednotka`) REFERENCES `jednotka` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `umiestnenie`
--

DROP TABLE IF EXISTS `umiestnenie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `umiestnenie` (
  `id` int NOT NULL AUTO_INCREMENT,
  `druh` int NOT NULL,
  `mnozstvo` int NOT NULL,
  `sklad` int NOT NULL,
  `regal` int NOT NULL,
  `policka` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sklad` (`sklad`),
  KEY `druh` (`druh`),
  CONSTRAINT `umiestnenie_ibfk_1` FOREIGN KEY (`sklad`) REFERENCES `sklad` (`id`),
  CONSTRAINT `umiestnenie_ibfk_2` FOREIGN KEY (`druh`) REFERENCES `tovar` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-19 19:15:26
