CREATE DATABASE IF NOT EXISTS `CraftShop`;

USE `CraftShop`;

--
-- Table structure for table `Customers`
--

DROP TABLE IF EXISTS `Customers`;
CREATE TABLE `Customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(128) NOT NULL,
  `firstName` varchar(128) NOT NULL,
  `lastName` varchar(128) NOT NULL,
  `createDate` datetime NOT NULL DEFAULT (utc_date()),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2033 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Seed some things

INSERT INTO Customers (email, firstName, lastName)
VALUES ('shopper1@gmailer.com', 'Shopper', 'Smith'),
    ('shopper1@gmailer.com', 'Shopper', 'Smith'),
    ('shopper1@gmailer.com', 'Shopper', 'Smith'),
    ('shopper1@gmailer.com', 'Shopper', 'Smith'),
    ('shopper1@gmailer.com', 'Shopper', 'Smith');

SELECT * from Customers