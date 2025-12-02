-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: inventaris_barang
-- ------------------------------------------------------
-- Server version	8.0.43

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
  `id_admin` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `id_user` (`id_user`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barang`
--

DROP TABLE IF EXISTS `barang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `barang` (
  `id_barang` int NOT NULL AUTO_INCREMENT,
  `kode_barang` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama_barang` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lokasi_barang` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `jumlah_total` int DEFAULT '0',
  `jumlah_tersedia` int DEFAULT '0',
  `deskripsi` text COLLATE utf8mb4_unicode_ci,
  `kondisi_barang` enum('baik','rusak ringan','rusak berat') COLLATE utf8mb4_unicode_ci DEFAULT 'baik',
  `status` enum('tersedia','dipinjam','rusak','hilang') COLLATE utf8mb4_unicode_ci DEFAULT 'tersedia',
  `foto_barang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_barang`),
  UNIQUE KEY `kode_barang` (`kode_barang`),
  KEY `idx_barang_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barang`
--

LOCK TABLES `barang` WRITE;
/*!40000 ALTER TABLE `barang` DISABLE KEYS */;
INSERT INTO `barang` VALUES (1,'BRG-001','Proyektor Epson','Ruang Lab 1',5,5,'Proyektor untuk presentasi','baik','tersedia','https://drive.google.com/file/d/1dYJjiFHbBYzFkETk1xwZzTAteCgwmQLa/view?usp=sharing','2025-11-15 22:19:00','2025-11-28 13:11:26'),(2,'BRG-002','Laptop Dell Latitude','Ruang Admin',10,8,'Laptop untuk mahasiswa','baik','tersedia','https://drive.google.com/file/d/1dq7wLgLu_Y1j2LT3rKrfVFq-fwC01GgW/view?usp=sharing','2025-11-15 22:19:00','2025-11-28 13:11:26'),(3,'BRG-003','Kamera Canon EOS','Ruang Media',3,3,'Kamera DSLR untuk dokumentasi','baik','tersedia','https://drive.google.com/file/d/1f8CNfSgIFEJzX0HuQLZzNI95J_gpCOhU/view?usp=sharing','2025-11-15 22:19:00','2025-11-28 13:11:26'),(4,'BRG-004','Sound System','Aula',2,2,'Sound system untuk acara','baik','tersedia','https://drive.google.com/file/d/1kqIMZsmkf7ihGygdvjitirxvChj0-rVO/view?usp=sharing','2025-11-15 22:19:00','2025-11-28 13:11:26'),(5,'BRG-005','Meja Lipat','Gudang',50,45,'Meja lipat untuk event','baik','tersedia','https://drive.google.com/file/d/1GhTMKLxKbYyRzaTHtn6O_nNjSV8q4A0W/view?usp=sharing','2025-11-15 22:19:00','2025-11-28 13:11:26'),(6,'BRG-006','Printer','Sekhim',1,1,'Print hima','baik','tersedia',NULL,'2025-11-28 08:49:43','2025-11-28 08:49:43');
/*!40000 ALTER TABLE `barang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow` (
  `id_peminjaman` int NOT NULL AUTO_INCREMENT,
  `id_peminjam` int NOT NULL,
  `id_admin` int DEFAULT NULL,
  `kode_barang` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jumlah_pinjam` int NOT NULL,
  `kondisi_barang` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tgl_peminjaman` date NOT NULL,
  `tgl_pinjam` date NOT NULL,
  `tgl_kembali` date DEFAULT NULL,
  `dl_kembali` date NOT NULL,
  `foto_pengembalian` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status_barang` enum('dipinjam','dikembalikan','hilang','rusak','pending') COLLATE utf8mb4_unicode_ci DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_peminjaman`),
  KEY `id_peminjam` (`id_peminjam`),
  KEY `id_admin` (`id_admin`),
  KEY `kode_barang` (`kode_barang`),
  KEY `idx_borrow_status` (`status_barang`),
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`id_peminjam`) REFERENCES `peminjam` (`id_peminjam`),
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id_admin`),
  CONSTRAINT `borrow_ibfk_3` FOREIGN KEY (`kode_barang`) REFERENCES `barang` (`kode_barang`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instansi`
--

DROP TABLE IF EXISTS `instansi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instansi` (
  `id_instansi` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `nama_instansi` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_instansi`),
  UNIQUE KEY `id_user` (`id_user`),
  CONSTRAINT `instansi_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instansi`
--

LOCK TABLES `instansi` WRITE;
/*!40000 ALTER TABLE `instansi` DISABLE KEYS */;
INSERT INTO `instansi` VALUES (1,4,'Badan Eksekutif Mahasiswa FIK');
/*!40000 ALTER TABLE `instansi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lapor`
--

DROP TABLE IF EXISTS `lapor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lapor` (
  `id_laporan` int NOT NULL AUTO_INCREMENT,
  `no_laporan` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_peminjaman` int NOT NULL,
  `kode_barang` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('diproses','selesai','ditolak') COLLATE utf8mb4_unicode_ci DEFAULT 'diproses',
  `tgl_laporan` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_laporan`),
  UNIQUE KEY `no_laporan` (`no_laporan`),
  KEY `id_peminjaman` (`id_peminjaman`),
  KEY `kode_barang` (`kode_barang`),
  CONSTRAINT `lapor_ibfk_1` FOREIGN KEY (`id_peminjaman`) REFERENCES `borrow` (`id_peminjaman`),
  CONSTRAINT `lapor_ibfk_2` FOREIGN KEY (`kode_barang`) REFERENCES `barang` (`kode_barang`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lapor`
--

LOCK TABLES `lapor` WRITE;
/*!40000 ALTER TABLE `lapor` DISABLE KEYS */;
/*!40000 ALTER TABLE `lapor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_activity`
--

DROP TABLE IF EXISTS `log_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_activity` (
  `id_log` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `keterangan` text COLLATE utf8mb4_unicode_ci,
  `aktifitas` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_role` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_log`),
  KEY `idx_log_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_activity`
--

LOCK TABLES `log_activity` WRITE;
/*!40000 ALTER TABLE `log_activity` DISABLE KEYS */;
INSERT INTO `log_activity` VALUES (1,'admin','Login ke sistem','LOGIN','admin','2025-11-15 22:19:00'),(2,'admin','Menambah barang baru: Proyektor Epson','CREATE_BARANG','admin','2025-11-15 22:19:00'),(3,'2345111','Login ke sistem','LOGIN','peminjam','2025-11-20 04:32:52'),(4,'GedungPutih/lembaga@upi.edu','Login ke sistem','LOGIN','instansi','2025-11-20 04:43:06'),(5,'2278900','Login ke sistem','LOGIN','peminjam','2025-11-28 10:24:05'),(6,'2504783','Login ke sistem','LOGIN','peminjam','2025-11-28 11:48:28'),(7,'2504783','Logout dari sistem','LOGOUT','peminjam','2025-11-28 11:51:01'),(8,'2278900','Logout dari sistem','LOGOUT','peminjam','2025-11-28 11:51:09'),(9,'2504783','Login ke sistem','LOGIN','peminjam','2025-11-28 12:09:58');
/*!40000 ALTER TABLE `log_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `peminjam`
--

DROP TABLE IF EXISTS `peminjam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `peminjam` (
  `id_peminjam` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `no_telepon` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_peminjam`),
  UNIQUE KEY `id_user` (`id_user`),
  CONSTRAINT `peminjam_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `peminjam`
--

LOCK TABLES `peminjam` WRITE;
/*!40000 ALTER TABLE `peminjam` DISABLE KEYS */;
INSERT INTO `peminjam` VALUES (1,2,'081234567890'),(2,3,'082345678901');
/*!40000 ALTER TABLE `peminjam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('admin','peminjam','instansi') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('aktif','nonaktif') COLLATE utf8mb4_unicode_ci DEFAULT 'aktif',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_user_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','Administrator','admin','aktif','2025-11-15 22:18:59','2025-11-15 22:18:59'),(2,'budi123','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','Budi Santoso','peminjam','aktif','2025-11-15 22:18:59','2025-11-15 22:18:59'),(3,'siti456','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','Siti Nurhaliza','peminjam','aktif','2025-11-15 22:18:59','2025-11-15 22:18:59'),(4,'bem_fik','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','BEM FIK','instansi','aktif','2025-11-15 22:19:00','2025-11-15 22:19:00'),(5,'240678','567045A','Muhammad Akbar','peminjam','aktif','2025-11-19 05:45:57','2025-11-19 05:45:57'),(6,'2387659','90854AA','Mark Lee','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(7,'2278900','0678SS23','Dasha Taran','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(8,'2589065','56789KI0','Dilraba Dilmurat','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(9,'2567339','89045QW','Hwang Mulan','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(10,'2409787','6789045','Bruno Mars','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(11,'13739497','274SBCH35','Jay','peminjam','aktif','2025-11-19 05:59:30','2025-11-19 05:59:30'),(12,'2567895','78H5G22','Bima Rexa','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(13,'2345111','45WK789','Shim Jake','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(14,'2403119','0683205','Kim Jisoo','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(15,'2402445','Hj890FG','Kim Jennie','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(16,'2402346','89G09HJ','Lalisa Manoban','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(17,'2404567','0678224','Roseanne Park','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(18,'2307856','Ji90786','Dew Jirawat Sutivanisak','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(19,'237890','98G7832','Nani Hirunkit Changkham','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(20,'2305678','34w67rt','Sky Wongravee','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(21,'2506789','OH78902','Indy Tanjararak','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(22,'2307867','Ji90OP6','Huang Renjun','peminjam','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(23,'2503118','578965A','Syaqila Zahra','peminjam','aktif','2025-11-19 05:50:25','2025-11-19 05:50:25'),(24,'2378909','98F7992','Nailra Al-Hambra','peminjam','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(25,'2305655','34667Yt','Jean Alexandria','peminjam','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(26,'2506783','OH78Y72','Watanabe Haruto','peminjam','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(27,'2406783','OH77890','Yoshinori Kanemoto','peminjam','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(28,'2207967','J32WEP6','Elzhard Rahadian Askara','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(29,'2438297','377483GB','Muhammad Zayyan','peminjam','aktif','2025-11-19 05:59:30','2025-11-19 05:59:30'),(30,'2278999','9HG8992','Erlind Haaland','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(31,'2206783','OH78Y99','Lee Sangyeon','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(32,'2245783','9067890','Kevin Moon','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(33,'2267673','KL90770','Eric Sohn','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(34,'2504783','H890890','Katarina','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(35,'2289783','7H9J890','Winter Alexandria','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(36,'2505655','346678H','Zhang Linghe','peminjam','aktif','2025-11-19 14:42:04','2025-11-19 14:42:04'),(37,'2590709','078H67H','Nailoong','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(38,'2595689','056HJ90','Bombloong','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(39,'2490709','89H0K0V','Muhammad Al-Fatih','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(40,'2466609','23GH7DF','Nafila Ajmal','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(41,'2431709','H8C6B77','Arya Adimanggala','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(42,'2489709','X455678','Annisa Zulfa','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(43,'2412399','456GHJK','Reifana Al-Kindi','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(44,'2456789','90KLBNM','Yasir Ahmadin','peminjam','aktif','2025-11-20 02:10:10','2025-11-20 02:10:10'),(56,'0273846','143637AG6','Dita Karang','peminjam','aktif','2025-11-19 05:59:30','2025-11-19 05:59:30'),(80,'78307890','AS347408','Carmenita','peminjam','aktif','2025-11-19 05:59:30','2025-11-19 05:59:30'),(89,'2503678','5764865A','Syaqila Amara','peminjam','aktif','2025-11-19 05:59:30','2025-11-19 05:59:30'),(98,'2245675','567GI87','Maxwell Elsavador','peminjam','aktif','2025-11-19 13:03:39','2025-11-19 13:03:39'),(900,'HIMATRONIKA-AI/himpunan@upi.edu','WE345RT9','HIMATRONIKA-AI','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(901,'HIMAPSTI/himpunan@upi.edu','12W6Y89H','HIMA PSTI','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(902,'HIMAUDI/himpunan@upi.edu','56G9KK90','HIMA-UDI','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(903,'HIMAPGSD/himpunan@upi.edu','F67G990K','HIMA PGSD','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(904,'HMST/himpunan@upi.edu','23DF5T6H','HMST','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(905,'LDKFokusSalam/ukm@upi.edu','56D4KIOP','LDK UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(906,'KUBUS/ukm@upi.edu','89KL90OP','KUBUS UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(907,'LEPPIM/ukm@upi.edu','H896G75R','LEPPIM UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(908,'ISOLA/ukm@upi.edu','FT6G7B8N','ISOLA UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(909,'KSR/ukm@upi.edu','90KHUPD5','KSR UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(910,'BEMREMAUPIPWK/ormawa@upi.edu','HJKLFG58','BEM RWM UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09'),(1000,'GedungPutih/lembaga@upi.edu','#@HI89KL','Lembaga UPI PWK','instansi','aktif','2025-11-19 14:28:09','2025-11-19 14:28:09');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_foto_barang`
--

DROP TABLE IF EXISTS `v_foto_barang`;
/*!50001 DROP VIEW IF EXISTS `v_foto_barang`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_foto_barang` AS SELECT 
 1 AS `id_barang`,
 1 AS `nama_barang`,
 1 AS `foto_barang`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_peminjaman_aktif`
--

DROP TABLE IF EXISTS `v_peminjaman_aktif`;
/*!50001 DROP VIEW IF EXISTS `v_peminjaman_aktif`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_peminjaman_aktif` AS SELECT 
 1 AS `id_peminjaman`,
 1 AS `nama_peminjam`,
 1 AS `no_telepon`,
 1 AS `kode_barang`,
 1 AS `nama_barang`,
 1 AS `jumlah_pinjam`,
 1 AS `tgl_pinjam`,
 1 AS `dl_kembali`,
 1 AS `sisa_hari`,
 1 AS `status_barang`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_statistik_barang`
--

DROP TABLE IF EXISTS `v_statistik_barang`;
/*!50001 DROP VIEW IF EXISTS `v_statistik_barang`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_statistik_barang` AS SELECT 
 1 AS `kode_barang`,
 1 AS `nama_barang`,
 1 AS `jumlah_total`,
 1 AS `jumlah_tersedia`,
 1 AS `jumlah_dipinjam`,
 1 AS `status`,
 1 AS `kondisi_barang`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_foto_barang`
--

DROP TABLE IF EXISTS `vw_foto_barang`;
/*!50001 DROP VIEW IF EXISTS `vw_foto_barang`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_foto_barang` AS SELECT 
 1 AS `id_barang`,
 1 AS `nama_barang`,
 1 AS `foto_barang`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_foto_barang_update`
--

DROP TABLE IF EXISTS `vw_foto_barang_update`;
/*!50001 DROP VIEW IF EXISTS `vw_foto_barang_update`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_foto_barang_update` AS SELECT 
 1 AS `id_barang`,
 1 AS `foto_lama`,
 1 AS `foto_baru`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_foto_barang`
--

/*!50001 DROP VIEW IF EXISTS `v_foto_barang`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_foto_barang` AS select `barang`.`id_barang` AS `id_barang`,`barang`.`nama_barang` AS `nama_barang`,`barang`.`foto_barang` AS `foto_barang` from `barang` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_peminjaman_aktif`
--

/*!50001 DROP VIEW IF EXISTS `v_peminjaman_aktif`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_peminjaman_aktif` AS select `b`.`id_peminjaman` AS `id_peminjaman`,`u`.`nama` AS `nama_peminjam`,`p`.`no_telepon` AS `no_telepon`,`br`.`kode_barang` AS `kode_barang`,`br`.`nama_barang` AS `nama_barang`,`b`.`jumlah_pinjam` AS `jumlah_pinjam`,`b`.`tgl_pinjam` AS `tgl_pinjam`,`b`.`dl_kembali` AS `dl_kembali`,(to_days(`b`.`dl_kembali`) - to_days(curdate())) AS `sisa_hari`,`b`.`status_barang` AS `status_barang` from (((`borrow` `b` join `peminjam` `p` on((`b`.`id_peminjam` = `p`.`id_peminjam`))) join `user` `u` on((`p`.`id_user` = `u`.`id_user`))) join `barang` `br` on((`b`.`kode_barang` = `br`.`kode_barang`))) where (`b`.`status_barang` = 'dipinjam') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_statistik_barang`
--

/*!50001 DROP VIEW IF EXISTS `v_statistik_barang`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_statistik_barang` AS select `barang`.`kode_barang` AS `kode_barang`,`barang`.`nama_barang` AS `nama_barang`,`barang`.`jumlah_total` AS `jumlah_total`,`barang`.`jumlah_tersedia` AS `jumlah_tersedia`,(`barang`.`jumlah_total` - `barang`.`jumlah_tersedia`) AS `jumlah_dipinjam`,`barang`.`status` AS `status`,`barang`.`kondisi_barang` AS `kondisi_barang` from `barang` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_foto_barang`
--

/*!50001 DROP VIEW IF EXISTS `vw_foto_barang`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_foto_barang` AS select `barang`.`id_barang` AS `id_barang`,`barang`.`nama_barang` AS `nama_barang`,`barang`.`foto_barang` AS `foto_barang` from `barang` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_foto_barang_update`
--

/*!50001 DROP VIEW IF EXISTS `vw_foto_barang_update`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_foto_barang_update` AS select `barang`.`id_barang` AS `id_barang`,`barang`.`foto_barang` AS `foto_lama`,(case when (`barang`.`id_barang` = 1) then 'https://drive.google.com/file/d/1dYJjiFHbBYzFkETk1xwZzTAteCgwmQLa/view?usp=sharing' when (`barang`.`id_barang` = 2) then 'https://drive.google.com/file/d/1dq7wLgLu_Y1j2LT3rKrfVFq-fwC01GgW/view?usp=sharing' when (`barang`.`id_barang` = 3) then 'https://drive.google.com/file/d/1f8CNfSgIFEJzX0HuQLZzNI95J_gpCOhU/view?usp=sharing' when (`barang`.`id_barang` = 4) then 'https://drive.google.com/file/d/1kqIMZsmkf7ihGygdvjitirxvChj0-rVO/view?usp=sharing' when (`barang`.`id_barang` = 5) then 'https://drive.google.com/file/d/1GhTMKLxKbYyRzaTHtn6O_nNjSV8q4A0W/view?usp=sharing' else `barang`.`foto_barang` end) AS `foto_baru` from `barang` where (`barang`.`id_barang` in (1,2,3,4,5)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-28 21:17:30
