-- MySQL dump 10.13  Distrib 8.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: stylefinder
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `closet`
--

DROP TABLE IF EXISTS `closet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `closet` (
  `closet_id` bigint NOT NULL AUTO_INCREMENT,
  `closet_category` varchar(255) DEFAULT NULL,
  `closet_details` varchar(255) DEFAULT NULL,
  `closet_image` varchar(255) DEFAULT NULL,
  `closet_part` enum('dress','lowerBody','outerCloth','upperBody') DEFAULT NULL,
  `closet_textures` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`closet_id`),
  KEY `FKgnd6pbba358v4ugkal18glwf6` (`user_id`),
  CONSTRAINT `FKgnd6pbba358v4ugkal18glwf6` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closet`
--

LOCK TABLES `closet` WRITE;
/*!40000 ALTER TABLE `closet` DISABLE KEYS */;
INSERT INTO `closet` VALUES (6,'셔츠','디스트로이드','CLOSET/7b3c78fb-c649-4765-bd26-3364d344da15.jpg','upperBody','패딩',5),(12,'재킷','단추','CLOSET/5defccf5-c677-4693-8657-11af4bc9a46a.jfif','upperBody','우븐',2),(17,'티셔츠','슬릿','CLOSET/18aae6a4-2eaf-44c1-8fee-cf08801d5205.jpg','dress','니트',5),(18,'티셔츠','드롭숄더','CLOSET/10a1c03f-20c9-4b22-a7f5-269578dfccba.jpg','upperBody','저지,우븐',5),(19,'티셔츠','디스트로이드','CLOSET/1e13cc48-d7b6-4b52-a6bd-54827c30d250.jpg','upperBody','저지',5),(20,'티셔츠','드롭숄더','CLOSET/65de879c-b58c-4d93-8e65-ef0964894555.jpg','upperBody','저지',5),(21,'팬츠','포켓','CLOSET/470dd58f-2dd7-4f06-9016-5eae1b9ef31c.jpg','upperBody','우븐',5),(29,'티셔츠','드롭숄더','CLOSET/e7122e34-d348-4437-94f6-8766fa28fae9.jpg','upperBody','저지',2),(30,'티셔츠','드롭숄더','CLOSET/d456e024-2028-4314-95d8-04f6632593c1.png','lowerBody','저지',5),(33,'티셔츠','단추','CLOSET/93af85d3-d2fb-4dd7-94a8-eb0a08691a9c.jpg','upperBody','우븐',3),(34,'블라우스','스트링','CLOSET/92525f7f-1759-488b-a31c-70cfee40a596.jpg','upperBody','저지,우븐',3),(35,'티셔츠','스트링','CLOSET/1626da92-04dc-4ef0-9b38-70928b406aaa.jpg','lowerBody','우븐',3),(36,'팬츠','슬릿','CLOSET/5463563c-f4cd-434a-9193-377de04b2390.jpg','lowerBody','우븐',3),(37,'티셔츠','드롭숄더','CLOSET/3bfb1608-1ac7-4dfd-b48f-914ef1adeaf6.jpg','outerCloth','저지',3),(38,'티셔츠','디스트로이드','CLOSET/d7611bae-6a80-435d-a42e-1571f7bbbf3c.jpg','dress','저지',5),(39,'티셔츠','자수','CLOSET/c30f6e9b-6b36-4061-af29-2872b8fc5acf.jpg','upperBody','우븐',10),(40,'재킷','단추','CLOSET/ad71961b-91df-431c-978d-ed6411a69a37.jfif','upperBody','우븐',1);
/*!40000 ALTER TABLE `closet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `comment_content` varchar(255) NOT NULL,
  `comment_created_date` date DEFAULT NULL,
  `comment_updated_date` date DEFAULT NULL,
  `feed_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKmq57ocw5jrw8rd2lot1g8t0v2` (`feed_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKmq57ocw5jrw8rd2lot1g8t0v2` FOREIGN KEY (`feed_id`) REFERENCES `feed` (`feed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'엥 저런 옷 어디서 삼? 왜 삼?ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ','2024-04-03','2024-04-03',1,6),(2,'진평동 사냐? 나와라','2024-04-03','2024-04-03',1,2),(3,'야야','2024-04-03','2024-04-03',1,2),(4,'대답하라고','2024-04-03','2024-04-03',1,2),(5,'하...','2024-04-03','2024-04-03',1,2),(6,' 응 안나가죠? 아무고토 못하쥬?','2024-04-03','2024-04-03',1,6),(7,'힝...','2024-04-03','2024-04-03',1,2),(8,'불주먹 무서워...','2024-04-03','2024-04-03',1,2),(9,'드레스 위에 아우터... 맞나요?','2024-04-03','2024-04-03',4,2),(10,'댓글작성','2024-04-03','2024-04-03',4,5),(11,'댓글','2024-04-03','2024-04-03',5,5),(12,'댓글','2024-04-03','2024-04-03',5,5),(13,'댓글','2024-04-03','2024-04-03',5,5),(14,'댓글','2024-04-03','2024-04-03',5,5),(15,'댓글','2024-04-03','2024-04-03',5,5),(16,'댓글','2024-04-03','2024-04-03',5,5),(17,'아몰랑','2024-04-03','2024-04-03',4,5),(18,'해줘~~','2024-04-03','2024-04-03',4,5),(19,'해줘ㅓㅓㅓㅓㅓ','2024-04-03','2024-04-03',4,5),(20,'댓글','2024-04-03','2024-04-03',1,5),(21,'댓글','2024-04-03','2024-04-03',1,5),(22,'soso 슴슴하네요; 애국룩인가요?','2024-04-03','2024-04-03',7,8),(23,'분홍 드레스 위에 빨간 니트? 흠;','2024-04-03','2024-04-03',4,8),(24,'장금이요','2024-04-03','2024-04-03',5,8),(25,'그 빨간 원피스 입고 많이 드세요','2024-04-03','2024-04-03',6,8),(26,'나쁜 말 하지 마세요','2024-04-03','2024-04-03',9,8),(27,'응애 나 아기에바 피카츄 잡아줘','2024-04-03','2024-04-03',8,8),(28,'ㄷㄷ 붐따 드립니다','2024-04-03','2024-04-03',10,8),(29,'와..... 좋네요.... 물론 옷이요','2024-04-03','2024-04-03',12,8),(30,'이 사이트 왜 사용하세요?','2024-04-03','2024-04-03',13,7),(31,'↑ 그건 제가 알아서 할개요','2024-04-03','2024-04-03',13,8),(32,'아니 생각하니까 어이가 없네','2024-04-03','2024-04-03',13,8),(33,'ㅋㅋㅋㅋ 아 오랜만에 웃고 갑니다','2024-04-03','2024-04-03',12,7),(34,'패딩 하나만 입는게 그게 그렇게 이해가 안되나요?','2024-04-03','2024-04-03',13,8),(35,'패딩 하나만 입는 사람은 이 사이트 이용하면 안되나요?','2024-04-03','2024-04-03',13,8),(36,'아니 진짜 기가 차네','2024-04-03','2024-04-03',13,8),(37,'저기요 휴대폰 번호좀요','2024-04-03','2024-04-03',13,8),(38,'번호따는 거 아니고','2024-04-03','2024-04-03',13,8),(39,'기분나빠서 그럽니다','2024-04-03','2024-04-03',13,8),(40,'저기요','2024-04-03','2024-04-03',13,8),(41,'빨리 번호 알려주세요','2024-04-03','2024-04-03',13,8),(42,'번호 아니면 집 주소라도요','2024-04-03','2024-04-03',13,8),(43,'선물 좀 드리려고 합니다.','2024-04-03','2024-04-03',13,8),(44,'ㅋㅋ ㅋㅋ ㅋㅋ 쫄?','2024-04-03','2024-04-03',13,8),(45,'쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?쫄?','2024-04-03','2024-04-03',13,8),(46,'와 너무 에뻐요~~~','2024-04-03','2024-04-03',15,8),(47,'상의 어디서 사셨나요? 정보좀요 ㅎ','2024-04-03','2024-04-03',15,8),(48,'댓글','2024-04-03','2024-04-03',17,5),(49,'멋진 블루블루 블루클럽이네요!','2024-04-03','2024-04-03',19,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feed` (
  `feed_id` bigint NOT NULL AUTO_INCREMENT,
  `coordi_id` varchar(255) NOT NULL,
  `dress` varchar(255) DEFAULT NULL,
  `feed_content` varchar(255) NOT NULL,
  `feed_created_date` date NOT NULL,
  `feed_likes` bigint DEFAULT NULL,
  `feed_title` varchar(50) NOT NULL,
  `feed_updated_date` date NOT NULL,
  `lower_body` varchar(255) DEFAULT NULL,
  `origin_writer` bigint DEFAULT NULL,
  `outer_cloth` varchar(255) DEFAULT NULL,
  `upper_body` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`feed_id`),
  KEY `FKeupe1ba7u2e7sr6r3fa4dhdo7` (`user_id`),
  CONSTRAINT `FKeupe1ba7u2e7sr6r3fa4dhdo7` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed`
--

LOCK TABLES `feed` WRITE;
/*!40000 ALTER TABLE `feed` DISABLE KEYS */;
INSERT INTO `feed` VALUES (1,'660cf2b1eee20c7caca72e3d','big_date_image/61933.jpg','코디 쉽네ㅋ','2024-04-03',3,'이런 코디 어때요?','2024-04-03',NULL,2,NULL,'big_date_image/485388.jpg',2),(4,'660d403e88195e0fcac06e85','big_date_image/792106.jpg','나랑 놀러갈 사람 어디 없나~?','2024-04-03',2,'오늘 날씨 좋네요','2024-04-03','big_date_image/1290047.jpg',3,'big_date_image/691070.jpg','CLOSET/3b63f093-47da-404d-8395-2e241f6f6450.jpg',3),(5,'660d41c588195e0fcac06e86','big_date_image/1203328.jpg','이터널선샤인 같은? 그런거요','2024-04-03',4,'사극 재밌는거 없나요','2024-04-03','big_date_image/1213115.jpg',5,'big_date_image/277139.jpg',NULL,5),(6,'660d47ac88195e0fcac06e87','big_date_image/775918.jpg','화로구이 무한리필 갈거에요','2024-04-03',1,'회식 어디로 갈까요?','2024-04-03','big_date_image/753792.jpg',5,'big_date_image/1221837.jpg','big_date_image/708891.jpg',5),(7,'660d53c68f4a9054997684b9',NULL,'ㅋ 평가할 자격이 있다면?ㅋ','2024-04-03',0,'코디 평가 좀 해주세요...','2024-04-03','big_date_image/1268102.jpg',1,'big_date_image/1246212.jpg','CLOSET/3b63f093-47da-404d-8395-2e241f6f6450.jpg',1),(8,'660d54a888195e0fcac06e88','big_date_image/454726.jpg','전 안해요ㅋ 애들 게임ㅋ','2024-04-03',0,'포켓몬 고 하시면 분 있으세요?','2024-04-03','big_date_image/495741.jpg',5,'big_date_image/767942.jpg','big_date_image/183567.jpg',5),(9,'660d606fbe8196387de0501f','big_date_image/544433.jpg','히히 그냥 어그로 끌고 싶었어요','2024-04-03',0,'이야 게시판 개판이네','2024-04-03','big_date_image/486405.jpg',5,'big_date_image/7365.jpg','big_date_image/486131.jpg',5),(10,'660d6500be8196387de05020',NULL,'상하의 상하의 상하의 트위스티드페이트 춤을 추면서~','2024-04-03',0,'상하의 조합 괜찮나요?','2024-04-03','big_date_image/517463.jpg',7,NULL,'big_date_image/251472.jpg',7),(11,'660d674ebe8196387de05021','big_date_image/240541.jpg','번따룩이라고 해두죠','2024-04-03',0,'이렇게 입고 번호 따였습니다.','2024-04-03','big_date_image/1136498.jpg',8,'big_date_image/773869.jpg','big_date_image/578271.jpg',8),(12,'660d6754be8196387de05022',NULL,'우유를 많이 먹어서 탈이 난 사람에게 이런 말을 건네보면 어떨까요? \"과우유불급\"','2024-04-03',2,'오늘의 한 줄','2024-04-03','big_date_image/522649.jpg',9,'big_date_image/958988.jpg','big_date_image/1096726.jpg',9),(13,'660d68a7be8196387de05023',NULL,'길이가 길어서 다 가려집니다. 아무도 몰라요.','2024-04-03',0,'전 귀찮으면 이거 하나만 입습니다.','2024-04-03',NULL,8,'big_date_image/1247063.jpg',NULL,8),(14,'660d6b6a1672fa70f9976b14','big_date_image/972210.jpg','이상한 옷 패딩으로 가리기','2024-04-03',0,'패션의 새로운 패러다임','2024-04-03',NULL,7,'big_date_image/1247063.jpg',NULL,7),(15,'660d6ccfbe8196387de05024','big_date_image/61933.jpg','아무래도 이런 할머니조끼를 걸쳐주는게 더 쌈뽕나죠 ㅋ 하의는 뭐 안입는게 제일 베스트긴 하지만 깜장바지도 낫배드죠','2024-04-03',1,'바다님 코디 좋긴한데','2024-04-03','big_date_image/987981.jpg',8,'big_date_image/415109.jpg','big_date_image/485388.jpg',8),(16,'660d784ebe8196387de05025','big_date_image/998729.jpg','멋집니다~','2024-04-03',0,'멋진 옷','2024-04-03','big_date_image/1270818.jpg',10,'big_date_image/1257085.jpg','big_date_image/1087266.jpg',10),(17,'660d7bf8a739371f4d55896b','big_date_image/349378.jpg','피드 내용','2024-04-03',1,'피드 제목','2024-04-03','big_date_image/287673.jpg',5,'big_date_image/127751.jpg','big_date_image/48755.jpg',5),(18,'660d7cfaa739371f4d55896c','big_date_image/796933.jpg','내용','2024-04-03',0,'제목','2024-04-03','big_date_image/56011.jpg',5,'big_date_image/366927.jpg','big_date_image/1176827.jpg',5),(19,'660d7d30a739371f4d55896d','big_date_image/349378.jpg','ㅇㅇㅇ','2024-04-03',1,'아앙ㅇㅇ','2024-04-03','big_date_image/287673.jpg',5,'big_date_image/127751.jpg','big_date_image/48755.jpg',5);
/*!40000 ALTER TABLE `feed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NOT NULL,
  `orig_file_name` varchar(255) NOT NULL,
  `stored_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'PROFILE/deea1b38-82f9-43ee-bec5-29b01e4736e9.jpg','main3.jpg','deea1b38-82f9-43ee-bec5-29b01e4736e9.jpg'),(2,'CLOSET/271bcbc9-6ce5-4247-907a-101329b50df0.jpg','B.jpg','271bcbc9-6ce5-4247-907a-101329b50df0.jpg'),(3,'CLOSET/4c8e425f-63f6-4c0a-b6c8-50311c44f3ad.jpg','B.jpg','4c8e425f-63f6-4c0a-b6c8-50311c44f3ad.jpg'),(4,'PROFILE/52096d8d-bd29-4539-9817-fb5c6b25abf7.jpg','profile4.jpg','52096d8d-bd29-4539-9817-fb5c6b25abf7.jpg'),(5,'CLOSET/3b63f093-47da-404d-8395-2e241f6f6450.jpg','lee.jpg','3b63f093-47da-404d-8395-2e241f6f6450.jpg'),(6,'CLOSET/1831f289-8635-4b14-9c34-1c63c654b178.jpg','lee.jpg','1831f289-8635-4b14-9c34-1c63c654b178.jpg'),(7,'CLOSET/26235b16-bad6-40b2-a297-c46eb323f706.jpg','lee.jpg','26235b16-bad6-40b2-a297-c46eb323f706.jpg'),(8,'CLOSET/7b3c78fb-c649-4765-bd26-3364d344da15.jpg','sample5.jpg','7b3c78fb-c649-4765-bd26-3364d344da15.jpg'),(9,'CLOSET/bb26f89a-5bf8-4aa5-a87a-a5743a46c0ad.jpg','sample5.jpg','bb26f89a-5bf8-4aa5-a87a-a5743a46c0ad.jpg'),(10,'CLOSET/80f628d2-d5cc-4cf4-b77e-8d70b67a4a21.jpg','sample5.jpg','80f628d2-d5cc-4cf4-b77e-8d70b67a4a21.jpg'),(11,'CLOSET/c527e106-2cec-4158-924f-3ffd4d5c41d8.jpg','sample5.jpg','c527e106-2cec-4158-924f-3ffd4d5c41d8.jpg'),(12,'CLOSET/6f98477b-2e74-4e03-a79c-b061cfde928a.jpg','sample5.jpg','6f98477b-2e74-4e03-a79c-b061cfde928a.jpg'),(13,'CLOSET/cdaa6fd3-14cd-4d0b-addc-6603af997d32.jpg','sample5.jpg','cdaa6fd3-14cd-4d0b-addc-6603af997d32.jpg'),(14,'CLOSET/5defccf5-c677-4693-8657-11af4bc9a46a.jfif','다운로드 (3).jfif','5defccf5-c677-4693-8657-11af4bc9a46a.jfif'),(15,'PROFILE/45ecdc0a-7895-490f-ad05-39e246a032c8.jfif','다운로드.jfif','45ecdc0a-7895-490f-ad05-39e246a032c8.jfif'),(16,'CLOSET/ed243f2f-c549-482e-b9d6-448a29c8aafa.jpg','main3.jpg','ed243f2f-c549-482e-b9d6-448a29c8aafa.jpg'),(17,'CLOSET/18aae6a4-2eaf-44c1-8fee-cf08801d5205.jpg','main3.jpg','18aae6a4-2eaf-44c1-8fee-cf08801d5205.jpg'),(18,'CLOSET/d5155537-fd79-4a60-9aae-b845654fdb54.jpg','main3.jpg','d5155537-fd79-4a60-9aae-b845654fdb54.jpg'),(19,'CLOSET/a3aa94ad-31aa-487a-8394-f7a935f656ff.jpg','main3.jpg','a3aa94ad-31aa-487a-8394-f7a935f656ff.jpg'),(20,'CLOSET/7c7cfe02-c0b8-4eb6-8406-6ddba71750c2.jpg','main3.jpg','7c7cfe02-c0b8-4eb6-8406-6ddba71750c2.jpg'),(21,'CLOSET/10a1c03f-20c9-4b22-a7f5-269578dfccba.jpg','main7.jpg','10a1c03f-20c9-4b22-a7f5-269578dfccba.jpg'),(22,'CLOSET/1e13cc48-d7b6-4b52-a6bd-54827c30d250.jpg','main6.jpg','1e13cc48-d7b6-4b52-a6bd-54827c30d250.jpg'),(23,'CLOSET/65de879c-b58c-4d93-8e65-ef0964894555.jpg','main8.jpg','65de879c-b58c-4d93-8e65-ef0964894555.jpg'),(24,'CLOSET/470dd58f-2dd7-4f06-9016-5eae1b9ef31c.jpg','main4.jpg','470dd58f-2dd7-4f06-9016-5eae1b9ef31c.jpg'),(25,'PROFILE/c80d52cd-6ef2-4b2d-bcc3-a194c1c3850a.jpg','chalo-garcia-Ep4GNix-Ptc-unsplash.jpg','c80d52cd-6ef2-4b2d-bcc3-a194c1c3850a.jpg'),(26,'PROFILE/06ac95ef-d5f3-476f-9410-7725b862140c.jpg','chalo-garcia-Ep4GNix-Ptc-unsplash.jpg','06ac95ef-d5f3-476f-9410-7725b862140c.jpg'),(27,'CLOSET/6d8b0a18-8799-4007-a5e0-6d413904757d.jpg','chalo-garcia-Ep4GNix-Ptc-unsplash.jpg','6d8b0a18-8799-4007-a5e0-6d413904757d.jpg'),(28,'CLOSET/2f1d35df-7048-430f-8fde-15ce448f13b7.jpg','chalo-garcia-Ep4GNix-Ptc-unsplash.jpg','2f1d35df-7048-430f-8fde-15ce448f13b7.jpg'),(29,'CLOSET/03bdf81a-f8a5-4397-a4eb-bbabf6260241.jpg','lee.jpg','03bdf81a-f8a5-4397-a4eb-bbabf6260241.jpg'),(30,'CLOSET/4d91370c-1d58-46f6-9a23-9043fc7e1905.jpg','adele-shafiee-vagr_XT9Cms-unsplash.jpg','4d91370c-1d58-46f6-9a23-9043fc7e1905.jpg'),(31,'CLOSET/f55722bc-1e2f-49b5-a30d-8c594476a894.jpg','engin-akyurt-jaZoffxg1yc-unsplash.jpg','f55722bc-1e2f-49b5-a30d-8c594476a894.jpg'),(32,'CLOSET/640c289f-56f6-4fa1-82af-59ed700e7e41.jpg','helen-ast-nSmkZ4AfL2M-unsplash.jpg','640c289f-56f6-4fa1-82af-59ed700e7e41.jpg'),(33,'CLOSET/59165789-4e69-4ac0-952d-7cc43efb6972.jpg','katsiaryna-endruszkiewicz-BteCp6aq4GI-unsplash.jpg','59165789-4e69-4ac0-952d-7cc43efb6972.jpg'),(34,'CLOSET/e7122e34-d348-4437-94f6-8766fa28fae9.jpg','katsiaryna-endruszkiewicz-BteCp6aq4GI-unsplash.jpg','e7122e34-d348-4437-94f6-8766fa28fae9.jpg'),(35,'CLOSET/d456e024-2028-4314-95d8-04f6632593c1.png','main2.png','d456e024-2028-4314-95d8-04f6632593c1.png'),(36,'PROFILE/b1c3bdc9-4e63-4e84-a326-e889c01ae857.jpg','profile1.jpg','b1c3bdc9-4e63-4e84-a326-e889c01ae857.jpg'),(37,'PROFILE/9abf7954-a1cc-49eb-bf40-cb9bde672765.jpg','profile1.jpg','9abf7954-a1cc-49eb-bf40-cb9bde672765.jpg'),(38,'CLOSET/740104f8-0e51-4aea-86c2-952325423864.jpg','main8.jpg','740104f8-0e51-4aea-86c2-952325423864.jpg'),(39,'CLOSET/d060e9f2-2031-49e1-9de6-8a44234bb4e4.jpg','main6.jpg','d060e9f2-2031-49e1-9de6-8a44234bb4e4.jpg'),(40,'PROFILE/c9c53176-adcd-4001-b6dc-af8cce4401d2.png','ce4f2895-fc05-4ffe-ad9c-478fbba9f706.png','c9c53176-adcd-4001-b6dc-af8cce4401d2.png'),(41,'CLOSET/93af85d3-d2fb-4dd7-94a8-eb0a08691a9c.jpg','lee.jpg','93af85d3-d2fb-4dd7-94a8-eb0a08691a9c.jpg'),(42,'PROFILE/fd04d713-464f-4854-b7fc-7441472a70c1.webp','1안.webp','fd04d713-464f-4854-b7fc-7441472a70c1.webp'),(43,'CLOSET/92525f7f-1759-488b-a31c-70cfee40a596.jpg','B.jpg','92525f7f-1759-488b-a31c-70cfee40a596.jpg'),(44,'CLOSET/1626da92-04dc-4ef0-9b38-70928b406aaa.jpg','예쁜치마바지8.jpg','1626da92-04dc-4ef0-9b38-70928b406aaa.jpg'),(45,'CLOSET/5463563c-f4cd-434a-9193-377de04b2390.jpg','168520913485854.jpg','5463563c-f4cd-434a-9193-377de04b2390.jpg'),(46,'CLOSET/3bfb1608-1ac7-4dfd-b48f-914ef1adeaf6.jpg','730808c09db24ff09c6b6d6f529909da_20211027173133.jpg','3bfb1608-1ac7-4dfd-b48f-914ef1adeaf6.jpg'),(47,'CLOSET/d7611bae-6a80-435d-a42e-1571f7bbbf3c.jpg','main6.jpg','d7611bae-6a80-435d-a42e-1571f7bbbf3c.jpg'),(48,'CLOSET/c30f6e9b-6b36-4061-af29-2872b8fc5acf.jpg','셔츠.jpg','c30f6e9b-6b36-4061-af29-2872b8fc5acf.jpg'),(49,'CLOSET/ad71961b-91df-431c-978d-ed6411a69a37.jfif','다운로드 (3).jfif','ad71961b-91df-431c-978d-ed6411a69a37.jfif');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `likes_id` bigint NOT NULL AUTO_INCREMENT,
  `feed_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`likes_id`),
  KEY `FK73m9gw0xtqh2a34inbx5hbsrm` (`feed_id`),
  KEY `FKi2wo4dyk4rok7v4kak8sgkwx0` (`user_id`),
  CONSTRAINT `FK73m9gw0xtqh2a34inbx5hbsrm` FOREIGN KEY (`feed_id`) REFERENCES `feed` (`feed_id`) ON DELETE CASCADE,
  CONSTRAINT `FKi2wo4dyk4rok7v4kak8sgkwx0` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (13,1,2),(15,4,5),(16,5,5),(18,4,3),(21,5,3),(23,5,1),(26,1,5),(27,5,9),(28,6,8),(29,12,7),(30,1,8),(31,15,8),(33,12,1),(34,17,5),(35,19,1);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommend`
--

DROP TABLE IF EXISTS `recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommend` (
  `recommend_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`recommend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommend`
--

LOCK TABLES `recommend` WRITE;
/*!40000 ALTER TABLE `recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sample` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sample_column` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample`
--

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;
/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_dislike_categories` varchar(255) DEFAULT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_height` int DEFAULT NULL,
  `user_instagram` varchar(255) DEFAULT NULL,
  `user_introduce` varchar(255) DEFAULT NULL,
  `user_like_categories` varchar(255) DEFAULT NULL,
  `user_nickname` varchar(50) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_profile_image` varchar(255) DEFAULT NULL,
  `user_weight` int DEFAULT NULL,
  `user_youtube` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_j09k2v8lxofv2vecxu2hde9so` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'','kia54682@gmail.com',173,NULL,'코디를 좋아하는 코더입니다.','블라우스,청바지','코디코더','$2a$10$9qDF6sGtmn0p/aZRqXWiYe6FfCJdUtz5fNrSowRAoUbOS54vRfVX2','PROFILE/06ac95ef-d5f3-476f-9410-7725b862140c.jpg',173,NULL),(2,'','kia546@gmail.com',173,NULL,'','코트,점프수트,청바지','안녕바다','$2a$10$ehc0ghAKEnOOP9zqi2vqFev/G1NwQ43SFEXHSR4p42.VAWCL3C2b6','PROFILE/45ecdc0a-7895-490f-ad05-39e246a032c8.jfif',173,NULL),(3,'','testuser1@gmail.com',164,'','','','테스트유저','$2a$10$jdqlr9AP8vlIDPh5pQWtfeD2THaIlCx9ef1efUjSUb60Qd7/KaAOK','PROFILE/defaultProfileImage.jpeg',53,''),(5,'','se03013@naver.com',173,NULL,'피드내용변경','니트웨어,베스트,스커트,점프수트','zhzh','$2a$10$/0LBFqnEBhBO0x7lJj6l1.WsJswEd9/JYpSgjnKQ0vMOYG.BBSIr6','PROFILE/deea1b38-82f9-43ee-bec5-29b01e4736e9.jpg',68,NULL),(6,'','testuser2@gmail.com',174,'','','브라탑,블라우스,페딩','진평동불주먹','$2a$10$7hq3VtQPkz5ne4TI2v.MB.H95VuYyC6qkV./KlFVD8FAdQwyMohFG','PROFILE/52096d8d-bd29-4539-9817-fb5c6b25abf7.jpg',38,''),(7,'','kimkj546@gmail.com',133,NULL,'','탑','나는나빠','$2a$10$XKdr2x1NZDPFMfHFfkC4Q.s4gUx7g5CueFM.lF.qhYN/rAoGFu5tq','PROFILE/fd04d713-464f-4854-b7fc-7441472a70c1.webp',56,NULL),(8,'','jejin5718@gmail.com',198,NULL,'저 옷 잘 입는 편인데\n다들 보고 배우세요','','김에바','$2a$10$szrhquiijS7yPN7JTZHy6OpQq4lkXrcMLtb.51OfjegH8pqQpL6JO','PROFILE/9abf7954-a1cc-49eb-bf40-cb9bde672765.jpg',22,NULL),(9,'','testuser@gmail.com',158,NULL,'','니트웨어','스노우맨','$2a$10$ZUGEc1/km1rbyZs388U.YeYC1vq8o9ULHW.62jxluwWC1r0x8aVya','PROFILE/c9c53176-adcd-4001-b6dc-af8cce4401d2.png',43,NULL),(10,'','testuser3@gmail.com',198,'','','','테튜','$2a$10$DLpexFwJGnNMzJIx9O5qru6fYo8UaR4NKvRdCQXTbmMv64LmVoNk6','PROFILE/defaultProfileImage.jpeg',18,'');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-03 17:03:54
