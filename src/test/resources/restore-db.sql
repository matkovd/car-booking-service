DELETE FROM `bookings` where 1=1;
DELETE FROM `contact_details` where 1=1;
DELETE FROM `cars` where 1=1;
INSERT INTO `cars` (`id`, `number`, `description`) values (36, "123ABC", "description");
INSERT INTO `contact_details` (`id`, `name`, `surname`, `id_number`, `phone`) values (17, "Test", "Testvyi", "123", "71112223344");
