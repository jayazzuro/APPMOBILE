CREATE DATABASE  IF NOT EXISTS `db_quanao` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_quanao`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: db_quanao
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `idAd` int NOT NULL AUTO_INCREMENT,
  `passWord` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `hoTen` varchar(45) NOT NULL,
  `sex` varchar(45) NOT NULL,
  PRIMARY KEY (`idAd`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ctdh`
--

DROP TABLE IF EXISTS `ctdh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ctdh` (
  `idCTDH` int NOT NULL AUTO_INCREMENT,
  `idDH` int NOT NULL,
  `maKH` int NOT NULL,
  `soLuong` int NOT NULL,
  `TongTien` varchar(45) NOT NULL,
  `diaChi` varchar(45) NOT NULL,
  PRIMARY KEY (`idCTDH`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ctdh`
--

LOCK TABLES `ctdh` WRITE;
/*!40000 ALTER TABLE `ctdh` DISABLE KEYS */;
INSERT INTO `ctdh` VALUES (20,36,23,1,'350.000','12'),(21,37,22,2,'1.150.000','TP Hồ Chí Minh '),(22,38,22,1,'350.000','TP Hồ Chí Minh '),(23,39,22,4,'3.850.000','TP Hồ Chí Minh '),(24,40,22,1,'350.000','TP Hồ Chí Minh ');
/*!40000 ALTER TABLE `ctdh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dathang`
--

DROP TABLE IF EXISTS `dathang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dathang` (
  `idDH` int NOT NULL AUTO_INCREMENT,
  `idKH` varchar(45) NOT NULL,
  `TongSL` int NOT NULL,
  `TongTien` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idDH`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dathang`
--

LOCK TABLES `dathang` WRITE;
/*!40000 ALTER TABLE `dathang` DISABLE KEYS */;
INSERT INTO `dathang` VALUES (36,'23',1,'350.000'),(37,'22',2,'1.150.000'),(38,'22',1,'350.000'),(39,'22',4,'3.850.000'),(40,'22',1,'350.000');
/*!40000 ALTER TABLE `dathang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `giohang`
--

DROP TABLE IF EXISTS `giohang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giohang` (
  `idCart` int NOT NULL AUTO_INCREMENT,
  `tenHang` varchar(45) NOT NULL,
  `donGia` varchar(45) NOT NULL,
  `DVT` varchar(45) NOT NULL,
  `hinhAnh` varchar(45) NOT NULL,
  `soLuong` varchar(45) NOT NULL,
  `maKH` varchar(45) NOT NULL,
  PRIMARY KEY (`idCart`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giohang`
--

LOCK TABLES `giohang` WRITE;
/*!40000 ALTER TABLE `giohang` DISABLE KEYS */;
INSERT INTO `giohang` VALUES (74,'DUMA STREET #1','350.000','VND','DUMA_STREET_1.png','1','23'),(76,'POLO STREET #1','350.000','VND','POLO_STREET_1.png','1','22');
/*!40000 ALTER TABLE `giohang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hanghoa`
--

DROP TABLE IF EXISTS `hanghoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hanghoa` (
  `maHang` int NOT NULL AUTO_INCREMENT,
  `tenHang` varchar(45) NOT NULL,
  `DonGia` varchar(45) NOT NULL,
  `DVT` varchar(45) NOT NULL,
  `moTa` text NOT NULL,
  `hinhAnh` varchar(100) NOT NULL,
  `TenTheLoai` varchar(45) NOT NULL,
  PRIMARY KEY (`maHang`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hanghoa`
--

LOCK TABLES `hanghoa` WRITE;
/*!40000 ALTER TABLE `hanghoa` DISABLE KEYS */;
INSERT INTO `hanghoa` VALUES (24,'DUMA STREET #1','350.000','VND','áo đẹp','DUMA_STREET_1.png','T-SHIRT'),(25,'DUMA STREET #2','400.000','VND','Áo đẹp ','DUMA_STREET_2.png','T-SHIRT'),(110,'POLO STREET #1','350.000','VND','áo đẹp','POLO_STREET_1.png','POLO SHIRT'),(119,'POLO STREET #2','350.000','VND','','POLO_STREET_2.png','POLO SHIRT'),(120,'POLO STREET #3','600.000','VND','hay','POLO_STREET_3.png','POLO SHIRT'),(126,'DUMA STREET #3','500.000','VND','Aó quá đẹp','DUMA_STREET_3.png','T-SHIRT'),(128,'DUMA STREET #1','123','VND','111','Fffront.png','T-SHIRT');
/*!40000 ALTER TABLE `hanghoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `maKH` int NOT NULL AUTO_INCREMENT,
  `hoTen` varchar(45) NOT NULL,
  `diaChi` varchar(45) NOT NULL,
  `SDT` varchar(15) NOT NULL,
  `passWord` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `sex` varchar(10) NOT NULL,
  PRIMARY KEY (`maKH`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES (22,'Jay','TP Hồ Chí Minh ','123456','123','1@gmail.com','nam'),(23,'Phạm Duy Mạnh','12','123456','123','12@gmail.com','nam'),(24,'Phạm Duy Mạnh','Hà Nội','0234456','123','14@gmail.com','nam'),(25,'huy','Thanh Hóa','0258789','1','0@gmail.com','nữ'),(26,'tuyền','123','123','1','t@gmail.com','nam');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theloai`
--

DROP TABLE IF EXISTS `theloai`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theloai` (
  `maTL` int NOT NULL AUTO_INCREMENT,
  `tenTheLoai` varchar(45) NOT NULL,
  PRIMARY KEY (`maTL`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theloai`
--

LOCK TABLES `theloai` WRITE;
/*!40000 ALTER TABLE `theloai` DISABLE KEYS */;
INSERT INTO `theloai` VALUES (1,'T-SHIRT'),(2,'POLO SHIRT');
/*!40000 ALTER TABLE `theloai` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16 17:10:13
