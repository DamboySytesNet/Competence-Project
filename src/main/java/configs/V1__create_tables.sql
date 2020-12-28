CREATE SCHEMA IF NOT EXISTS `competence-schema`;

CREATE TABLE `competence-schema`.`experiments` (
  `id` VARCHAR(36) NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `competence-schema`.`profiles` (
  `name` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE INDEX `id_UNIQUE` (`name` ASC));

CREATE TABLE `competence-schema`.`persons` (
  `id` VARCHAR(36) NOT NULL,
  `phone_number` VARCHAR(15) NOT NULL,
  `profile_name` VARCHAR(64) NOT NULL,
  `experiment_id` VARCHAR(36) NOT NULL,
  FOREIGN KEY (`profile_name`) REFERENCES `competence-schema`.`profiles`(`name`),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC),
  UNIQUE INDEX `profile_name_UNIQUE` (`profile_name` ASC),      -- do wyjebania, dodac gender
  FOREIGN KEY (`experiment_id`) REFERENCES `competence-schema`.`experiments`(`id`));

CREATE TABLE `competence-schema`.`poi_types` (
  `name` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

CREATE TABLE `competence-schema`.`poi` (
  `id` VARCHAR(36) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(128) NULL,
  `x` DOUBLE NOT NULL,
  `y` DOUBLE NOT NULL,
  `type` VARCHAR(16) NOT NULL,
  `experiment_id` VARCHAR(36) NOT NULL,
  FOREIGN KEY (`type`) REFERENCES `competence-schema`.`poi_types`(`name`),
  FOREIGN KEY (`experiment_id`) REFERENCES `competence-schema`.`experiments`(`id`),
  PRIMARY KEY (`id`));
