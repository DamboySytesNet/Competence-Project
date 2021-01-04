insert into `competence-schema`.`profiles` values('student');
insert into `competence-schema`.`profiles` values('teacher');
insert into `competence-schema`.`profiles` values('stuff');

insert into `competence-schema`.`poi_types` values('outdoor');
insert into `competence-schema`.`poi_types` values('indoor');
insert into `competence-schema`.`poi_types` values('other');


insert into `competence-schema`.`experiments` values('ccb7764e-4e85-11eb-ae93-0242ac130002', NOW());


alter table `competence-schema`.`persons` add column `user_gender` VARCHAR(64) NOT NULL;
ALTER TABLE `competence-schema`.`persons` DROP FOREIGN KEY `persons_ibfk_1`;    -- tu nie jestem pewny czy zawsze bedzie ta sama nazwa
ALTER TABLE `competence-schema`.`persons` DROP INDEX `profile_name_UNIQUE`;
ALTER TABLE `competence-schema`.`persons` ADD FOREIGN KEY (profile_name) REFERENCES `competence-schema`.`profiles`(`name`);

CREATE TABLE `competence-schema`.`user_gender` (
    `name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`name`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC));

ALTER TABLE `competence-schema`.`persons` ADD FOREIGN KEY (user_gender) REFERENCES `competence-schema`.`user_gender`(`name`);

insert into `competence-schema`.`user_gender` values('male');
insert into `competence-schema`.`user_gender` values('female');
insert into `competence-schema`.`user_gender` values('helikopter_szturmowy');
alter table `competence-schema`.`persons` add column `user_age` INT NOT NULL;
