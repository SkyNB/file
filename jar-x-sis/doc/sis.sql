CREATE SCHEMA IF NOT EXISTS `sis`;
CREATE TABLE IF NOT EXISTS `sis`.`role` (
  `id`        bigint(11) NOT NULL AUTO_INCREMENT,
  `name`      varchar(16),
  `status`    int(5),
  `descr`     varchar(16),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `sis`.`resources` (
  `id`          bigint(11) NOT NULL AUTO_INCREMENT,
  `pid`         bigint(11) NOT NULL AUTO_INCREMENT,
  `name`        varchar(16),
  `val`         varchar(16),
  `icon`        varchar(16),
  `target`      varchar(16),
  `status`      int(5),
  `sort`        int(5),
  `descr`       varchar(16),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `sis`.`account` (
  `id`          bigint(11) NOT NULL AUTO_INCREMENT,
  `rid`         bigint(11),
  `account`     varchar(16),
  `pwd`         varchar(16),
  `nick`        varchar(16),
  `status`      int(5),
  `type`        int(5),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `sis`.`account_role` (
  `aid`         bigint(11) NOT NULL,
  `rid`         bigint(11) NOT NULL,
  KEY `aid` (`aid`),
  KEY `rid` (`rid`)
);




CREATE TABLE IF NOT EXISTS `sis`.`resources` (
  `id`          bigint(11) NOT NULL AUTO_INCREMENT,
  `pid`         bigint(11),
  `name`        varchar(16),
  `val`         varchar(16),
  `icon`        varchar(16),
  `descr`       varchar(120),
  `status`      int(5),
  `level`       int(5)
  `sort`        int(5),
  `typ`         int(5),
  `target`      int(5),
  KEY `id` (`id`)
);


CREATE TABLE IF NOT EXISTS `sis`.`account_role` (
  `role_id`     bigint(11) NOT NULL,
  `resource_id` bigint(11) NOT NULL,
  KEY `roleId` (`role_id`),
  KEY `resource_id` (`resource_id`)
);