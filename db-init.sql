CREATE DATABASE paste;
USE paste;

CREATE TABLE `record`
(
    `code`        char(6) NOT NULL,
    `text`        text,
    `create_time` bigint DEFAULT NULL,
    `expire_time` bigint DEFAULT NULL,
    PRIMARY KEY (`code`)
);