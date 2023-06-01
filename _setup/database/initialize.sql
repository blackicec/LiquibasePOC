CREATE DATABASE IF NOT EXISTS `ExampleDB`;

USE `ExampleDB`;

CREATE TABLE IF NOT EXISTS ReunionOrders
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    firstName     VARCHAR(256)             NOT NULL,
    lastName      VARCHAR(256)             NOT NULL,
    email         VARCHAR(128)             NOT NULL,
    squareOrderId VARCHAR(256)             NOT NULL,
    receiptUrl    VARCHAR(256)             NULL,
    status        VARCHAR(32)              NULL,
    createDate    DATETIME DEFAULT (NOW()) NOT NULL
);

CREATE TABLE IF NOT EXISTS ReunionShirtSizes
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    size     VARCHAR(16)       NOT NULL,
    category VARCHAR(16)       NOT NULL,
    `order`  INT               NOT NULL,
    isActive TINYINT DEFAULT 1 NULL
);