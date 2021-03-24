CREATE TABLE IF NOT EXISTS `cars` (
     `id`          INT NOT NULL auto_increment,
     `number`      VARCHAR(100),
     `description` VARCHAR(100),
     PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `contact_details` (
     `id`        INT NOT NULL auto_increment,
     `name`      VARCHAR(100) NOT NULL,
     `surname`   VARCHAR(100) NOT NULL,
     `id_number` VARCHAR(100) NOT NULL,
     `phone`     VARCHAR(100) NOT NULL,
     PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `bookings` (
     `id`                  INT NOT NULL auto_increment,
     `car_id`              INT NOT NULL,
     `contact_details_id`  INT NOT NULL,
     `start_time`          BIGINT UNSIGNED NOT NULL,
     `end_time`            BIGINT UNSIGNED NOT NULL,
     `deleted`             BOOLEAN NOT NULL DEFAULT false,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_booking_car` FOREIGN KEY(`car_id`) REFERENCES cars(`id`),
     CONSTRAINT `fk_booking_contact_details` FOREIGN KEY(`contact_details_id`) REFERENCES contact_details(`id`)
);
