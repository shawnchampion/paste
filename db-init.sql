CREATE DATABASE paste;
USE paste;

CREATE TABLE `text_record`
(
    `code`        char(6) NOT NULL,
    `text`        text    NOT NULL,
    `create_time` bigint  NOT NULL,
    `expire_time` bigint  NOT NULL,
    PRIMARY KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `file_record`
(
    `code`        char(6)      NOT NULL,
    `filename`    varchar(100) NOT NULL,
    `size`        bigint       NOT NULL,
    `path`        varchar(200) NOT NULL,
    `create_time` bigint       NOT NULL,
    `expire_time` bigint       NOT NULL,
    PRIMARY KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;