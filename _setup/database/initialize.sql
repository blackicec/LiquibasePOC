CREATE DATABASE IF NOT EXISTS `LiquibaseBuiltMe`;

USE `LiquibaseBuiltMe`;

CREATE TABLE IF NOT EXISTS orders
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    firstName     VARCHAR(256)             NOT NULL,
    lastName      VARCHAR(256)             NOT NULL,
    email         VARCHAR(128)             NOT NULL,
    status        VARCHAR(32)              NULL,
    createDate    DATETIME DEFAULT (NOW()) NOT NULL
);

CREATE TABLE IF NOT EXISTS shirtSizes
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    size     VARCHAR(16)       NOT NULL,
    category VARCHAR(16)       NOT NULL,
    `order`  INT               NOT NULL,
    isActive TINYINT DEFAULT 1 NULL
);

CREATE TABLE IF NOT EXISTS _historicals
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    tableName       VARCHAR(128) NOT NULL,
    migrationClass  VARCHAR(128) NOT NULL,
    data            JSON NOT NULL,
    createDate      DATETIME DEFAULT (NOW()) NOT NULL
)