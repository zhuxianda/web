 CREATE TABLE `wx_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) NOT NULL,
  `state` int(11) NOT NULL,
  `createTime` bigint(11) NOT NULL,
  PRIMARY KEY (`id`,`openid`)
) ENGINE=InnoDB
