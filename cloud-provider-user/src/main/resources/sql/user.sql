USE `springboot`;
/*Table structure for table `user` */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pass_word` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_card` char(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone_number` char(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;